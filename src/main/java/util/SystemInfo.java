package util;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by lipengfei on 2015/1/25.
 */
public class SystemInfo {

    /**
     * 获取本机用户名
     *
     * @return
     */
    public static String getUsername(){
        return System.getProperties().getProperty("user.name");
    }

    public static void main(String[] args) {
        System.out.println(SystemInfo.getUsername());
    }

}
