package gamecenter.core.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    public static SimpleDateFormat date_format = new SimpleDateFormat("yyyy.MM.dd-HH.mm.ss");

    public static DateFormat fullDate_format = DateFormat.getDateInstance(DateFormat.FULL);

    public static String dateToString(Date date) {
        return date_format.format(date);
    }
}
