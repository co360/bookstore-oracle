package bookstore.web.servlet.client;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;

import bookstore.dao.UserDaoImp;
import bookstore.domain.User;
import bookstore.exception.ModifyException;
import bookstore.exception.RegisterException;
import bookstore.service.UserService;
import bookstore.utils.ActiveCodeUtils;

/**
 * Servlet implementation class ModifyUserServlet
 */
@WebServlet("/ModifyUser")
public class ModifyUserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ModifyUserServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 1.得到当前用户
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		
//		String username = user.getUsername();
		int user_id = user.getId();
		String password = request.getParameter("password");
		String gender = request.getParameter("radiobutton");
		String telephone = request.getParameter("text2");
//		System.out.println(password+gender+telephone+username+user_id);
		// 调用service完成注册操作。
		UserService service = new UserService();
		try {
			service.modify(password, gender, telephone, user_id);
		} catch (ModifyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			response.getWriter().write(e.getMessage());
			return;
		}
		// 注册成功，跳转到registersuccess.jsp
		response.sendRedirect(request.getContextPath() + "/client/modifysuccess.jsp");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
