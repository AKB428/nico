package akb428.tkws.dao.mariadb;

import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import akb428.tkws.config.Application;
import akb428.tkws.dao.AbstractMediaUrlDao;

public class MediaUrlDao extends AbstractMediaUrlDao {

	String hostname = Application.properties.getProperty("RDB.hostname");
	String username = Application.properties.getProperty("RDB.username");
	String password = Application.properties.getProperty("RDB.password");
	String database = Application.properties.getProperty("RDB.database");

	public MediaUrlDao() {

		try {
			// "jdbc:mysql://localhost/jdbctestdb";
			con = DriverManager.getConnection(String.format("jdbc:mariadb:/%s/%s", hostname, database), username, password);

			tableCheckAndCreate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void closeHandler() {
		try {
			con.close();
		} catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}

	}

	@Override
	public void tableCheckAndCreate() throws SQLException {
		// http://stackoverflow.com/questions/2942788/check-if-table-exists
		Statement statement = con.createStatement();
		DatabaseMetaData dbm = con.getMetaData();
		// check if "employee" table is there
		// http://markmail.org/message/tdl26btburmhuhkc
		String[] types = { "TABLE" };
		ResultSet tables = dbm.getTables(null, null, TABLE_NAME, types);
		if (tables.next()) {
			// Table exists
		} else {

			statement
					.executeUpdate("create table "
							+ TABLE_NAME
							+ " (ID INT PRIMARY KEY AUTO_INCREMENT, url VARCHAR(2048), search_word  VARCHAR(1024), twitter_user_name VARCHAR(128), created_at VARCHAR(128),  updated_at VARCHAR(128), note VARCHAR(255));");
			statement
					.executeUpdate("create table "
							+ TABLE_HISTORY_NAME
							+ " (ID INT PRIMARY KEY AUTO_INCREMENT, original_id INT, url VARCHAR(2048), search_word VARCHAR(1024), twitter_user_name VARCHAR(128), created_at VARCHAR(128) ,updated_at VARCHAR(128), note VARCHAR(255));");

			statement.executeUpdate("create index url_index on " + TABLE_NAME + "(url);");
			statement.executeUpdate("create index url_index_history on " + TABLE_HISTORY_NAME + "(url);");
		}

		statement.close();

	}

}
