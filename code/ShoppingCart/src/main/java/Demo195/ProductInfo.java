package Demo195;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductInfo {
	private int pid;
	private String pname;
	private double price;
	private String hsncode;
	private int pcid;
	private int qty;
	private double gst;
	double pricewithoutgst;
	double gstAmount;
	double shippingcharges = 0;
	double scharges = 0;

	public ProductInfo(int pid, String pname, double price, String hsncode, int pcid, int qty, double gst) {
		this.pid = pid;
		this.pname = pname;
		this.price = price;
		this.hsncode = hsncode;
		this.pcid = pcid;
		this.qty = qty;
		this.gst = gst;
	}

	public double getshippingchargesamount(double price) {
		if (price < 2000) {
			shippingcharges = 0;
			shippingcharges += 100;

		} else if (price < 10000) {
			shippingcharges = 0;
			shippingcharges += 350;

		} else if (price < 20000) {
			shippingcharges = 0;
			shippingcharges += 700;

		} else if (price < 50000) {
			shippingcharges = 0;
			shippingcharges += 1000;

		} else if (price < 100000) {
			shippingcharges = 0;
			shippingcharges += 2000;

		} else if (price < 150000) {
			shippingcharges = 0;
			shippingcharges += 2500;

		} else {
			shippingcharges = 0;
			shippingcharges += 3500;
		}
		return shippingcharges;
	}

	// Getters and setters for all fields
	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	public String getPname() {
		return pname;
	}

	public void setPname(String pname) {
		this.pname = pname;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getHsncode() {
		return hsncode;
	}

	public void setHsncode(String hsncode) {
		this.hsncode = hsncode;
	}

	public int getPcid() {
		return pcid;
	}

	public void setPcid(int pcid) {
		this.pcid = pcid;
	}

	public int getQty() {
		return qty;
	}

	public void setQty(int qty) {
		this.qty = qty;
	}

	// Method to increment the quantity
	public void incrementQuantity() {
		this.qty += 1;
	}

	// Method to calculate the total price
	public double calculateTotalPrice() {

		return (this.price);
	}

	public double getpricewithoutgst() {
		pricewithoutgst = (this.price / (1 + (this.gst / 100)));
		System.out.println(pricewithoutgst);

		return pricewithoutgst;
	}

	public double getgstAmount() {
		gstAmount = this.price - pricewithoutgst;
		System.out.println(gstAmount);

		return gstAmount;
	}

	// Method to find the GST based on the HSN code
	public void findgst(String hsncode) throws SQLException {
		try {
			Class.forName("org.postgresql.Driver");
			Connection conn = DriverManager.getConnection("jdbc:postgresql://192.168.110.48:5432/plf_training",
					"plf_training_admin", "pff123");
			String sql = "SELECT gst_percentage FROM hsncode2003 WHERE hsncode=?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, hsncode);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				this.gst = rs.getDouble("gst_percentage");
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public double getGst() {
		return gst;
	}

	public void setGst(double gst) {
		this.gst = gstAmount;
	}

	public double getshippingcharges() {
		scharges = (pricewithoutgst * getshippingchargesamount(price)) / price;
		return scharges;
	}
}