package main;

final class FreqAssetElement {
	private String type;
	private int frequency;
	private int profits;
	private int legal;

	FreqAssetElement(final Asset asset) {
		this.type = asset.getNameAsset();
		this.frequency = 1;
		this.profits = asset.getProfit();
		if (asset.getType().equals("Legal")) {
			this.legal = 1;
		} else {
			this.legal = 0;
		}
	}

	protected String getType() {
		return this.type;
	}

	protected void copyFromAsset(final Asset asset) {
		this.frequency += 1;
		this.profits += asset.getProfit();
	}

	protected int getFrequency() {
		return this.frequency;
	}

	protected int getProfits() {
		return this.profits;
	}

	protected int getLegal() {
		return this.legal;
	}
}
