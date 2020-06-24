package bookstore.dao;

import java.sql.SQLException;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;

import bookstore.domain.User;
import bookstore.utils.DataSourceUtils;

public class UserDaoImp implements UserDao {

	@Override
	public void addUser(User user) throws SQLException {
		// TODO
//		String sql = "insert into user(username,password,gender,email,telephone,introduce,activecode,state) values(?,?,?,?,?,?,?,?)";
		String sql = "insert into \"user\"(\"username\",\"password\",\"gender\",\"email\",\"telephone\",\"introduce\",\"activeCode\",\"state\") values(?,?,?,?,?,?,?,?)";
		QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
		int row = runner.update(sql, new Object[] { user.getUsername(), user.getPassword(), user.getGender(),
				user.getEmail(), user.getTelephone(), user.getIntroduce(), user.getActiveCode(), 1 });
		if (row == 0) {
			throw new RuntimeException();
		}
	}

	@Override
	public User findUserByActiveCode(String activeCode) throws SQLException {
//		String sql = "select * from user where activecode=?";
		String sql = "select * from \"user\" where \"activecode\"=?";
		QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
		return runner.query(sql, new BeanHandler<User>(User.class), activeCode);
	}

	@Override
	public void activeUser(String activeCode) throws SQLException {
//		String sql = "update user set state=? where activecode=?";
		String sql = "update \"user\" set \"state\"=? where \"activecode\"=?";
		QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
		runner.update(sql, 1, activeCode);
	}

	@Override
	public User findUserByUsernameAndPassword(String username, String password) throws SQLException {
//		String sql = "select * from user where username=? and password=?";
		String sql = "select * from \"user\" where \"username\"=? and \"password\"=?";
		QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
		return runner.query(sql, new BeanHandler<User>(User.class), username, password);
	}

	@Override
	public void modifyUser(String password, String gender, String telephone, int user_id) throws SQLException {
//		String sql = "update user set password=?, gender=?, telephone=? where id=?";
		String sql = "update \"user\" set \"password\"=?, \"gender\"=?, \"telephone\"=? where \"id\"=?";
		QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
		runner.update(sql, password, gender, telephone, user_id);
	}

}
