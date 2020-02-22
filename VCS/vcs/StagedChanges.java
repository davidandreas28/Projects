package vcs;

import java.util.ArrayList;

public final class StagedChanges {
	private ArrayList<String> changes;
	private static StagedChanges instance = null;

	public StagedChanges() {
		changes = new ArrayList<String>();
	}

	public static StagedChanges getInstance() {
		if (instance == null) {
			instance = new StagedChanges();
		}
		return instance;
	}

	public ArrayList<String> getChanges() {
		return changes;
	}

}
