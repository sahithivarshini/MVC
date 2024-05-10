package Demo195;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class OrderDAL implements OrderDAO {

	static {
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private Connection getConnection() throws SQLException {
		return DriverManager.getConnection("jdbc:postgresql://192.168.110.48:5432/plf_training", "plf_training_admin",
				"pff123");
	}

	@Override
	public int insertOrder(Order order) throws SQLException {
		String sql = "INSERT INTO orders2003 (oid, oname, ototal, cid) VALUES (?, ?, ?, ?) RETURNING oid;";
		try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			int oid = OIDGenerator.generateOID();
			stmt.setInt(1, oid);
			stmt.setString(2, order.getOname());
			stmt.setDouble(3, order.getOtotal());
			stmt.setInt(4, order.getCid());

			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					return rs.getInt(1);
				}
			}
		}
		return -1; // Error case
	}

	@Override
	public void insertOrderedProducts(List<OrderedProduct> products) throws SQLException {
		String sql = "INSERT INTO ordered_products2003 (oid, pid, qty, price) VALUES (?, ?, ?, ?);";
		try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			for (OrderedProduct product : products) {
				stmt.setInt(1, product.getOid());
				stmt.setInt(2, product.getPid());
				stmt.setInt(3, product.getQty());
				stmt.setDouble(4, product.getPrice());
				stmt.addBatch();
			}
			stmt.executeBatch();
		}
	}
}
