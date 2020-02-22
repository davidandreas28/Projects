package main;

import java.util.ArrayList;

final class Bribed extends Player {
	private FreqTable tableOfFreq;

	protected Bribed(final String name) {
		super(name.toUpperCase());
		tableOfFreq = new FreqTable();
	}

	void createSackAndDeclaration() {
		this.tableOfFreq.emptyFreqVector();
		if (this.getCoins() < Constants.SACK_LENGTH) {
			if (this.thereAreLegalItemsInHand()) {
				for (int i = 0; i < this.getAssetsInHand().size(); ++i) {
					if (this.getAssetsInHand().get(i).getType().equals("Legal")) {
						if (tableOfFreq.findInFreqTable(
							this.getAssetsInHand().get(i))) {
								tableOfFreq.getFreqTable()
								.get(tableOfFreq.getPosOfDuplicate(
								this.getAssetsInHand().get(i)))
								.copyFromAsset(
								this.getAssetsInHand().get(i));
						} else {
							tableOfFreq.putInFreqTable(
							this.getAssetsInHand().get(i));
						}
					}
				}

				int duplicates = 1;
				int max = 0;
				ArrayList<String> sameElements = new ArrayList<String>();
				for (int i = 0; i < this.tableOfFreq.getFreqTable().size(); ++i) {
					if (max < this.tableOfFreq.
							getFreqTable().
							get(i).getFrequency()) {
						while (!(sameElements.isEmpty())) {
							sameElements.remove(0);
						}
						max = this.tableOfFreq.getFreqTable().
								get(i).getFrequency();
						duplicates = 1;
						sameElements.add(this.tableOfFreq.
								getFreqTable().get(i).getType());
					} else if (max == this.tableOfFreq.
							getFreqTable().get(i).getFrequency()) {
						duplicates++;
						sameElements.add(this.tableOfFreq.
								getFreqTable().get(i).getType());
					}
				}

				if (duplicates == 1) {
					for (int i = 0; i < this.getAssetsInHand().size(); ++i) {
						if (this.getAssetsInHand().get(i).
								getNameAsset().equals(
								sameElements.get(0))) {
							Asset aux = this.getAssetsInHand().
									remove(i);
							this.getAssetsInSack().add(aux);
							i--;
						}
					}
					this.setDeclaredAsset(sameElements.get(0));
				} else {
					int[] profitsVec = new int[sameElements.size()];
					for (int i = 0; i < sameElements.size(); ++i) {
						profitsVec[i] = 0;
					}
					for (int i = 0; i < sameElements.size(); ++i) {
						for (int j = 0; j < this.tableOfFreq.
								getFreqTable().size(); ++j) {
							if (this.tableOfFreq.getFreqTable().
							  get(j).getType().
							  equals(sameElements.get(i))) {
								profitsVec[i] += this.tableOfFreq.
								getFreqTable().get(j).getProfits();
							}
						}
					}
					int maxProfits = 0;
					int maxProfitsIndex = -1;
					for (int i = 0; i < duplicates; ++i) {
						if (maxProfits < profitsVec[i]) {
							maxProfits = profitsVec[i];
							maxProfitsIndex = i;
						}
						continue;
					}
					for (int i = 0; i < this.getAssetsInHand().size(); ++i) {
						if (this.getAssetsInHand().get(i).getNameAsset()
								.equals(sameElements.
								get(maxProfitsIndex))) {
							Asset aux = this.
									getAssetsInHand().remove(i);
							this.getAssetsInSack().add(aux);
							i--;
						}
					}
					this.setDeclaredAsset(sameElements.get(maxProfitsIndex));
				}
			} else {
				int maxProfits = 0;
				int maxProfitsIndex = -1;
				for (int i = 0; i < this.getAssetsInHand().size(); ++i) {
					if (maxProfits < this.getAssetsInHand().
							get(i).getProfit()) {
						maxProfits = this.getAssetsInHand().
								get(i).getProfit();
						maxProfitsIndex = i;
					}
				}
				Asset aux = this.getAssetsInHand().remove(maxProfitsIndex);
				this.getAssetsInSack().add(aux);
				this.setDeclaredAsset("Apple");
			}
			this.newRound();
		} else if (!this.thereAreIllegalCards()) {
			for (int i = 0; i < this.getAssetsInHand().size(); ++i) {
				if (tableOfFreq.findInFreqTable(this.getAssetsInHand().get(i))) {
					tableOfFreq.getFreqTable().get(tableOfFreq.
							getPosOfDuplicate(
							this.getAssetsInHand().get(i)))
							.copyFromAsset(this.
							getAssetsInHand().get(i));
				} else {
					tableOfFreq.putInFreqTable(this.getAssetsInHand().get(i));
				}

			}

			int duplicates = 1;
			int max = 0;
			ArrayList<String> sameElements = new ArrayList<String>();
			for (int i = 0; i < this.tableOfFreq.getFreqTable().size(); ++i) {
				if (max < this.tableOfFreq.getFreqTable().get(i).getFrequency()) {
					while (!(sameElements.isEmpty())) {
						sameElements.remove(0);
					}
					max = this.tableOfFreq.getFreqTable().get(i).getFrequency();
					duplicates = 1;
					sameElements.add(this.tableOfFreq.
							getFreqTable().get(i).getType());
				} else if (max == this.tableOfFreq.
						getFreqTable().get(i).getFrequency()) {
					duplicates++;
					sameElements.add(this.tableOfFreq.
							getFreqTable().get(i).getType());
				}
			}
			if (duplicates == 1) {
				for (int i = 0; i < this.getAssetsInHand().size(); ++i) {
					if (this.getAssetsInHand().get(i).
							getNameAsset().equals(
							sameElements.get(0))) {
						Asset aux = this.getAssetsInHand().remove(i);
						this.getAssetsInSack().add(aux);
						i--;
					}
				}
				this.setDeclaredAsset(sameElements.get(0));
			} else {
				int[] profitsVec = new int[sameElements.size()];
				for (int i = 0; i < sameElements.size(); ++i) {
					profitsVec[i] = 0;
				}
				for (int i = 0; i < sameElements.size(); ++i) {
					for (int j = 0; j < this.tableOfFreq.
							getFreqTable().size(); ++j) {
						if (this.tableOfFreq.getFreqTable().
								get(j).getType().
								equals(sameElements.get(i))) {
							profitsVec[i] += this.tableOfFreq.
									getFreqTable().get(j).
									getProfits();
						}
					}
				}
				int maxProfits = 0;
				int maxProfitsIndex = -1;
				for (int i = 0; i < duplicates; ++i) {
					if (maxProfits < profitsVec[i]) {
						maxProfits = profitsVec[i];
						maxProfitsIndex = i;
					}
					continue;
				}
				for (int i = 0; i < this.getAssetsInHand().size(); ++i) {
					if (this.getAssetsInHand().get(i).
							getNameAsset().equals(
							sameElements.get(maxProfitsIndex))) {
						Asset aux = this.getAssetsInHand().remove(i);
						this.getAssetsInSack().add(aux);
						i--;
					}
				}
				this.setDeclaredAsset(sameElements.get(maxProfitsIndex));
			}
		} else {
			if (this.howManyIllegalCards() > 2 && this.getCoins() >= Constants.PRICE1) {
				for (int i = 0; i < this.getAssetsInHand().size(); ++i) {
					if (this.getAssetsInHand().
							get(i).getType().equals("Illegal")) {
						Asset aux = this.getAssetsInHand().remove(i);
						this.getAssetsInSack().add(aux);
						i--;
					}
				}
				this.setBribe(Constants.PRICE1);
				this.setDeclaredAsset("Apple");

			} else if (this.howManyIllegalCards() > 2
					&& (this.getCoins() < Constants.PRICE1
					&& this.getCoins() >= Constants.PRICE2)) {
				for (int i = 0; i < this.getAssetsInHand().size(); ++i) {
					int counter = 0;
					if (this.getAssetsInHand().get(i).
							getType().equals("Illegal")) {
						counter++;
						Asset aux = this.getAssetsInHand().remove(i);
						this.getAssetsInSack().add(aux);
						i--;
						if (counter > 2) {
							break;
						}
					}
				}
				this.setBribe(Constants.PRICE2);
				this.setDeclaredAsset("Apple");
			} else if (this.howManyIllegalCards() <= 2) {
				for (int i = 0; i < this.getAssetsInHand().size(); ++i) {
					if (this.getAssetsInHand().get(i).
							getType().equals("Illegal")) {
						Asset aux = this.getAssetsInHand().remove(i);
						this.getAssetsInSack().add(aux);
						i--;
					}
				}
				this.setBribe(Constants.PRICE2);
				this.setDeclaredAsset("Apple");

			}
		}
		this.newRound();

	}

