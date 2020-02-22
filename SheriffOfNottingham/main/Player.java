package main;

import java.util.ArrayList;

public abstract class Player {
	private String typeOfPlayer;
	private int coins;
	private int sheriffModeOn;
	private int round;
	private int beenSheriff;
	private String declaredAsset = null;
	private ArrayList<Asset> assetsInHand;
	private ArrayList<Asset> assetsInSack;
	private ArrayList<Asset> assetsOnMerchantStand;
	private int bribe;
	private int legalCards;

	protected Player(final String name) {
		this.coins = Constants.COINS;
		this.typeOfPlayer = name;
		this.sheriffModeOn = 0;
		this.beenSheriff = 0;
		this.round = 1;
		this.assetsInHand = new ArrayList<Asset>(Constants.ARRAY_LENGTH);
		this.assetsInSack = new ArrayList<Asset>(Constants.SACK_LENGTH);
		this.bribe = 0;
		this.assetsOnMerchantStand = new ArrayList<Asset>();
		this.declaredAsset = new String();
		legalCards = 0;
	}

	final void getCards(final Assets tableAssets) {
		if (!tableAssets.getAssets().isEmpty()) {
			if (assetsInHand.size() < Constants.ARRAY_LENGTH) {
				while (assetsInHand.size() != Constants.ARRAY_LENGTH) {
					Asset aux = tableAssets.getAssets().remove(0);
					assetsInHand.add(aux);
				}
				return;
			}
		} else {

			return;
		}
	}

	final ArrayList<Asset> getAssetsInHand() {
		return assetsInHand;
	}

	final int getCoins() {
		return this.coins;
	}

	final int getSheriffModeOn() {
		return this.sheriffModeOn;
	}

	final String getTypeOfPlayer() {
		return this.typeOfPlayer;
	}

	final int getBeenSheriff() {
		return this.beenSheriff;
	}

	final void setDeclaredAsset(String declaredAsset) {
		this.declaredAsset = declaredAsset;
	}

	final ArrayList<Asset> getAssetsInSack() {
		return this.assetsInSack;
	}

	final int getBribe() {
		return this.bribe;
	}

	final boolean thereAreLegalItemsInHand() {
		for (int i = 0; i < this.assetsInHand.size(); ++i) {
			if (this.assetsInHand.get(i).getType().equals("Legal")) {
				return true;
			}
			continue;
		}
		return false;
	}

	final String getDeclaredAsset() {
		return this.declaredAsset;
	}

	final void addToCoins(final int value) {
		this.coins += value;
	}

	final boolean lieCheck() {
		for (int i = 0; i < this.getAssetsInSack().size(); ++i) {
			if (!this.getAssetsInSack().get(i).
					getNameAsset().equals(this.declaredAsset)) {
				return false;
			}
		}
		return true;
	}

	final int countAssets() {
		return assetsInSack.size();
	}

	final ArrayList<Asset> getAssetsOnMerchantStand() {
		return this.assetsOnMerchantStand;
	}

	final void payToSheriff(final Asset asset) {
		this.coins -= asset.getPenalty();

	}

	final void removeBribe() {
		this.bribe = 0;
	}

	final void transferToAssetsOnMerchantStand(final ArrayList<Asset> merchantStand) {
		while (!assetsInSack.isEmpty()) {
			merchantStand.add(assetsInSack.remove(0));
		}
	}

	final void makeThisSheriff() {
		this.sheriffModeOn = 1;
	}

	final void removeSheriffAtributte() {
		this.beenSheriff += 1;
	}

	final void newRound() {
		this.round += 1;
	}

	final int getRound() {
		return this.round;
	}

	final boolean thereAreIllegalCards() {
		for (int i = 0; i < assetsInHand.size(); ++i) {
			if (assetsInHand.get(i).getType().equals("Illegal")) {
				return true;
			}
		}
		return false;
	}

	final void emptyTheSack() {
		while (!this.getAssetsInSack().isEmpty()) {
			this.getAssetsInSack().remove(0);
		}
	}

	final void payPenalty(final int sum) {
		this.coins -= sum;
	}

	final int howManyIllegalCards() {
		int counter = 0;
		for (int i = 0; i < assetsInHand.size(); i++) {
			if (assetsInHand.get(i).getType().equals("Illegal")) {
				counter++;
			}
		}
		return counter;
	}

	final void setBribe(final int value) {
		this.coins -= value;
		this.bribe = value;
	}

	final void setLegalCards(final int value) {
		this.legalCards = value;
	}

	final int getLegalCards() {
		return this.legalCards;
	}

	abstract void createSackAndDeclaration();

	abstract void inspection(Player player, Assets assetTable);

}
