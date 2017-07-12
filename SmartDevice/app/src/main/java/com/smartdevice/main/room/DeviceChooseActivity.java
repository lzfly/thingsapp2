package com.smartdevice.main.room;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.smartdevice.control.RoomControl;
import com.smartdevice.main.R;
import com.smartdevice.mode.Device;
import com.smartdevice.mode.DeviceAdapter;
import com.smartdevice.mode.Room;
import com.smartdevice.mode.RoomBindDevice;
import com.smartdevice.mode.SceneDeviceAdapter;
import com.smartdevice.mode.Session;
import com.smartdevice.ui.CustActionBarV3;
import com.smartdevice.utils.CustUtils;
import com.smartdevice.utils.ParamsUtils;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

public class DeviceChooseActivity extends ActionBarActivity{
	
	private SceneDeviceAdapter adapter;
	private List<Device> choosedList;
	private RoomBindDevice roomBindDevice;
	private Room room;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.device_choose_layout);
		room = (Room) getIntent().getSerializableExtra("com.smartdevice.mode.Room");
		ListView chooseView = (ListView) findViewById(R.id.id_device_choose);
		adapter = new SceneDeviceAdapter(DeviceAdapter.getDeviceList(), this);
		chooseView.setAdapter(adapter);
		CustActionBarV3 actionBar = new CustActionBarV3(getSupportActionBar());
		actionBar.setTitle(R.string.device_choose);
		actionBar.getIconView().setText(R.string.grobal_cancel);
		actionBar.getMenuView().setText(R.string.grobal_sure);
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
				getChoosedDevices();
				saveChoosedDevices();
			}
		});
	}

	private void getChoosedDevices(){
		Map<Integer, Boolean> choosedMap = adapter.getChoosedMap();
		choosedList = new ArrayList<>();
		
		for (int i = 0; i < choosedMap.size(); i++) {
			if(choosedMap.get(i)){
				choosedList.add(adapter.getDeviceChooseList().get(i));
			}
		}
	}
	
	private void saveChoosedDevices(){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				RoomControl control = new RoomControl();
				String result = null;
				for (int i = 0; i < choosedList.size(); i++) {
					try {
						result = control.addRoomBindDevice(Session.getUsername(), 
								room.getRoomName(), 
								choosedList.get(i).getDeviceSN(), 
								choosedList.get(i).getDeviceName(), 
								choosedList.get(i).getDeviceType().getTypeCode());
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				Message msg = new Message();
				msg.obj = result;
				saveHandler.sendMessage(msg);
			}
		}).start();
	}
	
	private Handler saveHandler = new Handler(){
		
		@Override
		public void handleMessage(android.os.Message msg) {
			String result = (String) msg.obj;
			if (result != null) {
				try {
					JSONObject object = new JSONObject(result);
					int status = object.getInt("status");
					if(status == ParamsUtils.SUCCESS_CODE){
						CustUtils.showToast(getApplicationContext(), R.string.grobal_save_successful);
						
						Intent intent = new Intent();
						intent.putExtra(ParamsUtils.DEVICE_CHOOSED_LIST, (Serializable)choosedList);
						setResult(RESULT_OK, intent);
						
						finish();
					}else {
						CustUtils.showToast(getApplicationContext(), R.string.grobal_save_fail);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}else {
				CustUtils.showToast(getApplicationContext(), R.string.grobal_save_fail);
			}
		};
	};
}
