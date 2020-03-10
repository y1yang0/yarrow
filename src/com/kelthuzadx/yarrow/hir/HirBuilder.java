package com.kelthuzadx.yarrow.hir;

import com.kelthuzadx.yarrow.bytecode.Bytecode;
import com.kelthuzadx.yarrow.bytecode.BytecodeStream;
import com.kelthuzadx.yarrow.core.YarrowError;
import com.kelthuzadx.yarrow.hir.instr.*;
import com.kelthuzadx.yarrow.hir.value.Cond;
import com.kelthuzadx.yarrow.hir.value.Value;
import com.kelthuzadx.yarrow.hir.value.ValueType;
import com.kelthuzadx.yarrow.util.CompilerErrors;
import jdk.vm.ci.hotspot.HotSpotResolvedJavaMethod;
import jdk.vm.ci.meta.JavaConstant;
import jdk.vm.ci.meta.JavaType;

import java.util.*;


/**
 * HirBuilder performs an abstract interpretation, it would transform java bytecode to compiler HIR.
 */
public class HirBuilder {
    private HotSpotResolvedJavaMethod method;

    private CFG cfg;

    private Instruction lastInstr;

    public HirBuilder(HotSpotResolvedJavaMethod method) {
        this.method = method;
        this.lastInstr = null;
    }

    public HirBuilder build() {
        // construct cfg and detect reducible loops
        cfg = CFG.build(method);

        // doing abstract interpretation by iterating all blocks in CFG
        Map<Integer,Boolean> visit = new HashMap<>(cfg.getBlocks().length);
        Queue<BlockStartInstr> workList = new ArrayDeque<>();
        workList.add(cfg.blockContain(0));
        while (!workList.isEmpty()){
            BlockStartInstr blockStart = workList.remove();
            if(!visit.containsKey(blockStart.getBlockId())){
                visit.put(blockStart.getBlockId(),true);
                VmState state = new VmState();
                lastInstr = blockStart;
                fulfillBlock(state, blockStart);
            }
        }
        return this;
    }

