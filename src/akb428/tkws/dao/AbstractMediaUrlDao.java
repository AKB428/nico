package akb428.tkws.dao;

import java.sql.Connection;

public abstract class AbstractMediaUrlDao implements IMediaUrlDao{

	protected static final String TABLE_NAME = "media_url";
	protected static final String TABLE_HISTORY_NAME = "media_url_history";
	protected Connection con = null;
}
