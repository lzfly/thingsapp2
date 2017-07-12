package com.smartdevice.main;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

import com.smartdevice.control.DeviceControl;
import com.smartdevice.control.GroupControl;
import com.smartdevice.mode.Device;
import com.smartdevice.mode.DeviceAttribute;
import com.smartdevice.mode.DeviceType;
import com.smartdevice.mode.Group;
import com.smartdevice.mode.GroupBindDevice;
import com.smartdevice.mode.SceneDeviceChoosedAdapter;
import com.smartdevice.mode.Session;
import com.smartdevice.ui.CustActionBarV3;
import com.smartdevice.ui.CustLinearView;
import com.smartdevice.ui.SwipeListView;
import com.smartdevice.utils.CustUtils;
import com.smartdevice.utils.LogUtil;
import com.smartdevice.utils.ParamsUtils;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class GroupInfoActivity extends ActionBarActivity{
	
	private Group group;
	private List<GroupBindDevice> groupdeviceList;
	private List<Device> deviceList = new ArrayList<>();
	SwipeListView devicesView;
	List<Device> deviceChoosedList = new ArrayList<>();
	Set<String> snSet;
	SceneDeviceChoosedAdapter adapter;
	private final static int SCENE_ADD_DEVICE_REQUEST_CODE = 0x0;
	private final static int SCENE_DEVICE_ATTR_REQUEST_CODE = 0x1;
	boolean endFlag = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.scene_info_layout);
		init();
		devicesView = (SwipeListView) findViewById(R.id.id_scene_devices); 
		CustLinearView sceneNameView = (CustLinearView) findViewById(R.id.id_scene_name);
		View turnOnView = findViewById(R.id.id_scene_turn_on);
		View addDeviceView = findViewById(R.id.id_scene_device_add);
		Intent intent = getIntent();
		group = (Group) intent.getSerializableExtra("com.smartdevice.mode.group");
		sceneNameView.setContent(group.getGroupName());
		sceneNameView.getContentView().setTextColor(Color.GRAY);
		sceneNameView.getContentView().setVisibility(View.VISIBLE);
		
		getGroupDeviceList();
		
		turnOnView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*开启当前场景，触发当前场景下的设备*/
				if(deviceList.size() == 0){
					CustUtils.showToast(getApplicationContext(), R.string.scene_have_no_device);
				}else {
					if(deviceChoosedList.size() > 0){
						CustUtils.showToast(getApplicationContext(), R.string.scene_device_not_save);
					}else {
						triggerDevice();
					}
				}
			}
		});
		
		addDeviceView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(), AddSceneDeviceActivity.class);
				intent.putExtra(ParamsUtils.DEVICE_ADDED_LIST, (Serializable)deviceList);
				intent.putExtra(ParamsUtils.SCENE_NAME, group.getGroupName());
				startActivityForResult(intent, SCENE_ADD_DEVICE_REQUEST_CODE);
			}
		});
		
		devicesView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(), DeviceAttributeActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("com.smartdevice.mode.Device", (Device) view.getTag());
				intent.putExtras(bundle);
				intent.putExtra(ParamsUtils.FROM_SCENE_INFO_ACTIVITY_START, 0x01);
				startActivityForResult(intent, SCENE_DEVICE_ATTR_REQUEST_CODE);
			}
			
		});
	}
	
	private void init(){
		CustActionBarV3 actionBarV3 = new CustActionBarV3(getSupportActionBar());
		actionBarV3.setTitle(R.string.scene_info);
		actionBarV3.getMenuView().setText(R.string.scene_ok);
		actionBarV3.getIconView().setText(R.string.scene_cancel);
		actionBarV3.getIconView().setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		actionBarV3.getMenuView().setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//if(deviceChoosedList.size() > 0){
					//save the new added device
					//saveNewAddedDevice();
				//}else {
					setResult(RESULT_OK);
					finish();
				//}
			}
		});
		
	}
	
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(android.os.Message msg) {
			if(msg.arg1 == 1){
				if(((String)msg.obj).equals("-1")){
					CustUtils.showToast(getApplicationContext(), R.string.scene_turn_on_fail);
				}else {
					CustUtils.showToast(getApplicationContext(), R.string.scene_turn_on_successful);
				}
			}else {
				getDeviceList();
				adapter = new SceneDeviceChoosedAdapter
						(deviceList, getApplicationContext());
				adapter.setSceneName(group.getGroupName());
				devicesView.setAdapter(adapter);
			}
		};
	};
	
	private void getGroupDeviceList(){
       new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				GroupControl groupControl = new GroupControl();
				groupdeviceList = groupControl.getGroupBindDevices(Session.getUsername(), group.getGroupName());
				snSet = new HashSet<>();
				if(groupdeviceList != null && groupdeviceList.size() > 0){
					for (int i = 0; i < groupdeviceList.size(); i++) {
						GroupBindDevice groupBindDevice = groupdeviceList.get(i);
						String sn = groupBindDevice.getDeviceSN();
						snSet.add(sn);
					}
					Message msg = new Message();
					handler.sendMessage(msg);
				}
			}
		}).start();
	}
	
	private void getDeviceList(){
		Iterator<String> iterator = snSet.iterator();
		while(iterator.hasNext()){
			String sn = (String) iterator.next();
			LogUtil.d("GroupInfoActivity", "device sn " + sn);
			Device device = new Device();
			device.setDeviceSN(sn);
			List<DeviceAttribute> attrList = new ArrayList<>();
			String deviceName = "";
			int typeCode = 0;
			for (int i = 0; i < groupdeviceList.size(); i++) {
				GroupBindDevice groupBindDevice = groupdeviceList.get(i);
				DeviceAttribute attribute = new DeviceAttribute();
				if(sn.equals(groupBindDevice.getDeviceSN())){
					attribute.setAttrCode(groupdeviceList.get(i).getAttrCode());
					attribute.setAttrCurrentValue(groupdeviceList.get(i).getAttrValue());
					attrList.add(attribute);
					deviceName = groupBindDevice.getDeviceName();
					typeCode = groupBindDevice.getTypeCode();
				}
			}
			DeviceType deviceType = new DeviceType();
			deviceType.setTypeCode(typeCode);
			LogUtil.d("GroupInfoActivity", "device name " + deviceName);
			device.setDeviceType(deviceType);
			device.setDeviceName(deviceName);
			device.setAttrList(attrList);
			deviceList.add(device);
		}
	}
	
	private void triggerDevice(){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if(groupdeviceList != null && groupdeviceList.size() != 0){
					for (int i = 0; i < groupdeviceList.size(); i++) 
					{
						DeviceControl control = new DeviceControl();
						String result = control.pushDeviceAttribute(groupdeviceList.get(i).getDeviceSN(), 
								groupdeviceList.get(i).getAttrCode(), 
								groupdeviceList.get(i).getAttrValue());	
						Message message = new Message();
						message.obj = result;
						message.arg1 = 1;
						handler.sendMessage(message);
						if(result.equals("-1")){
							break;
						}
					}
				}
			}
		}).start();
	}
	
	private void saveNewAddedDevice(){
			new Thread(new Runnable() {
				GroupControl control = new GroupControl();
				String result = null;
				@Override
				public void run() {
					// TODO Auto-generated method stub
					for (int i = 0; i < deviceChoosedList.size(); i++) {
						for (int j = 0; j < deviceChoosedList.get(i).getAttrList().size(); j++) {
							try {
								result = control.addGroupBindDevice(Session.getUsername(), 
										group.getGroupName(), 
										deviceChoosedList.get(i).getDeviceSN(), 
										deviceChoosedList.get(i).getDeviceName(), 
										deviceChoosedList.get(i).getDeviceType().getTypeCode(), 
										deviceChoosedList.get(i).getAttrList().get(j).getAttrCode(), 
										deviceChoosedList.get(i).getAttrList().get(j).getAttrCurrentValue());
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								result = null;
							}
							Message msg2 = new Message();
							msg2.obj = result;
							addSceneHandler.sendMessage(msg2);
						}
					}
					endFlag = true;
				}
			}).start();
	}
	
	private Handler addSceneHandler = new Handler(){
		@Override
		public void handleMessage(android.os.Message msg) {
			String result = (String) msg.obj;
			if(result != null){
				try {
					JSONObject object = new JSONObject(result);
					int status = object.getInt("status");
					if(status == ParamsUtils.SUCCESS_CODE){
						CustUtils.showToast(getApplicationContext(), R.string.grobal_save_successful);
					}
					if(endFlag){
						setResult(RESULT_OK);
						finish();
					}
				}catch(JSONException e){
					e.printStackTrace();
					CustUtils.showToast(getApplicationContext(), R.string.grobal_save_fail);
				}
			}else {
				CustUtils.showToast(getApplicationContext(), R.string.grobal_save_fail);
			}
		}
	};
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		//super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == RESULT_OK){
			if(requestCode == SCENE_ADD_DEVICE_REQUEST_CODE){
				deviceChoosedList = (List<Device>)data.getSerializableExtra(
						ParamsUtils.DEVICE_CHOOSED_LIST);
				deviceList.addAll(deviceChoosedList);
				if(adapter != null){
					adapter.setDeviceChoosedList(deviceList);
					adapter.notifyDataSetChanged();
				}
			}
			
		}
	}
}
