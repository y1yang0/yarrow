package com.kelthuzadx.yarrow.ir;

import com.kelthuzadx.yarrow.bytecode.BytecodeStream;
import com.kelthuzadx.yarrow.core.YarrowError;
import com.kelthuzadx.yarrow.core.YarrowProperties.Debug;
import com.kelthuzadx.yarrow.util.Logger;
import jdk.vm.ci.hotspot.HotSpotResolvedJavaMethod;
import jdk.vm.ci.meta.ExceptionHandler;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static com.kelthuzadx.yarrow.bytecode.Bytecode.*;

class CFG {
    private HotSpotResolvedJavaMethod method;
    private int globalBlockId;
    private int codeSize;
    private byte[] code;
    private ExceptionHandler[] xhandler;
    private Block[] bciToBlockMapping;
    private Block[] blocks;
    private HashMap<Integer, Integer> loopMap;
    private int nextLoopIndex;


    private CFG(HotSpotResolvedJavaMethod method) {
        this.method = method;
        this.globalBlockId = 0;
        this.codeSize = method.getCodeSize();
        this.code = method.getCode();
        assert codeSize == code.length;
        this.xhandler = method.getExceptionHandlers();
        this.bciToBlockMapping = null;
        this.blocks = null;
        this.loopMap = new HashMap<>();
        this.nextLoopIndex = 0;

    }

    static CFG build(HotSpotResolvedJavaMethod method) {
        CFG cfg = new CFG(method);
        cfg.mapBciToBlocks();
        cfg.uniqueBlocks();
        HashSet<Integer> visit = new HashSet<>(cfg.blocks.length);
        HashSet<Integer> active = new HashSet<>(cfg.blocks.length);
        cfg.identifyLoop(visit, active, cfg.bciToBlockMapping[0]);
        if (Debug.PrintCFG) {
            cfg.printBciToBlocks();
            cfg.printAllBlockRange();
            cfg.printAllBlock();
        }
        return cfg;
    }

    private void mapBciToBlocks() {
        this.bciToBlockMapping = new Block[this.codeSize];

        Block currentBlock = null;
        BytecodeStream stream = new BytecodeStream(code, codeSize);
        while (stream.hasNext()) {
            int bci = stream.next();
            if (currentBlock == null) {
                currentBlock = createBlockAt(bci);
            } else {
                if (bciToBlockMapping[bci] != null) {
                    bciToBlockMapping[currentBlock.getEndBci()].addSuccessor(bciToBlockMapping[bci]);
                    currentBlock = bciToBlockMapping[bci];
                }
            }
            currentBlock.setEndBci(bci);
            bciToBlockMapping[bci] = currentBlock;
            switch (stream.currentBytecode()) {
                case IFEQ:
                case IFNE:
                case IFLT:
                case IFGE:
                case IFGT:
                case IFLE:
                case IF_ICMPEQ:
                case IF_ICMPNE:
                case IF_ICMPLT:
                case IF_ICMPGE:
                case IF_ICMPGT:
                case IF_ICMPLE:
                case IF_ACMPEQ:
                case IF_ACMPNE:
                case IFNULL:
                case IFNONNULL: {
                    // Set current block as empty so that next bci would create
                    // new basic block
                    currentBlock = null;
                    bciToBlockMapping[bci].addSuccessor(createBlockAt(bci + stream.getBytecodeData()));
                    bciToBlockMapping[bci].addSuccessor(createBlockAt(stream.peekNextBci()));
                    break;
                }
                case GOTO:
                case GOTO_W: {
                    currentBlock = null;
                    bciToBlockMapping[bci].addSuccessor(createBlockAt(bci + stream.getBytecodeData()));
                    break;
                }
                case RET:
                case JSR:
                case JSR_W: {
                    Logger.error("Unimplement jsr/ret bytecode");
                }
                case TABLESWITCH: {
                    currentBlock = null;
                    BytecodeStream.TableSwitch ts = stream.getTableSwitch();
                    bciToBlockMapping[bci].addSuccessor(createBlockAt(bci + ts.getDefaultDest()));
                    for (int i = 0; i < ts.getNumOfCase(); i++) {
                        bciToBlockMapping[bci].addSuccessor(createBlockAt(bci + ts.getKeyDest(i)));
                    }
                    break;
                }
                case LOOKUPSWITCH: {
                    currentBlock = null;
                    BytecodeStream.LookupSwitch ls = stream.getLookupSwitch();
                    bciToBlockMapping[bci].addSuccessor(createBlockAt(bci + ls.getDefaultDest()));
                    for (int i = 0; i < ls.getNumOfCase(); i++) {
                        int x = bci + ls.getOffset(i);
                        bciToBlockMapping[bci].addSuccessor(createBlockAt(x));
                    }
                    break;
                }
                case IRETURN:
                case LRETURN:
                case FRETURN:
                case DRETURN:
                case ARETURN:
                case RETURN: {
                    currentBlock = null;
                    break;
                }
                default: {
                    if (!currentBlock.mayThrowEx()) {
                        Block xh = handleException(bci);
                        if (xh != null) {
                            currentBlock = null;
                            bciToBlockMapping[bci].addSuccessor(xh);
                            bciToBlockMapping[bci].setMayThrowEx(true);
                            bciToBlockMapping[bci].addSuccessor(createBlockAt(stream.peekNextBci()));
                        }
                    }
                    break;
                }
            }
        }
    }

