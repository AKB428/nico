package akb428.tkws.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.Properties;

import akb428.tkws.dao.FactoryMediaUrlDao;
import akb428.tkws.dao.IMediaUrlDao;

public class Application {

	public static Properties properties = null;
	public static boolean isMessageQueue = false;
	public static String _saveFolder = null;
	public static int mediaDownloadThreadSleepSec = 5;

	public static String configFilename = "./config/application.properties";

	public Application() throws UnsupportedEncodingException, IOException, ClassNotFoundException, SQLException {
		new Application(configFilename);
	}

	public Application(String configFilename) throws UnsupportedEncodingException, IOException, ClassNotFoundException, SQLException {

		Application.configFilename = configFilename;
		InputStream inStream = new FileInputStream(Application.configFilename);
		properties = new Properties();
		properties.load(new InputStreamReader(inStream, "UTF-8"));

		loadDriver();

		initIsMessageQueue();
		
		mediaDownloadThreadSleepSec = Integer.valueOf(Application.properties.getProperty("mediaDownloader.sleepSec")).intValue();
		
		IMediaUrlDao iMediaUrlDao = FactoryMediaUrlDao.create();
		iMediaUrlDao.tableCheckAndCreate();
	}

	private void loadDriver() throws ClassNotFoundException {
		if ("H2".equals(Application.properties.getProperty("RDB.software"))) {
			Class.forName("org.h2.Driver");
		} else if ("MariaDB".equals(Application.properties.getProperty("RDB.software"))) {
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
