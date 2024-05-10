package Demo195;

import java.security.SecureRandom;

public class CIDGenerator {

	private static final SecureRandom random = new SecureRandom();

	public static int generateCID() {
		// Generate a random number, for example, within the range of 100000 to 999999
		return 100000 + random.nextInt(900000);
	}
}
