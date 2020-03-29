package com.kelthuzadx.yarrow.lir;

import com.kelthuzadx.yarrow.core.YarrowRuntime;
import com.kelthuzadx.yarrow.lir.instr.LabelInstr;
import com.kelthuzadx.yarrow.lir.operand.VirtualRegister;
import jdk.vm.ci.hotspot.HotSpotResolvedObjectType;

@SuppressWarnings("unused")
public abstract class RuntimeStub {
    private String name;
    private long stubAddress;
    private LabelInstr trampoline;
    private LabelInstr continuation;

    public RuntimeStub(String name, long stubAddress) {
        this.name = name;
        this.stubAddress = stubAddress;
        this.trampoline = new LabelInstr();
        this.continuation = new LabelInstr();
    }

    public long getStubAddress() {
        return stubAddress;
    }

    public LabelInstr getTrampoline() {
        return trampoline;
    }

    public LabelInstr getContinuation() {
        return continuation;
    }

    public String toString() {
        return name;
    }

    public static class StubRtldDefault extends RuntimeStub {
        public StubRtldDefault() {
            super("RTLD_DEFAULT", YarrowRuntime.access.getAddress("RTLD_DEFAULT"));
        }
    }

    public static class StubBackedgeEvent extends RuntimeStub {
        public StubBackedgeEvent() {
            super("CompilerRuntime::backedge_event", YarrowRuntime.access.getAddress("CompilerRuntime::backedge_event"));
        }
    }


    public static class StubInitializeKlassBySymbol extends RuntimeStub {
        public StubInitializeKlassBySymbol() {
            super("CompilerRuntime::initialize_klass_by_symbol", YarrowRuntime.access.getAddress("CompilerRuntime::initialize_klass_by_symbol"));
        }
    }


    public static class StubInvocationEvent extends RuntimeStub {
        public StubInvocationEvent() {
            super("CompilerRuntime::invocation_event", YarrowRuntime.access.getAddress("CompilerRuntime::invocation_event"));
        }
    }


    public static class StubResolveDynamicInvoke extends RuntimeStub {
        public StubResolveDynamicInvoke() {
            super("CompilerRuntime::resolve_dynamic_invoke", YarrowRuntime.access.getAddress("CompilerRuntime::resolve_dynamic_invoke"));
        }
    }


    public static class StubResolveKlassBySymbol extends RuntimeStub {
        public StubResolveKlassBySymbol() {
            super("CompilerRuntime::resolve_klass_by_symbol", YarrowRuntime.access.getAddress("CompilerRuntime::resolve_klass_by_symbol"));
        }
    }


    public static class StubResolveMethodBySymbolAndLoadCounters extends RuntimeStub {
        public StubResolveMethodBySymbolAndLoadCounters() {
            super("CompilerRuntime::resolve_method_by_symbol_and_load_counters", YarrowRuntime.access.getAddress("CompilerRuntime::resolve_method_by_symbol_and_load_counters"));
        }
    }


    public static class StubResolveStringBySymbol extends RuntimeStub {
        public StubResolveStringBySymbol() {
            super("CompilerRuntime::resolve_string_by_symbol", YarrowRuntime.access.getAddress("CompilerRuntime::resolve_string_by_symbol"));
        }
    }


    public static class StubFetchUnrollInfo extends RuntimeStub {
        public StubFetchUnrollInfo() {
            super("Deoptimization::fetch_unroll_info", YarrowRuntime.access.getAddress("Deoptimization::fetch_unroll_info"));
        }
    }


    public static class StubUncommonTrap extends RuntimeStub {
        public StubUncommonTrap() {
            super("Deoptimization::uncommon_trap", YarrowRuntime.access.getAddress("Deoptimization::uncommon_trap"));
        }
    }


    public static class StubUnpackFrames extends RuntimeStub {
        public StubUnpackFrames() {
            super("Deoptimization::unpack_frames", YarrowRuntime.access.getAddress("Deoptimization::unpack_frames"));
        }
    }


    public static class StubDynamicNewArray extends RuntimeStub {
        public StubDynamicNewArray() {
            super("JVMCIRuntime::dynamic_new_array", YarrowRuntime.access.getAddress("JVMCIRuntime::dynamic_new_array"));
        }
    }


    public static class StubDynamicNewArrayOrNull extends RuntimeStub {
        public StubDynamicNewArrayOrNull() {
            super("JVMCIRuntime::dynamic_new_array_or_null", YarrowRuntime.access.getAddress("JVMCIRuntime::dynamic_new_array_or_null"));
        }
    }


    public static class StubDynamicNewInstance extends RuntimeStub {
        public StubDynamicNewInstance() {
            super("JVMCIRuntime::dynamic_new_instance", YarrowRuntime.access.getAddress("JVMCIRuntime::dynamic_new_instance"));
        }
    }


