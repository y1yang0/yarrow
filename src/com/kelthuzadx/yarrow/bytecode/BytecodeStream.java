package com.kelthuzadx.yarrow.bytecode;

import com.kelthuzadx.yarrow.util.Assert;
import com.kelthuzadx.yarrow.util.Logger;

import java.util.Iterator;

import static com.kelthuzadx.yarrow.bytecode.Bytecode.*;

public class BytecodeStream implements Iterator<Integer> {
    private final int codeSize;
    private final byte[] code;
    private int bci;
    private int nextBci;
    private boolean isWide;
    private int data;
    private String bcString;

    public BytecodeStream(byte[] code, int codeSize) {
        assert code.length == codeSize : "bytecode array has unexpected length";
        this.codeSize = codeSize;
        this.code = code;
        reset(0);
    }

    @Override
    public boolean hasNext() {
        return codeSize != nextBci;
    }

    // consume current bytecode and return next byte code index
    @Override
    public Integer next() {
        isWide = false;
        bci = nextBci;
        int c = code[bci] & 0xff;

        StringBuilder sb = new StringBuilder();
        sb.append(bci).append(":").append(Bytecode.getBytecodeName(c));
        switch (c) {
            case NOP:
            case ACONST_NULL:
            case ICONST_M1:
            case ICONST_0:
            case ICONST_1:
            case ICONST_2:
            case ICONST_3:
            case ICONST_4:
            case ICONST_5:
            case LCONST_0:
            case LCONST_1:
            case FCONST_0:
            case FCONST_1:
            case FCONST_2:
            case DCONST_0:
            case DCONST_1:
            case ILOAD_0:
            case ILOAD_1:
            case ILOAD_2:
            case ILOAD_3:
            case LLOAD_0:
            case LLOAD_1:
            case LLOAD_2:
            case LLOAD_3:
            case FLOAD_0:
            case FLOAD_1:
            case FLOAD_2:
            case FLOAD_3:
            case DLOAD_0:
            case DLOAD_1:
            case DLOAD_2:
            case DLOAD_3:
            case ALOAD_0:
            case ALOAD_1:
            case ALOAD_2:
            case ALOAD_3:
            case ISTORE_0:
            case ISTORE_1:
            case ISTORE_2:
            case ISTORE_3:
            case LSTORE_0:
            case LSTORE_1:
            case LSTORE_2:
            case LSTORE_3:
            case FSTORE_0:
            case FSTORE_1:
            case FSTORE_2:
            case FSTORE_3:
            case DSTORE_0:
            case DSTORE_1:
            case DSTORE_2:
            case DSTORE_3:
            case ASTORE_0:
            case ASTORE_1:
            case ASTORE_2:
            case ASTORE_3:
            case IALOAD:
            case LALOAD:
            case FALOAD:
            case DALOAD:
            case AALOAD:
            case BALOAD:
            case CALOAD:
            case SALOAD:
            case IASTORE:
            case LASTORE:
            case FASTORE:
            case DASTORE:
            case AASTORE:
            case BASTORE:
            case CASTORE:
            case SASTORE:
            case POP:
            case POP2:
            case DUP:
            case DUP_X1:
            case DUP_X2:
            case DUP2:
            case DUP2_X1:
            case DUP2_X2:
            case SWAP:
            case IADD:
            case LADD:
            case FADD:
            case DADD:
            case ISUB:
            case LSUB:
            case FSUB:
            case DSUB:
            case IMUL:
            case LMUL:
            case FMUL:
            case DMUL:
            case IDIV:
            case LDIV:
            case FDIV:
            case DDIV:
            case IREM:
            case LREM:
            case FREM:
            case DREM:
            case INEG:
            case LNEG:
            case FNEG:
            case DNEG:
            case ISHL:
            case LSHL:
            case ISHR:
            case LSHR:
            case IUSHR:
            case LUSHR:
            case IAND:
            case LAND:
            case IOR:
            case LOR:
            case IXOR:
            case LXOR:
            case I2L:
            case I2F:
            case I2D:
            case L2I:
            case L2F:
            case L2D:
            case F2I:
            case F2L:
            case F2D:
            case D2I:
            case D2L:
            case D2F:
            case I2B:
            case I2C:
            case I2S:
            case LCMP:
            case FCMPL:
            case FCMPG:
            case DCMPL:
            case DCMPG:
            case IRETURN:
            case LRETURN:
            case FRETURN:
            case DRETURN:
            case ARETURN:
            case RETURN:
            case ARRAYLENGTH:
            case ATHROW:
            case MONITORENTER:
            case MONITOREXIT:
                nextBci = bci + 1;
                break;
            case BIPUSH:
            case LDC:
            case ILOAD:
            case LLOAD:
            case FLOAD:
            case DLOAD:
            case ALOAD:
            case ISTORE:
            case LSTORE:
            case FSTORE:
            case DSTORE:
            case ASTORE:
            case RET:
            case NEWARRAY:
                data = code[bci + 1];
                sb.append(" ").append(data);
                nextBci = bci + 2;
                break;
            case SIPUSH:
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
            case GOTO:
            case JSR:
            case GETSTATIC:
            case PUTSTATIC:
            case GETFIELD:
            case PUTFIELD:
            case NEW:
            case ANEWARRAY:
            case CHECKCAST:
            case INSTANCEOF:
            case IFNULL:
            case IFNONNULL:
            case IINC: {
                if (c == LDC_W || c == LDC2_W) {
                    data = readU2(bci + 1);
                } else {
                    data = readS2(bci + 1);
                }
                sb.append(" ").append(data);
                nextBci = bci + 3;
                break;
            }
            case GOTO_W:
            case JSR_W:
                data = readS4(bci + 1);
                sb.append(" ").append(data);
                nextBci = bci + 5;
                break;
            case TABLESWITCH:
                TableSwitch ts = new TableSwitch();
                nextBci = bci + ts.align() + 12 + ts.getNumOfCase() * 4;
                break;
            case LOOKUPSWITCH:
                LookupSwitch ls = new LookupSwitch();
                nextBci = bci + ls.align() + 8 + ls.getNumOfCase() * 8;
                break;
            case INVOKEVIRTUAL:
            case INVOKESPECIAL:
            case INVOKESTATIC:
                nextBci = bci + 3;
                break;
            case INVOKEINTERFACE:
            case INVOKEDYNAMIC:
                nextBci = bci + 5;
                break;
            case WIDE:
                isWide = true;
                bci = bci + 1;
                sb.append(" ").append(code[bci]);
                if (code[bci] == IINC) {
                    nextBci = bci + 5;
                } else {
                    data = readU2(bci + 1);
                    sb.append(" ").append(data);
                    nextBci = bci + 3;
                }
                break;
            case MULTIANEWARRAY:
                nextBci = bci + 4;
                break;
            case BREAKPOINT:
            case ILLEGAL:
            case END:
            default:
                Logger.error("Incorrect bytecode was fed");
        }
        this.bcString = sb.toString();
        return bci;
    }

