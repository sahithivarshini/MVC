package Demo195;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/cat")
public class ProductCategoryServlet extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		PrintWriter out = response.getWriter();

		try {
			// Establish database connection
			Class.forName("org.postgresql.Driver");
			conn = DriverManager.getConnection("jdbc:postgresql://192.168.110.48:5432/plf_training",
					"plf_training_admin", "pff123");

			// Prepare SQL query to fetch category names
			String sql = "SELECT pcname FROM product_category2003";
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();

			StringBuilder options = new StringBuilder();
			while (rs.next()) {
				String categoryName = rs.getString("pcname");
				options.append("<option value=\"").append(categoryName).append("\">").append(categoryName)
						.append("</option>");
			}

			// Send the options as the response
			out.print(options.toString());
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			// Handle exceptions
		} finally {
			// Close resources
			try {
				if (rs != null)
					rs.close();
				if (stmt != null)
					stmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
