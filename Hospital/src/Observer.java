import java.util.ArrayList;

public interface Observer {
	public void register(Observable o);
	public void notify(int id, Patient p, Doctor d, String operation, String whoThisFor);
}
