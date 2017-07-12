package com.smartdevice.utils;

import com.smartdevice.main.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class SwitchView extends View implements View.OnTouchListener{

	private boolean isChoosed = false;
	private boolean isOnSlip = false;
	private boolean isChgLsnOn = false;
	private float downX, nowX; //按下时的x,当前的x, 
	private Rect btnOn, btnOff; //打开和关闭状态下,游标的Rect  
	private OnChangedListener changedListener;
	private Bitmap bgOn, bgOff, bgSlip;
	
	public SwitchView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init();
	}
	
	//此构造函数在xml中使用控件时调用  
    public SwitchView(Context context, AttributeSet attrs) {  
        super(context, attrs);  
        // TODO Auto-generated constructor stub  
        init();  
    }
	
	private void init(){
		bgOn = BitmapFactory.decodeResource(getResources(), R.drawable.icon_switch_green);
		bgOff = BitmapFactory.decodeResource(getResources(), R.drawable.icon_switch_gray);
		bgSlip = BitmapFactory.decodeResource(getResources(), R.drawable.icon_half_switch_gray);
		
		btnOn = new Rect(0, 0, bgSlip.getWidth(), bgSlip.getHeight());
		btnOff = new Rect(bgOff.getWidth()-bgSlip.getWidth(),
				0,
				bgOff.getWidth(),
				bgSlip.getHeight());
		
		setOnTouchListener(this);
	}
	
	public boolean isChecked(){  
        return isChoosed;  
    }  
   
    public void setChecked(boolean check){  
        isChoosed = check;  
        invalidate();  
    }  
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		Matrix matrix = new Matrix();
		Paint paint = new Paint();
		float x;
		
		if((nowX < (bgOn.getWidth()/2)) && !isChoosed){
			canvas.drawBitmap(bgOff, matrix, paint);  //画出关闭时的背景
		}else{
			canvas.drawBitmap(bgOn, matrix, paint);// 画出打开时的背景 
		}
		
		if (isOnSlip)// 是否是在滑动状  
        {  
            if (nowX >= bgOn.getWidth())// 是否划出指定范围,不能让游标跑到绘制控件范围外?必须做这个判?  
                x = bgOn.getWidth() - bgSlip.getWidth() / 2;// 减去游标1/2的长度  
            else  
                x = nowX - bgSlip.getWidth() / 2;  
        } else {// 非滑动状  
            if (isChoosed)// 根据现在的开关状态设置画游标的位  
                x = btnOff.left;  
            else  
                x = btnOn.left;  
        }  
        if (x < 0)// 对游标位置进行异常判  
            x = 0;  
        else if (x > bgOn.getWidth() - bgSlip.getWidth())  
            x = bgOn.getWidth() - bgSlip.getWidth();  
        canvas.drawBitmap(bgSlip, x, 0, paint);// 画出游标.  
	}

	@Override
	 public boolean onTouch(View v, MotionEvent event) {  
        // TODO Auto-generated method stub  
        switch (event.getAction())// 根据动作来执行代  
        {  
        case MotionEvent.ACTION_MOVE:// 滑动  
            nowX = event.getX();  
            break;  
        case MotionEvent.ACTION_DOWN:// 按下  
            if (event.getX() > bgOn.getWidth()  
                    || event.getY() > bgOn.getHeight())  
                return false;  
            isOnSlip = true;  
            downX = event.getX();  
            nowX = downX;  
            break;  
        case MotionEvent.ACTION_UP:// 松开  
        	isOnSlip = false;  
            boolean LastChoose = isChoosed;  
            if (event.getX() >= (bgOn.getWidth() / 2))  
            	isChoosed = true;  
            else  
            	isChoosed = false;  
            if (isChgLsnOn && (LastChoose != isChoosed))// 如果设置了监听器,就调用其方法..  
                changedListener.OnChanged(isChoosed);  
            break;  
        default:  
   
        }  
        invalidate();// 重画控件  
        return true;  
    }  
	
	public void SetOnChangedListener(OnChangedListener listener) {// 设置监听?当状态修改的时?  
        isChgLsnOn = true;  
        changedListener = listener;  
    } 

}
