package util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormatUtil {

    public static String SimpleDateFormat(Object thisDate){


        SimpleDateFormat sdf1 = new SimpleDateFormat("yy/MM/dd HH:mm");

        String returnDate = sdf1.format(thisDate);
        return returnDate;
    }
}
