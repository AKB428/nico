package akb428.tkws.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

public class Application {

	public static Properties properties = null;
	public static boolean isMessageQueue = false;
	public static String _saveFolder = null;

	public static String configFilename = "./config/application.properties";

	public Application() throws UnsupportedEncodingException, IOException, ClassNotFoundException {
		new Application(configFilename);
	}

	public Application(String configFilename) throws UnsupportedEncodingException, IOException, ClassNotFoundException {

		Application.configFilename = configFilename;
		InputStream inStream = new FileInputStream(Application.configFilename);
		properties = new Properties();
		properties.load(new InputStreamReader(inStream, "UTF-8"));

		loadDriver();

		initIsMessageQueue();
	}

	private void loadDriver() throws ClassNotFoundException {
		if ("H2".equals(Application.properties.getProperty("application.mode"))) {
			Class.forName("org.h2.Driver");
		} else if ("MariaDB".equals(Application.properties.getProperty("application.mode"))) {
			Class.forName("org.mariadb.jdbc.Driver");
		}
	}

	private void initIsMessageQueue() {
		isMessageQueue = Application.properties.getProperty("messageQueue").equals("true") ? true : false;
	}
	
	public static String saveFolder() {
		if (_saveFolder != null) {
			return _saveFolder;
		}

		 _saveFolder = Application.properties.getProperty("twitter.media.downloadPath") + 
		Application.properties.getProperty("twitter.searchTargetId");
		 
		 return _saveFolder;
	}
}
