package gamecenter.core.beans.builders;

import gamecenter.core.beans.wechat.WechatProfile;

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
        public InitIdBuilder payKey(String payKey);
    }

    public static interface InitIdBuilder {
        public Builder initId(String initId);
    }

    public static class Builder implements WechatAppIdBuilder, WechatAppSecretBuilder, MchidBuilder, PayKeyBuilder, InitIdBuilder {

        private String wechatAppId;
        private String wechatAppSecret;
        private String mchid;
        private String payKey;
        private String initId;

        public Builder wechatAppSecret(String wechatAppSecret) {
            this.wechatAppSecret = wechatAppSecret;
            return this;
        }

        public WechatAppSecretBuilder wechatAppId(String wechatAppId) {
            this.wechatAppId = wechatAppId;
            return this;
        }

        public Builder mchid(String mchid) {
            this.mchid = mchid;
            return this;
        }

        public InitIdBuilder payKey(String payKey) {
            this.payKey = payKey;
            return this;
        }

        public Builder initId(String initId) {
            this.initId = initId;
            return this;
        }

        public WechatProfile build() {
            return new WechatProfile(wechatAppId, wechatAppSecret, mchid, payKey, initId);
        }
    }
}
