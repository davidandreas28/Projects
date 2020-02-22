import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;


public class Hospital implements Observer{
	static int simulationLength;
	private int nurses;
	private int investigators;
	static int IDGLOBAL = 0;;
	static int round = 0;
	private ArrayList<Doctor> doctors;
	private ArrayList<Patient> incomingPatients;
	
	private ArrayList<Patient> triageQueue = new ArrayList<>();
	private ArrayList<Patient> examinationQueue = new ArrayList<>();
	private ArrayList<Patient> investigationQueue = new ArrayList<>();
	
	private ArrayList<Patient> sentHome = new ArrayList<>();
	private ArrayList<Patient> sentOutOfHospital = new ArrayList<>();
	private ArrayList<Observable> printers = new ArrayList<>();
	
	private ArrayList<Patient> spitalizedPatients = new ArrayList<>();
	static int nurseOrder = 0;
	public void setUrgencies() {
		for (Patient p : incomingPatients) {
			p.setUrgency(UrgencyEstimator.getInstance().estimateUrgency(p.getState().
					getIllnessName(), p.getState().getSeverity()));
		}
	}
	
	public void addPatientsInTriage(int round) {
		for (Patient p : incomingPatients) {
			if (p.getTime() == round) {
				triageQueue.add(p);
			}
		}
	}
	
	public int getSimulationLength() {
		return simulationLength;
	}
	public void setSimulationLength(int simulationLength) {
		this.simulationLength = simulationLength;
	}
	public int getNurses() {
		return nurses;
	}
	public void setNurses(int nurses) {
		this.nurses = nurses;
	}
	public int getInvestigators() {
		return investigators;
	}
	public void setInvestigators(int investigators) {
		this.investigators = investigators;
	}
	public ArrayList<Doctor> getDoctors() {
		return doctors;
	}
	public void setDoctors(ArrayList<Doctor> doctors) {
		this.doctors = doctors;
	}
	public ArrayList<Patient> getIncomingPatients() {
		return incomingPatients;
	}
	public void setIncomingPatients(ArrayList<Patient> incomingPatients) {
		this.incomingPatients = incomingPatients;
	}
	
	public ArrayList<Patient> getPatientsInTriageQueue() {
		return triageQueue;
	}

	public void triagePatients() {
		Collections.sort(triageQueue, new TriageComparator());
	}
	
	public void sendToExamination() {
		for (int i = 0; i < nurses; ++i) {
			if (triageQueue.isEmpty())
				break;
			examinationQueue.add(triageQueue.get(0));
			triageQueue.remove(0);
			if (triageQueue.isEmpty())
				break;
		}
		if (!triageQueue.isEmpty()) {
			for (Patient p : triageQueue) {
				notify(nurseOrder, p, p.getItsDoctor(), "TRIAGE", "P");
			}
		}
	}

	public ArrayList<Patient> getExaminationQueue() {
		return examinationQueue;
	}

	public void setExaminationQueue(ArrayList<Patient> examinationQueue) {
		this.examinationQueue = examinationQueue;
	}

