package Demo195;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AllProductsDAL implements AllProductsDAO {

	private static final String SELECT_ALL_PRODUCTS = "SELECT * FROM products2003";

	@Override
	public List<Product> getAllProducts() {
		List<Product> products = new ArrayList<>();

		try (Connection connection = DriverManager.getConnection("jdbc:postgresql://192.168.110.48:5432/plf_training",
				"plf_training_admin", "pff123");
				PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_PRODUCTS)) {

			ResultSet rs = preparedStatement.executeQuery();

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
		}

		return products;
	}
}
