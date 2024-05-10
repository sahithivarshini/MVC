package Demo195;

public class Customer {
	private String name;
	private String mobile;
	private String location;
	private String address;
	private String username;
	private String password;

	// Constructor
	public Customer(String name, String mobile, String location, String address, String username, String password) {
		this.name = name;
		this.mobile = mobile;
		this.location = location;
		this.address = address;
		this.username = username;
		this.password = password;
	}

	// Getters and Setters
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
