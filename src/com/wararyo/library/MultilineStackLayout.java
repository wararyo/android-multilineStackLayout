package com.wararyo.library;

import com.wararyo.library.multilinestacklayout.R;

import android.R.anim;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.test.suitebuilder.annotation.MediumTest;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewDebug.ExportedProperty;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class MultilineStackLayout extends ViewGroup {

	int morientation;
	int mcolumns;
	int mpaddingTop,mpaddingRight,mpaddingBottom,mpaddingLeft;
	int mspacewidth;
	
	final int HORIZONTAL = 0;
	final int VERTICAL = 1;
	
	final int COLUMNS_MAX = 32;
	
	public MultilineStackLayout(Context context) {
		super(context);
		Initialize(context, null);
		// TODO Auto-generated constructor stub
	}
	public MultilineStackLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		Initialize(context, attrs);
		// TODO Auto-generated constructor stub
	}
	@SuppressLint("NewApi")
	public MultilineStackLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		Initialize(context, attrs);
		// TODO Auto-generated constructor stub
	}
	
	
	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		int count = this.getChildCount();
		//if(count == 0) return;
		
		int width = getWidth() - mpaddingLeft - mpaddingRight;
		int columnwidth = width / mcolumns;
		int sumX[] = new int[mcolumns];
		int sumY[]= new int[mcolumns];
		
		int offsetX[] = new int[mcolumns];
		
		for(int n : sumX){
			n = 0;
		}
		for(int n : sumY){
			n = 0;
		}
		for(int i=0;i<mcolumns;i++){
			offsetX[i] = (i * width)/mcolumns;
		}
		for(int i=0; i<count; i++){
			View child = this.getChildAt(i);
			LayoutParams lp = (LayoutParams) child.getLayoutParams();
			if(child.getVisibility() == GONE) continue;
			int height = child.getMeasuredHeight();
			int column = i%mcolumns;
			child.layout(offsetX[column] + mpaddingLeft + mspacewidth, sumY[column] + mpaddingTop + mspacewidth, offsetX[column] + columnwidth + mpaddingLeft - mspacewidth, sumY[column] + height + mpaddingTop - mspacewidth);
			Log.v("CardLayout", "onLayout:" + i + " " + sumY[column] + " " + width + " " + height);
			sumY[column] += height;
		}
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
		final int count = getChildCount();
		//if(count == 0) return;

		int sizeWidth = MeasureSpec.getSize(widthMeasureSpec) - this.getPaddingLeft() - this.getPaddingRight();
		int sizeHeight = MeasureSpec.getSize(heightMeasureSpec) - this.getPaddingTop() - this.getPaddingBottom();
		int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
		int modeHeight = MeasureSpec.getMode(heightMeasureSpec);
		
		int size;
		int mode;
		
		mpaddingTop = getPaddingTop();
		mpaddingRight = getPaddingRight();
		mpaddingBottom = getPaddingBottom();
		mpaddingLeft = getPaddingLeft();
		
		if(morientation == HORIZONTAL){
			size = sizeWidth;
			mode = modeWidth;
		}
		else{
			size = sizeHeight;
			mode = modeHeight;
		}

		int width = sizeWidth;
		int columnwidth = width / mcolumns;
		
		int itemwidth = columnwidth - (2*mspacewidth);

		
		int sumX = 0;
		int sumY[] = new int[mcolumns];
		for(int n : sumY) n = 0;
		
		for (int i=0; i<count; i++){
			final View child = getChildAt(i);
			final int column = i%mcolumns;
			if(child.getVisibility() == GONE) continue;
			int childWidthSpec = 0;
			int childHeightSpec = 0;
			LayoutParams lp = (LayoutParams) child.getLayoutParams();

			switch(lp.width){
				case LayoutParams.MATCH_PARENT: childWidthSpec = MeasureSpec.makeMeasureSpec(itemwidth - (2*mspacewidth), MeasureSpec.EXACTLY);break;
				case LayoutParams.WRAP_CONTENT: childWidthSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);break;
				default: childWidthSpec = MeasureSpec.makeMeasureSpec(lp.width, MeasureSpec.EXACTLY);break;
			}
			switch(lp.height){
				case LayoutParams.MATCH_PARENT: childHeightSpec = MeasureSpec.makeMeasureSpec(sizeHeight - (2*mspacewidth), MeasureSpec.EXACTLY);break;
				case LayoutParams.WRAP_CONTENT: childHeightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);break;
				default: childHeightSpec = MeasureSpec.makeMeasureSpec(lp.height, MeasureSpec.EXACTLY);break;
			}

			child.measure(childWidthSpec, childHeightSpec);
			//sumX += child.getMeasuredWidth();
			sumY[column] += child.getMeasuredHeight();

