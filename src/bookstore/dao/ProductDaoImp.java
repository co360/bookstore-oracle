package bookstore.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ArrayListHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import bookstore.domain.Order;
import bookstore.domain.OrderItem;
import bookstore.domain.Product;
import bookstore.utils.DataSourceUtils;

public class ProductDaoImp implements ProductDao {

	// 前台，获取本周热销商品
	@Override
	public List<Object[]> getWeekHotProduct() throws SQLException {
//		String sql = "SELECT products.id,products.name, " + " products.imgurl,SUM(orderitem.buynum) totalsalnum "
//				+ " FROM orderitem,orders,products " + " WHERE orderitem.order_id = orders.id "
//				+ " AND products.id = orderitem.product_id " + " AND orders.paystate=1 "
//				+ " AND orders.ordertime > DATE_SUB(NOW(), INTERVAL 7 DAY) "
//				+ " GROUP BY products.id,products.name,products.imgurl " + " ORDER BY totalsalnum DESC "
//				+ " LIMIT 0,2 ";
		String sql = "SELECT * \r\n" + 
				"FROM (SELECT \"products\".\"id\",\"products\".\"name\",\"products\".\"imgurl\",SUM(\"orderitem\".\"buynum\") totalsalnum FROM \"orderitem\",\"orders\",\"products\" WHERE \"orderitem\".\"order_id\" = \"orders\".\"id\" AND \"products\".\"id\" = \"orderitem\".\"product_id\" AND \"orders\".\"paystate\"='1' AND \"orders\".\"ordertime\" > sysdate-'7' GROUP BY \"products\".\"id\",\"products\".\"name\",\"products\".\"imgurl\" ORDER BY totalsalnum DESC) \r\n" + 
				"WHERE rownum<='2'";
		QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
		return runner.query(sql, new ArrayListHandler());
	}

	// 获取当前页数据
	@Override
	public List<Product> findByPage(int currentPage, int currentCount, String category) throws SQLException {
		// 要执行的sql语句
		String sql = null;
		// 参数
		Object[] obj = null;
		if (!"全部商品".equals(category)) {
//			sql = "select * from products  where category=? limit ?,?";
			sql = "select * from \"products\" where \"category\"=? and rownum<=? minus select * from \"products\" where \"category\"=? and rownum<?";
//			obj = new Object[] { category, (currentPage - 1) * currentCount, category, currentCount };
			obj = new Object[] { category, currentCount, category,(currentPage - 1) * currentCount };
		} else {
//			sql = "select * from products  limit ?,?";
			sql = "select * from \"products\" where rownum<=? minus select * from \"products\" where rownum<?";
//			obj = new Object[] { (currentPage - 1) * currentCount, currentCount };
			obj = new Object[] { currentCount, (currentPage - 1) * currentCount };
		}
		QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
		return runner.query(sql, new BeanListHandler<Product>(Product.class), obj);
	}

	// 根据商品分类获取商品数据总条数
	@Override
	public int findAllCount(String category) throws SQLException {
		String sql = "select count(*) from \"products\"";
		QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
		if (!"全部商品".equals(category)) {
			sql += " where \"category\"=?";
			Long count = Long.parseLong(runner.query(sql, new ScalarHandler(), category).toString());
			return count.intValue();
		} else {
//			Long count = (Long) runner.query(sql, new ScalarHandler());
			Long count = Long.parseLong(runner.query(sql, new ScalarHandler()).toString());
			return count.intValue();
		}
	}

	public List<Product> findBookByName(int currentPage, int currentCount, String searchfield) throws SQLException {
		// 根据名字模糊查询图书
//		String sql = "SELECT * FROM \"products\" WHERE \"name\" LIKE '%" + searchfield + "%' LIMIT ?,?";
		String sql = "SELECT * FROM \"products\" WHERE \"name\" LIKE '%" + searchfield + "%' AND rownum<=? minus SELECT * FROM \"products\" WHERE \"name\" LIKE '%" + searchfield + "%' AND rownum<?";
		QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
//		return runner.query(sql, new BeanListHandler<Product>(Product.class), currentPage - 1, currentCount);
		return runner.query(sql, new BeanListHandler<Product>(Product.class), currentCount, currentPage - 1);
	}

	public int findBookByNameAllCount(String searchfield) throws SQLException {
		String sql = "SELECT COUNT(*) FROM \"products\" WHERE \"name\" LIKE '%" + searchfield + "%'";
		QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
		// 查询出满足条件的总数量，为long类型
		Long count = Long.parseLong(runner.query(sql, new ScalarHandler()).toString());
		return count.intValue();
	}

	// 根据id查找商品
	@Override
	public Product findProductById(String id) throws SQLException {
		String sql = "select * from \"products\" where \"id\"=?";
		QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
		return runner.query(sql, new BeanHandler<Product>(Product.class), id);
		// return null;
	}

	// 生成订单时，将商品数量减少
	@Override
	public void changeProductNum(Order order) throws SQLException {
		String sql = "update \"products\" set \"pnum\"=\"pnum\"-? where \"id\"=?";
		QueryRunner runner = new QueryRunner();
		List<OrderItem> items = order.getOrderItems();
		Object[][] params = new Object[items.size()][2];
		for (int i = 0; i < params.length; i++) {
			params[i][0] = items.get(i).getBuynum();
			params[i][1] = items.get(i).getP().getId();
		}
		runner.batch(DataSourceUtils.getConnection(), sql, params);
	}

	// 删除订单时，修改商品数量
	public void updateProductNum(List<OrderItem> items) throws SQLException {
		String sql = "update \"products\" set \"pnum\"=\"pnum\"+? where \"id\"=?";
		QueryRunner runner = new QueryRunner();
		Object[][] params = new Object[items.size()][2];
		for (int i = 0; i < params.length; i++) {
			params[i][0] = items.get(i).getBuynum();
			params[i][1] = items.get(i).getP().getId();
		}
		runner.batch(DataSourceUtils.getConnection(), sql, params);
	}

	public List<Product> listAll() throws SQLException {
		QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "SELECT * FROM \"products\"";
		return runner.query(sql, new BeanListHandler<Product>(Product.class));
	}
}