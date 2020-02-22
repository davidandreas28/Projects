package vcs;

import java.util.ArrayList;

public final class Branch {
	private ArrayList<Commit> commitList = null;
	private final String name;
	public Branch(String name) {
		this.name = name;
		commitList = new ArrayList<Commit>();
	}
	public ArrayList<Commit> getCommitList() {
		return commitList;
	}
	public String getName() {
		return name;
	}
}
