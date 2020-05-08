package com.kelthuzadx.yarrow.lir;

import com.kelthuzadx.yarrow.bytecode.Bytecode;
import com.kelthuzadx.yarrow.core.YarrowConfigAccess;
import com.kelthuzadx.yarrow.core.YarrowError;
import com.kelthuzadx.yarrow.core.YarrowRuntime;
import com.kelthuzadx.yarrow.hir.BlockFlag;
import com.kelthuzadx.yarrow.hir.Cond;
import com.kelthuzadx.yarrow.hir.Hir;
import com.kelthuzadx.yarrow.hir.instr.*;
import com.kelthuzadx.yarrow.lir.operand.*;
import com.kelthuzadx.yarrow.lir.stub.ClassCastExStub;
import com.kelthuzadx.yarrow.lir.stub.NewArrayStub;
import com.kelthuzadx.yarrow.lir.stub.NewInstanceStub;
import com.kelthuzadx.yarrow.lir.stub.VmStub;
import com.kelthuzadx.yarrow.optimize.InstructionVisitor;
import com.kelthuzadx.yarrow.optimize.Phase;
import com.kelthuzadx.yarrow.util.Logger;
import jdk.vm.ci.amd64.AMD64;
import jdk.vm.ci.code.CallingConvention;
import jdk.vm.ci.code.MemoryBarriers;
import jdk.vm.ci.code.RegisterValue;
import jdk.vm.ci.code.StackSlot;
import jdk.vm.ci.hotspot.HotSpotCallingConventionType;
import jdk.vm.ci.hotspot.HotSpotResolvedJavaType;
import jdk.vm.ci.hotspot.HotSpotResolvedObjectType;
import jdk.vm.ci.meta.*;

import java.util.ArrayDeque;
import java.util.HashSet;

import static com.kelthuzadx.yarrow.core.YarrowProperties.Debug.PrintIR;
import static com.kelthuzadx.yarrow.core.YarrowProperties.Debug.TraceLIRGeneration;


/**
 * Generate low level IR for x86_64 architecture, many lir generating follows x86 ABI convention
 *
 * @author kelthuzadx
 */
public class LirBuilder extends InstructionVisitor implements Phase {
    private final LirValueKindFactory valueFactory;
    private final Hir hir;
    private final Lir lir;
    private final LirGenerator gen;


