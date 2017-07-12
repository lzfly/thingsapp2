package com.smartdevice.main.room;

import org.json.JSONException;

import com.smartdevice.control.RoomControl;
import com.smartdevice.main.R;
import com.smartdevice.mode.Device;
import com.smartdevice.mode.DeviceAttrView;
import com.smartdevice.mode.DeviceType;
import com.smartdevice.mode.RoomBindDevice;
import com.smartdevice.ui.CustLinearView;
import com.smartdevice.utils.CustActionBar;
import com.smartdevice.utils.CustUtils;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DeviceControlActivity extends ActionBarActivity implements OnClickListener{
    
	private static final String TAG = DeviceControlActivity.class.getSimpleName();
	private ImageView iconView;
	//private TextView nameView;
	private CustLinearView nameView;
	private TextView snView;
	private TextView typeView;
	private LinearLayout devInfoLayout;
	private int typeCode;
	DeviceAttrView attrView;
	private ImageView actionbarIcon;
	RoomBindDevice bindDevice;
	String result = new String();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.device_info_layout); 
		iconView = (ImageView) findViewById(R.id.id_devinfo_icon);
		nameView = (CustLinearView) findViewById(R.id.id_devinfo_name);
		snView = (TextView) findViewById(R.id.id_deviceinfo_sn);
		typeView = (TextView) findViewById(R.id.id_deviceinfo_type);
		devInfoLayout = (LinearLayout) findViewById(R.id.id_devinfo_attr);
		Intent intent = this.getIntent();
		Device device = (Device)intent.getSerializableExtra("com.smartdevice.mode.Device");
		bindDevice = (RoomBindDevice) intent.getSerializableExtra("com.smartdevice.mode.RoomBindDevice");
		Log.i(TAG, "Device name " + device.getDeviceName());
		//Log.i(TAG, "Device base type id " + device.getDeviceType().getBaseTypeId());
		//Log.i(TAG, "Device type id " + device.getDeviceType().getTypeId());
		typeCode = device.getDeviceType().getTypeCode();
		setDeviceIcon(typeCode);
		if(device.getDeviceType().getTypeName() != null
				&& !device.getDeviceType().getTypeName().equals("") ){
			typeView.setText(device.getDeviceType().getTypeName());
		}
		
		nameView.setContent(device.getDeviceName());
		nameView.setContentColor(getResources().getColor(R.color.light_gray_20));
		snView.setText(device.getDeviceSN());
		attrView = new DeviceAttrView(this, device);
		ViewGroup viewGroup = attrView.getAttrViews();
		devInfoLayout.addView(viewGroup);
		
		CustActionBar actionBar = new CustActionBar(getSupportActionBar());
		actionBar.setTitle(R.string.device_info);
		actionBar.getMenuTxView().setVisibility(View.VISIBLE);
		actionBar.getMenuTxView().setText(R.string.room_device_delete);
		actionbarIcon = actionBar.getIconView();
		actionbarIcon.setOnClickListener(this);
		actionBar.getMenuTxView().setOnClickListener(this);
		
	}
	
	private void setDeviceIcon(int typeId){
		switch (typeId) {
		case DeviceType.DEVICE_TYPE_ADJUST_BRG_LIGHT:
		case DeviceType.DEVICE_TYPE_COLOR_TEMPERATURE_LIGHT:
			iconView.setImageResource(R.drawable.icon_light);
			break;
		case DeviceType.DEVICE_TYPE_SWITCH:
			iconView.setImageResource(R.drawable.icon_switcher);
			break;
		case DeviceType.DEVICE_TYPE_CURTAIN:
			iconView.setImageResource(R.drawable.icon_curtain);
			break;
		case DeviceType.DEVICE_TYPE_ELETRIC_METER:
			iconView.setImageResource(R.drawable.icon_eletric_meter);
			break;
		case DeviceType.DEVICE_TYPE_WATER_METER:
			iconView.setImageResource(R.drawable.icon_water_meter);
			break;
		case DeviceType.DEVICE_TYPE_HEAT_METER:
			iconView.setImageResource(R.drawable.icon_heat);
			break;
		case DeviceType.DEVICE_TYPE_SWITCH_SOCKET:
			iconView.setImageResource(R.drawable.icon_socket);
			break;
		case DeviceType.DEVICE_TYPE_GAS_METER:
			iconView.setImageResource(R.drawable.icon_gas_meter);
			break;
		case DeviceType.DEVICE_TYPE_BODY_INFARE:
			iconView.setImageResource(R.drawable.icon_body_infare);
			break;
		case DeviceType.DEVICE_TYPE_TEMPERATURE_HUMIDITY_SENSOR:
			iconView.setImageResource(R.drawable.icon_thermometer);
		    break;
		case DeviceType.DEVICE_TYPE_MAGNET:
			iconView.setImageResource(R.drawable.icon_magnatic);
			break;
		case DeviceType.DEVICE_TYPE_CAMERA:
			iconView.setImageResource(R.drawable.icon_camera);
			break;
		default:
			iconView.setImageResource(R.drawable.icon_unknow_device);
			break;
		}
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case android.R.id.home:
			Intent intent = new Intent();
			Bundle bundle = new Bundle();
			bundle.putSerializable("com.smartdevice.mode.Device", attrView.getDeviceInfo());
			intent.putExtras(bundle);
			setResult(RESULT_OK, intent);
			finish();
			break;

		default:
			break;
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
//		Intent intent = new Intent();
//		Bundle bundle = new Bundle();
//		bundle.putSerializable("com.smartdevice.mode.Device", attrView.getDeviceInfo());
//		intent.putExtras(bundle);
//		setResult(RESULT_OK, intent);
//		finish();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Log.i(TAG, "onClicked!");
		switch (v.getId()) {
		case R.id.id_actionbar_icon:
			Intent intent = new Intent();
			Bundle bundle = new Bundle();
			bundle.putSerializable("com.smartdevice.mode.Device", attrView.getDeviceInfo());
			intent.putExtras(bundle);
			setResult(RESULT_OK, intent);
			finish();
			break;

		case R.id.id_actionbar_menu_tx:
			deleteRoomDevice();
			
		default:
			break;
		}
	}
	
	private void deleteRoomDevice(){
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				RoomControl control = new RoomControl();
				try {
					result = control.DeleteRoomBindDevice(bindDevice.getId());
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
				CustUtils.showToast(getApplicationContext(), R.string.grobal_delete_successful);
				setResult(RESULT_OK);
				finish();
			}
		}
    };
}
