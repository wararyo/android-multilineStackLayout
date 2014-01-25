package com.wararyo.library;

import com.wararyo.library.cardlayout.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class CardLayout extends ViewGroup {

	int orientation;
	int columns;
	
	final int HORIZONTAL = 0;
	final int VERTICAL = 1;
	
	public CardLayout(Context context) {
		super(context);
		Initialize(context, null);
		// TODO Auto-generated constructor stub
	}
	public CardLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		Initialize(context, attrs);
		// TODO Auto-generated constructor stub
	}
	@SuppressLint("NewApi")
	public CardLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		Initialize(context, attrs);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		if(columns == 0) return;
		
		int count = this.getChildCount();
		int width = getWidth();
		int columnwidth = width / columns;
		int sumX[] = new int[columns];
		int sumY[]= new int[columns];
		
		int offsetX[] = new int[columns];
		
		for(int n : sumX){
			n = 0;
		}
		for(int n : sumY){
			n = 0;
		}
		for(int i=0;i<columns;i++){
			offsetX[i] = (i * width)/columns;
		}
		for(int i=0; i<count; i++){
			View child = this.getChildAt(i);
			if(child.getVisibility() == GONE) continue;
			int height = child.getMeasuredHeight();
			int column = i%columns;
			child.layout(offsetX[column], sumY[column], offsetX[column] + columnwidth , sumY[column] + height);
			Log.v("CardLayout", "onLayout:" + i + " " + sumY + " " + width + " " + height);
			sumY[column] += height;
		}
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
		int sizeWidth = MeasureSpec.getSize(widthMeasureSpec) - this.getPaddingLeft() - this.getPaddingRight();
		int sizeHeight = MeasureSpec.getSize(heightMeasureSpec) - this.getPaddingTop() - this.getPaddingBottom();
		int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
		int modeHeight = MeasureSpec.getMode(heightMeasureSpec);
		
		int size;
		int mode;
		
		if(orientation == HORIZONTAL){
			size = sizeWidth;
			mode = modeWidth;
		}
		else{
			size = sizeHeight;
			mode = modeHeight;
		}

		int width = sizeWidth;
		int columnwidth = width / columns;
		
		final int count = getChildCount();
		int sumX = 0;
		int sumY[] = new int[columns];
		for(int n : sumY) n = 0;
		
		for (int i=0; i<count; i++){
			final View child = getChildAt(i);
			final int column = i%columns;
			if(child.getVisibility() == GONE) continue;
			int childWidthSpec = 0;
			int childHeightSpec = 0;
			LayoutParams lp = (LayoutParams) child.getLayoutParams();

			switch(lp.width){
				case LayoutParams.MATCH_PARENT: childWidthSpec = MeasureSpec.makeMeasureSpec(columnwidth, MeasureSpec.EXACTLY);break;
				case LayoutParams.WRAP_CONTENT: childWidthSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);break;
				default: childWidthSpec = MeasureSpec.makeMeasureSpec(lp.width, MeasureSpec.EXACTLY);break;
			}
			switch(lp.height){
				case LayoutParams.MATCH_PARENT: childHeightSpec = MeasureSpec.makeMeasureSpec(sizeHeight, MeasureSpec.EXACTLY);break;
				case LayoutParams.WRAP_CONTENT: childHeightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);break;
				default: childHeightSpec = MeasureSpec.makeMeasureSpec(lp.height, MeasureSpec.EXACTLY);break;
			}

			child.measure(childWidthSpec, childHeightSpec);
			//sumX += child.getMeasuredWidth();
			sumY[column] += child.getMeasuredHeight();

;		}
		
		int maxY = 0;
		for(int n : sumY) if(maxY < n) maxY = n;
		setMeasuredDimension(width, maxY);
		
		//super.onMeasure(widthMeasureSpec, heightMeasureSpec);

	}
	
	public void Initialize(Context c, AttributeSet attrs){
		TypedArray ta = c.obtainStyledAttributes(attrs, R.styleable.CardLayout);
		try{
			orientation = ta.getInteger(R.styleable.CardLayout_orientation, HORIZONTAL);
			columns = ta.getInteger(R.styleable.CardLayout_columns, 1);
		}
		finally{
			ta.recycle();
		}
	}
	
	
    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return new LayoutParams(getContext(), attributeSet);
    }

    @Override
    protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }
	
	public static class LayoutParams extends ViewGroup.MarginLayoutParams{
		
		int marginT,marginR,marginB,marginL;
		int width,height;
		int gravity;
		
		public LayoutParams(Context context, AttributeSet attrs){
			super(context, attrs);
			Initialize(context, attrs);
		}
		
		public LayoutParams(int width, int height){
			super(width,height);
		}
		public LayoutParams(ViewGroup.LayoutParams layoutParams){
			super(layoutParams);
		}
		
		private void Initialize(Context c,AttributeSet attrs){
			TypedArray ta = c.obtainStyledAttributes(attrs, R.styleable.CardLayout_LayoutParams);
			try{
				marginT = ta.getDimensionPixelOffset(R.styleable.CardLayout_LayoutParams_layout_marginTop, 0);
				marginR = ta.getDimensionPixelOffset(R.styleable.CardLayout_LayoutParams_layout_marginRight, 0);
				marginB = ta.getDimensionPixelOffset(R.styleable.CardLayout_LayoutParams_layout_marginBottom, 0);
				marginL = ta.getDimensionPixelOffset(R.styleable.CardLayout_LayoutParams_layout_marginBottom, 0);
				height = ta.getLayoutDimension(R.styleable.CardLayout_LayoutParams_layout_height, LayoutParams.WRAP_CONTENT);
				width = ta.getLayoutDimension(R.styleable.CardLayout_LayoutParams_layout_width, LayoutParams.MATCH_PARENT);
			}finally{
				ta.recycle();
			}
		}
	}

}
