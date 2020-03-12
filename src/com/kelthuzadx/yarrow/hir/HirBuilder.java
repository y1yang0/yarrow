package com.kelthuzadx.yarrow.hir;

import com.kelthuzadx.yarrow.bytecode.Bytecode;
import com.kelthuzadx.yarrow.bytecode.BytecodeStream;
import com.kelthuzadx.yarrow.hir.instr.*;
import com.kelthuzadx.yarrow.util.CompilerErrors;
import com.kelthuzadx.yarrow.util.Converter;
import com.kelthuzadx.yarrow.util.Logger;
import jdk.vm.ci.hotspot.HotSpotResolvedJavaField;
import jdk.vm.ci.hotspot.HotSpotResolvedJavaMethod;
import jdk.vm.ci.meta.*;

import java.io.PrintWriter;
import java.util.*;

import static com.kelthuzadx.yarrow.core.YarrowProperties.Debug.PrintSSA;

/**
 * HirBuilder performs an abstract interpretation, it would transform java bytecode to compiler HIR.
 */
public class HirBuilder {
    private HotSpotResolvedJavaMethod method;

    private CFG cfg;

    private Instruction lastInstr;

    private Queue<BlockStartInstr> workList;

    public HirBuilder(HotSpotResolvedJavaMethod method) {
        this.method = method;
        this.lastInstr = null;
    }

    public HirBuilder build() {
        // Construct cfg and detect reducible loops
        cfg = new CFG(method);
        cfg.build();

        // Abstract interpretation
        Set<Integer> visit = new HashSet<>(cfg.getBlocks().length);

        workList = new ArrayDeque<>();
        BlockStartInstr methodEntryBlock = cfg.blockContain(0);
        methodEntryBlock.merge(createEntryVmState());
        workList.add(methodEntryBlock);

        while (!workList.isEmpty()){
            BlockStartInstr blockStart = workList.remove();
            if(!visit.contains(blockStart.getBlockId())){
                visit.add(blockStart.getBlockId());
                if(lastInstr == null){
                    lastInstr = methodEntryBlock;
                }else{
                    lastInstr.setNext(blockStart);
                    lastInstr = blockStart;
                }
                fulfillBlock(blockStart);
            }
        }

        if(PrintSSA){
            Logger.logf("=====SSA Form=====>");
            BlockStartInstr methodEntry = cfg.blockContain(0);
            printSSA(new HashSet<>(),methodEntry);
        }

        return this;
    }

    private VmState createEntryVmState(){
        VmState state = new VmState(method.getMaxStackSize(),method.getMaxLocals());
        int paramIndex = 0;

        if(method.hasReceiver()){
            ParamInstr receiverInstr = new ParamInstr(new Value(JavaKind.Object),method,false,paramIndex);
            state.set(paramIndex,receiverInstr);
            paramIndex++;
        }

        Signature sig = method.getSignature();
        for(int i=0;i<sig.getParameterCount(false/*Receiver already processed*/);i++){
            ParamInstr pi = new ParamInstr(new Value(sig.getParameterKind(i)),method,false,paramIndex);
            state.set(paramIndex,pi);
            paramIndex++;
        }
        return state;

    }

    private void printSSA(Set<BlockStartInstr> visit,BlockStartInstr block){
        if(block==null || visit.contains(block)){
            return;
        }

        block.iterateBytecode(instr-> {
            Logger.logf("{}",instr.toString());
        });
        visit.add(block);
        for(BlockStartInstr succ:block.getSuccessor()){
            printSSA(visit,succ);
        }
    }


