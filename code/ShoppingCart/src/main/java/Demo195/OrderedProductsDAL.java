package Demo195;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class OrderedProductsDAL implements OrderedProductsDAO {

	@Override
	public int insertOrders(double totalPrice, CartList cartList, String username) throws SQLException {
		int oid = 0;
		int c = 0;
		double shippingcharges = 0;
		ResultSet r1 = null;
		Connection conn1 = DriverManager.getConnection("jdbc:postgresql://192.168.110.48:5432/plf_training",
				"plf_training_admin", "pff123");
		String sql1 = "select cid from customer2003 where username=?";
		PreparedStatement st = conn1.prepareStatement(sql1);
		st.setString(1, username);
		r1 = st.executeQuery();
		if (r1.next()) {
			c = r1.getInt("cid");
		}

		List<ProductInfo> product = cartList.getAllProducts();
		oid = OIDGenerator.generateOID();
		Connection conn = DriverManager.getConnection("jdbc:postgresql://192.168.110.48:5432/plf_training",
				"plf_training_admin", "pff123");
		String sql = "insert into orders2003 values(?,?,?,?)";
		// String psql = "insert into products2003 (shippingcharges) values (?) where pid=?";
		PreparedStatement stmt = conn.prepareStatement(sql);
		// PreparedStatement pstmt = conn.prepareStatement(psql);
		/*
		 * for (int i = 0; i < product.size(); i++) { pstmt.setDouble(1, product.get(i).getshippingcharges());
		 * pstmt.setInt(2, product.get(i).getPid()); pstmt.executeUpdate(); }
		 */
		stmt.setInt(1, oid);
		stmt.setString(2, "order " + oid);
		stmt.setDouble(3, totalPrice);
		stmt.setInt(4, c);
		int r = stmt.executeUpdate();

		String sql2 = "insert into ordered_products2003 values(?,?,?,?,?,?)";
		PreparedStatement stmt2 = conn.prepareStatement(sql2);
		for (int i = 0; i < product.size(); i++) {
			stmt2.setInt(1, oid);
			stmt2.setInt(2, product.get(i).getPid());
			stmt2.setInt(3, product.get(i).getQty());
			stmt2.setDouble(4, product.get(i).getpricewithoutgst());
			stmt2.setString(5, product.get(i).getPname());
			stmt2.setDouble(6, product.get(i).getgstAmount());
			stmt2.executeUpdate();
		}
		return oid;
	}
}