    public LirBuilder(Hir hir) {
        this.valueFactory = new LirValueKindFactory();
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
    public LirBuilder build() {
        if (TraceLIRGeneration) {
            Logger.logf("===== Generate Lir from Hir=====");
        }
        HashSet<Integer> visit = new HashSet<>();
        ArrayDeque<BlockStartInstr> workList = new ArrayDeque<>();
        workList.add(hir.getEntryBlock());
        while (!workList.isEmpty()) {
            BlockStartInstr blockStart = workList.remove();
            if (!visit.contains(blockStart.getBlockId())) {
                visit.add(blockStart.getBlockId());
                transformBlock(blockStart);
                workList.addAll(blockStart.getBlockEnd().getSuccessor());
            }
        }
        return this;
    }

    @Override
    public String name() {
        return "Low level IR";
    }

    @Override
    public void log() {
        if (PrintIR) {
            lir.printLir();
        }
    }

    @Override
    public void visitMemBarrierInstr(MemBarrierInstr instr) {
        switch (instr.getBarrierType()) {
            case MemoryBarriers.STORE_STORE:
                gen.emitMembar(Mnemonic.MEMBAR_STORE_STORE);
            case MemoryBarriers.LOAD_LOAD:
                gen.emitMembar(Mnemonic.MEMBAR_LOAD_LOAD);
                return;
            case MemoryBarriers.LOAD_STORE:
                gen.emitMembar(Mnemonic.MEMBAR_LOAD_STORE);
                return;
            case MemoryBarriers.STORE_LOAD:
                gen.emitMembar(Mnemonic.MEMBAR_STORE_STORE);
                return;
            default:
                YarrowError.unimplemented();
        }
    }

    @Override
    public void visitInstanceOfInstr(InstanceOfInstr instr) {
        LirOperand object = instr.getObject().loadOperandToReg(this, gen);
        LirOperand result = new VirtualRegister(instr.type());
        instr.storeOperand(result);
        gen.emitInstanceOf(result, object, instr.getKlass());
    }

    @Override
    public void visitShiftInstr(ShiftInstr instr) {
        LirOperand count;
        if (!(instr.getRight() instanceof ConstantInstr) || instr.getLeft().isType(JavaKind.Long)) {
            VirtualRegister rcx = new VirtualRegister(AMD64.rcx);
            count = instr.getRight().loadOperandToReg(this, gen, rcx);
        } else {
            count = instr.getRight().loadOperand(this);
        }
        LirOperand value = instr.getLeft().loadOperandToReg(this, gen);
        LirOperand result = new VirtualRegister(instr.type());
        instr.storeOperand(result);

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
        YarrowError.shouldNotReachHere();
    }

    @Override
    public void visitParamInstr(ParamInstr instr) {
        instr.storeOperand(new VirtualRegister(instr.type()));
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
        LirOperand result = new VirtualRegister(instr.type());
        instr.storeOperand(result);
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
    public void visitNewMultiArrayInstr(NewMultiArrayInstr instr) {
        HirInstr[] sizeArray = instr.getSizeArray();
        for (int i = 0; i < sizeArray.length; i++) {
            LirOperand size = sizeArray[i].loadOperand(this);
            if (!size.isConstValue()) {
                size = sizeArray[i].loadOperandToReg(this, gen);
                JavaKind type = sizeArray[i].type();
                Address addr = new Address(new VirtualRegister(AMD64.rsp), LirOperand.illegal, 1, i * 4, type);
                gen.emitMov(size, addr);
            }
        }

        VirtualRegister klass = new VirtualRegister(AMD64.rbx);
        gen.emitMov(klass, new ConstValue(JavaConstant.forLong(YarrowRuntime.getKlassPointer((HotSpotResolvedJavaType) instr.getKlass()))));

        LirOperand rank = new VirtualRegister(AMD64.rbx);
        gen.emitMov(rank, new ConstValue(JavaConstant.forInt(sizeArray.length)));

        VirtualRegister varArgs = new VirtualRegister(AMD64.rcx);
        gen.emitMov(varArgs, new VirtualRegister(AMD64.rsp));

        LirOperand[] args = new LirOperand[3];
        args[0] = klass;
        args[1] = rank;
        args[2] = varArgs;

        LirOperand ret = new VirtualRegister(YarrowRuntime.regConfig.getReturnRegister(instr.type()));
        Address stubAddr = new Address(new ConstValue(JavaConstant.forLong(VmStub.StubNewArray.getStubAddress())), LirOperand.illegal, 1, 0, JavaKind.Int);
        gen.emitCallRt(ret, stubAddr, args);
        LirOperand result = new VirtualRegister(instr.type());
        gen.emitMov(result, ret);
        instr.storeOperand(result);
    }

    @Override
    public void visitStoreIndexInstr(StoreIndexInstr instr) {
        var array = instr.getArray().loadOperandToReg(this, gen);
        var index = instr.getIndex().loadOperandToReg(this, gen);
        var storeValue = instr.getStoreValue().loadOperandToReg(this, gen);
        Address address;
        if (index.isConstValue()) {
            address = new Address(array, index, instr.getElementType());
        } else {
            address = new Address(array, index, Address.scaleFor(instr.getElementType()), YarrowConfigAccess.access().arrayClassElementOffset, instr.getElementType());
        }
        gen.emitMov(address, storeValue);
    }

    @Override
    public void visitStoreFieldInstr(StoreFieldInstr instr) {
        var base = instr.getObject().loadOperandToReg(this, gen);
        var storeValue = instr.getStoreValue().loadOperandToReg(this, gen);
        instr.storeOperand(null);
        var offset = new ConstValue(JavaConstant.forInt(instr.getOffset()));
        var address = new Address(base, offset, instr.getField().getJavaKind());
        gen.emitMov(address, storeValue);
    }

    @Override
    public void visitPhiInstr(PhiInstr instr) {
        YarrowError.shouldNotReachHere();
    }

    @Override
    public void visitBlockStartInstr(BlockStartInstr instr) {
        gen.setCurrentBlockId(instr.id());
        if (instr.getFlag() == BlockFlag.NormalEntry) {
            gen.emitNormalEntry();
            YarrowError.guarantee(instr.getBlockEnd().getSuccessor().size() == 1, "Expect one successor");
        } else if (instr.getFlag() == BlockFlag.OsrEntry) {
            gen.emitOsrEntry();
            YarrowError.guarantee(instr.getBlockEnd().getSuccessor().size() == 1, "Expect one successor");
        }
    }

    @Override
    public void visitLoadFieldInstr(LoadFieldInstr instr) {
        var base = instr.getObject().loadOperandToReg(this, gen);
        var result = new VirtualRegister(instr.getField().getJavaKind());
        instr.storeOperand(result);
        var offset = new ConstValue(JavaConstant.forInt(instr.getOffset()));
        var address = new Address(base, offset, instr.getField().getJavaKind());
        gen.emitMov(result, address);
        // FIXME: ADD GC BARRIER IF NEEDED
    }

    @Override
    public void visitCompareInstr(CompareInstr instr) {
        //FIXME: remove redundant mov instruction for constant value
        LirOperand left = instr.getLeft().loadOperandToReg(this, gen);

        LirOperand right = instr.getRight().loadOperandToReg(this, gen);
        LirOperand result = new VirtualRegister(instr.type());
        instr.storeOperand(result);
        if (instr.getLeft().isType(JavaKind.Long)) {
            if (left.isVirtualRegister()) {
                LirOperand t = new VirtualRegister(instr.getLeft().type());
                gen.emitMov(t, left);
                left = t;
            }
        }
        if (instr.getLeft().isType(JavaKind.Float) || instr.getLeft().isType(JavaKind.Double)) {
            gen.emitFcmp(result, left, right, instr.getOpcode() == Bytecode.FCMPL || instr.getOpcode() == Bytecode.DCMPL);
        } else if (instr.getLeft().isType(JavaKind.Long)) {
            gen.emitLcmp(result, left, right);
        }
    }

    @Override
    public void visitNegateInstr(NegateInstr instr) {
        LirOperand value = instr.getValue().loadOperandToReg(this, gen);
        if (value.isVirtualRegister()) {
            LirOperand newValue = new VirtualRegister(instr.getValue().type());
            gen.emitMov(newValue, value);
            value = newValue;
        }
        LirOperand result = new VirtualRegister(instr.type());
        instr.storeOperand(result);
        gen.emitNeg(value, result);
    }

    @Override
    public void visitConstantInstr(ConstantInstr instr) {
        instr.storeOperand(new ConstValue(instr.getConstant()));
    }

    @Override
    public void visitTableSwitchInstr(TableSwitchInstr instr) {
        instr.storeOperand(null);
        var index = instr.getIndex().loadOperandToReg(this, gen);
        new PhiResolver(gen).resolve(instr.getSuccessor(), instr.getVmState());
        for (int i = 0; i < instr.getLength(); i++) {
            gen.emitCmp(index, i + instr.getLowKey(), Cond.EQ);
            gen.emitBranch(Cond.EQ, JavaKind.Int, instr.getSuccessor().get(i));
        }
        gen.emitJmp(instr.getSuccessor().get(instr.getSuccessor().size() - 1));
    }

    @Override
    public void visitCheckCastInstr(CheckCastInstr instr) {
        LirOperand object = instr.getObject().loadOperandToReg(this, gen);
        LirOperand result = new VirtualRegister(instr.type());
        instr.storeOperand(result);
        var stub = new ClassCastExStub(object);
        gen.emitCheckCast(result, object, (HotSpotResolvedJavaType) instr.getKlass(), stub);
    }

    @Override
    public void visitAccessFieldInstr(AccessFieldInstr instr) {
        YarrowError.shouldNotReachHere();
    }

    @Override
    public void visitStateInstr(StateInstr instr) {
        YarrowError.shouldNotReachHere();
    }

    @Override
    public void visitLoadIndexInstr(LoadIndexInstr instr) {
        var array = instr.getArray().loadOperandToReg(this, gen);
        var index = instr.getIndex().loadOperandToReg(this, gen);
        var result = new VirtualRegister(instr.getElementType());
        instr.storeOperand(result);
        Address address;
        if (index.isConstValue()) {
            address = new Address(array, index, instr.getElementType());
        } else {
            address = new Address(array, index, Address.scaleFor(instr.getElementType()), YarrowConfigAccess.access().arrayClassElementOffset, instr.getElementType());
        }
        gen.emitMov(result, address);
    }

    @Override
    public void visitArithmeticInstr(ArithmeticInstr instr) {
        LirOperand left = instr.getLeft().loadOperandToReg(this, gen);
        LirOperand right = instr.getRight().loadOperandToReg(this, gen);
        LirOperand result = new VirtualRegister(instr.type());
        instr.storeOperand(result);
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
        LirOperand array = instr.getArray().loadOperandToReg(this, gen);
        LirOperand result = new VirtualRegister(instr.type());
        instr.storeOperand(result);
        Address addr = new Address(array, LirOperand.illegal, 1, YarrowConfigAccess.access().getArrayLengthOffset(), JavaKind.Int);
        gen.emitMov(result, addr);
    }

    @Override
    public void visitMonitorExitInstr(MonitorExitInstr instr) {

    }

    @Override
    public void visitMonitorEnterInstr(MonitorEnterInstr instr) {

    }

    @Override
    public void visitLookupSwitchInstr(LookupSwitchInstr instr) {
        instr.storeOperand(null);
        var index = instr.getIndex().loadOperandToReg(this, gen);
        new PhiResolver(gen).resolve(instr.getSuccessor(), instr.getVmState());
        for (int i = 0; i < instr.getLength(); i++) {
            gen.emitCmp(index, instr.getKey()[i], Cond.EQ);
            gen.emitBranch(Cond.EQ, JavaKind.Int, instr.getSuccessor().get(i));
        }
        gen.emitJmp(instr.getSuccessor().get(instr.getSuccessor().size() - 1));
    }

    @Override
    public void visitIfInstr(IfInstr instr) {
        var left = instr.getLeft().loadOperandToReg(this, gen);
        var right = instr.getRight().loadOperandToReg(this, gen);
        instr.storeOperand(null);
        gen.emitCmp(left, right, instr.getCond());
        new PhiResolver(gen).resolve(instr.getSuccessor(), instr.getVmState());

        gen.emitBranch(instr.getCond(), instr.getRight().type(), instr.getSuccessor().get(0));
        gen.emitJmp(instr.getSuccessor().get(1));
    }

    @Override
    public void visitNewInstr(NewInstr instr) {
        VirtualRegister retReg = new VirtualRegister(YarrowRuntime.regConfig.getReturnRegister(instr.type()));
        var klassPointer = YarrowRuntime.getKlassPointer((HotSpotResolvedJavaType) instr.getKlass());

        VirtualRegister metadataReg = new VirtualRegister(AMD64.rdx);
        gen.emitMov(metadataReg, new ConstValue(JavaConstant.forLong(klassPointer)));
        var stub = new NewInstanceStub((HotSpotResolvedObjectType) instr.getKlass(), metadataReg, retReg);
        gen.emitJmp(stub);
        gen.emitLabel(stub.getContinuation());
        VirtualRegister result = new VirtualRegister(instr.type());
        gen.emitMov(result, retReg);
        instr.storeOperand(result);
    }

    @Override
    public void visitTypeCastInstr(TypeCastInstr instr) {
        LirOperand fromOperand = instr.getFrom().loadOperandToReg(this, gen);
        LirOperand fromResult = new VirtualRegister(instr.type());
        instr.storeOperand(fromResult);

        LirOperand toOperand = fromOperand;
        LirOperand toResult = fromResult;

        gen.emitJavaCast(toResult, toOperand, instr.getOpcode());

        // FIXME:SPECIAL HANDLE
    }

    @Override
    public void visitInstruction(HirInstr instr) {
        YarrowError.shouldNotReachHere();
    }

    @Override
    public void visitAccessArrayInstr(AccessArrayInstr instr) {
        YarrowError.shouldNotReachHere();
    }

    @Override
    public void visitNewObjectArrayInstr(NewObjectArrayInstr instr) {
        VirtualRegister length = (VirtualRegister) instr.arrayLength().loadOperandToReg(this, gen,
                new VirtualRegister(AMD64.rbx));
        VirtualRegister retReg = new VirtualRegister(YarrowRuntime.regConfig.getReturnRegister(instr.type()));
        VirtualRegister temp1 = new VirtualRegister(AMD64.rcx);
        VirtualRegister temp2 = new VirtualRegister(AMD64.rsi);
        VirtualRegister temp3 = new VirtualRegister(AMD64.rdi);
        VirtualRegister temp4 = retReg;
        VirtualRegister klassReg = new VirtualRegister(AMD64.rdx);
        var klassPointer = YarrowRuntime.getKlassPointer((HotSpotResolvedJavaType) instr.getKlass());
        gen.emitMov(klassReg, new ConstValue(JavaConstant.forLong(klassPointer)));
        var stub = new NewArrayStub(length, klassReg, retReg);
        gen.emitAllocateArray(stub, klassReg, retReg, length, temp1, temp2, temp3, temp4, JavaKind.Object);
        LirOperand result = new VirtualRegister(instr.type());
        gen.emitMov(result, retReg);
        instr.storeOperand(result);
    }

    @Override
    public void visitCallInstr(CallInstr instr) {
        Signature sig = instr.getSignature();
        JavaKind[] paramKinds = sig.toParameterKinds(instr.hasReceiver());
        JavaType[] paramTypes = new JavaType[paramKinds.length];
        for (int i = 0; i < paramKinds.length; i++) {
            paramTypes[i] = sig.getParameterType(i, null);
        }
        HirInstr[] param = new HirInstr[instr.argumentCount() + (instr.hasReceiver() ? 1 : 0)];
        int i = 0;
        if (param.length > 0) {
            if (instr.hasReceiver()) {
                param[i++] = instr.getReceiver();
            }
            for (int j = 0; i < param.length; i++, j++) {
                param[i] = instr.getArguments(j);
            }
        }

        CallingConvention cc = YarrowRuntime.regConfig.getCallingConvention(
                HotSpotCallingConventionType.JavaCall, sig.getReturnType(null), paramTypes, valueFactory);
        LirOperand receiver = LirOperand.illegal;
        LirOperand result = LirOperand.illegal;
        if (instr.getSignature().getReturnKind() != JavaKind.Void) {
            result = new VirtualRegister(YarrowRuntime.regConfig.getReturnRegister(instr.getSignature().getReturnKind()));
        }
        AllocatableValue[] args = cc.getArguments();
        for (i = 0; i < param.length; i++) {
            if (args[i] instanceof RegisterValue) {
                param[i].loadOperandToReg(this, gen, new VirtualRegister(((RegisterValue) args[i]).getRegister()));
            } else if(args[i] instanceof StackSlot){
                // TODO
                StackSlot slot = (StackSlot)args[i];
            }else{
                YarrowError.shouldNotReachHere();
            }
        }
    }

    @Override
    public void visitGotoInstr(GotoInstr instr) {
        instr.storeOperand(LirOperand.illegal);
        new PhiResolver(gen).resolve(instr.getSuccessor(), instr.getVmState());
        gen.emitJmp(instr.getSuccessor().get(0));
    }

    @Override
    public void visitReturnInstr(ReturnInstr instr) {
        if (instr.isType(JavaKind.Void)) {
            gen.emitReturn(LirOperand.illegal);
            instr.storeOperand(null); // ReturnInstr has no operand result
            return;
        }

        VirtualRegister retReg = new VirtualRegister(YarrowRuntime.regConfig.getReturnRegister(instr.type()));
        LirOperand left = instr.getReturnValue().loadOperandToReg(this, gen, retReg);
        gen.emitReturn(left);
        instr.storeOperand(null); // ReturnInstr has no operand result
    }

    @Override
    public void visitOp2Instr(Op2Instr instr) {
        YarrowError.shouldNotReachHere();
    }

    @Override
    public void visitThrowInstr(ThrowInstr instr) {
        YarrowError.unimplemented("unsupported");
    }

    @Override
    public void visitNewTypeArrayInstr(NewTypeArrayInstr instr) {
        VirtualRegister length = (VirtualRegister) instr.arrayLength().loadOperandToReg(this, gen,
                new VirtualRegister(AMD64.rbx));
        VirtualRegister retReg = new VirtualRegister(YarrowRuntime.regConfig.getReturnRegister(instr.type()));
        VirtualRegister temp1 = new VirtualRegister(AMD64.rcx);
        VirtualRegister temp2 = new VirtualRegister(AMD64.rsi);
        VirtualRegister temp3 = new VirtualRegister(AMD64.rdi);
        VirtualRegister temp4 = retReg;
        VirtualRegister klassReg = new VirtualRegister(AMD64.rdx);
        var klassPointer = YarrowRuntime.getKlassPointer(instr.getElemementType().toJavaClass());
        gen.emitMov(klassReg, new ConstValue(JavaConstant.forLong(klassPointer)));
        var stub = new NewArrayStub(length, klassReg, retReg);
        gen.emitAllocateArray(stub, klassReg, retReg, length, temp1, temp2, temp3, temp4, instr.getElemementType());
        LirOperand result = new VirtualRegister(instr.type());
        gen.emitMov(result, retReg);
        instr.storeOperand(result);
    }

}
