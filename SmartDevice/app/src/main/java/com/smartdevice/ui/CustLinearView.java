package com.smartdevice.ui;

import com.smartdevice.main.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CustLinearView extends LinearLayout{
	
	private ImageView iconView;
	private TextView titleView;
	private TextView contentView;
	private ImageView indicatorView;
	private Drawable iconImage;
	private String titleTxt;
	private Drawable indicator;
	private String content;
	private View container;

	public CustLinearView(Context context) {
		//super(context);
		// TODO Auto-generated constructor stub
		this(context, null);
	}
	
	public CustLinearView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		container = inflater.inflate(R.layout.cust_linear_view_layout, this);
	    iconView = (ImageView) container.findViewById(R.id.id_cust_icon_view);
	    titleView = (TextView) container.findViewById(R.id.id_cust_title_view);
	    contentView = (TextView) container.findViewById(R.id.id_cust_content_view);
	    indicatorView = (ImageView) container.findViewById(R.id.id_cust_indicator_view);
		
		TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CustLinearView);
		String titleText = array.getString(R.styleable.CustLinearView_titleTxt);
		Log.i("CustLinearView", "CustLinearView title " + titleText);
	   
	    String contentText = array.getString(R.styleable.CustLinearView_content);
	    Drawable iconDrawable = array.getDrawable(R.styleable.CustLinearView_iconImage);
	    Drawable indicatorDrawable = array.getDrawable(R.styleable.CustLinearView_indicator);
	    Log.i("CustLinearView", "CustLinearView indicatorDrawable " + indicatorDrawable);
	    int iconVisible = array.getInt(R.styleable.CustLinearView_iconVisible, View.GONE);
	    int indicatorVisible = array.getInt(R.styleable.CustLinearView_indicatorVisible, View.GONE);
	    Log.i("CustLinearView", "CustLinearView iconVisible " + iconVisible);
	    int contentVisible = array.getInt(R.styleable.CustLinearView_contentVisible, View.GONE);
	    
	    iconView.setVisibility(iconVisible);
	    indicatorView.setVisibility(indicatorVisible);
	    contentView.setVisibility(contentVisible);
	    if(iconDrawable != null)
	    iconView.setImageDrawable(iconDrawable);
	    if(titleText != null)
	    titleView.setText(titleText);
	    if(contentText != null)
	    contentView.setText(contentText);
	    if(indicatorDrawable != null)
	    indicatorView.setImageDrawable(indicatorDrawable);
	    array.recycle();
	}

	
	public void setTitleTxt(String title){
		titleView.setText(title);
	}

	public Drawable getIconImage() {
		return iconImage;
	}

	public void setIconImage(Drawable iconImage) {
		this.iconImage = iconImage;
	}

	public Drawable getIndicator() {
		return indicator;
	}

	public void setIndicator(Drawable indicator) {
		this.indicator = indicator;
	}

	public String getContent() {
		return contentView.getText().toString();
	}

	public void setContent(String content) {
		contentView.setText(content);
	}
	
	public void setContentColor(int resId){
		contentView.setTextColor(resId);
	}

	public String getTitleTxt() {
		return titleTxt;
	}

	public ImageView getIconView() {
		return iconView;
	}

	public void setIconView(ImageView iconView) {
		this.iconView = iconView;
	}

	public TextView getTitleView() {
		return titleView;
	}

	public void setTitleView(TextView titleView) {
		this.titleView = titleView;
	}

	public TextView getContentView() {
		return contentView;
	}

	public void setContentView(TextView contentView) {
		this.contentView = contentView;
	}

	public ImageView getIndicatorView() {
		return indicatorView;
	}

	public void setIndicatorView(ImageView indicatorView) {
		this.indicatorView = indicatorView;
	}

	public View getContainer() {
		return container;
	}
}
