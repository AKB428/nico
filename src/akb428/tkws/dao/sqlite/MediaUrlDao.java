package akb428.tkws.dao.sqlite;

import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import akb428.tkws.dao.AbstractMediaUrlDao;

public class MediaUrlDao extends AbstractMediaUrlDao {

	public MediaUrlDao() {
		// create a database connection
		try {
			con = DriverManager.getConnection("jdbc:sqlite:"
					+ "twitterKeyWordSearch.sqlite");

			tableCheckAndCreate();
		} catch (SQLException e) {
			// TODO 自動生成された catch ブロック
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
		ResultSet tables = dbm.getTables(null, null, TABLE_NAME, null);
		if (tables.next()) {
			// Table exists
		} else {

			statement
					.executeUpdate("create table "
							+ TABLE_NAME
							+ " (id INTEGER PRIMARY KEY AUTOINCREMENT, url, search_word, twitter_user_name, created_at, updated_at, note)");
			statement
					.executeUpdate("create table "
							+ TABLE_HISTORY_NAME
							+ " (id INTEGER PRIMARY KEY AUTOINCREMENT, original_id INTEGER, url, search_word, twitter_user_name, created_at, updated_at, note)");

			statement.executeUpdate("create index url_index on " + TABLE_NAME
					+ "(url);");
			statement.executeUpdate("create index url_index_history on "
					+ TABLE_HISTORY_NAME + "(url);");
		}

		statement.close();

	}

	@Override
	public boolean isExistUrl(String url) {

		Statement stmt;
		ResultSet rs;
		ResultSet rsHistory;

		try {
			stmt = con.createStatement();
			String query = "SELECT * FROM " + TABLE_NAME + " WHERE url = '"
					+ url + "'";
			rs = stmt.executeQuery(query);

			String query4History = "SELECT * FROM " + TABLE_HISTORY_NAME
					+ " WHERE url = '" + url + "'";
			rsHistory = stmt.executeQuery(query4History);

			while (rs.next() || rsHistory.next()) {
				return true;
			}

			rs.close();
			rsHistory.close();
			stmt.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}



}
