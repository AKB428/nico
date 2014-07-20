package akb428.dao;

import java.sql.SQLException;

/*
 * 将来SQLite, MariaDB, MySQL等を切り替えるためインターフェイス化しておく
 *
 */
public interface IMediaUrlDao {

	public void closeHandler();

	public void tableCheckAndCreate() throws SQLException;

	public boolean isExistUrl(String url);

	public void registUrl(String url, String keyword, String twitterUserName);

	public void deleteAndMoveHistoryUrl(String url);

}
