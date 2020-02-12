import com.kelthuzadx.yarrow.service.YarrowServiceLocator;

module yarrow {
    requires jdk.internal.vm.ci;
    requires java.logging;

    provides jdk.vm.ci.services.JVMCIServiceLocator with YarrowServiceLocator;
}