;		}
		
		int maxY = 0;
		for(int n : sumY) if(maxY < n) maxY = n;
		setMeasuredDimension(width + mpaddingLeft + mpaddingRight, maxY + mpaddingTop + mpaddingBottom + mspacewidth);
		
		//super.onMeasure(widthMeasureSpec, heightMeasureSpec);

	}
	
	public void setColumns(int columns){
		mcolumns = columns;
		VerifyProperties();
		requestLayout();
	}
	public int getColumns(){
		return mcolumns;
	}

	public void setOrientation(int orientation){
		morientation = orientation;
		VerifyProperties();
		requestLayout();
	}
	public int getOrientaiton(){
		return morientation;
	}
	
	public void Initialize(Context c, AttributeSet attrs){
		TypedArray ta = c.obtainStyledAttributes(attrs, R.styleable.MultilineStackLayout);
		try{
			morientation = ta.getInteger(R.styleable.MultilineStackLayout_orientation, HORIZONTAL);
			mcolumns = ta.getInteger(R.styleable.MultilineStackLayout_columns, 1);
			mspacewidth = ta.getDimensionPixelSize(R.styleable.MultilineStackLayout_space_width, 0);
			Log.v("MultilineStackLayout", "Initialize:" + mspacewidth);
		}
		finally{
			ta.recycle();
		}
		VerifyProperties();
	}
	
	private void VerifyProperties(){
		
		if(mcolumns < 1) mcolumns = 1;
		else if(mcolumns > COLUMNS_MAX) mcolumns = COLUMNS_MAX;
		
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
		
		@ExportedProperty(category = "layout")
		int width = LayoutParams.MATCH_PARENT;
		
		@ExportedProperty(category = "layout")
		int height = LayoutParams.WRAP_CONTENT;

		int gravity;
		
		public LayoutParams(Context context, AttributeSet attrs){
			super(context, attrs);
			Initialize(context, attrs);
		}
		
		public LayoutParams(int width, int height){
			super(width,height);
			this.width = width;
			this.height = height;
		}
		public LayoutParams(ViewGroup.LayoutParams layoutParams){
			super(layoutParams);
		}
		
		private void Initialize(Context c,AttributeSet attrs){
			TypedArray ta = c.obtainStyledAttributes(attrs, R.styleable.MultilineStackLayout_MarginLayout);
			try{
				marginT = ta.getDimensionPixelOffset(R.styleable.MultilineStackLayout_MarginLayout_android_layout_marginTop, 0);
				marginR = ta.getDimensionPixelOffset(R.styleable.MultilineStackLayout_MarginLayout_android_layout_marginRight, 0);
				marginB = ta.getDimensionPixelOffset(R.styleable.MultilineStackLayout_MarginLayout_android_layout_marginBottom, 0);
				marginL = ta.getDimensionPixelOffset(R.styleable.MultilineStackLayout_MarginLayout_android_layout_marginBottom, 0);
				height = ta.getLayoutDimension(R.styleable.MultilineStackLayout_MarginLayout_android_layout_height, LayoutParams.WRAP_CONTENT);
				width = ta.getLayoutDimension(R.styleable.MultilineStackLayout_MarginLayout_android_layout_width, LayoutParams.MATCH_PARENT);
			}finally{
				ta.recycle();
			}
		}
	}

}
