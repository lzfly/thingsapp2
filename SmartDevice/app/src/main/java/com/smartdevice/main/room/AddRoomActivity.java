package com.smartdevice.main.room;

import org.json.JSONException;
import org.json.JSONObject;

import com.smartdevice.control.RoomControl;
import com.smartdevice.main.R;
import com.smartdevice.mode.Session;
import com.smartdevice.ui.CustActionBarV3;
import com.smartdevice.utils.CustUtils;
import com.smartdevice.utils.LogUtil;
import com.smartdevice.utils.ParamsUtils;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.Spinner;

public class AddRoomActivity extends ActionBarActivity{

	private EditText nameView;
	private Spinner typeSpinner;
	private int roomType;
	private String roomName;
	private ProgressDialog dialog;
	private String result = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_room_layout);
		nameView = (EditText) findViewById(R.id.id_room_name);
		typeSpinner = (Spinner) findViewById(R.id.id_room_type);
		typeSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				roomType = position;
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				roomType = 0;
			}
			
		});
		
		CustActionBarV3 actionBar = new CustActionBarV3(getSupportActionBar());
		actionBar.setTitle(R.string.room_new);
		actionBar.getIconView().setText(R.string.grobal_cancel);
		actionBar.getMenuView().setText(R.string.grobal_ok);
		actionBar.getIconView().setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		actionBar.getMenuView().setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				roomName = nameView.getEditableText().toString();
				roomName = roomName.replace(" ", "");
				if(roomName.equals("")){
					CustUtils.showToast(getApplicationContext(), R.string.room_name_none);
				}else {
				    if(CustUtils.checkLegalString(roomName)){
				    	dialog = ProgressDialog.show(v.getContext(), null, "保存房间 ...");
						new Thread(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								RoomControl control = new RoomControl();
								try {
									result = control.addRoom(Session.getUsername(), roomName);
									Message message = new Message();
									message.obj = result;
									handler.sendMessage(message);
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}).start();
				    }else {
				    	CustUtils.showToast(AddRoomActivity.this, R.string.grobal_illegal_string);
					}
					
				}
			}
		});
	}
	
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			String result = (String) msg.obj;
			if(result != null){
				try {
					JSONObject object = new JSONObject(result);
					int status = object.getInt("status");
					if(status == ParamsUtils.SUCCESS_CODE){
						CustUtils.showToast(getApplicationContext(), R.string.grobal_save_successful);
					}else {
						CustUtils.showToast(getApplicationContext(), R.string.grobal_save_fail);
					}
					dialog.dismiss();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					dialog.dismiss();
					e.printStackTrace();
				}
				setResult(RESULT_OK);
				finish();
			}else{
				CustUtils.showToast(getApplicationContext(), R.string.grobal_save_fail);
				dialog.dismiss();
			}
		};
	};
	
//	public void startTimer(){
//		Runnable runnable = new Runnable() {
//			long startTime=System.currentTimeMillis();  
//			long enableTime;
//			boolean isRun = true;
//			@Override
//			public void run() {
//				// TODO Auto-generated method stub
//				while (isRun) {
//					enableTime = System.currentTimeMillis()-startTime;  
//					if((result != null) || enableTime > 3000){
//						isRun = false;
//						Message message = new Message();
//						message.obj = result;
//						LogUtil.i("AddRoomActivity", "send end message...");
//						handler.sendMessage(message);
//					}
//					try{  
//                        Thread.sleep(50);  
//                    }catch (Exception e) {  
//                        LogUtil.d("CustUtils", "计时器线程 sleep ex:"+e.toString());  
//                    } 
//				}
//			}
//		};
//		new Thread(runnable).start();
//	}
	
	enum RoomType{
		LIVING_ROOM(0, "客厅"),
		BED_ROOM(1, "卧室");
		
		public int code;
		public String name;
		
		private RoomType(int code, String name) {
			// TODO Auto-generated constructor stub
			this.code = code;
			this.name = name;
		}
		
	}
}
