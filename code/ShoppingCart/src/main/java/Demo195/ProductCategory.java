package Demo195;

public class ProductCategory {
	private int pcid = 1;
	private String pcname = "san";

	public ProductCategory(int pcid, String pcname) {
		this.pcid = pcid;
		this.pcname = pcname;
	}

	// Getters and setters
	public int getPcid() {
		return pcid;
	}

	public void setPcid(int pcid) {
		this.pcid = pcid;
	}

	public String getPcname() {
		return pcname;
	}

	public void setPcname(String pcname) {
		this.pcname = pcname;
	}
}
