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

@WebServlet("/checkServiceability")
public class CheckServiceabilityServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Fetch pin code from request parameter
		String pinCode = request.getParameter("pinCode");
		System.out.println("Received pin code: " + pinCode);

		// Fetch product category ID from request parameter
		// Fetch product category ID from request parameter
		String productCategoryId = request.getParameter("productCategoryId");
		System.out.println("Received product category ID: " + productCategoryId);

		// Call a method to check serviceability based on the pin code and product category ID
		var serviceable = isServiceable(pinCode, productCategoryId);

		System.out.println("Serviceable: " + serviceable);

		// Set response content type
		response.setContentType("application/json");

		// Create JSON response
		String jsonResponse = "{\"serviceable\": " + serviceable + "}";
		System.out.println("JSON response: " + jsonResponse);

		// Send JSON response
		PrintWriter out = response.getWriter();
		out.print(jsonResponse);
		out.flush();
	}

	// Method to check serviceability based on the pin code and product category ID
	private boolean isServiceable(String pinCode, String productCategoryId) {
		boolean serviceable = false;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			// Load JDBC driver
			Class.forName("org.postgresql.Driver");
			System.out.println("JDBC Driver loaded successfully");

			// Connect to database
			conn = DriverManager.getConnection("jdbc:postgresql://192.168.110.48:5432/plf_training",
					"plf_training_admin", "pff123");
			System.out.println("Connected to database");

			// Prepare SQL statement
			String sql = "SELECT * FROM ServiceableRegions195 s "
					+ "JOIN productcategorywiseserviceableregions195 p ON s.srrg_id = p.srrg_id "
					+ "WHERE CAST(? AS INTEGER) BETWEEN s.srrg_pinfrom AND s.srrg_pinto "
					+ "AND p.prct_id = CAST(? AS INTEGER)";

			stmt = conn.prepareStatement(sql);
			stmt.setString(1, pinCode);
			stmt.setString(2, productCategoryId);
			System.out.println("SQL statement: " + stmt.toString());

			// Execute SQL query
			rs = stmt.executeQuery();
			System.out.println("SQL query executed");

			// Check if any row is returned
			if (rs.next()) {
				serviceable = true;
				System.out.println("Serviceable region found");
			} else {
				System.out.println("No serviceable region found");
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			// Close resources
			try {
				if (rs != null)
					rs.close();
				if (stmt != null)
					stmt.close();
				if (conn != null)
					conn.close();
				System.out.println("Database resources closed");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return serviceable;
	}
}
