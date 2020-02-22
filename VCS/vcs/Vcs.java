package vcs;

import java.util.ArrayList;

import filesystem.FileSystemOperation;
import filesystem.FileSystemSnapshot;
import utils.OutputWriter;
import utils.Visitor;

public final class Vcs implements Visitor {
    private final OutputWriter outputWriter;
    private FileSystemSnapshot activeSnapshot;
    private StagedChanges stagedChanges;
    private ArrayList<Branch> branchList;
    private Branch currentBranch;
    private Commit currentCommit;
    /**
     * Vcs constructor.
     *
     * @param outputWriter the output writer
     */
    public Vcs(OutputWriter outputWriter) {
        this.outputWriter = outputWriter;
    }
    public StagedChanges getStagedChanges() {
		return stagedChanges;
	}
    public void setSystemSnapshot(FileSystemSnapshot snap) {
    	this.activeSnapshot = snap;
    }
    public void setCurrentCommit(Commit currentCommit) {
    	this.currentCommit = currentCommit;
    }
    public void setCurrentBranch(Branch currentBranch) {
    	this.currentBranch = currentBranch;
    }
    public ArrayList<Branch> getBranchList() {
    	return this.branchList;
    }
    public FileSystemSnapshot getActiveSnapshot() {
    	return this.activeSnapshot;
    }
    public Branch getCurrentBranch() {
    	return this.currentBranch;
    }
    public Commit getCurrentCommit() {
    	return this.currentCommit;
    }
    /**
     * Does initialisations.
     */
    public void init() {
        this.activeSnapshot = new FileSystemSnapshot(outputWriter);
        //TODO other initialisations
        // Create Object of Type StagedChanges
        stagedChanges = StagedChanges.getInstance();
        // Init Brench List
        branchList = new ArrayList<Branch>();
        // Add Master Brench
        branchList.add(new Branch("master"));
        // Update Current Brench
        currentBranch = branchList.get(0);
        // Add First Commit (ID = 3)
        branchList.get(0).getCommitList().
        add(new Commit("First commit",
        		this.activeSnapshot.cloneFileSystem()));
        // Update Current Commit
        currentCommit = branchList.get(0).getCommitList().get(0);
    }

    /**
     * Visits a file system operation.
     *
     * @param fileSystemOperation the file system operation
     * @return the return code
     */
    public int visit(FileSystemOperation fileSystemOperation) {
        return fileSystemOperation.execute(this.activeSnapshot);
    }

    /**
     * Visits a vcs operation.
     *
     * @param vcsOperation the vcs operation
     * @return return code
     */
    @Override
    public int visit(VcsOperation vcsOperation) {
        //TODO
        return vcsOperation.execute(this);
    }
    //TODO methods through which vcs operations interact with this
}
