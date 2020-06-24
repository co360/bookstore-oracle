package bookstore.web.servlet.client;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bookstore.domain.Notice;
import bookstore.service.NoticeService;
import bookstore.service.ProductService;

/**
 * Servlet implementation class ShowIndexServlet
 */
@WebServlet("/ShowIndexServlet")
public class ShowIndexServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ShowIndexServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 查询最近一条公告，传递到index.jsp页面进行展示
		Notice notice = new Notice();
		NoticeService noticeService = new NoticeService();
		notice = noticeService.getRecentNotice();
		request.setAttribute("n", notice);

		// 查询本周热销的两条商品，传递到index.jsp页面进行展示
		ProductService productService = new ProductService();
		List<Object[]> list = productService.getWeekHotProduct();
		request.setAttribute("pList", list);

		// 请求转发,跳转到/client/index.jsp页面
		RequestDispatcher dispatcher = null;
		dispatcher = this.getServletContext().getRequestDispatcher("/client/index.jsp");
		dispatcher.forward(request, response);

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
