package com.kelthuzadx.yarrow.ir;

import com.kelthuzadx.yarrow.ir.node.ConstNode;
import com.kelthuzadx.yarrow.ir.node.ValueNode;
import jdk.vm.ci.hotspot.HotSpotResolvedJavaMethod;
import jdk.vm.ci.meta.JavaConstant;
import jdk.vm.ci.runtime.JVMCI;

import static com.kelthuzadx.yarrow.core.Bytecode.*;

public class GraphBuilder {
    private IdealGraph graph;
    private HotSpotResolvedJavaMethod method;

    public GraphBuilder(HotSpotResolvedJavaMethod method){
        this.method = method;
    }

    public IdealGraph build(){
        Block[] blocks = CFG.build(method);
        for(Block block:blocks){
            JvmState jvms = new JvmState(method);

        }
        return graph;
    }

    private void doBlock(Block block, JvmState jvms){
        BytecodeStream stream = new BytecodeStream(method.getCode(),method.getCodeSize());
        while (stream.hasNext()){
            int bci = stream.next();
            int opcode = stream.currentBytecode();
            switch (opcode) {
                case NOP            : /* oops... */ break;
                case ACONST_NULL    : jvms.pushStack(new ConstNode(JavaConstant.NULL_POINTER));  break;
                case ICONST_M1      :
                case ICONST_0       :
                case ICONST_1       :
                case ICONST_2       :
                case ICONST_3       :
                case ICONST_4       :
                case ICONST_5       : jvms.pushStack(new ConstNode(JavaConstant.forInt(opcode - ICONST_0))); break;
                case LCONST_0       :
                case LCONST_1       : jvms.pushStack(new ConstNode(JavaConstant.forLong(opcode - LCONST_0))); break;
                case FCONST_0       :
                case FCONST_1       :
                case FCONST_2       : jvms.pushStack(new ConstNode(JavaConstant.forFloat(opcode - FCONST_0))); break;
                case DCONST_0       :
                case DCONST_1       : jvms.pushStack(new ConstNode(JavaConstant.forDouble(opcode - DCONST_0))); break;
                case BIPUSH         :
                case SIPUSH         : jvms.pushStack(new ConstNode(JavaConstant.forInt(stream.getBytecodeData()))); break;
                case LDC            :
                case LDC_W          :
                case LDC2_W         : genLoadConstant(stream.readCPI(), opcode); break;
                case ILOAD          : loadLocal(stream.readLocalIndex(), JavaKind.Int); break;
                case LLOAD          : loadLocal(stream.readLocalIndex(), JavaKind.Long); break;
                case FLOAD          : loadLocal(stream.readLocalIndex(), JavaKind.Float); break;
                case DLOAD          : loadLocal(stream.readLocalIndex(), JavaKind.Double); break;
                case ALOAD          : loadLocalObject(stream.readLocalIndex()); break;
                case ILOAD_0        :
                case ILOAD_1        :
                case ILOAD_2        :
                case ILOAD_3        : loadLocal(opcode - ILOAD_0, JavaKind.Int); break;
                case LLOAD_0        :
                case LLOAD_1        :
                case LLOAD_2        :
                case LLOAD_3        : loadLocal(opcode - LLOAD_0, JavaKind.Long); break;
                case FLOAD_0        :
                case FLOAD_1        :
                case FLOAD_2        :
                case FLOAD_3        : loadLocal(opcode - FLOAD_0, JavaKind.Float); break;
                case DLOAD_0        :
                case DLOAD_1        :
                case DLOAD_2        :
                case DLOAD_3        : loadLocal(opcode - DLOAD_0, JavaKind.Double); break;
                case ALOAD_0        :
                case ALOAD_1        :
                case ALOAD_2        :
                case ALOAD_3        : loadLocalObject(opcode - ALOAD_0); break;
                case IALOAD         : genLoadIndexed(JavaKind.Int   ); break;
                case LALOAD         : genLoadIndexed(JavaKind.Long  ); break;
                case FALOAD         : genLoadIndexed(JavaKind.Float ); break;
                case DALOAD         : genLoadIndexed(JavaKind.Double); break;
                case AALOAD         : genLoadIndexed(JavaKind.Object); break;
                case BALOAD         : genLoadIndexed(JavaKind.Byte  ); break;
                case CALOAD         : genLoadIndexed(JavaKind.Char  ); break;
                case SALOAD         : genLoadIndexed(JavaKind.Short ); break;
                case ISTORE         : storeLocal(JavaKind.Int, stream.readLocalIndex()); break;
                case LSTORE         : storeLocal(JavaKind.Long, stream.readLocalIndex()); break;
                case FSTORE         : storeLocal(JavaKind.Float, stream.readLocalIndex()); break;
                case DSTORE         : storeLocal(JavaKind.Double, stream.readLocalIndex()); break;
                case ASTORE         : storeLocal(JavaKind.Object, stream.readLocalIndex()); break;
                case ISTORE_0       :
                case ISTORE_1       :
                case ISTORE_2       :
                case ISTORE_3       : storeLocal(JavaKind.Int, opcode - ISTORE_0); break;
                case LSTORE_0       :
                case LSTORE_1       :
                case LSTORE_2       :
                case LSTORE_3       : storeLocal(JavaKind.Long, opcode - LSTORE_0); break;
                case FSTORE_0       :
                case FSTORE_1       :
                case FSTORE_2       :
                case FSTORE_3       : storeLocal(JavaKind.Float, opcode - FSTORE_0); break;
                case DSTORE_0       :
                case DSTORE_1       :
                case DSTORE_2       :
                case DSTORE_3       : storeLocal(JavaKind.Double, opcode - DSTORE_0); break;
                case ASTORE_0       :
                case ASTORE_1       :
                case ASTORE_2       :
                case ASTORE_3       : storeLocal(JavaKind.Object, opcode - ASTORE_0); break;
                case IASTORE        : genStoreIndexed(JavaKind.Int   ); break;
                case LASTORE        : genStoreIndexed(JavaKind.Long  ); break;
                case FASTORE        : genStoreIndexed(JavaKind.Float ); break;
                case DASTORE        : genStoreIndexed(JavaKind.Double); break;
                case AASTORE        : genStoreIndexed(JavaKind.Object); break;
                case BASTORE        : genStoreIndexed(JavaKind.Byte  ); break;
                case CASTORE        : genStoreIndexed(JavaKind.Char  ); break;
                case SASTORE        : genStoreIndexed(JavaKind.Short ); break;
                case POP            :
                case POP2           :
                case DUP            :
                case DUP_X1         :
                case DUP_X2         :
                case DUP2           :
                case DUP2_X1        :
                case DUP2_X2        :
                case SWAP           : frameState.stackOp(opcode); break;
                case IADD           :
                case ISUB           :
                case IMUL           : genArithmeticOp(JavaKind.Int, opcode); break;
                case IDIV           :
                case IREM           : genIntegerDivOp(JavaKind.Int, opcode); break;
                case LADD           :
                case LSUB           :
                case LMUL           : genArithmeticOp(JavaKind.Long, opcode); break;
                case LDIV           :
                case LREM           : genIntegerDivOp(JavaKind.Long, opcode); break;
                case FADD           :
                case FSUB           :
                case FMUL           :
                case FDIV           :
                case FREM           : genArithmeticOp(JavaKind.Float, opcode); break;
                case DADD           :
                case DSUB           :
                case DMUL           :
                case DDIV           :
                case DREM           : genArithmeticOp(JavaKind.Double, opcode); break;
                case INEG           : genNegateOp(JavaKind.Int); break;
                case LNEG           : genNegateOp(JavaKind.Long); break;
                case FNEG           : genNegateOp(JavaKind.Float); break;
                case DNEG           : genNegateOp(JavaKind.Double); break;
                case ISHL           :
                case ISHR           :
                case IUSHR          : genShiftOp(JavaKind.Int, opcode); break;
                case IAND           :
                case IOR            :
                case IXOR           : genLogicOp(JavaKind.Int, opcode); break;
                case LSHL           :
                case LSHR           :
                case LUSHR          : genShiftOp(JavaKind.Long, opcode); break;
                case LAND           :
                case LOR            :
                case LXOR           : genLogicOp(JavaKind.Long, opcode); break;
                case IINC           : genIncrement(); break;
                case I2F            : genFloatConvert(FloatConvert.I2F, JavaKind.Int, JavaKind.Float); break;
                case I2D            : genFloatConvert(FloatConvert.I2D, JavaKind.Int, JavaKind.Double); break;
                case L2F            : genFloatConvert(FloatConvert.L2F, JavaKind.Long, JavaKind.Float); break;
                case L2D            : genFloatConvert(FloatConvert.L2D, JavaKind.Long, JavaKind.Double); break;
                case F2I            : genFloatConvert(FloatConvert.F2I, JavaKind.Float, JavaKind.Int); break;
                case F2L            : genFloatConvert(FloatConvert.F2L, JavaKind.Float, JavaKind.Long); break;
                case F2D            : genFloatConvert(FloatConvert.F2D, JavaKind.Float, JavaKind.Double); break;
                case D2I            : genFloatConvert(FloatConvert.D2I, JavaKind.Double, JavaKind.Int); break;
                case D2L            : genFloatConvert(FloatConvert.D2L, JavaKind.Double, JavaKind.Long); break;
                case D2F            : genFloatConvert(FloatConvert.D2F, JavaKind.Double, JavaKind.Float); break;
                case L2I            : genNarrow(JavaKind.Long, JavaKind.Int); break;
                case I2L            : genSignExtend(JavaKind.Int, JavaKind.Long); break;
                case I2B            : genSignExtend(JavaKind.Byte, JavaKind.Int); break;
                case I2S            : genSignExtend(JavaKind.Short, JavaKind.Int); break;
                case I2C            : genZeroExtend(JavaKind.Char, JavaKind.Int); break;
                case LCMP           : genCompareOp(JavaKind.Long, false); break;
                case FCMPL          : genCompareOp(JavaKind.Float, true); break;
                case FCMPG          : genCompareOp(JavaKind.Float, false); break;
                case DCMPL          : genCompareOp(JavaKind.Double, true); break;
                case DCMPG          : genCompareOp(JavaKind.Double, false); break;
                case IFEQ           : genIfZero(Condition.EQ); break;
                case IFNE           : genIfZero(Condition.NE); break;
                case IFLT           : genIfZero(Condition.LT); break;
                case IFGE           : genIfZero(Condition.GE); break;
                case IFGT           : genIfZero(Condition.GT); break;
                case IFLE           : genIfZero(Condition.LE); break;
                case IF_ICMPEQ      : genIfSame(JavaKind.Int, Condition.EQ); break;
                case IF_ICMPNE      : genIfSame(JavaKind.Int, Condition.NE); break;
                case IF_ICMPLT      : genIfSame(JavaKind.Int, Condition.LT); break;
                case IF_ICMPGE      : genIfSame(JavaKind.Int, Condition.GE); break;
                case IF_ICMPGT      : genIfSame(JavaKind.Int, Condition.GT); break;
                case IF_ICMPLE      : genIfSame(JavaKind.Int, Condition.LE); break;
                case IF_ACMPEQ      : genIfSame(JavaKind.Object, Condition.EQ); break;
                case IF_ACMPNE      : genIfSame(JavaKind.Object, Condition.NE); break;
                case GOTO           : genGoto(); break;
                case JSR            : genJsr(stream.readBranchDest()); break;
                case RET            : genRet(stream.readLocalIndex()); break;
                case TABLESWITCH    : genSwitch(new BytecodeTableSwitch(getStream(), bci())); break;
                case LOOKUPSWITCH   : genSwitch(new BytecodeLookupSwitch(getStream(), bci())); break;
                case IRETURN        : genReturn(frameState.pop(JavaKind.Int), JavaKind.Int); break;
                case LRETURN        : genReturn(frameState.pop(JavaKind.Long), JavaKind.Long); break;
                case FRETURN        : genReturn(frameState.pop(JavaKind.Float), JavaKind.Float); break;
                case DRETURN        : genReturn(frameState.pop(JavaKind.Double), JavaKind.Double); break;
                case ARETURN        : genReturn(frameState.pop(JavaKind.Object), JavaKind.Object); break;
                case RETURN         : genReturn(null, JavaKind.Void); break;
                case GETSTATIC      : cpi = stream.readCPI(); genGetStatic(cpi, opcode); break;
                case PUTSTATIC      : cpi = stream.readCPI(); genPutStatic(cpi, opcode); break;
                case GETFIELD       : cpi = stream.readCPI(); genGetField(cpi, opcode); break;
                case PUTFIELD       : cpi = stream.readCPI(); genPutField(cpi, opcode); break;
                case INVOKEVIRTUAL  : cpi = stream.readCPI(); genInvokeVirtual(cpi, opcode); break;
                case INVOKESPECIAL  : cpi = stream.readCPI(); genInvokeSpecial(cpi, opcode); break;
                case INVOKESTATIC   : cpi = stream.readCPI(); genInvokeStatic(cpi, opcode); break;
                case INVOKEINTERFACE: cpi = stream.readCPI(); genInvokeInterface(cpi, opcode); break;
                case INVOKEDYNAMIC  : cpi = stream.readCPI4(); genInvokeDynamic(cpi, opcode); break;
                case NEW            : genNewInstance(stream.readCPI()); break;
                case NEWARRAY       : genNewPrimitiveArray(stream.readLocalIndex()); break;
                case ANEWARRAY      : genNewObjectArray(stream.readCPI()); break;
                case ARRAYLENGTH    : genArrayLength(); break;
                case ATHROW         : genThrow(); break;
                case CHECKCAST      : genCheckCast(stream.readCPI()); break;
                case INSTANCEOF     : genInstanceOf(stream.readCPI()); break;
                case MONITORENTER   : genMonitorEnter(frameState.pop(JavaKind.Object), stream.nextBCI()); break;
                case MONITOREXIT    : genMonitorExit(frameState.pop(JavaKind.Object), null, stream.nextBCI()); break;
                case MULTIANEWARRAY : genNewMultiArray(stream.readCPI()); break;
                case IFNULL         : genIfNull(Condition.EQ); break;
                case IFNONNULL      : genIfNull(Condition.NE); break;
                case GOTO_W         : genGoto(); break;
                case JSR_W          : genJsr(stream.readBranchDest()); break;
                case BREAKPOINT     : throw new PermanentBailoutException("concurrent setting of breakpoint");
                default             : throw new PermanentBailoutException("Unsupported opcode %d (%s) [bci=%d]", opcode, nameOf(opcode), bci);
            }
            // @formatter:on
            // Checkstyle: resume
        }
        }
    }
}
