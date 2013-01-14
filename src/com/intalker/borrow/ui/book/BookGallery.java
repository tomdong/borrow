package com.intalker.borrow.ui.book;

import java.util.ArrayList;

import com.intalker.borrow.HomeActivity;
import com.intalker.borrow.R;
import com.intalker.borrow.data.AppData;
import com.intalker.borrow.data.BookInfo;
import com.intalker.borrow.data.UserInfo;
import com.intalker.borrow.util.DensityAdaptor;
import com.intalker.borrow.util.LayoutUtil;
import com.intalker.borrow.util.ScanUtil;

import android.content.Context;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class BookGallery extends RelativeLayout {

	private RelativeLayout mTopPanel = null;
	private RelativeLayout mBottomPanel = null;
	private BookShelfView mShelfView = null;
	private TextView mShelfOwnerTextView = null;
	
	public BookGallery(Context context) {
		super(context);
		
		createUI();
	}
	
	public void updateTopPanel()
	{
		UserInfo curLoginUser = UserInfo.getCurLoginUser();
		if(null != curLoginUser)
		{
			mShelfOwnerTextView.setText(curLoginUser.getNickName()
					+ this.getContext().getString(R.string.shelf_owner_suffix));
		}
	}

	private void createUI()
	{
		createTopPanel();
		createScrolledShelfView();
		createBottomPanel();
	}
	
	private void createTopPanel()
	{
		mTopPanel = new RelativeLayout(this.getContext());
		mTopPanel.setBackgroundResource(R.drawable.hori_bar);
		RelativeLayout.LayoutParams topPanelLP = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.FILL_PARENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		topPanelLP.height = LayoutUtil.getGalleryTopPanelHeight();
		this.addView(mTopPanel, topPanelLP);
		
		mShelfOwnerTextView = new TextView(this.getContext());
		mShelfOwnerTextView.setText(R.string.app_name);
		mShelfOwnerTextView.setTextColor(Color.WHITE);
		RelativeLayout.LayoutParams textLP = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		textLP.addRule(RelativeLayout.CENTER_VERTICAL);
		textLP.leftMargin = DensityAdaptor.getDensityIndependentValue(5);
		mTopPanel.addView(mShelfOwnerTextView, textLP);
	}
	
	private void createBottomPanel()
	{
		mBottomPanel = new RelativeLayout(this.getContext());
		View imageBackground = new View(this.getContext());
		imageBackground.setBackgroundResource(R.drawable.hori_bar);
		RelativeLayout.LayoutParams imageBGLP = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.FILL_PARENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		imageBGLP.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		imageBGLP.height = LayoutUtil.getGalleryBottomPanelHeight();
		mBottomPanel.addView(imageBackground, imageBGLP);
		
		ImageButton scanBtn = new ImageButton(this.getContext());
		scanBtn.setImageResource(R.drawable.scan_barcode);
		scanBtn.setBackgroundDrawable(null);
		scanBtn.setOnTouchListener(new OnTouchListener(){

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				ImageButton imageBtn = (ImageButton)v;
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					imageBtn.setImageResource(R.drawable.scan_barcode_down);
					break;
				default:
					imageBtn.setImageResource(R.drawable.scan_barcode);
					break;
				}
				return false;
			}
			
		});
		scanBtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				ScanUtil.scanBarCode(HomeActivity.getApp());
			}
			
		});
		RelativeLayout.LayoutParams scanBtnLP = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		scanBtnLP.addRule(RelativeLayout.CENTER_HORIZONTAL);
		scanBtnLP.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		mBottomPanel.addView(scanBtn, scanBtnLP);
		
		RelativeLayout.LayoutParams bottomPanelLP = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.FILL_PARENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		bottomPanelLP.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		this.addView(mBottomPanel, bottomPanelLP);
	}
	
	private void createScrolledShelfView()
	{
		mShelfView = new BookShelfView(this.getContext());
		RelativeLayout.LayoutParams shelfViewLP = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.FILL_PARENT,
				RelativeLayout.LayoutParams.FILL_PARENT);
		shelfViewLP.topMargin = LayoutUtil.getGalleryTopPanelHeight();
		shelfViewLP.bottomMargin = LayoutUtil.getGalleryBottomPanelHeight();
		this.addView(mShelfView, shelfViewLP);
	}
	
	public void clearBooks()
	{
		mShelfView.clearShelfRows();
	}
	
	public void resetBookShelf()
	{
		mShelfView.initializeShelfRows();
		AppData.getInstance().clearBooks();
	}
	
	// for test
	public void fillWithRandomBooks()
	{
		mShelfView.clearShelfRows();
		mShelfView.addRandomBooks();
	}
	
	public void addBook(BookShelfItem bookItem)
	{
	}
	
	public void initialWithCachedData()
	{
		//mShelfView.clearShelfRows();
		ArrayList<BookInfo> books = AppData.getInstance().getBooks();
		for(BookInfo bookInfo : books)
		{
			mShelfView.addBookForCachedBook(bookInfo);
		}
	}
}