    private void fulfillBlock(BlockStartInstr block){
        VmState state = block.getVmState();

        BytecodeStream bs =new BytecodeStream(method.getCode(),block.getStartBci(),block.getEndBci());
        while (bs.hasNext()){
            int curBci = bs.next();
            int opcode = bs.currentBytecode();
            switch (opcode) {
                case Bytecode.NOP: break;
                case Bytecode.ACONST_NULL:loadConst(state,JavaKind.Object,null);break;
                case Bytecode.ICONST_M1:loadConst(state,JavaKind.Int,-1);break;
                case Bytecode.ICONST_0:loadConst(state,JavaKind.Int,0);break;
                case Bytecode.ICONST_1:loadConst(state,JavaKind.Int,1);break;
                case Bytecode.ICONST_2:loadConst(state,JavaKind.Int,2);break;
                case Bytecode.ICONST_3:loadConst(state,JavaKind.Int,3);break;
                case Bytecode.ICONST_4:loadConst(state,JavaKind.Int,4);break;
                case Bytecode.ICONST_5:loadConst(state,JavaKind.Int,5);break;
                case Bytecode.LCONST_0:loadConst(state,JavaKind.Long,0L);break;
                case Bytecode.LCONST_1:loadConst(state,JavaKind.Long,1L);break;
                case Bytecode.FCONST_0:loadConst(state,JavaKind.Float,0.0f);break;
                case Bytecode.FCONST_1:loadConst(state,JavaKind.Float,1.0f);break;
                case Bytecode.FCONST_2:loadConst(state,JavaKind.Float,2.0f);break;
                case Bytecode.DCONST_0:loadConst(state,JavaKind.Double,0.0d);break;
                case Bytecode.DCONST_1:loadConst(state,JavaKind.Double,1.0d);break;
                case Bytecode.BIPUSH:loadConst(state,JavaKind.Int,(byte)bs.getBytecodeData());break;
                case Bytecode.SIPUSH:loadConst(state,JavaKind.Int,(short)bs.getBytecodeData());break;
                case Bytecode.LDC:
                case Bytecode.LDC_W:
                case Bytecode.LDC2_W:ldc(state,bs.getBytecodeData());break;
                case Bytecode.ILOAD:load(state,JavaKind.Int,bs.getBytecodeData());break;
                case Bytecode.LLOAD:load(state,JavaKind.Long,bs.getBytecodeData());break;
                case Bytecode.FLOAD:load(state,JavaKind.Float,bs.getBytecodeData());break;
                case Bytecode.DLOAD:load(state,JavaKind.Double,bs.getBytecodeData());break;
                case Bytecode.ALOAD:load(state,JavaKind.Object,bs.getBytecodeData());break;
                case Bytecode.ILOAD_0:load(state,JavaKind.Int,0);break;
                case Bytecode.ILOAD_1:load(state,JavaKind.Int,1);break;
                case Bytecode.ILOAD_2:load(state,JavaKind.Int,2);break;
                case Bytecode.ILOAD_3:load(state,JavaKind.Int,3);break;
                case Bytecode.LLOAD_0:load(state,JavaKind.Long,0);break;
                case Bytecode.LLOAD_1:load(state,JavaKind.Long,1);break;
                case Bytecode.LLOAD_2:load(state,JavaKind.Long,2);break;
                case Bytecode.LLOAD_3:load(state,JavaKind.Long,3);break;
                case Bytecode.FLOAD_0:load(state,JavaKind.Float,0);break;
                case Bytecode.FLOAD_1:load(state,JavaKind.Float,1);break;
                case Bytecode.FLOAD_2:load(state,JavaKind.Float,2);break;
                case Bytecode.FLOAD_3:load(state,JavaKind.Float,3);break;
                case Bytecode.DLOAD_0:load(state,JavaKind.Double,0);break;
                case Bytecode.DLOAD_1:load(state,JavaKind.Double,1);break;
                case Bytecode.DLOAD_2:load(state,JavaKind.Double,2);break;
                case Bytecode.DLOAD_3:load(state,JavaKind.Double,3);break;
                case Bytecode.ALOAD_0:load(state,JavaKind.Object,0);break;
                case Bytecode.ALOAD_1:load(state,JavaKind.Object,1);break;
                case Bytecode.ALOAD_2:load(state,JavaKind.Object,2);break;
                case Bytecode.ALOAD_3:load(state,JavaKind.Object,3);break;
                case Bytecode.IALOAD:loadArray(state,JavaKind.Int);break;
                case Bytecode.LALOAD:loadArray(state,JavaKind.Long);break;
                case Bytecode.FALOAD:loadArray(state,JavaKind.Float);break;
                case Bytecode.DALOAD:loadArray(state,JavaKind.Double);break;
                case Bytecode.AALOAD:loadArray(state,JavaKind.Object);break;
                case Bytecode.BALOAD:loadArray(state,JavaKind.Byte);break;
                case Bytecode.CALOAD:loadArray(state,JavaKind.Char);break;
                case Bytecode.SALOAD:loadArray(state,JavaKind.Short);break;
                case Bytecode.ISTORE:store(state,JavaKind.Int,bs.getBytecodeData());break;
                case Bytecode.LSTORE:store(state,JavaKind.Long,bs.getBytecodeData());break;
                case Bytecode.FSTORE:store(state,JavaKind.Float,bs.getBytecodeData());break;
                case Bytecode.DSTORE:store(state,JavaKind.Double,bs.getBytecodeData());break;
                case Bytecode.ASTORE:store(state,JavaKind.Object,bs.getBytecodeData());break;
                case Bytecode.ISTORE_0:store(state,JavaKind.Int,0);break;
                case Bytecode.ISTORE_1:store(state,JavaKind.Int,1);break;
                case Bytecode.ISTORE_2:store(state,JavaKind.Int,2);break;
                case Bytecode.ISTORE_3:store(state,JavaKind.Int,3);break;
                case Bytecode.LSTORE_0:store(state,JavaKind.Long,0);break;
                case Bytecode.LSTORE_1:store(state,JavaKind.Long,1);break;
                case Bytecode.LSTORE_2:store(state,JavaKind.Long,2);break;
                case Bytecode.LSTORE_3:store(state,JavaKind.Long,3);break;
                case Bytecode.FSTORE_0:store(state,JavaKind.Float,0);break;
                case Bytecode.FSTORE_1:store(state,JavaKind.Float,1);break;
                case Bytecode.FSTORE_2:store(state,JavaKind.Float,2);break;
                case Bytecode.FSTORE_3:store(state,JavaKind.Float,3);break;
                case Bytecode.DSTORE_0:store(state,JavaKind.Double,0);break;
                case Bytecode.DSTORE_1:store(state,JavaKind.Double,1);break;
                case Bytecode.DSTORE_2:store(state,JavaKind.Double,2);break;
                case Bytecode.DSTORE_3:store(state,JavaKind.Double,3);break;
                case Bytecode.ASTORE_0:store(state,JavaKind.Object,0);break;
                case Bytecode.ASTORE_1:store(state,JavaKind.Object,1);break;
                case Bytecode.ASTORE_2:store(state,JavaKind.Object,2);break;
                case Bytecode.ASTORE_3:store(state,JavaKind.Object,3);break;
                case Bytecode.IASTORE:storeArray(state,JavaKind.Int);break;
                case Bytecode.LASTORE:storeArray(state,JavaKind.Long);break;
                case Bytecode.FASTORE:storeArray(state,JavaKind.Float);break;
                case Bytecode.DASTORE:storeArray(state,JavaKind.Double);break;
                case Bytecode.AASTORE:storeArray(state,JavaKind.Object);break;
                case Bytecode.BASTORE:storeArray(state,JavaKind.Byte);break;
                case Bytecode.CASTORE:storeArray(state,JavaKind.Char);break;
                case Bytecode.SASTORE:storeArray(state,JavaKind.Short);break;
                case Bytecode.POP:state.pop();break;
                case Bytecode.POP2:state.pop();state.pop();break;
                case Bytecode.DUP:
                case Bytecode.DUP_X1:
                case Bytecode.DUP_X2:
                case Bytecode.DUP2:
                case Bytecode.DUP2_X1:
                case Bytecode.DUP2_X2:duplicate(state,opcode);break;
                case Bytecode.SWAP:swap(state);break;
                case Bytecode.IADD:
                case Bytecode.ISUB:
                case Bytecode.IMUL:
                case Bytecode.IDIV:
                case Bytecode.IREM:arithmetic(state,JavaKind.Int,opcode);break;
                case Bytecode.LADD:
                case Bytecode.LSUB:
                case Bytecode.LMUL:
                case Bytecode.LDIV:
                case Bytecode.LREM:arithmetic(state,JavaKind.Long,opcode);break;
                case Bytecode.FADD:
                case Bytecode.FSUB:
                case Bytecode.FMUL:
                case Bytecode.FDIV:
                case Bytecode.FREM:arithmetic(state,JavaKind.Float,opcode);break;
                case Bytecode.DADD:
                case Bytecode.DSUB:
                case Bytecode.DMUL:
                case Bytecode.DDIV:
                case Bytecode.DREM:arithmetic(state,JavaKind.Double,opcode);break;
                case Bytecode.INEG:negate(state,JavaKind.Int);break;
                case Bytecode.LNEG:negate(state,JavaKind.Long);break;
                case Bytecode.FNEG:negate(state,JavaKind.Float);break;
                case Bytecode.DNEG:negate(state,JavaKind.Double);break;
                case Bytecode.ISHL:
                case Bytecode.ISHR:
                case Bytecode.IUSHR: shift(state,JavaKind.Int,opcode);break;
                case Bytecode.LSHL:
                case Bytecode.LSHR:
                case Bytecode.LUSHR: shift(state,JavaKind.Long,opcode);break;
                case Bytecode.IAND:
                case Bytecode.IOR:
                case Bytecode.IXOR:logic(state,JavaKind.Int,opcode);break;
                case Bytecode.LAND:
                case Bytecode.LOR:
                case Bytecode.LXOR:logic(state,JavaKind.Long,opcode);break;
                case Bytecode.IINC:increment(state,bs.getIINC());break;
                case Bytecode.I2L:typeCast(state,JavaKind.Int,JavaKind.Long,opcode);break;
                case Bytecode.I2F:typeCast(state,JavaKind.Int,JavaKind.Float,opcode);break;
                case Bytecode.I2D:typeCast(state,JavaKind.Int,JavaKind.Double,opcode);break;
                case Bytecode.L2I:typeCast(state,JavaKind.Long,JavaKind.Int,opcode);break;
                case Bytecode.L2F:typeCast(state,JavaKind.Long,JavaKind.Float,opcode);break;
                case Bytecode.L2D:typeCast(state,JavaKind.Long,JavaKind.Double,opcode);break;
                case Bytecode.F2I:typeCast(state,JavaKind.Float,JavaKind.Int,opcode);break;
                case Bytecode.F2L:typeCast(state,JavaKind.Float,JavaKind.Long,opcode);break;
                case Bytecode.F2D:typeCast(state,JavaKind.Float,JavaKind.Double,opcode);break;
                case Bytecode.D2I:typeCast(state,JavaKind.Double,JavaKind.Int,opcode);break;
                case Bytecode.D2L:typeCast(state,JavaKind.Double,JavaKind.Long,opcode);break;
                case Bytecode.D2F:typeCast(state,JavaKind.Double,JavaKind.Float,opcode);break;
                case Bytecode.I2B:typeCast(state,JavaKind.Int,JavaKind.Byte,opcode);break;
                case Bytecode.I2C:typeCast(state,JavaKind.Int,JavaKind.Char,opcode);break;
                case Bytecode.I2S:typeCast(state,JavaKind.Int,JavaKind.Short,opcode);break;
                case Bytecode.LCMP:compare(state,JavaKind.Long,opcode);break;
                case Bytecode.FCMPL:
                case Bytecode.FCMPG:compare(state,JavaKind.Float,opcode);break;
                case Bytecode.DCMPL:
                case Bytecode.DCMPG:compare(state,JavaKind.Double,opcode);break;
                case Bytecode.IFEQ:branchIfZero(state,JavaKind.Int,Cond.EQ,curBci+bs.getBytecodeData(),bs.peekNextBci());break;
                case Bytecode.IFNE:branchIfZero(state,JavaKind.Int,Cond.NE,curBci+bs.getBytecodeData(),bs.peekNextBci());break;
                case Bytecode.IFLT:branchIfZero(state,JavaKind.Int,Cond.LT,curBci+bs.getBytecodeData(),bs.peekNextBci());break;
                case Bytecode.IFGE:branchIfZero(state,JavaKind.Int,Cond.GE,curBci+bs.getBytecodeData(),bs.peekNextBci());break;
                case Bytecode.IFGT:branchIfZero(state,JavaKind.Int,Cond.GT,curBci+bs.getBytecodeData(),bs.peekNextBci());break;
                case Bytecode.IFLE:branchIfZero(state,JavaKind.Int,Cond.LE,curBci+bs.getBytecodeData(),bs.peekNextBci());break;
                case Bytecode.IF_ICMPEQ:branchIfSame(state,JavaKind.Int,Cond.EQ,curBci+bs.getBytecodeData(),bs.peekNextBci());break;
                case Bytecode.IF_ICMPNE:branchIfSame(state,JavaKind.Int,Cond.NE,curBci+bs.getBytecodeData(),bs.peekNextBci());break;
                case Bytecode.IF_ICMPLT:branchIfSame(state,JavaKind.Int,Cond.LT,curBci+bs.getBytecodeData(),bs.peekNextBci());break;
                case Bytecode.IF_ICMPGE:branchIfSame(state,JavaKind.Int,Cond.GE,curBci+bs.getBytecodeData(),bs.peekNextBci());break;
                case Bytecode.IF_ICMPGT:branchIfSame(state,JavaKind.Int,Cond.GT,curBci+bs.getBytecodeData(),bs.peekNextBci());break;
                case Bytecode.IF_ICMPLE:branchIfSame(state,JavaKind.Int,Cond.LE,curBci+bs.getBytecodeData(),bs.peekNextBci());break;
                case Bytecode.IF_ACMPEQ:branchIfSame(state,JavaKind.Object,Cond.EQ,curBci+bs.getBytecodeData(),bs.peekNextBci());break;
                case Bytecode.IF_ACMPNE:branchIfSame(state,JavaKind.Object,Cond.NE,curBci+bs.getBytecodeData(),bs.peekNextBci());break;
                case Bytecode.GOTO:
                case Bytecode.GOTO_W:goTo(state,curBci+bs.getBytecodeData());break;
                case Bytecode.JSR:
                case Bytecode.RET:
                case Bytecode.JSR_W: CompilerErrors.unsupported();
                case Bytecode.TABLESWITCH:tableSwitch(state,bs.getTableSwitch(),curBci);break;
                case Bytecode.LOOKUPSWITCH:lookupSwitch(state,bs.getLookupSwitch(),curBci);break;
                case Bytecode.IRETURN:returnOp(state,JavaKind.Int,false);break;
                case Bytecode.LRETURN:returnOp(state,JavaKind.Long,false);break;
                case Bytecode.FRETURN:returnOp(state,JavaKind.Float,false);break;
                case Bytecode.DRETURN:returnOp(state,JavaKind.Double,false);break;
                case Bytecode.ARETURN:returnOp(state,JavaKind.Object,false);break;
                case Bytecode.RETURN:returnOp(state,JavaKind.Void,true);break;
                case Bytecode.GETSTATIC:
                case Bytecode.GETFIELD:
                case Bytecode.PUTSTATIC:
                case Bytecode.PUTFIELD:accessField(state,bs.getBytecodeData(),opcode);break;
                case Bytecode.INVOKEVIRTUAL:
                case Bytecode.INVOKESPECIAL:
                case Bytecode.INVOKESTATIC:
                case Bytecode.INVOKEINTERFACE:
                case Bytecode.INVOKEDYNAMIC:call(state);break;
                case Bytecode.NEW:newInstance(state,bs.getBytecodeData());break;
                case Bytecode.NEWARRAY:newTypeArray(state,bs.getBytecodeData());break;
                case Bytecode.ANEWARRAY:newObjectArray(state,bs.getBytecodeData());break;
                case Bytecode.ARRAYLENGTH:arrayLength(state);break;
                case Bytecode.ATHROW:athrow(state);break;
                case Bytecode.CHECKCAST:checkCast(state,bs.getBytecodeData());break;
                case Bytecode.INSTANCEOF:instanceOf(state,bs.getBytecodeData());break;
                case Bytecode.MONITORENTER:
                case Bytecode.MONITOREXIT:CompilerErrors.unsupported();
                case Bytecode.WIDE:CompilerErrors.shouldNotReachHere();
                case Bytecode.MULTIANEWARRAY:multiNewArray(state,bs.getMultiNewArray());break;
                case Bytecode.IFNULL:branchIfNull(state,JavaKind.Object,Cond.EQ,curBci+bs.getBytecodeData(),bs.peekNextBci());break;
                case Bytecode.IFNONNULL:branchIfNull(state,JavaKind.Object,Cond.NE,curBci+bs.getBytecodeData(),bs.peekNextBci());break;
                default                        : CompilerErrors.shouldNotReachHere();
            }
        }

        // This could happen when back edge splits one consist block
        if(!(lastInstr instanceof BlockEndInstr)){
            BlockEndInstr endInstr = new GotoInstr(cfg.blockContain(bs.peekNextBci()));
            appendToBlock(endInstr);
        }
        block.setBlockEnd((BlockEndInstr)lastInstr);

        for(BlockStartInstr succ:((BlockEndInstr)lastInstr).getSuccessor()){
            succ.merge(block.getVmState());
            workList.add(succ);
        }

    }

