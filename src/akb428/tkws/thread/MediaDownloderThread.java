package akb428.tkws.thread;

import akb428.tkws.component.MediaDownloderComponent;

public class MediaDownloderThread extends Thread {

	public static final int SLEEP_TIME = 1000*30;

	@Override
	public void run() {

		while (true) {
			System.out.println("MediaDownloderThreadを起動します");

			MediaDownloderComponent mediaDownloderComponent = new MediaDownloderComponent();

			if (mediaDownloderComponent.isDownloadList()) {
				mediaDownloderComponent.download();
			}

			try {
				sleep(SLEEP_TIME);
			} catch (InterruptedException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
		}

	}

}
