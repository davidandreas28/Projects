package main;

import java.util.ArrayList;

final class FreqTable {
	private ArrayList<FreqAssetElement> freqTable;

	protected FreqTable() {
		this.freqTable = new ArrayList<FreqAssetElement>();
	}

	protected boolean findInFreqTable(final Asset asset) {
		for (int i = 0; i < this.freqTable.size(); ++i) {
			if (this.freqTable.get(i).getType().equals(asset.getNameAsset())) {
				return true;
			}
			continue;
		}
		return false;
	}

	protected ArrayList<FreqAssetElement> getFreqTable() {
		return this.freqTable;
	}

	protected int getPosOfDuplicate(final Asset asset) {
		for (int i = 0; i < this.freqTable.size(); ++i) {
			if (this.freqTable.get(i).getType().equals(asset.getNameAsset())) {
				return i;
			}
		}
		return 0;
	}

	protected void putInFreqTable(final Asset asset) {
		this.freqTable.add(new FreqAssetElement(asset));
	}

	protected void emptyFreqVector() {
		while (!freqTable.isEmpty()) {
			freqTable.remove(0);
		}
	}

	protected void deleteTable() {
		while (this.freqTable.size() != 0) {
			this.freqTable.remove(0);
		}
	}

}
