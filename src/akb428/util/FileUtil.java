package akb428.util;

import java.util.Calendar;

public class FileUtil {

	// 年_月/日付のフォルダ名を返却する
	public static String getFolderPathNameYearAndMonthSubDirectoryDay() {

        String path = "";
        Calendar cal = Calendar.getInstance();
        path += String.valueOf( cal.get(Calendar.YEAR)) + "_" ;
        path += String.valueOf( cal.get(Calendar.MONTH) + 1);
        path += System.getProperties().getProperty("file.separator");
        path += String.valueOf( cal.get(Calendar.DATE));
		return path;
	}

	public static String createPath(String folderName, String fileName) {
		return folderName +  System.getProperties().getProperty("file.separator") + fileName;
	}
}
