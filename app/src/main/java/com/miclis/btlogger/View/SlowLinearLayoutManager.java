package com.miclis.btlogger.View;

import android.content.Context;
import android.graphics.PointF;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;

public class SlowLinearLayoutManager extends LinearLayoutManager {
	private static final float MILLIS_PER_PIXEL = 120f;

	public SlowLinearLayoutManager (Context context){
		super(context);
	}

	public SlowLinearLayoutManager (Context context, int orientation, boolean reverseLayout){
		super(context, orientation, reverseLayout);
	}

	public SlowLinearLayoutManager (Context context, AttributeSet attributeSet, int defStyleAttr, int defStyleRes){
		super(context, attributeSet, defStyleAttr, defStyleRes);
	}

	@Override
	public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
		final LinearSmoothScroller linearSmoothScroller = new LinearSmoothScroller(recyclerView.getContext()){
			@Override
			public PointF computeScrollVectorForPosition(int targetPosition) {
				return super.computeScrollVectorForPosition(targetPosition);
			}

			@Override
			protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
				return MILLIS_PER_PIXEL / displayMetrics.densityDpi;
			}
		};

		linearSmoothScroller.setTargetPosition(position);
		startSmoothScroll(linearSmoothScroller);
	}
}
