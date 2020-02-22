package main;

import java.util.ArrayList;
import java.util.Collections;

final class LetTheGameBegin {
	private int pos;
	private Assets assetsTable;
	private ArrayList<Player> players;

	protected LetTheGameBegin(final GameInput gameinput) {
		pos = 0;
		assetsTable = new Assets(gameinput.getAssetIds().size());
		for (int i = 0; i < gameinput.getAssetIds().size(); ++i) {
			assetsTable.getAssets().add(new Asset(gameinput.getAssetIds().get(i)));
		}
		players = new ArrayList<Player>(gameinput.getPlayerNames().size());

		for (int i = 0; i < gameinput.getPlayerNames().size(); ++i) {
			if (gameinput.getPlayerNames().get(i).equals("bribed")) {
				Bribed auxBribed = new Bribed(gameinput.getPlayerNames().get(i));
				players.add(auxBribed);
				continue;
			} else if (gameinput.getPlayerNames().get(i).equals("greedy")) {
				Greedy auxGreedy = new Greedy(gameinput.getPlayerNames().get(i));
				players.add(auxGreedy);
				continue;
			} else if (gameinput.getPlayerNames().get(i).equals("basic")) {
				Basic auxBasic = new Basic(gameinput.getPlayerNames().get(i));
				players.add(auxBasic);
				continue;
			}
		}
	}

	protected void transfIntoCoins() {
		for (int i = 0; i < players.size(); ++i) {
			int initSize = players.get(i).getAssetsOnMerchantStand().size();
			for (int j = 0; j < initSize; ++j) {
				players.get(i).addToCoins(players.get(i).getAssetsOnMerchantStand().
						get(j).getProfit() + players.get(i).
						getAssetsOnMerchantStand().
						get(j).getBonus());
				if (players.get(i).getAssetsOnMerchantStand().
						get(j).getNameAsset().equals("Silk")) {
					for (int k = 0; k < Constants.CONSTANT_3; ++k) {
						players.get(i).getAssetsOnMerchantStand().
						add(new Asset(Constants.CONSTANT_1));
					}
				} else if (players.get(i).getAssetsOnMerchantStand().
						get(j).getNameAsset().equals("Pepper")) {
					for (int k = 0; k < Constants.CONSTANT_2; ++k) {
						players.get(i).getAssetsOnMerchantStand().
						add(new Asset(Constants.CONSTANT_3));
					}
				} else if (players.get(i).getAssetsOnMerchantStand().
						get(j).getNameAsset().equals("Barrel")) {
					for (int k = 0; k < Constants.CONSTANT_2; ++k) {
						players.get(i).getAssetsOnMerchantStand().
						add(new Asset(Constants.CONSTANT_2));
					}
				}
			}
		}
	}

	protected void startGame() {
		int finalIndex = this.players.size() - 1;
		while (players.get(finalIndex).getBeenSheriff() < 2) {
			for (int i = 0; i < players.size(); ++i) {
				players.get(i).getCards(assetsTable);
			}
			for (int i = 0; i < players.size(); ++i) {
				if (i != pos) {
					players.get(i).createSackAndDeclaration();
				}
			}
			for (int i = 0; i < players.size(); ++i) {
				if (i != pos) {
					players.get(pos).inspection(players.get(i), assetsTable);
				}
			}
			players.get(pos).removeSheriffAtributte();
			pos++;
			if (pos == players.size()) {
				pos = 0;
			}
		}
	}

