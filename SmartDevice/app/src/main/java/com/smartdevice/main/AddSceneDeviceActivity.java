package com.smartdevice.main;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

import com.smartdevice.control.GroupControl;
import com.smartdevice.mode.Device;
import com.smartdevice.mode.DeviceAdapter;
import com.smartdevice.mode.DeviceAttribute;
import com.smartdevice.mode.SceneDeviceAdapter;
import com.smartdevice.mode.SceneDeviceChoosedAdapter;
import com.smartdevice.mode.Session;
import com.smartdevice.ui.CustActionBarV3;
import com.smartdevice.utils.CustUtils;
import com.smartdevice.utils.LogUtil;
import com.smartdevice.utils.ParamsUtils;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class AddSceneDeviceActivity extends ActionBarActivity{
    private final static String TAG = AddSceneDeviceActivity.class.getSimpleName();
	SceneDeviceAdapter adapter;
	private Toast mToast;
	List<Device> deviceChoosedList = new ArrayList<>();
	SceneDeviceChoosedAdapter choosedAdapter;
	private final static int ADD_SCENE_SET_DEVICE_ATTRIBUTE = 0x1;
	private String sceneName = "";
	boolean endFlag = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_scene_device_layout);
		CustActionBarV3 actionBar = new CustActionBarV3(getSupportActionBar());
		actionBar.setTitle(R.string.scene_device_add);
		actionBar.getMenuView().setText(R.string.scene_ok);
		actionBar.getIconView().setText(R.string.scene_cancel);
		actionBar.getIconView().setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		init();
		TextView addView = (TextView) findViewById(R.id.id_add_scene_add_device);
		final ListView deviceChoosedView = (ListView) findViewById(R.id.id_add_scene_device_choosed);
		addView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(adapter != null){
					deviceChoosedList.clear();
					HashMap<Integer, Boolean> choosedMap= adapter.getChoosedMap();
					List<Device> deviceChooseList = adapter.getDeviceChooseList();
					for (int i = 0; i < choosedMap.size(); i++) {
						if (choosedMap.get(i)) {
							deviceChoosedList.add(deviceChooseList.get(i));
						}
					}
					if(deviceChoosedList.isEmpty()){
						showToast(R.string.scene_one_device);
					}else {
						choosedAdapter = 
								new SceneDeviceChoosedAdapter(deviceChoosedList, v.getContext());
						deviceChoosedView.setAdapter(choosedAdapter);
					}
				}
			}
		});
		
		deviceChoosedView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(view.getContext(), DeviceAttributeActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("com.smartdevice.mode.Device", (Device) view.getTag());
				intent.putExtras(bundle);
				intent.putExtra("add_scene_device", ADD_SCENE_SET_DEVICE_ATTRIBUTE);
				startActivityForResult(intent, ADD_SCENE_SET_DEVICE_ATTRIBUTE);
			}
		});
		
		actionBar.getMenuView().setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
	            if((sceneName != null) && (!sceneName.equals(""))){
	            	//在已有场景下添加设备
	            	saveNewAddedDevice();
	            }else {
	            	Intent intent = new Intent();
					intent.putExtra(ParamsUtils.DEVICE_CHOOSED_LIST, (Serializable)deviceChoosedList);
					setResult(RESULT_OK, intent);
					finish();
				}
			}
		});
	}
	
	private void init(){
		sceneName = getIntent().getStringExtra(ParamsUtils.SCENE_NAME);
		List<Device> deviceList = DeviceAdapter.getDeviceList();
		List<Device> deviceChooseList = new ArrayList<>();
		Set<Device> deviceSet = new HashSet<>();
		if(deviceList != null && deviceList.size() != 0){
			for (int i = 0; i < deviceList.size(); i++) {
				Device device = deviceList.get(i);
				List<DeviceAttribute> attrList = device.getAttrList();
				LogUtil.i("AddSceneDeviceActivity", "Devie name " + device.getDeviceName());
				if(attrList != null && attrList.size() != 0){
					for (int j = 0; j < attrList.size(); j++) {
						DeviceAttribute attribute = attrList.get(j);
						if(attribute.getPermission().contains("W")){
							deviceSet.add(device);
						}
					}
				}
			}
		}else {
			
		}
		Iterator<Device> iterator = deviceSet.iterator();
 		while(iterator.hasNext()){
 			deviceChooseList.add(iterator.next());
 		}
 		List<Device> addedDeviceList = (List<Device>) getIntent().getSerializableExtra(ParamsUtils.DEVICE_ADDED_LIST);
 		/*排除已经添加的设备*/
 		if(addedDeviceList != null && addedDeviceList.size() > 0){
 			Iterator<Device> tmpIterator = deviceChooseList.iterator();
 			while (tmpIterator.hasNext()) {
 				Device device = tmpIterator.next();
 				String tmpSN = device.getDeviceSN();
 				for (int j = 0; j < addedDeviceList.size(); j++) {
 					if(tmpSN.equals(addedDeviceList.get(j).getDeviceSN())){
 						tmpIterator.remove();
 					}
 				}
 			}
 		}
 		LogUtil.i(TAG, "Choose device count " + deviceChooseList.size());
		ListView deviceChooseView = (ListView) findViewById(R.id.id_add_scene_device_choose);
		adapter = new SceneDeviceAdapter(deviceChooseList, this);
		deviceChooseView.setAdapter(adapter);
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
									sceneName, 
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
				Message msg3 = new Message();
				msg3.obj = result;
				addSceneHandler.sendMessage(msg3);
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
					Intent intent = new Intent();
					intent.putExtra(ParamsUtils.DEVICE_CHOOSED_LIST, (Serializable)deviceChoosedList);
					setResult(RESULT_OK, intent);
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
		LogUtil.d("AddSceneDeviceActivity", "onActivityResult requestCode " + requestCode);
		if((requestCode == ADD_SCENE_SET_DEVICE_ATTRIBUTE)
				&& (resultCode == RESULT_OK)){
			if(data != null){
				Device device = (Device) data.getSerializableExtra("com.smartdevice.mode.Device");
				updateDeviceInfo(device);
			}
		}
	}
	
	private void updateDeviceInfo(Device device){
		choosedAdapter.updateDeviceList(device);
	}
	
	private void showToast(int toast){
		if(mToast == null){
			mToast = Toast.makeText(this, toast, Toast.LENGTH_LONG);
		}else {
			mToast.setText(toast);
		}
		mToast.show();
	}
}