    private void fulfillBlock(VmState state, BlockStartInstr block){
        BytecodeStream bs =new BytecodeStream(method.getCode(),method.getCodeSize());
        bs.reset(block.getStartBci());
        while (bs.hasNext()){
            int opcode = bs.next();
            switch (opcode) {
                case Bytecode.NOP: break;
                case Bytecode.ACONST_NULL:loadConst(state,ValueType.Object,null);break;
                case Bytecode.ICONST_M1:loadConst(state,ValueType.Int,-1);break;
                case Bytecode.ICONST_0:loadConst(state,ValueType.Int,0);break;
                case Bytecode.ICONST_1:loadConst(state,ValueType.Int,1);break;
                case Bytecode.ICONST_2:loadConst(state,ValueType.Int,2);break;
                case Bytecode.ICONST_3:loadConst(state,ValueType.Int,3);break;
                case Bytecode.ICONST_4:loadConst(state,ValueType.Int,4);break;
                case Bytecode.ICONST_5:loadConst(state,ValueType.Int,5);break;
                case Bytecode.LCONST_0:loadConst(state,ValueType.Long,0L);break;
                case Bytecode.LCONST_1:loadConst(state,ValueType.Long,1L);break;
                case Bytecode.FCONST_0:loadConst(state,ValueType.Float,0.0f);break;
                case Bytecode.FCONST_1:loadConst(state,ValueType.Float,1.0f);break;
                case Bytecode.FCONST_2:loadConst(state,ValueType.Float,2.0f);break;
                case Bytecode.DCONST_0:loadConst(state,ValueType.Double,0.0d);break;
                case Bytecode.DCONST_1:loadConst(state,ValueType.Double,1.0d);break;
                case Bytecode.BIPUSH:loadConst(state,ValueType.Int,(byte)bs.getBytecodeData());break;
                case Bytecode.SIPUSH:loadConst(state,ValueType.Int,(short)bs.getBytecodeData());break;
                case Bytecode.LDC:
                case Bytecode.LDC_W:
                case Bytecode.LDC2_W:ldc(state,bs.getBytecodeData());break;
                case Bytecode.ILOAD:load(state,ValueType.Int,bs.getBytecodeData());break;
                case Bytecode.LLOAD:load(state,ValueType.Long,bs.getBytecodeData());break;
                case Bytecode.FLOAD:load(state,ValueType.Float,bs.getBytecodeData());break;
                case Bytecode.DLOAD:load(state,ValueType.Double,bs.getBytecodeData());break;
                case Bytecode.ALOAD:load(state,ValueType.Object,bs.getBytecodeData());break;
                case Bytecode.ILOAD_0:load(state,ValueType.Int,0);break;
                case Bytecode.ILOAD_1:load(state,ValueType.Int,1);break;
                case Bytecode.ILOAD_2:load(state,ValueType.Int,2);break;
                case Bytecode.ILOAD_3:load(state,ValueType.Int,3);break;
                case Bytecode.LLOAD_0:load(state,ValueType.Long,0);break;
                case Bytecode.LLOAD_1:load(state,ValueType.Long,1);break;
                case Bytecode.LLOAD_2:load(state,ValueType.Long,2);break;
                case Bytecode.LLOAD_3:load(state,ValueType.Long,3);break;
                case Bytecode.FLOAD_0:load(state,ValueType.Float,0);break;
                case Bytecode.FLOAD_1:load(state,ValueType.Float,1);break;
                case Bytecode.FLOAD_2:load(state,ValueType.Float,2);break;
                case Bytecode.FLOAD_3:load(state,ValueType.Float,3);break;
                case Bytecode.DLOAD_0:load(state,ValueType.Double,0);break;
                case Bytecode.DLOAD_1:load(state,ValueType.Double,1);break;
                case Bytecode.DLOAD_2:load(state,ValueType.Double,2);break;
                case Bytecode.DLOAD_3:load(state,ValueType.Double,3);break;
                case Bytecode.ALOAD_0:load(state,ValueType.Object,0);break;
                case Bytecode.ALOAD_1:load(state,ValueType.Object,1);break;
                case Bytecode.ALOAD_2:load(state,ValueType.Object,2);break;
                case Bytecode.ALOAD_3:load(state,ValueType.Object,3);break;
                case Bytecode.IALOAD:loadArray(state,ValueType.Int);break;
                case Bytecode.LALOAD:loadArray(state,ValueType.Long);break;
                case Bytecode.FALOAD:loadArray(state,ValueType.Float);break;
                case Bytecode.DALOAD:loadArray(state,ValueType.Double);break;
                case Bytecode.AALOAD:loadArray(state,ValueType.Object);break;
                case Bytecode.BALOAD:loadArray(state,ValueType.Byte);break;
                case Bytecode.CALOAD:loadArray(state,ValueType.Char);break;
                case Bytecode.SALOAD:loadArray(state,ValueType.Short);break;
                case Bytecode.ISTORE:store(state,ValueType.Int,bs.getBytecodeData());break;
                case Bytecode.LSTORE:store(state,ValueType.Long,bs.getBytecodeData());break;
                case Bytecode.FSTORE:store(state,ValueType.Float,bs.getBytecodeData());break;
                case Bytecode.DSTORE:store(state,ValueType.Double,bs.getBytecodeData());break;
                case Bytecode.ASTORE:store(state,ValueType.Object,bs.getBytecodeData());break;
                case Bytecode.ISTORE_0:store(state,ValueType.Int,0);break;
                case Bytecode.ISTORE_1:store(state,ValueType.Int,1);break;
                case Bytecode.ISTORE_2:store(state,ValueType.Int,2);break;
                case Bytecode.ISTORE_3:store(state,ValueType.Int,3);break;
                case Bytecode.LSTORE_0:store(state,ValueType.Long,0);break;
                case Bytecode.LSTORE_1:store(state,ValueType.Long,1);break;
                case Bytecode.LSTORE_2:store(state,ValueType.Long,2);break;
                case Bytecode.LSTORE_3:store(state,ValueType.Long,3);break;
                case Bytecode.FSTORE_0:store(state,ValueType.Float,0);break;
                case Bytecode.FSTORE_1:store(state,ValueType.Float,1);break;
                case Bytecode.FSTORE_2:store(state,ValueType.Float,2);break;
                case Bytecode.FSTORE_3:store(state,ValueType.Float,3);break;
                case Bytecode.DSTORE_0:store(state,ValueType.Double,0);break;
                case Bytecode.DSTORE_1:store(state,ValueType.Double,1);break;
                case Bytecode.DSTORE_2:store(state,ValueType.Double,2);break;
                case Bytecode.DSTORE_3:store(state,ValueType.Double,3);break;
                case Bytecode.ASTORE_0:store(state,ValueType.Object,0);break;
                case Bytecode.ASTORE_1:store(state,ValueType.Object,1);break;
                case Bytecode.ASTORE_2:store(state,ValueType.Object,2);break;
                case Bytecode.ASTORE_3:store(state,ValueType.Object,3);break;
                case Bytecode.IASTORE:storeArray(state,ValueType.Int);break;
                case Bytecode.LASTORE:storeArray(state,ValueType.Long);break;
                case Bytecode.FASTORE:storeArray(state,ValueType.Float);break;
                case Bytecode.DASTORE:storeArray(state,ValueType.Double);break;
                case Bytecode.AASTORE:storeArray(state,ValueType.Object);break;
                case Bytecode.BASTORE:storeArray(state,ValueType.Byte);break;
                case Bytecode.CASTORE:storeArray(state,ValueType.Char);break;
                case Bytecode.SASTORE:storeArray(state,ValueType.Short);break;
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
                case Bytecode.IREM:arithmetic(state,ValueType.Int,opcode);break;
                case Bytecode.LADD:
                case Bytecode.LSUB:
                case Bytecode.LMUL:
                case Bytecode.LDIV:
                case Bytecode.LREM:arithmetic(state,ValueType.Long,opcode);break;
                case Bytecode.FADD:
                case Bytecode.FSUB:
                case Bytecode.FMUL:
                case Bytecode.FDIV:
                case Bytecode.FREM:arithmetic(state,ValueType.Float,opcode);break;
                case Bytecode.DADD:
                case Bytecode.DSUB:
                case Bytecode.DMUL:
                case Bytecode.DDIV:
                case Bytecode.DREM:arithmetic(state,ValueType.Double,opcode);break;
                case Bytecode.INEG:negate(state,ValueType.Int);break;
                case Bytecode.LNEG:negate(state,ValueType.Long);break;
                case Bytecode.FNEG:negate(state,ValueType.Float);break;
                case Bytecode.DNEG:negate(state,ValueType.Double);break;
                case Bytecode.ISHL:
                case Bytecode.ISHR:
                case Bytecode.IUSHR: shift(state,ValueType.Int,opcode);break;
                case Bytecode.LSHL:
                case Bytecode.LSHR:
                case Bytecode.LUSHR: shift(state,ValueType.Long,opcode);break;
                case Bytecode.IAND:
                case Bytecode.IOR:
                case Bytecode.IXOR:logic(state,ValueType.Int,opcode);break;
                case Bytecode.LAND:
                case Bytecode.LOR:
                case Bytecode.LXOR:logic(state,ValueType.Long,opcode);break;
                case Bytecode.IINC:increment(state,bs.getIINC());break;
                case Bytecode.I2L:typeCast(state,ValueType.Int,ValueType.Long,opcode);break;
                case Bytecode.I2F:typeCast(state,ValueType.Int,ValueType.Float,opcode);break;
                case Bytecode.I2D:typeCast(state,ValueType.Int,ValueType.Double,opcode);break;
                case Bytecode.L2I:typeCast(state,ValueType.Long,ValueType.Int,opcode);break;
                case Bytecode.L2F:typeCast(state,ValueType.Long,ValueType.Float,opcode);break;
                case Bytecode.L2D:typeCast(state,ValueType.Long,ValueType.Double,opcode);break;
                case Bytecode.F2I:typeCast(state,ValueType.Float,ValueType.Int,opcode);break;
                case Bytecode.F2L:typeCast(state,ValueType.Float,ValueType.Long,opcode);break;
                case Bytecode.F2D:typeCast(state,ValueType.Float,ValueType.Double,opcode);break;
                case Bytecode.D2I:typeCast(state,ValueType.Double,ValueType.Int,opcode);break;
                case Bytecode.D2L:typeCast(state,ValueType.Double,ValueType.Long,opcode);break;
                case Bytecode.D2F:typeCast(state,ValueType.Double,ValueType.Float,opcode);break;
                case Bytecode.I2B:typeCast(state,ValueType.Int,ValueType.Byte,opcode);break;
                case Bytecode.I2C:typeCast(state,ValueType.Int,ValueType.Char,opcode);break;
                case Bytecode.I2S:typeCast(state,ValueType.Int,ValueType.Short,opcode);break;
                case Bytecode.LCMP:compare(state,ValueType.Long,opcode);break;
                case Bytecode.FCMPL:
                case Bytecode.FCMPG:compare(state,ValueType.Float,opcode);break;
                case Bytecode.DCMPL:
                case Bytecode.DCMPG:compare(state,ValueType.Double,opcode);break;
                case Bytecode.IFEQ:branchIfZero(state,ValueType.Int,Cond.EQ,bs.getBytecodeData(),bs.peekNextBci());break;
                case Bytecode.IFNE:branchIfZero(state,ValueType.Int,Cond.NE,bs.getBytecodeData(),bs.peekNextBci());break;
                case Bytecode.IFLT:branchIfZero(state,ValueType.Int,Cond.LT,bs.getBytecodeData(),bs.peekNextBci());break;
                case Bytecode.IFGE:branchIfZero(state,ValueType.Int,Cond.GE,bs.getBytecodeData(),bs.peekNextBci());break;
                case Bytecode.IFGT:branchIfZero(state,ValueType.Int,Cond.GT,bs.getBytecodeData(),bs.peekNextBci());break;
                case Bytecode.IFLE:branchIfZero(state,ValueType.Int,Cond.LE,bs.getBytecodeData(),bs.peekNextBci());break;
                case Bytecode.IF_ICMPEQ:branchIfSame(state,ValueType.Int,Cond.EQ,bs.getBytecodeData(),bs.peekNextBci());break;
                case Bytecode.IF_ICMPNE:branchIfSame(state,ValueType.Int,Cond.NE,bs.getBytecodeData(),bs.peekNextBci());break;
                case Bytecode.IF_ICMPLT:branchIfSame(state,ValueType.Int,Cond.LT,bs.getBytecodeData(),bs.peekNextBci());break;
                case Bytecode.IF_ICMPGE:branchIfSame(state,ValueType.Int,Cond.GE,bs.getBytecodeData(),bs.peekNextBci());break;
                case Bytecode.IF_ICMPGT:branchIfSame(state,ValueType.Int,Cond.GT,bs.getBytecodeData(),bs.peekNextBci());break;
                case Bytecode.IF_ICMPLE:branchIfSame(state,ValueType.Int,Cond.LE,bs.getBytecodeData(),bs.peekNextBci());break;
                case Bytecode.IF_ACMPEQ:branchIfSame(state,ValueType.Object,Cond.EQ,bs.getBytecodeData(),bs.peekNextBci());break;
                case Bytecode.IF_ACMPNE:branchIfSame(state,ValueType.Object,Cond.NE,bs.getBytecodeData(),bs.peekNextBci());break;

                case Bytecode::_goto           : _goto(s.cur_bci(), s.get_dest()); break;
                case Bytecode::_jsr            : jsr(s.get_dest()); break;
                case Bytecode::_ret            : ret(s.get_index()); break;
                case Bytecode::_tableswitch    : table_switch(); break;
                case Bytecode::_lookupswitch   : lookup_switch(); break;
                case Bytecode::_ireturn        : method_return(ipop(), ignore_return); break;
                case Bytecode::_lreturn        : method_return(lpop(), ignore_return); break;
                case Bytecode::_freturn        : method_return(fpop(), ignore_return); break;
                case Bytecode::_dreturn        : method_return(dpop(), ignore_return); break;
                case Bytecode::_areturn        : method_return(apop(), ignore_return); break;
                case Bytecode::_return         : method_return(NULL  , ignore_return); break;
                case Bytecode::_getstatic      : // fall through
                case Bytecode::_putstatic      : // fall through
                case Bytecode::_getfield       : // fall through
                case Bytecode::_putfield       : access_field(code); break;
                case Bytecode::_invokevirtual  : // fall through
                case Bytecode::_invokespecial  : // fall through
                case Bytecode::_invokestatic   : // fall through
                case Bytecode::_invokedynamic  : // fall through
                case Bytecode::_invokeinterface: invoke(code); break;
                case Bytecode::_new            : new_instance(s.get_index_u2()); break;
                case Bytecode::_newarray       : new_type_array(); break;
                case Bytecode::_anewarray      : new_object_array(); break;
                case Bytecode::_arraylength    : { ValueStack* state_before = copy_state_for_exception(); ipush(append(new ArrayLength(apop(), state_before))); break; }
                case Bytecode::_athrow         : throw_op(s.cur_bci()); break;
                case Bytecode::_checkcast      : check_cast(s.get_index_u2()); break;
                case Bytecode::_instanceof     : instance_of(s.get_index_u2()); break;
                case Bytecode::_monitorenter   : monitorenter(apop(), s.cur_bci()); break;
                case Bytecode::_monitorexit    : monitorexit (apop(), s.cur_bci()); break;
                case Bytecode::_wide           : ShouldNotReachHere(); break;
                case Bytecode::_multianewarray : new_multi_array(s.cur_bcp()[3]); break;
                case Bytecode.IFNULL:branchIfNull(state,ValueType.Object,Cond.EQ,bs.getBytecodeData(),bs.peekNextBci());break;
                case Bytecode.IFNONNULL:branchIfNull(state,ValueType.Object,Cond.NE,bs.getBytecodeData(),bs.peekNextBci());break;
                case Bytecode::_goto_w         : _goto(s.cur_bci(), s.get_far_dest()); break;
                case Bytecode::_jsr_w          : jsr(s.get_far_dest()); break;
                case Bytecode.BREAKPOINT       :
                default                        : CompilerErrors.shouldNotReachHere();
            }
        }
    }

