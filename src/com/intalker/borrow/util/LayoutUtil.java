package com.intalker.borrow.util;

public class LayoutUtil {

	private static int navigationPanelWidth = -1;
	private static int socialPanelWidth = -1;
	private static int shelfRowHeight = -1;
	private static int shelfBookItemWidth = -1;
	private static int shelfBookItemHeight = -1;
	private static int shelfRowBoardHeight = -1;
	private static int rowBookCount = 3;
	private static int shelfBookGap = -1;
	private static int shelfWidth = -1;
	private static int shelfBookTopMargin = -1;
	
	private static int detailDialogWidth = -1;
	private static int detailDialogHeight = -1;
	private static int detailDialogBoundMargin = -1;
	private static int detailInfoLineHeight = -1;
	
	private static int loginDialogMargin = -1;
	private static int loginDialogInputWidth = -1;
	
	private static int galleryTopPanelHeight = -1;
	private static int galleryBottomPanelHeight = -1;
	
	private static int bigUserViewItemWidth = -1;
	private static int bigUserViewItemHeight = -1;
	
	//General parameters
	private static int smallMargin = -1;
	private static int mediumMargin = -1;
	private static int largeMargin = -1;
	
	public static int getRowBookCount() {
		return rowBookCount;
	}

	public static int getNavigationPanelWidth() {
		if (navigationPanelWidth < 0) {
			int w1 = DensityAdaptor.getDensityIndependentValue(180);
			int w2 = DensityAdaptor.getScreenWidth() * 3 / 4;
			navigationPanelWidth = w2 > w1 ? w2 : w1;
		}
		return navigationPanelWidth;
	}
	
	public static int getSocialPanelWidth() {
		if (socialPanelWidth < 0) {
			socialPanelWidth = DensityAdaptor.getScreenWidth() * 3 / 4;
		}
		return socialPanelWidth;
	}

	public static int getShelfRowHeight() {
		if (shelfRowHeight < 0) {
			shelfRowHeight = DensityAdaptor.getScreenHeight() / 4;
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
			shelfBookItemHeight = getShelfRowHeight() * 4 / 5;
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
			shelfWidth = DensityAdaptor.getScreenWidth();
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
			detailDialogWidth = DensityAdaptor.getDensityIndependentValue(300);//.getScreenWidth() * 4 / 5;
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
	
	public static int getLoginDialogMargin() {
		if(loginDialogMargin < 0) {
			loginDialogMargin = DensityAdaptor.getDensityIndependentValue(20);
		}
		return loginDialogMargin;
	}
	
	public static int getLoginDialogInputWidth() {
		if(loginDialogInputWidth < 0) {
			loginDialogInputWidth = DensityAdaptor.getDensityIndependentValue(150);
		}
		return loginDialogInputWidth;
	}
	
	public static int getGalleryTopPanelHeight() {
		if(galleryTopPanelHeight < 0) {
			galleryTopPanelHeight = DensityAdaptor.getDensityIndependentValue(36);
		}
		return galleryTopPanelHeight;
	}
	
	public static int getGalleryBottomPanelHeight() {
		if(galleryBottomPanelHeight < 0) {
			galleryBottomPanelHeight = DensityAdaptor.getDensityIndependentValue(48);
		}
		return galleryBottomPanelHeight;
	}
	
	public static int getSmallMargin() {
		if(smallMargin < 0) {
			smallMargin = DensityAdaptor.getDensityIndependentValue(2);
		}
		return smallMargin;
	}
	
	public static int getMediumMargin() {
		if(mediumMargin < 0) {
			mediumMargin = DensityAdaptor.getDensityIndependentValue(8);
		}
		return mediumMargin;
	}
	
	public static int getLargeMargin() {
		if(largeMargin < 0) {
			largeMargin = DensityAdaptor.getDensityIndependentValue(30);
		}
		return largeMargin;
	}
	
	public static int getBigUserViewItemWidth() {
		if(bigUserViewItemWidth < 0) {
			bigUserViewItemWidth = DensityAdaptor.getDensityIndependentValue(64);
		}
		return bigUserViewItemWidth;
	}
	
	public static int getBigUserViewItemHeight() {
		if(bigUserViewItemHeight < 0) {
			bigUserViewItemHeight = DensityAdaptor.getDensityIndependentValue(64);
		}
		return bigUserViewItemHeight;
	}

}