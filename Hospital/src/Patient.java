import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class Patient {
	private int id;
	private String name;
	private int age;
	private int time;
	private State state;
	private InvestigationResult invectigationResult = InvestigationResult.NOT_DIAGNOSED;
	private Urgency urgency;
	
	private int rounds;
	boolean shouldGoHome = false;
	private ArrayList<Doctor> possibleDoctors = new ArrayList<>();
	private Doctor itsDoctor;
	boolean beenOperated = false;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public int getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}
	public State getState() {
		return state;
	}
	public void setState(State state) {
		this.state = state;
	}
	public InvestigationResult getInvectigationResult() {
		return invectigationResult;
	}
	public void setInvestigationResult(InvestigationResult invectigationResult) {
		this.invectigationResult = invectigationResult;
	}
	
	public Urgency getUrgency() {
		return urgency;
	}
	public void setUrgency(Urgency urgency) {
		this.urgency = urgency;
	}
	public ArrayList<Doctor> getPossibleDoctors() {
		return possibleDoctors;
	}
	public void setPossibleDoctors(ArrayList<Doctor> possibleDoctors) {
		this.possibleDoctors = possibleDoctors;
	}
	public Doctor getItsDoctor() {
		return itsDoctor;
	}
	public void setItsDoctor(Doctor itsDoctor) {
		this.itsDoctor = itsDoctor;
	}
	
	public Doctor findItsDoctor(ArrayList<Doctor> source) {
		if (state.getIllnessName() == IllnessType.ABDOMINAL_PAIN) {
			for (Doctor d : source) {
				
				if (d.getType().equals("GASTROENTEROLOGIST")) {
					return 	d;
				} else if (d.getType().equals("GENERAL_SURGEON")) {
					return d;
				} else if (d.getType().equals("INTERNIST")) {
					return d;
				}
			}
		}
		if (state.getIllnessName() == IllnessType.ALLERGIC_REACTION) {
			for (Doctor d : source) {
				if (d.getType().equals("ER_PHYSICIAN")) {
					return d;
				} else if (d.getType().equals("GASTROENTEROLOGIST")) {
					return 	d;
				} else if (d.getType().equals("INTERNIST")) {
					return d;
				}
			}
		}
		if (state.getIllnessName() == IllnessType.BROKEN_BONES) {
			for (Doctor d : source) {
				if (d.getType().equals("ER_PHYSICIAN")) {
					return d;
				}
			}
		}
		if (state.getIllnessName() == IllnessType.BURNS) {
			for (Doctor d : source) {
				if (d.getType().equals("ER_PHYSICIAN")) {
					return d;
				} else if (d.getType().equals("GENERAL_SURGEON")) {
					return d;
				}
			}
		}
		if (state.getIllnessName() == IllnessType.CAR_ACCIDENT) {
			for (Doctor d : source) {
				if (d.getType().equals("ER_PHYSICIAN")) {
					return d;
				} else if (d.getType().equals("GENERAL_SURGEON")) {
					return d;
				}
			}
		}
		if (state.getIllnessName() == IllnessType.CUTS) {
			for (Doctor d : source) {
				if (d.getType().equals("ER_PHYSICIAN")) {
					return d;
				} else if (d.getType().equals("GENERAL_SURGEON")) {
					return d;
				}
			}
		}
		if (state.getIllnessName() == IllnessType.FOOD_POISONING) {
			for (Doctor d : source) {
				if (d.getType().equals("GASTROENTEROLOGIST")) {
					return 	d;
				} else if (d.getType().equals("INTERNIST")) {
					return d;
				}
			}
		}
		if (state.getIllnessName() == IllnessType.HEART_ATTACK) {
			for (Doctor d : source) {
				if (d.getType().equals("CARDIOLOGIST")) {
					return d;	
				} 
			}
		}
		if (state.getIllnessName() == IllnessType.HEART_DISEASE) {
			for (Doctor d : source) {
				if (d.getType().equals("INTERNIST") || d.getType().equals("CARDIOLOGIST")) {
					return d;	
				} 
			}
		}
		if (state.getIllnessName() == IllnessType.HIGH_FEVER) {
			for (Doctor d : source) {
				if (d.getType().equals("ER_PHYSICIAN")) {
					return d;
				} else if (d.getType().equals("INTERNIST")) {
					return d;
				}
			}
		}
		if (state.getIllnessName() == IllnessType.PNEUMONIA) {
			for (Doctor d : source) {
				if (d.getType().equals("INTERNIST")) {
					return d;
				} 
			}
		}
		if (state.getIllnessName() == IllnessType.SPORT_INJURIES) {
			for (Doctor d : source) {
				if (d.getType().equals("ER_PHYSICIAN")) {
					return d;
				} else if (d.getType().equals("GENERAL_SURGEON")) {
					return d;
				}
			}
		}
		if (state.getIllnessName() == IllnessType.STROKE) {
			for (Doctor d : source) {
				if (d.getType().equals("NEUROLOGIST")) {
					return d;
				}
			}
		}	
		return null;		
	}
	public boolean heGoesHome(Doctor doc) {
		if (state.getSeverity() <= doc.getMaxForTreatment())
			return true;
		return false;
	}
	public Doctor findItsSurgeon(ArrayList<Doctor> doctors) {
		for (Doctor d : doctors) {
			if (d.isSurgeon()) {
				if (d.getType().equals("GENERAL_SURGEON")) {
					if (state.getIllnessName() == IllnessType.ABDOMINAL_PAIN ||
							state.getIllnessName() == IllnessType.BURNS ||
							state.getIllnessName() == IllnessType.CAR_ACCIDENT ||
							state.getIllnessName() == IllnessType.CUTS ||
							state.getIllnessName() == IllnessType.SPORT_INJURIES	) {
						return d;
					}
				} else if (d.getType().equals("CARDIOLOGIST")) {
					if (state.getIllnessName() == IllnessType.HEART_ATTACK ||
							state.getIllnessName() == IllnessType.HEART_DISEASE) {
						return d;
					}
				} else if (d.getType().equals("ER_PHYSICIAN")) {
					if (state.getIllnessName() == IllnessType.BROKEN_BONES||
							state.getIllnessName() == IllnessType.BURNS ||
							state.getIllnessName() == IllnessType.CAR_ACCIDENT ||
							state.getIllnessName() == IllnessType.CUTS ||
							state.getIllnessName() == IllnessType.ALLERGIC_REACTION ||
							state.getIllnessName() == IllnessType.HIGH_FEVER ||
							state.getIllnessName() == IllnessType.SPORT_INJURIES) {
						return d;
					}
				} else if (d.getType().equals("NEUROLOGIST")) {
					if (state.getIllnessName() == IllnessType.STROKE) {
						return d;
					}
				}
			}
		}
		return null;
	}
	public int getRounds() {
		return rounds;
	}
	public void setRounds(int rounds) {
		this.rounds = rounds;
	}
	public void decreaseRounds() {
		rounds--;
	}
	public void isBeingOperatedOn(double abs) {
		
		
		
		state.setSeverity((int)Math.round(state.getSeverity() - abs));
	}
	public void getTreatment() {
		this.decreaseRounds();
		state.setSeverity(state.getSeverity() - Doctor.TREATMENT);
	}
	public void getPrescription() {
		double factorOp = getItsDoctor().getOperationFactor();
		double factorCo = getItsDoctor().getConsultFactor();
		if (getName().equals("Robinson Butler")){
			System.out.println(getName() + "E LA MEDICUL " + getItsDoctor().getType() +  " ARE  " + factorOp + " " + factorCo);
		}
		int partialAnsw = (int)Math.round(getState().getSeverity() * factorCo);
		setRounds(Math.max(partialAnsw, 3));
		if (getName().equals("Robinson Butler")){
			System.out.println(getName() + "ARE RUNDE IN NUMAR DE " + getRounds());
		}
		setInvestigationResult(InvestigationResult.TREATMENT);
	}
	public boolean isBeenOperated() {
		return beenOperated;
	}
	public void setBeenOperated(boolean beenOperated) {
		this.beenOperated = beenOperated;
	}
	public boolean isShouldGoHome() {
		return shouldGoHome;
	}
	public void setShouldGoHome(boolean shouldGoHome) {
		this.shouldGoHome = shouldGoHome;
	}
}
