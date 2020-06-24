package bookstore.dao;

import java.sql.SQLException;

import bookstore.domain.Notice;

public interface NoticeDao {
	// 前台系统，查询最新添加或修改的一条公告
	public Notice getRecentNotice() throws SQLException;
}