	@Override
	void inspection(final Player player, final Assets assetTable) {
		// TODO Auto-generated method stub
		inspectSack(player, assetTable);
	}

	void inspectSack(final Player player, final Assets assetTable) {
		if (player.getBribe() != 0) {
			this.addToCoins(player.getBribe());
			player.removeBribe();
		}
		if (player.lieCheck()) {
			int sum = player.countAssets() * player.
					getAssetsInSack().get(0).getPenalty();
			player.addToCoins(sum);
			this.payPenalty(sum);
			player.transferToAssetsOnMerchantStand(player.getAssetsOnMerchantStand());
		} else {
			for (int i = 0; i < player.getAssetsInSack().size(); ++i) {
				if (!(player.getAssetsInSack().get(i).getNameAsset().
						equals(player.getDeclaredAsset()))) {
					player.payToSheriff(player.getAssetsInSack().get(i));
					this.addToCoins(player.getAssetsInSack().
							get(i).getPenalty());
					Asset confiscatedAsset = player.getAssetsInSack().get(i);
					assetTable.getAssets().add(player.
							getAssetsInSack().remove(i));
					i--;
					// player.get__AssetsInSack().remove(i);

				}
			}
			player.transferToAssetsOnMerchantStand(player.getAssetsOnMerchantStand());
		}
	}

	protected void actLikeASheriff() {
	}
}
