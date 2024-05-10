package Demo195;

import java.sql.SQLException;

public interface OrderedProductsDAO {
	int insertOrders(double totalPrice, CartList cartList, String username) throws SQLException;
}