    public void reset(int bci) {
        this.bci = bci;
        this.nextBci = bci;
        this.isWide = false;
        this.data = Integer.MIN_VALUE;
    }

    public boolean isWide() {
        return isWide;
    }

    public int getBytecodeData() {
        return data;
    }

    public int currentBytecode() {
        return code[bci] & 0xff;
    }

    public int peekNextBci() {
        return nextBci;
    }

    public String getCurrentBytecodeString() {
        return bcString;
    }

    private int readS2(int i) {
        return (code[i] << 8) | (code[i + 1] & 0xff);
    }

    private int readU2(int i) {
        return ((code[i] & 0xff) << 8) | (code[i + 1] & 0xff);
    }

    private int readS4(int i) {
        return (code[i] << 24) | ((code[i + 1] & 0xff) << 16) | ((code[i + 2] & 0xff) << 8) | (code[i + 3] & 0xff);
    }

    public IINC getIINC() {
        Assert.matchInt(code[bci],IINC);
        return new IINC();
    }

    public TableSwitch getTableSwitch() {
        Assert.matchInt(code[bci],TABLESWITCH);
        return new TableSwitch();
    }

    public LookupSwitch getLookupSwitch() {
        Assert.matchInt(code[bci],LOOKUPSWITCH);
        return new LookupSwitch();
    }

    public InvokeDynamic getInvokeDynamic() {
        Assert.matchInt(code[bci],INVOKEDYNAMIC);
        return new InvokeDynamic();
    }

    public InvokeInterface getInvokeInterface() {
        Assert.matchInt(code[bci],INVOKEINTERFACE);
        return new InvokeInterface();
    }

    public InvokeVirtual getInvokeVirtual() {
        Assert.matchInt(code[bci],INVOKEVIRTUAL);
        return new InvokeVirtual();
    }

    public InvokeSpecial getInvokeSpecial() {
        Assert.matchInt(code[bci],INVOKESPECIAL);
        return new InvokeSpecial();
    }

    public InvokeStatic getInvokeStatic() {
        return new InvokeStatic();
    }

    public MultiNewArray getMultiNewArray(){
        return new MultiNewArray();
    }


    public final class IINC {
        public int getIncrementIndex() {
            if (isWide) {
                return readU2(bci + 1);
            } else {
                return code[bci + 1];
            }
        }

        public int getIncrementConst() {
            if (isWide) {
                return readU2(bci + 3);
            } else {
                return code[bci + 2];
            }
        }

    }

    public final class TableSwitch {
        public int align() {
            return (4 - bci % 4) % 4;
        }

        public int getDefaultDest() {
            return readS4(bci + align());
        }

        public int getNumOfCase() {
            return getHighKey() - getLowKey() + 1;
        }

        public int getHighKey() {
            return readS4(bci + align() + 8);
        }

        public int getLowKey() {
            return readS4(bci + align() + 4);
        }

        public int getKeyDest(int index) {
            return readS4(bci + align() + 12 + index * 4);
        }
    }

    public final class LookupSwitch {
        public int align() {
            return (4 - bci % 4) % 4;
        }

        public int getDefaultDest() {
            int a = align();
            int r = bci + align();
            int x = readS4(r);
            return readS4(bci + align());
        }

        public int getNumOfCase() {
            return readS4(bci + align() + 4);
        }

        public int getMatch(int index) {
            return readS4(bci + align() + 8 + index * 8);
        }

        public int getOffset(int index) {
            return readS4(bci + align() + 8 + index * 8 + 4);
        }
    }

    public final class InvokeDynamic {
        public int getConstPoolIndex() {
            return readS2(bci + 1);
        }
    }

    public final class InvokeInterface {
        public int getConstPoolIndex() {
            return readS2(bci + 1);
        }

        public int getCount() {
            return code[bci + 3];
        }
    }

    public final class InvokeVirtual {
        public int getConstPoolIndex() {
            return readS2(bci + 1);
        }
    }

    public final class InvokeSpecial {
        public int getConstPoolIndex() {
            return readS2(bci + 1);
        }
    }

    public final class InvokeStatic {
        public int getConstPoolIndex() {
            return readS2(bci + 1);
        }
    }

    public final class MultiNewArray {
        public int getConstPoolIndex() {
            return readU2(bci + 1);
        }

        public int getDimension() {
            return code[bci + 3];
        }
    }

}
