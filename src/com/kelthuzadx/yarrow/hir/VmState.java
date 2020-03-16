package com.kelthuzadx.yarrow.hir;

import com.kelthuzadx.yarrow.core.YarrowError;
import com.kelthuzadx.yarrow.hir.instr.BlockStartInstr;
import com.kelthuzadx.yarrow.hir.instr.Instruction;
import com.kelthuzadx.yarrow.hir.instr.PhiInstr;
import jdk.vm.ci.meta.JavaKind;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class VmState {
    private final int maxStackSize;
    private Stack<Instruction> stack;
    private Instruction[] local;
    private List<Instruction> lock;

    public VmState(int maxStackSize, int localSize) {
        stack = new Stack<>();
        stack.ensureCapacity(maxStackSize);
        local = new Instruction[localSize];
        lock = new ArrayList<>();
        this.maxStackSize = maxStackSize;
    }

    public void push(JavaKind type, Instruction instr) {
        switch (type) {
            case Int:
            case Float:
            case Object:
                if (stack.size() + 1 > maxStackSize) {
                    throw new YarrowError("stack excess maximum capacity");
                }
                stack.push(instr);
                break;
            case Long:
            case Double:
                if (stack.size() + 2 > maxStackSize) {
                    throw new YarrowError("stack excess maximum capacity");
                }
                stack.push(instr);
                stack.push(null);
                break;
            default:
                YarrowError.shouldNotReachHere();
        }

    }

    public void unsafePush(Instruction instr) {
        stack.push(instr);
    }

    public Instruction unsafePop() {
        return stack.pop();
    }

    public Instruction pop(JavaKind type) {
        switch (type) {
            case Int:
            case Float:
            case Object: {
                var val = stack.pop();
                Instruction.assertType(val, type);
                return val;
            }
            case Long:
            case Double: {
                Instruction placeholder = stack.pop();
                if (placeholder != null) {
                    throw new YarrowError("Must be null slot");
                }
                var val = stack.pop();
                Instruction.assertType(val, type);
                return val;
            }
            default:
                YarrowError.shouldNotReachHere();
        }
        return null;
    }

    public int getStackSize() {
        return stack.size();
    }

    public Stack<Instruction> getStack() {
        return stack;
    }

    public void set(int index, Instruction instr) {
        local[index] = instr;
    }

    public Instruction get(int index) {
        return local[index];
    }

    public int getLocalSize() {
        return local.length;
    }

    public Instruction lock(Instruction object) {
        lock.add(object);
        return lock.get(lock.size() - 1);
    }

    public Instruction unlock() {
        Instruction object = lock.get(lock.size() - 1);
        lock.remove(lock.size() - 1);
        return object;
    }

    public int getLockSize() {
        return lock.size();
    }

    public List<Instruction> getLock() {
        return lock;
    }

    public Instruction[] getLocal() {
        return local;
    }

    public VmState copy() {
        VmState newState = new VmState(this.maxStackSize, this.local.length);
        newState.stack.addAll(this.stack);
        System.arraycopy(this.local, 0, newState.local, 0, newState.local.length);
        newState.lock.addAll(this.lock);
        return newState;
    }

    public void createPhiForStack(BlockStartInstr block, int index) {
        Value val = new Value(stack.elementAt(index).getType());
        PhiInstr phi = new PhiInstr(val, -index - 1, block);
        stack.set(index, phi);
    }

    public void createPhiForLocal(BlockStartInstr block, int index) {
        Value val = new Value(local[index].getType());
        PhiInstr phi = new PhiInstr(val, index, block);
        local[index] = phi;
    }

    @Override
    public String toString() {
        String sk = stack.stream().map(instr -> "" + instr.getId()).collect(Collectors.joining(","));
        String lc = Arrays.stream(local).map(instr -> "" + instr).collect(Collectors.joining(","));
        String lx = lock.stream().map(instr -> instr == null ? "" : "" + instr.getId()).collect(Collectors.joining(","));
        return "VmState{" +
                "lock=[" + lx +
                "],stack=[" + sk +
                "],local=[" + lc +
                "]}";
    }
}
