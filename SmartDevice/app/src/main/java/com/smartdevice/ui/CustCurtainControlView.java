package com.smartdevice.ui;

import com.smartdevice.main.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CustCurtainControlView extends LinearLayout{

	private LinearLayout container;
	private TextView openView;
	private TextView closeView;
	private TextView stopView;
	
	public CustCurtainControlView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		//LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.inflate(context, R.layout.curtain_control_button_layout, this);
		openView = (TextView) findViewById(R.id.id_curtain_button_open);
		closeView = (TextView) findViewById(R.id.id_curtain_button_close);;
		stopView = (TextView) findViewById(R.id.id_curtain_button_stop);;
	}

	public TextView getOpenView() {
		return openView;
	}

	public TextView getCloseView() {
		return closeView;
	}

	public TextView getStopView() {
		return stopView;
	}

}
