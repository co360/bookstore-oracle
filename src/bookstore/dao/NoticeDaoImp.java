package bookstore.dao;

import java.sql.SQLException;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;

import bookstore.domain.Notice;
import bookstore.utils.DataSourceUtils;

public class NoticeDaoImp implements NoticeDao {

	@Override
	public Notice getRecentNotice() throws SQLException {
		QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
//		String sql = "select * from notice order by n_time desc limit 0,1";
		String sql = "select * from \"notice\" where rownum<=2 order by \"n_time\" desc";
		Notice notice = runner.query(sql, new BeanHandler<Notice>(Notice.class));
		return notice;
	}
}
