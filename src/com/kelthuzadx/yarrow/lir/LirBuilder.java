package com.kelthuzadx.yarrow.lir;

import com.kelthuzadx.yarrow.bytecode.Bytecode;
import com.kelthuzadx.yarrow.core.YarrowError;
import com.kelthuzadx.yarrow.core.YarrowRuntime;
import com.kelthuzadx.yarrow.hir.BlockFlag;
import com.kelthuzadx.yarrow.hir.Hir;
import com.kelthuzadx.yarrow.hir.instr.*;
import com.kelthuzadx.yarrow.lir.operand.LirOperand;
import com.kelthuzadx.yarrow.lir.operand.OperandFactory;
import com.kelthuzadx.yarrow.lir.operand.VirtualRegister;
import com.kelthuzadx.yarrow.optimize.InstructionVisitor;
import com.kelthuzadx.yarrow.optimize.Phase;
import com.kelthuzadx.yarrow.util.CompilerErrors;
import jdk.vm.ci.amd64.AMD64;
import jdk.vm.ci.code.MemoryBarriers;
import jdk.vm.ci.meta.JavaKind;

import java.util.ArrayDeque;
import java.util.HashSet;

/**
 * Generate low level IR for x86_64 architecture, many lir generating follows x86 ABI convention
 *
 * @author kelthuzadx
 */
public class LirBuilder extends InstructionVisitor implements Phase {
    private final Hir hir;
    private Lir lir;
    private HashSet<Integer> visit;
    private ArrayDeque<BlockStartInstr> workList;
    private LirGenerator gen;


    public LirBuilder(Hir hir) {
        this.hir = hir;
        this.lir = new Lir();
        this.gen = new LirGenerator(lir);
    }

    private void transformBlock(BlockStartInstr block) {
        HirInstr last = block;
        while (last != null && last != block.getBlockEnd()) {
            last.visit(this);
            last = last.getNext();
        }
        if (last != null && last == block.getBlockEnd()) {
            last.visit(this);
        }
    }

    @Override
    public Phase build() {
        visit = new HashSet<>();
        workList = new ArrayDeque<>();
        workList.add(hir.getEntryBlock());
        while (!workList.isEmpty()) {
            BlockStartInstr blockStart = workList.remove();
            if (!visit.contains(blockStart.getBlockId())) {
                visit.add(blockStart.getBlockId());
                transformBlock(blockStart);
                workList.addAll(blockStart.getBlockEnd().getSuccessor());
            }
        }
        lir.printLir();
        return this;
    }

    @Override
    public String name() {
        return "Create Low level IR";
    }

    @Override
    public void log() {

    }

    @Override
    public void visitMemBarrierInstr(MemBarrierInstr instr) {
        switch (instr.getBarrierType()) {
            case MemoryBarriers.STORE_STORE:
                gen.emitMembar(Mnemonic.MembarStoreStore);
            case MemoryBarriers.LOAD_LOAD:
                gen.emitMembar(Mnemonic.MembarLoadLoad);
                return;
            case MemoryBarriers.LOAD_STORE:
                gen.emitMembar(Mnemonic.MembarLoadStore);
                return;
            case MemoryBarriers.STORE_LOAD:
                gen.emitMembar(Mnemonic.MembarStoreStore);
                return;
            default:
                YarrowError.unimplemented();
        }
    }

    @Override
    public void visitInstanceOfInstr(InstanceOfInstr instr) {

    }

    @Override
    public void visitShiftInstr(ShiftInstr instr) {
        LirOperand count;
        if (!(instr.getRight() instanceof ConstantInstr) || instr.getLeft().isType(JavaKind.Long)) {
            VirtualRegister rcx = OperandFactory.createVirtualRegister(AMD64.rcx);
            count = instr.getRight().loadOperandToReg(this, gen, rcx);
        } else {
            count = instr.getRight().loadOperand(this);
        }
        LirOperand value = instr.getLeft().loadOperandToReg(this, gen);
        LirOperand result = OperandFactory.createVirtualRegister(instr.type());
        instr.installOperand(result);

        switch (instr.getOpcode()) {
            case Bytecode.ISHL:
            case Bytecode.LSHL:
                gen.emitShl(result, value, count);
                break;
            case Bytecode.ISHR:
            case Bytecode.LSHR:
                gen.emitShr(result, value, count);
                break;
            case Bytecode.IUSHR:
            case Bytecode.LUSHR:
                gen.emitUshr(result, value, count);
                break;
        }
    }

