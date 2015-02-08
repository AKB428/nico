package akb428.twitter.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;

import akb428.twitter.model.videoinfo.Variant;

public class VideoInfo {

	private Long durationMillis = 0L;
	private List<Variant> variants = null;
	private List<Long> aspectRatio = null;

	/**
	 * ツイートの生JSON文字列からVideoInfo情報をListにして返却します VideoInfoが見つからない場合はnullを返却します
	 * ツイートの生JSONはTwitter4Jであれば
	 * 
	 * ----------------------------------------------------------------
	 * ConfigurationBuilder cb = new ConfigurationBuilder();
	 * cb.setJSONStoreEnabled(true); String rawJsonString =
	 * DataObjectFactory.getRawJSON(Status status);
	 * ----------------------------------------------------------------
	 * 
	 * で取得できます。
	 * 
	 * @param rawJsonString
	 *            JSON文字列
	 * @return VideoInfoのリスト。ビデオ情報がない場合はnull
	 * @throws JsonProcessingException
	 * @throws IOException
	 */
	public static List<VideoInfo> fromRawJson(String rawJsonString) throws JsonProcessingException, IOException {
		List<VideoInfo> videoInfoList = new ArrayList<>();
		ObjectMapper mapper = new ObjectMapper();
		JsonNode rootNode = mapper.readTree(rawJsonString);

		JsonNode extentdedEntitiesNode = rootNode.get("extended_entities");
		if (extentdedEntitiesNode == null) {
			return null;
		}
		JsonNode mediaNodo = extentdedEntitiesNode.get("media");
		if (mediaNodo == null) {
			return null;
		}

		Iterator<JsonNode> mediaNodeList = mediaNodo.getElements();
		// mediaノードにはビデオ以外もあるので、動画情報のみ取得するようにする
		// video_infoがあるかどうか
		// media.type="video" かどうか
		// のいずれかで判定する
		// media: [ {type: video, video_info:{}}, {type: video, video_info:{}},
		// {}, ]
		while (mediaNodeList.hasNext()) {
			JsonNode videoNode = mediaNodeList.next().get("video_info");
			if (videoNode != null) {
				VideoInfo videoInfo = fromVideoJsonNode(videoNode);
				videoInfoList.add(videoInfo);
			}
		}

		return videoInfoList;
	}

	/**
	 * videoInfo（JsonNodeクラス）をVideoInfoクラスに変換します
	 * VideoInfoクラスの各フィールドに対応するJSONプロパティが存在しないときはフィールドには何も設定しません。（nullのまま）
	 * 
	 * @param videoNode
	 * @return
	 */
	public static VideoInfo fromVideoJsonNode(JsonNode videoNode) {
		VideoInfo videoInfo = new VideoInfo();
		JsonNode durationMillisNode = videoNode.get("duration_millis");

		if (durationMillisNode != null) {
			videoInfo.setDurationMillis(durationMillisNode.getLongValue());
		}

		JsonNode aspectRatioNode = videoNode.get("aspect_ratio");
		if (aspectRatioNode != null) {
			List<Long> aspectRatioList = new ArrayList<>();
			Iterator<JsonNode> aspectRatioChildNodes = aspectRatioNode.getElements();
			while (aspectRatioChildNodes.hasNext()) {
				aspectRatioList.add(aspectRatioChildNodes.next().getLongValue());
			}
			videoInfo.setAspectRatio(aspectRatioList);
		}

		JsonNode variantsNode = videoNode.get("variants");

		if (variantsNode != null) {
			List<Variant> variantList = new ArrayList<>();
			Iterator<JsonNode> variantChildNodes = variantsNode.getElements();
			while (variantChildNodes.hasNext()) {
				Variant variant = new Variant();
				JsonNode child = variantChildNodes.next();
				if (child.get("bitrate") != null) {
					variant.setBitrate(child.get("bitrate").getLongValue());
				}
				variant.setContentType(child.get("content_type").getTextValue());
				variant.setUrl(child.get("url").getTextValue());
				variantList.add(variant);
			}
			videoInfo.setVariants(variantList);
		}

		return videoInfo;
	}

	/**
	 * VideoInfoリストを整形してprintします
	 */
	public void printFormatVideoInfo() {
		System.out.println("video_info");
		System.out.println("|-- duration_millis : " + this.getDurationMillis());
		System.out.println("|-- aspect_ratio : " + this.getAspectRatio());
		System.out.println("|-- variants : [ ");
		for (Variant variant : this.getVariants()) {
			System.out.println("  {");
			System.out.println("  |-- bitrate : " + variant.getBitrate());
			System.out.println("  |-- content_type : " + variant.getContentType());
			System.out.println("  |-- url : " + variant.getUrl());
			System.out.println("  }");
		}
		System.out.println("  ]");
	}

	public long getDurationMillis() {
		return durationMillis;
	}

	public void setDurationMillis(long durationMillis) {
		this.durationMillis = durationMillis;
	}

	public List<Variant> getVariants() {
		return variants;
	}

	public void setVariants(List<Variant> variants) {
		this.variants = variants;
	}

	public List<Long> getAspectRatio() {
		return aspectRatio;
	}

	public void setAspectRatio(List<Long> aspectRatio) {
		this.aspectRatio = aspectRatio;
	}
}
