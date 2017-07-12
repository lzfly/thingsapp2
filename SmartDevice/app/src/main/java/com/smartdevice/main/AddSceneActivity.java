package com.smartdevice.main;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.smartdevice.control.GroupControl;
import com.smartdevice.mode.Device;
import com.smartdevice.mode.SceneDeviceChoosedAdapter;
import com.smartdevice.mode.Session;
import com.smartdevice.mode.User;
import com.smartdevice.ui.CustActionBarV3;
import com.smartdevice.ui.CustLinearView;
import com.smartdevice.utils.DensityUtil;
import com.smartdevice.utils.LogUtil;
import com.smartdevice.utils.Params;
import com.smartdevice.utils.ParamsUtils;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class AddSceneActivity extends ActionBarActivity{
	
	private final static int SET_SCENE_REQUEST_CODE = 0x0;
	private final static int ADD_SCENE_DEVICE_REQUEST_CODE = 0x2;
	CustLinearView sceneNameView;
	View addSceneDeviceView;
	private String sceneName;
	private Toast mToast;
	
	private ListView triggerView;
	private ProgressDialog dialog;
	
	List<Device> deviceChoosedList;
	private boolean endFlag = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		init();
	}
	
	private void init(){
		setContentView(R.layout.add_scene_layout);
		CustActionBarV3 actionBar = new CustActionBarV3(getSupportActionBar());
		actionBar.setTitle(R.string.scene_add);
		actionBar.getMenuView().setText(R.string.scene_ok);
		actionBar.getIconView().setText(R.string.scene_cancel);
		sceneNameView = (CustLinearView) findViewById(R.id.id_add_scene_name);
		triggerView = (ListView) findViewById(R.id.id_add_scene_device_trigger);
		addSceneDeviceView = findViewById(R.id.id_add_scene_device);
		sceneNameView.getTitleView().setTextColor(Color.DKGRAY);
		sceneNameView.getContentView().setTextColor(Color.GRAY);
		sceneNameView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(v.getContext(), SetSceneNameActivity.class);
				startActivityForResult(intent, SET_SCENE_REQUEST_CODE);
			}
		});
		
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
				if(sceneName == null || sceneName.equals("")){
					showToast(R.string.scene_name_empty);
				}else {
					if(deviceChoosedList == null || deviceChoosedList.size() < 1){
						showToast(R.string.scene_device_empty);
					}else {
						dialog = ProgressDialog.show(v.getContext(), null, 
								getResources().getString(R.string.scene_new_building));
						/*add scene(group)*/
						new Thread(new  Runnable() {
							public void run() {
								GroupControl control = new GroupControl();
									String result = null;
									Message msg = new Message();
									try {
										result = control.addGroup(Session.getUsername(), sceneName);
										if(result != null){
											msg.obj = result;
											JSONObject object = new JSONObject(result);
											addSceneHandler.sendMessage(msg);
											int status = object.getInt("status");
											if(status == ParamsUtils.SUCCESS_CODE){
												for (int i = 0; i < deviceChoosedList.size(); i++) {
//													dialog.setMessage(getResources().getString(R.string.scene_device_adding) + 
//															deviceChoosedList.get(i).getDeviceName());
//													dialog.show();
													for (int j = 0; j < deviceChoosedList.get(i).getAttrList().size(); j++) {
														result = control.addGroupBindDevice(Session.getUsername(), 
																sceneName, 
																deviceChoosedList.get(i).getDeviceSN(), 
																deviceChoosedList.get(i).getDeviceName(), 
																deviceChoosedList.get(i).getDeviceType().getTypeCode(), 
																deviceChoosedList.get(i).getAttrList().get(j).getAttrCode(), 
																deviceChoosedList.get(i).getAttrList().get(j).getAttrCurrentValue());
														Message msg2 = new Message();
														msg2.obj = result;
														addSceneHandler.sendMessage(msg2);
													}
												}
												endFlag = true;
											}
										}else {
											msg.obj = null;
											addSceneHandler.sendMessage(msg);
										}
									} catch (JSONException e) {
										// TODO Auto-generated catch block
										msg.obj = null;
										addSceneHandler.sendMessage(msg);
										e.printStackTrace();
									}
									
							}
						}).start();
					}
				}
			}
		});
        
        addSceneDeviceView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(v.getContext(), AddSceneDeviceActivity.class);
				startActivityForResult(intent, ADD_SCENE_DEVICE_REQUEST_CODE);
			}
		});
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
						showToast(R.string.scene_add_successful);
					}else {
						showToast(R.string.scene_add_fail);
					}
					dialog.dismiss();
					if(endFlag){
						setResult(RESULT_OK);
						finish();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					dialog.dismiss();
					showToast(R.string.scene_add_fail);
				}
			}else {
				dialog.dismiss();
				showToast(R.string.scene_add_fail);
			}
		};
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		//super.onActivityResult(requestCode, resultCode, data);
		if(data != null && (resultCode == RESULT_OK)){
			if(requestCode == SET_SCENE_REQUEST_CODE){
				sceneName = data.getStringExtra(ParamsUtils.SCENE_NAME);
				sceneNameView.getContentView().setText(sceneName);
				sceneNameView.getContentView().setVisibility(View.VISIBLE);
			}else if(requestCode == ADD_SCENE_DEVICE_REQUEST_CODE){
				deviceChoosedList = (List<Device>)data.getSerializableExtra(ParamsUtils.DEVICE_CHOOSED_LIST);
				SceneDeviceChoosedAdapter choosedAdapter = new 
						SceneDeviceChoosedAdapter(deviceChoosedList, this);
				triggerView.setAdapter(choosedAdapter);
			}
		}
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
