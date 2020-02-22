/**
 * IMMEDIATE > URGENT > LESS_URGENT > NON_URGENT
 * NON_URGENT means it will not enter the emergency flows
 *
 * [Part of the homework's skeleton]
 */
public enum Urgency {
    IMMEDIATE(3),
    URGENT(2),
    LESS_URGENT(1),
    NON_URGENT(0),
    NOT_DETERMINED(-1);
	private int value;
	Urgency(int value) {
		this.value = value;
	}
	public int getValue() {
		return this.value;
	}
	
	
}
