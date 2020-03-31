package com.kelthuzadx.yarrow.lir.operand;

import jdk.vm.ci.meta.JavaConstant;
import jdk.vm.ci.meta.JavaKind;

public class ConstValue extends LirOperand {
    private JavaConstant constant;

    ConstValue(JavaConstant constant) {
        this.constant = constant;
    }

    @Override
    public JavaKind getJavaKind() {
        return constant.getJavaKind();
    }

    @Override
    public boolean isConstValue() {
        return true;
    }

    @Override
    public boolean isVirtualRegister() {
        return false;
    }

    @Override
    public boolean isStackVar() {
        return false;
    }

    @Override
    public boolean isAddress() {
        return false;
    }

    @Override
    public String toString() {
        if(constant.getJavaKind()==JavaKind.Int){
            return "0x"+Integer.toHexString(Integer.parseInt(constant.toValueString()));
        }else if(constant.getJavaKind()== JavaKind.Long){
            return  "0x"+Long.toHexString(Long.parseLong(constant.toValueString()));
        }
        return constant.toValueString();
    }
}
