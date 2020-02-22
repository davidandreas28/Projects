import java.util.ArrayList;
import java.util.Collections;

public class PatientPrinter implements Observable{
	private ArrayList<String> showMe;
	@Override
	public void update(int id, Patient p, Doctor d, String operation, String whoThisFor) {
		int index = -1;
		if (whoThisFor.equals("P")) {
			for (String s : showMe) {
				if (s.startsWith(p.getName())) {
					index = showMe.indexOf(s);
					break;
				}
			}
			if (index != -1) {
				showMe.remove(index);
			}
			if (operation.equals("HOME")) {
				if (d.getType().equals("CARDIOLOGIST")) {
					showMe.add(p.getName() +" is " + Status.HOME_CARDIO.getValue());
				} else if (d.getType().equals("ER_PHYSICIAN")) {
					showMe.add(p.getName() +" is " + Status.HOME_ERPHYSICIAN.getValue());
				} else if (d.getType().equals("GASTROENTEROLOGIST")) {
					showMe.add(p.getName() +" is " + Status.HOME_GASTRO.getValue());
				} else if (d.getType().equals("GENERAL_SURGEON")) {
					showMe.add(p.getName() +" is " + Status.HOME_SURGEON.getValue());
				} else if (d.getType().equals("INTERNIST")) {
					showMe.add(p.getName() +" is " + Status.HOME_INTERNIST.getValue());
				} else if (d.getType().equals("NEUROLOGIST")) {
					showMe.add(p.getName() +" is " + Status.HOME_NEURO.getValue());
				}
			} else if (operation.equals("TRIAGE")) {
				showMe.add(p.getName() + " is " + Status.TRIAGEQUEUE.getValue());
			} else if (operation.equals("OTHERH")) {
				showMe.add(p.getName() +" is " + Status.OTHERHOSPITAL.getValue());
			} else if (operation.equals("OPERATION")) {
				if (d.getType().equals("CARDIOLOGIST")) {
					showMe.add(p.getName() +" is " + Status.OPERATED_CARDIO.getValue());
				} else if (d.getType().equals("ER_PHYSICIAN")) {
					showMe.add(p.getName() +" is " + Status.OPERATED_ERPHYSICIAN.getValue());
				} else if (d.getType().equals("GENERAL_SURGEON")) {
					showMe.add(p.getName() +" is " + Status.OPERATED_SURGEON.getValue());
				} else if (d.getType().equals("NEUROLOGIST")) {
					showMe.add(p.getName() +" is " + Status.OPERATED_NEURO.getValue());
				}
			} else if (operation.equals("HOMETREATMENT")) {
				showMe.add(p.getName() + " is sent " + Status.HOME_DONE_TREATMENT.getValue());
			} else if (operation.equals("INVESTIGATION")) {
				showMe.add(p.getName() + " is " + Status.INVESTIGATIONSQUEUE.getValue());
			} else if (operation.equals("EXAMINATION")) {
				showMe.add(p.getName() + " is " + Status.EXAMINATIONSQUEUE.getValue());
			} else if (operation.equals("HHH")) {
				if (d.getType().equals("CARDIOLOGIST")) {
					showMe.add(p.getName() +" is " + Status.HOSPITALIZED_CARDIO.getValue());
				} else if (d.getType().equals("ER_PHYSICIAN")) {
					showMe.add(p.getName() +" is " + Status.HOSPITALIZED_ERPHYSICIAN.getValue());
				} else if (d.getType().equals("GASTROENTEROLOGIST")) {
					showMe.add(p.getName() +" is " + Status.HOSPITALIZED_GASTRO.getValue());
				} else if (d.getType().equals("GENERAL_SURGEON")) {
					showMe.add(p.getName() +" is " + Status.HOSPITALIZED_SURGEON.getValue());
				} else if (d.getType().equals("INTERNIST")) {
					showMe.add(p.getName() +" is " + Status.HOSPITALIZED_INTERNIST.getValue());
				} else if (d.getType().equals("NEUROLOGIST")) {
					showMe.add(p.getName() +" is " + Status.HOSPITALIZED_NEURO.getValue());
				}
			}
		}
	}
	public PatientPrinter() {
		this.showMe = new ArrayList<>();
	}
	public String toString() {
		String text = "";
		for (String s : showMe) {
		text  += s;
		text += "\n";
		}
		return text;
	}
	public void sortIt() {
		Collections.sort(showMe);
	}
	public void clearPrinter() {
		showMe.clear();
	}

}
