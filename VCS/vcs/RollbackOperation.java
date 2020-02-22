package vcs;

import java.util.ArrayList;
import utils.OperationType;

public final class RollbackOperation extends VcsOperation {

	public RollbackOperation(OperationType type, ArrayList<String> operationArgs) {
		super(type, operationArgs);
		// TODO Auto-generated constructor stub
	}

	@Override
	public int execute(Vcs vcs) {
		// TODO Auto-generated method stub
		/*
		 * Clear Staging
		 * Update FileSystem
		 */
		vcs.getStagedChanges().getChanges().clear();
		vcs.setSystemSnapshot(vcs.getCurrentCommit().
		getFileSystemSnapShot().cloneFileSystem());
		return 0;
	}

}
