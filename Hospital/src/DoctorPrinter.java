import java.util.ArrayList;
import java.util.Collections;

public class DoctorPrinter implements Observable{
	private ArrayList<String> showMe;
	private int counter;
	@Override
	public void update(int id, Patient p, Doctor d, String operation, String whoThisFor) {
		if (whoThisFor.equals("D")) {
			if (operation.equals("SENDHOME")) {
				showMe.add(d.getType() + " sent " + p.getName() + " home");
			} else if (operation.equals("REMAIN")) {
				showMe.add(d.getType() + " says that " + p.getName() + " should remain in hospital");
			}
		}
	}
	public DoctorPrinter() {
		this.showMe = new ArrayList<>();
	}
	public String toString() {
		String text = "~~~~ Doctors check their hospitalized patients and give verdicts ~~~~\n";
		for (String s : showMe) {
			text  += s;
			setCounter(++counter);
			if (counter <= showMe.size()) {
				text += "\n";
			}
		}

		return text;
	}
	public void clearPrinter() {
		showMe.clear();
	}
	public void sortIt() {
		Collections.sort(showMe);
	}
	public int getCounter() {
		return counter;
	}
	public void setCounter(int counter) {
		this.counter = counter;
	}
}
