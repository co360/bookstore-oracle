package bookstore.web.servlet.client;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bookstore.domain.Product;
import bookstore.service.ProductService;

/**
 * Servlet implementation class FindProductById
 */
@WebServlet("/findProductById")
public class FindProductByIdServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public FindProductByIdServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 得到商品的id
		String id = request.getParameter("id");
		// 获取type参数值，此处的type用于区别普通用户和超级用户
		String type = request.getParameter("type");
		ProductService service = new ProductService();
		try {
			// 通过id查找商品
			Product p = service.findProductById(id);
			request.setAttribute("p", p);
			// 跳转到info.jsp页面
			if (type == null) {
				request.getRequestDispatcher("/client/info.jsp").forward(request, response);
				return;
			}
			request.getRequestDispatcher("/admin/products/edit.jsp").forward(request, response);
			return;
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
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
