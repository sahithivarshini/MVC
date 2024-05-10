package Demo195;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class AllProductServlet
 */
@WebServlet("/AllProductServlet")
public class AllProductServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private AllProductsDAO allproductDAO;

	public void init() {
		allproductDAO = new AllProductsDAL();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		List<Product> products = allproductDAO.getAllProducts();

		// Set response content type to JSON
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");

		// Write JSON array directly to response PrintWriter
		try (PrintWriter out = response.getWriter()) {
			out.print("[");
			for (int i = 0; i < products.size(); i++) {
				Product product = products.get(i);
				out.print("{\"pid\":" + product.getPid() + ",");
				out.print("\"pname\":\"" + product.getPname() + "\",");
				out.print("\"price\":" + product.getPrice() + ",");
				out.print("\"hsncode\":\"" + product.getHsncode() + "\",");
				out.print("\"pcid\":\"" + product.getPcid() + "\",");
				out.print("\"image\":\"" + product.getImage() + "\"}");
				if (i < products.size() - 1) {
					out.print(",");
				}
			}
			out.print("]");
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}
