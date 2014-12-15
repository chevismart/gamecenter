package gamecenter.core.beans.builders;

import gamecenter.core.beans.wechat.WechatProfile;

/**
 * Created by Chevis on 14/12/15.
 */
public class WechatProfileBuilder {

    public static WechatAppIdBuilder newBuilder() {
        return new WechatProfileBuilder.Builder();
    }

    public static interface WechatAppIdBuilder {
        public WechatAppSecretBuilder wechatAppIdBuilder(String wechatAppIdBuilder);
    }

    public static interface WechatAppSecretBuilder {
        public Builder WechatAppSecretBuilder(String wechatAppSecretBuilder);
    }

    public static class Builder implements  WechatAppIdBuilder,WechatAppSecretBuilder{

        private String wechatAppId;
        private String wechatAppSecret;

        @Override
        public WechatAppSecretBuilder wechatAppIdBuilder(String wechatAppId) {
            this.wechatAppId =wechatAppId;
            return this;
        }

        @Override
        public Builder WechatAppSecretBuilder(String wechatAppSecret) {
            this.wechatAppSecret = wechatAppSecret;
            return this;
        }

        public WechatProfile build(){
            return new WechatProfile(wechatAppId,wechatAppSecret);
        }
    }
}
