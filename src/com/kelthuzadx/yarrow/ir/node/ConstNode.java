package com.kelthuzadx.yarrow.ir.node;

import jdk.vm.ci.meta.Constant;
import jdk.vm.ci.meta.JavaConstant;

public class ConstNode extends ValueNode{
    private Constant constant;

    public ConstNode(Constant constant) {
        this.constant = constant;
    }
}
