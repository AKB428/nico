package akb428.dao.sqlite;

import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import akb428.dao.AbstractMediaUrlDao;
import akb428.util.Calender;

public class MediaUrlDao extends AbstractMediaUrlDao{


	public MediaUrlDao() {
		// create a database connection
		try {
			con = DriverManager.getConnection("jdbc:sqlite:" + "twitterKeyWordSearch.sqlite");

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
	public void tableCheckAndCreate() throws SQLException  {
		//http://stackoverflow.com/questions/2942788/check-if-table-exists
		Statement statement = con.createStatement();
		DatabaseMetaData dbm = con.getMetaData();
		//check if "employee" table is there
		ResultSet tables = dbm.getTables(null, null, TABLE_NAME, null);
		if (tables.next()) {
			// Table exists
		}
		else {
			// Table does not exist
			statement.executeUpdate("create table " + TABLE_NAME
					+ " (id INTEGER PRIMARY KEY AUTOINCREMENT, url, search_word, twitter_user_name, created_at, updated_at, note)");
			// Table does not exist
			statement.executeUpdate("create table " + TABLE_HISTORY_NAME
					+ " (id INTEGER PRIMARY KEY AUTOINCREMENT, url, search_word, twitter_user_name, created_at, updated_at, note)");

			statement.executeUpdate("create index url_index on " + TABLE_NAME + "(url);");
			statement.executeUpdate("create index url_index_history on " + TABLE_HISTORY_NAME + "(url);");
		}

		statement.close();

	}

	@Override
	public boolean isExistUrl(String url) {

		Statement stmt;
		ResultSet rs;

		try {
			stmt = con.createStatement();
			String query = "SELECT * FROM " + TABLE_NAME + " WHERE url = '" + url + "'";
			rs = stmt.executeQuery(query);

			while (rs.next()) {
				return true;
			}

			rs.close();
			stmt.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public void registUrl(String url, String keyword, String twitterUserName) {
		try {
			String query = "INSERT INTO " + TABLE_NAME;
			query += " (url,search_word,twitter_user_name, created_at,updated_at) VALUES (?,?,?,?,?) ";

			PreparedStatement stmt = con.prepareStatement(query);

			stmt.setString(1, url);
			stmt.setString(2, keyword);
			stmt.setString(3, twitterUserName);
			stmt.setString(4, Calender.nowString());
			stmt.setString(5, Calender.nowString());
			stmt.executeUpdate();

			stmt.close();

		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

	}

	@Override
	public void deleteAndMoveHistoryUrl(String url) {
		// TODO 自動生成されたメソッド・スタブ

	}

}
