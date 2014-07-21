package akb428.tkws.dao;

import java.sql.SQLException;
import java.util.List;

import akb428.tkws.model.MediaUrlModel;

/*
 * 将来SQLite, MariaDB, MySQL等を切り替えるためインターフェイス化しておく
 *
 */
public interface IMediaUrlDao {

	public void closeHandler();

	public void tableCheckAndCreate() throws SQLException;

	public boolean isExistUrl(String url);

	public void registUrl(String url, String keyword, String twitterUserName);

	public void deleteAndCopyHistory(MediaUrlModel mediaUrlModel);

	public List<MediaUrlModel> getUrlList();

}