    public static class StubDynamicNewInstanceOrNull extends RuntimeStub {
        public StubDynamicNewInstanceOrNull() {
            super("JVMCIRuntime::dynamic_new_instance_or_null", YarrowRuntime.access.getAddress("JVMCIRuntime::dynamic_new_instance_or_null"));
        }
    }


    public static class StubExceptionHandlerForPc extends RuntimeStub {
        public StubExceptionHandlerForPc() {
            super("JVMCIRuntime::exception_handler_for_pc", YarrowRuntime.access.getAddress("JVMCIRuntime::exception_handler_for_pc"));
        }
    }


    public static class StubIdentityHashCode extends RuntimeStub {
        public StubIdentityHashCode() {
            super("JVMCIRuntime::identity_hash_code", YarrowRuntime.access.getAddress("JVMCIRuntime::identity_hash_code"));
        }
    }


    public static class StubLoadAndClearException extends RuntimeStub {
        public StubLoadAndClearException() {
            super("JVMCIRuntime::load_and_clear_exception", YarrowRuntime.access.getAddress("JVMCIRuntime::load_and_clear_exception"));
        }
    }


    public static class StubLogObject extends RuntimeStub {
        public StubLogObject() {
            super("JVMCIRuntime::log_object", YarrowRuntime.access.getAddress("JVMCIRuntime::log_object"));
        }
    }


    public static class StubLogPrimitive extends RuntimeStub {
        public StubLogPrimitive() {
            super("JVMCIRuntime::log_primitive", YarrowRuntime.access.getAddress("JVMCIRuntime::log_primitive"));
        }
    }


    public static class StubLogPrintf extends RuntimeStub {
        public StubLogPrintf() {
            super("JVMCIRuntime::log_printf", YarrowRuntime.access.getAddress("JVMCIRuntime::log_printf"));
        }
    }


    public static class StubMonitorenter extends RuntimeStub {
        public StubMonitorenter() {
            super("JVMCIRuntime::monitorenter", YarrowRuntime.access.getAddress("JVMCIRuntime::monitorenter"));
        }
    }


    public static class StubMonitorexit extends RuntimeStub {
        public StubMonitorexit() {
            super("JVMCIRuntime::monitorexit", YarrowRuntime.access.getAddress("JVMCIRuntime::monitorexit"));
        }
    }


    public static class StubNewArray extends RuntimeStub {
        private VirtualRegister length;
        private VirtualRegister klassRegister;
        private VirtualRegister result;

        public StubNewArray(VirtualRegister length, VirtualRegister klassRegister, VirtualRegister result) {
            super("JVMCIRuntime::new_array", YarrowRuntime.access.getAddress("JVMCIRuntime::new_array"));
            this.length = length;
            this.klassRegister = klassRegister;
            this.result = result;
        }

        public StubNewArray() {
            this(null, null, null);
        }
    }


    public static class StubNewArrayOrNull extends RuntimeStub {
        public StubNewArrayOrNull() {
            super("JVMCIRuntime::new_array_or_null", YarrowRuntime.access.getAddress("JVMCIRuntime::new_array_or_null"));
        }
    }


    public static class StubNewInstance extends RuntimeStub {
        private HotSpotResolvedObjectType klass;
        private VirtualRegister klassRegister;
        private VirtualRegister result;

        public StubNewInstance(HotSpotResolvedObjectType klass, VirtualRegister klassRegister, VirtualRegister result) {
            super("JVMCIRuntime::new_instance", YarrowRuntime.access.getAddress("JVMCIRuntime::new_instance"));
            this.klass = klass;
            this.klassRegister = klassRegister;
            this.result = result;
        }
    }


    public static class StubNewInstanceOrNull extends RuntimeStub {
        public StubNewInstanceOrNull() {
            super("JVMCIRuntime::new_instance_or_null", YarrowRuntime.access.getAddress("JVMCIRuntime::new_instance_or_null"));
        }
    }


    public static class StubNewMultiArray extends RuntimeStub {
        public StubNewMultiArray() {
            super("JVMCIRuntime::new_multi_array", YarrowRuntime.access.getAddress("JVMCIRuntime::new_multi_array"));
        }
    }


    public static class StubNewMultiArrayOrNull extends RuntimeStub {
        public StubNewMultiArrayOrNull() {
            super("JVMCIRuntime::new_multi_array_or_null", YarrowRuntime.access.getAddress("JVMCIRuntime::new_multi_array_or_null"));
        }
    }


    public static class StubObjectNotify extends RuntimeStub {
        public StubObjectNotify() {
            super("JVMCIRuntime::object_notify", YarrowRuntime.access.getAddress("JVMCIRuntime::object_notify"));
        }
    }


    public static class StubObjectNotifyall extends RuntimeStub {
        public StubObjectNotifyall() {
            super("JVMCIRuntime::object_notifyAll", YarrowRuntime.access.getAddress("JVMCIRuntime::object_notifyAll"));
        }
    }


