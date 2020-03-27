package com.kelthuzadx.yarrow.lir;

import com.kelthuzadx.yarrow.core.YarrowRuntime;

public class RuntimeStub {
    public static long RTLD_DEFAULT = 0xdeadbeef;

    static {
        CompilerRuntime.backedge_event = YarrowRuntime.access.getAddress("CompilerRuntime::backedge_event");
        CompilerRuntime.initialize_klass_by_symbol = YarrowRuntime.access.getAddress("CompilerRuntime::initialize_klass_by_symbol");
        CompilerRuntime.invocation_event = YarrowRuntime.access.getAddress("CompilerRuntime::invocation_event");
        CompilerRuntime.resolve_dynamic_invoke = YarrowRuntime.access.getAddress("CompilerRuntime::resolve_dynamic_invoke");
        CompilerRuntime.resolve_klass_by_symbol = YarrowRuntime.access.getAddress("CompilerRuntime::resolve_klass_by_symbol");
        CompilerRuntime.resolve_method_by_symbol_and_load_counters = YarrowRuntime.access.getAddress("CompilerRuntime::resolve_method_by_symbol_and_load_counters");
        CompilerRuntime.resolve_string_by_symbol = YarrowRuntime.access.getAddress("CompilerRuntime::resolve_string_by_symbol");
        Deoptimization.fetch_unroll_info = YarrowRuntime.access.getAddress("Deoptimization::fetch_unroll_info");
        Deoptimization.uncommon_trap = YarrowRuntime.access.getAddress("Deoptimization::uncommon_trap");
        Deoptimization.unpack_frames = YarrowRuntime.access.getAddress("Deoptimization::unpack_frames");
        JVMCIRuntime.dynamic_new_array = YarrowRuntime.access.getAddress("JVMCIRuntime::dynamic_new_array");
        JVMCIRuntime.dynamic_new_array_or_null = YarrowRuntime.access.getAddress("JVMCIRuntime::dynamic_new_array_or_null");
        JVMCIRuntime.dynamic_new_instance = YarrowRuntime.access.getAddress("JVMCIRuntime::dynamic_new_instance");
        JVMCIRuntime.dynamic_new_instance_or_null = YarrowRuntime.access.getAddress("JVMCIRuntime::dynamic_new_instance_or_null");
        JVMCIRuntime.exception_handler_for_pc = YarrowRuntime.access.getAddress("JVMCIRuntime::exception_handler_for_pc");
        JVMCIRuntime.identity_hash_code = YarrowRuntime.access.getAddress("JVMCIRuntime::identity_hash_code");
        JVMCIRuntime.load_and_clear_exception = YarrowRuntime.access.getAddress("JVMCIRuntime::load_and_clear_exception");
        JVMCIRuntime.log_object = YarrowRuntime.access.getAddress("JVMCIRuntime::log_object");
        JVMCIRuntime.log_primitive = YarrowRuntime.access.getAddress("JVMCIRuntime::log_primitive");
        JVMCIRuntime.log_printf = YarrowRuntime.access.getAddress("JVMCIRuntime::log_printf");
        JVMCIRuntime.monitorenter = YarrowRuntime.access.getAddress("JVMCIRuntime::monitorenter");
        JVMCIRuntime.monitorexit = YarrowRuntime.access.getAddress("JVMCIRuntime::monitorexit");
        JVMCIRuntime.new_array = YarrowRuntime.access.getAddress("JVMCIRuntime::new_array");
        JVMCIRuntime.new_array_or_null = YarrowRuntime.access.getAddress("JVMCIRuntime::new_array_or_null");
        JVMCIRuntime.new_instance = YarrowRuntime.access.getAddress("JVMCIRuntime::new_instance");
        JVMCIRuntime.new_instance_or_null = YarrowRuntime.access.getAddress("JVMCIRuntime::new_instance_or_null");
        JVMCIRuntime.new_multi_array = YarrowRuntime.access.getAddress("JVMCIRuntime::new_multi_array");
        JVMCIRuntime.new_multi_array_or_null = YarrowRuntime.access.getAddress("JVMCIRuntime::new_multi_array_or_null");
        JVMCIRuntime.object_notify = YarrowRuntime.access.getAddress("JVMCIRuntime::object_notify");
        JVMCIRuntime.object_notifyAll = YarrowRuntime.access.getAddress("JVMCIRuntime::object_notifyAll");
        JVMCIRuntime.test_deoptimize_call_int = YarrowRuntime.access.getAddress("JVMCIRuntime::test_deoptimize_call_int");
        JVMCIRuntime.thread_is_interrupted = YarrowRuntime.access.getAddress("JVMCIRuntime::thread_is_interrupted");
        JVMCIRuntime.throw_and_post_jvmti_exception = YarrowRuntime.access.getAddress("JVMCIRuntime::throw_and_post_jvmti_exception");
        JVMCIRuntime.throw_class_cast_exception = YarrowRuntime.access.getAddress("JVMCIRuntime::throw_class_cast_exception");
        JVMCIRuntime.throw_klass_external_name_exception = YarrowRuntime.access.getAddress("JVMCIRuntime::throw_klass_external_name_exception");
        JVMCIRuntime.validate_object = YarrowRuntime.access.getAddress("JVMCIRuntime::validate_object");
        JVMCIRuntime.vm_error = YarrowRuntime.access.getAddress("JVMCIRuntime::vm_error");
        JVMCIRuntime.vm_message = YarrowRuntime.access.getAddress("JVMCIRuntime::vm_message");
        JVMCIRuntime.write_barrier_post = YarrowRuntime.access.getAddress("JVMCIRuntime::write_barrier_post");
        JVMCIRuntime.write_barrier_pre = YarrowRuntime.access.getAddress("JVMCIRuntime::write_barrier_pre");
        SharedRuntime.OSR_migration_end = YarrowRuntime.access.getAddress("SharedRuntime::OSR_migration_end");
        SharedRuntime.drem = YarrowRuntime.access.getAddress("SharedRuntime::drem");
        SharedRuntime.enable_stack_reserved_zone = YarrowRuntime.access.getAddress("SharedRuntime::enable_stack_reserved_zone");
        SharedRuntime.exception_handler_for_return_address = YarrowRuntime.access.getAddress("SharedRuntime::exception_handler_for_return_address");
        SharedRuntime.frem = YarrowRuntime.access.getAddress("SharedRuntime::frem");
        SharedRuntime.register_finalizer = YarrowRuntime.access.getAddress("SharedRuntime::register_finalizer");
        os.dll_load = YarrowRuntime.access.getAddress("os::dll_load");
        os.dll_lookup = YarrowRuntime.access.getAddress("os::dll_lookup");
        os.javaTimeMillis = YarrowRuntime.access.getAddress("os::javaTimeMillis");
        os.javaTimeNanos = YarrowRuntime.access.getAddress("os::javaTimeNanos");
        RTLD_DEFAULT = YarrowRuntime.access.getAddress("RTLD_DEFAULT");
    }

