package Demo195;

import java.sql.SQLException;
import java.util.List;

public interface OrderDAO {
	int insertOrder(Order order) throws SQLException;

	void insertOrderedProducts(List<OrderedProduct> products) throws SQLException;
}
