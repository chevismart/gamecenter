package gamecenter.core.services.external;

import gamecenter.core.services.ExternalService;
import org.restlet.Application;
import org.restlet.Component;
import org.restlet.data.Protocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static gamecenter.core.services.external.SettlementResource.SETTLEMENT_URI;

public class YoubaoPaymentService extends Application implements ExternalService, Runnable {
    private static int port = 10001;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private Component component;

    public static void main(String[] args) {
        YoubaoPaymentService service =  new YoubaoPaymentService();
        service.run();
    }

    @Override
    public void run() {
        logger.info("External Service -> Youbao Service starting!");
        init();
        start();
        logger.info("External Service -> Youbao Service is started!");
    }

    @Override
    public void init() {
        component = new Component();
        component.getServers().add(Protocol.HTTP, port);
        component.getDefaultHost().attach(new Application());
        component.getDefaultHost().attach(PrepayResource.PRE_PAY_URI, PrepayResource.class);
        component.getDefaultHost().attach(SETTLEMENT_URI, SettlementResource.class);
    }

    @Override
    public void start() {
        try {
            component.start();
        } catch (Exception e) {
            logger.error("Restlet server startup error.", e);
        }
    }

    @Override
    public void stop() {
        try {
            component.stop();
        } catch (Exception e) {
            logger.error("Stop server error.", e);
        }
    }
}
