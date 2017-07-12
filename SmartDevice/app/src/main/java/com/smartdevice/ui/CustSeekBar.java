package com.smartdevice.ui;

import com.smartdevice.main.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class CustSeekBar{
	
	private SeekBar mSeekBar;
	private ImageView iconView;
	private TextView nameView;
	private TextView currentValueView;
	private TextView minView;
	private TextView maxView;
	
	private float minValue;
	private float maxValue;
	private float currentValue;
	private View container;
	
	public CustSeekBar(Context context){
		this(context, 0, 0.0f, 100.0f);
	}
	
	public CustSeekBar(Context context, int resIconId, 
			float minValue, float maxValue){
		LayoutInflater inflater = LayoutInflater.from(context);
		container = inflater.inflate(R.layout.cust_seekbar_layout, null);
		mSeekBar = (SeekBar) container.findViewById(R.id.id_cust_seekbar);
		iconView = (ImageView) container.findViewById(R.id.id_cust_seekbar_icon);
		nameView = (TextView) container.findViewById(R.id.id_cust_seekbar_name);
		currentValueView = (TextView) container.findViewById(R.id.id_cust_seekbar_currentvalue);
		minView = (TextView) container.findViewById(R.id.id_cust_seekbar_minvalue);
		maxView = (TextView) container.findViewById(R.id.id_cust_seekbar_maxvalue);
		
		this.minValue = minValue;
		this.maxValue = maxValue;
		minView.setText(String.valueOf(minValue));
		maxView.setText(String.valueOf(maxValue));
		mSeekBar.setMax((int)maxValue);
		if(resIconId != 0){
			iconView.setImageResource(resIconId);
		}
	}
	
	public void setMax(int max){
		mSeekBar.setMax(max);
	}
	
	public View createSeekBar(){
		return container;
	}
	
	public void setName(String name){
		nameView.setVisibility(View.VISIBLE);
		nameView.setText(name);
	}
	
	public void setIcon(int resId){
		iconView.setImageResource(resId);
	}
	
	public void setProgress(float progress){
		mSeekBar.setProgress((int)progress);
	}
	
	public void setCurrentValue(float value){
		currentValue = value;
		currentValueView.setText(String.valueOf(currentValue));
	}
	
	public void setOnSeekBarChangeListener(OnSeekBarChangeListener l){
		mSeekBar.setOnSeekBarChangeListener(l);
	}
	
	public void setVisibility(int visible){
		container.setVisibility(visible);
	}

	public TextView getMinView() {
		return minView;
	}

	public void setMinView(TextView minView) {
		this.minView = minView;
	}

	public SeekBar getmSeekBar() {
		return mSeekBar;
	}

	public ImageView getIconView() {
		return iconView;
	}

	public TextView getNameView() {
		return nameView;
	}

	public TextView getMaxView() {
		return maxView;
	}	
	
}
