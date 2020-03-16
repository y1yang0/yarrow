import os,re,sys
from subprocess import Popen, PIPE

def join(arr):
    return " ".join(arr)

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

    class_path =  "-p "+os.getcwd()+ "/../target/classes -m yarrow/com.kelthuzadx.yarrow.test."

    invoke = class_name+"."+method_name
    cmd = jdk+" "
    cmd += join(option_jvmci)+" "
    cmd += join(option_debug)+invoke+" "
    cmd += class_path+class_name
    print(cmd)
    return cmd

def extract_target_method(output):
    match = re.match(r'Compiled\sfrom\s"(.*?)\.java\"',output)
    class_name = match.group(1)
    pattern = re.compile(r'yarrow_(.*?)\(.*?\).*;')
    method_names = pattern.findall(output)
    return (class_name,method_names)

def run_test(class_name,method_name):
    cmd = build_cmd(class_name,method_name)
    os.system(cmd)

def run_all_test():
    pwd = os.getcwd()
    target_dir=pwd+"/../target/classes/com/kelthuzadx/yarrow/test"
    target_files = os.listdir(target_dir)
    for filename in target_files:
        if "$" not in filename:
            p = Popen(['javap', '-p', target_dir+"/"+filename], stdout=PIPE, stderr=PIPE, stdin=PIPE)
            output = p.stdout.read()
            class_name,method_names = extract_target_method(output)
            for method_name in method_names:
                run_test(class_name,method_name)




if __name__ == '__main__':
    if len(sys.argv)==1:
        run_all_test()
    else:
        class_name = sys.argv[1].split(".")[0]
        method_name = sys.argv[1].split(".")[1]
        run_test(class_name,method_name)