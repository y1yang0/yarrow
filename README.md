# yarrow

## Introduction
This is my graduation project. I intend to write an optimizing JIT compiler for HotSpot VM. Thanks to JVMCI, I can easily plug a JIT compiler into VM at runtime. Since JVMCI is an internal package now and it only exposes its services to Graal compiler backend, which is a default implementation of JVMCI for JDK9, I have to hack it so that its services can be exported to my yarrow module, for the sake of the simplicity, I just modify the module-info.java from JVMCI module and rebuild JDK.

Back to my project, the whole compilation is divided into two parts. yarrow parses Java bytecode to HIR as soon as yarrow polls a compile task from compile queue, analyses and classic optimization phases will be applied when parsing completes. After that, yarrow lowers HIR, it transforms machine-independent instructions to machine instructions and eliminate dead code and PHI instruction, newly created LIR plays a main role of code generation, instruction selection based on BURS, I'm not sure which register allocation algorithms would be implemented. If I have enough time, I will examine a peephole optimization phase for LIR.


## Reference 
[1] https://docs.oracle.com/javase/specs/jvms/se12/html/jvms-6.html#jvms-6.5.ldc
