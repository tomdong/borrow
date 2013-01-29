package com.intalker.borrow.ui.control;

import android.content.Context;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.Toast;

public class DragRefreshScroll extends ScrollView {
	private Scroller scroller;
	private RelativeLayout refreshView;
	private int refreshTargetTop = -60;
	private TextView mTipText = null;

	private RefreshListener refreshListener;

	private int lastY;
	private boolean isDragging = false;

	private Context mContext;
	private LinearLayout mContentView = null;

	public DragRefreshScroll(Context context) {
		super(context);
		mContext = context;
		init();
		this.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				return false;
			}

		});
	}

	public LinearLayout getScrollContent() {
		return mContentView;
	}

	private void init() {
		scroller = new Scroller(mContext);
		refreshView = new RelativeLayout(this.getContext());

		mContentView = new LinearLayout(mContext);
		mContentView.setOrientation(LinearLayout.VERTICAL);
		this.addView(mContentView);

		mTipText = new TextView(mContext);
		mTipText.setText("Release to refresh");
		RelativeLayout.LayoutParams textLP = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		textLP.addRule(RelativeLayout.CENTER_IN_PARENT);
		refreshView.addView(mTipText, textLP);

		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT, -refreshTargetTop);
		lp.topMargin = refreshTargetTop;
		lp.gravity = Gravity.CENTER;
		mContentView.addView(refreshView, lp);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		int y = (int) event.getRawY();

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			lastY = y;
			break;

		case MotionEvent.ACTION_MOVE:
			int m = y - lastY;
			if (((m < 6) && (m > -1)) || (!isDragging)) {
				doMovement(m);
			}
			this.lastY = y;
			break;

		case MotionEvent.ACTION_UP:
			fling();

			break;
		}
		return true;
	}

	private void fling() {
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) refreshView
				.getLayoutParams();
		if (lp.topMargin > 0) {
			refresh();
			Toast.makeText(mContext, "test", Toast.LENGTH_SHORT).show();
		}
		returnInitState();
	}

	private void returnInitState() {
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) this.refreshView
				.getLayoutParams();
		int i = lp.topMargin;
		scroller.startScroll(0, i, 0, refreshTargetTop);
		invalidate();
	}

	private void refresh() {
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) this.refreshView
				.getLayoutParams();
		int i = lp.topMargin;

		scroller.startScroll(0, i, 0, 0 - i);
		invalidate();
		if (refreshListener != null) {
			refreshListener.onRefresh();
		}
	}

	@Override
	public void computeScroll() {
		if (scroller.computeScrollOffset()) {
			int i = this.scroller.getCurrY();
			LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) this.refreshView
					.getLayoutParams();
			int k = Math.max(i, refreshTargetTop);
			lp.topMargin = k;
			this.refreshView.setLayoutParams(lp);
			this.refreshView.invalidate();
			invalidate();
		}
	}

	private void doMovement(int moveY) {
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) refreshView
				.getLayoutParams();
		if (moveY > 0) {
			float f1 = lp.topMargin;
			float f2 = moveY * 0.3F;
			int i = (int) (f1 + f2);
			lp.topMargin = i;
			refreshView.setLayoutParams(lp);
			refreshView.invalidate();
			invalidate();
		}
	}

	public void setRefreshListener(RefreshListener listener) {
		this.refreshListener = listener;
	}

	public void finishRefresh() {
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) this.refreshView
				.getLayoutParams();
		int i = lp.topMargin;
		scroller.startScroll(0, i, 0, refreshTargetTop);
		invalidate();
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent e) {
		int action = e.getAction();
		int y = (int) e.getRawY();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			lastY = y;
			break;

		case MotionEvent.ACTION_MOVE:
			int m = y - lastY;

			this.lastY = y;
			if (m > 6 && canScroll()) {
				return true;
			}
			break;
		case MotionEvent.ACTION_UP:

			break;

		case MotionEvent.ACTION_CANCEL:

			break;
		}
		return false;
	}

	private boolean canScroll() {
		View childView;
		if (getChildCount() > 1) {
			childView = this.getChildAt(1);
			if (childView instanceof ListView) {
				int top = ((ListView) childView).getChildAt(0).getTop();
				int pad = ((ListView) childView).getListPaddingTop();
				if ((Math.abs(top - pad)) < 3
						&& ((ListView) childView).getFirstVisiblePosition() == 0) {
					return true;
				} else {
					return false;
				}
			} else if (childView instanceof ScrollView) {
				if (((ScrollView) childView).getScrollY() == 0) {
					return true;
				} else {
					return false;
				}
			}

		}
		return false;
	}

	public interface RefreshListener {
		public void onRefresh();
	}
}
