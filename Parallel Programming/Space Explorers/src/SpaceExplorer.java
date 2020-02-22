import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Set;
import java.util.concurrent.Semaphore;
import java.nio.charset.StandardCharsets;

/**
 * Class for a space explorer.
 */
public class SpaceExplorer extends Thread {

	private Integer hashCount;
	private Set<Integer> discovered;
	private CommunicationChannel channel;
	private Semaphore semaphore;

	/**
	 * Creates a {@code SpaceExplorer} object.
	 * 
	 * @param hashCount
	 *            number of times that a space explorer repeats the hash operation
	 *            when decoding
	 * @param discovered
	 *            set containing the IDs of the discovered solar systems
	 * @param channel
	 *            communication channel between the space explorers and the
	 *            headquarters
	 */
	public SpaceExplorer(Integer hashCount, Set<Integer> discovered, CommunicationChannel channel) {
		this.hashCount = hashCount;
		this.discovered = discovered;
		this.channel = channel;
		this.semaphore = new Semaphore(1);
	}

	@Override
	public void run() {
		while(true) {
			Message newMessageReceived1;
			Message newMessageReceived2;

			try {
				semaphore.acquire();
			} catch (InterruptedException e) {
				return ;
			}
			synchronized(this) {
			newMessageReceived1 = this.channel.getMessageHeadQuarterChannel();
			if (newMessageReceived1.getData().equals(HeadQuarter.EXIT)) {
				semaphore.release();
				break;
				// break;
			}
			if (newMessageReceived1.getData().equals(HeadQuarter.END)) {
				semaphore.release();
				continue;
			}
			

			newMessageReceived2 = this.channel.getMessageHeadQuarterChannel();
				Integer parentSystem = newMessageReceived1.getCurrentSolarSystem();
				Integer currentSystem = newMessageReceived2.getCurrentSolarSystem();
				String currentSystemName = newMessageReceived2.getData();
				String encriptedMessage = encryptMultipleTimes(currentSystemName, hashCount);
				Message messageToHq = new Message(parentSystem, currentSystem, encriptedMessage);

			if (!discovered.contains(currentSystem)) {
			discovered.add(currentSystem);
				this.channel.putMessageSpaceExplorerChannel(messageToHq);
			}
			}
			semaphore.release();
		}
	}
	
	/**
	 * Applies a hash function to a string for a given number of times (i.e.,
	 * decodes a frequency).
	 * 
	 * @param input
	 *            string to he hashed multiple times
	 * @param count
	 *            number of times that the string is hashed
	 * @return hashed string (i.e., decoded frequency)
	 */
	private String encryptMultipleTimes(String input, Integer count) {
		String hashed = input;
		for (int i = 0; i < count; ++i) {
			hashed = encryptThisString(hashed);
		}

		return hashed;
	}

	/**
	 * Applies a hash function to a string (to be used multiple times when decoding
	 * a frequency).
	 * 
	 * @param input
	 *            string to be hashed
	 * @return hashed string
	 */
	private static String encryptThisString(String input) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			byte[] messageDigest = md.digest(input.getBytes(StandardCharsets.UTF_8));

			// convert to string
			StringBuffer hexString = new StringBuffer();
			for (int i = 0; i < messageDigest.length; i++) {
				String hex = Integer.toHexString(0xff & messageDigest[i]);
				if (hex.length() == 1)
					hexString.append('0');
				hexString.append(hex);
			}
			return hexString.toString();

		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}
}
