package Demo195;

import java.security.SecureRandom;

public class OIDGenerator {

	private static final SecureRandom random = new SecureRandom();

	public static int generateOID() {
		// Generate a random number within the range of 100000 to 999999
		return 100000 + random.nextInt(900000);
	}
}