package com.intalker.borrow.util;

public class LayoutUtil {

	private static int navigationPanelWidth = -1;
	private static int shelfRowHeight = -1;
	private static int shelfBookItemWidth = -1;
	private static int shelfBookItemHeight = -1;
	private static int shelfRowBoardHeight = -1;
	private static int rowBookCount = 4;
	private static int shelfBookGap = -1;
	private static int shelfWidth = -1;
	private static int shelfBookTopMargin = -1;
	
	public static int getRowBookCount() {
		return rowBookCount;
	}

	public static int getNavigationPanelWidth() {
		if (navigationPanelWidth < 0) {
			navigationPanelWidth = DensityAdaptor.getScreenWidth() / 5;
		}
		return navigationPanelWidth;
	}

	public static int getShelfRowHeight() {
		if (shelfRowHeight < 0) {
			shelfRowHeight = DensityAdaptor.getScreenHeight() / 5;
		}
		return shelfRowHeight;
	}

	public static int getShelfBoardHeight() {
		if (shelfRowBoardHeight < 0) {
			shelfRowBoardHeight = getShelfRowHeight() / 4;
		}
		return shelfRowBoardHeight;
	}

	public static int getShelfBookItemWidth() {
		if (shelfBookItemWidth < 0) {
			shelfBookItemWidth = getShelfRowHeight() * 2 / 5;
		}
		return shelfBookItemWidth;
	}
	
	public static int getShelfBookItemHeight() {
		if (shelfBookItemHeight < 0) {
			shelfBookItemHeight = getShelfRowHeight() * 2 / 3;
		}
		return shelfBookItemHeight;
	}
	
	public static int getShelfBookGap() {
		if (shelfBookGap < 0) {
			shelfBookGap = (getShelfWidth() - getShelfBookItemWidth()
					* rowBookCount)
					/ (rowBookCount + 1);
		}
		return shelfBookGap;
	}
	
	public static int getShelfWidth() {
		if (shelfWidth < 0) {
			shelfWidth = DensityAdaptor.getScreenWidth()
					- getNavigationPanelWidth();
		}
		return shelfWidth;
	}
	
	public static int getShelfBookTopMargin() {
		if (shelfBookTopMargin < 0) {
			shelfBookTopMargin = getShelfRowHeight() - getShelfBoardHeight()
					* 4 / 5 - getShelfBookItemHeight();
		}
		return shelfBookTopMargin;
	}
}
