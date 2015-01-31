package gamecenter.core.services.db;

import gamecenter.core.dao.AccessChannelDao;

import java.util.List;

/**
 * Created by Chevis on 2015/1/31.
 */
public class AccessChannelDBService {

    AccessChannelDao accessChannelDao;

    public List<String> getAllAccessChannel() {
        return accessChannelDao.selectAll();
    }

    public String getAccessChannel(String channelId) {
        return accessChannelDao.selectAccessChannel(channelId);
    }

    public AccessChannelDao getAccessChannelDao() {
        return accessChannelDao;
    }

    public void setAccessChannelDao(AccessChannelDao accessChannelDao) {
        this.accessChannelDao = accessChannelDao;
    }
}