    private void appendToBlock(Instruction curInstr){
        lastInstr.setNext(curInstr);
        lastInstr = curInstr;
    }

    private <T> void loadConst(VmState state, JavaKind type, T value){
        ConstantInstr instr = new ConstantInstr(new Value(type,value));
        appendToBlock(instr);
        state.push(instr);
    }

    private void ldc(VmState state, int index){
        Value temp = null;
        Object item = method.getConstantPool().lookupConstant(index);
        if(item instanceof JavaConstant){
            switch (((JavaConstant) item).getJavaKind()){
                case Boolean:
                case Byte:
                case Char:
                case Short:
                case Int:
                    temp = new Value(JavaKind.Int,((JavaConstant)item).asInt());
                    break;
                case Long:
                    temp = new Value(JavaKind.Long,((JavaConstant)item).asLong());
                    break;
                case Float:
                    temp = new Value(JavaKind.Long,((JavaConstant)item).asFloat());
                    break;
                case Double:
                    temp = new Value(JavaKind.Long,((JavaConstant)item).asDouble());
                    break;
                default:
                    CompilerErrors.shouldNotReachHere();
            }
        }else{
            temp = new Value(JavaKind.Object,item);
        }
        ConstantInstr instr = new ConstantInstr(temp);
        appendToBlock(instr);
        state.push(instr);
    }

