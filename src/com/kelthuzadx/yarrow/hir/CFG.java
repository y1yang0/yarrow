package com.kelthuzadx.yarrow.hir;

import com.kelthuzadx.yarrow.bytecode.Bytecode;
import com.kelthuzadx.yarrow.bytecode.BytecodeStream;
import com.kelthuzadx.yarrow.core.YarrowError;
import com.kelthuzadx.yarrow.hir.instr.BlockStartInstr;
import com.kelthuzadx.yarrow.util.Logger;
import com.kelthuzadx.yarrow.util.Mode;
import jdk.vm.ci.hotspot.HotSpotResolvedJavaMethod;

import java.util.*;
import java.util.stream.Collectors;

import static com.kelthuzadx.yarrow.bytecode.Bytecode.*;
import static com.kelthuzadx.yarrow.core.YarrowProperties.Debug.PrintCFG;
import static com.kelthuzadx.yarrow.core.YarrowProperties.Debug.PrintIRToFile;

class CFG {
    private final HotSpotResolvedJavaMethod method;
    private int globalBlockId;
    private int codeSize;
    private byte[] code;
    private ExHandler[] exHandler;
    private BlockStartInstr[] bciToBlockMapping;
    private BlockStartInstr[] blocks;
    private HashMap<Integer, Integer> loopMap;
    private int nextLoopIndex;


    public CFG(HotSpotResolvedJavaMethod method) {
        this.method = method;
        this.globalBlockId = 0;
        this.codeSize = method.getCodeSize();
        this.code = method.getCode();
        this.exHandler = new ExHandler[method.getExceptionHandlers().length];
        this.bciToBlockMapping = null;
        this.blocks = null;
        this.loopMap = new HashMap<>();
        this.nextLoopIndex = 0;

    }

    public void build() {
        mapBciToBlocks();
        uniqueBlocks();
        identifyLoop(new HashSet<>(blocks.length), new HashSet<>(blocks.length), bciToBlockMapping[0]);
        if (PrintCFG) {
            printBciToBlocks();
            printAllBlockRange();
            printAllBlock();
        }
        if (PrintIRToFile) {
            printCFGToDotFile();
            printCFGDetailToDotFile();
        }
    }

    public BlockStartInstr blockContain(int bci) {
        if (bci < 0 || bci >= bciToBlockMapping.length) {
            throw new YarrowError("invalid bytecode index " + bci);
        }
        return bciToBlockMapping[bci];
    }

    public BlockStartInstr[] getBlocks() {
        return blocks;
    }

    private void mapBciToBlocks() {
        this.bciToBlockMapping = new BlockStartInstr[this.codeSize];
        createExceptionBlock();

        BlockStartInstr currentBlock = null;
        BytecodeStream stream = new BytecodeStream(code);
        while (stream.hasNext()) {
            int bci = stream.next();
            if (currentBlock == null) {
                currentBlock = createBlockAt(bci);
            } else {
                if (bciToBlockMapping[bci] != null) {
                    if (!bciToBlockMapping[currentBlock.getEndBci()].hasSuccessor(bciToBlockMapping[bci])) {
                        bciToBlockMapping[currentBlock.getEndBci()].addSuccessor(bciToBlockMapping[bci]);
                    }
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
                    YarrowError.unimplemented("ret/jsr considers deprecated and thus not supported");
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
                    if (Bytecode.canTrap(bci)) {
                        for (ExHandler handler : exHandler) {
                            if (handler.tryCover(bci)) {
                                BlockStartInstr catchBlock = handler.getCatchEntry();
                                if (!currentBlock.hasSuccessor(catchBlock)) {
                                    currentBlock.addSuccessor(catchBlock);
                                }
                                if (handler.isCatchAll()) {
                                    break;
                                }
                            }
                        }
                    }
                    break;
                }
            }
        }
    }

    private void uniqueBlocks() {
        Set<BlockStartInstr> bs = new HashSet<>();
        for (BlockStartInstr value : bciToBlockMapping) {
            if (value != null) {
                bs.add(value);
            }
        }
        this.blocks = bs.toArray(new BlockStartInstr[0]);

        Arrays.sort(blocks, Comparator.comparingInt(BlockStartInstr::getBlockId));
    }

    private void createExceptionBlock() {
        for (int i = 0; i < method.getExceptionHandlers().length; i++) {
            var handler = method.getExceptionHandlers()[i];
            BlockStartInstr exBlock = createBlockAt(handler.getHandlerBCI());
            exBlock.setFlag(BlockFlag.CatchEntry);
            exHandler[i] = new ExHandler(handler, exBlock);
        }
    }

