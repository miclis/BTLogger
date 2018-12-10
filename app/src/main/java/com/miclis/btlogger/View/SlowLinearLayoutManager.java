package com.miclis.btlogger.View;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;

public class SlowLinearLayoutManager extends LinearLayoutManager {
	private static final float MILLIS_PER_PIXEL = 120f;

	public SlowLinearLayoutManager (Context context){
		super(context);
	}

	@Override
	public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
		final LinearSmoothScroller linearSmoothScroller = new LinearSmoothScroller(recyclerView.getContext()){
			@Override
			protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
				return MILLIS_PER_PIXEL / displayMetrics.densityDpi;
			}
		};

		linearSmoothScroller.setTargetPosition(position);
		startSmoothScroll(linearSmoothScroller);
	}
}
