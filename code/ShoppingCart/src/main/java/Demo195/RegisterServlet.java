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
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	String verify = "no";

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public RegisterServlet() {
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
		response.setContentType("text/html");
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		HttpSession session = request.getSession();

		// Initialize JDBC connection variables
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String name = request.getParameter("name");
		String mobile = request.getParameter("mobile");
		String location = request.getParameter("location");
		String address = request.getParameter("address");
		String username1 = request.getParameter("username");
		String password1 = request.getParameter("password");
		try {
			// Load the PostgreSQL JDBC driver
			Class.forName("org.postgresql.Driver");
			System.out.println("register invoked");
			// Establish the connection
			conn = DriverManager.getConnection("jdbc:postgresql://192.168.110.48:5432/plf_training",
					"plf_training_admin", "pff123");
			response.setContentType("text/plain");
			// Prepare the SQL query to check the user credentials
			String sql = "INSERT INTO customer2003 (cid,name, mobile, location, address, username, password) VALUES (?,?, ?, ?, ?, ?, ?)";
			int c = CIDGenerator.generateCID();
			HttpSession ss = request.getSession();
			ss.setAttribute("username", username);
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setInt(1, c);
			statement.setString(2, name);
			statement.setString(3, mobile);
			statement.setString(4, location);
			statement.setString(5, address);
			statement.setString(6, username1);
			statement.setString(7, password1);

			System.out.println(
					c + " " + name + " " + mobile + " " + location + " " + address + " " + username1 + " " + password1);
			// Execute the query
			int rowsInserted = statement.executeUpdate();

			response.sendRedirect("index.jsp");
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