package gamecenter.core.processors.tasks;

import gamecenter.core.services.ExternalService;

import java.util.List;

public class ExternalServiceLoader implements Runnable {

    private final List<ExternalService> externalServices;

    public ExternalServiceLoader(List<ExternalService> externalServices) {
        this.externalServices = externalServices;
    }

    public void run() {
        for (ExternalService externalService : externalServices) {
            externalService.run();
        }
    }
}
