stubs=["CompilerRuntime::backedge_event",
"CompilerRuntime::initialize_klass_by_symbol",
"CompilerRuntime::invocation_event",
"CompilerRuntime::resolve_dynamic_invoke",
"CompilerRuntime::resolve_klass_by_symbol",
"CompilerRuntime::resolve_method_by_symbol_and_load_counters",
"CompilerRuntime::resolve_string_by_symbol",
"Deoptimization::fetch_unroll_info",
"Deoptimization::uncommon_trap",
"Deoptimization::unpack_frames",
"JVMCIRuntime::dynamic_new_array",
"JVMCIRuntime::dynamic_new_array_or_null",
"JVMCIRuntime::dynamic_new_instance",
"JVMCIRuntime::dynamic_new_instance_or_null",
"JVMCIRuntime::exception_handler_for_pc",
"JVMCIRuntime::identity_hash_code",
"JVMCIRuntime::load_and_clear_exception",
"JVMCIRuntime::log_object",
"JVMCIRuntime::log_primitive",
"JVMCIRuntime::log_printf",
"JVMCIRuntime::monitorenter",
"JVMCIRuntime::monitorexit",
"JVMCIRuntime::new_array",
"JVMCIRuntime::new_array_or_null",
"JVMCIRuntime::new_instance",
"JVMCIRuntime::new_instance_or_null",
"JVMCIRuntime::new_multi_array",
"JVMCIRuntime::new_multi_array_or_null",
"JVMCIRuntime::object_notify",
"JVMCIRuntime::object_notifyAll",
"JVMCIRuntime::test_deoptimize_call_int",
"JVMCIRuntime::thread_is_interrupted",
"JVMCIRuntime::throw_and_post_jvmti_exception",
"JVMCIRuntime::throw_class_cast_exception",
"JVMCIRuntime::throw_klass_external_name_exception",
"JVMCIRuntime::validate_object",
"JVMCIRuntime::vm_error",
"JVMCIRuntime::vm_message",
"JVMCIRuntime::write_barrier_post",
"JVMCIRuntime::write_barrier_pre",
"SharedRuntime::OSR_migration_end",
"SharedRuntime::drem",
"SharedRuntime::enable_stack_reserved_zone",
"SharedRuntime::exception_handler_for_return_address",
"SharedRuntime::frem",
"SharedRuntime::register_finalizer",
"os::dll_load",
"os::dll_lookup",
"os::javaTimeMillis",
"os::javaTimeNanos"]

def camelCase(st):
    output = ''.join(x for x in st.title() if x.isalnum())
    output = output.replace("_","")
    return output[0].lower() + output[1:]

def pascalCase(st):
    st = st.title()
    st = st.replace("_","")
    return st

def gen(klass,method):
    content = """Stub{}("{}::{}",YarrowRuntime.access.getAddress("{}::{}")),
""".format(pascalCase(method),klass,method,klass,method)
    return content

####!!! Special handle for RTLD_DEFAULT
for val in stubs:
    a = val[:val.find(":")]
    b = val[val.find(":")+2:]
    content = gen(a,b)
    print(content)

