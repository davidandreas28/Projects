
public enum OperationFactor {
	CARDIOLOGIST(0.1),
	ER_PHYSICIAN (0.3),
	GENERAL_SURGEON(0.2),
	NEUROLOGIST(0.1);
	private double value;
	private OperationFactor(double value) {
		this.value = value;
	}
	public double getValue() {
		return value;
	}
}
