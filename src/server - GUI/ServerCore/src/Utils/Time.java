package Utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Time {
    public String getTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("[yyyy-MM-dd] - [HH-mm-ss]");
        Date date = new Date(System.currentTimeMillis());
        return formatter.format(date);
    }

    public Date WebKitToDate(long webKit) {
        long unixTimeMilliseconds = (webKit - 11644473600000000L) / 1000; // Convertir a milisegundos
        return new Date(unixTimeMilliseconds);
    }

}
