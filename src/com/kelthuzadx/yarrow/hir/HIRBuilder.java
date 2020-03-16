package com.kelthuzadx.yarrow.hir;

import com.kelthuzadx.yarrow.bytecode.Bytecode;
import com.kelthuzadx.yarrow.bytecode.BytecodeStream;
import com.kelthuzadx.yarrow.core.YarrowError;
import com.kelthuzadx.yarrow.hir.instr.*;
import com.kelthuzadx.yarrow.optimize.Walker;
import com.kelthuzadx.yarrow.util.Converter;
import com.kelthuzadx.yarrow.util.Logger;
import jdk.vm.ci.code.MemoryBarriers;
import jdk.vm.ci.hotspot.HotSpotObjectConstant;
import jdk.vm.ci.hotspot.HotSpotResolvedJavaField;
import jdk.vm.ci.hotspot.HotSpotResolvedJavaMethod;
import jdk.vm.ci.meta.*;

import java.util.*;

import static com.kelthuzadx.yarrow.core.YarrowProperties.Debug.PrintIR;

/**
 * HirBuilder performs an abstract interpretation, it transform java bytecode to compiler HIR.
 * Every single basic block has its own VmState, it would be holder by state instructions,(i.e
 * any instructions which derived from StateInstr)
 *
 * @author kelthuzadx
 */
public class HIRBuilder {
    // Target method to be compiled
    private HotSpotResolvedJavaMethod method;
    // Control flow graph for method
    private CFG cfg;
    // Last visited instruction
    private Instruction lastInstr;
    // Work list to support BFS on CFG
    private Queue<BlockStartInstr> workList;
    // Current vm state
    private VmState state;

    private boolean writeFinal;

    private boolean writeVolatile;

    public HIRBuilder(HotSpotResolvedJavaMethod method) {
        this.method = method;
        this.lastInstr = null;
        this.writeFinal = false;
        this.writeVolatile = false;
    }

    public HIRBuilder build() {
        // Construct cfg and detect reducible loops
        cfg = new CFG(method);
        cfg.build();

        // Abstract interpretation
        Set<Integer> visit = new HashSet<>(cfg.getBlocks().length);

        workList = new ArrayDeque<>();
        BlockStartInstr methodEntry = cfg.blockContain(0);
        methodEntry.mergeVmState(createEntryVmState());
        workList.add(methodEntry);

        while (!workList.isEmpty()) {
            BlockStartInstr blockStart = workList.remove();
            if (!visit.contains(blockStart.getBlockId())) {
                visit.add(blockStart.getBlockId());
                if (lastInstr == null) {
                    lastInstr = methodEntry;
                } else {
                    lastInstr.setNext(blockStart);
                    lastInstr = blockStart;
                }
                fulfillBlock(blockStart);
            }
        }

        if (PrintIR) {
            Logger.logf("=====Phase1: SSA Form=====>");
            printSSA(new HashSet<>(), cfg.blockContain(0));
        }

        return this;
    }

    private VmState createEntryVmState() {
        VmState state = new VmState(method.getMaxStackSize(), method.getMaxLocals());
        int paramIndex = 0;

        if (method.hasReceiver()) {
            ParamInstr receiverInstr = new ParamInstr(new Value(JavaKind.Object), method, false, paramIndex);
            state.set(paramIndex, receiverInstr);
            paramIndex++;
        }

        Signature sig = method.getSignature();
        for (int i = 0; i < sig.getParameterCount(false/*Receiver already processed*/); i++) {
            ParamInstr pi = new ParamInstr(new Value(sig.getParameterKind(i)), method, false, paramIndex);
            state.set(paramIndex, pi);
            paramIndex++;
        }
        return state;

    }

