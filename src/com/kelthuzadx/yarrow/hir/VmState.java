package com.kelthuzadx.yarrow.hir;

import com.kelthuzadx.yarrow.hir.instr.InstanceOfInstr;
import com.kelthuzadx.yarrow.hir.instr.Instruction;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

@SuppressWarnings("unused")
public class VmState {
    private Stack<Instruction> stack;
    private Instruction[] local;
    private List<Instruction> lock;

    public VmState(int stackSize, int localSize){
        stack = new Stack<>();
        local = new Instruction[localSize];
        lock =  new ArrayList<>();
    }

    public void push(Instruction instr){
        stack.push(instr);
    }

    public Instruction pop(){
        return stack.pop();
    }

    public void set(int index, Instruction instr){
        local[index] = instr;
    }

    public Instruction get(int index){
        return local[index];
    }

    public int lockPush(InstanceOfInstr object){
        return 0;
    }

    public int lockPop(){
        return 0;
    }
}