    @Override
    public void visitBlockEndInstr(BlockEndInstr instr) {
    }

    @Override
    public void visitParamInstr(ParamInstr instr) {
        instr.installOperand(OperandFactory.createVirtualRegister(instr.type()));
    }

    @Override
    public void visitLogicInstr(LogicInstr instr) {
        LirOperand left = instr.getLeft().loadOperandToReg(this, gen);
        LirOperand right;
        if (!(instr.getRight() instanceof ConstantInstr)) {
            right = instr.getRight().loadOperandToReg(this, gen);
        } else {
            right = instr.getRight().loadOperand(this);
        }
        LirOperand result = OperandFactory.createVirtualRegister(instr.type());
        instr.installOperand(result);
        if (left != result) {
            gen.emitMov(result, left);
            left = result;
        }
        switch (instr.getOpcode()) {
            case Bytecode.IAND:
            case Bytecode.LAND:
                gen.emitAnd(result, left, right);
                break;
            case Bytecode.IOR:
            case Bytecode.LOR:
                gen.emitOr(result, left, right);
                break;
            case Bytecode.IXOR:
            case Bytecode.LXOR:
                gen.emitXor(result, left, right);
                break;
            default:
                YarrowError.shouldNotReachHere();
        }
    }

    @Override
    public void visitMultiNewArrayInstr(MultiNewArrayInstr instr) {

    }

    @Override
    public void visitStoreIndexInstr(StoreIndexInstr instr) {

    }

    @Override
    public void visitStoreFieldInstr(StoreFieldInstr instr) {

    }

    @Override
    public void visitPhiInstr(PhiInstr instr) {

    }

    @Override
    public void visitBlockStartInstr(BlockStartInstr instr) {
        gen.setCurrentBlockId(instr.id());
        if (instr.getFlag() == BlockFlag.NormalEntry) {
            gen.emitNormalEntry();
            YarrowError.guarantee(instr.getBlockEnd().getSuccessor().size() == 1, "Expect one successor");
            gen.emitJmp(instr.getBlockEnd().getSuccessor().get(0));
        } else if (instr.getFlag() == BlockFlag.OsrEntry) {
            gen.emitOsrEntry();
            YarrowError.guarantee(instr.getBlockEnd().getSuccessor().size() == 1, "Expect one successor");
            gen.emitJmp(instr.getBlockEnd().getSuccessor().get(0));
        }
    }

    @Override
    public void visitLoadFieldInstr(LoadFieldInstr instr) {

    }

    @Override
    public void visitCompareInstr(CompareInstr instr) {

    }

    @Override
    public void visitNegateInstr(NegateInstr instr) {
        LirOperand value = instr.getValue().loadOperandToReg(this, gen);
        if (value.isVirtualRegister()) {
            LirOperand newValue = OperandFactory.createVirtualRegister(instr.getValue().type());
            gen.emitMov(newValue, value);
            value = newValue;
        }
        LirOperand result = OperandFactory.createVirtualRegister(instr.type());
        instr.installOperand(result);
        gen.emitNeg(value, result);
    }

    @Override
    public void visitConstantInstr(ConstantInstr instr) {
        instr.installOperand(OperandFactory.createConstValue(instr.getConstant()));
    }

    @Override
    public void visitTableSwitchInstr(TableSwitchInstr instr) {

    }

    @Override
    public void visitCheckCastInstr(CheckCastInstr instr) {

    }

    @Override
    public void visitAccessFieldInstr(AccessFieldInstr instr) {

    }

    @Override
    public void visitStateInstr(StateInstr instr) {

    }

