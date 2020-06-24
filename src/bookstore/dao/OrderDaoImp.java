package bookstore.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import bookstore.domain.Order;
import bookstore.domain.User;
import bookstore.utils.DataSourceUtils;

public class OrderDaoImp implements OrderDao {

	@Override
	public void addOrder(Order order) throws SQLException {
		// 1.生成Sql语句 TODO
//		String sql = "insert into orders values(?,?,?,?,?,0,null,?)";
		String sql = "insert into \"orders\"(\"id\",\"money\",\"receiverAddress\",\"receiverName\",\"receiverPhone\",\"paystate\",\"ordertime\",\"user_id\") values(?,?,?,?,?,0,null,?)";
		// 2.生成执行sql语句的QueryRunner,不传递参数
		QueryRunner runner = new QueryRunner();
		// 3.执行update()方法插入数据
		runner.update(DataSourceUtils.getConnection(), sql, order.getId(), order.getMoney(), order.getReceiverAddress(),
				order.getReceiverName(), order.getReceiverPhone(), order.getUser().getId());
	}

	public List<Order> findOrderByUser(User user) throws SQLException {
//		String sql = "select * from orders where user_id=?";
		String sql = "select * from \"orders\" where \"user_id\"=?";
		QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
		return runner.query(sql, new ResultSetHandler<List<Order>>() {
			public List<Order> handle(ResultSet rs) throws SQLException {
				List<Order> orders = new ArrayList<Order>();
				while (rs.next()) {
					Order order = new Order();
					order.setId(rs.getString("id"));
					order.setMoney(rs.getDouble("money"));
					order.setOrdertime(rs.getDate("ordertime"));
					order.setPaystate(rs.getInt("paystate"));
					order.setReceiverAddress(rs.getString("receiverAddress"));
					order.setReceiverName(rs.getString("receiverName"));
					order.setReceiverPhone(rs.getString("receiverPhone"));
					order.setUser(user);
					orders.add(order);
				}
				return orders;
			}
		}, user.getId());
	}

	public Order findOrderById(String id) throws SQLException {
//		String sql = "select * from orders where id=?";
		String sql = "select * from \"orders\" where \"id\"=?";
		QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
		return (Order) runner.query(sql, new BeanHandler<Order>(Order.class), id);
	}

	/**
	 * 根据订单号修改订单状态为已支付
	 * 
	 * @param id
	 * @throws SQLException
	 */
	public void updateOrderState(String id) throws SQLException {
//		String sql = "update orders set paystate=1 where id=?";
		String sql = "update \"orders\" set \"paystate\"=1 where \"id\"=?";
		QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
		runner.update(sql, id);
		System.out.println(runner.update(sql, id));
	}

	/**
	 * 根据id删除订单
	 * 
	 * @param id
	 * @throws SQLException
	 */
	public void delOrderById(String id) throws SQLException {
//		String sql = "delete from orders where id=?";
		String sql = "delete from \"orders\" where \"id\"=?";
		QueryRunner runner = new QueryRunner();
		runner.update(DataSourceUtils.getConnection(), sql, id);
	}

}
