package Demo195;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductDAL implements ProductDAO {
	@Override
	public List<Product> getProductsByCategory(String categoryId) {
		List<Product> products = new ArrayList<>();
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			conn = DriverManager.getConnection("jdbc:postgresql://192.168.110.48:5432/plf_training",
					"plf_training_admin", "pff123");
			String sql = "SELECT p.* FROM products2003 p JOIN product_category2003 pc ON p.pcid = pc.pcid WHERE pc.pcname = ?";
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, categoryId);
			rs = stmt.executeQuery();

			while (rs.next()) {
				Product product = new Product();
				product.setPid(rs.getInt("pid"));
				product.setPcid(rs.getInt("pcid"));
				product.setPname(rs.getString("pname"));
				product.setPrice(rs.getDouble("price"));
				product.setHsncode(rs.getString("hsncode"));
				product.setImage(rs.getString("image"));
				products.add(product);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
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

		return products;
	}
}
