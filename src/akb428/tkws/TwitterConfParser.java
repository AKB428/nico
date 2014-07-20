package akb428.tkws;

import java.io.File;
import java.io.IOException;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import akb428.tkws.model.TwitterModel;

public class TwitterConfParser {
	public static TwitterModel readConf(String confFilename)
			throws JsonParseException, JsonMappingException, IOException {

		ObjectMapper mapper = new ObjectMapper();
		JsonNode rootNode = mapper.readValue(new File(confFilename), JsonNode.class);

		TwitterModel twitterModel = new TwitterModel();
		twitterModel.setAccessToken(rootNode.get("access_token")
				.getTextValue());
		twitterModel.setAccessToken_secret(rootNode.get(
				"access_token_secret").getTextValue());
		twitterModel.setConsumerKey(rootNode.get("consumer_key")
				.getTextValue());
		twitterModel.setConsumerSecret(rootNode.get("consumer_secret")
				.getTextValue());

		return twitterModel;
	}
}
