package com.smartdevice.main.room;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.smartdevice.control.RoomControl;
import com.smartdevice.main.DeviceInfoActivity;
import com.smartdevice.main.R;
import com.smartdevice.main.linkdevice.CreatedLinkDeviceInfoActivity;
import com.smartdevice.mode.Device;
import com.smartdevice.mode.DeviceAdapter;
import com.smartdevice.mode.DeviceSimpleAdpatar;
import com.smartdevice.mode.Room;
import com.smartdevice.mode.RoomBindDevice;
import com.smartdevice.mode.Session;
import com.smartdevice.utils.CustActionBar;
import com.smartdevice.utils.CustUtils;
import com.smartdevice.utils.LogUtil;
import com.smartdevice.utils.ParamsUtils;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class RoomInfoActivity extends ActionBarActivity{

	private GridView deviceGridView;
	private List<RoomBindDevice> deviceList;
	private Room room;
	private DeviceSimpleAdpatar adapter;
	private String result = new String();
	private final static int ROOM_CHOOSE_DEVIE = 0x0;
	private final static int ROOM_CONTROL_DEVIE = 0x1;
	private ProgressDialog progressDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.room_info_layout);
		deviceGridView = (GridView) findViewById(R.id.id_room_info_device);
		TextView nameView = (TextView) findViewById(R.id.id_room_info_name);
		TextView typeView = (TextView) findViewById(R.id.id_room_info_type);
		room = (Room) getIntent().getSerializableExtra("com.smartdevice.mode.Room");
		nameView.setText(room.getRoomName());
		typeView.setVisibility(View.INVISIBLE);
		init();
		getRoomDeviceList();
		CustActionBar actionBar = new CustActionBar(getSupportActionBar());
		actionBar.setTitle(R.string.room_info);
		actionBar.setIconOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		actionBar.getMenuTxView().setText(R.string.room_delete);
		actionBar.getMenuTxView().setVisibility(View.VISIBLE);
		actionBar.getMenuTxView().setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//add a progress here
				
				// TODO Auto-generated method stub
				AlertDialog.Builder alertDialog = new AlertDialog.Builder(RoomInfoActivity.this);
				alertDialog.setMessage(R.string.sure_delete);
				alertDialog.setPositiveButton(R.string.grobal_sure, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
						deleleRoom();
						progressDialog = ProgressDialog.show(RoomInfoActivity.this, null, 
								getResources().getString(R.string.room_delete));
					}
				});
				alertDialog.setNegativeButton(R.string.grobal_cancel, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				});
				
				alertDialog.show();
			}
		});
	}
	
	
	
	private void init(){
		deviceList = new ArrayList<>();
		adapter = new DeviceSimpleAdpatar(this, deviceList);
	    deviceGridView.setAdapter(adapter);
	    deviceGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				LogUtil.i("RoomInfoActivity", "tag " + view.getTag(R.string.room_add_device_tag));
//				if(view.getTag(R.string.room_add_device_tag) != null
//						&&view.getTag(R.string.room_add_device_tag).equals("ADD_ITEM"))
				if(!deviceList.get(position).isFlag()){
					//add device
					Intent intent = new Intent(getApplicationContext(), DeviceChooseActivity.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable("com.smartdevice.mode.Room", room);
					intent.putExtras(bundle);
					startActivityForResult(intent, ROOM_CHOOSE_DEVIE);
				}else {
					//...
					Device device = getDeviceInfo(deviceList.get(position).getDeviceSN());
					if(device != null){
						Intent intent = new Intent(getApplicationContext(), DeviceControlActivity.class);
						Bundle bundle = new Bundle();
						bundle.putSerializable("com.smartdevice.mode.Device", device);
						bundle.putSerializable("com.smartdevice.mode.RoomBindDevice", deviceList.get(position));
						intent.putExtras(bundle);
						startActivityForResult(intent, ROOM_CONTROL_DEVIE);
					}
					
				}
			}
		});
	}
	
	private Device getDeviceInfo(String deviceSN){
		List<Device> deviceList = DeviceAdapter.getDeviceList();
		if(deviceList != null&& deviceList.size()>0){
			for (int i = 0; i < deviceList.size(); i++) {
				if(deviceList.get(i).getDeviceSN().equals(deviceSN)){
					return deviceList.get(i);
				}
			}
		}
		return null;
	}
	
	private void getRoomDeviceList(){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				RoomControl control = new RoomControl();
				deviceList = control.getRoomBindDevices(Session.getUsername(), room.getRoomName());
				handler.sendEmptyMessage(0);
			}
		}).start();
	}
	
	private void deleleRoom(){
         new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				RoomControl control = new RoomControl();
				try {
					result = control.DeleteRoom(room.getId());
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				handler.sendEmptyMessage(1);
			}
		}).start();
	}
	
	private Handler handler = new Handler(){
		
		@Override
		public void handleMessage(android.os.Message msg) {
			if(msg.what == 1){
//				try {
//					JSONObject object = new JSONObject(result);
//					int status = object.getInt("status");
//					if(status == ParamsUtils.SUCCESS_CODE){
//						CustUtils.showToast(getApplicationContext(), R.string.grobal_delete_successful);
//					}else {
//						CustUtils.showToast(getApplicationContext(), R.string.grobal_delete_fail);
//					}
//				} catch (JSONException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
				CustUtils.showToast(getApplicationContext(), R.string.grobal_delete_successful);
				progressDialog.dismiss();
				setResult(RESULT_OK);
				finish();
			}else {
				if(deviceList != null && deviceList.size() > 0){
					adapter.setDeviceList(deviceList);
					adapter.notifyDataSetChanged();
				}else {
					deviceList = new ArrayList<>();
					adapter.setDeviceList(deviceList);
					adapter.notifyDataSetChanged();
				}
			}
			
		};
	};
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if((requestCode == ROOM_CHOOSE_DEVIE)
				&& (resultCode == RESULT_OK)){
			if(data != null){
				List<Device> choosedList = (List<Device>) data.getSerializableExtra(ParamsUtils.DEVICE_CHOOSED_LIST);
				if(choosedList != null && choosedList.size() > 0){
					for (int j = 0; j < choosedList.size(); j++) {
						RoomBindDevice bindDevice = new RoomBindDevice();
						bindDevice.setDeviceName(choosedList.get(j).getDeviceName());
						bindDevice.setDeviceSN(choosedList.get(j).getDeviceSN());
						bindDevice.setGatewaySN(choosedList.get(j).getGatewaySN());
						bindDevice.setRoomName(room.getUserName());
						bindDevice.setTypeCode(choosedList.get(j).getDeviceType().getTypeCode());
						adapter.updateDeviceList(bindDevice);
					}
					adapter.notifyDataSetChanged();
				}
			}
		}else if ((requestCode == ROOM_CONTROL_DEVIE)
				&& (resultCode == RESULT_OK)) {
			getRoomDeviceList();
		}
	};
}
