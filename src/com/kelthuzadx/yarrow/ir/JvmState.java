package com.kelthuzadx.yarrow.ir;

import com.kelthuzadx.yarrow.ir.node.ValueNode;
import jdk.vm.ci.hotspot.HotSpotResolvedJavaMethod;

public class JvmState {
    private ValueNode[] local;
    private ValueNode[] stack;
    private int rsp;

    JvmState(HotSpotResolvedJavaMethod method){
        this.local = new ValueNode[method.getMaxLocals()];
        this.stack = new ValueNode[method.getMaxStackSize()];
        this.rsp = 0;
    }

    public ValueNode loadLocal(int index){
        return local[index];
    }

    public void pushStack(ValueNode node){
        stack[rsp++] = node;
    }

}
