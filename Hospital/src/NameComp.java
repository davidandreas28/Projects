import java.util.Comparator;

public class NameComp implements Comparator<Patient> {

	@Override
	public int compare(Patient arg0, Patient arg1) {
		if (arg0.getUrgency() == arg1.getUrgency() && arg0.getState().getSeverity() == arg1.getState().getSeverity()) {
			if (arg0.getName() != null &&  arg1.getName()== null) {
	            return -1;
	        } else if (arg0.getName() == null && arg1.getName() != null) {
	            return 1;
	        } else if (arg0.getName() != null && arg1.getName() != null) {
	            int compare = arg0.getName().compareToIgnoreCase(arg1.getName());
	            if (compare != 0) {
	                return -compare;
	            }
	        }
		}
        // Note that we did nothing if both surnames were null or equal
		return 0;
		
	}
}