    public static class CompilerRuntime {
        public static long backedge_event = 0xdeadbeef;
        public static long initialize_klass_by_symbol = 0xdeadbeef;
        public static long invocation_event = 0xdeadbeef;
        public static long resolve_dynamic_invoke = 0xdeadbeef;
        public static long resolve_klass_by_symbol = 0xdeadbeef;
        public static long resolve_method_by_symbol_and_load_counters = 0xdeadbeef;
        public static long resolve_string_by_symbol = 0xdeadbeef;
    }

    public static class Deoptimization {
        public static long fetch_unroll_info = 0xdeadbeef;
        public static long uncommon_trap = 0xdeadbeef;
        public static long unpack_frames = 0xdeadbeef;
    }

    public static class JVMCIRuntime {
        public static long dynamic_new_array = 0xdeadbeef;
        public static long dynamic_new_array_or_null = 0xdeadbeef;
        public static long dynamic_new_instance = 0xdeadbeef;
        public static long dynamic_new_instance_or_null = 0xdeadbeef;
        public static long exception_handler_for_pc = 0xdeadbeef;
        public static long identity_hash_code = 0xdeadbeef;
        public static long load_and_clear_exception = 0xdeadbeef;
        public static long log_object = 0xdeadbeef;
        public static long log_primitive = 0xdeadbeef;
        public static long log_printf = 0xdeadbeef;
        public static long monitorenter = 0xdeadbeef;
        public static long monitorexit = 0xdeadbeef;
        public static long new_array = 0xdeadbeef;
        public static long new_array_or_null = 0xdeadbeef;
        public static long new_instance = 0xdeadbeef;
        public static long new_instance_or_null = 0xdeadbeef;
        public static long new_multi_array = 0xdeadbeef;
        public static long new_multi_array_or_null = 0xdeadbeef;
        public static long object_notify = 0xdeadbeef;
        public static long object_notifyAll = 0xdeadbeef;
        public static long test_deoptimize_call_int = 0xdeadbeef;
        public static long thread_is_interrupted = 0xdeadbeef;
        public static long throw_and_post_jvmti_exception = 0xdeadbeef;
        public static long throw_class_cast_exception = 0xdeadbeef;
        public static long throw_klass_external_name_exception = 0xdeadbeef;
        public static long validate_object = 0xdeadbeef;
        public static long vm_error = 0xdeadbeef;
        public static long vm_message = 0xdeadbeef;
        public static long write_barrier_post = 0xdeadbeef;
        public static long write_barrier_pre = 0xdeadbeef;
    }

    public static class SharedRuntime {
        public static long OSR_migration_end = 0xdeadbeef;
        public static long drem = 0xdeadbeef;
        public static long enable_stack_reserved_zone = 0xdeadbeef;
        public static long exception_handler_for_return_address = 0xdeadbeef;
        public static long frem = 0xdeadbeef;
        public static long register_finalizer = 0xdeadbeef;
    }

    public static class os {
        public static long dll_load = 0xdeadbeef;
        public static long dll_lookup = 0xdeadbeef;
        public static long javaTimeMillis = 0xdeadbeef;
        public static long javaTimeNanos = 0xdeadbeef;
    }

}
