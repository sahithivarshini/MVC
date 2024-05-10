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

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	String verify = "no";

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public LoginServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		// response.getWriter().append("Served at: ").append(request.getContextPath());
		javax.servlet.RequestDispatcher dispatcher = request.getRequestDispatcher("login.jsp");
		dispatcher.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Retrieve username and password from the request
		String username = request.getParameter("username");
		String password = request.getParameter("password");

		// Initialize JDBC connection variables
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			// Load the PostgreSQL JDBC driver
			Class.forName("org.postgresql.Driver");

			// Establish the connection
			conn = DriverManager.getConnection("jdbc:postgresql://192.168.110.48:5432/plf_training",
					"plf_training_admin", "pff123");
			response.setContentType("text/plain");
			// Prepare the SQL query to check the user credentials
			String sql = "SELECT username,password FROM customer2003 WHERE username = ? and password=?";

			stmt = conn.prepareStatement(sql);
			stmt.setString(1, username);
			stmt.setString(2, password);
			rs = stmt.executeQuery();

			if (rs.next()) {
				// If the user exists, redirect to a success page
				String s_pass = rs.getString("password");
				if (password.equals(s_pass)) {
					System.out.println("success");
					verify = "yes";
				}
			} else {
				verify = "no";
			}
		} catch (ClassNotFoundException e) {
			// Handle class not found exception
			e.printStackTrace();
		} catch (SQLException e) {
			// Handle SQL exception
			e.printStackTrace();
		} finally {
			// Close JDBC resources in a finally block to ensure they are always closed
			try {
				if (rs != null)
					rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		// Redirect to an error page if an exception occurs or no valid response is sent
		PrintWriter pw = response.getWriter();
		pw.print(verify);
	}

}