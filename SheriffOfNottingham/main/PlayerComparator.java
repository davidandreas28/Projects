package main;

import java.util.Comparator;

final class PlayerComparator implements Comparator<Player> {
	public int compare(final Player arg0, final Player arg1) {
		// TODO Auto-generated method stub
		return arg1.getCoins() - arg0.getCoins();
	}

}