    private void fulfillBlock(BlockStartInstr block) {
        state = block.getVmState();

        BytecodeStream bs = new BytecodeStream(method.getCode(), block.getStartBci(), block.getEndBci());
        while (bs.hasNext()) {
            int curBci = bs.next();
            int opcode = bs.currentBytecode();
            System.out.println(bs.getCurrentBytecodeString());
            switch (opcode) {
                case Bytecode.NOP:
                    break;
                case Bytecode.ACONST_NULL:
                    loadConst(JavaKind.Object, null);
                    break;
                case Bytecode.ICONST_M1:
                    loadConst(JavaKind.Int, -1);
                    break;
                case Bytecode.ICONST_0:
                    loadConst(JavaKind.Int, 0);
                    break;
                case Bytecode.ICONST_1:
                    loadConst(JavaKind.Int, 1);
                    break;
                case Bytecode.ICONST_2:
                    loadConst(JavaKind.Int, 2);
                    break;
                case Bytecode.ICONST_3:
                    loadConst(JavaKind.Int, 3);
                    break;
                case Bytecode.ICONST_4:
                    loadConst(JavaKind.Int, 4);
                    break;
                case Bytecode.ICONST_5:
                    loadConst(JavaKind.Int, 5);
                    break;
                case Bytecode.LCONST_0:
                    loadConst(JavaKind.Long, 0L);
                    break;
                case Bytecode.LCONST_1:
                    loadConst(JavaKind.Long, 1L);
                    break;
                case Bytecode.FCONST_0:
                    loadConst(JavaKind.Float, 0.0f);
                    break;
                case Bytecode.FCONST_1:
                    loadConst(JavaKind.Float, 1.0f);
                    break;
                case Bytecode.FCONST_2:
                    loadConst(JavaKind.Float, 2.0f);
                    break;
                case Bytecode.DCONST_0:
                    loadConst(JavaKind.Double, 0.0d);
                    break;
                case Bytecode.DCONST_1:
                    loadConst(JavaKind.Double, 1.0d);
                    break;
                case Bytecode.BIPUSH:
                    loadConst(JavaKind.Int, (byte) bs.getBytecodeData());
                    break;
                case Bytecode.SIPUSH:
                    loadConst(JavaKind.Int, (short) bs.getBytecodeData());
                    break;
                case Bytecode.LDC:
                case Bytecode.LDC_W:
                case Bytecode.LDC2_W:
                    ldc(bs.getBytecodeData());
                    break;
                case Bytecode.ILOAD:
                    load(JavaKind.Int, bs.getBytecodeData());
                    break;
                case Bytecode.LLOAD:
                    load(JavaKind.Long, bs.getBytecodeData());
                    break;
                case Bytecode.FLOAD:
                    load(JavaKind.Float, bs.getBytecodeData());
                    break;
                case Bytecode.DLOAD:
                    load(JavaKind.Double, bs.getBytecodeData());
                    break;
                case Bytecode.ALOAD:
                    load(JavaKind.Object, bs.getBytecodeData());
                    break;
                case Bytecode.ILOAD_0:
                    load(JavaKind.Int, 0);
                    break;
                case Bytecode.ILOAD_1:
                    load(JavaKind.Int, 1);
                    break;
                case Bytecode.ILOAD_2:
                    load(JavaKind.Int, 2);
                    break;
                case Bytecode.ILOAD_3:
                    load(JavaKind.Int, 3);
                    break;
                case Bytecode.LLOAD_0:
                    load(JavaKind.Long, 0);
                    break;
                case Bytecode.LLOAD_1:
                    load(JavaKind.Long, 1);
                    break;
                case Bytecode.LLOAD_2:
                    load(JavaKind.Long, 2);
                    break;
                case Bytecode.LLOAD_3:
                    load(JavaKind.Long, 3);
                    break;
                case Bytecode.FLOAD_0:
                    load(JavaKind.Float, 0);
                    break;
                case Bytecode.FLOAD_1:
                    load(JavaKind.Float, 1);
                    break;
                case Bytecode.FLOAD_2:
                    load(JavaKind.Float, 2);
                    break;
                case Bytecode.FLOAD_3:
                    load(JavaKind.Float, 3);
                    break;
                case Bytecode.DLOAD_0:
                    load(JavaKind.Double, 0);
                    break;
                case Bytecode.DLOAD_1:
                    load(JavaKind.Double, 1);
                    break;
                case Bytecode.DLOAD_2:
                    load(JavaKind.Double, 2);
                    break;
                case Bytecode.DLOAD_3:
                    load(JavaKind.Double, 3);
                    break;
                case Bytecode.ALOAD_0:
                    load(JavaKind.Object, 0);
                    break;
                case Bytecode.ALOAD_1:
                    load(JavaKind.Object, 1);
                    break;
                case Bytecode.ALOAD_2:
                    load(JavaKind.Object, 2);
                    break;
                case Bytecode.ALOAD_3:
                    load(JavaKind.Object, 3);
                    break;
                case Bytecode.IALOAD:
                    loadArray(JavaKind.Int);
                    break;
                case Bytecode.LALOAD:
                    loadArray(JavaKind.Long);
                    break;
                case Bytecode.FALOAD:
                    loadArray(JavaKind.Float);
                    break;
                case Bytecode.DALOAD:
                    loadArray(JavaKind.Double);
                    break;
                case Bytecode.AALOAD:
                    loadArray(JavaKind.Object);
                    break;
                case Bytecode.BALOAD:
                    loadArray(JavaKind.Byte);
                    break;
                case Bytecode.CALOAD:
                    loadArray(JavaKind.Char);
                    break;
                case Bytecode.SALOAD:
                    loadArray(JavaKind.Short);
                    break;
                case Bytecode.ISTORE:
                    store(JavaKind.Int, bs.getBytecodeData());
                    break;
                case Bytecode.LSTORE:
                    store(JavaKind.Long, bs.getBytecodeData());
                    break;
                case Bytecode.FSTORE:
                    store(JavaKind.Float, bs.getBytecodeData());
                    break;
                case Bytecode.DSTORE:
                    store(JavaKind.Double, bs.getBytecodeData());
                    break;
                case Bytecode.ASTORE:
                    store(JavaKind.Object, bs.getBytecodeData());
                    break;
                case Bytecode.ISTORE_0:
                    store(JavaKind.Int, 0);
                    break;
                case Bytecode.ISTORE_1:
                    store(JavaKind.Int, 1);
                    break;
                case Bytecode.ISTORE_2:
                    store(JavaKind.Int, 2);
                    break;
                case Bytecode.ISTORE_3:
                    store(JavaKind.Int, 3);
                    break;
                case Bytecode.LSTORE_0:
                    store(JavaKind.Long, 0);
                    break;
                case Bytecode.LSTORE_1:
                    store(JavaKind.Long, 1);
                    break;
                case Bytecode.LSTORE_2:
                    store(JavaKind.Long, 2);
                    break;
                case Bytecode.LSTORE_3:
                    store(JavaKind.Long, 3);
                    break;
                case Bytecode.FSTORE_0:
                    store(JavaKind.Float, 0);
                    break;
                case Bytecode.FSTORE_1:
                    store(JavaKind.Float, 1);
                    break;
                case Bytecode.FSTORE_2:
                    store(JavaKind.Float, 2);
                    break;
                case Bytecode.FSTORE_3:
                    store(JavaKind.Float, 3);
                    break;
                case Bytecode.DSTORE_0:
                    store(JavaKind.Double, 0);
                    break;
                case Bytecode.DSTORE_1:
                    store(JavaKind.Double, 1);
                    break;
                case Bytecode.DSTORE_2:
                    store(JavaKind.Double, 2);
                    break;
                case Bytecode.DSTORE_3:
                    store(JavaKind.Double, 3);
                    break;
                case Bytecode.ASTORE_0:
                    store(JavaKind.Object, 0);
                    break;
                case Bytecode.ASTORE_1:
                    store(JavaKind.Object, 1);
                    break;
                case Bytecode.ASTORE_2:
                    store(JavaKind.Object, 2);
                    break;
                case Bytecode.ASTORE_3:
                    store(JavaKind.Object, 3);
                    break;
                case Bytecode.IASTORE:
                    storeArray(JavaKind.Int);
                    break;
                case Bytecode.LASTORE:
                    storeArray(JavaKind.Long);
                    break;
                case Bytecode.FASTORE:
                    storeArray(JavaKind.Float);
                    break;
                case Bytecode.DASTORE:
                    storeArray(JavaKind.Double);
                    break;
                case Bytecode.AASTORE:
                    storeArray(JavaKind.Object);
                    break;
                case Bytecode.BASTORE:
                    storeArray(JavaKind.Byte);
                    break;
                case Bytecode.CASTORE:
                    storeArray(JavaKind.Char);
                    break;
                case Bytecode.SASTORE:
                    storeArray(JavaKind.Short);
                    break;
                case Bytecode.POP:
                    state.pop();
                    break;
                case Bytecode.POP2:
                    state.pop();
                    state.pop();
                    break;
                case Bytecode.DUP:
                case Bytecode.DUP_X1:
                case Bytecode.DUP_X2:
                case Bytecode.DUP2:
                case Bytecode.DUP2_X1:
                case Bytecode.DUP2_X2:
                    duplicate(opcode);
                    break;
                case Bytecode.SWAP:
                    swap();
                    break;
                case Bytecode.IADD:
                case Bytecode.ISUB:
                case Bytecode.IMUL:
                case Bytecode.IDIV:
                case Bytecode.IREM:
                    arithmetic(JavaKind.Int, opcode);
                    break;
                case Bytecode.LADD:
                case Bytecode.LSUB:
                case Bytecode.LMUL:
                case Bytecode.LDIV:
                case Bytecode.LREM:
                    arithmetic(JavaKind.Long, opcode);
                    break;
                case Bytecode.FADD:
                case Bytecode.FSUB:
                case Bytecode.FMUL:
                case Bytecode.FDIV:
                case Bytecode.FREM:
                    arithmetic(JavaKind.Float, opcode);
                    break;
                case Bytecode.DADD:
                case Bytecode.DSUB:
                case Bytecode.DMUL:
                case Bytecode.DDIV:
                case Bytecode.DREM:
                    arithmetic(JavaKind.Double, opcode);
                    break;
                case Bytecode.INEG:
                    negate(JavaKind.Int);
                    break;
                case Bytecode.LNEG:
                    negate(JavaKind.Long);
                    break;
                case Bytecode.FNEG:
                    negate(JavaKind.Float);
                    break;
                case Bytecode.DNEG:
                    negate(JavaKind.Double);
                    break;
                case Bytecode.ISHL:
                case Bytecode.ISHR:
                case Bytecode.IUSHR:
                    shift(JavaKind.Int, opcode);
                    break;
                case Bytecode.LSHL:
                case Bytecode.LSHR:
                case Bytecode.LUSHR:
                    shift(JavaKind.Long, opcode);
                    break;
                case Bytecode.IAND:
                case Bytecode.IOR:
                case Bytecode.IXOR:
                    logic(JavaKind.Int, opcode);
                    break;
                case Bytecode.LAND:
                case Bytecode.LOR:
                case Bytecode.LXOR:
                    logic(JavaKind.Long, opcode);
                    break;
                case Bytecode.IINC:
                    increment(bs.getIINC());
                    break;
                case Bytecode.I2L:
                    typeCast(JavaKind.Int, JavaKind.Long, opcode);
                    break;
                case Bytecode.I2F:
                    typeCast(JavaKind.Int, JavaKind.Float, opcode);
                    break;
                case Bytecode.I2D:
                    typeCast(JavaKind.Int, JavaKind.Double, opcode);
                    break;
                case Bytecode.L2I:
                    typeCast(JavaKind.Long, JavaKind.Int, opcode);
                    break;
                case Bytecode.L2F:
                    typeCast(JavaKind.Long, JavaKind.Float, opcode);
                    break;
                case Bytecode.L2D:
                    typeCast(JavaKind.Long, JavaKind.Double, opcode);
                    break;
                case Bytecode.F2I:
                    typeCast(JavaKind.Float, JavaKind.Int, opcode);
                    break;
                case Bytecode.F2L:
                    typeCast(JavaKind.Float, JavaKind.Long, opcode);
                    break;
                case Bytecode.F2D:
                    typeCast(JavaKind.Float, JavaKind.Double, opcode);
                    break;
                case Bytecode.D2I:
                    typeCast(JavaKind.Double, JavaKind.Int, opcode);
                    break;
                case Bytecode.D2L:
                    typeCast(JavaKind.Double, JavaKind.Long, opcode);
                    break;
                case Bytecode.D2F:
                    typeCast(JavaKind.Double, JavaKind.Float, opcode);
                    break;
                case Bytecode.I2B:
                    typeCast(JavaKind.Int, JavaKind.Byte, opcode);
                    break;
                case Bytecode.I2C:
                    typeCast(JavaKind.Int, JavaKind.Char, opcode);
                    break;
                case Bytecode.I2S:
                    typeCast(JavaKind.Int, JavaKind.Short, opcode);
                    break;
                case Bytecode.LCMP:
                    compare(JavaKind.Long, opcode);
                    break;
                case Bytecode.FCMPL:
                case Bytecode.FCMPG:
                    compare(JavaKind.Float, opcode);
                    break;
                case Bytecode.DCMPL:
                case Bytecode.DCMPG:
                    compare(JavaKind.Double, opcode);
                    break;
                case Bytecode.IFEQ:
                    branchIfZero(JavaKind.Int, Cond.EQ, curBci + bs.getBytecodeData(), bs.peekNextBci());
                    break;
                case Bytecode.IFNE:
                    branchIfZero(JavaKind.Int, Cond.NE, curBci + bs.getBytecodeData(), bs.peekNextBci());
                    break;
                case Bytecode.IFLT:
                    branchIfZero(JavaKind.Int, Cond.LT, curBci + bs.getBytecodeData(), bs.peekNextBci());
                    break;
                case Bytecode.IFGE:
                    branchIfZero(JavaKind.Int, Cond.GE, curBci + bs.getBytecodeData(), bs.peekNextBci());
                    break;
                case Bytecode.IFGT:
                    branchIfZero(JavaKind.Int, Cond.GT, curBci + bs.getBytecodeData(), bs.peekNextBci());
                    break;
                case Bytecode.IFLE:
                    branchIfZero(JavaKind.Int, Cond.LE, curBci + bs.getBytecodeData(), bs.peekNextBci());
                    break;
                case Bytecode.IF_ICMPEQ:
                    branchIfSame(JavaKind.Int, Cond.EQ, curBci + bs.getBytecodeData(), bs.peekNextBci());
                    break;
                case Bytecode.IF_ICMPNE:
                    branchIfSame(JavaKind.Int, Cond.NE, curBci + bs.getBytecodeData(), bs.peekNextBci());
                    break;
                case Bytecode.IF_ICMPLT:
                    branchIfSame(JavaKind.Int, Cond.LT, curBci + bs.getBytecodeData(), bs.peekNextBci());
                    break;
                case Bytecode.IF_ICMPGE:
                    branchIfSame(JavaKind.Int, Cond.GE, curBci + bs.getBytecodeData(), bs.peekNextBci());
                    break;
                case Bytecode.IF_ICMPGT:
                    branchIfSame(JavaKind.Int, Cond.GT, curBci + bs.getBytecodeData(), bs.peekNextBci());
                    break;
                case Bytecode.IF_ICMPLE:
                    branchIfSame(JavaKind.Int, Cond.LE, curBci + bs.getBytecodeData(), bs.peekNextBci());
                    break;
                case Bytecode.IF_ACMPEQ:
                    branchIfSame(JavaKind.Object, Cond.EQ, curBci + bs.getBytecodeData(), bs.peekNextBci());
                    break;
                case Bytecode.IF_ACMPNE:
                    branchIfSame(JavaKind.Object, Cond.NE, curBci + bs.getBytecodeData(), bs.peekNextBci());
                    break;
                case Bytecode.GOTO:
                case Bytecode.GOTO_W:
                    goTo(curBci + bs.getBytecodeData());
                    break;
                case Bytecode.JSR:
                case Bytecode.RET:
                case Bytecode.JSR_W:
                    YarrowError.unimplemented("ret/jsr considers deprecated and thus not supported");
                case Bytecode.TABLESWITCH:
                    tableSwitch(bs.getTableSwitch(), curBci);
                    break;
                case Bytecode.LOOKUPSWITCH:
                    lookupSwitch(bs.getLookupSwitch(), curBci);
                    break;
                case Bytecode.IRETURN:
                    returnOp(JavaKind.Int, false);
                    break;
                case Bytecode.LRETURN:
                    returnOp(JavaKind.Long, false);
                    break;
                case Bytecode.FRETURN:
                    returnOp(JavaKind.Float, false);
                    break;
                case Bytecode.DRETURN:
                    returnOp(JavaKind.Double, false);
                    break;
                case Bytecode.ARETURN:
                    returnOp(JavaKind.Object, false);
                    break;
                case Bytecode.RETURN:
                    returnOp(JavaKind.Void, true);
                    break;
                case Bytecode.GETSTATIC:
                case Bytecode.GETFIELD:
                case Bytecode.PUTSTATIC:
                case Bytecode.PUTFIELD:
                    accessField(bs.getBytecodeData(), opcode);
                    break;
                case Bytecode.INVOKEVIRTUAL:
                    call(bs.getInvokeVirtual(), opcode);
                    break;
                case Bytecode.INVOKESPECIAL:
                    call(bs.getInvokeSpecial(), opcode);
                    break;
                case Bytecode.INVOKESTATIC:
                    call(bs.getInvokeStatic(), opcode);
                    break;
                case Bytecode.INVOKEINTERFACE:
                    call(bs.getInvokeInterface(), opcode);
                    break;
                case Bytecode.INVOKEDYNAMIC:
                    call(bs.getInvokeDynamic(), opcode);
                    break;
                case Bytecode.NEW:
                    newInstance(bs.getBytecodeData());
                    break;
                case Bytecode.NEWARRAY:
                    newTypeArray(bs.getBytecodeData());
                    break;
                case Bytecode.ANEWARRAY:
                    newObjectArray(bs.getBytecodeData());
                    break;
                case Bytecode.ARRAYLENGTH:
                    arrayLength();
                    break;
                case Bytecode.ATHROW:
                    athrow();
                    break;
                case Bytecode.CHECKCAST:
                    checkCast(bs.getBytecodeData());
                    break;
                case Bytecode.INSTANCEOF:
                    instanceOf(bs.getBytecodeData());
                    break;
                case Bytecode.MONITORENTER:
                    monitorEnter();
                    break;
                case Bytecode.MONITOREXIT:
                    monitorExit();
                    break;
                case Bytecode.WIDE:
                    YarrowError.shouldNotReachHere();
                case Bytecode.MULTIANEWARRAY:
                    multiNewArray(bs.getMultiNewArray());
                    break;
                case Bytecode.IFNULL:
                    branchIfNull(JavaKind.Object, Cond.EQ, curBci + bs.getBytecodeData(), bs.peekNextBci());
                    break;
                case Bytecode.IFNONNULL:
                    branchIfNull(JavaKind.Object, Cond.NE, curBci + bs.getBytecodeData(), bs.peekNextBci());
                    break;
                default:
                    YarrowError.shouldNotReachHere();
            }
        }

        // This could happen when back edge splits one consist block
        if (!(lastInstr instanceof BlockEndInstr)) {
            BlockEndInstr endInstr = new GotoInstr(null, cfg.blockContain(bs.peekNextBci()));
            appendToBlock(endInstr);
        }
        block.setBlockEnd((BlockEndInstr) lastInstr);

        for (BlockStartInstr succ : ((BlockEndInstr) lastInstr).getSuccessor()) {
            succ.mergeVmState(block.getVmState());
            workList.add(succ);
        }

    }