    public static class StubTestDeoptimizeCallInt extends RuntimeStub {
        public StubTestDeoptimizeCallInt() {
            super("JVMCIRuntime::test_deoptimize_call_int", YarrowRuntime.access.getAddress("JVMCIRuntime::test_deoptimize_call_int"));
        }
    }


    public static class StubThreadIsInterrupted extends RuntimeStub {
        public StubThreadIsInterrupted() {
            super("JVMCIRuntime::thread_is_interrupted", YarrowRuntime.access.getAddress("JVMCIRuntime::thread_is_interrupted"));
        }
    }


    public static class StubThrowAndPostJvmtiException extends RuntimeStub {
        public StubThrowAndPostJvmtiException() {
            super("JVMCIRuntime::throw_and_post_jvmti_exception", YarrowRuntime.access.getAddress("JVMCIRuntime::throw_and_post_jvmti_exception"));
        }
    }


    public static class StubThrowClassCastException extends RuntimeStub {
        public StubThrowClassCastException() {
            super("JVMCIRuntime::throw_class_cast_exception", YarrowRuntime.access.getAddress("JVMCIRuntime::throw_class_cast_exception"));
        }
    }


    public static class StubThrowKlassExternalNameException extends RuntimeStub {
        public StubThrowKlassExternalNameException() {
            super("JVMCIRuntime::throw_klass_external_name_exception", YarrowRuntime.access.getAddress("JVMCIRuntime::throw_klass_external_name_exception"));
        }
    }


    public static class StubValidateObject extends RuntimeStub {
        public StubValidateObject() {
            super("JVMCIRuntime::validate_object", YarrowRuntime.access.getAddress("JVMCIRuntime::validate_object"));
        }
    }


    public static class StubVmError extends RuntimeStub {
        public StubVmError() {
            super("JVMCIRuntime::vm_error", YarrowRuntime.access.getAddress("JVMCIRuntime::vm_error"));
        }
    }


    public static class StubVmMessage extends RuntimeStub {
        public StubVmMessage() {
            super("JVMCIRuntime::vm_message", YarrowRuntime.access.getAddress("JVMCIRuntime::vm_message"));
        }
    }


    public static class StubWriteBarrierPost extends RuntimeStub {
        public StubWriteBarrierPost() {
            super("JVMCIRuntime::write_barrier_post", YarrowRuntime.access.getAddress("JVMCIRuntime::write_barrier_post"));
        }
    }


    public static class StubWriteBarrierPre extends RuntimeStub {
        public StubWriteBarrierPre() {
            super("JVMCIRuntime::write_barrier_pre", YarrowRuntime.access.getAddress("JVMCIRuntime::write_barrier_pre"));
        }
    }


    public static class StubOsrMigrationEnd extends RuntimeStub {
        public StubOsrMigrationEnd() {
            super("SharedRuntime::OSR_migration_end", YarrowRuntime.access.getAddress("SharedRuntime::OSR_migration_end"));
        }
    }


    public static class StubDrem extends RuntimeStub {
        public StubDrem() {
            super("SharedRuntime::drem", YarrowRuntime.access.getAddress("SharedRuntime::drem"));
        }
    }


    public static class StubEnableStackReservedZone extends RuntimeStub {
        public StubEnableStackReservedZone() {
            super("SharedRuntime::enable_stack_reserved_zone", YarrowRuntime.access.getAddress("SharedRuntime::enable_stack_reserved_zone"));
        }
    }


    public static class StubExceptionHandlerForReturnAddress extends RuntimeStub {
        public StubExceptionHandlerForReturnAddress() {
            super("SharedRuntime::exception_handler_for_return_address", YarrowRuntime.access.getAddress("SharedRuntime::exception_handler_for_return_address"));
        }
    }


    public static class StubFrem extends RuntimeStub {
        public StubFrem() {
            super("SharedRuntime::frem", YarrowRuntime.access.getAddress("SharedRuntime::frem"));
        }
    }


    public static class StubRegisterFinalizer extends RuntimeStub {
        public StubRegisterFinalizer() {
            super("SharedRuntime::register_finalizer", YarrowRuntime.access.getAddress("SharedRuntime::register_finalizer"));
        }
    }


    public static class StubDllLoad extends RuntimeStub {
        public StubDllLoad() {
            super("os::dll_load", YarrowRuntime.access.getAddress("os::dll_load"));
        }
    }


    public static class StubDllLookup extends RuntimeStub {
        public StubDllLookup() {
            super("os::dll_lookup", YarrowRuntime.access.getAddress("os::dll_lookup"));
        }
    }


    public static class StubJavatimemillis extends RuntimeStub {
        public StubJavatimemillis() {
            super("os::javaTimeMillis", YarrowRuntime.access.getAddress("os::javaTimeMillis"));
        }
    }


    public static class StubJavatimenanos extends RuntimeStub {
        public StubJavatimenanos() {
            super("os::javaTimeNanos", YarrowRuntime.access.getAddress("os::javaTimeNanos"));
        }
    }

}
