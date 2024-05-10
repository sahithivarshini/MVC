package Demo195;

import java.util.List;

public interface ProductDAO {
	List<Product> getProductsByCategory(String categoryId);
}