    private void appendToBlock(Instruction curInstr) {
        lastInstr.setNext(curInstr);
        lastInstr = curInstr;

        if (lastInstr instanceof StateInstr) {
            ((StateInstr) lastInstr).setVmState(state.copy());
        }
    }

    private <T> void loadConst(JavaKind type, T value) {
        ConstantInstr instr = new ConstantInstr(new Value(type, value));
        appendToBlock(instr);
        state.push(instr);
    }

    private void ldc(int index) {
        Value temp = null;
        Object item = method.getConstantPool().lookupConstant(index);
        if (item instanceof JavaConstant) {
            switch (((JavaConstant) item).getJavaKind()) {
                case Boolean:
                case Byte:
                case Char:
                case Short:
                case Int:
                    temp = new Value(JavaKind.Int, ((JavaConstant) item).asInt());
                    break;
                case Long:
                    temp = new Value(JavaKind.Long, ((JavaConstant) item).asLong());
                    break;
                case Float:
                    temp = new Value(JavaKind.Float, ((JavaConstant) item).asFloat());
                    break;
                case Double:
                    temp = new Value(JavaKind.Double, ((JavaConstant) item).asDouble());
                    break;
                case Object:
                    temp = new Value(JavaKind.Object, ((HotSpotObjectConstant) item).asObject(((HotSpotObjectConstant) item).getType()));
                    break;
                default:
                    YarrowError.shouldNotReachHere();
            }
        }
        ConstantInstr instr = new ConstantInstr(temp);
        appendToBlock(instr);
        state.push(instr);
    }

