package gamecenter.core.processors.tasks;

import com.gamecenter.server.CloudServer;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class CloudServerLoader implements Runnable {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private CloudServer cloudServer = new CloudServer();

    public CloudServer getCloudServer() {
        return cloudServer;
    }

    public void setCloudServer(CloudServer cloudServer) {
        this.cloudServer = cloudServer;
    }

    @Override
    public void run() {
        try {
            cloudServer.main(ArrayUtils.EMPTY_STRING_ARRAY);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }
}
