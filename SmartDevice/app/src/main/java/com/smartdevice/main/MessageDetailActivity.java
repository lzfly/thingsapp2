package com.smartdevice.main;

import com.smartdevice.mode.Message;
import com.smartdevice.utils.CustActionBar;
import com.smartdevice.utils.LogUtil;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class MessageDetailActivity extends ActionBarActivity implements OnClickListener{

	private TextView contentView;
	private TextView titleView;
	private TextView timeView;
	private TextView devNameView;
	private TextView devTypeView;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
    	// TODO Auto-generated method stub
		Log.i("MessageDetailActivity", "onCreate ");
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.message_detail_layout);
    	init();
    	contentView = (TextView) findViewById(R.id.id_message_detail_content);
    	titleView = (TextView) findViewById(R.id.id_message_detail_title);
    	timeView = (TextView) findViewById(R.id.id_message_detail_time);
    	devNameView = (TextView) findViewById(R.id.id_message_detail_dev_name);
    	devTypeView = (TextView) findViewById(R.id.id_message_detail_dev_type);
    	contentView.setTextColor(Color.GRAY);
    	Intent intent = getIntent();
    	getMessageDetail(intent);
    	
    }
	
	@Override
	protected void onNewIntent(Intent intent) {
		Log.i("MessageDetailActivity", "onNewIntent ");
		super.onNewIntent(intent);
		setIntent(intent);
		getMessageDetail(getIntent());
	}
	
	private void getMessageDetail(Intent intent){
		//String message = intent.getStringExtra("message");
    	//Log.i("MessageDetailActivity", "message " + message);
    	Message message = (Message) intent.getSerializableExtra("com.smartdevice.mode.Message");
    	Log.i("MessageDetailActivity", "message " + message);
    	LogUtil.d("MessageDetailActivity", "message time is " + message.getTime());
    	contentView.setText(message.getContent());
    	timeView.setText(message.getTime());
    	titleView.setText(message.getTitle());
    	devNameView.setText(message.getFromName());
    	devTypeView.setText(message.getFromType());
	}
	
	private void init(){
		CustActionBar actionBar = new CustActionBar(getSupportActionBar());
		actionBar.setTitle(R.string.msg_str);
        actionBar.setIconOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		

		default:
			break;
		}
	}
	
	
}
