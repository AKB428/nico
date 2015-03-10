package akb428.tkws.component;

import java.io.File;
import java.io.IOException;
import java.util.List;

import akb428.tkws.config.Application;
import akb428.tkws.dao.FactoryMediaUrlDao;
import akb428.tkws.dao.IMediaUrlDao;
import akb428.tkws.model.MediaUrlModel;
import akb428.util.Calender;
import akb428.util.FileUtil;
import akb428.util.HttpUtil;
import akb428.util.RabbitMQ;

public class MediaDownloderComponent {


	List<MediaUrlModel> mediaUrlModelList = null;

	public boolean isDownloadList() {
		IMediaUrlDao mediaUrlDao = FactoryMediaUrlDao.create();

		mediaUrlModelList = mediaUrlDao.getUrlList();

		if (mediaUrlModelList.size() > 0 ) {
			return true;
		}

		return false;
	}

	public void download() {

		// ファイル名はURL末尾
		String folderCalenderPath = FileUtil.getFolderPathNameYearAndMonthSubDirectoryDay();

		IMediaUrlDao mediaUrlDao = FactoryMediaUrlDao.create();

		//ファイル保存
		for (MediaUrlModel mediaUrlModel: mediaUrlModelList){
			//ダウンロード
			try {
				String path = FileUtil.createPath(mediaUrlModel.getPath(), folderCalenderPath);
				File file = new File(path);
				if (!file.exists()) {
					file.mkdirs();
				}
				String filePath = HttpUtil.download(mediaUrlModel.getUrl(), path);

				if (Application.isMessageQueue) {
					String destPath = "/web_rabbitmq_nico/" + Calender.yyyyMMdd();
					filePath = new File(".").getAbsoluteFile().getParent() + "/" + filePath;
					
					RabbitMQ.send(destPath, filePath);
				}
			} catch (IOException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
			//deleteFlag設定
			mediaUrlDao.deleteAndCopyHistory(mediaUrlModel);
		}
	}

}
