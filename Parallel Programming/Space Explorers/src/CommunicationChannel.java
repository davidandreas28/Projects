// import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;

/**
 * Class that implements the channel used by headquarters and space explorers to communicate.
 */
public class CommunicationChannel {

	// private static int Size = 250;
	private LinkedBlockingQueue<Message> HeadQuarterChannel;
	private LinkedBlockingQueue<Message> SpaceExplorerChannel;
	private long lastThread;
	private Semaphore getSemaphoreHq, putSemaphoreHq, getSemaphoreSE, putSemaphoreSE;
	/**
	 * Creates a {@code CommunicationChannel} object.
	 */
	public CommunicationChannel() {
		HeadQuarterChannel = new LinkedBlockingQueue<>();
		SpaceExplorerChannel = new LinkedBlockingQueue<>();
		lastThread = -1;
		getSemaphoreHq = new Semaphore(1);
		putSemaphoreHq = new Semaphore(1);
		getSemaphoreSE = new Semaphore(1);
		putSemaphoreSE = new Semaphore(1);
	}

	/**
	 * Puts a message on the space explorer channel (i.e., where space explorers write to and 
	 * headquarters read from).
	 * 
	 * @param message
	 *            message to be put on the channel
	 */
	public void putMessageSpaceExplorerChannel(Message message) {
		try {
			putSemaphoreSE.acquire();
			SpaceExplorerChannel.put(message);
		} catch(InterruptedException e) {
			return ;
		}
		putSemaphoreSE.release();
	}

	/**
	 * Gets a message from the space explorer channel (i.e., where space explorers write to and
	 * headquarters read from).
	 * 
	 * @return message from the space explorer channel
	 */
	public Message getMessageSpaceExplorerChannel() {
		Message receivedMessage = null;
		try {
			getSemaphoreSE.acquire();

			receivedMessage = SpaceExplorerChannel.take();
		} catch(InterruptedException e) {
			return null;
		}
		getSemaphoreSE.release();
		return receivedMessage;
	}

	/**
	 * Puts a message on the headquarters channel (i.e., where headquarters write to and 
	 * space explorers read from).
	 * 
	 * @param message
	 *            message to be put on the channel
	 */
	public void putMessageHeadQuarterChannel(Message message) {
		if (lastThread != Thread.currentThread().getId()) {
			try {
				putSemaphoreHq.acquire();
			} catch(InterruptedException e) {
				return ;
			}
		}

		try {
			HeadQuarterChannel.put(message);
		} catch (InterruptedException e) {
			return ;
		}

		if (message.getData().equals(HeadQuarter.END) || message.getData().equals(HeadQuarter.EXIT)) {
			lastThread = -1;
			putSemaphoreHq.release();
			return;
		}

		if (lastThread == Thread.currentThread().getId()) {
			lastThread = -1;
			putSemaphoreHq.release();
			return;
		}
		lastThread = Thread.currentThread().getId();
	}

	/**
	 * Gets a message from the headquarters channel (i.e., where headquarters write to and
	 * space explorer read from).
	 * 
	 * @return message from the header quarter channel
	 */
	public Message getMessageHeadQuarterChannel() {
		Message receivedMessage = null;
		try {
			getSemaphoreHq.acquire();
			receivedMessage = HeadQuarterChannel.take();
		} catch(InterruptedException e) {
			return null;
		}
		getSemaphoreHq.release();
		return receivedMessage;
	}
}
