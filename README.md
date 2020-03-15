# yarrow 

## Preface
This is my graduation project. I intend to write an optimizing JIT compiler for HotSpot VM. 
Thanks to [JEP243](http://openjdk.java.net/jeps/243) JVMCI, I can easily plug a compiler into
VM at runtime with `-XX:+EnableJVMCI -XX:+UseJVMCICompiler ` options. Since JVMCI is an experimental
feature and it only exposes its services to Graal compiler backend, which is a default implementation 
of JVMCI, I have to hack it so that JVMCI services can be exported to my yarrow module. For the sake of 
the simplicity, I just modify the `module-info.java` from JVMCI module and rebuild entire JDK.

# Internals
Back to my project, the whole compilation is divided into two parts.  
yarrow parses Java bytecode to HIR as soon as yarrow polls a compile task from compile queue,
A so-called [abstract interpretation](https://en.wikipedia.org/wiki/Abstract_interpretation) phase
interprets bytecode and generate corresponding SSA instruction, SSA form need to merge different 
values of same variable. Therefore, if a basic block has more than one predecessor, PhiInstr might
be needed at the start of block. Analyses and classic optimization phases will be applied when 
parsing completes. After that, yarrow lowers HIR, it transforms machine-independent HIR instructions
to machine instructions and eliminates dead code and PHI instruction, newly created LIR plays the main 
role of code generation, instruction selection based on BURS, I'm not sure which register allocation
algorithm would be implemented. If I have enough time, I will examine a peephole optimization phase
for LIR.

# Usage
```
-XX:+UnlockExperimentalVMOptions
-XX:+EnableJVMCI
-XX:+UseJVMCICompiler
-Djvmci.Compiler=yarrow
-Xcomp
-Dyarrow.Debug.PrintCFG=true
-Dyarrow.Debug.PrintIR=true
-Dyarrow.Debug.PrintIRToFile=true
-XX:CompileCommand=compileonly,*<class>.<method>
```

## Reference 
[1] https://docs.oracle.com/javase/specs/jvms/se12/html/jvms-6.html#jvms-6.5.ldc
