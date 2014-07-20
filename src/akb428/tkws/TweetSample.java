package akb428.tkws;

import java.io.IOException;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import akb428.tkws.model.TwitterModel;

public class TweetSample {

	public static void main(String[] args) {
		// TODO 自動生成されたメソッド・スタブ

		TwitterModel twitterModel = null;
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

		Twitter twitter = new TwitterFactory().getInstance();
		twitter.setOAuthConsumer(twitterModel.getConsumerKey(),
				twitterModel.getConsumerSecret());
		twitter.setOAuthAccessToken(new AccessToken(twitterModel
				.getAccessToken(), twitterModel.getAccessToken_secret()));

		try {
			twitter.updateStatus(args[0]);
		} catch (TwitterException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}

}
