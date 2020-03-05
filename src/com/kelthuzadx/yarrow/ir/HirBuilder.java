package com.kelthuzadx.yarrow.ir;

import com.kelthuzadx.yarrow.bytecode.Bytecode;
import com.kelthuzadx.yarrow.bytecode.BytecodeStream;
import com.kelthuzadx.yarrow.core.YarrowError;
import com.kelthuzadx.yarrow.ir.hir.BlockStartInstr;
import com.kelthuzadx.yarrow.ir.hir.ConstantInstr;
import com.kelthuzadx.yarrow.ir.hir.Instruction;
import com.kelthuzadx.yarrow.ir.value.Value;
import com.kelthuzadx.yarrow.ir.value.ValueType;
import jdk.vm.ci.hotspot.HotSpotResolvedJavaMethod;
import jdk.vm.ci.meta.JavaConstant;
import jdk.vm.ci.meta.JavaKind;
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
                case Bytecode.NOP              : { break;}
                case Bytecode.ACONST_NULL    : {
                    ConstantInstr instr = new ConstantInstr(new Value(ValueType.Object,null));
                    appendInstr(instr);
                    state.push(instr);
                    break;
                }
                case Bytecode.ICONST_M1:{
                    ConstantInstr instr = new ConstantInstr(new Value(ValueType.Int,-1));
                    appendInstr(instr);
                    state.push(instr);
                    break;
                }
                case Bytecode.ICONST_0: {
                    ConstantInstr instr = new ConstantInstr(new Value(ValueType.Int,0));
                    appendInstr(instr);
                    state.push(instr);
                    break;
                }
                case Bytecode.ICONST_1:
                {
                    ConstantInstr instr = new ConstantInstr(new Value(ValueType.Int,1));
                    appendInstr(instr);
                    state.push(instr);
                    break;
                }
                case Bytecode.ICONST_2:{
                    ConstantInstr instr = new ConstantInstr(new Value(ValueType.Int,2));
                    appendInstr(instr);
                    state.push(instr);
                    break;
                }
                case Bytecode.ICONST_3:{
                    ConstantInstr instr = new ConstantInstr(new Value(ValueType.Int,3));
                    appendInstr(instr);
                    state.push(instr);
                    break;
                }
                case Bytecode.ICONST_4:{
                    ConstantInstr instr = new ConstantInstr(new Value(ValueType.Int,4));
                    appendInstr(instr);
                    state.push(instr);
                    break;
                }
                case Bytecode.ICONST_5:{
                    ConstantInstr instr = new ConstantInstr(new Value(ValueType.Int,5));
                    appendInstr(instr);
                    state.push(instr);
                    break;
                }
                case Bytecode.LCONST_0: {
                    ConstantInstr instr = new ConstantInstr(new Value(ValueType.Long,0f));
                    appendInstr(instr);
                    state.push(instr);
                    break;
                }
                case Bytecode.LCONST_1:
                {
                    ConstantInstr instr = new ConstantInstr(new Value(ValueType.Long,1f));
                    appendInstr(instr);
                    state.push(instr);
                    break;
                }
                case Bytecode.FCONST_0: {
                    ConstantInstr instr = new ConstantInstr(new Value(ValueType.Float,0.0f));
                    appendInstr(instr);
                    state.push(instr);
                    break;
                }
                case Bytecode.FCONST_1:
                {
                    ConstantInstr instr = new ConstantInstr(new Value(ValueType.Float,1.0f));
                    appendInstr(instr);
                    state.push(instr);
                    break;
                }
                case Bytecode.FCONST_2:{
                    ConstantInstr instr = new ConstantInstr(new Value(ValueType.Float,2.0f));
                    appendInstr(instr);
                    state.push(instr);
                    break;
                }
                case Bytecode.DCONST_0: {
                    ConstantInstr instr = new ConstantInstr(new Value(ValueType.Double,0.0d));
                    appendInstr(instr);
                    state.push(instr);
                    break;
                }
                case Bytecode.DCONST_1:
                {
                    ConstantInstr instr = new ConstantInstr(new Value(ValueType.Double,1.0d));
                    appendInstr(instr);
                    state.push(instr);
                    break;
                }
                case Bytecode.BIPUSH:{
                    Value temp = new Value(ValueType.Int,(byte)bs.getBytecodeData());
                    ConstantInstr instr = new ConstantInstr(temp);
                    appendInstr(instr);
                    state.push(instr);
                    break;
                }
                case Bytecode.SIPUSH:{
                    Value temp = new Value(ValueType.Int,(short)bs.getBytecodeData());
                    ConstantInstr instr = new ConstantInstr(temp);
                    appendInstr(instr);
                    state.push(instr);
                    break;
                }
                case Bytecode.LDC:
                case Bytecode.LDC_W:
                case Bytecode.LDC2_W:{
                    Value temp = null;
                    Object item = method.getConstantPool().lookupConstant(bs.getBytecodeData());
                    if(item instanceof JavaConstant){
                    }else if (item instanceof JavaType){
                        switch (((JavaType) item).getJavaKind()){
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
                    }else{
                        temp = new Value(ValueType.Object,item);
                    }
                    ConstantInstr instr = new ConstantInstr(temp);
                    appendInstr(instr);
                    state.push(instr);
                    break;
                }
                case Bytecode::_iload          : load_local(intType     , s.get_index()); break;
                case Bytecode::_lload          : load_local(longType    , s.get_index()); break;
                case Bytecode::_fload          : load_local(floatType   , s.get_index()); break;
                case Bytecode::_dload          : load_local(doubleType  , s.get_index()); break;
                case Bytecode::_aload          : load_local(instanceType, s.get_index()); break;
                case Bytecode::_iload_0        : load_local(intType   , 0); break;
                case Bytecode::_iload_1        : load_local(intType   , 1); break;
                case Bytecode::_iload_2        : load_local(intType   , 2); break;
                case Bytecode::_iload_3        : load_local(intType   , 3); break;
                case Bytecode::_lload_0        : load_local(longType  , 0); break;
                case Bytecode::_lload_1        : load_local(longType  , 1); break;
                case Bytecode::_lload_2        : load_local(longType  , 2); break;
                case Bytecode::_lload_3        : load_local(longType  , 3); break;
                case Bytecode::_fload_0        : load_local(floatType , 0); break;
                case Bytecode::_fload_1        : load_local(floatType , 1); break;
                case Bytecode::_fload_2        : load_local(floatType , 2); break;
                case Bytecode::_fload_3        : load_local(floatType , 3); break;
                case Bytecode::_dload_0        : load_local(doubleType, 0); break;
                case Bytecode::_dload_1        : load_local(doubleType, 1); break;
                case Bytecode::_dload_2        : load_local(doubleType, 2); break;
                case Bytecode::_dload_3        : load_local(doubleType, 3); break;
                case Bytecode::_aload_0        : load_local(objectType, 0); break;
                case Bytecode::_aload_1        : load_local(objectType, 1); break;
                case Bytecode::_aload_2        : load_local(objectType, 2); break;
                case Bytecode::_aload_3        : load_local(objectType, 3); break;
                case Bytecode::_iaload         : load_indexed(T_INT   ); break;
                case Bytecode::_laload         : load_indexed(T_LONG  ); break;
                case Bytecode::_faload         : load_indexed(T_FLOAT ); break;
                case Bytecode::_daload         : load_indexed(T_DOUBLE); break;
                case Bytecode::_aaload         : load_indexed(T_OBJECT); break;
                case Bytecode::_baload         : load_indexed(T_BYTE  ); break;
                case Bytecode::_caload         : load_indexed(T_CHAR  ); break;
                case Bytecode::_saload         : load_indexed(T_SHORT ); break;
                case Bytecode::_istore         : store_local(intType   , s.get_index()); break;
                case Bytecode::_lstore         : store_local(longType  , s.get_index()); break;
                case Bytecode::_fstore         : store_local(floatType , s.get_index()); break;
                case Bytecode::_dstore         : store_local(doubleType, s.get_index()); break;
                case Bytecode::_astore         : store_local(objectType, s.get_index()); break;
                case Bytecode::_istore_0       : store_local(intType   , 0); break;
                case Bytecode::_istore_1       : store_local(intType   , 1); break;
                case Bytecode::_istore_2       : store_local(intType   , 2); break;
                case Bytecode::_istore_3       : store_local(intType   , 3); break;
                case Bytecode::_lstore_0       : store_local(longType  , 0); break;
                case Bytecode::_lstore_1       : store_local(longType  , 1); break;
                case Bytecode::_lstore_2       : store_local(longType  , 2); break;
                case Bytecode::_lstore_3       : store_local(longType  , 3); break;
                case Bytecode::_fstore_0       : store_local(floatType , 0); break;
                case Bytecode::_fstore_1       : store_local(floatType , 1); break;
                case Bytecode::_fstore_2       : store_local(floatType , 2); break;
                case Bytecode::_fstore_3       : store_local(floatType , 3); break;
                case Bytecode::_dstore_0       : store_local(doubleType, 0); break;
                case Bytecode::_dstore_1       : store_local(doubleType, 1); break;
                case Bytecode::_dstore_2       : store_local(doubleType, 2); break;
                case Bytecode::_dstore_3       : store_local(doubleType, 3); break;
                case Bytecode::_astore_0       : store_local(objectType, 0); break;
                case Bytecode::_astore_1       : store_local(objectType, 1); break;
                case Bytecode::_astore_2       : store_local(objectType, 2); break;
                case Bytecode::_astore_3       : store_local(objectType, 3); break;
                case Bytecode::_iastore        : store_indexed(T_INT   ); break;
                case Bytecode::_lastore        : store_indexed(T_LONG  ); break;
                case Bytecode::_fastore        : store_indexed(T_FLOAT ); break;
                case Bytecode::_dastore        : store_indexed(T_DOUBLE); break;
                case Bytecode::_aastore        : store_indexed(T_OBJECT); break;
                case Bytecode::_bastore        : store_indexed(T_BYTE  ); break;
                case Bytecode::_castore        : store_indexed(T_CHAR  ); break;
                case Bytecode::_sastore        : store_indexed(T_SHORT ); break;
                case Bytecode::_pop            : // fall through
                case Bytecode::_pop2           : // fall through
                case Bytecode::_dup            : // fall through
                case Bytecode::_dup_x1         : // fall through
                case Bytecode::_dup_x2         : // fall through
                case Bytecode::_dup2           : // fall through
                case Bytecode::_dup2_x1        : // fall through
                case Bytecode::_dup2_x2        : // fall through
                case Bytecode::_swap           : stack_op(code); break;
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

    private void loadConstant(){

    }
}
