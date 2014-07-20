package akb428.tkws;

import java.io.IOException;

import akb428.tkws.model.TwitterModel;

public class ConsoleMain {

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


		System.out.println(twitterModel.getAccessToken());

	}


}
