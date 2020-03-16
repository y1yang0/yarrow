import os,re,sys
from subprocess import Popen, PIPE

def build_cmd(class_name,method_name):
    jdk="/Users/kelthuzadx/Desktop/hack-jdk12/jdk/bin/java"

    option_jvmci=[
        "-Dfile.encoding=UTF-8",
        "-XX:+UnlockExperimentalVMOptions",
        "-XX:+EnableJVMCI",
        "-XX:+UseJVMCICompiler",
        "-Djvmci.Compiler=yarrow",
        "-Xcomp"
    ]

    option_debug=[
        "-Dyarrow.Debug.PrintCFG=true",
        "-Dyarrow.Debug.PrintIR=true",
        "-Dyarrow.Debug.PrintIRToFile=true",
        "-XX:CompileCommand=compileonly,*"
    ]
    temp  =os.getcwd()+ "/../target/classes"
    class_path =  [
        "-p",
        temp,
        "-m",
        "yarrow/com.kelthuzadx.yarrow.test."
    ]

    invoke = class_name+"."+method_name
    cmd = [jdk]
    cmd.extend(option_jvmci)
    cmd.extend(option_debug)
    cmd[len(cmd)-1] += invoke
    cmd.extend(class_path)
    cmd[len(cmd)-1] += class_name
    return cmd

def colored_output(ok,class_name,method_name):
     tip = "Running "+class_name+"."+method_name+"......"
     OKGREEN = '\033[92m'
     FAIL = '\033[91m'
     ENDC = '\033[0m'
     result = ""
     if ok==0:
         tip = OKGREEN + tip
         result = "Passed"+ENDC
     else:
         tip = FAIL + tip
         result = "Failed"+ENDC
     print("{} {:>30}".format(tip,result))

def run_test(class_name,method_name):
    cmd = build_cmd(class_name,method_name)
    proc = Popen(cmd,stdout=PIPE,stderr=PIPE)
    proc.wait()
    ret = proc.stderr.read()
    ret = len(ret)!=0
    colored_output(ret,class_name,method_name)

def run_all_test():
    pwd = os.getcwd()
    target_dir=pwd+"/../target/classes/com/kelthuzadx/yarrow/test"
    target_files = os.listdir(target_dir)
    for filename in target_files:
        if "$" not in filename:
            run_test(filename[:filename.find(".")],'*')

if __name__ == '__main__':
    if len(sys.argv)==1:
        run_all_test()
    else:
        class_name = sys.argv[1].split(".")[0]
        method_name = sys.argv[1].split(".")[1]
        run_test(class_name,method_name)