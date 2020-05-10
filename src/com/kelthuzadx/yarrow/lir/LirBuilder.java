package com.kelthuzadx.yarrow.lir;

import com.kelthuzadx.yarrow.bytecode.Bytecode;
import com.kelthuzadx.yarrow.core.YarrowConfigAccess;
import com.kelthuzadx.yarrow.core.YarrowError;
import com.kelthuzadx.yarrow.core.YarrowRuntime;
import com.kelthuzadx.yarrow.hir.BlockFlag;
import com.kelthuzadx.yarrow.hir.Cond;
import com.kelthuzadx.yarrow.hir.Hir;
import com.kelthuzadx.yarrow.hir.instr.*;
import com.kelthuzadx.yarrow.lir.operand.Address;
import com.kelthuzadx.yarrow.lir.operand.ConstValue;
import com.kelthuzadx.yarrow.lir.operand.LirValueKindFactory;
import com.kelthuzadx.yarrow.lir.operand.VirtualRegister;
import com.kelthuzadx.yarrow.lir.stub.ClassCastExStub;
import com.kelthuzadx.yarrow.lir.stub.NewArrayStub;
import com.kelthuzadx.yarrow.lir.stub.NewInstanceStub;
import com.kelthuzadx.yarrow.lir.stub.VmStub;
import com.kelthuzadx.yarrow.optimize.HirInstrVisitor;
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
public class LirBuilder extends HirInstrVisitor implements Phase {
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

    public Lir getLir() {
        return lir;
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
        AllocatableValue object = instr.getObject().loadOperandToReg(this, gen);
        AllocatableValue result = new VirtualRegister(instr.type());
        instr.storeOperand(result);
        gen.emitInstanceOf(result, object, instr.getKlass());
    }

    @Override
    public void visitShiftInstr(ShiftInstr instr) {
        AllocatableValue count;
        if (!(instr.getRight() instanceof ConstantInstr) || instr.getLeft().isType(JavaKind.Long)) {
            VirtualRegister rcx = new VirtualRegister(AMD64.rcx);
            count = instr.getRight().loadOperandToReg(this, gen, rcx);
        } else {
            count = instr.getRight().loadOperand(this);
        }
        AllocatableValue value = instr.getLeft().loadOperandToReg(this, gen);
        AllocatableValue result = new VirtualRegister(instr.type());
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
        AllocatableValue left = instr.getLeft().loadOperandToReg(this, gen);
        AllocatableValue right;
        if (!(instr.getRight() instanceof ConstantInstr)) {
            right = instr.getRight().loadOperandToReg(this, gen);
        } else {
            right = instr.getRight().loadOperand(this);
        }
        AllocatableValue result = new VirtualRegister(instr.type());
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
            AllocatableValue size = sizeArray[i].loadOperand(this);
            if (!(size instanceof ConstValue)) {
                size = sizeArray[i].loadOperandToReg(this, gen);
                JavaKind type = sizeArray[i].type();
                Address addr = new Address(new VirtualRegister(AMD64.rsp), AllocatableValue.ILLEGAL, 1, i * 4, type);
                gen.emitMov(size, addr);
            }
        }

        VirtualRegister klass = new VirtualRegister(AMD64.rbx);
        gen.emitMov(klass, new ConstValue(JavaConstant.forLong(YarrowRuntime.getKlassPointer((HotSpotResolvedJavaType) instr.getKlass()))));

        AllocatableValue rank = new VirtualRegister(AMD64.rbx);
        gen.emitMov(rank, new ConstValue(JavaConstant.forInt(sizeArray.length)));

        VirtualRegister varArgs = new VirtualRegister(AMD64.rcx);
        gen.emitMov(varArgs, new VirtualRegister(AMD64.rsp));

        AllocatableValue[] args = new AllocatableValue[3];
        args[0] = klass;
        args[1] = rank;
        args[2] = varArgs;

