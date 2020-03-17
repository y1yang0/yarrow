package com.kelthuzadx.yarrow.optimize;

import com.kelthuzadx.yarrow.hir.HIR;
import com.kelthuzadx.yarrow.hir.instr.AccessArrayInstr;
import com.kelthuzadx.yarrow.hir.instr.BlockEndInstr;
import com.kelthuzadx.yarrow.hir.instr.BlockStartInstr;
import com.kelthuzadx.yarrow.hir.instr.Instruction;
import jdk.vm.ci.code.MemoryBarriers;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

public class Ideal implements Optimizer {
    private HIR hir;
    private Queue<BlockStartInstr> workList;
    private Set<Integer> visit;

    public Ideal(){}

    @DecorateTarget(klass = AccessArrayInstr.class)
    public void doAccessArrayInstr(AccessArrayInstr instr){
        System.out.println("called");
    }

    @Override
    public HIR optimize(HIR hir) {
        this.hir = hir;
        this.workList = new ArrayDeque<>();
        this.visit = new HashSet<>();


        BlockStartInstr start = hir.getEntryBlock();
        workList.add(start);
        while(!workList.isEmpty()){
            var block = workList.remove();
            if(!visit.contains(block.getInstrId())){
                visit.add(block.getInstrId());
                {
                    BlockEndInstr end = block.getBlockEnd();
                    Instruction cur = block;
                    while(cur!=end){
                        dispatch(cur);
                        cur=cur.getNext();
                    }
                    dispatch(cur);
                }
                workList.addAll(block.getBlockEnd().getSuccessor());
            }
        }
        return hir;
    }

    private <T extends Class<? extends Instruction>> void dispatch(Instruction instr){
        Method[] methods = Ideal.class.getMethods();
        for(Method method:methods){
            var anno = method.getAnnotation(DecorateTarget.class);
            if(anno!=null){

            }
        }
    }
}
