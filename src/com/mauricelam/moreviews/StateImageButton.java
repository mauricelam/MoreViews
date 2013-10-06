package com.mauricelam.moreviews;

import android.content.Context;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageButton;

public class StateImageButton extends ImageButton {
	private static final String androidns = "http://schemas.android.com/apk/res/android";
	private int highlightColor;
	private int normalColor;

	public StateImageButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context, attrs);
	}

	public StateImageButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	public StateImageButton(Context context) {
		super(context);
	}

	private void init(Context context, AttributeSet attrs) {
		highlightColor = attrs.getAttributeIntValue(androidns, "textColorHighlight", 0);
		normalColor = attrs.getAttributeIntValue(androidns, "textColor", 0);
		if (!isInEditMode()) {
			this.setColorFilter(normalColor, PorterDuff.Mode.SRC_IN);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			this.setColorFilter(highlightColor, PorterDuff.Mode.SRC_IN);
		} else if (event.getAction() == MotionEvent.ACTION_UP) {
			this.setColorFilter(normalColor, PorterDuff.Mode.SRC_IN);
		}
		return super.onTouchEvent(event);
	}

}
