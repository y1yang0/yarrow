package com.kelthuzadx.yarrow.ir;

import jdk.vm.ci.meta.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Block {
    private int id;
    private int startBci;
    private int endBci;
    private List<Block> successor;
    private boolean mayThrowEx;
    private ExceptionHandler xhandler;

    Block(int id, int bci) {
        this.id = id;
        this.startBci = this.endBci = bci;
        this.successor = new ArrayList<>();
        this.mayThrowEx = false;
    }

    public int getEndBci() {
        return endBci;
    }

    public int getStartBci() {
        return startBci;
    }

    public void setEndBci(int endBci) {
        this.endBci = endBci;
    }

    public void setStartBci(int startBci) {
        this.startBci = startBci;
    }

    public void addSuccessor(Block block) {
        this.successor.add(block);
    }

    public List<Block> getSuccessor() {
        return successor;
    }

    public void removeSuccessor() {
        this.successor.clear();
    }

    public boolean mayThrowEx() {
        return mayThrowEx;
    }

    public void setMayThrowEx(boolean mayThrowEx) {
        this.mayThrowEx = mayThrowEx;
    }

    public int getId() {
        return id;
    }

    public void setXhandler(ExceptionHandler xhandler) {
        this.xhandler = xhandler;
    }

    public ExceptionHandler getXhandler() {
        return xhandler;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Block){
            return ((Block)obj).id == this.id;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        String successorString = successor.stream().map(
                b-> "#"+b.getId()+" "+(b.xhandler!=null?"!":"")+"["+b.getStartBci()+","+b.getEndBci()+"]"
        ).collect(Collectors.toList()).toString();

        return "#" +
                id +
                " " +
                (xhandler != null ? "!" : "") +
                "[" +
                startBci +
                "," +
                endBci +
                "]" +
                " => " +
                successorString;
    }
}
