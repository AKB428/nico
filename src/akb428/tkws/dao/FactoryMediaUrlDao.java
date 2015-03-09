package akb428.tkws.dao;

import akb428.tkws.config.Application;

public class FactoryMediaUrlDao {

	public static IMediaUrlDao create() {

		IMediaUrlDao mediaUrlDao = null;
		if ("H2".equals(Application.properties.getProperty("application.mode"))) {
			mediaUrlDao = new akb428.tkws.dao.h2.MediaUrlDao();
		} else if ("MariaDB".equals(Application.properties.getProperty("application.mode"))) {
			mediaUrlDao = new akb428.tkws.dao.mariadb.MediaUrlDao();
		}
		return mediaUrlDao;
	}

}
