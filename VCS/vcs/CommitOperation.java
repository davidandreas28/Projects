package vcs;

import java.util.ArrayList;

import utils.ErrorCodeManager;
import utils.OperationType;

public final class CommitOperation extends VcsOperation {

	public CommitOperation(OperationType type, ArrayList<String> operationArgs) {
		super(type, operationArgs);
		// TODO Auto-generated constructor stub
	}

	@Override
	public int execute(Vcs vcs) {
		// TODO Auto-generated method stub
		/*
		 * If Staging is Empty
		 * Return Error
		 */
		if (vcs.getStagedChanges().getChanges().isEmpty()) {
			return ErrorCodeManager.VCS_BAD_CMD_CODE;
		}
		/*
		 * Add new Commit
		 * Clear Staging
		 * Update CurrentCommit
		 */
		vcs.getCurrentBranch().getCommitList().
		add(new Commit(this.operationArgs.get(1),
				vcs.getActiveSnapshot().cloneFileSystem()));
		vcs.getStagedChanges().getChanges().clear();
		vcs.setCurrentCommit(vcs.getCurrentBranch().
		getCommitList().get(vcs.getCurrentBranch().
		getCommitList().size() - 1));
		return 0;
	}

}
