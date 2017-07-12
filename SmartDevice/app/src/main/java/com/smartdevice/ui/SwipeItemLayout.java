package com.smartdevice.ui;

import com.smartdevice.utils.LogUtil;

import android.content.Context;
import android.support.v4.widget.ScrollerCompat;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.AbsListView;
import android.widget.FrameLayout;

public class SwipeItemLayout extends FrameLayout{
	private static final String TAG = SwipeItemLayout.class.getSimpleName();
	private View contentView;
	private View menuView;
	private Interpolator closeInterpolator;
	private Interpolator openInterpolator;
	
	private ScrollerCompat mOpenScroller;
	private ScrollerCompat mCloseScroller;
	
	private static final int STATE_CLOSE = 0;
	private static final int STATE_OPEN = 1;
	private int state = STATE_CLOSE;
	
	private int mDownX;
	private int mBaseX;

	public SwipeItemLayout(View contentView,View menuView,Interpolator closeInterpolator, Interpolator openInterpolator){
		super(contentView.getContext());
		this.contentView = contentView;
		this.menuView = menuView;
		this.closeInterpolator = closeInterpolator;
		this.openInterpolator = openInterpolator;
		
		init();
	}
	
	private void init(){
		setLayoutParams(new AbsListView.LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));
		if (closeInterpolator != null) {
			mCloseScroller = ScrollerCompat.create(getContext(),
					closeInterpolator);
		} else {
			mCloseScroller = ScrollerCompat.create(getContext());
		}
		if (openInterpolator != null) {
			mOpenScroller = ScrollerCompat.create(getContext(),
					openInterpolator);
		} else {
			mOpenScroller = ScrollerCompat.create(getContext());
		}
		
		LayoutParams contentParams = new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		contentView.setLayoutParams(contentParams);

		menuView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.MATCH_PARENT));
		
		addView(menuView);
		addView(contentView);
	}
	
	public boolean isOpen(){
		return state == STATE_OPEN;
	}
	
	public boolean onSwipe(MotionEvent event){
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mDownX = (int) event.getX();
			LogUtil.i(TAG, "ACTION_DOWN mDownX " + mDownX);
			break;
		case MotionEvent.ACTION_MOVE:
			int dis = (int) (mDownX - event.getX());
			LogUtil.i(TAG, "ACTION_MOVE dis " + dis);
			if (state == STATE_OPEN) {
				dis += menuView.getWidth();
			}
			swipe(dis);
			break;
		case MotionEvent.ACTION_UP:
			LogUtil.i(TAG, "ACTION_UP");
			if((mDownX - event.getX()) > (menuView.getWidth() / 2)){
				/*open menu*/
				smoothOpenMenu();
			}else {
				/*close menu*/
				smoothCloseMenu();
				return false;
			}
			break;
		default:
			break;
		}
		return true;
	}
	
	private void swipe(int dis){
		if (dis > menuView.getWidth()) {
			dis = menuView.getWidth();
		}
		if (dis < 0) {
			dis = 0;
		}
		
		contentView.layout(-dis, contentView.getTop(),
				contentView.getWidth() - dis, getMeasuredHeight());
		menuView.layout(contentView.getWidth() - dis, menuView.getTop(),
				contentView.getWidth() + menuView.getWidth() - dis,
				menuView.getBottom());
	}
	
	@Override
	public void computeScroll() {
		if(state == STATE_OPEN){
			if(mOpenScroller.computeScrollOffset()){
				swipe(mOpenScroller.getCurrX());
				postInvalidate();
			}
		}else {
			if (mCloseScroller.computeScrollOffset()) {
				swipe(mBaseX - mCloseScroller.getCurrX());
				postInvalidate();
			}
		}
	}
	
	public void smoothOpenMenu(){
		state = STATE_OPEN;
		mOpenScroller.startScroll(-contentView.getLeft(), 0, 
				menuView.getWidth(), 0);
		postInvalidate();
	}
	
	public void smoothCloseMenu(){
		state = STATE_CLOSE;
		mBaseX = -contentView.getLeft();
		mCloseScroller.startScroll(0, 0, mBaseX, 0);
		postInvalidate();
	}
	
    public void closeMenu() {
		if(mCloseScroller.computeScrollOffset()){
			mCloseScroller.abortAnimation();
		}
		if(state == STATE_OPEN){
			state = STATE_CLOSE;
			swipe(0);
		}
	}
    
    public void openMenu() {
		if (state == STATE_CLOSE) {
			state = STATE_OPEN;
			swipe(menuView.getWidth());
		}
	}
    
    public View getContentView() {
		return contentView;
	}

	public View getMenuView() {
		return menuView;
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		menuView.measure(MeasureSpec.makeMeasureSpec(0,
				MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(
				getMeasuredHeight(), MeasureSpec.EXACTLY));
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		contentView.layout(0, 0, getMeasuredWidth(),
				contentView.getMeasuredHeight());
		menuView.layout(getMeasuredWidth(), 0,
				getMeasuredWidth() + menuView.getMeasuredWidth(),
				contentView.getMeasuredHeight());
	}
}
