package com.smartdevice.main;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.json.JSONException;

import com.smartdevice.control.UserControl;
import com.smartdevice.mode.Gateway;
import com.smartdevice.mode.GatewayAdapter;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class RegisterActivity extends Activity{
	
	private TextView hintView;
	TextView backView;
	TextView registerView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_layout);
		
		backView = (TextView) findViewById(R.id.id_register_back);
		registerView = (TextView) findViewById(R.id.id_register_button);
		final TextView usernameView = (TextView) findViewById(R.id.id_register_username);
		final TextView passwordView = (TextView) findViewById(R.id.id_register_password);
		final TextView passwordEnsureView = (TextView) findViewById(R.id.id_register_password_ensure);
		hintView = (TextView) findViewById(R.id.id_register_hint);
		
		backView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onDestroy();
			}
		});
		
		registerView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				backView.setEnabled(false);
				registerView.setEnabled(false);
				final String username = usernameView.getEditableText().toString();
				final String password = passwordView.getEditableText().toString();
				final String passwordEnsure = passwordEnsureView.getEditableText().toString();
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						
						UserControl control = new UserControl();
						Message msg = new Message();
						
						if(username.length() == 0 || password.length() == 0) 
						{
							msg.arg1 = 1;
							handler.sendMessage(msg);
							return;
						}
						if(isConSpeCharacters(username)|| isConSpeCharacters(password) )
						{
							msg.arg1 = 1;
							handler.sendMessage(msg);
							return;
						}
						
						int position;
					    position=username.indexOf(" "); 
					    Log.i(RegisterActivity.class.getSimpleName(), "username " + position);
						if(position >= 0) 
						{
							msg.arg1 = 1;
							handler.sendMessage(msg);
							return;
						}
					    position=password.indexOf(" "); 
					    Log.i(RegisterActivity.class.getSimpleName(), "password " + position);
						if(position >= 0) 
						{
							msg.arg1 = 1;
							handler.sendMessage(msg);
							return;
						}
						
						if(!password.equals(passwordEnsure)) 
						{
							msg.arg1 = 1;
							handler.sendMessage(msg);
							return;
						}
						
						try {
							int status = control.addUser(username, password);
							msg.arg1 = status;
						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						handler.sendMessage(msg);
					}
				}).start();
			}
		});
	}

	private Handler handler = new Handler(){
		@Override
		public void handleMessage(android.os.Message msg) {
			hintView.setVisibility(View.VISIBLE);
			backView.setEnabled(true);
			registerView.setEnabled(true);
			if(msg.arg1 == 0){
				hintView.setText(R.string.register_ok);
			}else {
				hintView.setText(R.string.register_fail);
			}
		};
	};
	
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
