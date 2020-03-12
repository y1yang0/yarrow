package com.kelthuzadx.yarrow.hir;

import com.kelthuzadx.yarrow.hir.instr.BlockStartInstr;
import com.kelthuzadx.yarrow.hir.instr.InstanceOfInstr;
import com.kelthuzadx.yarrow.hir.instr.Instruction;
import com.kelthuzadx.yarrow.hir.instr.PhiInstr;
import jdk.vm.ci.meta.JavaKind;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

@SuppressWarnings("unused")
public class VmState {
    private final int stackSize;
    private Stack<Instruction> stack;
    private Instruction[] local;
    private List<Instruction> lock;

    public VmState(int stackSize, int localSize){
        stack = new Stack<>();
        stack.ensureCapacity(stackSize);
        local = new Instruction[localSize];
        lock =  new ArrayList<>();
        this.stackSize = stackSize;
    }

    public void push(Instruction instr){
        if(stack.size()+1>stackSize){
            throw new RuntimeException("stack excess maximum capacity");
        }
        stack.push(instr);
    }

    public Instruction pop(){
        return stack.pop();
    }

    public int getStackSize(){
        return stackSize;
    }

    public void set(int index, Instruction instr){
        local[index] = instr;
    }

    public Instruction get(int index){
        return local[index];
    }

    public int getLocalSize(){
        return local.length;
    }

    public int lockPush(InstanceOfInstr object){
        return 0;
    }

    public int lockPop(){
        return 0;
    }

    public VmState copy(){
        VmState newState = new VmState(this.stackSize,this.local.length);
        newState.stack.addAll(this.stack);
        System.arraycopy(this.local, 0, newState.local, 0, newState.local.length);
        newState.lock.addAll(this.lock);
        return newState;
    }

    public void createPhiForStack(BlockStartInstr block,int index){
        Value val = new Value(stack.elementAt(index).getType());
        PhiInstr phi = new PhiInstr(val,-index-1,block);
        stack.set(index,phi);
    }

    public void createPhiForLocal(BlockStartInstr block,int index){
        Value val = new Value(local[index].getType());
        PhiInstr phi = new PhiInstr(val,-index-1,block);
        local[index] = phi;
    }
}
