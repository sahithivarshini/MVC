package Demo195;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/products")
public class ProductServlet extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Get the selected category ID from the request parameter
		String categoryId = request.getParameter("categoryId");

		// Create an instance of ProductDAO using ProductDAL
		ProductDAO productDAO = new ProductDAL();

		// Call the DAO method to fetch products based on the category ID
		List<Product> products = productDAO.getProductsByCategory(categoryId);

		// Convert the list of products to JSON format
		StringBuilder jsonBuilder = new StringBuilder();
		jsonBuilder.append("[");
		for (int i = 0; i < products.size(); i++) {
			Product product = products.get(i);
			jsonBuilder.append("{").append("\"pid\":").append(product.getPid()).append(",").append("\"pcid\":")
					.append(product.getPcid()).append(",").append("\"pname\":\"").append(product.getPname())
					.append("\",").append("\"price\":").append(product.getPrice()).append(",").append("\"hsncode\":\"")
					.append(product.getHsncode()).append("\",").append("\"image\":\"").append(product.getImage())
					.append("\"").append("}");
			if (i < products.size() - 1) {
				jsonBuilder.append(",");
			}
		}
		jsonBuilder.append("]");

		// Set content type and write the response
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();

		out.println(jsonBuilder.toString());
	}
}
