package com.intalker.borrow.ui.book;

import com.intalker.borrow.HomeActivity;
import com.intalker.borrow.R;
import com.intalker.borrow.cloud.CloudAPI;
import com.intalker.borrow.cloud.CloudAPIAsyncTask.ICloudAPITaskListener;
import com.intalker.borrow.data.AppData;
import com.intalker.borrow.data.BookInfo;
import com.intalker.borrow.isbn.ISBNResolver;
import com.intalker.borrow.ui.FullSizeImageDialog;
import com.intalker.borrow.ui.UIConfig;
import com.intalker.borrow.ui.control.ControlFactory;
import com.intalker.borrow.ui.control.HaloButton;
import com.intalker.borrow.util.DensityAdaptor;
import com.intalker.borrow.util.LayoutUtil;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class BookDetailDialog extends Dialog {

	private RelativeLayout mContent = null;
	private RelativeLayout mLayout = null;
	private ImageView mCoverImageView = null;
	private RelativeLayout mDetailInfoPanel = null;
	
	private BookShelfItem mBookItem = null;
	
	private TextView mNameTextView = null;
	private TextView mAuthorTextView = null;
	private TextView mPublisherTextView = null;
	private TextView mPageCountTextView = null;
	private TextView mISBNTextView = null;
	private TextView mDescriptionTextView = null;
	
	private HaloButton mDeleteButton = null;
	private HaloButton mCloseButton = null;
	
	private FullSizeImageDialog mFullSizeImageDialog = null;
	
	private Bitmap mCoverImage = null;
	private BookDetailDialog mDialog = null;
	
	public BookDetailDialog(Context context) {
		super(context, R.style.Theme_TransparentDialog);
		mDialog = this;
		
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setCanceledOnTouchOutside(true);
		
		mFullSizeImageDialog = new FullSizeImageDialog(context);
		
		createUI(context);
		addListeners();
	}
	
	private void createUI(Context context) {
		mContent = new RelativeLayout(context);
		this.setContentView(mContent);
		
		mLayout = new RelativeLayout(context);
		mLayout.setBackgroundResource(R.drawable.parchment_bg);
		RelativeLayout.LayoutParams mainLayoutLP = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		mainLayoutLP.width = LayoutUtil.getDetailDialogWidth();
		mainLayoutLP.height = LayoutUtil.getDetailDialogHeight();
		mContent.addView(mLayout, mainLayoutLP);
		
		mCoverImageView = new ImageView(context);
		RelativeLayout.LayoutParams coverImageLP = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		coverImageLP.width = LayoutUtil.getDetailDialogWidth() / 2;
		coverImageLP.height = LayoutUtil.getDetailDialogHeight() / 2;
		int boundMargin = LayoutUtil.getDetailDialogBoundMargin();
		coverImageLP.leftMargin = boundMargin;
		coverImageLP.topMargin = boundMargin;
		mCoverImageView.setImageResource(R.drawable.bookcover_unknown);
		//mCoverImage.setScaleType(ScaleType.FIT_START);
		mCoverImageView.setScaleType(ScaleType.FIT_CENTER);
		//mCoverImage.setBackgroundColor(Color.BLUE);
		mLayout.addView(mCoverImageView, coverImageLP);
		
		mDetailInfoPanel = new RelativeLayout(context);
		//mDetailInfoPanel.setBackgroundColor(Color.WHITE);
		RelativeLayout.LayoutParams detailInfoPanelLP = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		detailInfoPanelLP.width = LayoutUtil.getDetailDialogWidth() / 2 - boundMargin * 3;
		detailInfoPanelLP.height = LayoutUtil.getDetailDialogHeight() / 2;
		detailInfoPanelLP.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		detailInfoPanelLP.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		detailInfoPanelLP.topMargin = boundMargin;
		detailInfoPanelLP.rightMargin = boundMargin;
		mLayout.addView(mDetailInfoPanel, detailInfoPanelLP);
		
		mNameTextView = addInfoTextView(R.string.book_name, 0);
		mAuthorTextView = addInfoTextView(R.string.author, 1);
		mPublisherTextView = addInfoTextView(R.string.publisher, 2);
		mPageCountTextView = addInfoTextView(R.string.page_count, 3);
		mISBNTextView = addInfoTextView(R.string.isbn, 4);
		
		int separatorY = boundMargin + LayoutUtil.getDetailDialogHeight() / 2;
		mLayout.addView(ControlFactory.createHoriSeparatorForRelativeLayout(context,
				LayoutUtil.getDetailDialogWidth(),
				separatorY));
		
		int descriptionHeight = LayoutUtil.getDetailDialogHeight() * 3 / 10;
		mLayout.addView(createDescriptionPanel(descriptionHeight));
		
		mLayout.addView(ControlFactory.createHoriSeparatorForRelativeLayout(context,
				LayoutUtil.getDetailDialogWidth(),
				separatorY + descriptionHeight + boundMargin - DensityAdaptor.getDensityIndependentValue(8)));

		// Buttons
		mDeleteButton = new HaloButton(context, R.drawable.trash);
		
		RelativeLayout.LayoutParams deleteBtnLP = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		deleteBtnLP.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		deleteBtnLP.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		deleteBtnLP.leftMargin = boundMargin;
		deleteBtnLP.bottomMargin = boundMargin;
		
		mLayout.addView(mDeleteButton, deleteBtnLP);
		
		//Close button.
		mCloseButton = new HaloButton(context, R.drawable.close);
		
		RelativeLayout.LayoutParams closeBtnLP = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		closeBtnLP.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		closeBtnLP.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
//		closeBtnLP.rightMargin = boundMargin;
//		closeBtnLP.topMargin = boundMargin;
		
		mLayout.addView(mCloseButton, closeBtnLP);
	}
	
	private void addListeners() {
		mDeleteButton.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				Context context = v.getContext();
				String confirm = context.getString(R.string.confirm);
				String deleteBookConfirm = context.getString(R.string.delete_book_confirm);
				String ok = context.getString(R.string.ok);
				String cancel = context.getString(R.string.cancel);

				AlertDialog alertDialog = new AlertDialog.Builder(context)
						.setTitle(confirm)
						.setMessage(deleteBookConfirm)
						.setIcon(R.drawable.question)
						.setPositiveButton(ok,
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										AppData.getInstance().removeBook(
												mBookItem.getInfo());
										mBookItem.setVisibility(View.GONE);
										mDialog.dismiss();
										
										String isbn = mBookItem.getInfo()
												.getISBN();

										CloudAPI.deleteBook(
												HomeActivity.getApp(), isbn,
												new ICloudAPITaskListener() {

													@Override
													public void onFinish(
															int returnCode) {

														Context context = HomeActivity
																.getApp();
														CloudAPI.isSuccessful(
																context,
																returnCode);
													}

												});
									}
								})
						.setNegativeButton(cancel,
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
									}
								}).create();
				alertDialog.show();
			}
		});
		
		mCloseButton.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				mDialog.dismiss();
			}
		});
		
		mCoverImageView.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				mFullSizeImageDialog.show();
			}
		});
	}
	
	private TextView addInfoTextView(int textResId, int index)
	{
		Context context = this.getContext();
		TextView label = new TextView(context);
		label.setTextColor(UIConfig.getNormalTextColor());
		label.setText(textResId);
		
		RelativeLayout.LayoutParams labelLP = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		int lineHeight = LayoutUtil.getDetailInfoLineHeight();
		int topMargin = lineHeight * index * 5 / 2 + lineHeight;
		labelLP.topMargin = topMargin;
		
		mDetailInfoPanel.addView(label, labelLP);
		
		TextView valueText = new TextView(context);
		valueText.setText("???");
		valueText.setTextSize(12.0f);
		valueText.setTextColor(Color.BLACK);
		
		RelativeLayout.LayoutParams valueTextLP = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		
		valueTextLP.topMargin = topMargin + lineHeight;
		valueTextLP.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		mDetailInfoPanel.addView(valueText, valueTextLP);

		return valueText;
	}

	private ScrollView createDescriptionPanel(int height)
	{
		ScrollView scrollView = new ScrollView(this.getContext());
		
		LinearLayout scrollContent = new LinearLayout(this.getContext());
		scrollContent.setOrientation(LinearLayout.VERTICAL);
		scrollView.addView(scrollContent);
		
		mDescriptionTextView = new TextView(this.getContext());
		mDescriptionTextView.setText("???");
		mDescriptionTextView.setTextColor(Color.BLACK);
		scrollContent.addView(mDescriptionTextView);
		
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		
		int margin = LayoutUtil.getDetailDialogBoundMargin();
		lp.leftMargin = margin;
		lp.rightMargin = margin;
		lp.topMargin = margin * 2 + LayoutUtil.getDetailDialogHeight() / 2 - DensityAdaptor.getDensityIndependentValue(6);
		lp.width = LayoutUtil.getDetailDialogWidth() - margin * 2;
		lp.height = height;
		
		scrollView.setLayoutParams(lp);
		
		return scrollView;
	}
	
	public void setInfo(BookShelfItem bookItem) {
		if (null != bookItem) {
			mBookItem = bookItem;
			BookInfo bookInfo = bookItem.getInfo();
			if (null != bookInfo) {
				mCoverImage = bookInfo.getCoverImage();
				if(null != mCoverImage)
				{
				mCoverImageView.setImageBitmap(mCoverImage);
				mFullSizeImageDialog.setImage(mCoverImage);
				}
				else
				{
					mCoverImageView.setImageResource(R.drawable.bookcover_unknown);
					mFullSizeImageDialog.setImage(R.drawable.bookcover_unknown);
				}
				mNameTextView.setText(bookInfo.getBookName());
				mAuthorTextView.setText(bookInfo.getAuthor());
				mPublisherTextView.setText(bookInfo.getPublisher());
				mPageCountTextView.setText(bookInfo.getPageCount());
				mISBNTextView.setText(bookInfo.getISBN());
				mDescriptionTextView.setText(bookInfo.getSummary());
			}
		}
	}

}
