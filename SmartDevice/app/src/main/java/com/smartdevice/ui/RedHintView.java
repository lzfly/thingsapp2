package com.smartdevice.ui;

import com.smartdevice.main.R;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class RedHintView extends FrameLayout{
	
	private TextView hintView;
	private ImageView hostView;
	private String hints;
    private View container;
	
	public RedHintView(Context context) {
		//super(context);
		// TODO Auto-generated constructor stub
	    this(context, null);	 
	}
	
	public RedHintView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		 LayoutInflater inflater = LayoutInflater.from(context);
		 container = inflater.inflate(R.layout.red_hint_layout, this);
         hintView = (TextView) container.findViewById(R.id.id_hint_nunber);
         hostView = (ImageView) container.findViewById(R.id.id_host_img_view);
         
         hintView.setVisibility(View.INVISIBLE);
	}

	public void setHints(String hints){
		if(hintView.getVisibility() != View.VISIBLE)
			hintView.setVisibility(View.VISIBLE);
	
		hintView.setText(hints);
	}
	
	public void setHostImageDrawable(Drawable drawable){
		hostView.setImageDrawable(drawable);
	}
	
	public void setHostImageResource(int resId){
		hostView.setImageResource(resId);
	}
	
	public void invisbleHintView(){
		hintView.setVisibility(View.INVISIBLE);
	}
	
	public void setHintViewVisibility(int visible){
		hintView.setVisibility(visible);
	}
}
