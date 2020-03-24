# yarrow 

## Preface
This is my graduation project, it still work in progress. I intend to write an optimizing JIT compiler for HotSpot VM. 
Thanks to [JEP243](http://openjdk.java.net/jeps/243) JVMCI, I can easily plug a compiler into
JVM at runtime with `-XX:+EnableJVMCI -XX:+UseJVMCICompiler -Djvmci.Compiler=yarrow` options.
Since JVMCI is an experimental feature and it only exposes its services to Graal compiler backend, 
which is a default implementation of JVMCI, I have to hack it so that JVMCI services can be exported 
to my yarrow module. For the sake of the simplicity, I just modify the `module-info.java` from JVMCI 
module and rebuild entire JDK. Back to my project, yarrow is highly inspired by Client Compiler for 
HotSpot VM(aka. C1). As every knows, intermediate representation are the stepping stone form what the
programmer wrote to what the machine understands. Intermediate representations must bridge a large semantic
gap. Yarrow uses a two-tiered control flow graph containing basic blocks (tier 1) of SSA
instructions(tier 2) HIR.

The whole compilation is divided into two parts. yarrow parses Java bytecode to HIR as soon as yarrow
polls a compilation task from compile queue. In order to achieve transformation, compiler finds leader
instructions and creates a control flow graph within bytecode, the minimal component of control flow grpah
is basic block, it connects to other blocks by `successors` field. 
ontrol flow graph becomes 1-Tier of HIR, you can dump the final graph by switching `-Dyarrow.Debug.PrintIRToFile=true`,
You can understand what compiler's internal does by the following demo:

**Example 1: Source Code**
```java
static void yarrow_complex(int k) {
        int val = 12;
        if (k >= 100) {
            if (k >= 200) {
                if (k >= 400) {
                    val += (val ^ 32);
                    int res = val + 1;
                    double d = res;
                }
            }
        }
        for (int i = val; i >= 0; i--) {
            if (i % 2 == 0) {
                continue;
            }
            if (i + val >= 100) {
                val -= 100;
            } else {
                for (int t = i; t < i + 10; t++) {
                    val += t;
                    switch (t) {
                        case 23:
                            val += 32;
                            break;
                        case 323:
                            val += 23;
                        case 32:
                            val += 3233;
                            break;
                        default:
                            val = 44;
                    }
                }
                break;
            }
        }
    }
```

**Example 1: Control Flow Graph**

![](doc/ControlTest_yarrow_complex_phase0.png)

**Example 2: Source Code**

```java
ublic static int[][] fillMatrix(int[][] M) {
        for (int xx = 0; xx < 100000; xx++) {
            int ml = 0;
            for (int i = 0; i < M.length; i++) {
                ml = ml < M[i].length ? M[i].length : ml;
            }
            int[][] Nm = new int[M.length][ml];
            for (int i = 0; i < M.length; i++) {
                for (int j = 0; j < M[i].length; j++) {
                    Nm[i][j] = M[i][j];
                }
            }
            return Nm;
        }
        return null;
    }
```

**Example 2: Control Flow Graph**

![](doc/MatrixTest_fillMatrix_phase0.png)

Later, a so-called [abstract interpretation](https://en.wikipedia.org/wiki/Abstract_interpretation) phase
interprets bytecode and generates corresponding SSA instructions, SSA form needs to merge different 
values of same variable. Therefore, if a basic block has more than one predecessor, PhiInstr might
be needed at the start of block. Again, you can dump graph using mentioned option:

**Example 1: SSA Form**

![](doc/ControlTest_yarrow_complex_phase1.png)

**Example 2: SSA Form**

![](doc/MatrixTest_fillMatrix_phase1.png)

The simple structure of the HIR allows the easy implementation of global optimizations, which are applied
both during and after the construction of HIR. Theoretically, all optimizations developed for traditional
compilers could be applied, but most of them require the analysis of the data flow and are too time-consuming for 
JIT compiler, so yarrow implements only simple and fast optimizations, that's why it is called optimizing
compiler. The most classic optimizations such as constant folding,algebraic simplification and simple constant
propagation will be applied as soon as instruction tries to insert it into a basic block

![](doc/IdealTest_main_phase0.png)

Compiler idealizes many instructions, it reduces computations and chooses a simple canonical form:

![](doc/IdealTest_main_phase1.png)

Also compiler removes unreachable blocks

![](doc/IdealTest_main_phase0.png)

![](doc/IdealTest_main_phase1.png)

Compiler uses local value numbering to find whether newly appended instruction can be replaced by previous 
instruction

![](doc/LVNTest_lvn_phase0.png)

![](doc/LVNTest_lvn_phase1.png)

This could eliminate many redundant field access operations and computations. 
After that, yarrow lowers HIR, it transforms machine-independent HIR instructions
to machine instructions and eliminates dead code and PHI instruction, newly created LIR plays the main 
role of code generation, instruction selection based on BURS, I'm not sure which register allocation
algorithm would be implemented. If I have enough time, I will examine a peephole optimization phase
for LIR.

## Example
Let say we have following java code, it repeats many times to calculate the sum of `[1,n]`:
```java
package com.kelthuzadx.yarrow.test;

public class ControlTest {
    public static int sum(int base) {
        int r = base;
        for (int i = 0; i < 10; i++) {
            r += i * 1 / 1;
        }
        return r;
    }
    
    public static void main(String[] args) {
        for (int i = 0; i < 999998; i++) {
            sum(i);
        }
    }
}
```
Since I only care about method *sum*, I can tell JVM my intention:
```bash
-XX:+UnlockExperimentalVMOptions
-XX:+EnableJVMCI
-XX:+UseJVMCICompiler
-Djvmci.Compiler=yarrow
-Xcomp
-Dyarrow.Debug.PrintCFG=true
-Dyarrow.Debug.PrintIR=true
-Dyarrow.Debug.PrintIRToFile=true
-XX:CompileCommand=compileonly,*SumTest.sum
```
Yarrow would output its internal intermediate representation. Furthermore, yarrow can
dump IR to `*.dot` file which can be used by [graphviz](http://www.graphviz.org/), this 
facilitates knowing what happens inside optimizing compiler.
The following firgure shows the frist step compiler does, it finds leader instructions which 
can split control flow and builds the complete control flow graph

![](doc/SumTest_sum_phase1.png)

After that, yarrow generates corresponding SSA instructions

![](doc/SumTest_sum_phase2.png)
