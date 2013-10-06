package com.mauricelam.moreviews;

import android.app.Activity;
import android.content.Context;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;

public class MenuButton {
	private static MenuButton menuBtn;

	public static void addMenuButton(Activity activity) {
		menuBtn = new MenuButton(activity);
	}

	public static void onPrepareOptionsMenu(Activity activity) {
		menuBtn.showMenu(activity);
	}

	public static void onOptionMenuClosed(Activity activity) {
		menuBtn.hideMenu(activity);
	}

	private ImageButton btn;
	private Activity activity;

	public MenuButton(Activity activity) {
		if (activity.findViewById(R.id.menubutton) != null) {
			return;
		}
		this.activity = activity;
		ViewGroup root = (ViewGroup) activity
				.findViewById(android.R.id.content);
		View inflated = LayoutInflater.from(activity).inflate(
				R.layout.menubutton, root, true);
		if (inflated instanceof ImageButton)
			btn = (ImageButton) inflated;
		else
			btn = (ImageButton) inflated.findViewById(R.id.menubutton);
		if (btn == null) {
			return;
		}
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Context context = view.getContext();
				if (context instanceof Activity) {
					Activity activity = (Activity) context;
					activity.openOptionsMenu();
				}
			}
		});
		final GestureDetector d = new GestureDetector(activity,
				new GestureListener());
		btn.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				d.onTouchEvent(event);
				return false;
			}
		});
	}

	public void showMenu(Activity activity) {
		View view = activity.findViewById(R.id.menubutton);
		if (view != null)
			animateUp(activity, view);
	}

	public void hideMenu(Activity activity) {
		View view = activity.findViewById(R.id.menubutton);
		if (view != null)
			animateDown(activity, view);
	}

	private void animateUp(Context context, View view) {
		Animation animation = AnimationUtils.loadAnimation(context,
				R.anim.menubuttonup);
		view.startAnimation(animation);
	}

	private void animateDown(Context context, View view) {
		Animation animation = AnimationUtils.loadAnimation(context,
				R.anim.menubuttondown);
		view.startAnimation(animation);
	}

	private class GestureListener extends SimpleOnGestureListener {

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			float scrollDist = e2.getY() - e1.getY();
			if (scrollDist < -70) {
				activity.openOptionsMenu();
			} else {
				activity.closeOptionsMenu();
			}
			return super.onScroll(e1, e2, distanceX, distanceY);
		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			float scrollDist = e2.getY() - e1.getY();
			if (scrollDist + velocityY / 10 < -70) {
				activity.openOptionsMenu();
			} else {
				activity.closeOptionsMenu();
			}
			return super.onFling(e1, e2, velocityX, velocityY);
		}
	}

}
