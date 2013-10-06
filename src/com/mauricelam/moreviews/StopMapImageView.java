package com.mauricelam.moreviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

public class StopMapImageView extends SmartImageView {
	private static final int INVALID_COORD = Integer.MAX_VALUE;

	private int lat = INVALID_COORD, lng = INVALID_COORD;
	private int zoom;
	private float heightRatio;
	private boolean showCenterMarker = true;
	private String markerColor = "blue";

	public StopMapImageView(Context context) {
		super(context);
	}

	public StopMapImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	public StopMapImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context, attrs);
	}

	private void init(Context context, AttributeSet attrs) {
		TypedArray styledAttrs = context
				.obtainStyledAttributes(attrs, R.styleable.StopMapImageView);
		zoom = styledAttrs.getInt(R.styleable.StopMapImageView_zoom, 0);
		markerColor = styledAttrs.getString(R.styleable.StopMapImageView_markerColor);
		showCenterMarker = styledAttrs.getBoolean(R.styleable.StopMapImageView_showCenterMarker,
				true);
		heightRatio = styledAttrs.getFloat(R.styleable.StopMapImageView_heightRatio, 0.5f);

		styledAttrs.recycle();
	}

	public void setCenter(int lat, int lng) {
		this.lat = lat;
		this.lng = lng;
	}

	public void setZoom(int zoom) {
		this.zoom = zoom;
	}

	public void setCenterMarker(boolean marker) {
		showCenterMarker = marker;
	}

	public void setMarkerColor(String color) {
		markerColor = color;
	}

	public void setHeightRatio(float ratio) {
		if (heightRatio != ratio) {
			heightRatio = ratio;
			load();
		}
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		if (w != 0) {
			load();
		}
	}

	public void load() {
		int width = getWidth();
		if (width != 0 && lat != INVALID_COORD && lng != INVALID_COORD) {
			String url = getURL(width, (int) (width * heightRatio));
			setImageUrl(url);
		}
	}

	private String getURL(int width, int height) {
		StringBuilder sb = new StringBuilder(300).append("http://maps.googleapis.com/maps/api/staticmap?");
		sb.append("center=");
		appendLocationString(sb, lat, lng);
		sb.append("&zoom=").append(zoom);
		sb.append("&size=").append(width).append('x').append(height);
		sb.append("&sensor=false");
		if (showCenterMarker) {
			sb.append("&markers=color:").append(markerColor);
			sb.append("|");
			appendLocationString(sb, lat, lng);
		}
		return sb.toString();
	}

	private void appendLocationString(StringBuilder sb, int lat, int lng) {
		sb.append(lat / 1E6).append(',').append(lng / 1E6);
	}

}