	public void examinatePatients() {
		Collections.sort(examinationQueue, new UrgencyComp());
		Collections.sort(examinationQueue, new SeverityComp());
		Collections.sort(examinationQueue, new NameComp());

		ArrayList<Integer> tempVec = new ArrayList<>(); 
		int index;
		for (Patient p : examinationQueue) {
			if (p.getInvectigationResult() == InvestigationResult.NOT_DIAGNOSED) {
				if (p.getName().equals("Robinson Butler")) {
					System.out.println(p.getName() + " ESTE EXAMINAT");
				}
				Doctor temp = p.findItsDoctor(doctors);
				p.setItsDoctor(temp);
				notify(nurseOrder, p, p.getItsDoctor(), "EXAMINATION", "P");
				// temp.getItsPatients().add(p);
				rearrangeDoctors(p.getItsDoctor());
				if (p.heGoesHome(p.getItsDoctor())) {
					p.setInvestigationResult(InvestigationResult.TREATMENT);
					sentHome.add(p);
					notify(nurseOrder, p, p.getItsDoctor(), "HOME", "P");
				} else {
					if (p.getName().equals("Robinson Butler")) {
						System.out.println(p.getName() + " ESTE TRIMIS LA INSPECTIE");
					}
					sendToInvestigation(p);
				}
				index = examinationQueue.indexOf(p);
				tempVec.add(index);
			} else if (p.getInvectigationResult() == InvestigationResult.OPERATE) {
				Doctor surgeon = p.findItsSurgeon(doctors);
				
				if (surgeon == null) {
					sentOutOfHospital.add(p);
					index = examinationQueue.indexOf(p);
					tempVec.add(index);
					notify(nurseOrder, p, p.getItsDoctor(), "OTHERH", "P");
				} else {
					notify(nurseOrder, p, surgeon, "OPERATION", "P");
					notify(nurseOrder, p, surgeon, "REMAIN", "D");
					surgeon.operates(p);
					notify(nurseOrder, p, p.getItsDoctor(), "", "N");
					rearrangeDoctors(p.getItsDoctor());
					
					
				}
			} else if (p.getInvectigationResult() == InvestigationResult.HOSPITALIZE) {
				if (p.getName().equals("Robinson Butler")){
					System.out.println(p.getName() + " ESTE EXAMINAT ----> " + p.getInvectigationResult());
				}
					if (p.beenOperated == false) {
						p.setItsDoctor(p.findItsDoctor(doctors));
						rearrangeDoctors(p.getItsDoctor());
					}
					notify(nurseOrder, p, p.getItsDoctor(), "REMAIN", "D");
					notify(nurseOrder, p, p.getItsDoctor(), "HHH", "P");
					p.getPrescription();
					p.getTreatment();
					notify(nurseOrder, p, p.getItsDoctor(), "", "N");
					
					
			} else {
				if (p.beenOperated) {
					if (p.getRounds() <= 0 || p.getState().getSeverity() <= 0) {
						if (p.getName().equals("Robinson Butler")){
							System.out.println(p.getName() + " TREBUIE SA MEARGA ACASA");
						}
						p.setInvestigationResult(InvestigationResult.TREATMENT);
						sentHome.add(p);
						index = examinationQueue.indexOf(p);
						tempVec.add(index);

						notify(nurseOrder, p, p.getItsDoctor(), "SENDHOME", "D");
						notify(nurseOrder, p, p.getItsDoctor(), "HOMETREATMENT", "P");
					} else {
						p.getTreatment();
						notify(nurseOrder, p, p.getItsDoctor(), "", "N");
						notify(nurseOrder, p, p.getItsDoctor(), "REMAIN", "D");
					}
				} else {
					if (p.getName().equals("Robinson Butler")){
						System.out.println(p.getName() + " isis continua tratamentul");
					}
					if (p.shouldGoHome) {
							p.getItsDoctor();
							sentHome.add(p);
							index = examinationQueue.indexOf(p);
							tempVec.add(index);
							notify(nurseOrder, p, p.getItsDoctor(), "HOME", "P");
							rearrangeDoctors(p.getItsDoctor());
					} else {
						if (p.getRounds() <= 0 || p.getState().getSeverity() <= 0) {
							p.getItsDoctor();
							sentHome.add(p);
							index = examinationQueue.indexOf(p);
							tempVec.add(index);
							notify(nurseOrder, p, p.getItsDoctor(), "SENDHOME", "D");
							notify(nurseOrder, p, p.getItsDoctor(), "HOME", "P");
							rearrangeDoctors(p.getItsDoctor());
						} else {
							
							p.getTreatment();
							notify(nurseOrder, p, p.getItsDoctor(), "", "N");
							notify(nurseOrder, p, p.getItsDoctor(), "REMAIN", "D");
						}
					}
				}
			}
		}
		cleanExaminationQueue(tempVec);	
	}

