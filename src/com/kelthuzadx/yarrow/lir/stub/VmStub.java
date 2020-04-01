package com.kelthuzadx.yarrow.lir.stub;

import com.kelthuzadx.yarrow.core.YarrowRuntime;

public enum VmStub {
    StubRtldDefault("RTLD_DEFAULT", YarrowRuntime.access.getAddress("RTLD_DEFAULT")),

    StubBackedgeEvent("CompilerRuntime::backedge_event", YarrowRuntime.access.getAddress("CompilerRuntime::backedge_event")),

    StubInitializeKlassBySymbol("CompilerRuntime::initialize_klass_by_symbol", YarrowRuntime.access.getAddress("CompilerRuntime::initialize_klass_by_symbol")),

    StubInvocationEvent("CompilerRuntime::invocation_event", YarrowRuntime.access.getAddress("CompilerRuntime::invocation_event")),

    StubResolveDynamicInvoke("CompilerRuntime::resolve_dynamic_invoke", YarrowRuntime.access.getAddress("CompilerRuntime::resolve_dynamic_invoke")),

    StubResolveKlassBySymbol("CompilerRuntime::resolve_klass_by_symbol", YarrowRuntime.access.getAddress("CompilerRuntime::resolve_klass_by_symbol")),

    StubResolveMethodBySymbolAndLoadCounters("CompilerRuntime::resolve_method_by_symbol_and_load_counters", YarrowRuntime.access.getAddress("CompilerRuntime::resolve_method_by_symbol_and_load_counters")),

    StubResolveStringBySymbol("CompilerRuntime::resolve_string_by_symbol", YarrowRuntime.access.getAddress("CompilerRuntime::resolve_string_by_symbol")),

    StubFetchUnrollInfo("Deoptimization::fetch_unroll_info", YarrowRuntime.access.getAddress("Deoptimization::fetch_unroll_info")),

    StubUncommonTrap("Deoptimization::uncommon_trap", YarrowRuntime.access.getAddress("Deoptimization::uncommon_trap")),

    StubUnpackFrames("Deoptimization::unpack_frames", YarrowRuntime.access.getAddress("Deoptimization::unpack_frames")),

    StubDynamicNewArray("JVMCIRuntime::dynamic_new_array", YarrowRuntime.access.getAddress("JVMCIRuntime::dynamic_new_array")),

    StubDynamicNewArrayOrNull("JVMCIRuntime::dynamic_new_array_or_null", YarrowRuntime.access.getAddress("JVMCIRuntime::dynamic_new_array_or_null")),

    StubDynamicNewInstance("JVMCIRuntime::dynamic_new_instance", YarrowRuntime.access.getAddress("JVMCIRuntime::dynamic_new_instance")),

    StubDynamicNewInstanceOrNull("JVMCIRuntime::dynamic_new_instance_or_null", YarrowRuntime.access.getAddress("JVMCIRuntime::dynamic_new_instance_or_null")),

    StubExceptionHandlerForPc("JVMCIRuntime::exception_handler_for_pc", YarrowRuntime.access.getAddress("JVMCIRuntime::exception_handler_for_pc")),

    StubIdentityHashCode("JVMCIRuntime::identity_hash_code", YarrowRuntime.access.getAddress("JVMCIRuntime::identity_hash_code")),

    StubLoadAndClearException("JVMCIRuntime::load_and_clear_exception", YarrowRuntime.access.getAddress("JVMCIRuntime::load_and_clear_exception")),

    StubLogObject("JVMCIRuntime::log_object", YarrowRuntime.access.getAddress("JVMCIRuntime::log_object")),

    StubLogPrimitive("JVMCIRuntime::log_primitive", YarrowRuntime.access.getAddress("JVMCIRuntime::log_primitive")),

    StubLogPrintf("JVMCIRuntime::log_printf", YarrowRuntime.access.getAddress("JVMCIRuntime::log_printf")),

    StubMonitorenter("JVMCIRuntime::monitorenter", YarrowRuntime.access.getAddress("JVMCIRuntime::monitorenter")),

    StubMonitorexit("JVMCIRuntime::monitorexit", YarrowRuntime.access.getAddress("JVMCIRuntime::monitorexit")),

    StubNewArray("JVMCIRuntime::new_array", YarrowRuntime.access.getAddress("JVMCIRuntime::new_array")),

    StubNewArrayOrNull("JVMCIRuntime::new_array_or_null", YarrowRuntime.access.getAddress("JVMCIRuntime::new_array_or_null")),

