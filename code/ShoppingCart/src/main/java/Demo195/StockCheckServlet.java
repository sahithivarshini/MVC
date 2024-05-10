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

@WebServlet("/StockCheckServlet")
public class StockCheckServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	// JDBC URL, username, and password
	private static final String JDBC_URL = "jdbc:postgresql://192.168.110.48:5432/plf_training";
	private static final String JDBC_USERNAME = "plf_training_admin";
	private static final String JDBC_PASSWORD = "pff123";

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Get product ID from request parameter
		String productId = request.getParameter("pid");
		int pid = Integer.parseInt(productId);

		// Initialize database connection
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			// Connect to the database
			conn = DriverManager.getConnection(JDBC_URL, JDBC_USERNAME, JDBC_PASSWORD);

			// Prepare SQL query to retrieve stock for the given product ID
			String sql = "SELECT prod_stock FROM ProductStock195 WHERE prod_id = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, pid);

			// Execute query
			rs = pstmt.executeQuery();

			// Check if the product exists and get the stock
			int stock = 0;
			if (rs.next()) {
				stock = rs.getInt("prod_stock");
			}

			// Set response content type
			response.setContentType("application/json");
			PrintWriter out = response.getWriter();
			// Write stock information as JSON response
			out.println("{\"stock\": " + stock + "}");
		} catch (SQLException e) {
			e.printStackTrace();
			// Handle database errors
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		} finally {
			// Close JDBC objects
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
