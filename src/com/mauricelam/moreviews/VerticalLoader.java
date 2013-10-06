package com.mauricelam.moreviews;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.widget.ProgressBar;

public class VerticalLoader extends ProgressBar {
	private static final String androidns = "http://schemas.android.com/apk/res/android";
    public static final int LOADER = R.drawable.vloader;
    public static final int LOADER_LIGHT = R.drawable.vloader_light;

	public VerticalLoader(Context context) {
		super(context);
		init(context, null);
	}

	public VerticalLoader(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	public VerticalLoader(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context, attrs);
	}

    public void setLight(boolean light) {
        if (light) {
            setIndeterminateDrawable(getResources().getDrawable(LOADER_LIGHT));
        } else {
            setIndeterminateDrawable(getResources().getDrawable(LOADER));
        }
    }
	
	public void init(Context context, AttributeSet attrs){
//        int color = getResources().getColor(android.R.attr.textColorPrimary);
        TypedValue tv = new TypedValue();
        context.getTheme().resolveAttribute(android.R.attr.textColorPrimary, tv, true);
        int color = getResources().getColor(tv.resourceId);
        int brightness = Color.red(color) + Color.blue(color) + Color.green(color);
        setLight(brightness > 384);
//        if (attrs != null && attrs.getAttributeIntValue(androidns, "progress", 0) == 1) {
//            setLight(true);
//        } else {
//            setLight(false);
//        }
		setIndeterminate(true);
	}

}
