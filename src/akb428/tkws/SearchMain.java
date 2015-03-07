package akb428.tkws;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;

import twitter4j.FilterQuery;
import twitter4j.MediaEntity;
import twitter4j.Status;
import twitter4j.StatusAdapter;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.auth.AccessToken;
import akb428.tkws.dao.IMediaUrlDao;
import akb428.tkws.dao.h2.MediaUrlDao;
import akb428.tkws.thread.MediaDownloderThread;

public class SearchMain {
	
	public static Properties applicationProperties = null;
	private static Boolean isMessageQueue = null;
	
	public static void main(String[] args) throws ClassNotFoundException, UnsupportedEncodingException, IOException {

		String configFile = "./config/application.properties";
		
		if (args.length == 1) {
				configFile = args[0];
		}
		InputStream inStream = new FileInputStream(configFile);
		applicationProperties = new Properties();
		applicationProperties.load(new InputStreamReader(inStream, "UTF-8"));
		
		// TODO　設定ファイルでDB切り替え
		Class.forName("org.h2.Driver");

		TwitterStream twitterStream = new TwitterStreamFactory().getInstance();
		twitterStream.setOAuthConsumer(applicationProperties.getProperty("twitter.consumer_key"),
				applicationProperties.getProperty("twitter.consumer_secret"));
		twitterStream.setOAuthAccessToken(new AccessToken(applicationProperties.getProperty("twitter.access_token"),
				applicationProperties.getProperty("twitter.access_token_secret")));

		// TODO 設定ファイルでMariaDBなどに切り替える
		IMediaUrlDao dao = new MediaUrlDao();
		
		twitterStream.addListener(new MyStatusAdapter(dao));
		ArrayList<String> track = new ArrayList<String>();
		track.addAll(Arrays.asList(applicationProperties.getProperty("twitter.searchKeyword").split(",")));

		String[] trackArray = track.toArray(new String[track.size()]);

		twitterStream.filter(new FilterQuery(0, null, trackArray));


		MediaDownloderThread mediaDownloderThread = new MediaDownloderThread();
		mediaDownloderThread.start();
	}
	
	public static boolean isMessageQueue() {
		if (isMessageQueue != null ) {
			return isMessageQueue.booleanValue();
		}
		
		if (SearchMain.applicationProperties.getProperty("messageQueue").equals("true")) {
			isMessageQueue = true;
			return isMessageQueue;
		}
		isMessageQueue = false;
		return isMessageQueue;
	}

}

class MyStatusAdapter extends StatusAdapter {
	
	IMediaUrlDao dao = null;
	
	public MyStatusAdapter (IMediaUrlDao dao) {
		this.dao = dao;
	}
	
	public void onStatus(Status status) {
		System.out.println("@" + status.getUser().getScreenName());
		System.out.println(status.getText());
		// MediaEntity[] arrMedia = status.getMediaEntities(); これだと写真１枚しか取得できない
		MediaEntity[] arrMediaExt = status.getExtendedMediaEntities();
		
		if (arrMediaExt.length > 0 ) {
			System.out.println("メディアURLが見つかりました");
		}
		
		for (MediaEntity media : arrMediaExt) {
			// http://kikutaro777.hatenablog.com/entry/2014/01/26/110350
			System.out.println(media.getMediaURL());

			if(!dao.isExistUrl(media.getMediaURL())) {
				// TODO keywordを保存したいがここでは取得できないため一時的にtextをそのまま保存
				// idはインクリメントで自動払い出し
				dao.registUrl(media.getMediaURL(), status.getText(), status.getUser().getScreenName());
			}
		}
	}
}
