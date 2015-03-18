package gamecenter.core.utils;

import java.util.Date;

/**
 * Created by Chevis on 2014/12/15.
 */
public class TimeUtil {

    public static int millionSecondFromSecond(int second) {
        return second * 1000;
    }

    public static int nanoSecondFromMillionSecond(int second) {
        return second * 1000000;
    }

    public static Date getExpiryDateTime(Date datetime, int expiryInSeconds, int shiftSecond) {
        return null == datetime ? null : new Date(datetime.getTime() + millionSecondFromSecond(expiryInSeconds - shiftSecond));
    }

    public static boolean isExpiry(Date current, Date date) {
        return null == current || null == date ? true : current.after(date);
    }

    public static Date getCurrentDateTime() {
        return new Date();
    }
}
