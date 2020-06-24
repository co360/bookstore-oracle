package bookstore.service;

import java.sql.SQLException;
import java.util.Date;
import javax.security.auth.login.LoginException;

import bookstore.dao.UserDaoImp;
import bookstore.domain.User;
import bookstore.exception.ModifyException;
import bookstore.exception.RegisterException;

public class UserService {
	private UserDaoImp dao = new UserDaoImp();

	// 注册操作
	public void register(User user) throws RegisterException {
		// 调用dao完成注册操作
		try {
			dao.addUser(user);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RegisterException("注冊失败");
		}
	}

	// 登录操作
	public User login(String username, String password) throws LoginException {
		try {
			// 根据登录时表单输入的用户名和密码，查找用户
			User user = dao.findUserByUsernameAndPassword(username, password);
			// 如果找到，还需要确定用户是否为激活用户
			if (user != null) {
				return user;
			}
			throw new LoginException("用户名或密码错误");
		} catch (SQLException e) {
			e.printStackTrace();
			throw new LoginException("登录失败");
		}
	}

	// 修改操作
	public void modify(String password, String gender, String telephone, int user_id) throws ModifyException {
		// 调用dao完成注册操作
		try {
			dao.modifyUser(password, gender, telephone, user_id);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ModifyException("修改失败");
		}
	}

}
