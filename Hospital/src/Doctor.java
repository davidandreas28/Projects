
import java.util.ArrayList;

public class Doctor {
	private String type;
	private boolean isSurgeon;
	private int maxForTreatment;
	private int ID;
	private ArrayList<Patient> itsPatients = new ArrayList<>();
	
	static int TREATMENT = 22;
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public boolean isSurgeon() {
		return isSurgeon;
	}
	public void setIsSurgeon(boolean isSurgeon) {
		this.isSurgeon = isSurgeon;
	}
	public int getMaxForTreatment() {
		return maxForTreatment;
	}
	public void setMaxForTreatment(int maxForTreatment) {
		this.maxForTreatment = maxForTreatment;
	}
	
	public double getConsultFactor() {
		if (this.type.equals("CARDIOLOGIST")) {
			return ConsultFactor.CARDIOLOGIST.getValue();	
		} else if (this.type.equals("ER_PHYSICIAN")) {
			return ConsultFactor.ER_PHYSICIAN.getValue();
		} else if (this.type.equals("GASTROENTEROLOGIST")) {
			return 	ConsultFactor.GASTROENTEROLOGIST.getValue();
		} else if (this.type.equals("GENERAL_SURGEON")) {
			return ConsultFactor.GENERAL_SURGEON.getValue();
		} else if (this.type.equals("INTERNIST")) {
			return ConsultFactor.INTERNIST.getValue();
		} else if (this.type.equals("NEUROLOGIST")) {
			return ConsultFactor.NEUROLOGIST.getValue();
		}
		return 0;
	}
	
	public double getOperationFactor() {
		if (this.type.equals("CARDIOLOGIST")) {
			return OperationFactor.CARDIOLOGIST.getValue();	
		} else if (this.type.equals("ER_PHYSICIAN")) {
			return OperationFactor.ER_PHYSICIAN.getValue();
		} else if (this.type.equals("GENERAL_SURGEON")) {
			return OperationFactor.GENERAL_SURGEON.getValue();
		} else if (this.type.equals("NEUROLOGIST")) {
			return OperationFactor.NEUROLOGIST.getValue();
		}
		return 0;
	}
	
	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
	}
	public ArrayList<Patient> getItsPatients() {
		return itsPatients;
	}
	public void setItsPatients(ArrayList<Patient> itsPatients) {
		this.itsPatients = itsPatients;
	}
	public void operates(Patient p) {
		p.setItsDoctor(this);
		p.setBeenOperated(true);
		this.itsPatients.add(p);
		double factorOp = this.getOperationFactor();
		double factorCo = this.getConsultFactor();
		
		p.isBeingOperatedOn(Math.round(p.getState().getSeverity() * factorOp));
		int partialAnsw = (int)Math.round(p.getState().getSeverity() * factorCo);
		p.setRounds(Math.max(partialAnsw, 3));
		p.getTreatment();
		p.setInvestigationResult(InvestigationResult.TREATMENT);
		
	}
}
