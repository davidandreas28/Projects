
public interface Observable {

	void update(int id, Patient p, Doctor d, String operation, String whoThisFor);
	void clearPrinter();
	void sortIt();
}
