package vcs;

import java.util.ArrayList;
import utils.OperationType;

public final class StatusOperation extends VcsOperation {

	public StatusOperation(OperationType type, ArrayList<String> operationArgs) {
		super(type, operationArgs);
		// TODO Auto-generated constructor stub
	}

	@Override
	public int execute(Vcs vcs) {
		// TODO Auto-generated method stub
		/*
		 * Write in file using this template
		 */
        vcs.getActiveSnapshot().getOutputWriter().write(
        		"On branch: " + vcs.getCurrentBranch().
        		getName() + "\n");
        vcs.getActiveSnapshot().getOutputWriter().write(
        		"Staged changes:\n");
        for (String it : vcs.getStagedChanges().getChanges()) {
        	vcs.getActiveSnapshot().getOutputWriter().write(
        			"\t" + it + "\n");
        }
        return 0;
	}
}
