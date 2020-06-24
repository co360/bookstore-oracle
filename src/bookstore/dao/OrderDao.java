package bookstore.dao;

import java.sql.SQLException;
import java.util.List;

import bookstore.domain.Order;
import bookstore.domain.User;

public interface OrderDao {
	// 生成订单
	public void addOrder(Order order) throws SQLException;

	// 根据User查询订单信息
	public List<Order> findOrderByUser(User user) throws SQLException;

	// 根据id查询订单信息
	public Order findOrderById(String id) throws SQLException;
}