	private void cleanExaminationQueue(ArrayList<Integer> tempVec) {
		int aux = 0;
		for (Integer i : tempVec) {
			int index = i - aux;
			examinationQueue.remove(index);
			aux++;
		}
	}

	private void sendToInvestigation(Patient p) {
		if (p.getName().equals("Robinson Butler")) {
			System.out.println(p.getName() + " ESTEPUS IN INVESTIGATIONQUEUE");
		}
		investigationQueue.add(p);
	}

	private void rearrangeDoctors(Doctor itsDoctor) {	
		int index = 0;
		for (Doctor d : doctors) {
			if (d.getID() == itsDoctor.getID()) {
				index = doctors.indexOf(d);
				doctors.add(d);
				break;
			}
		}
		doctors.remove(index);
	}

	public void setIDForDoctors() {
		for (Doctor d : doctors) {
			d.setID(IDGLOBAL++);
		}
	}

	public ArrayList<Patient> getInvestigationQueue() {
		return investigationQueue;
	}

	public void setInvestigationQueue(ArrayList<Patient> investigationQueue) {
		this.investigationQueue = investigationQueue;
	}

	public void investigatePatients() {
		Collections.sort(getInvestigationQueue(), new UrgencyComp());
		Collections.sort(getInvestigationQueue(), new SeverityComp());
		Collections.sort(getInvestigationQueue(), new NameComp());
		
		for (int i = 0; i < investigators; ++i) {
			if (investigationQueue.isEmpty()) {
				break;
			}
			investigatePatient(getInvestigationQueue().get(0));
		}
		if (!investigationQueue.isEmpty()) {
			for (Patient p : investigationQueue) {
				notify(nurseOrder, p, p.getItsDoctor(), "INVESTIGATION", "P");
			}
		}
	}

	private void investigatePatient(Patient patient) {
		if (patient.getState().getSeverity() > ERTehncian.C1.getValue()) {
			patient.setInvestigationResult(InvestigationResult.OPERATE);
		} else if (patient.getState().getSeverity() <= ERTehncian.C1.getValue()
				&& patient.getState().getSeverity() > ERTehncian.C2.getValue()) {
			patient.setInvestigationResult(InvestigationResult.HOSPITALIZE);
			//notify(nurseOrder, patient, patient.getItsDoctor(), "REMAIN", "D");
			if (patient.getName().equals("Robinson Butler")) {
				System.out.println(patient.getName() + " ESTE INSPECTAT ----> " + patient.getInvectigationResult());
			}
			notify(nurseOrder, patient, patient.getItsDoctor(), "EXAMINATION", "P");
		} else if (patient.getState().getSeverity() <= ERTehncian.C2.getValue()) {
			patient.setInvestigationResult(InvestigationResult.TREATMENT);
			patient.setShouldGoHome(true);
		}
		int index = investigationQueue.indexOf(patient);
		examinationQueue.add(patient);
		investigationQueue.remove(index);
	}

	public ArrayList<Patient> getSentHome() {
		return sentHome;
	}

	public void setSentHome(ArrayList<Patient> sentHome) {
		this.sentHome = sentHome;
	}

	public ArrayList<Patient> getSentOutOfHospital() {
		return sentOutOfHospital;
	}

	public void setSentOutOfHospital(ArrayList<Patient> sentOutOfHospital) {
		this.sentOutOfHospital = sentOutOfHospital;
	}


@Override
public void register(Observable o) {
	// TODO Auto-generated method stub
	printers.add(o);
}

@Override
public void notify(int id, Patient p, Doctor d, String operation, String whoThisFor) {
	// TODO Auto-generated method stub
	for (Observable o : printers) {
		o.update(id, p, d, operation, whoThisFor);
	}
}

public ArrayList<Observable> getPrinters() {
	return printers;
}

public void setPrinters(ArrayList<Observable> printers) {
	this.printers = printers;
}	
}
