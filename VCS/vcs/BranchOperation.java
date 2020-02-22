package vcs;

import java.util.ArrayList;

import utils.ErrorCodeManager;
import utils.OperationType;

public final class BranchOperation extends VcsOperation {

	public BranchOperation(OperationType type, ArrayList<String> operationArgs) {
		super(type, operationArgs);
		// TODO Auto-generated constructor stub
	}

	@Override
	public int execute(Vcs vcs) {
		// TODO Auto-generated method stub
		/* if Branch with the same ID is found
		 * return Error;
		 * Otherwise, add new Branch in 
		 * List; its name is passed
		 * through the operationArguments;
		 */
		for (Branch v : vcs.getBranchList()) {
			if (v.getName().equals(this.operationArgs.get(0))) {
				return ErrorCodeManager.VCS_BAD_CMD_CODE;
			}
		}
		vcs.getBranchList().add(new Branch(this.operationArgs.get(0)));
		return 0;
	}

}