        AllocatableValue ret = new VirtualRegister(YarrowRuntime.regConfig.getReturnRegister(instr.type()));
        Address stubAddr = new Address(new ConstValue(JavaConstant.forLong(VmStub.StubNewArray.getStubAddress())), AllocatableValue.ILLEGAL, 1, 0, JavaKind.Int);
        gen.emitCallRt(ret, stubAddr, args);
        AllocatableValue result = new VirtualRegister(instr.type());
        gen.emitMov(result, ret);
        instr.storeOperand(result);
    }

    @Override
    public void visitStoreIndexInstr(StoreIndexInstr instr) {
        var array = instr.getArray().loadOperandToReg(this, gen);
        var index = instr.getIndex().loadOperandToReg(this, gen);
        var storeValue = instr.getStoreValue().loadOperandToReg(this, gen);
        Address address;
        if (index instanceof ConstValue) {
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
        instr.storeOperand(AllocatableValue.ILLEGAL);
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
        gen.setCurrentBlockId(instr);
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
        AllocatableValue left = instr.getLeft().loadOperandToReg(this, gen);

        AllocatableValue right = instr.getRight().loadOperandToReg(this, gen);
        AllocatableValue result = new VirtualRegister(instr.type());
        instr.storeOperand(result);
        if (instr.getLeft().isType(JavaKind.Long)) {
            if (left instanceof VirtualRegister) {
                AllocatableValue t = new VirtualRegister(instr.getLeft().type());
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
        AllocatableValue value = instr.getValue().loadOperandToReg(this, gen);
        if (value instanceof VirtualRegister) {
            AllocatableValue newValue = new VirtualRegister(instr.getValue().type());
            gen.emitMov(newValue, value);
            value = newValue;
        }
        AllocatableValue result = new VirtualRegister(instr.type());
        instr.storeOperand(result);
        gen.emitNeg(value, result);
    }

    @Override
    public void visitConstantInstr(ConstantInstr instr) {
        instr.storeOperand(new ConstValue(instr.getConstant()));
    }

    @Override
    public void visitTableSwitchInstr(TableSwitchInstr instr) {
        instr.storeOperand(AllocatableValue.ILLEGAL);
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
        AllocatableValue object = instr.getObject().loadOperandToReg(this, gen);
        AllocatableValue result = new VirtualRegister(instr.type());
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
        if (index instanceof ConstValue) {
            address = new Address(array, index, instr.getElementType());
        } else {
            address = new Address(array, index, Address.scaleFor(instr.getElementType()), YarrowConfigAccess.access().arrayClassElementOffset, instr.getElementType());
        }
        gen.emitMov(result, address);
    }

    @Override
    public void visitArithmeticInstr(ArithmeticInstr instr) {
        AllocatableValue left = instr.getLeft().loadOperandToReg(this, gen);
        AllocatableValue right = instr.getRight().loadOperandToReg(this, gen);
        AllocatableValue result = new VirtualRegister(instr.type());
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
        AllocatableValue array = instr.getArray().loadOperandToReg(this, gen);
        AllocatableValue result = new VirtualRegister(instr.type());
        instr.storeOperand(result);
        Address addr = new Address(array, AllocatableValue.ILLEGAL, 1, YarrowConfigAccess.access().getArrayLengthOffset(), JavaKind.Int);
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
        instr.storeOperand(AllocatableValue.ILLEGAL);
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
        instr.storeOperand(AllocatableValue.ILLEGAL);
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
        AllocatableValue fromOperand = instr.getFrom().loadOperandToReg(this, gen);
        AllocatableValue fromResult = new VirtualRegister(instr.type());
        instr.storeOperand(fromResult);

        AllocatableValue toOperand = fromOperand;
        AllocatableValue toResult = fromResult;

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
        AllocatableValue result = new VirtualRegister(instr.type());
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
        HirInstr[] param = new HirInstr[instr.getArguments().length + (instr.hasReceiver() ? 1 : 0)];
        int i = 0;
        if (param.length > 0) {
            if (instr.hasReceiver()) {
                param[i++] = instr.getReceiver();
            }
            for (int j = 0; i < param.length; i++, j++) {
                param[i] = instr.getArguments()[j];
            }
        }

        CallingConvention cc = YarrowRuntime.regConfig.getCallingConvention(
                HotSpotCallingConventionType.JavaCall, sig.getReturnType(null), paramTypes, valueFactory);
        AllocatableValue receiver = AllocatableValue.ILLEGAL;
        AllocatableValue resultRegister = AllocatableValue.ILLEGAL;
        if (instr.getSignature().getReturnKind() != JavaKind.Void) {
            resultRegister = new VirtualRegister(YarrowRuntime.regConfig.getReturnRegister(instr.getSignature().getReturnKind()));
        }
        AllocatableValue[] args = cc.getArguments();
        for (i = 0; i < param.length; i++) {
            if (args[i] instanceof RegisterValue) {
                param[i].loadOperandToReg(this, gen, new VirtualRegister(((RegisterValue) args[i]).getRegister()));
            } else if (args[i] instanceof StackSlot) {
                StackSlot slot = (StackSlot) args[i];
                gen.emitMov(slot, param[i].loadOperandRaw());
            } else {
                YarrowError.shouldNotReachHere();
            }
        }
        if (instr.hasReceiver()) {
            YarrowError.unimplemented();
        }
        switch (instr.getOpcode()) {

            case Bytecode.INVOKESTATIC:
                gen.emitJavaCall(Mnemonic.CALL_STATIC, resultRegister, instr.getMethod(), AllocatableValue.ILLEGAL, args);
                break;
            case Bytecode.INVOKEINTERFACE:
            case Bytecode.INVOKESPECIAL:
            case Bytecode.INVOKEVIRTUAL: {
                if (instr.getOpcode() == Bytecode.INVOKESPECIAL) {
                    gen.emitJavaCall(Mnemonic.CALL_OPTVIRTUAL, resultRegister, instr.getMethod(), receiver, args);
                } else {
                    gen.emitJavaCall(Mnemonic.CALL_ICVIRTUAL, resultRegister, instr.getMethod(), receiver, args);
                }
                break;
            }
            case Bytecode.INVOKEDYNAMIC:
                gen.emitJavaCall(Mnemonic.CALL_DYNAMIC, resultRegister, instr.getMethod(), receiver, args);
                break;
            default:
                YarrowError.unimplemented();
        }

        if (resultRegister != AllocatableValue.ILLEGAL) {
            AllocatableValue result = new VirtualRegister(instr.type());
            instr.storeOperand(result);
            gen.emitMov(result, resultRegister);
        }
    }

    @Override
    public void visitGotoInstr(GotoInstr instr) {
        instr.storeOperand(AllocatableValue.ILLEGAL);
        new PhiResolver(gen).resolve(instr.getSuccessor(), instr.getVmState());
        gen.emitJmp(instr.getSuccessor().get(0));
    }

    @Override
    public void visitReturnInstr(ReturnInstr instr) {
        if (instr.isType(JavaKind.Void)) {
            gen.emitReturn(AllocatableValue.ILLEGAL);
            instr.storeOperand(AllocatableValue.ILLEGAL); // ReturnInstr has no operand result
            return;
        }

        VirtualRegister retReg = new VirtualRegister(YarrowRuntime.regConfig.getReturnRegister(instr.type()));
        AllocatableValue left = instr.getReturnValue().loadOperandToReg(this, gen, retReg);
        gen.emitReturn(left);
        instr.storeOperand(AllocatableValue.ILLEGAL); // ReturnInstr has no operand result
    }

    @Override
    public void visitOp2Instr(Op2HirInstr instr) {
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
        AllocatableValue result = new VirtualRegister(instr.type());
        gen.emitMov(result, retReg);
        instr.storeOperand(result);
    }

}
