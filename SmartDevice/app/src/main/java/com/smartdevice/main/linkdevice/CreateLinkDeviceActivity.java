package com.smartdevice.main.linkdevice;

import java.io.UnsupportedEncodingException;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;
import org.json.JSONException;

import com.smartdevice.control.LogicEntigyControl;
import com.smartdevice.main.R;
import com.smartdevice.mode.DeviceAttrType;
import com.smartdevice.mode.LogicEntity;
import com.smartdevice.mode.Session;
import com.smartdevice.service.MqttSend;
import com.smartdevice.ui.CustActionBarV3;
import com.smartdevice.utils.CustUtils;
import com.smartdevice.utils.ParamsUtils;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class CreateLinkDeviceActivity extends ActionBarActivity implements OnClickListener{

	private ImageView triggerView;
	private ImageView actionView;
	private LogicEntity triggerEntity;
	private LogicEntity actionEntity;
	private TextView ifView;
	private TextView thenView;
	private LogicEntity linkEntity;
	private SaveHandler saveHandler;
	private ProgressDialog dialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		init();
	}
	
	private void init(){
		setContentView(R.layout.create_link_device_layout);
		CustActionBarV3 actionBar = new CustActionBarV3(getSupportActionBar());
		actionBar.setTitle(R.string.link_device_create);
		actionBar.getIconView().setText(R.string.grobal_cancel);
		actionBar.getMenuView().setText(R.string.grobal_ok);
		actionBar.getIconView().setOnClickListener(this);
		actionBar.getMenuView().setOnClickListener(this);
		triggerView = (ImageView) findViewById(R.id.id_link_device_trigger_add);
		actionView = (ImageView) findViewById(R.id.id_link_device_action_add);
		ifView = (TextView) findViewById(R.id.id_trigger_if);
		thenView = (TextView) findViewById(R.id.id_trigger_then);
		actionView.setEnabled(false);
		triggerView.setOnClickListener(this);
		actionView.setOnClickListener(this);
		linkEntity = new LogicEntity();
		saveHandler = new SaveHandler();
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		setIntent(intent);
		setDataAndView();
	}
	
	private void setDataAndView(){
		Intent intent = getIntent();
		String fromType = intent.getStringExtra(ParamsUtils.LINK_DEVICE_ATTR_TYPE);
		if(fromType != null){
			if(fromType.equals(ParamsUtils.LINK_DEVICE_ATTR_TRIGGER)){
				triggerEntity = (LogicEntity) intent.getSerializableExtra("com.smartdevice.mode.LogicEntity");
				CustUtils.setDeviceIcon(triggerEntity.getIfTypeCode(), triggerView);
				String ifString = "如果" + triggerEntity.getIfDeviceName() + 
						getAttrName(triggerEntity.getIfAttrCode()) + 
						getOperateName(triggerEntity.getIfAttrCode(), triggerEntity.getIfOperateCode()) + 
						getControlName(triggerEntity.getIfAttrCode(), triggerEntity.getIfAttrValue());
				ifView.setText(ifString);
				actionView.setEnabled(true);
				actionView.setImageResource(R.drawable.icon_trigger_add);
				linkEntity.setIfDeviceName(triggerEntity.getIfDeviceName());
				linkEntity.setIfDeviceSN(triggerEntity.getIfDeviceSN());
				linkEntity.setIfTypeCode(triggerEntity.getIfTypeCode());
				linkEntity.setIfAttrCode(triggerEntity.getIfAttrCode());
				linkEntity.setIfOperateCode(triggerEntity.getIfOperateCode());
				linkEntity.setIfAttrValue(triggerEntity.getIfAttrValue());
				linkEntity.setGatewaySN(triggerEntity.getGatewaySN());
			}else if(fromType.equals(ParamsUtils.LINK_DEVICE_ATTR_ACTION)){
				actionEntity = (LogicEntity) intent.getSerializableExtra("com.smartdevice.mode.LogicEntity");
				CustUtils.setDeviceIcon(actionEntity.getThTypeCode(), actionView);
				String thString = "然后" + actionEntity.getThDeviceName() + 
						getAttrName(actionEntity.getThAttrCode()) + 
						"设为" + 
						getControlName(actionEntity.getThAttrCode(), actionEntity.getThAttrValue());
				thenView.setText(thString);
				linkEntity.setThDeviceName(actionEntity.getThDeviceName());
				linkEntity.setThDeviceSN(actionEntity.getThDeviceSN());
				linkEntity.setThTypeCode(actionEntity.getThTypeCode());
				linkEntity.setThAttrCode(actionEntity.getThAttrCode());
				linkEntity.setThAttrValue(actionEntity.getThAttrValue());
			}
		}
		linkEntity.setUserName(Session.getUsername());
	}
	
	private String getAttrName(int attrCode){
		return DeviceAttrType.AttrType.getName(attrCode);
	}
	
	private String getControlName(int attrCode, String control){
		if(attrCode == DeviceAttrType.ATTR_TYPE_OPEN
				|| attrCode == DeviceAttrType.ATTR_TYPE_CURTAIN_STATE
				|| attrCode == DeviceAttrType.ATTR_TYPE_IS_ALARM ){
			if(control.equals("1")){
				return new String("打开");
			}else{
				return new String("关闭");
			}
		}
		return control;
	}
	
	private String getOperateName(int attrCode, String operatorCode){
		if(attrCode == DeviceAttrType.ATTR_TYPE_OPEN
				|| attrCode == DeviceAttrType.ATTR_TYPE_CURTAIN_STATE
				|| attrCode == DeviceAttrType.ATTR_TYPE_IS_ALARM ){
			if(operatorCode.equals("=")){
				operatorCode = "";
			}
		}else {
			if(operatorCode.equals("=")){
				operatorCode = "等于";
			}else if (operatorCode.equals(">")) {
				operatorCode = "大于";
			}else if (operatorCode.equals("<")) {
				operatorCode = "小于";
			}
		}
		
		return operatorCode;
	}
	

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.id_actionbar_v3_icon:
			finish();
			break;
		case R.id.id_actionbar_v3_menu:
			/*save the recipe*/
			if(linkEntity == null){
				CustUtils.showToast(this, R.string.link_device_trigger_none);
			}else{
				if(linkEntity.getIfAttrValue() == null
						|| linkEntity.getIfAttrValue().equals("")){
					CustUtils.showToast(this, R.string.link_device_trigger_none);
				}else if (linkEntity.getThAttrValue() == null
					|| linkEntity.getThAttrValue().equals("")){
						CustUtils.showToast(this, R.string.link_device_action_none);
				}else {
					//save to database
					dialog = ProgressDialog.show(v.getContext(), null, 
							getResources().getString(R.string.link_device_new_link));
					new SavaThread().start();
					new NoticeThread().start();
				}
			}
			break;
		case R.id.id_link_device_trigger_add:
			Intent intent = new Intent(this, TriggerLinkDeviceActivity.class);
			startActivity(intent);
			break;
		case R.id.id_link_device_action_add:
			Intent actionIntent = new Intent(this, ActionLinkDeviceActivity.class);
			startActivity(actionIntent);
			break;
		default:
			break;
		}
	}
	
	class SavaThread extends Thread{
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			int res = 0;
			LogicEntigyControl control = new LogicEntigyControl();
			try {
				res = control.addLogicEntity(linkEntity);
				Message message = new Message();
				message.arg1 = res;
				saveHandler.sendMessage(message);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				res = -1;
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				res = -1;
				e.printStackTrace();
			}
		}
	}
	
	class NoticeThread extends Thread{
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			SystemClock.sleep(1000*2);
			try {
				MqttSend send = new MqttSend();
				send.Send("/v1/to_gateway/" + linkEntity.getGatewaySN(), 0, String
						.valueOf("linkdevice").getBytes());
			} catch (MqttSecurityException e) {
				// TODO Auto-generated catch block^M
				e.printStackTrace();
				return;
			} catch (MqttException e) {
				// TODO Auto-generated catch block^M
				Log.i("MqttSend", "send error....");
				e.printStackTrace();
				return;
			}

		}
	}
	
	class SaveHandler extends Handler{
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			int res = msg.arg1;
			if(res == ParamsUtils.SUCCESS_CODE){
				CustUtils.showToast(getApplicationContext(), R.string.grobal_save_successful);
			}else {
				CustUtils.showToast(getApplicationContext(), R.string.grobal_save_fail);
			}
			if(dialog != null){
				dialog.dismiss();
			}
			Intent intent = new Intent(getApplicationContext(), CreatedLinkDeviceActivity.class);
			startActivity(intent);
			finish();
		}
	}
}
