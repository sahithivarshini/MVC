package Demo195;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONObject;

@WebServlet("/CheckoutServlet")
public class CheckoutServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		String cartJson = (String) session.getAttribute("cart");
		int oid = 0;
		if (cartJson != null) {
			try {
				JSONObject cartData = new JSONObject(cartJson);
				JSONArray items = cartData.getJSONArray("items");
				HashMap<Integer, ProductInfo> productMap = new HashMap<>();
				CartList cartList = new CartList();

				for (int i = 0; i < items.length(); i++) {
					JSONObject item = items.getJSONObject(i);
					int pid = item.getInt("pid");
					String pname = item.getString("pname");
					double price = item.getDouble("price");
					String hsncode = item.getString("hsncode");

					int pcid = item.optInt("pcid", 1);

					ProductInfo info = productMap.getOrDefault(pid,
							new ProductInfo(pid, pname, price, hsncode, pcid, 0, 0.0));
					info.incrementQuantity();
					info.setPrice(info.getQty() * price);
					info.findgst(hsncode);
					productMap.put(pid, info);
				}

				productMap.values().forEach(cartList::addProducts);
				String u = (String) session.getAttribute("username");
				System.out.println(u);
				// Calculate total price for the cart
				double totalPrice = 0;

				for (ProductInfo product : cartList.getAllProducts()) {
					totalPrice += product.calculateTotalPrice();
					System.out.println("Pid: " + product.getPid() + ", Pcid: " + product.getPcid() + ", Product Name: "
							+ product.getPname() + ", Price: $" + product.getPrice() + ", HSN Code: "
							+ product.getHsncode() + ", Quantity: " + product.getQty() + ", GST: " + product.getGst()
							+ "%");
				}

				System.out.println("Total Cart Price: $" + totalPrice);
				OrderedProductsDAO opd = new OrderedProductsDAL();
				oid = opd.insertOrders(totalPrice, cartList, u);
				session.setAttribute("oid", oid);
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.sendRedirect("final.jsp");

			} catch (Exception e) {
				e.printStackTrace();
				response.getWriter().write("Error processing cart data");
			}
		} else {
			response.getWriter().write("No cart data found in session");
		}
	}
}
