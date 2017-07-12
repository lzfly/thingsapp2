package com.smartdevice.ui;

import com.smartdevice.main.R;

import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class CustActionBarV3 {

	private ActionBar actionBar;
	private TextView iconView;
	private TextView titleView;
	private TextView menuView;
	private int titleVisible = View.VISIBLE;
	private int iconVisible = View.VISIBLE;
	private int menuVisible = View.VISIBLE;
	
	public CustActionBarV3(ActionBar actionBar){
		this.actionBar = actionBar;
		initActionBar();
	}
	
	public CustActionBarV3(ActionBar actionBar, String title){
		this(actionBar);
		titleView.setText(title);
	}
	
	public void initActionBar(){
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		actionBar.setCustomView(R.layout.action_bar_v3_layout);
		iconView = (TextView) actionBar.getCustomView().findViewById(R.id.id_actionbar_v3_icon);
		titleView = (TextView) actionBar.getCustomView().findViewById(R.id.id_actionbar_v3_title);
		menuView = (TextView) actionBar.getCustomView().findViewById(R.id.id_actionbar_v3_menu);
		menuView.setVisibility(menuVisible);
	}
	
	public void setTitle(int title){
		titleView.setText(title);
	}

	public TextView getTitleView() {
		return titleView;
	}

	public TextView getMenuView() {
		return menuView;
	}
	
	public TextView getIconView(){
		return iconView;
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
}
