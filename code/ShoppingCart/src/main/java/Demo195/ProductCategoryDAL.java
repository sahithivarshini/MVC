package Demo195;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductCategoryDAL implements ProductCategoryDAO {

	@Override
	public String[] getAllProductCategories() {
		String[] categories = null;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			conn = DriverManager.getConnection("jdbc:postgresql://192.168.110.48:5432/plf_training",
					"plf_training_admin", "pff123");
			String sql = "SELECT pcname FROM product_category2003";
			stmt = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = stmt.executeQuery();

			// Get the number of rows in the result set
			rs.last();
			int rowCount = rs.getRow();
			rs.beforeFirst(); // Move cursor back to the beginning

			categories = new String[rowCount];
			int i = 0;
			while (rs.next()) {
				String pcname = rs.getString("pcname");
				categories[i++] = pcname;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			// Log the exception and provide a more user-friendly error message
		} finally {
			// Close ResultSet, PreparedStatement, and Connection in a finally block
			try {
				if (rs != null) {
					rs.close();
				}
				if (stmt != null) {
					stmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return categories;
	}
}
