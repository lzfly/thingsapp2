package com.smartdevice.utils;


import com.smartdevice.main.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MessageView extends LinearLayout{
	
	private static final String TAG = MessageView.class.getSimpleName();
	private Drawable icon;
	private String title;
	private String date;
	private String content;
	private ImageView mIcon;
	private TextView mTitleView;
	private TextView mDateView;
	private TextView mContentView;
	private LinearLayout mLayout;
	
	public MessageView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		mLayout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.message_layout, null);
		mTitleView = (TextView) mLayout.findViewById(R.id.id_title_view);
		mDateView = (TextView) mLayout.findViewById(R.id.id_date_view);
		mContentView = (TextView) mLayout.findViewById(R.id.id_content_view);
		mIcon = (ImageView) mLayout.findViewById(R.id.id_icon_view);
	}
	
	public MessageView(Context context, AttributeSet attrs) {
		this(context);
		//super(context, attrs);
		// TODO Auto-generated constructor stub
		Log.i(TAG, "MessageView constructor");
		TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.MessageView);
		String title = array.getString(R.styleable.MessageView_titleText);
		Log.i(TAG, "MessageView title " + title);
	    String date = array.getString(R.styleable.MessageView_dateText);
	    String content = array.getString(R.styleable.MessageView_contentText);
	    Drawable icon = array.getDrawable(R.styleable.MessageView_iconDrawble);
	    mIcon.setImageDrawable(icon);
	    mTitleView.setText(title);
	    mDateView.setText(date);
	    mContentView.setText(content);    
	}

}
