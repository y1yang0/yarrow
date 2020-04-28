package com.kelthuzadx.yarrow.hir;

import com.kelthuzadx.yarrow.core.YarrowError;
import com.kelthuzadx.yarrow.hir.instr.BlockStartInstr;
import com.kelthuzadx.yarrow.hir.instr.HirInstr;
import com.kelthuzadx.yarrow.hir.instr.ParamInstr;
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
    private Stack<HirInstr> stack;
    private HirInstr[] local;
    private List<HirInstr> lock;

    public VmState(int maxStackSize, int localSize) {
        stack = new Stack<>();
        stack.ensureCapacity(maxStackSize);
        local = new HirInstr[localSize];
        lock = new ArrayList<>();
        this.maxStackSize = maxStackSize;
    }

    public void push(JavaKind type, HirInstr instr) {
        YarrowError.guarantee(instr.isType(type), "type mismatch");
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

    public void unsafePush(HirInstr instr) {
        stack.push(instr);
    }

    public HirInstr unsafePop() {
        return stack.pop();
    }

    public HirInstr pop(JavaKind type) {
        switch (type) {
            case Int:
            case Float:
            case Object: {
                var val = stack.pop();
                YarrowError.guarantee(val.isType(type), "type mismatch");
                return val;
            }
            case Long:
            case Double: {
                HirInstr placeholder = stack.pop();
                if (placeholder != null) {
                    throw new YarrowError("Must be null slot");
                }
                var val = stack.pop();
                YarrowError.guarantee(val.isType(type), "type mismatch");
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

    public Stack<HirInstr> getStack() {
        return stack;
    }

    public void set(int index, HirInstr instr) {
        local[index] = instr;
    }

    public HirInstr get(int index) {
        return local[index];
    }

    public int getLocalSize() {
        return local.length;
    }

    public HirInstr lock(HirInstr object) {
        lock.add(object);
        return lock.get(lock.size() - 1);
    }

    public HirInstr unlock() {
        HirInstr object = lock.get(lock.size() - 1);
        lock.remove(lock.size() - 1);
        return object;
    }

    public int getLockSize() {
        return lock.size();
    }

    public List<HirInstr> getLock() {
        return lock;
    }

    public HirInstr[] getLocal() {
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
        PhiInstr phi = new PhiInstr(stack.elementAt(index).type(), -index - 1, block);
        stack.set(index, phi);
    }

    public void createPhiForLocal(BlockStartInstr block, int index) {
        PhiInstr phi = new PhiInstr(local[index].type(), index, block);
        local[index] = phi;
    }

    @Override
    public String toString() {
        String sk = stack.stream().map(instr -> "i" + instr.id()).collect(Collectors.joining(","));
        String lc = Arrays.stream(local).map(instr -> {
            if (instr == null) {
                return null;
            }
            if (instr instanceof ParamInstr) {
                return instr.toString();
            } else {
                if(instr instanceof PhiInstr){
                    return instr.toString();
                }
                return "i" + instr.id();
            }
        }).collect(Collectors.joining(","));
        String lx = lock.stream().map(instr ->
                instr == null ?
                        "null" :
                        (instr instanceof PhiInstr?
                                ((PhiInstr)instr).toString():
                                ("i" + instr.id())
                        )
        ).collect(Collectors.joining(","));
        return "VmState{" +
                "lock=[" + lx +
                "],stack=[" + sk +
                "],local=[" + lc +
                "]}";
    }
}
