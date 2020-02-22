import java.util.ArrayList;

public interface Subject {
	public void register(Observable o);	
	public void notifyObserver();
	
}
