package com.mauricelam.moreviews;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;

public class SmartImageView extends ImageView {
	private static final int LOADING_THREADS = 4;
	private static ExecutorService threadPool = Executors.newFixedThreadPool(LOADING_THREADS);

	private SmartImageTask currentTask;
	private Drawable overlay;

	public SmartImageView(Context context) {
		super(context);
	}

	public SmartImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	public SmartImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context, attrs);
	}

	private void init(Context context, AttributeSet attrs) {
		TypedArray styledAttrs = context.obtainStyledAttributes(attrs, R.styleable.SmartImageView);
		overlay = styledAttrs.getDrawable(R.styleable.SmartImageView_overlay);
	}

	public void setOverlay(Drawable d) {
		this.overlay = d;
	}

	public void setOverlay(Context context, int res) {
		Drawable d = context.getResources().getDrawable(res);
		setOverlay(d);
	}

	// Helpers to set image by URL
	public void setImageUrl(String url) {
		setImage(new WebImage(url));
	}

	public void setImageUrl(String url, final Integer fallbackResource) {
		setImage(new WebImage(url), fallbackResource);
	}

	public void setImageUrl(String url, final Integer fallbackResource, final Integer loadingResource) {
		setImage(new WebImage(url), fallbackResource, loadingResource);
	}

	// Helpers to set image by contact address book id
	public void setImageContact(long contactId) {
		setImage(new ContactImage(contactId));
	}

	public void setImageContact(long contactId, final Integer fallbackResource) {
		setImage(new ContactImage(contactId), fallbackResource);
	}

	public void setImageContact(long contactId, final Integer fallbackResource, final Integer loadingResource) {
		setImage(new ContactImage(contactId), fallbackResource, fallbackResource);
	}

	// Set image using SmartImage object
	public void setImage(final SmartImage image) {
		setImage(image, null, null);
	}

	public void setImage(final SmartImage image, final Integer fallbackResource) {
		setImage(image, fallbackResource, fallbackResource);
	}

	public void setImage(final SmartImage image, final Integer fallbackResource, final Integer loadingResource) {
		// Set a loading resource
		if (loadingResource != null) {
			setImageResource(loadingResource);
		}

		// Cancel any existing tasks for this image view
		if (currentTask != null) {
			currentTask.cancel();
			currentTask = null;
		}

		// Set up the new task
		currentTask = new SmartImageTask(getContext(), image);
		currentTask.setOnCompleteHandler(new SmartImageTask.OnCompleteHandler() {
			@Override
			public void onComplete(Bitmap bitmap) {
				if (bitmap != null) {
					setImageBitmap(bitmap);
					drawOverlay();
				} else {
					// Set fallback resource
					if (fallbackResource != null) {
						setImageResource(fallbackResource);
					}
				}
			}
		});

		// Run the task in a threadpool
		threadPool.execute(currentTask);
	}

	private void drawOverlay() {
		if (overlay != null) {
			//((ShapeDrawable) overlay).setIntrinsicWidth(getWidth());
			Drawable original = getDrawable();
			Drawable[] layers = new Drawable[] { original, overlay };
			LayerDrawable d = new LayerDrawable(layers);
			if (getWidth() > original.getIntrinsicWidth()) {
				int sidePadding = (getWidth() - original.getIntrinsicWidth()) / 2;
				d.setLayerInset(0, sidePadding, 0, sidePadding, 0);
			}
			setImageDrawable(d);
		}
	}

	public static void cancelAllTasks() {
		threadPool.shutdownNow();
		threadPool = Executors.newFixedThreadPool(LOADING_THREADS);
	}
}