package com.kelthuzadx.yarrow.lir;

import com.kelthuzadx.yarrow.hir.VmState;
import com.kelthuzadx.yarrow.hir.instr.BlockStartInstr;
import com.kelthuzadx.yarrow.hir.instr.HirInstr;
import com.kelthuzadx.yarrow.hir.instr.PhiInstr;
import com.kelthuzadx.yarrow.lir.operand.LirOperand;
import com.kelthuzadx.yarrow.lir.operand.VirtualRegister;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * When generating LIR from HIR, I need eliminate all PhiInstr instructions.
 *
 * @author kelthuzadx
 */
public class PhiResolver {
    private final LirGenerator gen;
    private final Map<Integer, ResolveNode> vregMap;
    private final List<ResolveNode> virtualOperand;
    private final List<ResolveNode> otherOperand;

    private ResolveNode loop;
    private LirOperand temp;

    public PhiResolver(LirGenerator gen) {
        this.gen = gen;
        vregMap = new HashMap<>();
        virtualOperand = new ArrayList<>();
        otherOperand = new ArrayList<>();
    }

    public void resolve(List<BlockStartInstr> successor, VmState curState) {
        if (successor.size() == 1) {
            BlockStartInstr succ = successor.get(0);
            if (succ.getPredecessor().size() <= 1) {
                return;
            }

            // now block has at least two predecessor blocks,
            // resolve all PhiInstr in stack and local slots
            VmState succState = succ.getVmState();
            for (int i = 0; i < succState.getStackSize(); i++) {
                moveToPhi(curState.getStack().get(i), succState.getStack().get(i));
            }

            for (int i = 0; i < succState.getLocalSize(); i++) {
                if (succState.getLocal()[i] != null) {
                    moveToPhi(curState.getLocal()[i], succState.getLocal()[i]);
                }
            }

            // generate move from non virtual register to arbitrary destination
            generateMove();
        }
    }

    private void moveToPhi(HirInstr cur, HirInstr sux) {
        if (sux instanceof PhiInstr) {
            PhiInstr phi = (PhiInstr) sux;
            if (cur != null && cur != phi) {
                LirOperand source = cur.loadOperandRaw();
                LirOperand dest = phi.loadOperandRaw();
                if (dest == null) {
                    phi.storeOperand(new VirtualRegister(phi.type()));
                    dest = phi.loadOperandRaw();
                }
                createResolveNode(source, true).append(createResolveNode(dest, false));
            }
        }
    }

    private ResolveNode createResolveNode(LirOperand operand, boolean isSource) {
        ResolveNode resolveNode;

        if (operand.isVirtualRegister()) {
            int vregId = ((VirtualRegister) operand).getVirtualRegisterId();
            vregMap.put(vregId, null);
            resolveNode = vregMap.get(vregId);

            if (resolveNode == null) {
                resolveNode = new ResolveNode(operand);
                vregMap.put(vregId, resolveNode);
            }

            if (isSource && !virtualOperand.contains(resolveNode)) {
                virtualOperand.add(resolveNode);
            }
        } else {
            resolveNode = new ResolveNode(operand);
            otherOperand.add(resolveNode);
        }
        return resolveNode;
    }

    private void generateMove() {
        for (ResolveNode node : virtualOperand) {
            if (!node.isVisited()) {
                move(null, node);
                node.setStartNode(true);
            }
        }

        for (ResolveNode node : otherOperand) {
            for (ResolveNode destNode : node.destinations) {
                gen.emitMov(destNode.operand(), node.operand());
            }
        }
    }

    private void move(ResolveNode src, ResolveNode dest) {
        if (!dest.isVisited()) {
            dest.setVisited(true);
            for (ResolveNode d : dest.getDestinations()) {
                move(dest, d);
            }
        } else if (!dest.isStartNode()) {
            loop = dest;
            temp = new VirtualRegister(src.operand().getJavaKind());
            gen.emitMov(temp, src.operand());
            return;
        }

        if (!dest.isAssigned()) {
            if (loop == dest) {
                temp = new VirtualRegister(src.operand().getJavaKind());
                gen.emitMov(temp, src.operand());
                dest.setAssigned(true);
            } else if (src != null) {
                gen.emitMov(dest.operand(), src.operand());
                dest.setAssigned(true);
            }
        }
    }

    static class ResolveNode {
        private final LirOperand operand;
        private final List<ResolveNode> destinations;
        private boolean visited;
        private boolean assigned;
        private boolean startNode;

        public ResolveNode(LirOperand operand) {
            destinations = new ArrayList<>();
            this.operand = operand;
        }

        public LirOperand operand() {
            return operand;
        }

        public void append(ResolveNode node) {
            destinations.add(node);
        }

        public List<ResolveNode> getDestinations() {
            return destinations;
        }

        public boolean isVisited() {
            return visited;
        }

        public void setVisited(boolean visited) {
            this.visited = visited;
        }

        public boolean isAssigned() {
            return assigned;
        }

        public void setAssigned(boolean assigned) {
            this.assigned = assigned;
        }

        public boolean isStartNode() {
            return startNode;
        }

        public void setStartNode(boolean startNode) {
            this.startNode = startNode;
        }
    }
}
