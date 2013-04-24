package com.intalker.borrow.ui.book;

import java.util.ArrayList;

import com.intalker.borrow.HomeActivity;
import com.intalker.borrow.R;
import com.intalker.borrow.data.AppData;
import com.intalker.borrow.data.BookInfo;
import com.intalker.borrow.data.MessageInfo;
import com.intalker.borrow.ui.control.HaloButton;
import com.intalker.borrow.ui.social.BookOwnersDialog;
import com.intalker.borrow.ui.social.IncomeMessagesDialog;
import com.intalker.borrow.util.LayoutUtil;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;

public class BookShelfItem extends RelativeLayout {
	public static BookShelfItem lastBookForTest = null;
	private ImageView mCoverImageView = null;
	private BookInfo mInfo = null;
	private ArrayList<MessageInfo> mMessageInfoList = null;
	private HaloButton mMessageBtn = null;

	public BookShelfItem(Context context, BookInfo bookInfo) {
		super(context);
		createUI(context);
		if (null != bookInfo) {
			mInfo = bookInfo;
			Bitmap coverImage = mInfo.getCoverImage();
			if(null != coverImage)
			{
				setCoverImage(coverImage);
				//mCoverImageView.setImageBitmap(coverImage);
			}
			else
			{
				mCoverImageView.setImageResource(R.drawable.bookcover_unknown);
			}
		} else {
			mInfo = new BookInfo();
			AppData.getInstance().addOwnedBook(mInfo);
		}
		
		this.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				BookShelfItem item = (BookShelfItem) arg0;
				if(null != item)
				{
					BookDetailDialog detailDialog = HomeActivity.getApp()
							.getBookGallery().getBookDetailDialog();
					detailDialog.setInfo(item);
					detailDialog.show();
				}
			}
			
		});
		
//		this.setOnLongClickListener(new OnLongClickListener(){
//
//			@Override
//			public boolean onLongClick(View v) {
//				// TODO Auto-generated method stub
//				Toast.makeText(v.getContext(), "test", Toast.LENGTH_SHORT).show();
//				return true;
//			}
//			
//		});
		
		mMessageBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				IncomeMessagesDialog msgsDialog = new IncomeMessagesDialog(HomeActivity.getApp());
				msgsDialog.setMessages(mMessageInfoList);
				msgsDialog.show();
			}
		});
		
		mMessageInfoList = new ArrayList<MessageInfo>();
	}
	
	public void clearMessages() {
		mMessageInfoList.clear();
		mMessageBtn.setVisibility(GONE);
	}
	
	public void attachMessage(MessageInfo msg) {
		for (MessageInfo info : mMessageInfoList) {
			if (info.getId().compareTo(msg.getId()) == 0) {
				return;
			}
		}
		mMessageInfoList.add(msg);
		mMessageBtn.setVisibility(VISIBLE);
		mMessageBtn.setText(String.valueOf(mMessageInfoList.size()));
	}

	public void setCoverImage(Bitmap coverImage) {
		int w = coverImage.getWidth();
		int h = coverImage.getHeight();
		float ratio = (float) h / (float) w;
		RelativeLayout.LayoutParams lp = (LayoutParams) mCoverImageView.getLayoutParams();
		lp.height = (int) (lp.width * ratio);
		mCoverImageView.setLayoutParams(lp);
		mCoverImageView.setImageBitmap(coverImage);
		mInfo.setCoverImage(coverImage);
	}

	public void setISBN(String isbn) {
		mInfo.setISBN(isbn);
	}

	public void setCoverAsUnknown() {
		mCoverImageView.setImageResource(R.drawable.bookcover_unknown);
	}

	private void createUI(Context context) {
		mCoverImageView = new ImageView(context);
		RelativeLayout.LayoutParams coverImgaeViewLP = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);

		coverImgaeViewLP.width = LayoutUtil.getBookShelfItemWidth();
		//coverImgaeViewLP.height = LayoutUtil.getBookShelfItemHeight();
		
		mCoverImageView.setScaleType(ScaleType.FIT_XY);

//		double random = Math.random();
//
//		int resId = 0;
//		if (random < 0.3) {
//			resId = R.drawable.bookcover_test;
//		} else if (random < 0.7) {
//			resId = R.drawable.bookcover_test1;
//		} else {
//			resId = R.drawable.bookcover_test2;
//		}
//		mCoverImageView.setImageResource(resId);

		coverImgaeViewLP.addRule(RelativeLayout.CENTER_HORIZONTAL);
		coverImgaeViewLP.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		mCoverImageView.setLayoutParams(coverImgaeViewLP);
		this.addView(mCoverImageView);
		
		mMessageBtn = new HaloButton(context, R.drawable.bubble);
		RelativeLayout.LayoutParams msgBtnLP = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		msgBtnLP.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		msgBtnLP.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		int smallMargin = LayoutUtil.getSmallMargin();
		msgBtnLP.leftMargin = msgBtnLP.bottomMargin = smallMargin;
		mMessageBtn.setLayoutParams(msgBtnLP);
		this.addView(mMessageBtn);
		mMessageBtn.setVisibility(GONE);
	}

	public void hideBeforeLoaded() {
		this.setVisibility(View.GONE);
		lastBookForTest = this;
	}

	public void show() {
		this.setVisibility(View.VISIBLE);
	}
	
	public void setDetailInfo(String name, String author, String publisher,
			String pageCount, String description) {
		mInfo.setBookName(name);
		mInfo.setAuthor(author);
		mInfo.setPublisher(publisher);
		mInfo.setPageCount(pageCount);
		mInfo.setSummary(description);
	}
	
	public BookInfo getInfo() {
		return mInfo;
	}
}
