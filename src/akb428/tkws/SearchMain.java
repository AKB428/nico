package akb428.tkws;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import twitter4j.FilterQuery;
import twitter4j.MediaEntity;
import twitter4j.Status;
import twitter4j.StatusAdapter;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.auth.AccessToken;
import akb428.tkws.dao.IMediaUrlDao;
import akb428.tkws.dao.h2.MediaUrlDao;
import akb428.tkws.model.TwitterModel;
import akb428.tkws.thread.MediaDownloderThread;

public class SearchMain {

	public static void main(String[] args) throws ClassNotFoundException {

		TwitterModel twitterModel = null;

		// TODO 設定ファイルでMariaDBなどに切り替える
		//Class.forName("org.sqlite.JDBC");
		Class.forName("org.h2.Driver");

		if (args.length != 2) {
			try {
				twitterModel = TwitterConfParser
						.readConf("conf/twitter_conf.json");
			} catch (IOException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
		} else {
			try {
				twitterModel = TwitterConfParser.readConf(args[1]);
			} catch (IOException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
		}

		TwitterStream twitterStream = new TwitterStreamFactory().getInstance();
		twitterStream.setOAuthConsumer(twitterModel.getConsumerKey(),
				twitterModel.getConsumerSecret());
		twitterStream.setOAuthAccessToken(new AccessToken(twitterModel
				.getAccessToken(), twitterModel.getAccessToken_secret()));

		twitterStream.addListener(new MyStatusAdapter());
		ArrayList<String> track = new ArrayList<String>();
		track.addAll(Arrays.asList(args[0].split(",")));

		String[] trackArray = track.toArray(new String[track.size()]);

		twitterStream.filter(new FilterQuery(0, null, trackArray));


		MediaDownloderThread mediaDownloderThread = new MediaDownloderThread();
		mediaDownloderThread.start();
	}

}

class MyStatusAdapter extends StatusAdapter {

	// TODO 設定ファイルでMariaDBなどに切り替える
	IMediaUrlDao dao = new MediaUrlDao();


	public void onStatus(Status status) {
		System.out.println("@" + status.getUser().getScreenName());
		System.out.println(status.getText());
		MediaEntity[] arrMedia = status.getMediaEntities();

		if (arrMedia.length > 0 ) {
			System.out.println("メディアURLが見つかりました");
		}
		for (MediaEntity media : arrMedia) {
			// http://kikutaro777.hatenablog.com/entry/2014/01/26/110350
			System.out.println(media.getMediaURL());

			if(!dao.isExistUrl(media.getMediaURL())) {
				// TODO keywordを保存したいがここでは取得できないため一時的にtextをそのまま保存
				dao.registUrl(media.getMediaURL(), status.getText(), status.getUser().getScreenName());
			}
		}
	}
}
