package gamecenter.core.processors.tasks;

import com.gamecenter.server.CloudServer;
import gamecenter.core.listeners.AbstractRunnable;
import org.apache.commons.lang3.ArrayUtils;

import java.io.IOException;

/**
 * Created by Chevis on 2014/12/23.
 */
public class CloudServerLoader extends AbstractRunnable {

    CloudServer cloudServer = new CloudServer();

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
