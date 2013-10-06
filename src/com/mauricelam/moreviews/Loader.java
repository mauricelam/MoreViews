package com.mauricelam.moreviews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * User: mauricelam
 * Date: 7/5/13
 * Time: 7:18 PM
 */
public class Loader extends LinearLayout {
    private static final String androidns = "http://schemas.android.com/apk/res/android";

    public Loader(Context context) {
        super(context);
        init(context, null);
    }

    public Loader(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public Loader(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    public void init (Context context, AttributeSet attrs) {
        if (attrs != null) {
            int style = attrs.getAttributeResourceValue(androidns, "textAppearance", 0);
            if (style > 0)
                context = new ContextThemeWrapper(context, style);
        }
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.textverticalloader, this, true);
        if (attrs != null) {
            TextView text = (TextView) view.findViewById(R.id.loaderText);
            String loadingText = getString(attrs, androidns, "text");
            if (loadingText != null) {
                text.setText(loadingText);
            }

//            int light = attrs.getAttributeIntValue(androidns, "progress", 0);
//            VerticalLoader loader = (VerticalLoader) view.findViewById(R.id.ProgressBar01);
//            loader.setLight(light == 1);
        }
    }

    public String getString (AttributeSet attrs, String schema, String key) {
        int resId = attrs.getAttributeResourceValue(schema, key, -1);
        String output;
        if(resId != -1){
            output = getContext().getResources().getString(resId);
        } else{
            output = attrs.getAttributeValue(schema, key);
        }
        return output;
    }

}
