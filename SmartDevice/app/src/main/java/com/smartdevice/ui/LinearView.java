package com.smartdevice.ui;

import com.smartdevice.main.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class LinearView{

	private LinearLayout parentLayout;
	private ImageView iconView;
	private TextView titleView;
	private TextView contentView;
	private ImageView indicatorView;
	private Context context;
	private View infoView;
	private OnClickListener listener;
	private String title;
	private String content;
	
	public LinearView(Context context){
		this.context = context;
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.linear_view_layout, null);
		parentLayout = (LinearLayout) view.findViewById(R.id.id_linear_layout_view);
		infoView = view.findViewById(R.id.id_info_view);
		iconView = (ImageView) view.findViewById(R.id.id_icon_img_view);
		titleView = (TextView) view.findViewById(R.id.id_info_title_view);
		contentView = (TextView) view.findViewById(R.id.id_info_content_view);
		indicatorView = (ImageView) view.findViewById(R.id.id_indicator_view);
		
	}
	
	public LinearView(Context context, int icon, int title){
		this(context);
		iconView.setImageResource(icon);
		titleView.setText(title);
	}
	
	public int getInfoViewId(){
		return infoView.getId();
	}
	
	public void setOnClickListener(OnClickListener listener){
		this.listener = listener;
		parentLayout.setOnClickListener(listener);
	}
	
	public void invisibleIndicator(){
		indicatorView.setVisibility(View.INVISIBLE);
	}
	
	public void disableIcon(){
		iconView.setVisibility(View.GONE);
	}
	
	public void setIconVisibility(int visible) {
		iconView.setVisibility(visible);
	}
	
	public LinearLayout createLinearView(){
		return parentLayout;
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

	public ImageView getIndicatorView() {
		return indicatorView;
	}

	public void setIndicatorView(ImageView indicatorView) {
		this.indicatorView = indicatorView;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		titleView.setText(title);
	}
	
	public String getContent(){
		return content;
	}
	
	public void setContent(String content){
		this.content = content;
		contentView.setText(content);
	}
	
	public void setIndicator(int resId){
		indicatorView.setImageResource(resId);
	}
	
	public void setIcon(int resId){
		iconView.setImageResource(resId);
	}
	
	public void setTitlePaddingLeft(int pix) {
		titleView.setPadding(pix, 0, 0, 0);
	}
}