    private void load(VmState state, JavaKind type, int index){
        Instruction temp = state.get(index);
        Instruction.assertType(temp,type);
        state.push(temp);
    }

    private void loadArray(VmState state, JavaKind type){
        Instruction index = state.pop();
        Instruction array = state.pop();
        Instruction.assertType(array,JavaKind.Object);
        Instruction.assertType(index,JavaKind.Int);
        LoadIndexInstr instr = new LoadIndexInstr(array,index,null, type);
        appendToBlock(instr);
        state.push(instr);
    }

    private void store(VmState state, JavaKind type, int index){
        Instruction temp = state.pop();
        Instruction.assertType(temp,type);
        state.set(index,temp);
    }

    private void storeArray(VmState state, JavaKind type){
        Instruction value = state.pop();
        Instruction index = state.pop();
        Instruction array = state.pop();
        Instruction.assertType(index,JavaKind.Int);
        Instruction.assertType(value,type);
        StoreIndexInstr instr = new StoreIndexInstr(array,index,null,type,value);
        appendToBlock(instr);
    }

    private void duplicate(VmState state, int opcode){
        switch (opcode){
            case Bytecode.DUP:{
                Instruction temp = state.pop();
                state.push(temp);
                state.push(temp);
                break;
            }
            case Bytecode.DUP_X1:{
                Instruction temp = state.pop();
                Instruction temp2 = state.pop();
                state.push(temp);
                state.push(temp2);
                state.push(temp);
                break;
            }
            case Bytecode.DUP_X2:{
                Instruction temp = state.pop();
                Instruction temp2 = state.pop();
                Instruction temp3 = state.pop();
                state.push(temp);
                state.push(temp3);
                state.push(temp2);
                state.push(temp);
                break;
            }
            case Bytecode.DUP2:{
                Instruction temp = state.pop();
                Instruction temp2 = state.pop();
                state.push(temp);
                state.push(temp2);
                state.push(temp);
                state.push(temp2);
                break;
            }
            case Bytecode.DUP2_X1:{
                Instruction temp = state.pop();
                Instruction temp2 = state.pop();
                Instruction temp3 = state.pop();
                state.push(temp2);
                state.push(temp);
                state.push(temp3);
                state.push(temp2);
                state.push(temp);
                break;
            }
            case Bytecode.DUP2_X2:{
                Instruction temp = state.pop();
                Instruction temp2 = state.pop();
                Instruction temp3 = state.pop();
                Instruction temp4 = state.pop();
                state.push(temp2);
                state.push(temp);
                state.push(temp4);
                state.push(temp3);
                state.push(temp2);
                state.push(temp);
                break;
            }
            default:
                CompilerErrors.shouldNotReachHere();
        }
    }

