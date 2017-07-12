package com.smartdevice.utils;

import com.smartdevice.main.R;

import android.support.v7.app.ActionBar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class CustActionBar {

	private ActionBar actionBar;
	private ImageView iconView;
	private TextView titleView;
	private ImageView menuView;
	private TextView menuTxView;
	private int titleVisible = View.VISIBLE;
	private int iconVisible = View.VISIBLE;
	private int menuVisible = View.GONE;
	
	public CustActionBar(ActionBar actionBar){
		this.actionBar = actionBar;
		initActionBar();
	}
	
	public CustActionBar(ActionBar actionBar, String title){
		this(actionBar);
		titleView.setText(title);
	}
	
	public void initActionBar(){
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		actionBar.setCustomView(R.layout.action_bar_layout);
		iconView = (ImageView) actionBar.getCustomView().findViewById(R.id.id_actionbar_icon);
		titleView = (TextView) actionBar.getCustomView().findViewById(R.id.id_actionbar_title);
		menuView = (ImageView) actionBar.getCustomView().findViewById(R.id.id_actionbar_menu);
		menuTxView = (TextView) actionBar.getCustomView().findViewById(R.id.id_actionbar_menu_tx);
		menuView.setVisibility(menuVisible);
	}
	
	public void setTitle(int title){
		titleView.setText(title);
	}

	public TextView getTitleView() {
		return titleView;
	}

	public ImageView getMenuView() {
		return menuView;
	}
	
	public ImageView getIconView(){
		return iconView;
	}
	
	public TextView getMenuTxView() {
		return menuTxView;
	}

	public void setTitleVisible(int visible){
		titleVisible = visible;
		titleView.setVisibility(titleVisible);
	}
	
	public void setIconVisible(int visible){
		iconVisible = visible;
		iconView.setVisibility(iconVisible);
	}
	
	public void setMenuVisible(int visible){
		menuView.setVisibility(visible);
	}
	
	public void setIconOnClickListener(OnClickListener l){
		iconView.setOnClickListener(l);
	}
}
