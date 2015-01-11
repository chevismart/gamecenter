package gamecenter.core.utils;

import com.opensymphony.xwork2.ActionContext;
import gamecenter.core.beans.AccessChannel;
import gamecenter.core.beans.AppProfile;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * Created by Chevis on 2014/12/18.
 */
public class ProfileUtil {

    private final static String HYPEN = "-";

    public static boolean verifyAppProfile(AppProfile appProfile) {
        return null != appProfile &&
                StringUtils.isNotEmpty(appProfile.getAppId()) &&
                StringUtils.isNotEmpty(appProfile.getAppName());
    }

    public static String getUserUnifyId(AccessChannel accessChannel, String id) {
        return accessChannel.name().concat(HYPEN).concat(id);
    }

    public static String getUserOriginalId(String internalId) {
        return internalId.split(HYPEN)[1];
    }

    public static Map<String, Object> getSession() {
        return ActionContext.getContext().getSession();
    }

}
