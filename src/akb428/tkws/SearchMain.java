package akb428.tkws;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.msgpack.MessagePack;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

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
		twitterStream.setOAuthAccessToken(new AccessToken(applicationProperties.getProperty("twitter.access_token"), applicationProperties
				.getProperty("twitter.access_token_secret")));

		if ("stand_alone".equals(applicationProperties.getProperty("application.mode"))) {
			IMediaUrlDao dao = new MediaUrlDao();
			twitterStream.addListener(new StandAloneStatusAdapter(dao));
			MediaDownloderThread mediaDownloderThread = new MediaDownloderThread();
			mediaDownloderThread.start();
		} else if ("send_task_to_worker".equals(applicationProperties.getProperty("application.mode"))) {
			ConnectionFactory factory = new ConnectionFactory();
			factory.setHost("localhost");
			Connection connection = factory.newConnection();
			Channel channel = connection.createChannel();
			channel.queueDeclare(SendTaskToWorkerStatusAdapter.QUEUE_NAME, false, false, false, null);

			twitterStream.addListener(new SendTaskToWorkerStatusAdapter(channel, new MessagePack()));
		}

		ArrayList<String> track = new ArrayList<String>();
		track.addAll(Arrays.asList(applicationProperties.getProperty("twitter.searchKeyword").split(",")));
		String[] trackArray = track.toArray(new String[track.size()]);
		twitterStream.filter(new FilterQuery(0, null, trackArray));
	}

	public static boolean isMessageQueue() {
		if (isMessageQueue != null) {
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

class StandAloneStatusAdapter extends StatusAdapter {

	IMediaUrlDao dao = null;

	public StandAloneStatusAdapter(IMediaUrlDao dao) {
		this.dao = dao;
	}

	public void onStatus(Status status) {
		System.out.println("@" + status.getUser().getScreenName());
		System.out.println(status.getText());
		// MediaEntity[] arrMedia = status.getMediaEntities(); これだと写真１枚しか取得できない
		MediaEntity[] arrMediaExt = status.getExtendedMediaEntities();

		if (arrMediaExt.length > 0) {
			System.out.println("メディアURLが見つかりました");
		}

		for (MediaEntity media : arrMediaExt) {
			// http://kikutaro777.hatenablog.com/entry/2014/01/26/110350
			System.out.println(media.getMediaURL());

			if (!dao.isExistUrl(media.getMediaURL())) {
				// TODO keywordを保存したいがここでは取得できないため一時的にtextをそのまま保存
				// idはインクリメントで自動払い出し
				dao.registUrl(media.getMediaURL(), status.getText(), status.getUser().getScreenName());
			}
		}
	}
}

class SendTaskToWorkerStatusAdapter extends StatusAdapter {
	public static final String QUEUE_NAME = "development.nico.sendTask";
	public static final String SEARCH_TARGET_ID = SearchMain.applicationProperties.getProperty("twitter.searchTargetId");
	private Channel channel;
	private MessagePack msgpack;
	public SendTaskToWorkerStatusAdapter(Channel channel, MessagePack msgpack) {
		this.channel = channel;
		this.msgpack = msgpack;
	}

	public void onStatus(Status status) {
		System.out.println("@" + status.getUser().getScreenName());
		System.out.println(status.getText());
		MediaEntity[] arrMediaExt = status.getExtendedMediaEntities();

		if (arrMediaExt.length > 0) {
			System.out.println("メディアURLが見つかりました");
		}

		for (MediaEntity media : arrMediaExt) {
			List<String> src = new ArrayList<String>();
			src.add(SEARCH_TARGET_ID);
			src.add(media.getMediaURL());
			src.add(media.getText());
			src.add(status.getUser().getScreenName());
		
	
			try {
				byte[] message = msgpack.write(src);
	
			/*
			String message = String.format("{\"search_target_id\":\"%s\",\"url\":\"%s\", \"text\":\"%s\", \"username\":\"%s\"}",
					SEARCH_TARGET_ID, media.getMediaURL(), status.getText(), 
					status.getUser().getScreenName());*/

				//channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
				channel.basicPublish("", QUEUE_NAME, null, message);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}