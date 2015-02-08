package akb428.tkws;

import java.io.IOException;
import java.util.List;

import org.codehaus.jackson.JsonProcessingException;

import akb428.twitter.model.VideoInfo;
import akb428.twitter.model.videoinfo.Variant;

//args[0] = {"contributors":null,"text":"ツイッター動画投稿テスト http://t.co/y9hjEfABw1","geo":null,"retweeted":false,"in_reply_to_screen_name":null,"possibly_sensitive":false,"truncated":false,"lang":"ja","entities":{"symbols":[],"urls":[],"hashtags":[],"media":[{"sizes":{"thumb":{"w":150,"resize":"crop","h":150},"small":{"w":340,"resize":"fit","h":340},"large":{"w":720,"resize":"fit","h":720},"medium":{"w":600,"resize":"fit","h":600}},"id":561814337585569793,"media_url_https":"https://pbs.twimg.com/ext_tw_video_thumb/561814337585569793/pu/img/ZyW1taT-3_AkDB-4.jpg","media_url":"http://pbs.twimg.com/ext_tw_video_thumb/561814337585569793/pu/img/ZyW1taT-3_AkDB-4.jpg","expanded_url":"http://twitter.com/n428dev/status/561814427150741505/video/1","indices":[13,35],"id_str":"561814337585569793","type":"photo","display_url":"pic.twitter.com/y9hjEfABw1","url":"http://t.co/y9hjEfABw1"}],"user_mentions":[]},"in_reply_to_status_id_str":null,"id":561814427150741505,"extended_entities":{"media":[{"sizes":{"thumb":{"w":150,"resize":"crop","h":150},"small":{"w":340,"resize":"fit","h":340},"large":{"w":720,"resize":"fit","h":720},"medium":{"w":600,"resize":"fit","h":600}},"id":561814337585569793,"media_url_https":"https://pbs.twimg.com/ext_tw_video_thumb/561814337585569793/pu/img/ZyW1taT-3_AkDB-4.jpg","video_info":{"duration_millis":11965,"variants":[{"bitrate":832000,"content_type":"video/webm","url":"https://video.twimg.com/ext_tw_video/561814337585569793/pu/vid/480x480/QKhWzHdEK0QDPtg8.webm"},{"bitrate":1280000,"content_type":"video/mp4","url":"https://video.twimg.com/ext_tw_video/561814337585569793/pu/vid/720x720/IdBKWbELa8D8cqLD.mp4"},{"bitrate":320000,"content_type":"video/mp4","url":"https://video.twimg.com/ext_tw_video/561814337585569793/pu/vid/240x240/7EK_gSEEPFBATzXI.mp4"},{"bitrate":832000,"content_type":"video/mp4","url":"https://video.twimg.com/ext_tw_video/561814337585569793/pu/vid/480x480/QKhWzHdEK0QDPtg8.mp4"},{"content_type":"application/x-mpegURL","url":"https://video.twimg.com/ext_tw_video/561814337585569793/pu/pl/6G6kgS5lRXAOiCcg.m3u8"}],"aspect_ratio":[1,1]},"media_url":"http://pbs.twimg.com/ext_tw_video_thumb/561814337585569793/pu/img/ZyW1taT-3_AkDB-4.jpg","expanded_url":"http://twitter.com/n428dev/status/561814427150741505/video/1","indices":[13,35],"id_str":"561814337585569793","type":"video","display_url":"pic.twitter.com/y9hjEfABw1","url":"http://t.co/y9hjEfABw1"}]},"source":"<a href=\"http://twitter.com/download/iphone\" rel=\"nofollow\">Twitter for iPhone<\/a>","in_reply_to_user_id_str":null,"favorited":false,"in_reply_to_status_id":null,"retweet_count":0,"created_at":"Sun Feb 01 09:12:52 +0000 2015","in_reply_to_user_id":null,"favorite_count":0,"id_str":"561814427150741505","place":null,"user":{"location":"","default_profile":true,"profile_background_tile":false,"statuses_count":66,"lang":"ja","profile_link_color":"0084B4","id":826661216,"following":false,"protected":false,"profile_location":null,"favourites_count":5,"profile_text_color":"333333","description":"428で働くプログラマー","verified":false,"contributors_enabled":false,"profile_sidebar_border_color":"C0DEED","name":"AKB428","profile_background_color":"C0DEED","created_at":"Sun Sep 16 07:06:52 +0000 2012","is_translation_enabled":false,"default_profile_image":false,"followers_count":9,"profile_image_url_https":"https://pbs.twimg.com/profile_images/507934202185011200/HcQGt2_r_normal.jpeg","geo_enabled":false,"profile_background_image_url":"http://abs.twimg.com/images/themes/theme1/bg.png","profile_background_image_url_https":"https://abs.twimg.com/images/themes/theme1/bg.png","follow_request_sent":false,"entities":{"description":{"urls":[]}},"url":null,"utc_offset":32400,"time_zone":"Irkutsk","notifications":false,"profile_use_background_image":true,"friends_count":40,"profile_sidebar_fill_color":"DDEEF6","screen_name":"n428dev","id_str":"826661216","profile_image_url":"http://pbs.twimg.com/profile_images/507934202185011200/HcQGt2_r_normal.jpeg","listed_count":0,"is_translator":false},"coordinates":null}

public class TwitterVideoInfoParserSample {

	public static void main(String[] args) throws JsonProcessingException,
			IOException {

		String rawJsonString = args[0];

		List<VideoInfo> videoInfoList = VideoInfo.fromRawJson(rawJsonString);

		for (VideoInfo videoInfo : videoInfoList) {
			System.out.println("video_info");
			System.out.println("|-- duration_millis : " + videoInfo.getDurationMillis());
			System.out.println("|-- aspect_ratio : " + videoInfo.getAspectRatio());
			System.out.println("|-- variants : [ ");
			for (Variant variant : videoInfo.getVariants()) {
				System.out.println("  {");
				System.out.println("  |-- bitrate : " + variant.getBitrate());
				System.out.println("  |-- content_type : " + variant.getContentType());
				System.out.println("  |-- url : " + variant.getUrl());
				System.out.println("  }");
			}
			System.out.println("  ]");
		}

	}

}