    private void load(JavaKind type, int index) {
        Instruction temp = state.get(index);
        Instruction.assertType(temp, type);
        state.push(temp);
    }

    private void loadArray(JavaKind type) {
        Instruction index = state.pop();
        Instruction array = state.pop();
        Instruction.assertType(array, JavaKind.Object);
        Instruction.assertType(index, JavaKind.Int);
        LoadIndexInstr instr = new LoadIndexInstr(array, index, null, type);
        appendToBlock(instr);
        state.push(instr);
    }

    private void store(JavaKind type, int index) {
        Instruction temp = state.pop();
        Instruction.assertType(temp, type);
        state.set(index, temp);
    }

    private void storeArray(JavaKind type) {
        Instruction value = state.pop();
        Instruction index = state.pop();
        Instruction array = state.pop();
        Instruction.assertType(index, JavaKind.Int);
        Instruction.assertType(value, type);
        StoreIndexInstr instr = new StoreIndexInstr(array, index, null, type, value);
        appendToBlock(instr);
    }

    private void duplicate(int opcode) {
        switch (opcode) {
            case Bytecode.DUP: {
                Instruction temp = state.pop();
                state.push(temp);
                state.push(temp);
                break;
            }
            case Bytecode.DUP_X1: {
                Instruction temp = state.pop();
                Instruction temp2 = state.pop();
                state.push(temp);
                state.push(temp2);
                state.push(temp);
                break;
            }
            case Bytecode.DUP_X2: {
                Instruction temp = state.pop();
                Instruction temp2 = state.pop();
                Instruction temp3 = state.pop();
                state.push(temp);
                state.push(temp3);
                state.push(temp2);
                state.push(temp);
                break;
            }
            case Bytecode.DUP2: {
                Instruction temp = state.pop();
                Instruction temp2 = state.pop();
                state.push(temp);
                state.push(temp2);
                state.push(temp);
                state.push(temp2);
                break;
            }
            case Bytecode.DUP2_X1: {
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
            case Bytecode.DUP2_X2: {
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
                YarrowError.shouldNotReachHere();
        }
    }

    private void swap() {
        Instruction temp = state.pop();
        Instruction temp1 = state.pop();
        state.push(temp);
        state.push(temp1);
    }

    private void arithmetic(JavaKind type, int opcode) {
        Instruction right = state.pop();
        Instruction left = state.pop();
        Instruction.assertType(left, type);
        Instruction.assertType(right, type);
        ArithmeticInstr instr = new ArithmeticInstr(opcode, left, right);
        appendToBlock(instr);
        state.push(instr);
    }

    private void negate(JavaKind type) {
        Instruction temp = state.pop();
        Instruction.assertType(temp, type);
        NegateInstr instr = new NegateInstr(temp);
        appendToBlock(instr);
        state.push(instr);
    }

    private void shift(JavaKind type, int opcode) {
        Instruction right = state.pop();
        Instruction left = state.pop();
        Instruction.assertType(right, JavaKind.Int);
        Instruction.assertType(left, type);
        ShiftInstr instr = new ShiftInstr(opcode, left, right);
        appendToBlock(instr);
        state.push(instr);
    }

    private void logic(JavaKind type, int opcode) {
        Instruction right = state.pop();
        Instruction left = state.pop();
        Instruction.assertType(right, type);
        Instruction.assertType(left, type);
        LogicInstr instr = new LogicInstr(opcode, left, right);
        appendToBlock(instr);
        state.push(instr);
    }

    private void increment(BytecodeStream.IINC iinc) {
        // increment local variable by constant
        int index = iinc.getIncrementIndex();
        int constant = iinc.getIncrementConst();

        load(JavaKind.Int, index);

        ConstantInstr instr = new ConstantInstr(new Value(JavaKind.Int, constant));
        appendToBlock(instr);
        state.push(instr);

        arithmetic(JavaKind.Int, Bytecode.IADD);

        store(JavaKind.Int, index);
    }

    private void typeCast(JavaKind fromType, JavaKind toType, int opcode) {
        Instruction from = state.pop();
        Instruction.assertType(from, fromType);
        JavaKind t = toType;
        if (t == JavaKind.Byte || t == JavaKind.Char || t == JavaKind.Short) {
            t = JavaKind.Int;
        }
        TypeCastInstr instr = new TypeCastInstr(opcode, from, t);
        appendToBlock(instr);
        state.push(instr);
    }

    private void compare(JavaKind type, int opcode) {
        Instruction right = state.pop();
        Instruction left = state.pop();
        Instruction.assertType(left, type);
        Instruction.assertType(right, type);
        CompareInstr instr = new CompareInstr(opcode, left, right);
        appendToBlock(instr);
        state.push(instr);
    }

    private void branchIfZero(JavaKind type, Cond cond, int trueBci, int falseBci) {
        VmState stateBefore = state.copy();
        Instruction left = state.pop();
        ConstantInstr right = new ConstantInstr(new Value(JavaKind.Int, 0));
        Instruction.assertType(left, type);
        branchIf(stateBefore, left, right, cond, trueBci, falseBci);
    }

    private void branchIfNull(JavaKind type, Cond cond, int trueBci, int falseBci) {
        VmState stateBefore = state.copy();
        Instruction left = state.pop();
        ConstantInstr right = new ConstantInstr(new Value(JavaKind.Object, null));
        Instruction.assertType(left, type);
        branchIf(stateBefore, left, right, cond, trueBci, falseBci);
    }

    private void branchIfSame(JavaKind type, Cond cond, int trueBci, int falseBci) {
        VmState stateBefore = state.copy();
        Instruction right = state.pop();
        Instruction left = state.pop();
        Instruction.assertType(left, type);
        Instruction.assertType(right, type);
        branchIf(stateBefore, left, right, cond, trueBci, falseBci);
    }

    private void branchIf(VmState stateBefore, Instruction left, Instruction right, Cond cond, int trueBci, int falseBci) {
        IfInstr instr = new IfInstr(stateBefore, cfg.blockContain(trueBci), cfg.blockContain(falseBci), left, right, cond);
        appendToBlock(instr);
    }

    private void goTo(int destBci) {
        GotoInstr instr = new GotoInstr(null, cfg.blockContain(destBci));
        appendToBlock(instr);
    }

    private void tableSwitch(BytecodeStream.TableSwitch sw, int curBci) {
        int len = sw.getNumOfCase();
        BlockStartInstr[] succ = new BlockStartInstr[len + 1];
        int i = 0;
        while (i < len) {
            succ[i] = cfg.blockContain(sw.getKeyDest(i) + curBci);
            i++;
        }
        succ[i] = cfg.blockContain(sw.getDefaultDest() + curBci);

        VmState stateBefore = state.copy();
        Instruction index = state.pop();
        Instruction.assertType(index, JavaKind.Int);
        TableSwitchInstr instr = new TableSwitchInstr(stateBefore, Arrays.asList(succ), index, sw.getLowKey());
        appendToBlock(instr);
    }

    private void lookupSwitch(BytecodeStream.LookupSwitch sw, int curBci) {
        int len = sw.getNumOfCase();
        BlockStartInstr[] succ = new BlockStartInstr[len + 1];
        int[] key = new int[len];
        int i = 0;
        while (i < len) {
            key[i] = sw.getMatch(i);
            succ[i] = cfg.blockContain(sw.getOffset(i) + curBci);
            i++;
        }
        succ[i] = cfg.blockContain(sw.getDefaultDest() + curBci);

        VmState stateBefore = state.copy();
        Instruction index = state.pop();
        Instruction.assertType(index, JavaKind.Int);
        LookupSwitchInstr instr = new LookupSwitchInstr(stateBefore, Arrays.asList(succ), index, key);
        appendToBlock(instr);
    }

    private void returnOp(JavaKind type, boolean justReturn) {
        Instruction val = null;
        if (!justReturn) {
            val = state.pop();
            Instruction.assertType(val, type);
        }

        JavaKind returnKind = method.getSignature().getReturnKind();
        switch (returnKind) {
            case Byte: {
                ConstantInstr mask = new ConstantInstr(new Value(JavaKind.Int, 0xFF));
                appendToBlock(mask);
                LogicInstr t = new LogicInstr(Bytecode.IAND, mask, val);
                appendToBlock(t);
                val = t;
                break;
            }
            case Short:
            case Char: {
                ConstantInstr mask = new ConstantInstr(new Value(JavaKind.Int, 0xFFFF));
                appendToBlock(mask);
                LogicInstr t = new LogicInstr(Bytecode.IAND, mask, val);
                appendToBlock(t);
                val = t;
                break;
            }
            case Boolean: {
                ConstantInstr mask = new ConstantInstr(new Value(JavaKind.Int, 1));
                appendToBlock(mask);
                LogicInstr t = new LogicInstr(Bytecode.IAND, mask, val);
                appendToBlock(t);
                val = t;
                break;
            }
            default:
                break;
        }

        // If <init> writes final field, a StoreStore barrier will be inserted before method return.
        // FIXME: AlwaysSafeConstructor does the same thing
        if (method.isConstructor() && writeFinal) {
            MemBarrierInstr memBarInstr = new MemBarrierInstr(MemoryBarriers.STORE_STORE);
            appendToBlock(memBarInstr);
        }

        ReturnInstr instr = new ReturnInstr(val);
        appendToBlock(instr);
    }

    private void accessField(int index, int opcode) {
        ConstantInstr holder = null;
        HotSpotResolvedJavaField field = (HotSpotResolvedJavaField) method.getConstantPool().lookupField(index, method, opcode);
        if (opcode == Bytecode.PUTSTATIC || opcode == Bytecode.GETSTATIC) {
            holder = new ConstantInstr(new Value(JavaKind.Object, field.getJavaKind().toJavaClass()));
            appendToBlock(holder);
        }

        switch (opcode) {
            case Bytecode.GETSTATIC: {
                LoadFieldInstr instr = new LoadFieldInstr(holder, field.getOffset(), field);
                appendToBlock(instr);
                state.push(instr);
                break;
            }
            case Bytecode.PUTSTATIC: {
                Instruction val = state.pop();
                StoreFieldInstr instr = new StoreFieldInstr(holder, field.getOffset(), field, val);
                appendToBlock(instr);
                break;
            }
            case Bytecode.GETFIELD: {
                Instruction object = state.pop();
                Instruction.assertType(object, JavaKind.Object);
                LoadFieldInstr instr = new LoadFieldInstr(object, field.getOffset(), field);
                appendToBlock(instr);
                state.push(instr);
                break;
            }
            case Bytecode.PUTFIELD: {
                if (field.isFinal()) {
                    this.writeFinal = true;
                }
                if (field.isVolatile()) {
                    this.writeVolatile = true;
                }
                Instruction val = state.pop();
                Instruction object = state.pop();
                Instruction.assertType(object, JavaKind.Object);
                StoreFieldInstr instr = new StoreFieldInstr(object, field.getOffset(), field, val);
                appendToBlock(instr);
                break;
            }
            default:
                YarrowError.shouldNotReachHere();
        }
    }

    private void call(BytecodeStream.Invoke invoke, int opcode) {
        JavaMethod target = null;
        boolean hasReceiver = false;
        VmState stateBefore = state.copy();
        switch (opcode) {
            case Bytecode.INVOKEINTERFACE: {
                var m = ((BytecodeStream.InvokeInterface) invoke);
                target = method.getConstantPool().lookupMethod(m.getConstPoolIndex(), opcode);
                hasReceiver = true;
                break;
            }
            case Bytecode.INVOKESPECIAL: {
                var m = ((BytecodeStream.InvokeSpecial) invoke);
                target = method.getConstantPool().lookupMethod(m.getConstPoolIndex(), opcode);
                hasReceiver = true;
                break;
            }
            case Bytecode.INVOKESTATIC: {
                var m = ((BytecodeStream.InvokeStatic) invoke);
                target = method.getConstantPool().lookupMethod(m.getConstPoolIndex(), opcode);
                break;
            }
            case Bytecode.INVOKEVIRTUAL: {
                var m = ((BytecodeStream.InvokeVirtual) invoke);
                target = method.getConstantPool().lookupMethod(m.getConstPoolIndex(), opcode);
                hasReceiver = true;
                break;
            }
            case Bytecode.INVOKEDYNAMIC: {
                var m = ((BytecodeStream.InvokeDynamic) invoke);
                target = method.getConstantPool().lookupMethod(m.getConstPoolIndex(), opcode);
                break;
            }
            default:
                YarrowError.unimplemented();
        }

        Signature sig = target.getSignature();
        int argc = sig.getParameterCount(false);
        Instruction[] arguments = new Instruction[argc];
        Instruction receiver = null;
        for (int i = 0; i < argc; i++) {
            arguments[i] = state.pop();
            //Instruction.assertType(arguments[i], sig.getParameterKind(i));
        }
        if (hasReceiver) {
            receiver = state.pop();
            Instruction.assertType(receiver, JavaKind.Object);
        }
        CallInstr instr = new CallInstr(new Value(sig.getReturnKind()), stateBefore, receiver, arguments, target, sig, opcode);
        appendToBlock(instr);

        if (sig.getReturnKind() != JavaKind.Void) {
            state.push(instr);
        }
    }

    private void newInstance(int index) {
        VmState stateBefore = state.copy();
        JavaType klass = method.getConstantPool().lookupType(index, -1);
        NewInstr instr = new NewInstr(stateBefore, klass);
        appendToBlock(instr);
        state.push(instr);
    }

    private void newTypeArray(int elementType) {
        VmState stateBefore = state.copy();
        Instruction len = state.pop();
        Instruction.assertType(len, JavaKind.Int);
        JavaKind type = Converter.fromBasicType(elementType);
        NewTypeArrayInstr instr = new NewTypeArrayInstr(stateBefore, len, type);
        appendToBlock(instr);
        state.push(instr);
    }

    private void newObjectArray(int index) {
        VmState stateBefore = state.copy();
        Instruction len = state.pop();
        Instruction.assertType(len, JavaKind.Int);
        JavaType klass = method.getConstantPool().lookupType(index, -1);

        NewObjectArrayInstr instr = new NewObjectArrayInstr(stateBefore, len, klass);
        appendToBlock(instr);
        state.push(instr);
    }

    private void arrayLength() {
        Instruction array = state.pop();
        Instruction.assertType(array, JavaKind.Object);
        ArrayLenInstr instr = new ArrayLenInstr(array);
        appendToBlock(instr);
        state.push(instr);
    }

    private void athrow() {
        VmState stateBefore = state.copy();
        Instruction exception = state.pop();
        Instruction.assertType(exception, JavaKind.Object);
        ThrowInstr instr = new ThrowInstr(stateBefore, new ArrayList<>(), exception);
        appendToBlock(instr);
    }

    private void checkCast(int index) {
        VmState stateBefore = state.copy();
        JavaType klass = method.getConstantPool().lookupType(index, -1);
        Instruction object = state.pop();
        Instruction.assertType(object, JavaKind.Object);
        CheckCastInstr instr = new CheckCastInstr(stateBefore, klass, object);
        appendToBlock(instr);
        state.push(instr);
    }

    private void instanceOf(int index) {
        VmState stateBefore = state.copy();
        JavaType klass = method.getConstantPool().lookupType(index, -1);
        Instruction object = state.pop();
        Instruction.assertType(object, JavaKind.Object);
        InstanceOfInstr instr = new InstanceOfInstr(stateBefore, klass, object);
        appendToBlock(instr);
        state.push(instr);
    }

    private void monitorEnter() {
        Instruction lockObj = state.pop();
        Instruction.assertType(lockObj, JavaKind.Object);
        VmState stateBefore = state.copy(); // save state before locking(but after pop element) in case of deopt after a nullptr exception
        Instruction lock = state.lock(lockObj);
        MonitorEnterInstr instr = new MonitorEnterInstr(lock, stateBefore);
        appendToBlock(instr);
    }

    private void monitorExit() {
        Instruction lock = state.unlock();
        MonitorExitInstr instr = new MonitorExitInstr(lock);
        appendToBlock(instr);
    }

    private void multiNewArray(BytecodeStream.MultiNewArray mna) {
        VmState stateBefore = state.copy();
        JavaType klass = method.getConstantPool().lookupType(mna.getConstPoolIndex(), -1);
        int dimension = mna.getDimension();
        Instruction[] dimenInstr = new Instruction[dimension];
        for (int i = dimension - 1; i >= 0; i--) {
            Instruction di = state.pop();
            Instruction.assertType(di, JavaKind.Int);
            dimenInstr[i] = di;
        }

        MultiNewArrayInstr instr = new MultiNewArrayInstr(stateBefore, klass, dimenInstr);
        appendToBlock(instr);
        state.push(instr);
    }

    private void printSSA(Set<BlockStartInstr> visit, BlockStartInstr block) {
        if (block == null || visit.contains(block)) {
            return;
        }
        Logger.logf("{}", block.getVmState().toString());
        Walker.walkBytecodes(block, instr -> Logger.logf("{}", instr.toString()));
        Logger.logf("");
        visit.add(block);
        for (BlockStartInstr succ : block.getBlockEnd().getSuccessor()) {
            printSSA(visit, succ);
        }
    }
}






















