package akb428.tkws;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import twitter4j.FilterQuery;
import twitter4j.Status;
import twitter4j.StatusAdapter;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.auth.AccessToken;
import akb428.tkws.model.TwitterModel;

public class SearchMain {

	public static void main(String[] args) {
		// TODO 自動生成されたメソッド・スタブ

		TwitterModel twitterModel = null;
		if (args.length != 2) {
			try {
				twitterModel = TwitterConfParser.readConf("conf/twitter_conf.json");
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
		twitterStream.setOAuthConsumer(twitterModel.getConsumerKey(), twitterModel.getConsumerSecret());
		twitterStream.setOAuthAccessToken(new AccessToken(twitterModel.getAccessToken(), twitterModel.getAccessToken_secret()));

		twitterStream.addListener(new MyStatusAdapter());
		ArrayList<String> track = new ArrayList<String>();
		track.addAll(Arrays.asList(args[0].split(",")));

		String[] trackArray = track.toArray(new String[track.size()]);

		twitterStream.filter(new FilterQuery(0, null, trackArray));
	}


}

class MyStatusAdapter extends StatusAdapter {
	public void onStatus (Status status) {
		System.out.println(status.getUser().getScreenName() + " " + status.getText() );
	}
}
