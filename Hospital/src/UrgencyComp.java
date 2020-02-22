import java.util.Comparator;

public class UrgencyComp implements Comparator<Patient> {

	@Override
	public int compare(Patient arg0, Patient arg1) {
		return arg1.getUrgency().getValue() - arg0.getUrgency().getValue();
	}
	
}
