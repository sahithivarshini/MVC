package Demo195;

import java.util.Random;

public class IdGenerator {
	private static final Random random = new Random();

	public static int generateRandomId(int upperBound) {
		return random.nextInt(upperBound);
	}
}