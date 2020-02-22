package main;

final class Asset {
	private int identificationNumber;
	private String nameAsset;
	private String type;
	private int profit;
	private int penalty;
	private int bonus;

	protected Asset(final int identificationNumber) {
		if (identificationNumber == 0) {
			this.identificationNumber = identificationNumber;
			this.nameAsset = Constants.APPLE_NAME;
			this.type = Constants.LEGAL;
			this.profit = Constants.APPLE_PROFIT;
			this.penalty = Constants.APPLE_PENALTY;
			this.bonus = Constants.APPLE_BONUS;
		} else if (identificationNumber == 1) {
			this.identificationNumber = identificationNumber;
			this.nameAsset = Constants.CHEESE_NAME;
			this.type = Constants.LEGAL;
			this.profit = Constants.CHEESE_PROFIT;
			this.penalty = Constants.CHEESE_PENALTY;
			this.bonus = Constants.CHEESE_BONUS;
		} else if (identificationNumber == 2) {
			this.identificationNumber = identificationNumber;
			this.nameAsset = Constants.BREAD_NAME;
			this.type = Constants.LEGAL;
			this.profit = Constants.BREAD_PROFIT;
			this.penalty = Constants.BREAD_PENALTY;
			this.bonus = Constants.BREAD_BONUS;
		} else if (identificationNumber == Constants.CONSTANT_3) {
			this.identificationNumber = identificationNumber;
			this.nameAsset = Constants.CHICKEN_NAME;
			this.type = Constants.LEGAL;
			this.profit = Constants.CHICKEN_PROFIT;
			this.penalty = Constants.CHICKEN_PENALTY;
			this.bonus = Constants.CHICKEN_BONUS;
		} else if (identificationNumber == Constants.CONSTANT_10) {
			this.identificationNumber = identificationNumber;
			this.nameAsset = Constants.SILK_NAME;
			this.type = Constants.ILLEGAL;
			this.profit = Constants.SILK_PROFIT;
			this.penalty = Constants.SILK_PENALTY;
			this.bonus = Constants.SILK_BONUS;
		} else if (identificationNumber == Constants.CONSTANT_11) {
			this.identificationNumber = identificationNumber;
			this.nameAsset = Constants.PEPPER_NAME;
			this.type = Constants.ILLEGAL;
			this.profit = Constants.PEPPER_PROFIT;
			this.penalty = Constants.PEPPER_PENALTY;
			this.bonus = Constants.PEPPER_BONUS;
		} else {
			this.identificationNumber = identificationNumber;
			this.nameAsset = Constants.BARREL_NAME;
			this.type = Constants.ILLEGAL;
			this.profit = Constants.BARREL_PROFIT;
			this.penalty = Constants.BARREL_PENALTY;
			this.bonus = Constants.BARREL_BONUS;
		}
	}

	protected Asset() {

	}

	String getNameAsset() {
		return this.nameAsset;
	}

	String getType() {
		return this.type;
	}

	int getProfit() {
		return this.profit;
	}

	int getPenalty() {
		return this.penalty;
	}

	int getBonus() {
		return this.bonus;
	}

}