	protected void giveTheBonus() {
		// PENTRU APPLE
		for (int i = 0; i < players.size(); ++i) {
			int counter = 0;
			for (int j = 0; j < players.get(i).
					getAssetsOnMerchantStand().size(); ++j) {
				if (players.get(i).getAssetsOnMerchantStand().
						get(j).getNameAsset().equals("Apple")) {
					counter++;
				}
			}
			players.get(i).setLegalCards(counter);
		}

		BonusComparator bonuscomparator = new BonusComparator();
		Collections.sort(players, bonuscomparator);

		int diferentiator = 0;
		int price = Constants.PRICE4;
		boolean[] testVec = new boolean[players.size()];
		for (int i = 0; i < players.size(); ++i) {
			testVec[i] = true;
		}
		
		for (int i = 0; i < players.size() - 1; ++i) {
			if (players.get(i).getLegalCards() == players.get(i + 1).getLegalCards()) {
				if (testVec[i]) {
					players.get(i).addToCoins(price);
					testVec[i] = false;
				}
				if (testVec[i + 1]) {
					players.get(i + 1).addToCoins(price);
					testVec[i + 1] = false;
				}
			} else {
				diferentiator++;
				if (testVec[i] == true) {
					players.get(i).addToCoins(price);
					testVec[i] = false;
				}
				if (diferentiator == 2) {
					break;
				}
				price -= Constants.PRICE1;
				if (testVec[i + 1]) {
					players.get(i + 1).addToCoins(price);
					testVec[i + 1] = false;
				}
			}

		}
		// PENTRU CHEESE
		for (int i = 0; i < players.size(); ++i) {
			int counter = 0;
			for (int j = 0; j < players.get(i).
					getAssetsOnMerchantStand().size(); ++j) {
				if (players.get(i).getAssetsOnMerchantStand().
						get(j).getNameAsset().equals("Cheese")) {
					counter++;
				}
			}
			players.get(i).setLegalCards(counter);
		}

		Collections.sort(players, bonuscomparator);

		diferentiator = 0;
		price = Constants.PRICE3;
		for (int i = 0; i < players.size(); ++i) {
			testVec[i] = true;
		}

		for (int i = 0; i < players.size() - 1; ++i) {
			if (players.get(i).getLegalCards() == players.get(i + 1).getLegalCards()) {
				if (testVec[i]) {
					players.get(i).addToCoins(price);
					testVec[i] = false;
				}
				if (testVec[i + 1]) {
					players.get(i + 1).addToCoins(price);
					testVec[i + 1] = false;
				}
			} else {
				diferentiator++;
				if (testVec[i]) {
					players.get(i).addToCoins(price);
					testVec[i] = false;
				}
				if (diferentiator == 2) {
					break;
				}
				price -= Constants.PRICE2;
				if (testVec[i + 1]) {
					players.get(i + 1).addToCoins(price);
					testVec[i + 1] = false;
				}
			}

		}
		// PENTRU BREAD
		for (int i = 0; i < players.size(); ++i) {
			int counter = 0;
			for (int j = 0; j < players.get(i)
					.getAssetsOnMerchantStand().size(); ++j) {
				if (players.get(i).getAssetsOnMerchantStand().
						get(j).getNameAsset().equals("Bread")) {
					counter++;
				}
			}
			players.get(i).setLegalCards(counter);
		}

		Collections.sort(players, bonuscomparator);

		diferentiator = 0;
		price = Constants.PRICE3;
		for (int i = 0; i < players.size(); ++i) {
			testVec[i] = true;
		}

		for (int i = 0; i < players.size() - 1; ++i) {
			if (players.get(i).getLegalCards()
					== players.get(i + 1).getLegalCards()) {
				if (testVec[i]) {
					players.get(i).addToCoins(price);
					testVec[i] = false;
				}
				if (testVec[i + 1]) {
					players.get(i + 1).addToCoins(price);
					testVec[i + 1] = false;
				}
			} else {
				diferentiator++;
				if (testVec[i]) {
					players.get(i).addToCoins(price);
					testVec[i] = false;
				}
				if (diferentiator == 2) {
					break;
				}
				price -= Constants.PRICE2;
				if (testVec[i + 1]) {
					players.get(i + 1).addToCoins(price);
					testVec[i + 1] = false;
				}
			}

		}

		// PENTRU CHICKEN
		for (int i = 0; i < players.size(); ++i) {
			int counter = 0;
			for (int j = 0; j < players.get(i).
					getAssetsOnMerchantStand().size(); ++j) {
				if (players.get(i).getAssetsOnMerchantStand().
						get(j).getNameAsset().equals("Chicken")) {
					counter++;
				}
			}
			players.get(i).setLegalCards(counter);
		}

		Collections.sort(players, bonuscomparator);

		diferentiator = 0;
		price = Constants.PRICE1;
		for (int i = 0; i < players.size(); ++i) {
			testVec[i] = true;
		}

		for (int i = 0; i < players.size() - 1; ++i) {
			if (players.get(i).getLegalCards() == players.get(i + 1).getLegalCards()) {
				if (testVec[i]) {
					players.get(i).addToCoins(price);
					testVec[i] = false;
				}
				if (testVec[i + 1]) {
					players.get(i + 1).addToCoins(price);
					testVec[i + 1] = false;
				}
			} else {
				diferentiator++;
				if (testVec[i]) {
					players.get(i).addToCoins(price);
					testVec[i] = false;
				}
				if (diferentiator == 2) {
					break;
				}
				price -= Constants.PRICE2;
				if (testVec[i + 1]) {
					players.get(i + 1).addToCoins(price);
					testVec[i + 1] = false;
				}
			}
		}
	}

	protected void results() {

		PlayerComparator playercomparator = new PlayerComparator();
		Collections.sort(players, playercomparator);
		for (int i = 0; i < players.size(); ++i) {
			System.out.println(players.get(i).getTypeOfPlayer()
					+ ": " + players.get(i).getCoins());
		}
	}
}
