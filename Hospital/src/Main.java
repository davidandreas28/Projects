
public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String path = args[0];
		ERSimulator.getInstance().readValues(path);
		ERSimulator.getInstance().beginSimulation();
	}

}
