package gamecenter.core.beans.builders;

import gamecenter.core.beans.wechat.WechatProfile;

/**
 * Created by Chevis on 14/12/15.
 */
public class WechatProfileBuilder implements Builder {

    public static WechatAppIdBuilder newBuilder() {
        return new WechatProfileBuilder.Builder();
    }

    public static interface WechatAppIdBuilder {
        public WechatAppSecretBuilder wechatAppId(String wechatAppId);
    }

    public static interface WechatAppSecretBuilder {
        public MchidBuilder wechatAppSecret(String wechatAppSecret);
    }

    public static interface MchidBuilder {
        public PayKeyBuilder mchid(String wechatAppSecret);
    }

    public static interface PayKeyBuilder {
        public Builder payKey(String payKey);
    }

    public static class Builder implements WechatAppIdBuilder, WechatAppSecretBuilder, MchidBuilder, PayKeyBuilder {

        private String wechatAppId;
        private String wechatAppSecret;
        private String mchid;
        private String payKey;

        @Override
        public WechatAppSecretBuilder wechatAppId(String wechatAppId) {
            this.wechatAppId = wechatAppId;
            return this;
        }

        @Override
        public Builder wechatAppSecret(String wechatAppSecret) {
            this.wechatAppSecret = wechatAppSecret;
            return this;
        }

        @Override
        public Builder mchid(String mchid) {
            this.mchid = mchid;
            return this;
        }

        @Override
        public Builder payKey(String payKey) {
            this.payKey = payKey;
            return this;
        }

        public WechatProfile build() {
            return new WechatProfile(wechatAppId, wechatAppSecret, mchid, payKey);
        }
    }
}
