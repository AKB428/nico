package akb428.tkws;

import java.io.IOException;
import java.util.List;

import twitter4j.MediaEntity;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.URLEntity;
import twitter4j.auth.AccessToken;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.json.DataObjectFactory;
import akb428.tkws.model.TwitterModel;

public class GetUserTimeLineTweetSample {

	public static void main(String[] args) throws TwitterException {
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
		
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setJSONStoreEnabled(true);

		Twitter twitter = new TwitterFactory(cb.build()).getInstance();
		twitter.setOAuthConsumer(twitterModel.getConsumerKey(),
				twitterModel.getConsumerSecret());
		twitter.setOAuthAccessToken(new AccessToken(twitterModel
				.getAccessToken(), twitterModel.getAccessToken_secret()));

		List<Status> statuses = twitter.getUserTimeline(args[0]);
		System.out.println("Showing home timeline.");
		for (Status status : statuses) {
			System.out.println(status.getUser().getName() + ":"
					+ status.getText());
			String rawJSON = DataObjectFactory.getRawJSON(status);
			System.out.println(rawJSON);
			
			MediaEntity[] arrMedia = status.getMediaEntities();
			for (MediaEntity media : arrMedia) {
				System.out.println("MediaEntity= " + media.getMediaURL());
			}

			MediaEntity[] arrMediaExt = status.getExtendedMediaEntities();
			for (MediaEntity media : arrMediaExt) {
				System.out.println("ExtendedMediaEntities= "+ media.getMediaURL());
			}
			
			URLEntity[] entity = status.getURLEntities();
			for (URLEntity urlEntity : entity) {
				System.out.println("URLEntity= " + urlEntity.getExpandedURL());
			}
		}
	}

}
