package akb428.tkws.component;

import java.io.File;
import java.util.List;

import akb428.tkws.dao.sqlite.MediaUrlDao;
import akb428.util.FileUtil;

public class MediaDownloderComponent {


	List<String> urlList = null;

	public boolean isDownloadList() {
		MediaUrlDao mediaUrlDao = new MediaUrlDao();

		urlList = mediaUrlDao.getUrlList();

		if (urlList.size() > 0 ) {
			return true;
		}

		return false;
	}

	public void download() {

		// ファイル名はURL末尾
		String folderPath = FileUtil.getFolderPathNameYearAndMonthSubDirectoryDay();
		File file = new File(folderPath);
		if (!file.exists()) {
			file.mkdirs();
		}
		String path = null;
		MediaUrlDao mediaUrlDao = new MediaUrlDao();

		//ファイル保存
		for (String url: urlList){
			//ダウンロード

			//deleteFlag設定
			mediaUrlDao.deleteAndCopyHistory(url);
		}
	}

}
