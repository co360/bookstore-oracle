package bookstore.service;

import java.sql.SQLException;
import java.util.List;

import bookstore.dao.OrderDaoImp;
import bookstore.dao.OrderItemDaoImp;
import bookstore.dao.ProductDaoImp;
import bookstore.domain.Order;
import bookstore.domain.OrderItem;
import bookstore.domain.User;
import bookstore.utils.DataSourceUtils;

public class OrderService {
	private OrderDaoImp odao = new OrderDaoImp();
	private OrderItemDaoImp oidao = new OrderItemDaoImp();
	private ProductDaoImp pdao = new ProductDaoImp();

	// 1.添加订单
	public void addOrder(Order order) {
		try {
			// 1.开启事务
			DataSourceUtils.startTransaction();
			// 2.完成操作
			// 2.1向orders表中添加数据
			odao.addOrder(order);
			// 2.2向orderItem表中添加数据
			oidao.addOrderItem(order);
			// 2.3修改商品表中数据.
			pdao.changeProductNum(order);
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				DataSourceUtils.rollback(); // 事务回滚
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} finally {
			try {
				// 关闭，释放以及提交事务
				DataSourceUtils.releaseAndCloseConnection();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public List<Order> findOrderByUser(User user) {
		List<Order> orders = null;
		try {
			// 查找出订单信息
			orders = odao.findOrderByUser(user);

			// // 查找出订单项信息.
			// for (Order order : orders) {
			// List<OrderItem> items = oidao.findOrderItemByOrder(order);
			// //查找到订单中的订单项信息
			//
			// order.setOrderItems(items);
			// }

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return orders;
	}

	public Order findOrderById(String id) {
		Order order = null;
		try {
			// 查找出订单信息
			order = odao.findOrderById(id);
			// 查找出订单的详细信息
			List<OrderItem> items = oidao.findOrderItemByOrder(order);
			order.setOrderItems(items);

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return order;
	}

	public void updateState(String orderid) {
		try {
			odao.updateOrderState(orderid);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	// 根据id删除订单 管理员删除订单
	public void delOrderById(String id) {
		try {
			DataSourceUtils.startTransaction();// 开启事务
			oidao.delOrderItems(id);
			odao.delOrderById(id);
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				DataSourceUtils.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} finally {
			try {
				DataSourceUtils.releaseAndCloseConnection();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	// 普通用户删除订单
	public void delOrderByIdWithClient(String id) {
		try {
			DataSourceUtils.startTransaction();// 开启事务
			// 从订单项中查找商品购买数量
			Order order = new Order();
			order.setId(id);
			List<OrderItem> items = oidao.findOrderItemByOrder(order);
			// 修改商品数量
			pdao.updateProductNum(items);
			oidao.delOrderItems(id); // 删除订单项
			odao.delOrderById(id); // 删除订单
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				DataSourceUtils.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} finally {
			try {
				DataSourceUtils.releaseAndCloseConnection();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
