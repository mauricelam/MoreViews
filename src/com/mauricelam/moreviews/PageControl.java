package com.mauricelam.moreviews;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.Shape;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class PageControl extends LinearLayout {
	private int mIndicatorSize = 7;

	private Drawable activeDrawable;
	private Drawable inactiveDrawable;

	private ArrayList<PageIndicator> indicators;

	private int mPageCount = 0;
	private int mCurrentPage = 0;

	private Context mContext;
	private OnPageControlClickListener mOnPageControlClickListener = null;

	public PageControl(Context context) {
		super(context);
		mContext = context;
		initPageControl();
	}

	public PageControl(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		// will now wait until onFinishInflate to call initPageControl()
	}

	@Override
	protected void onFinishInflate() {
		initPageControl();
	}

	private void initPageControl() {
		// Log.i("uk.co.jasonfry.android.tools.ui.PageControl",
		// "Initialising PageControl");

		indicators = new ArrayList<>();

		activeDrawable = new ShapeDrawable();
		inactiveDrawable = new ShapeDrawable();

		activeDrawable.setBounds(0, 0, mIndicatorSize, mIndicatorSize);
		inactiveDrawable.setBounds(0, 0, mIndicatorSize, mIndicatorSize);

		Shape s1 = new OvalShape();
		s1.resize(mIndicatorSize, mIndicatorSize);

		Shape s2 = new OvalShape();
		s2.resize(mIndicatorSize, mIndicatorSize);

		int i[] = new int[2];
		i[0] = android.R.attr.textColorSecondary;
		i[1] = android.R.attr.textColorSecondaryInverse;
		TypedArray a = mContext.getTheme().obtainStyledAttributes(i);

		((ShapeDrawable) activeDrawable).getPaint().setColor(
				a.getColor(0, Color.DKGRAY));
		((ShapeDrawable) inactiveDrawable).getPaint().setColor(
				a.getColor(1, Color.LTGRAY));

		((ShapeDrawable) activeDrawable).setShape(s1);
		((ShapeDrawable) inactiveDrawable).setShape(s2);

		mIndicatorSize = (int) (mIndicatorSize * getResources()
				.getDisplayMetrics().density);

		setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if (mOnPageControlClickListener != null) {
					switch (event.getAction()) {
					case MotionEvent.ACTION_UP:

						if (PageControl.this.getOrientation() == LinearLayout.HORIZONTAL) {
							// if on left of view
							if (event.getX() < (PageControl.this.getWidth() / 2)) {
								if (mCurrentPage > 0) {
									mOnPageControlClickListener.goBackwards();
								}
							} else { // if on right of view
								if (mCurrentPage < (mPageCount - 1)) {
									mOnPageControlClickListener.goForwards();
								}
							}
						} else {
							// if on top half of view
							if (event.getY() < (PageControl.this.getHeight() / 2)) {
								if (mCurrentPage > 0) {
									mOnPageControlClickListener.goBackwards();
								}
							} else { // if on bottom half of view
								if (mCurrentPage < (mPageCount - 1)) {
									mOnPageControlClickListener.goForwards();
								}
							}
						}
						return false;
					}
				}
				return true;
			}
		});
	}

	/**
	 * Set the drawable object for an active page indicator
	 * 
	 * @param d
	 *            The drawable object for an active page indicator
	 */
	public void setDefaultActiveDrawable(Drawable d) {
		activeDrawable = d;
		// indicators.get(mCurrentPage).setBackgroundDrawable(activeDrawable);
	}

	/**
	 * Return the current drawable object for an active page indicator
	 * 
	 * @return Returns the current drawable object for an active page indicator
	 */
	public Drawable getDefaultActiveDrawable() {
		return activeDrawable;
	}

	/**
	 * Set the drawable object for an inactive page indicator
	 * 
	 * @param d
	 *            The drawable object for an inactive page indicator
	 */
	public void setDefaultInactiveDrawable(Drawable d) {
		inactiveDrawable = d;
		// for (int i = 0; i < mPageCount; i++) {
		// indicators.get(i).setBackgroundDrawable(inactiveDrawable);
		// }
		// indicators.get(mCurrentPage).setBackgroundDrawable(activeDrawable);
	}

	/**
	 * Return the current drawable object for an inactive page indicator
	 * 
	 * @return Returns the current drawable object for an inactive page
	 *         indicator
	 */
	public Drawable getDefaultInactiveDrawable() {
		return inactiveDrawable;
	}

	public void setActiveDrawable(Drawable d, int position) {
		if (position >= 0 && position < indicators.size())
			indicators.get(position).setActiveDrawable(d);
	}

	public void setInactiveDrawable(Drawable d, int position) {
		if (position >= 0 && position < indicators.size())
			indicators.get(position).setInactiveDrawable(d);
	}

	/**
	 * Set the number of pages this PageControl should have
	 * 
	 * @param pageCount
	 *            The number of pages this PageControl should have
	 */
	// private void setPageCount(int pageCount) {
	// // create added dots
	// for (int i = mPageCount; i < pageCount; i++) {
	// final PageIndicator indicator = new PageIndicator(mContext);
	// indicators.add(indicator);
	// addView(indicator);
	// }
	// // remove excess dots
	// for (int i = mPageCount - 1; i >= pageCount; i--) {
	// indicators.remove(i);
	// removeViewAt(i);
	// }
	// mPageCount = pageCount;
	// }

	/**
	 * Set the add to or subtract number of pages in this PageControl
	 * 
	 * @param position
	 *            at which position are new pages added / removed
	 * @param number
	 *            the number of pages to add (negative for remove)
	 */
	public void addPageCount(int position, int number) {
		try {
			indicators.get(mCurrentPage).deselectPage();
		} catch (IndexOutOfBoundsException e) {
			// safely ignore
		}
		for (int i = position; i < position + number; i++) {
			final PageIndicator indicator = new PageIndicator(mContext);
			indicators.add(i, indicator);
			addView(indicator, i);
		}
		// remove excess dots
		for (int i = position - number - 1; i >= position; i--) {
			indicators.remove(i);
			removeViewAt(i);
		}
		mPageCount += number;
		try {
			// bounds check
			if (mCurrentPage >= mPageCount)
				mCurrentPage = mPageCount - 1;
			else if (mCurrentPage < 0)
				mCurrentPage = 0;
			indicators.get(mCurrentPage).selectPage();
		} catch (IndexOutOfBoundsException e) {
			// ignore
		}
	}

	/**
	 * Return the number of pages this PageControl has
	 * 
	 * @return Returns the number of pages this PageControl has
	 */
	public int getPageCount() {
		return mPageCount;
	}

	/**
	 * Set the current page the PageControl should be on
	 * 
	 * @param currentPage
	 *            The current page the PageControl should be on
	 */
	public void setCurrentPage(int currentPage) {
		if (currentPage < mPageCount) {
			// reset old indicator
			indicators.get(mCurrentPage).deselectPage();
			// set up new indicator
			indicators.get(currentPage).selectPage();
			mCurrentPage = currentPage;
		}
	}

	/**
	 * Return the current page the PageControl is on
	 * 
	 * @return Returns the current page the PageControl is on
	 */
	public int getCurrentPage() {
		return mCurrentPage;
	}

	/**
	 * Set the size of the page indicator drawables
	 * 
	 * @param indicatorSize
	 *            The size of the page indicator drawables
	 */
	public void setIndicatorSize(int indicatorSize) {
		mIndicatorSize = indicatorSize;
		for (int i = 0; i < mPageCount; i++) {
			indicators.get(i).setLayoutParams(
					new LayoutParams(mIndicatorSize, mIndicatorSize));
		}
	}

	/**
	 * Return the size of the page indicator drawables
	 * 
	 * @return Returns the size of the page indicator drawables
	 */
	public int getIndicatorSize() {
		return mIndicatorSize;
	}

	/**
	 * 
	 * @author Jason Fry - jasonfry.co.uk
	 * 
	 *         Interface definition for a callback to be invoked when a
	 *         PageControl is clicked.
	 * 
	 */
	public interface OnPageControlClickListener {
		/**
		 * Called when the PageControl should go forwards
		 * 
		 */
		public abstract void goForwards();

		/**
		 * Called when the PageControl should go backwards
		 * 
		 */
		public abstract void goBackwards();
	}

	/**
	 * Set the OnPageControlClickListener object for this PageControl
	 * 
	 * @param onPageControlClickListener
	 *            The OnPageControlClickListener you wish to set
	 */
	public void setOnPageControlClickListener(
			OnPageControlClickListener onPageControlClickListener) {
		mOnPageControlClickListener = onPageControlClickListener;
	}

	/**
	 * Return the OnPageControlClickListener that has been set on this
	 * PageControl
	 * 
	 * @return Returns the OnPageControlClickListener that has been set on this
	 *         PageControl
	 */
	public OnPageControlClickListener getOnPageControlClickListener() {
		return mOnPageControlClickListener;
	}

	private class PageIndicator extends ImageView {
		private Drawable activeDrawable;
		private Drawable inactiveDrawable;
		private boolean isCurrentPage = false;

		public PageIndicator(Context context) {
			super(context);
			this.activeDrawable = getDefaultActiveDrawable();
			this.inactiveDrawable = getDefaultInactiveDrawable();
			init();
		}

		public PageIndicator(Context context, Drawable activeDrawable,
				Drawable inactiveDrawable) {
			super(context);
			this.activeDrawable = activeDrawable;
			this.inactiveDrawable = inactiveDrawable;
			init();
		}

		public void setActiveDrawable(Drawable d) {
			activeDrawable = d;
			if (isCurrentPage)
				this.setBackgroundDrawable(d);
		}

		public void setInactiveDrawable(Drawable d) {
			inactiveDrawable = d;
			if (!isCurrentPage)
				this.setBackgroundDrawable(d);
		}

		public void selectPage() {
			isCurrentPage = true;
			this.setBackgroundDrawable(activeDrawable);
		}

		public void deselectPage() {
			isCurrentPage = false;
			this.setBackgroundDrawable(inactiveDrawable);
		}

		private void init() {
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					mIndicatorSize, mIndicatorSize);
			params.setMargins(mIndicatorSize / 4, 0, mIndicatorSize / 4, 0);
			this.setLayoutParams(params);
			this.setBackgroundDrawable(inactiveDrawable);
		}

		private Drawable getDefaultActiveDrawable() {
			return PageControl.this.activeDrawable;
		}

		private Drawable getDefaultInactiveDrawable() {
			return PageControl.this.inactiveDrawable;
		}

	}

}