    private void swap(VmState state){
        Instruction temp = state.pop();
        Instruction temp1 = state.pop();
        state.push(temp);
        state.push(temp1);
    }

    private void arithmetic(VmState state, JavaKind type,int opcode){
        Instruction right = state.pop();
        Instruction left = state.pop();
        Instruction.assertType(left,type);
        Instruction.assertType(right,type);
        ArithmeticInstr instr = new ArithmeticInstr(opcode,left,right);
        appendToBlock(instr);
        state.push(instr);
    }

    private void negate(VmState state, JavaKind type){
        Instruction temp = state.pop();
        Instruction.assertType(temp,type);
        NegateInstr instr = new NegateInstr(temp);
        appendToBlock(instr);
        state.push(instr);
    }

    private void shift(VmState state, JavaKind type, int opcode){
        Instruction right = state.pop();
        Instruction left = state.pop();
        Instruction.assertType(right,JavaKind.Int);
        Instruction.assertType(left,type);
        ShiftInstr instr = new ShiftInstr(opcode,left,right);
        appendToBlock(instr);
        state.push(instr);
    }

    private void logic(VmState state, JavaKind type, int opcode){
        Instruction right = state.pop();
        Instruction left = state.pop();
        Instruction.assertType(right,type);
        Instruction.assertType(left,type);
        LogicInstr instr = new LogicInstr(opcode,left,right);
        appendToBlock(instr);
        state.push(instr);
    }

