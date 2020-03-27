import com.kelthuzadx.yarrow.service.YarrowServiceLocator;

module yarrow {
    requires jdk.internal.vm.ci;
    requires java.logging;
    requires jdk.unsupported;

    provides jdk.vm.ci.services.JVMCIServiceLocator with YarrowServiceLocator;
}