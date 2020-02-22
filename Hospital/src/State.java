/**
 * All the patient's states based on the queue they are in or its hospitalization status.
 * The string values are needed for printing the homework's output messages.
 *
 * [Part of the homework's skeleton]
 */
enum Status{
	    TRIAGEQUEUE("in triage queue"),
	    EXAMINATIONSQUEUE("in examinations queue"),
	    INVESTIGATIONSQUEUE("in investigations queue"),
	    HOSPITALIZED_CARDIO("hospitalized by cardiologist"),
	    HOSPITALIZED_ERPHYSICIAN("hospitalized by erphysician"),
	    HOSPITALIZED_GASTRO("hospitalized by gastroenterologist"),
	    HOSPITALIZED_SURGEON("hospitalized by general surgeon"),
	    HOSPITALIZED_INTERNIST("hospitalized by internist"),
	    HOSPITALIZED_NEURO("hospitalized by neurologist"),
	    OPERATED_CARDIO("operated by cardiologist"),
	    OPERATED_ERPHYSICIAN("operated by erphysician"),
	    OPERATED_SURGEON("operated by general surgeon"),
	    OPERATED_NEURO("operated by neurologist"),
	    HOME_CARDIO("sent home by cardiologist"),
	    HOME_ERPHYSICIAN("sent home by erphysician"),
	    HOME_GASTRO("sent home by gastroenterologist"),
	    HOME_SURGEON("sent home by general surgeon"),
	    HOME_INTERNIST("sent home by internist"),
	    HOME_NEURO("sent home by neurologist"),
	    HOME_DONE_TREATMENT("sent home after treatment"),
	    OTHERHOSPITAL("transferred to other hospital")
	    ;
	    private String value;
	    
	    Status(String value) {
	    	this.value = value;
	    }
		
	    public String getValue() {
	    	return value;
	    }
	}
public class State{
    private IllnessType illnessName;
    private int severity;
    
    public void setIllnessName(IllnessType name) {
    	this.illnessName = name;
    }
    public IllnessType getIllnessName() {
        return illnessName;
    }

	public int getSeverity() {
		return severity;
	}

	public void setSeverity(int severity) {
		this.severity = severity;
	}
}