    private void increment(VmState state, BytecodeStream.IINC iinc){
        // increment local variable by constant
        int index = iinc.getIncrementIndex();
        int constant = iinc.getIncrementConst();

        load(state,JavaKind.Int,index);

        ConstantInstr instr = new ConstantInstr(new Value(JavaKind.Int,constant));
        appendToBlock(instr);
        state.push(instr);

        arithmetic(state,JavaKind.Int,Bytecode.IADD);

        store(state,JavaKind.Int,index);
    }

    private void typeCast(VmState state,  JavaKind fromType, JavaKind toType, int opcode){
        Instruction from = state.pop();
        Instruction.assertType(from,fromType);
        JavaKind t = toType;
        if(t==JavaKind.Byte || t==JavaKind.Char|| t==JavaKind.Short){
            t = JavaKind.Int;
        }
        TypeCastInstr instr = new TypeCastInstr(opcode,from,t);
        appendToBlock(instr);
        state.push(instr);
    }

    private void compare(VmState state, JavaKind type, int opcode){
        Instruction right = state.pop();
        Instruction left = state.pop();
        Instruction.assertType(left,type);
        Instruction.assertType(right,type);
        CompareInstr instr = new CompareInstr(opcode,left,right);
        appendToBlock(instr);
        state.push(instr);
    }