    StubNewInstance("JVMCIRuntime::new_instance", YarrowRuntime.access.getAddress("JVMCIRuntime::new_instance")),

    StubNewInstanceOrNull("JVMCIRuntime::new_instance_or_null", YarrowRuntime.access.getAddress("JVMCIRuntime::new_instance_or_null")),

    StubNewMultiArray("JVMCIRuntime::new_multi_array", YarrowRuntime.access.getAddress("JVMCIRuntime::new_multi_array")),

    StubNewMultiArrayOrNull("JVMCIRuntime::new_multi_array_or_null", YarrowRuntime.access.getAddress("JVMCIRuntime::new_multi_array_or_null")),

    StubObjectNotify("JVMCIRuntime::object_notify", YarrowRuntime.access.getAddress("JVMCIRuntime::object_notify")),

    StubObjectNotifyall("JVMCIRuntime::object_notifyAll", YarrowRuntime.access.getAddress("JVMCIRuntime::object_notifyAll")),

    StubTestDeoptimizeCallInt("JVMCIRuntime::test_deoptimize_call_int", YarrowRuntime.access.getAddress("JVMCIRuntime::test_deoptimize_call_int")),

    StubThreadIsInterrupted("JVMCIRuntime::thread_is_interrupted", YarrowRuntime.access.getAddress("JVMCIRuntime::thread_is_interrupted")),

    StubThrowAndPostJvmtiException("JVMCIRuntime::throw_and_post_jvmti_exception", YarrowRuntime.access.getAddress("JVMCIRuntime::throw_and_post_jvmti_exception")),

    StubThrowClassCastException("JVMCIRuntime::throw_class_cast_exception", YarrowRuntime.access.getAddress("JVMCIRuntime::throw_class_cast_exception")),

    StubThrowKlassExternalNameException("JVMCIRuntime::throw_klass_external_name_exception", YarrowRuntime.access.getAddress("JVMCIRuntime::throw_klass_external_name_exception")),

    StubValidateObject("JVMCIRuntime::validate_object", YarrowRuntime.access.getAddress("JVMCIRuntime::validate_object")),

    StubVmError("JVMCIRuntime::vm_error", YarrowRuntime.access.getAddress("JVMCIRuntime::vm_error")),

    StubVmMessage("JVMCIRuntime::vm_message", YarrowRuntime.access.getAddress("JVMCIRuntime::vm_message")),

    StubWriteBarrierPost("JVMCIRuntime::write_barrier_post", YarrowRuntime.access.getAddress("JVMCIRuntime::write_barrier_post")),

    StubWriteBarrierPre("JVMCIRuntime::write_barrier_pre", YarrowRuntime.access.getAddress("JVMCIRuntime::write_barrier_pre")),

    StubOsrMigrationEnd("SharedRuntime::OSR_migration_end", YarrowRuntime.access.getAddress("SharedRuntime::OSR_migration_end")),

    StubDrem("SharedRuntime::drem", YarrowRuntime.access.getAddress("SharedRuntime::drem")),

    StubEnableStackReservedZone("SharedRuntime::enable_stack_reserved_zone", YarrowRuntime.access.getAddress("SharedRuntime::enable_stack_reserved_zone")),

    StubExceptionHandlerForReturnAddress("SharedRuntime::exception_handler_for_return_address", YarrowRuntime.access.getAddress("SharedRuntime::exception_handler_for_return_address")),

    StubFrem("SharedRuntime::frem", YarrowRuntime.access.getAddress("SharedRuntime::frem")),

    StubRegisterFinalizer("SharedRuntime::register_finalizer", YarrowRuntime.access.getAddress("SharedRuntime::register_finalizer")),

    StubDllLoad("os::dll_load", YarrowRuntime.access.getAddress("os::dll_load")),

    StubDllLookup("os::dll_lookup", YarrowRuntime.access.getAddress("os::dll_lookup")),

    StubJavatimemillis("os::javaTimeMillis", YarrowRuntime.access.getAddress("os::javaTimeMillis")),

    StubJavatimenanos("os::javaTimeNanos", YarrowRuntime.access.getAddress("os::javaTimeNanos"));

    private String name;

    private long stubAddress;

    VmStub(String name, long stubAddress) {
        this.name = name;
        this.stubAddress = stubAddress;
    }


    public long getStubAddress() {
        return stubAddress;
    }

    public String toString() {
        return name;
    }
}
