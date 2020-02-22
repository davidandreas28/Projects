package main;

import java.util.Comparator;

final class BonusComparator implements Comparator<Player> {

	@Override
	public int compare(final Player arg0, final Player arg1) {
		// TODO Auto-generated method stub
		return arg1.getLegalCards() - arg0.getLegalCards();
	}
}
