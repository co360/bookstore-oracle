package bookstore.dao;

import java.sql.SQLException;

import bookstore.domain.Order;

public interface OrderItemDao {
	// 添加订单项
	public void addOrderItem(Order order) throws SQLException;

}