    private void appendInstr(Instruction curInstr){
        lastInstr.setNext(curInstr);
        lastInstr = curInstr;
    }

    private <T> void loadConst(VmState state, ValueType type, T value){
        ConstantInstr instr = new ConstantInstr(new Value(type,value));
        appendInstr(instr);
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
                    temp = new Value(ValueType.Int,((JavaConstant)item).asInt());
                    break;
                case Long:
                    temp = new Value(ValueType.Long,((JavaConstant)item).asLong());
                    break;
                case Float:
                    temp = new Value(ValueType.Long,((JavaConstant)item).asFloat());
                    break;
                case Double:
                    temp = new Value(ValueType.Long,((JavaConstant)item).asDouble());
                    break;
                default:
                    throw new YarrowError("unexpect scenario");
            }
        }else if (item instanceof JavaType){
            item = new Value(ValueType.Array, item);
        }else{
            temp = new Value(ValueType.Object,item);
        }
        ConstantInstr instr = new ConstantInstr(temp);
        appendInstr(instr);
        state.push(instr);
    }

    private void load(VmState state, ValueType type, int index){
        Instruction temp = state.get(index);
        if(!temp.isType(ValueType.Int)){
            throw new YarrowError("type error");
        }
        state.push(temp);
    }

    private void loadArray(VmState state, ValueType type){
        Instruction index = state.pop();
        Instruction array = state.pop();
        if(!array.isType(ValueType.Array) || !index.isType(ValueType.Int)){
            throw new YarrowError("type error");
        }
        LoadIndexInstr instr = new LoadIndexInstr(array,index,null, type);
        appendInstr(instr);
        state.push(instr);
    }

    private void store(VmState state, ValueType type, int index){
        Instruction temp = state.pop();
        if(!temp.isType(ValueType.Int)){
            throw new YarrowError("type error");
        }
        state.set(index,temp);
    }

    private void storeArray(VmState state, ValueType type){
        Instruction value = state.pop();
        Instruction index = state.pop();
        Instruction array = state.pop();

        if(!index.isType(ValueType.Int) || !value.isType(type)){
            throw new YarrowError("type error");
        }

        StoreIndexInstr instr = new StoreIndexInstr(array,index,null,type,value);
        appendInstr(instr);
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

    private void arithmetic(VmState state, ValueType type,int opcode){
        Instruction right = state.pop();
        Instruction left = state.pop();
        if(!right.isType(type) || !right.isType(type)){
            throw new YarrowError("type error");
        }
        ArithmeticInstr instr = new ArithmeticInstr(opcode,left,right);
        appendInstr(instr);
        state.push(instr);
    }

    private void negate(VmState state, ValueType type){
        Instruction temp = state.pop();
        if(!temp.isType(type)){
            throw new YarrowError("type error");
        }
        NegateInstr instr = new NegateInstr(temp);
        appendInstr(instr);
        state.push(instr);
    }

    private void shift(VmState state, ValueType type, int opcode){
        Instruction right = state.pop();
        Instruction left = state.pop();
        if(!right.isType(ValueType.Int) || !left.isType(type)){
            throw new YarrowError("type error");
        }
        ShiftInstr instr = new ShiftInstr(opcode,left,right);
        appendInstr(instr);
        state.push(instr);
    }

    private void logic(VmState state, ValueType type, int opcode){
        Instruction right = state.pop();
        Instruction left = state.pop();
        if(!right.isType(type) || !left.isType(type)){
            throw new YarrowError("type error");
        }
        LogicInstr instr = new LogicInstr(opcode,left,right);
        appendInstr(instr);
        state.push(instr);
    }

    private void increment(VmState state, BytecodeStream.IINC iinc){
        // increment local variable by constant
        int index = iinc.getIncrementIndex();
        int constant = iinc.getIncrementConst();

        load(state,ValueType.Int,index);

        ConstantInstr instr = new ConstantInstr(new Value(ValueType.Int,constant));
        appendInstr(instr);
        state.push(instr);

        arithmetic(state,ValueType.Int,Bytecode.IADD);

        store(state,ValueType.Int,index);
    }

    private void typeCast(VmState state,  ValueType fromType, ValueType toType, int opcode){
        Instruction from = state.pop();
        if(!from.isType(fromType)){
            throw new YarrowError("type error");
        }
        ValueType t = toType;
        if(t==ValueType.Byte || t==ValueType.Char|| t==ValueType.Short){
            t = ValueType.Int;
        }
        TypeCastInstr instr = new TypeCastInstr(opcode,from,t);
        appendInstr(instr);
        state.push(instr);
    }

    private void compare(VmState state, ValueType type, int opcode){
        Instruction right = state.pop();
        Instruction left = state.pop();
        if(!right.isType(type) || !right.isType(type)){
            throw new YarrowError("type error");
        }
        CompareInstr instr = new CompareInstr(opcode,left,right);
        appendInstr(instr);
        state.push(instr);
    }

    private void branchIfZero(VmState state, ValueType type, Cond cond, int trueBci, int falseBci){
        Instruction left = state.pop();
        ConstantInstr right = new ConstantInstr(new Value(ValueType.Int,0));
        if(!left.isType(type)){
            throw new YarrowError("type error");
        }
        branchIf(state,left,right,cond,trueBci,falseBci);
    }

    private void branchIfNull(VmState state, ValueType type, Cond cond, int trueBci, int falseBci){
        Instruction left = state.pop();
        ConstantInstr right = new ConstantInstr(new Value(ValueType.Object,null));
        if(!left.isType(type)){
            throw new YarrowError("type error");
        }
        branchIf(state,left,right,cond,trueBci,falseBci);
    }

    private void branchIfSame(VmState state, ValueType type, Cond cond, int trueBci, int falseBci){
        Instruction left = state.pop();
        Instruction right = state.pop();
        if(!left.isType(type)||!right.isType(type)){
            throw new YarrowError("type error");
        }
        branchIf(state,left,right,cond,trueBci,falseBci);
    }

    private void branchIf(VmState state,Instruction left, Instruction right, Cond cond, int trueBci, int falseBci){
        ArrayList<BlockStartInstr> succ = new ArrayList<BlockStartInstr>(){{
           add(cfg.blockContain(trueBci));
           add(cfg.blockContain(falseBci));
        }};
        IfInstr instr = new IfInstr(succ,left,right,cond);
        appendInstr(instr);
    }
}






