    @Override
    public void visitLoadIndexInstr(LoadIndexInstr instr) {

    }

    @Override
    public void visitArithmeticInstr(ArithmeticInstr instr) {
        LirOperand left = instr.getLeft().loadOperandToReg(this, gen);
        LirOperand right = instr.getRight().loadOperand(this);
        LirOperand result = OperandFactory.createVirtualRegister(instr.type());
        instr.installOperand(result);
        if (left != result) {
            gen.emitMov(result, left);
            left = result;
        }
        switch (instr.getOpcode()) {
            case Bytecode.IADD:
            case Bytecode.LADD:
            case Bytecode.FADD:
            case Bytecode.DADD:
                gen.emitAdd(result, left, right);
                break;
            case Bytecode.ISUB:
            case Bytecode.LSUB:
            case Bytecode.FSUB:
            case Bytecode.DSUB:
                gen.emitSub(result, left, right);
                break;
            case Bytecode.IMUL:
            case Bytecode.LMUL:
            case Bytecode.FMUL:
            case Bytecode.DMUL:
                gen.emitMul(result, left, right);
                break;
            case Bytecode.IDIV:
            case Bytecode.LDIV:
                CompilerErrors.bailOut();
            case Bytecode.FDIV:
            case Bytecode.DDIV:
                gen.emitDiv(result, left, right);
                break;
            case Bytecode.IREM:
            case Bytecode.LREM:
            case Bytecode.FREM:
            case Bytecode.DREM:
                gen.emitRem(result, left, right);
                break;
            default:
                YarrowError.shouldNotReachHere();
        }
    }


    @Override
    public void visitArrayLenInstr(ArrayLenInstr instr) {

    }

    @Override
    public void visitMonitorExitInstr(MonitorExitInstr instr) {

    }

    @Override
    public void visitMonitorEnterInstr(MonitorEnterInstr instr) {

    }

    @Override
    public void visitLookupSwitchInstr(LookupSwitchInstr instr) {

    }

    @Override
    public void visitIfInstr(IfInstr instr) {

    }

    @Override
    public void visitNewInstr(NewInstr instr) {

    }

    @Override
    public void visitTypeCastInstr(TypeCastInstr instr) {
        LirOperand fromOperand = instr.getFrom().loadOperandToReg(this, gen);
        LirOperand fromResult = OperandFactory.createVirtualRegister(instr.type());
        instr.installOperand(fromResult);

        LirOperand toOperand = fromOperand;
        LirOperand toResult = fromResult;

        gen.emitJavaCast(toResult, toOperand, instr.getOpcode());

        if (fromResult != toResult) {
            gen.emitMov(toResult, fromResult);
        }
        // FIXME:SPECIAL HANDLE
    }

    @Override
    public void visitInstruction(HirInstr instr) {

    }

    @Override
    public void visitAccessArrayInstr(AccessArrayInstr instr) {

    }

    @Override
    public void visitNewObjectArrayInstr(NewObjectArrayInstr instr) {

    }

    @Override
    public void visitCallInstr(CallInstr instr) {

    }

    @Override
    public void visitGotoInstr(GotoInstr instr) {

    }

    @Override
    public void visitReturnInstr(ReturnInstr instr) {
        if (instr.isType(JavaKind.Void)) {
            gen.emitReturn(LirOperand.illegal);
            instr.installOperand(null); // ReturnInstr has no operand result
            return;
        }

        VirtualRegister retReg = OperandFactory.createVirtualRegister(YarrowRuntime.regConfig.getReturnRegister(instr.type()));
        LirOperand left = instr.getReturnValue().loadOperandToReg(this, gen, retReg);
        gen.emitReturn(left);
        instr.installOperand(null); // ReturnInstr has no operand result
    }

    @Override
    public void visitOp2Instr(Op2Instr instr) {
        YarrowError.shouldNotReachHere();
    }

    @Override
    public void visitThrowInstr(ThrowInstr instr) {

    }

    @Override
    public void visitNewTypeArrayInstr(NewTypeArrayInstr instr) {

    }
}
