package com.smartdevice.main;

import java.io.UnsupportedEncodingException;

import org.json.JSONException;
import org.json.JSONObject;

import com.smartdevice.control.LoginControl;
import com.smartdevice.utils.Network;
import com.smartdevice.utils.Params;
import com.smartdevice.utils.ParamsUtils;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;

public class WelcomeActivity extends Activity{

	private String username;
	private String password;
	private String sessionid;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome_layout);
		SharedPreferences preferences = getApplicationContext().getSharedPreferences(ParamsUtils.USER_INFO, MODE_PRIVATE);
		username = preferences.getString(ParamsUtils.USER_NAME, null);
		password = preferences.getString(ParamsUtils.PASSWORD, null);
		sessionid = preferences.getString(ParamsUtils.SESSIONID, null);
		
		if((username == null) || (password == null) || (sessionid == null)){
			new Handler().postDelayed(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
					startActivity(intent);
					//have to delay to avaid screen flashing
					//SystemClock.sleep(1000);
					new Handler().postDelayed(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							onDestroy();
						}
					}, 1000);
					
				}
			}, ParamsUtils.WELCOME_VIEW_DELAY);
			
		}else {
			new Handler().postDelayed(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					Network network = new Network(getApplicationContext());
					if(network.isConnected()){
						new Thread( new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								System.out.println("username " + username);
								System.out.println("sessionid " + sessionid);
								Message msg = new Message();
								LoginControl control = new LoginControl(Params.base_uri + Params.session_uri);
								try {
									String respose = control.login(username, password, sessionid);
									msg.obj = respose;
									handler.sendMessage(msg);
								} catch (UnsupportedEncodingException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								
								
								
							}
						}).start();
					}else {
						//...Not have network
						Intent intent = new Intent(getApplicationContext(), MainActivity.class);
						startActivity(intent);
					}
				}
			}, ParamsUtils.WELCOME_VIEW_DELAY);
		}
	}
	
	private Handler handler = new Handler(){
		
		@Override
		public void handleMessage(android.os.Message msg) {
			String respose = (String) msg.obj;
			if(respose != null){
				JSONObject resposeJson;
				try {
					resposeJson = new JSONObject(respose);
					int status = resposeJson.getInt("status");
					//if()...
					if(status == ParamsUtils.SUCCESS_CODE){
						Intent intent = new Intent(getApplicationContext(), MainActivity.class);
						startActivity(intent);
					}else {
						Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
						startActivity(intent);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else {
				Intent intent = new Intent(getApplicationContext(), MainActivity.class);
				startActivity(intent);
			}
			
			onDestroy();
		};
		
		
	};
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		finish();
	}
}