    private void branchIfZero(VmState state, JavaKind type, Cond cond, int trueBci, int falseBci){
        Instruction left = state.pop();
        ConstantInstr right = new ConstantInstr(new Value(JavaKind.Int,0));
        Instruction.assertType(left,type);
        branchIf(state,left,right,cond,trueBci,falseBci);
    }

    private void branchIfNull(VmState state, JavaKind type, Cond cond, int trueBci, int falseBci){
        Instruction left = state.pop();
        ConstantInstr right = new ConstantInstr(new Value(JavaKind.Object,null));
        Instruction.assertType(left,type);
        branchIf(state,left,right,cond,trueBci,falseBci);
    }

    private void branchIfSame(VmState state, JavaKind type, Cond cond, int trueBci, int falseBci){
        Instruction left = state.pop();
        Instruction right = state.pop();
        Instruction.assertType(left,type);
        Instruction.assertType(right,type);
        branchIf(state,left,right,cond,trueBci,falseBci);
    }

    private void branchIf(VmState state,Instruction left, Instruction right, Cond cond, int trueBci, int falseBci){
        ArrayList<BlockStartInstr> succ = new ArrayList<>() {{
            add(cfg.blockContain(trueBci));
            add(cfg.blockContain(falseBci));
        }};
        IfInstr instr = new IfInstr(succ,left,right,cond);
        appendToBlock(instr);
    }

    private void goTo(VmState state, int destBci){
        GotoInstr instr = new GotoInstr(cfg.blockContain(destBci));
        appendToBlock(instr);
    }

    private void tableSwitch(VmState state, BytecodeStream.TableSwitch sw, int curBci){
        int len = sw.getNumOfCase();
        ArrayList<BlockStartInstr> succ = new ArrayList<>(len+1);
        int i=0;
        while(i<len){
            succ.set(i,cfg.blockContain(sw.getKeyDest(i)+curBci));
            i++;
        }
        succ.set(i,cfg.blockContain(sw.getDefaultDest()+curBci));

        Instruction index = state.pop();
        Instruction.assertType(index,JavaKind.Int);
        TableSwitchInstr instr = new TableSwitchInstr(succ,index,sw.getLowKey(),sw.getHighKey());
        appendToBlock(instr);
    }

    private void lookupSwitch(VmState state, BytecodeStream.LookupSwitch sw, int curBci){
        int len = sw.getNumOfCase();
        ArrayList<BlockStartInstr> succ = new ArrayList<>(len+1);
        int[] key = new int[len];
        int i=0;
        while(i<len){
            key[i] = sw.getMatch(i);
            succ.set(i,cfg.blockContain(sw.getOffset(i)+curBci));
            i++;
        }
        succ.set(i,cfg.blockContain(sw.getDefaultDest()+curBci));

        Instruction index = state.pop();
        Instruction.assertType(index,JavaKind.Int);
        LookupSwitchInstr instr = new LookupSwitchInstr(succ,index,key);
        appendToBlock(instr);
    }

    private void returnOp(VmState state, JavaKind type, boolean justReturn){
        Instruction val = null;
        if(!justReturn) {
            val = state.pop();
            Instruction.assertType(val,type);
        }

        JavaKind returnKind = method.getSignature().getReturnKind();
        switch (returnKind){
            case Byte:{
                ConstantInstr mask = new ConstantInstr(new Value(JavaKind.Int,0xFF));
                appendToBlock(mask);
                LogicInstr t = new LogicInstr(Bytecode.IAND,mask,val);
                appendToBlock(t);
                val = t;
                break;
            }
            case Short:
            case Char:{
                ConstantInstr mask = new ConstantInstr(new Value(JavaKind.Int,0xFFFF));
                appendToBlock(mask);
                LogicInstr t = new LogicInstr(Bytecode.IAND,mask,val);
                appendToBlock(t);
                val = t;
                break;
            }
            case Boolean:{
                ConstantInstr mask = new ConstantInstr(new Value(JavaKind.Int,1));
                appendToBlock(mask);
                LogicInstr t = new LogicInstr(Bytecode.IAND,mask,val);
                appendToBlock(t);
                val = t;
                break;
            }
            default:
                break;
        }

        ReturnInstr instr = new ReturnInstr(val);
        appendToBlock(instr);
    }

