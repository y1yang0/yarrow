package com.kelthuzadx.yarrow.ir;

import com.kelthuzadx.yarrow.core.YarrowProperties.*;
import com.kelthuzadx.yarrow.util.Logger;
import jdk.vm.ci.hotspot.HotSpotResolvedJavaMethod;
import jdk.vm.ci.meta.ExceptionHandler;

import java.util.*;

import static com.kelthuzadx.yarrow.core.Bytecode.*;

class CFG {
    private HotSpotResolvedJavaMethod method;
    private int globalBlockId;
    private int globalLoopId;
    private int codeSize;
    private byte[] code;
    private ExceptionHandler[] xhandler;
    private Block[] bciToBlockMapping;
    private Block[] blocks;


    private CFG(HotSpotResolvedJavaMethod method){
        this.method = method;
        this.globalBlockId = 0;
        this.globalLoopId = 0;
        this.codeSize = method.getCodeSize();
        this.code = method.getCode();
        assert codeSize==code.length;
        this.xhandler = method.getExceptionHandlers();
        this.bciToBlockMapping = null;
        this.blocks = null;

    }

    static Block[] build(HotSpotResolvedJavaMethod method){
        CFG cfg = new CFG(method);
        cfg.mapBciToBlocks();
        if(Debug.PrintCFG){
            cfg.printBciToBlocks();
        }
        cfg.uniqueBlocks();
        if(Debug.PrintCFG){
            cfg.printAllBlock();
        }
        return cfg.blocks;
    }

    private void mapBciToBlocks(){
        this.bciToBlockMapping = new Block[this.codeSize];

        Block currentBlock = null;
        BytecodeStream stream = new BytecodeStream(code,codeSize);
        while(stream.hasNext()){
            int bci = stream.next();
            if(currentBlock == null){
                currentBlock = createBlockAt(bci);
            }else{
                if(bciToBlockMapping[bci]!=null){
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
                    bciToBlockMapping[bci].addSuccessor(createBlockAt(bci+stream.getBytecodeData()));
                    bciToBlockMapping[bci].addSuccessor(createBlockAt(stream.peekNextBci()));
                    break;
                }
                case GOTO:
                case GOTO_W: {
                    currentBlock = null;
                    bciToBlockMapping[bci].addSuccessor(createBlockAt(bci+stream.getBytecodeData()));
                    break;
                }
                case RET:
                case JSR:
                case JSR_W: {
                    Logger.error("Unimplement jsr/ret bytecode");
                }
                case TABLESWITCH:{
                    currentBlock = null;
                    BytecodeStream.TableSwitch ts = stream.getTableSwitch();
                    bciToBlockMapping[bci].addSuccessor(createBlockAt(bci+ts.getDefaultDest()));
                    for(int i=0;i<ts.getNumOfCase();i++){
                        bciToBlockMapping[bci].addSuccessor(createBlockAt(bci+ts.getKeyDest(i)));
                    }
                    break;
                }
                case LOOKUPSWITCH:{
                    currentBlock = null;
                    BytecodeStream.LookupSwitch ls = stream.getLookupSwitch();
                    bciToBlockMapping[bci].addSuccessor(createBlockAt(bci+ls.getDefaultDest()));
                    for(int i=0;i<ls.getNumOfCase();i++){
                        int x = bci+ls.getOffset(i);
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
                    if(!currentBlock.mayThrowEx()){
                        Block xh = handleException(bci);
                        if(xh!=null){
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

    private void uniqueBlocks(){
        Set<Block> bs = new HashSet<>();
        for (Block value : bciToBlockMapping) {
            if (value != null) {
                bs.add(value);
            }
        }
        this.blocks = bs.toArray(new Block[0]);
    }

    private void buildReducibleLoops(){

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
            formerBlock.setEndBci(bci-1);
            for (Block formerBlockSuccessor : formerBlock.getSuccessor()) {
                newBlock.addSuccessor(formerBlockSuccessor);
            }
            formerBlock.removeSuccessor();
            formerBlock.addSuccessor(newBlock);
            // Remap bci from former block to new block
            for(int i=newBlock.getStartBci();i<=newBlock.getEndBci();i++){
                bciToBlockMapping[i] = newBlock;
            }
            return newBlock;
        } else {
            return formerBlock;
        }
    }

    private Block handleException(int bci){
        Block lastXHandler = null;
        for(ExceptionHandler xh : xhandler){
            if(xh.getStartBCI()<=bci && bci<xh.getEndBCI()){
                Block xBlock = createBlockAt(xh.getHandlerBCI());
                xBlock.setXhandler(xh);
                bciToBlockMapping[xh.getHandlerBCI()] = xBlock;
                if(xh.isCatchAll()){
                    return xBlock;
                }else{
                    lastXHandler = xBlock;
                }
            }
        }
        return lastXHandler;
    }

    private void printBciToBlocks(){
        Logger.logf("Bci to block mapping for method {}.{}()====>",
                method.getDeclaringClass().getUnqualifiedName(),
                method.getName());
        for(int i = 0; i< bciToBlockMapping.length; i++){
            Logger.logf("{} : {}",i, bciToBlockMapping[i]!=null? bciToBlockMapping[i].toString():"[]");
        }
    }
    private void printAllBlock(){
        Logger.log("All blocks====>");
        for(Block block:blocks){
            Logger.logf("{}",block.toString());
        }
    }
}