    private void uniqueBlocks() {
        Set<Block> bs = new HashSet<>();
        for (Block value : bciToBlockMapping) {
            if (value != null) {
                bs.add(value);
            }
        }
        this.blocks = bs.toArray(new Block[0]);
    }

    private Block createBlockAt(int bci) {
        Block formerBlock = bciToBlockMapping[bci];
        if (formerBlock == null) {
            bciToBlockMapping[bci] = new Block(globalBlockId++, bci);
            return bciToBlockMapping[bci];
        }

        if (bci != formerBlock.getStartBci()) {
            // Create new block after splitting former block
            Block newBlock = new Block(globalBlockId++, bci);
            newBlock.setStartBci(bci);
            newBlock.setEndBci(formerBlock.getEndBci());
            formerBlock.setEndBci(bci - 1);
            for (Block formerBlockSuccessor : formerBlock.getSuccessor()) {
                newBlock.addSuccessor(formerBlockSuccessor);
            }
            formerBlock.removeSuccessor();
            formerBlock.addSuccessor(newBlock);
            // Remap bci from former block to new block
            for (int i = newBlock.getStartBci(); i <= newBlock.getEndBci(); i++) {
                bciToBlockMapping[i] = newBlock;
            }
            return newBlock;
        } else {
            return formerBlock;
        }
    }

    private Block handleException(int bci) {
        Block lastXHandler = null;
        for (ExceptionHandler xh : xhandler) {
            if (xh.getStartBCI() <= bci && bci < xh.getEndBCI()) {
                Block xBlock = createBlockAt(xh.getHandlerBCI());
                xBlock.setXhandler(xh);
                bciToBlockMapping[xh.getHandlerBCI()] = xBlock;
                if (xh.isCatchAll()) {
                    return xBlock;
                } else {
                    lastXHandler = xBlock;
                }
            }
        }
        return lastXHandler;
    }

    private int identifyLoop(Set<Integer> visit, Set<Integer> active, Block block) {
        int id = block.getId();

        if (visit.contains(id)) {
            if (active.contains(id)) {
                loopMap.put(id, 1 << nextLoopIndex);
                nextLoopIndex++;
                block.setLoopHeader(true);
            }
            return loopMap.get(id);
        }

        visit.add(id);
        active.add(id);
        int loopState = 0;
        for (Block sux : block.getSuccessor()) {
            loopState |= identifyLoop(visit, active, sux);
        }
        if (!active.remove(id)) {
            throw new YarrowError("Active set is corrupted");
        }

        if (block.isLoopHeader()) {
            loopState &= ~loopMap.get(id); // clear loop header block bit so that
        }

        loopMap.put(id, loopState);
        return loopState;
    }

    private boolean isLoopBlock(int blockId) {
        return loopMap.get(blockId) != 0;
    }

    private void printBciToBlocks() {
        Logger.logf("=====Bci to block mapping({}.{})=====>",
                method.getDeclaringClass().getUnqualifiedName(),
                method.getName());
        for (int i = 0; i < bciToBlockMapping.length; i++) {
            Logger.logf("{} : {}", i, bciToBlockMapping[i] != null ? bciToBlockMapping[i].toString() : "[]");
        }
    }

    private void printAllBlockRange() {
        Logger.logf("{}", "=====All block ranges=====>");
        for (Block block : blocks) {
            Logger.logf("{}", block.toString());
        }
    }

    private void printAllBlock() {
        Logger.logf("{}", "=====All blocks=====>");
        for (Block block : blocks) {
            String flag = block.isLoopHeader() ? "[LH]" : "";
            flag += isLoopBlock(block.getId()) ? "[L]" : "";
            Logger.logf("#{} {}{", block.getId(), flag);
            BytecodeStream bs = new BytecodeStream(code, codeSize);
            bs.reset(block.getStartBci());
            while (bs.hasNext()) {
                int bci = bs.next();
                Logger.logf(" {}", bs.getCurrentBytecodeString());
                if (bci == block.getEndBci()) {
                    break;
                }
            }
            Logger.logf("} => {}", block.getSuccessor().stream().map(
                    b -> "#" + b.getId()
            ).collect(Collectors.toList()));
        }
    }
}

