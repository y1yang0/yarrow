package com.kelthuzadx.yarrow.core;

import jdk.vm.ci.code.Architecture;
import jdk.vm.ci.code.CodeCacheProvider;
import jdk.vm.ci.code.RegisterConfig;
import jdk.vm.ci.code.TargetDescription;
import jdk.vm.ci.meta.ConstantReflectionProvider;
import jdk.vm.ci.meta.MetaAccessProvider;
import jdk.vm.ci.runtime.JVMCI;
import jdk.vm.ci.runtime.JVMCIRuntime;

public class YarrowRuntime {
    public static YarrowConfigAccess access = YarrowConfigAccess.access();

    public static MetaAccessProvider metaAccess = JVMCI.getRuntime().getHostJVMCIBackend().getMetaAccess();

    public static CodeCacheProvider codeCache = JVMCI.getRuntime().getHostJVMCIBackend().getCodeCache();

    public static ConstantReflectionProvider constReflection = JVMCI.getRuntime().getHostJVMCIBackend().getConstantReflection();

    public static TargetDescription target = JVMCI.getRuntime().getHostJVMCIBackend().getTarget();

    public static Architecture arch = target.arch;

    public static RegisterConfig regConfig = JVMCI.getRuntime().getHostJVMCIBackend().getCodeCache().getRegisterConfig();

    public static long newInstance = 0xdeadbeef;

    public static void initialize(){
        newInstance = access.getAddress("JVMCIRuntime::new_ssinstance");
        access.getAddress("CompilerRuntime::backedge_event");
        access.getAddress("CompilerRuntime::initialize_klass_by_symbol");
        access.getAddress("CompilerRuntime::invocation_event");
        access.getAddress("CompilerRuntime::resolve_dynamic_invoke");
        access.getAddress("CompilerRuntime::resolve_klass_by_symbol");
        access.getAddress("CompilerRuntime::resolve_method_by_symbol_and_load_counters");
        access.getAddress("CompilerRuntime::resolve_string_by_symbol");
        access.getAddress("Deoptimization::fetch_unroll_info");
        access.getAddress("Deoptimization::uncommon_trap");
        access.getAddress("Deoptimization::unpack_frames");
        access.getAddress("JVMCIRuntime::dynamic_new_array");
        access.getAddress("JVMCIRuntime::dynamic_new_array_or_null");
        access.getAddress("JVMCIRuntime::dynamic_new_instance");
        access.getAddress("JVMCIRuntime::dynamic_new_instance_or_null");
        access.getAddress("JVMCIRuntime::exception_handler_for_pc");
        access.getAddress("JVMCIRuntime::identity_hash_code");
        access.getAddress("JVMCIRuntime::load_and_clear_exception");
        access.getAddress("JVMCIRuntime::log_object");
        access.getAddress("JVMCIRuntime::log_primitive");
        access.getAddress("JVMCIRuntime::log_printf");
        access.getAddress("JVMCIRuntime::monitorenter");
        access.getAddress("JVMCIRuntime::monitorexit");
        access.getAddress("JVMCIRuntime::new_array");
        access.getAddress("JVMCIRuntime::new_array_or_null");
        access.getAddress("JVMCIRuntime::new_instance");
        access.getAddress("JVMCIRuntime::new_instance_or_null");
        access.getAddress("JVMCIRuntime::new_multi_array");
        access.getAddress("JVMCIRuntime::new_multi_array_or_null");
        access.getAddress("JVMCIRuntime::object_notify");
        access.getAddress("JVMCIRuntime::object_notifyAll");
        access.getAddress("JVMCIRuntime::test_deoptimize_call_int");
        access.getAddress("JVMCIRuntime::thread_is_interrupted");
        access.getAddress("JVMCIRuntime::throw_and_post_jvmti_exception");
        access.getAddress("JVMCIRuntime::throw_class_cast_exception");
        access.getAddress("JVMCIRuntime::throw_klass_external_name_exception");
        access.getAddress("JVMCIRuntime::validate_object");
        access.getAddress("JVMCIRuntime::vm_error");
        access.getAddress("JVMCIRuntime::vm_message");
        access.getAddress("JVMCIRuntime::write_barrier_post");
        access.getAddress("JVMCIRuntime::write_barrier_pre");
        access.getAddress("RTLD_DEFAULT");
        access.getAddress("SharedRuntime::OSR_migration_end");
        access.getAddress("SharedRuntime::drem");
        access.getAddress("SharedRuntime::enable_stack_reserved_zone");
        access.getAddress("SharedRuntime::exception_handler_for_return_address");
        access.getAddress("SharedRuntime::frem");
        access.getAddress("SharedRuntime::register_finalizer");
        access.getAddress("os::dll_load");
        access.getAddress("os::dll_lookup");
        access.getAddress("os::javaTimeMillis");
        access.getAddress("os::javaTimeNanos");
    }

}
