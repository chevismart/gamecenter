package gamecenter.core.beans.builders;

import gamecenter.core.beans.AppProfile;
import gamecenter.core.beans.wechat.WechatProfile;

/**
 * Created by Chevis on 14/12/15.
 */
public class AppProfileBuilder {

    public static AppIdBuilder newBuilder() {
        return new AppProfileBuilder.Builder();
    }

    public static interface AppNameBuilder {
        public Builder appName(String name);
    }

    public static interface AppIdBuilder {
        public AppNameBuilder appId(String appId);
    }

    public static class Builder implements AppIdBuilder, AppNameBuilder {
        private String appName;
        private String appId;
        private WechatProfile wechatProfile;

        @Override
        public Builder appName(String appName) {
            this.appName = appName;
            return this;
        }


        @Override
        public AppNameBuilder appId(String appId) {
            this.appId = appId;
            return this;
        }

        public AppProfile build() {
            return new AppProfile(appName, appId);
        }
    }

}
