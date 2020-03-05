package com.kelthuzadx.yarrow.ir;

import com.kelthuzadx.yarrow.ir.hir.Instruction;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

@SuppressWarnings("unused")
public class VmState {
    private Stack<Instruction> stack;
    private List<Instruction> local;
    private List<Instruction> lock;

    public VmState(){
        stack = new Stack<>();
        local = new ArrayList<>();
        lock = new ArrayList<>();
    }

    public VmState(int stackSize, int localSize){
        stack = new Stack<>();
        stack.ensureCapacity(stackSize);
        local = new ArrayList<>(localSize);
        lock =  new ArrayList<>();
    }

    public void push(Instruction instr){
        stack.add(instr);
    }

    public Instruction pop(){
        return stack.pop();
    }

    public void set(int index, Instruction instr){
        local.set(index,instr);
    }

    public Instruction get(int index){
        return local.get(index);
    }

}
