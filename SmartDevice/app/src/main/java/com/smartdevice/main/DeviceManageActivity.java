package com.smartdevice.main;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import com.smartdevice.control.DeviceControl;
import com.smartdevice.mode.BaseDeviceType;
import com.smartdevice.mode.Device;
import com.smartdevice.mode.DeviceTypeAdapter;
import com.smartdevice.utils.CustActionBar;

import android.app.ActionBar;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

public class DeviceManageActivity extends ActionBarActivity{
    private final static String TAG = DeviceManageActivity.class.getSimpleName();
	private ListView deviceTypeListView;
	private List<Object> deviceTypeList;
	private LinearLayout deviceListContainer;
	
	private FragmentManager mFragmentManager;
	private MeterFragment meterFragment;
	private LightFragment lightFragment;
	private TextView listTextView;
	private AllDevicesFragment allDevicesFragment;
	
	private BaseDeviceFragment baseDeviceFragment;
	
	public List<Device> allDeviceList;
	
	private static boolean isReady = false;
	private ActionBar mActionBar;
	private CustActionBar actionBarView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Log.i(TAG, "onCreate...");
		setContentView(R.layout.device_manage_layout);
		
		/*
		mActionBar = getActionBar();
		actionBarView = new ActionBarView(mActionBar);
		actionBarView.setTitle(R.string.devices_str);
		Log.i("DeviceManagerActivity", "Height " + mActionBar.getHeight());
		actionBarView.setHeight(mActionBar.getHeight());
		*/
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(false);
		getSupportActionBar().setTitle(R.string.devices_str);
		
		deviceTypeListView = (ListView)findViewById(R.id.id_devicelist_types);
		deviceTypeListView.setLayoutParams(new LinearLayout.LayoutParams
				(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		listTextView = (TextView) findViewById(R.id.id_devicelist_txt);
		DeviceTypeAdapter deviceTypeAdapter = new DeviceTypeAdapter(this);
        deviceTypeAdapter.setArrayList(getDeviceTypeList());
		deviceTypeListView.setAdapter(deviceTypeAdapter);
		deviceListContainer = (LinearLayout) findViewById(R.id.id_devicelist_container);
		deviceTypeListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				int itemId = (int) deviceTypeListView.getAdapter().getItemId(
						position);
				String typeName = ((DeviceTypeAdapter) deviceTypeListView
						.getAdapter()).getItemName(position);
				listTextView.setText(typeName);
				Log.i(TAG, "Item ID " + itemId);
				if (isReady) {
					switch (itemId) {
					case BaseDeviceType.BASE_DEVICE_TYPE_METER:
						Log.i(TAG, "Device Type "
								+ BaseDeviceType.BASE_DEVICE_TYPE_METER);
						if (meterFragment == null)
							meterFragment = new MeterFragment(allDeviceList);
						setCurrentFragment(meterFragment);
						break;

					case BaseDeviceType.BASE_DEVICE_TYPE_LIGHTS:
						Log.i(TAG, "Device Type "
								+ BaseDeviceType.BASE_DEVICE_TYPE_LIGHTS);
						if (lightFragment == null)
							lightFragment = new LightFragment(allDeviceList);
						setCurrentFragment(lightFragment);
						break;
					case BaseDeviceType.BASE_DEVICE_TYPE_SWITCH:
						baseDeviceFragment = new BaseDeviceFragment(itemId,
								allDeviceList);
						setCurrentFragment(baseDeviceFragment);
						break;
					case BaseDeviceType.BASE_DEVICE_TYPE_ALARM:
						baseDeviceFragment = new BaseDeviceFragment(itemId,
								allDeviceList);
						setCurrentFragment(baseDeviceFragment);
						break;
					case BaseDeviceType.BASE_DEVICE_TYPE_SENSOR:
						baseDeviceFragment = new BaseDeviceFragment(itemId,
								allDeviceList);
						setCurrentFragment(baseDeviceFragment);
						break;
					case BaseDeviceType.BASE_DEVICE_TYPE_CAMERA:
						baseDeviceFragment = new BaseDeviceFragment(itemId,
								allDeviceList);
						setCurrentFragment(baseDeviceFragment);
						break;
					case BaseDeviceType.BASE_DEVICE_TYPE_OTHERS:
						baseDeviceFragment = new BaseDeviceFragment(itemId,
								allDeviceList);
						setCurrentFragment(baseDeviceFragment);
						break;
					case BaseDeviceType.BASE_DEVICE_TYPE_ALL:
						if (allDevicesFragment == null)
							allDevicesFragment = new AllDevicesFragment(
									allDeviceList);
						setCurrentFragment(allDevicesFragment);
						break;
					default:
						break;
					}
				}
			}
		});
		
		/*Get all the devices list at the first time*/
		allDeviceList = new ArrayList<>();
		if(allDeviceList == null || allDeviceList.size() == 0){
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					Message msg = new Message();
					allDeviceList = getAllDeviceList();
					msg.obj = allDeviceList;
					msg.arg1 = 1;
					handler.sendMessage(msg);
				}
				
			}).start();
			
		}
        
		/*
		if (savedInstanceState == null) {
			mFragmentManager = getSupportFragmentManager();
			mFragmentManager.beginTransaction().add(R.id.id_devicelist_container, meterFragment).commit();
		}
		*/
	}
	
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			if(msg.arg1 == 1)
				isReady = true;
			if (isReady) {
				meterFragment = new MeterFragment(allDeviceList);
				mFragmentManager = getSupportFragmentManager();
				mFragmentManager.beginTransaction()
						.add(R.id.id_devicelist_container, meterFragment)
						.commit();
			}
		}
	};
	
	private void setCurrentFragment(Fragment fragment){
		FragmentTransaction transaction = mFragmentManager.beginTransaction();
		transaction.replace(R.id.id_devicelist_container, fragment);
		transaction.commit();
	}
	
	private List<Object> getDeviceTypeList(){
		deviceTypeList = new ArrayList<>();
		int[] dtArrayId = getResources().getIntArray(R.array.device_types_id);
		String[] dtArrayName = getResources().getStringArray(R.array.device_types);
		for (int i = 0; i < dtArrayId.length; i++) {
			Log.i(TAG, "Device Type " + dtArrayId[i]);
			BaseDeviceType  baseDeviceType= new BaseDeviceType();
			baseDeviceType.setBaseTypeId(dtArrayId[i]);
			baseDeviceType.setBaseTypeName(dtArrayName[i]);
			deviceTypeList.add(baseDeviceType);
		}
		return deviceTypeList;
	}
	
	private List<Device> getAllDeviceList(){
		List<Device> allDeviceList = new ArrayList<>();
		
		DeviceControl control = new DeviceControl();
		try {
			allDeviceList = control.getAllDevices();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return allDeviceList;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;

		default:
			break;
		}
		
		return super.onOptionsItemSelected(item);
	}

}
