import java.util.Comparator;

public class TriageComparator implements Comparator<Patient> {

	@Override
	public int compare(Patient a, Patient b) {
		if (a.getState().getSeverity() > b.getState().getSeverity()) {
			return b.getState().getSeverity() - a.getState().getSeverity();
		}
		return 0;
	}

}