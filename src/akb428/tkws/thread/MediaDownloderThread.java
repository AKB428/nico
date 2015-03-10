package akb428.tkws.thread;

import akb428.tkws.component.MediaDownloderComponent;
import akb428.tkws.config.Application;

public class MediaDownloderThread extends Thread {

	@Override
	public void run() {

		while (true) {
			System.out.println("MediaDownloderThreadを起動します");

			MediaDownloderComponent mediaDownloderComponent = new MediaDownloderComponent();

			if (mediaDownloderComponent.isDownloadList()) {
				mediaDownloderComponent.download();
			}

			try {
				sleep(Application.mediaDownloadThreadSleepSec * 1000);
			} catch (InterruptedException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
		}

	}

}
