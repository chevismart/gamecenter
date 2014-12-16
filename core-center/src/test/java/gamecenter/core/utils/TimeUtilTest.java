package gamecenter.core.utils;

import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

import static junit.framework.Assert.assertEquals;


public class TimeUtilTest {

    @Test
    public void getExpiryDateTimeCorrectly() throws Exception {
        int onesecond = 1;
        Date current = TimeUtil.getCurrentDateTime();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(current);
        calendar.add(Calendar.SECOND, 1);
        Date oneSecondDelayDate = TimeUtil.getExpiryDateTime(current, onesecond);
        assertEquals(calendar.getTime().getTime(), oneSecondDelayDate.getTime());
    }
}