    private void accessField(VmState state, int index, int opcode){
        ConstantInstr holder=null;
        JavaField field = method.getConstantPool().lookupField(index,method,opcode);
        if(opcode==Bytecode.PUTSTATIC || opcode==Bytecode.GETSTATIC){
            holder = new ConstantInstr(new Value(JavaKind.Object,field.getJavaKind().toJavaClass()));
            appendToBlock(holder);
        }
        switch (opcode){
            case Bytecode.GETSTATIC:{
                LoadFieldInstr instr = new LoadFieldInstr(holder,((HotSpotResolvedJavaField)field).getOffset(),field);
                appendToBlock(instr);
                state.push(instr);
                break;
            }
            case Bytecode.PUTSTATIC:{
                Instruction val = state.pop();
                StoreFieldInstr instr = new StoreFieldInstr(holder,((HotSpotResolvedJavaField)field).getOffset(),field,val);
                appendToBlock(instr);
                break;
            }
            case Bytecode.GETFIELD:{
                Instruction object = state.pop();
                Instruction.assertType(object,JavaKind.Object);
                LoadFieldInstr instr = new LoadFieldInstr(object,((HotSpotResolvedJavaField)field).getOffset(),field);
                appendToBlock(instr);
                state.push(instr);
                break;
            }
            case Bytecode.PUTFIELD:{
                Instruction val = state.pop();
                Instruction object = state.pop();
                Instruction.assertType(object,JavaKind.Object);
                StoreFieldInstr instr = new StoreFieldInstr(object,((HotSpotResolvedJavaField)field).getOffset(),field,val);
                appendToBlock(instr);
                break;
            }
            default:CompilerErrors.shouldNotReachHere();
        }
    }


    private void call(VmState state) {
    }

    private void newInstance(VmState state, int index){
        JavaType klass = method.getConstantPool().lookupType(index,-1);
        NewInstr instr = new NewInstr(klass);
        appendToBlock(instr);
        state.push(instr);
    }

    private void newTypeArray(VmState state, int elementType){
        Instruction len = state.pop();
        Instruction.assertType(len,JavaKind.Int);
        JavaKind type = Converter.fromBasicType(elementType);
        NewTypeArrayInstr instr = new NewTypeArrayInstr(len,type);
        appendToBlock(instr);
        state.push(instr);
    }

    private void newObjectArray(VmState state,int index){
        Instruction len = state.pop();
        Instruction.assertType(len,JavaKind.Int);
        JavaType klass = method.getConstantPool().lookupType(index,-1);

        NewObjectArrayInstr instr = new NewObjectArrayInstr(len,klass);
        appendToBlock(instr);
        state.push(instr);
    }

    private void arrayLength(VmState state){
        Instruction array =  state.pop();
        Instruction.assertType(array,JavaKind.Object);
        ArrayLenInstr instr = new ArrayLenInstr(array);
        appendToBlock(instr);
        state.push(instr);
    }

    private void athrow(VmState state){
        Instruction exception = state.pop();
        Instruction.assertType(exception,JavaKind.Object);
        ThrowInstr instr = new ThrowInstr(new ArrayList<>(),exception);
        appendToBlock(instr);
    }

    private void checkCast(VmState state,int index){
        JavaType klass = method.getConstantPool().lookupType(index,-1);
        Instruction object = state.pop();
        Instruction.assertType(object,JavaKind.Object);
        CheckCastInstr instr = new CheckCastInstr(klass,object);
        appendToBlock(instr);
        state.push(instr);
    }

    private void instanceOf(VmState state,int index){
        JavaType klass = method.getConstantPool().lookupType(index,-1);
        Instruction object = state.pop();
        Instruction.assertType(object,JavaKind.Object);
        InstanceOfInstr instr = new InstanceOfInstr(klass,object);
        appendToBlock(instr);
        state.push(instr);
    }

    private void monitorEnter(VmState state){
    }

    private void monitorExit(VmState state){
    }

    private void multiNewArray(VmState state, BytecodeStream.MultiNewArray mna){
        JavaType klass = method.getConstantPool().lookupType(mna.getConstPoolIndex(),-1);
        int dimension = mna.getDimension();
        Instruction[] dimenInstrs = new Instruction[dimension];
        for(int i=dimension-1;i>=0;i--){
            Instruction di = state.pop();
            Instruction.assertType(di,JavaKind.Int);
            dimenInstrs[i] = di;
        }

        MultiNewArrayInstr instr = new MultiNewArrayInstr(klass,dimenInstrs);
        appendToBlock(instr);
        state.push(instr);
    }
}






