    private BlockStartInstr createBlockAt(int bci) {
        BlockStartInstr formerBlock = bciToBlockMapping[bci];
        if (formerBlock == null) {
            bciToBlockMapping[bci] = new BlockStartInstr(globalBlockId++, bci);
            return bciToBlockMapping[bci];
        }

        if (bci != formerBlock.getStartBci()) {
            // Create new block after splitting former block
            BlockStartInstr newBlock = new BlockStartInstr(globalBlockId++, bci);
            newBlock.setStartBci(bci);
            newBlock.setEndBci(formerBlock.getEndBci());
            formerBlock.setEndBci(bci - 1);
            for (BlockStartInstr formerBlockSuccessor : formerBlock.getSuccessor()) {
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

    private int identifyLoop(Set<Integer> visit, Set<Integer> active, BlockStartInstr blockStart) {
        int id = blockStart.getBlockId();

        if (visit.contains(id)) {
            if (active.contains(id)) {
                loopMap.put(id, 1 << nextLoopIndex);
                nextLoopIndex++;
                blockStart.setLoopHeader(true);
            }
            return loopMap.get(id);
        }

        visit.add(id);
        active.add(id);
        int loopState = 0;
        for (BlockStartInstr sux : blockStart.getSuccessor()) {
            loopState |= identifyLoop(visit, active, sux);
        }
        if (!active.remove(id)) {
            throw new YarrowError("Active set is corrupted");
        }

        if (blockStart.isLoopHeader()) {
            loopState &= ~loopMap.get(id); // clear loop header block bit so that
        }

        loopMap.put(id, loopState);
        return loopState;
    }

    private boolean isLoopBlock(int blockId) {
        return loopMap.get(blockId) != null && loopMap.get(blockId) != 0;
    }

    private void printBciToBlocks() {
        Logger.logf("=====Mapping bci to block=====>");
        for (int i = 0; i < bciToBlockMapping.length; i++) {
            Logger.logf("{} : {}", i, bciToBlockMapping[i] != null ? bciToBlockMapping[i].toCFGString() : "[]");
        }
    }

    private void printAllBlockRange() {
        Logger.logf("{}", "=====All block ranges=====>");
        for (BlockStartInstr block : blocks) {
            Logger.logf("{}", block.toCFGString());
        }
    }

    private void printAllBlock() {
        Logger.logf("{}", "=====Phase1: Control Flow Graph=====>");
        for (BlockStartInstr block : blocks) {
            String flag = block.isLoopHeader() ? "[LH]" : "";
            flag += isLoopBlock(block.getBlockId()) ? "[L]" : "";
            Logger.logf("#{} {}{", block.getBlockId(), flag);
            BytecodeStream bs = new BytecodeStream(code, block.getStartBci());
            while (bs.hasNext()) {
                int bci = bs.next();
                Logger.logf(" {}", bs.getCurrentBytecodeString());
                if (bci == block.getEndBci()) {
                    break;
                }
            }
            Logger.logf("} => {}", block.getSuccessor().stream().map(
                    b -> "#" + b.getBlockId()
            ).collect(Collectors.toList()));
        }
    }

    private void printCFGToDotFile() {
        StringBuilder content = new StringBuilder();
        content.append("digraph G{\n");
        for (BlockStartInstr block : blocks) {
            if (!block.getSuccessor().isEmpty()) {
                for (BlockStartInstr succ : block.getSuccessor()) {
                    content.append("\tB").append(block.getBlockId()).append("-> B").append(succ.getBlockId()).append(";\n");
                }
            }
        }
        for (BlockStartInstr block : blocks) {
            content.append("\tB").append(block.getBlockId()).append("[shape=box];\n");
        }
        content.append("}");

        Logger.log(Mode.File, method.getName()+"_phase1_pure.dot", content.toString());
    }

    private void printCFGDetailToDotFile() {
        StringBuilder content = new StringBuilder();
        content.append("digraph G{\n");
        for (BlockStartInstr block : blocks) {
            if (!block.getSuccessor().isEmpty()) {
                for (BlockStartInstr succ : block.getSuccessor()) {
                    content.append("\tB").append(block.getBlockId()).append("-> B").append(succ.getBlockId()).append(";\n");
                }
            }
        }
        for (BlockStartInstr block : blocks) {
            content.append("\tB").append(block.getBlockId()).append("[shape=record,label=\"");
            content.append("{ B").append(block.getBlockId()).append(" | ");
            BytecodeStream bs = new BytecodeStream(code, block.getStartBci());
            while (bs.hasNext()) {
                int bci = bs.next();
                content.append(bs.getCurrentBytecodeString()).append("\\l");
                if (bci == block.getEndBci()) {
                    break;
                }
            }
            content.append(" }\"];\n");
        }
        content.append("}");

        Logger.log(Mode.File, method.getName()+"_phase1.dot", content.toString());
    }
}

