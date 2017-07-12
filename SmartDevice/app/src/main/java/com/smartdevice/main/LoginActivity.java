package com.smartdevice.main;

import java.io.UnsupportedEncodingException;

import org.json.JSONException;
import org.json.JSONObject;

import com.smartdevice.control.LoginControl;
import com.smartdevice.mode.Session;
import com.smartdevice.utils.Params;
import com.smartdevice.utils.ParamsUtils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class LoginActivity extends Activity{
	
	private TextView loginView;
	private EditText usernameView;
	private EditText passwordView;
	private LinearLayout registerView;
	private TextView findbackpwdView;
	private ProgressBar progressBar;
	private String username;
	private String password;
	private TextView loginHintView;
	private SharedPreferences userInfoPreferences;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_layout);
		
		userInfoPreferences = this.getSharedPreferences(ParamsUtils.USER_INFO, MODE_PRIVATE);
		loginView = (TextView) findViewById(R.id.id_login_button);
		usernameView = (EditText) findViewById(R.id.id_login_username);
		passwordView = (EditText) findViewById(R.id.id_login_password);
		registerView = (LinearLayout) findViewById(R.id.id_register);
		findbackpwdView = (TextView) findViewById(R.id.id_find_back_pwd);
		progressBar = (ProgressBar) findViewById(R.id.id_loading_bar);
		loginHintView = (TextView) findViewById(R.id.id_login_hint);
		username = usernameView.getText().toString();
		password = passwordView.getText().toString();
		
		loginView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ConnectivityManager manager = (ConnectivityManager) v.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
				NetworkInfo networkInfo = manager.getActiveNetworkInfo();
				if(networkInfo != null){
					Boolean connected = networkInfo.isConnected();
					if(!connected){
						loginHintView.setVisibility(View.VISIBLE);
						loginHintView.setText(R.string.login_fail);
					}else {
						username = usernameView.getText().toString().trim();
						password = passwordView.getText().toString().trim();
						
						if(username == null || username.equals("") || isConSpeCharacters(username)){
							usernameView.setHint(R.string.enter_username);
							usernameView.setHintTextColor(getResources().getColor(R.color.red));
						}
						if(password == null || password.equals("") || isConSpeCharacters(password)){
							passwordView.setHint(R.string.enter_password);
							passwordView.setHintTextColor(getResources().getColor(R.color.red));
						}
						
						if((username != null && !username.equals(""))
								&&(password != null && !password.equals(""))){
							setEnable(false);
							new Thread(runnable).start();
						}
					}
				}else {
					loginHintView.setVisibility(View.VISIBLE);
					loginHintView.setText(R.string.login_fail);
				}
			}
		});
		
		
		registerView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(v.getContext(), RegisterActivity.class);
				startActivity(intent);
				//registerView.setTextColor(getResources().getColor(R.color.white));
			}
		});
	}
	
	private Handler handler = new Handler(){
		
		@Override
		public void handleMessage(android.os.Message msg) {
			if(msg.obj != null){
				String respose = (String) msg.obj;
				try {
					JSONObject resposeJson = new JSONObject(respose);
					int status = resposeJson.getInt("status");
					
					if(status == ParamsUtils.SUCCESS_CODE){
						String sessionId = resposeJson.getString("sessionid");
						int id = resposeJson.getInt("id");
						Intent intent = new Intent(getApplicationContext(), MainActivity.class);
						startActivity(intent);
						Editor editor = userInfoPreferences.edit();
						editor.putString(ParamsUtils.USER_NAME, username);
						editor.putString(ParamsUtils.PASSWORD, password);
						editor.putString(ParamsUtils.SESSIONID, sessionId);
						editor.putInt(ParamsUtils.USER_ID, id);
						editor.commit();
						Session.setSessionId(sessionId);
						Session.setId(id);
						onDestroy();
					}else{
						setEnable(true);
						String info = resposeJson.getString("info");
						int code = resposeJson.getInt("code");
						loginHintView.setText(info);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					setEnable(true);
					loginHintView.setText(R.string.login_fail);
				}
			}else {
				setEnable(true);
				loginHintView.setText(R.string.login_network_error);
			}
			
		};
	};
	
	private Runnable runnable = new Runnable() {
		
		@Override
		public void run() {
			/*Check user information from server*/
			Message msg = new Message();
			String result = new String();
			LoginControl control = new LoginControl(Params.base_uri + Params.session_uri);
			try {
				result = control.login(username, password);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				result = null;
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				result = null;
			}
			msg.obj = result;
			
			handler.sendMessage(msg);
		}
	};
	
	private void setEnable(boolean enable){
		if(enable){
			loginView.setVisibility(View.VISIBLE);
			progressBar.setVisibility(View.GONE);
			loginHintView.setVisibility(View.VISIBLE);
		}else {
			loginHintView.setVisibility(View.INVISIBLE);
			progressBar.setVisibility(View.VISIBLE);
			loginView.setVisibility(View.GONE);
		}
		registerView.setEnabled(enable);
		findbackpwdView.setEnabled(enable);
		usernameView.setEnabled(enable);
		passwordView.setEnabled(enable);
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		finish();
	}
	
	public static boolean isConSpeCharacters(String string){ 
	    if(string.replaceAll("[\u4e00-\u9fa5]*[a-z]*[A-Z]*\\d*-*_*\\s*","").length()==0){ 
	        //不包含特殊字符 
	        return false; 
	    } 
	    return true; 
	}
}
