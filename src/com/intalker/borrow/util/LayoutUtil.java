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
	
	private static int detailDialogWidth = -1;
	private static int detailDialogHeight = -1;
	
	private static int detailDialogBoundMargin = -1;
	
	private static int detailInfoLineHeight = -1;
	
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

	public static int getBookShelfItemWidth() {
		if (shelfBookItemWidth < 0) {
			shelfBookItemWidth = getShelfRowHeight() / 2;
		}
		return shelfBookItemWidth;
	}
	
	public static int getBookShelfItemHeight() {
		if (shelfBookItemHeight < 0) {
			shelfBookItemHeight = getShelfRowHeight() * 3 / 5;
		}
		return shelfBookItemHeight;
	}
	
	public static int getShelfBookGap() {
		if (shelfBookGap < 0) {
			shelfBookGap = (getShelfWidth() - getBookShelfItemWidth()
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
					* 4 / 5 - getBookShelfItemHeight();
		}
		return shelfBookTopMargin;
	}
	
	public static int getDetailDialogWidth() {
		if(detailDialogWidth < 0) {
			detailDialogWidth = DensityAdaptor.getScreenWidth() * 4 / 5;
		}
		return detailDialogWidth;
	}
	
	public static int getDetailDialogHeight() {
		if(detailDialogHeight < 0) {
			detailDialogHeight = getDetailDialogWidth() * 3 / 2;
		}
		return detailDialogHeight;
	}
	
	public static int getDetailDialogBoundMargin() {
		if(detailDialogBoundMargin < 0) {
			detailDialogBoundMargin = DensityAdaptor.getDensityIndependentValue(10);
		}
		return detailDialogBoundMargin;
	}
	
	public static int getDetailInfoLineHeight() {
		if(detailInfoLineHeight < 0) {
			detailInfoLineHeight = DensityAdaptor.getDensityIndependentValue(16);
		}
		return detailInfoLineHeight;
	}
}
