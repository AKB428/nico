package akb428.tkws.component;

import java.io.File;
import java.io.IOException;
import java.util.List;

import akb428.tkws.dao.h2.MediaUrlDao;
import akb428.tkws.model.MediaUrlModel;
import akb428.util.FileUtil;
import akb428.util.HttpUtil;

public class MediaDownloderComponent {


	List<MediaUrlModel> mediaUrlModelList = null;

	public boolean isDownloadList() {
		MediaUrlDao mediaUrlDao = new MediaUrlDao();

		mediaUrlModelList = mediaUrlDao.getUrlList();

		if (mediaUrlModelList.size() > 0 ) {
			return true;
		}

		return false;
	}

	public void download() {

		// ファイル名はURL末尾
		String folderCalenderPath = FileUtil.getFolderPathNameYearAndMonthSubDirectoryDay();
		String path = FileUtil.createPath("media", folderCalenderPath);
		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
		}

		MediaUrlDao mediaUrlDao = new MediaUrlDao();

		//ファイル保存
		for (MediaUrlModel mediaUrlModel: mediaUrlModelList){
			//ダウンロード
			try {
				HttpUtil.download(mediaUrlModel.getUrl(), path);
			} catch (IOException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
			//deleteFlag設定
			mediaUrlDao.deleteAndCopyHistory(mediaUrlModel);
		}
	}

}
