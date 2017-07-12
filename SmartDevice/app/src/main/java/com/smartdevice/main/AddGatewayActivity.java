package com.smartdevice.main;

import org.json.JSONException;
import org.json.JSONObject;

import com.smartdevice.control.GatewayControl;
import com.smartdevice.mode.Session;
import com.smartdevice.utils.ACache;
import com.smartdevice.utils.CustActionBar;
import com.smartdevice.utils.Params;
import com.smartdevice.utils.ParamsUtils;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AddGatewayActivity extends ActionBarActivity{

	private final static String TAG = AddGatewayActivity.class.getSimpleName();
	View bindbarView;
	TextView bindHintView;
	EditText gatewayView;
	String gatewayId;
	int gatewayCount;
	BindHandler handler;
	TextView addView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		init();
		addView = (TextView) findViewById(R.id.id_submit_view);
		addView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				gatewayId = gatewayView.getEditableText().toString();
				Log.i(TAG, "gatewayId " + gatewayId.trim());
				gatewayId = gatewayId.trim();
				if(gatewayId.trim().isEmpty()){
					bindHintView.setVisibility(View.VISIBLE);
					bindbarView.setVisibility(View.GONE);
					bindHintView.setText(R.string.bind_gateway_none);
					bindHintView.setTextColor(getResources().getColor((R.color.red)));
				}else {
					setHintVisible(true);
					new Thread(new BindRunnale()).start();
				}
			}
		});
	}
	
	private void init(){
		setContentView(R.layout.add_gateway_layout);
		CustActionBar actionBar = new CustActionBar(getSupportActionBar());
		actionBar.setTitle(R.string.add_gateway);
		actionBar.setIconOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		bindbarView = findViewById(R.id.id_binding_bar);
		bindHintView = (TextView) findViewById(R.id.id_bind_gateway_hint);
		gatewayView = (EditText) findViewById(R.id.id_gateway_id);
		
		if(Session.getUsername() == null){
			SharedPreferences userInfoPreferences = this.getSharedPreferences(ParamsUtils.USER_INFO, MODE_PRIVATE);
			Session.setUsername(userInfoPreferences.getString(ParamsUtils.USER_NAME, null));
		}
		handler = new BindHandler();
	}
	
	private void setHintVisible(boolean enable){
		if(enable){
			bindbarView.setVisibility(View.VISIBLE);
			bindHintView.setVisibility(View.VISIBLE);
			addView.setEnabled(false);
			gatewayView.setEnabled(false);
		}else {
			bindbarView.setVisibility(View.GONE);
			addView.setEnabled(true);
			gatewayView.setEnabled(true);
		}
	}
	
	class BindRunnale implements Runnable{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			Message msg = new Message();
			String result = new String();
			GatewayControl control = new GatewayControl(Params.base_uri + Params.bind_gateway);
			try {
				Log.i(TAG, "gatewayId " + gatewayId);
				Log.i(TAG, "username " + Session.getUsername());
				result = control.bind(gatewayId, Session.getUsername());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			msg.obj = result;
		    handler.sendMessage(msg);

		}
		
	}
	
	class BindHandler extends Handler{
		
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if(msg.obj != null){
				try {
					String respose = (String) msg.obj;
					JSONObject object = new JSONObject(respose);
					int status = object.getInt("status");
					int code = object.getInt("code");
					if(status == ParamsUtils.SUCCESS_CODE){
						/*Bind successfully, then save gateway information on the device*/
						ACache mCache = ACache.get(getApplicationContext(), 
								ParamsUtils.CACHE_SIZE, 1000); 
						SharedPreferences preferences = getApplicationContext().getSharedPreferences(ParamsUtils.GATEWAY_COUNT, MODE_PRIVATE);
						gatewayCount = preferences.getInt(ParamsUtils.GATEWAY_COUNT_CURRENT, 0);
						gatewayCount = gatewayCount + 1;
						mCache.put("gateway" + gatewayCount, gatewayId);
						Editor editor = preferences.edit();
						editor.putInt(ParamsUtils.GATEWAY_COUNT_CURRENT, gatewayCount);
						editor.commit();
						setHintVisible(false);
						bindHintView.setText(R.string.bind_ok);
						
						Log.i(AddGatewayActivity.class.getSimpleName(), "sendBroadcast");
						Intent intent = new Intent("com.smartdevice.main.device.update"); 

						sendBroadcast(intent);
						
						
					}else {
						setHintVisible(false);
						bindHintView.setText(R.string.bind_fail);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					setHintVisible(false);
					bindHintView.setText(R.string.bind_fail);
				}
			}else {
				setHintVisible(false);
				bindHintView.setText(R.string.bind_fail);
			}
			
		}
	}

}
