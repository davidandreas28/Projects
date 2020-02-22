package main;

import java.util.ArrayList;


final class Assets {
	private ArrayList<Asset> assets;
	protected Assets(final int n) {
		assets = new ArrayList<Asset>(n);
	}

	protected Assets() {
		assets = new ArrayList<Asset>(Constants.ARRAY_LENGTH);
	}

	protected ArrayList<Asset> getAssets() {
		return this.assets;
	}

}
