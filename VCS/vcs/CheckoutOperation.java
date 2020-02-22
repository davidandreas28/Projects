package vcs;

import java.util.ArrayList;

import utils.ErrorCodeManager;
import utils.OperationType;

public final class CheckoutOperation extends VcsOperation {

	public CheckoutOperation(OperationType type, ArrayList<String> operationArgs) {
		super(type, operationArgs);
		// TODO Auto-generated constructor stub
	}

	@Override
	public int execute(Vcs vcs) {
		// TODO Auto-generated method stub
		/*
		 * If Staging is Empty,
		 * return Error
		 */
		if (!vcs.getStagedChanges().getChanges().isEmpty()) {
			return ErrorCodeManager.VCS_STAGED_OP_CODE;
		}
		/*
		 *  Commit Checkout Case
		 */
		if (this.operationArgs.get(0).equals("-c")) {
			int idToMatch = Integer.parseInt(this.operationArgs.get(1));
			boolean isOk = false;
			/*
			 * If Commit with same ID is Found
			 * delete all following Commits
			 * If Commit with the same ID is not
			 * found, return Error
			 */
			for (Commit it : vcs.getCurrentBranch().getCommitList()) {
				if (it.getID() == idToMatch) {
					isOk = true;
				}
			}
			if (!isOk) {
				return ErrorCodeManager.VCS_BAD_PATH_CODE;
			}
			for (int i = 0; i < vcs.getCurrentBranch().
					getCommitList().size(); ++i) {
				if (vcs.getCurrentBranch().getCommitList().
						get(i).getID() == idToMatch) {
					for (int j = vcs.getCurrentBranch().
							getCommitList().size() - 1;
							j >= i + 1; --j) {
						vcs.getCurrentBranch().getCommitList().remove(j);
					}
				}
				/*
				 * Update CurrentCommit 
				 * Update FileSystem
				 */
				vcs.setCurrentCommit(vcs.
						getCurrentBranch().getCommitList().get(i));
				vcs.setSystemSnapshot(vcs.
						getCurrentBranch().getCommitList().
						get(i).getFileSystemSnapShot().
						cloneFileSystem());
				break;
			}
		} else {
			/*
			 * Branch Checkout
			 */
			boolean isOk = false;
			int index = -1;
			/*
			 * If Branch with the wanted name found
			 * Update CurrentBranch
			 * Else
			 * Return Error
			 */
			for (int i = 0; i < vcs.getBranchList().size(); ++i) {
				if (this.operationArgs.get(0).equals(
						vcs.getBranchList().get(i).getName())) {
					isOk = true;
					index = i;
					break;
				}
			}
			if (!isOk) {
				return ErrorCodeManager.VCS_BAD_CMD_CODE;
			}
			vcs.setCurrentBranch(vcs.
					getBranchList().get(index));
		}
		return 0;
	}

}
