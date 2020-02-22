
public enum ConsultFactor {
	CARDIOLOGIST(0.4),
	ER_PHYSICIAN (0.1),
	GASTROENTEROLOGIST(0.5),
	GENERAL_SURGEON(0.2),
	INTERNIST(0.01),
	NEUROLOGIST(0.5);
	private double value;
	private ConsultFactor(double value) {
		this.value = value;
	}
	public double getValue() {
		return value;
	}
}
