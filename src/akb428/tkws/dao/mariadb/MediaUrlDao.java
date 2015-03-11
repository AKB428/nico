package akb428.tkws.dao.mariadb;

import java.sql.Connection;
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
			con = DriverManager.getConnection(String.format("jdbc:mariadb://%s/%s", hostname, database), username, password);

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

		if (isExsistTable(con, TABLE_NAME)) {
			// Table exists
		} else {

			statement
					.executeUpdate("create table "
							+ TABLE_NAME
							+ " (ID INT PRIMARY KEY AUTO_INCREMENT, path VARCHAR(1024), url VARCHAR(767), search_word  VARCHAR(1024), twitter_user_name VARCHAR(128), created_at VARCHAR(128),  updated_at VARCHAR(128), note VARCHAR(255));");
			statement
					.executeUpdate("create table "
							+ TABLE_HISTORY_NAME
							+ " (ID INT PRIMARY KEY AUTO_INCREMENT, original_id INT, path VARCHAR(767), url VARCHAR(2048), search_word VARCHAR(1024), twitter_user_name VARCHAR(128), created_at VARCHAR(128) ,updated_at VARCHAR(128), note VARCHAR(255));");

			statement.executeUpdate("create index url_index on " + TABLE_NAME + "(url);");
			statement.executeUpdate("create index url_index_history on " + TABLE_HISTORY_NAME + "(url);");
		}

		statement.close();

	}
	
	public boolean isExsistTable(Connection con, String table) {

		ResultSet rs;
		Statement stmt;

		String query = "SHOW TABLE STATUS";
		try {
			stmt = con.createStatement();

			rs = stmt.executeQuery(query);

			while (rs.next()) {
					String rss = rs.getString("Name");
				if (0 == table.compareToIgnoreCase(rss)) {
					rs.close();
					stmt.close();
					return true;
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;

	}

}
