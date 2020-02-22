package vcs;

import java.util.ArrayList;

import utils.OperationType;

public final class LogOperation extends VcsOperation {

	public LogOperation(OperationType type, ArrayList<String> operationArgs) {
		super(type, operationArgs);
		// TODO Auto-generated constructor stub
	}

	@Override
	public int execute(Vcs vcs) {
		// TODO Auto-generated method stub
		int counter = 0;
		for (Commit it : vcs.getCurrentBranch().getCommitList()) {
			vcs.getActiveSnapshot().getOutputWriter().
			write("Commit id: " + it.getID() + "\n");
			vcs.getActiveSnapshot().getOutputWriter().
			write("Message: " + it.getName() + "\n");
			counter++;
			if (counter != vcs.getCurrentBranch().getCommitList().size()) {
				vcs.getActiveSnapshot().getOutputWriter().write("\n");
			}
		}
		return 0;
	}

}
