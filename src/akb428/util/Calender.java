package akb428.util;

import java.util.Calendar;

public class Calender {
	public static String nowString(){
        String dateString = "";
        Calendar cal = Calendar.getInstance();
        dateString += String.valueOf( cal.get(Calendar.YEAR)) + "年" ;
        dateString += String.valueOf( cal.get(Calendar.MONTH) + 1) + "月";
        dateString += String.valueOf( cal.get(Calendar.DATE)) + "日";
        dateString += String.valueOf( cal.get(Calendar.HOUR_OF_DAY)) + "時";
        dateString += String.valueOf( cal.get(Calendar.MINUTE)) + "分";
        dateString += String.valueOf( cal.get(Calendar.SECOND)) + "秒";
        return dateString;
	}
}
