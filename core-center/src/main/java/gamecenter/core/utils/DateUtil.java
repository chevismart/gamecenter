package gamecenter.core.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by chevi on 2016/1/13.
 */
public class DateUtil {

    public static SimpleDateFormat date_format = new SimpleDateFormat("yyyy.MM.dd-HH.mm.ss");

    public static String dateToString(Date date) {
        return date_format.format(date);
    }
}
