import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Observer;

import com.fasterxml.jackson.databind.ObjectMapper;


public class ERSimulator {
	private static ERSimulator instance = null;
	private Hospital hospital;
	private ERSimulator() {
		
	}
	public static ERSimulator getInstance() {
		if (instance == null) {
			instance = new ERSimulator();
		}
		return instance;
	}
	
	public void readValues(String path) {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			hospital = objectMapper.readValue(new File(path), Hospital.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void beginSimulation() {
		hospital.setUrgencies();
		hospital.setIDForDoctors();
		
		hospital.register(new PatientPrinter());
		hospital.register(new NursePrinter());
		hospital.register(new DoctorPrinter());
		
		for (int i = 0; i < hospital.getSimulationLength(); ++i) {
			
			// Collections.sort(hospital.getPatientsTakingCareOf(), new UrgencyComp());
			// Collections.sort(hospital.getPatientsTakingCareOf(), new SeverityComp());
			//  Collections.sort(hospital.getPatientsTakingCareOf(), new NameComp());
			System.out.println("~~~ Patients in round " + (i + 1) + " ~~~");
			hospital.addPatientsInTriage(i);
			hospital.triagePatients();			
			hospital.sendToExamination();
			hospital.examinatePatients();
			hospital.investigatePatients();
			hospital.round++;
			/*if (!hospital.getExaminationQueue().isEmpty()) {
				for (Patient p : hospital.getExaminationQueue()) {
					if (p.getInvectigationResult() == InvestigationResult.TREATMENT) {
						hospital.notify(Hospital.nurseOrder, p, p.getItsDoctor(), "EXAMINATION", "P");		
					}
				}
			}*/
			
			for (Observable o : hospital.getPrinters()) {
				o.sortIt();
				System.out.println(o.toString());
				if (o instanceof NursePrinter || o instanceof DoctorPrinter) {
					o.clearPrinter();
				}
				// o.clearPrinter();
			}
			/*for (Patient p : hospital.getPatientsInTriageQueue()) {
				System.out.println(p.getName() + Status.TRIAGEQUEUE.getValue());
			}
			for (Patient p : hospital.getExaminationQueue()) {
				System.out.println(p.getName() + Status.EXAMINATIONSQUEUE.getValue());
			}
			for (Patient p : hospital.getInvestigationQueue()) {
				System.out.println(p.getName() + Status.INVESTIGATIONSQUEUE.getValue());
			}
			for (Patient p : hospital.getSentHome()) {
				System.out.println(p.getName() + Status.HOME_SURGEON.getValue());
			}
			for (Patient p : hospital.getSentOutOfHospital()) {
				System.out.println(p.getName() + Status.OTHERHOSPITAL.getValue());
			}*/
			
			
			/*System.out.println("\n");
			System.out.println("EXAMINATION");
			for (Patient p : hospital.getExaminationQueue()) {
				System.out.println("Patient " + p.getName() + " este examinat !!");
			}
			System.out.println("IN TRIAGE QUEUE AU RAMAS :");
			for (Patient p : hospital.getPatientsInTriageQueue()) {
				System.out.println("Patient " + p.getName() + " are severitatea " + p.getState().getSeverity());
			}
			System.out.println("IN EXAMINATION QUEUE AU RAMAS :");
			for (Patient p : hospital.getExaminationQueue()) {
				System.out.println("Patient " + p.getName() + " are severitatea " + p.getState().getSeverity());
			}
			System.out.println("IN INVESTIGATION QUEUE AU RAMAS :");
			for (Patient p : hospital.getInvestigationQueue()) {
				System.out.println("Patient " + p.getName() + " are severitatea " + p.getState().getSeverity());
			}
			hospital.investigatePatients();
			
			System.out.println("IN TRIAGE QUEUE AU RAMAS :");
			for (Patient p : hospital.getPatientsInTriageQueue()) {
				System.out.println("Patient " + p.getName() + " are severitatea " + p.getState().getSeverity() + " " + p.getInvectigationResult().getValue());
			}
			System.out.println("IN EXAMINATION QUEUE AU RAMAS :");
			for (Patient p : hospital.getExaminationQueue()) {
				System.out.println("Patient " + p.getName() + " are severitatea " + p.getState().getSeverity() + " " + p.getInvectigationResult().getValue());
			}
			System.out.println("IN INVESTIGATION QUEUE AU RAMAS :");
			for (Patient p : hospital.getInvestigationQueue()) {
				System.out.println("Patient " + p.getName() + " are severitatea " + p.getState().getSeverity() + " " + p.getInvectigationResult().getValue());
			}*/
			
			
		}
		
	}
	
	public Hospital getHospital() {
		return hospital;
	}
	
	public void setHospital(Hospital hospital) {
		this.hospital = hospital;
	}
	
	
}
