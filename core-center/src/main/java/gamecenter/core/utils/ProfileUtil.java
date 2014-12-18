package gamecenter.core.utils;

import gamecenter.core.beans.AccessChannel;
import gamecenter.core.beans.AppProfile;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by Chevis on 2014/12/18.
 */
public class ProfileUtil {

    public static boolean verifyAppProfile(AppProfile appProfile) {
        return null != appProfile &&
                StringUtils.isNotEmpty(appProfile.getAppId()) &&
                StringUtils.isNotEmpty(appProfile.getAppName());
    }

    public static String getUserUnifyId(AccessChannel accessChannel, String id) {
        return accessChannel.name().concat("-").concat(id);
    }
}
