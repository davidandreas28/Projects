package vcs;

import filesystem.FileSystemSnapshot;
import utils.IDGenerator;

public final class Commit {
	private FileSystemSnapshot currentFileSystem;
	private final String name;
	private final int id;
	public Commit(String name, FileSystemSnapshot snap) {
		this.name = name;
		this.id = IDGenerator.generateCommitID();
		currentFileSystem = snap;
	}
	public int getID() {
		return id;
	}
	public String getName() {
		return name;
	}
	public FileSystemSnapshot getFileSystemSnapShot() {
		return this.currentFileSystem;
	}
}
