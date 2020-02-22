import java.util.Comparator;

public class SeverityComp implements Comparator<Patient> {

	@Override
	public int compare(Patient arg0, Patient arg1) {
		if (arg1.getUrgency().getValue() == arg0.getUrgency().getValue()) {
			return arg1.getState().getSeverity() - arg0.getState().getSeverity();
		}
		return 0;
	}

}
