package vcs;

import java.util.ArrayList;

import utils.ErrorCodeManager;
import utils.OperationType;

/*
 * USEFUL IN 'vcs master' CASE
 * >>> SEE INPUT TEST 6
 */

public final class InvalidVcsOperation extends VcsOperation {

	public InvalidVcsOperation(OperationType type, ArrayList<String> operationArgs) {
		super(type, operationArgs);
		// TODO Auto-generated constructor stub
	}

	@Override
	public int execute(Vcs vcs) {
		// TODO Auto-generated method stub
		return ErrorCodeManager.VCS_BAD_CMD_CODE;
	}

}
