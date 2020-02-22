import java.util.ArrayList;
import java.util.Collections;

public class NursePrinter implements Observable {
	private ArrayList<String> showMe;
	@Override
	public void update(int id, Patient p, Doctor d, String operation, String whoThisFor) {
		int index = -1;
		if (whoThisFor.equals("N")) {
			for (String s : showMe) {
				if (s.contains(p.getName())) {
					index = showMe.indexOf(s);
				}
			}
			if (index != -1) {
				showMe.remove(index);
			}
			showMe.add(" treated " + p.getName() + " and patient"
					+ "has " + p.getRounds() + " more rounds");
		}
	}
	public NursePrinter() {
		this.showMe = new ArrayList<>();
	}
	public String toString() {
		String text = "~~~~ Nurses treat patients ~~~~\n";
		for (String s : showMe) {
		text  += "Nurse " + Hospital.nurseOrder +  s;
		text += "\n";
		Hospital.nurseOrder++;
		}
		return text;
	}
	public void clearPrinter() {
		Hospital.nurseOrder = 0;
		showMe.clear();
	}
	public void sortIt() {
		Collections.sort(showMe);
	}
}
