package akb428.tkws.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import akb428.tkws.model.MediaUrlModel;
import akb428.util.Calender;

public abstract class AbstractMediaUrlDao implements IMediaUrlDao{

	protected static final String TABLE_NAME = "MEDIA_URL";
	protected static final String TABLE_HISTORY_NAME = "MEDIA_URL_HISTORY";
	protected Connection con = null;

	@Override
	public boolean isExistUrl(String url) {

		Statement stmt;
		Statement stmt2;
		ResultSet rs;
		ResultSet rsHistory;

		try {
			stmt = con.createStatement();
			stmt2 = con.createStatement();
			String query = "SELECT * FROM " + TABLE_NAME + " WHERE url = '"
					+ url + "'";
			rs = stmt.executeQuery(query);

			String query4History = "SELECT * FROM " + TABLE_HISTORY_NAME
					+ " WHERE url = '" + url + "'";
			rsHistory = stmt2.executeQuery(query4History);


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
	public void deleteAndCopyHistory(MediaUrlModel mediaUrlModel) {

		try {

			String query = "INSERT INTO " + TABLE_HISTORY_NAME;
			query += " (original_id, url,search_word,twitter_user_name, created_at,updated_at) VALUES (?,?,?,?,?,?) ";

			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setInt(1, mediaUrlModel.getId());
			stmt.setString(2, mediaUrlModel.getUrl());
			stmt.setString(3, mediaUrlModel.getSearch_word());
			stmt.setString(4, mediaUrlModel.getTwitter_user_name());
			stmt.setString(5, Calender.nowString());
			stmt.setString(6, Calender.nowString());
			stmt.executeUpdate();

			stmt.close();

			// 削除
			PreparedStatement stmt2 = con.prepareStatement("DELETE FROM "
					+ TABLE_NAME + " WHERE url ='" + mediaUrlModel.getUrl()
					+ "';");
			stmt2.executeUpdate();
			stmt2.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public List<MediaUrlModel> getUrlList() {
		List<MediaUrlModel> mediaUrlModelList = new ArrayList<MediaUrlModel>();

		Statement stmt;
		ResultSet rs;

		try {
			stmt = con.createStatement();
			String query = "SELECT * FROM " + TABLE_NAME + ";";
			rs = stmt.executeQuery(query);

			while (rs.next()) {
				MediaUrlModel mediaUrlModel = new MediaUrlModel();
				mediaUrlModel.setId(rs.getInt("id"));
				mediaUrlModel.setUrl(rs.getString("url"));
				mediaUrlModel.setSearch_word(rs.getString("search_word"));
				mediaUrlModel.setTwitter_user_name(rs
						.getString("twitter_user_name"));

				mediaUrlModelList.add(mediaUrlModel);
			}

			rs.close();
			stmt.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return mediaUrlModelList;
	}
}
