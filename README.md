# yarrow

## Introduction
This is my graduation project. I intend to write an optimizing JIT compiler for HotSpot VM. Thanks to JVMCI, I can easily plug a JIT compiler into VM at runtime. Since JVMCI is an internal package now and it only exposes its services to Graal compiler backend, which is a default implementation of JVMCI for JDK9, I have to hack it so that its services can be exported to my yarrow module, for the sake of the simplicity, I just modify the module-info.java from JVMCI module and rebuild JDK.

Back to my project, the whole compilation is divided into two parts. yarrow parses Java bytecode to HIR as soon as yarrow polls a compile task from compile queue, analyses and classic optimization phases will be applied when parsing completes. After that, yarrow lowers HIR, it transforms machine-independent instructions to machine instructions and eliminate dead code and PHI instruction, newly created LIR plays a main role of code generation, instruction selection based on BURS, I'm not sure which register allocation algorithms would be implemented. If I have enough time, I will examine a peephole optimization phase for LIR.


## HIR constuction
构造HIR本质上是一个抽象解释的过程，在抽象解释之前还需要找出所有可能分割基本块的字节码（goto,lookupswitch,tableswitch,if_*cmp)，另外找出循环也是必要的，因为SSA需要使用Phi指令合并数据流，而循环头是控制流合并的发生地。
抽象解释的核心依赖于一个VmState，它遍历所有基本块，即上一步发现的BlockStartInstr，然后对于每个BlockStartInstr编译器解释执行字节码，这一步依赖VmState的操作数栈和局部变量表，当一切完成后将构造出包含SSA指令的基本块，基本块通过前驱和后继指针关联其它基本块，形成一个有向图，即控制流图。

## Reference 
[1] https://docs.oracle.com/javase/specs/jvms/se12/html/jvms-6.html#jvms-6.5.ldc
