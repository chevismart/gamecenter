package gamecenter.core.services.db;

import gamecenter.core.beans.wechat.WechatProfile;
import gamecenter.core.dao.WechatMapper;
import gamecenter.core.domain.Wechat;

public class AppService {
    //dao
    private final WechatMapper wechatMapper;

    public AppService(WechatMapper wechatMapper) {
        this.wechatMapper = wechatMapper;
    }

    boolean addWechat(WechatProfile wechatprofile) {
        Wechat wechat = new Wechat();
        wechat.setWechatappid(wechatprofile.getWechatAppId());
        wechat.setWechatappname("test");
        wechat.setWechatappsecret(wechatprofile.getWechatAppSecret());
        wechat.setWechatmchid(wechatprofile.getMchid());
        wechat.setWechatpaykey(wechatprofile.getPayKey());
        wechatMapper.insert(wechat);
        return true;
    }
}
