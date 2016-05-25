package gamecenter.core.services;

public interface ExternalService extends Runnable{
    void init();
    void start();
    void stop();
}
