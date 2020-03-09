package com.kelthuzadx.yarrow.ir;

import com.kelthuzadx.yarrow.bytecode.Bytecode;
import com.kelthuzadx.yarrow.bytecode.BytecodeStream;
import com.kelthuzadx.yarrow.core.YarrowError;
import com.kelthuzadx.yarrow.ir.hir.*;
import com.kelthuzadx.yarrow.ir.value.Value;
import com.kelthuzadx.yarrow.ir.value.ValueType;
import com.kelthuzadx.yarrow.util.Errors;
import jdk.vm.ci.hotspot.HotSpotResolvedJavaMethod;
import jdk.vm.ci.meta.JavaConstant;
import jdk.vm.ci.meta.JavaType;

import java.util.*;

public class HirBuilder {
    private HotSpotResolvedJavaMethod method;

    private Instruction lastInstr;

    public HirBuilder(HotSpotResolvedJavaMethod method) {
        this.method = method;
        this.lastInstr = null;
    }

    public HirBuilder build() {
        // construct cfg and detect reducible loops
        CFG cfg = CFG.build(method);

        // doing abstract interpretation by iterating all blocks in CFG
        Map<Integer,Boolean> visit = new HashMap<>(cfg.getBlocks().length);
        Queue<BlockStartInstr> workList = new ArrayDeque<>();
        workList.add(cfg.getEntryBlock());
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
                case Bytecode::_iadd           : arithmetic_op(intType   , code); break;
                case Bytecode::_ladd           : arithmetic_op(longType  , code); break;
                case Bytecode::_fadd           : arithmetic_op(floatType , code); break;
                case Bytecode::_dadd           : arithmetic_op(doubleType, code); break;
                case Bytecode::_isub           : arithmetic_op(intType   , code); break;
                case Bytecode::_lsub           : arithmetic_op(longType  , code); break;
                case Bytecode::_fsub           : arithmetic_op(floatType , code); break;
                case Bytecode::_dsub           : arithmetic_op(doubleType, code); break;
                case Bytecode::_imul           : arithmetic_op(intType   , code); break;
                case Bytecode::_lmul           : arithmetic_op(longType  , code); break;
                case Bytecode::_fmul           : arithmetic_op(floatType , code); break;
                case Bytecode::_dmul           : arithmetic_op(doubleType, code); break;
                case Bytecode::_idiv           : arithmetic_op(intType   , code, copy_state_for_exception()); break;
                case Bytecode::_ldiv           : arithmetic_op(longType  , code, copy_state_for_exception()); break;
                case Bytecode::_fdiv           : arithmetic_op(floatType , code); break;
                case Bytecode::_ddiv           : arithmetic_op(doubleType, code); break;
                case Bytecode::_irem           : arithmetic_op(intType   , code, copy_state_for_exception()); break;
                case Bytecode::_lrem           : arithmetic_op(longType  , code, copy_state_for_exception()); break;
                case Bytecode::_frem           : arithmetic_op(floatType , code); break;
                case Bytecode::_drem           : arithmetic_op(doubleType, code); break;
                case Bytecode::_ineg           : negate_op(intType   ); break;
                case Bytecode::_lneg           : negate_op(longType  ); break;
                case Bytecode::_fneg           : negate_op(floatType ); break;
                case Bytecode::_dneg           : negate_op(doubleType); break;
                case Bytecode::_ishl           : shift_op(intType , code); break;
                case Bytecode::_lshl           : shift_op(longType, code); break;
                case Bytecode::_ishr           : shift_op(intType , code); break;
                case Bytecode::_lshr           : shift_op(longType, code); break;
                case Bytecode::_iushr          : shift_op(intType , code); break;
                case Bytecode::_lushr          : shift_op(longType, code); break;
                case Bytecode::_iand           : logic_op(intType , code); break;
                case Bytecode::_land           : logic_op(longType, code); break;
                case Bytecode::_ior            : logic_op(intType , code); break;
                case Bytecode::_lor            : logic_op(longType, code); break;
                case Bytecode::_ixor           : logic_op(intType , code); break;
                case Bytecode::_lxor           : logic_op(longType, code); break;
                case Bytecode::_iinc           : increment(); break;
                case Bytecode::_i2l            : convert(code, T_INT   , T_LONG  ); break;
                case Bytecode::_i2f            : convert(code, T_INT   , T_FLOAT ); break;
                case Bytecode::_i2d            : convert(code, T_INT   , T_DOUBLE); break;
                case Bytecode::_l2i            : convert(code, T_LONG  , T_INT   ); break;
                case Bytecode::_l2f            : convert(code, T_LONG  , T_FLOAT ); break;
                case Bytecode::_l2d            : convert(code, T_LONG  , T_DOUBLE); break;
                case Bytecode::_f2i            : convert(code, T_FLOAT , T_INT   ); break;
                case Bytecode::_f2l            : convert(code, T_FLOAT , T_LONG  ); break;
                case Bytecode::_f2d            : convert(code, T_FLOAT , T_DOUBLE); break;
                case Bytecode::_d2i            : convert(code, T_DOUBLE, T_INT   ); break;
                case Bytecode::_d2l            : convert(code, T_DOUBLE, T_LONG  ); break;
                case Bytecode::_d2f            : convert(code, T_DOUBLE, T_FLOAT ); break;
                case Bytecode::_i2b            : convert(code, T_INT   , T_BYTE  ); break;
                case Bytecode::_i2c            : convert(code, T_INT   , T_CHAR  ); break;
                case Bytecode::_i2s            : convert(code, T_INT   , T_SHORT ); break;
                case Bytecode::_lcmp           : compare_op(longType  , code); break;
                case Bytecode::_fcmpl          : compare_op(floatType , code); break;
                case Bytecode::_fcmpg          : compare_op(floatType , code); break;
                case Bytecode::_dcmpl          : compare_op(doubleType, code); break;
                case Bytecode::_dcmpg          : compare_op(doubleType, code); break;
                case Bytecode::_ifeq           : if_zero(intType   , If::eql); break;
                case Bytecode::_ifne           : if_zero(intType   , If::neq); break;
                case Bytecode::_iflt           : if_zero(intType   , If::lss); break;
                case Bytecode::_ifge           : if_zero(intType   , If::geq); break;
                case Bytecode::_ifgt           : if_zero(intType   , If::gtr); break;
                case Bytecode::_ifle           : if_zero(intType   , If::leq); break;
                case Bytecode::_if_icmpeq      : if_same(intType   , If::eql); break;
                case Bytecode::_if_icmpne      : if_same(intType   , If::neq); break;
                case Bytecode::_if_icmplt      : if_same(intType   , If::lss); break;
                case Bytecode::_if_icmpge      : if_same(intType   , If::geq); break;
                case Bytecode::_if_icmpgt      : if_same(intType   , If::gtr); break;
                case Bytecode::_if_icmple      : if_same(intType   , If::leq); break;
                case Bytecode::_if_acmpeq      : if_same(objectType, If::eql); break;
                case Bytecode::_if_acmpne      : if_same(objectType, If::neq); break;
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
                case Bytecode::_ifnull         : if_null(objectType, If::eql); break;
                case Bytecode::_ifnonnull      : if_null(objectType, If::neq); break;
                case Bytecode::_goto_w         : _goto(s.cur_bci(), s.get_far_dest()); break;
                case Bytecode::_jsr_w          : jsr(s.get_far_dest()); break;
                case Bytecode.BREAKPOINT       :
                default                        : throw new YarrowError("Should not reach here"); break;
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
                Errors.shouldNotReachHere();
        }
    }

    private void swap(VmState state){
        Instruction temp = state.pop();
        Instruction temp1 = state.pop();
        state.push(temp);
        state.push(temp1);
    }

}






















