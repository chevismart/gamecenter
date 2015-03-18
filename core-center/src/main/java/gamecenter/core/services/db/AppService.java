package gamecenter.core.services.db;

import gamecenter.core.beans.wechat.WechatProfile;
import gamecenter.core.dao.WechatMapper;
import gamecenter.core.domain.Wechat;

public class AppService {
    //dao
    WechatMapper wechatMapper;

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

    public void setWechatMapper(WechatMapper wechatMapper) {
        this.wechatMapper = wechatMapper;
    }

}
