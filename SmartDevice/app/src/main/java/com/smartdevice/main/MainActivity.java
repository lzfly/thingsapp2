package com.smartdevice.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;

import com.smartdevice.control.DeviceControl;
import com.smartdevice.mode.Device;
import com.smartdevice.mode.Session;
import com.smartdevice.service.MqttService;
import com.smartdevice.ui.CustActionBar;
import com.smartdevice.ui.RedHintView;
import com.smartdevice.utils.ACache;
import com.smartdevice.utils.CustUtils;
import com.smartdevice.utils.DensityUtil;
import com.smartdevice.utils.LogUtil;
import com.smartdevice.utils.ParamsUtils;

import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.R.menu;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.util.ArrayMap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity implements OnClickListener{
    private final static String TAG = MainActivity.class.getSimpleName();
	FragmentManager mFragmentManager;
	MessageFragment msgFragment;
	HomeFragment homeFragment;
	DevicesFragment devicesFragment;
	SenerioFragment senerioFragment;
	InfoFragment infoFragment;
	TextView homeTextView;
	//TextView msgTextView;
	View msgNavContainer;
	RedHintView msgTextView;
	TextView infoTextView;
	TextView senTextView;
	private Drawable homeDrawable;
	private Drawable msgDrawable;
	private Drawable infoDrawable;
	private Drawable homeSelectedDrawable;
	private Drawable msgSelectedDrawable;
	private Drawable infoSelectedDrawable;
	private Drawable senerioDrawable;
	private Drawable senerioSelectedDrawable;
	
	public List<Device> allDeviceList;
	
	private int gatewayCount;
	
	public static final String SERVICE_CLASSNAME = "com.smartdevice.service.MQTTService";
	
	private CustActionBar custActionBar;
	
	private String currentFragment;
	
	private int backPressedCount = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		custActionBar = new CustActionBar(getSupportActionBar());
		custActionBar.getTitleView().setVisibility(View.GONE);
		custActionBar.setMenuVisible(View.VISIBLE);
		custActionBar.getMenuView().setImageResource(R.drawable.icon_add);
		
//		if (android.os.Build.VERSION.SDK_INT > 18) {
//
//			   Window window = getWindow();
//			   window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
//			   WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//		}
		
		if(!serviceIsRunning())
			startMQTTService();
		
		homeTextView = (TextView) findViewById(R.id.id_home_nav);
		msgNavContainer = findViewById(R.id.id_msg_nav);
		msgTextView =  (RedHintView) findViewById(R.id.id_msg_nav_hint);
		
		msgTextView.setHintViewVisibility(View.VISIBLE);  //this is just for test!!!
		
		infoTextView = (TextView) findViewById(R.id.id_info_nav);
		senTextView = (TextView) findViewById(R.id.id_senerio_nav);
		homeTextView.setOnClickListener(this); 
		//msgTextView.setOnClickListener(this);
		msgNavContainer.setOnClickListener(this);
	    infoTextView.setOnClickListener(this);
	    senTextView.setOnClickListener(this);
	    homeDrawable = getResources().getDrawable(R.drawable.icon_home);
        msgDrawable = getResources().getDrawable(R.drawable.icon_msg);
        infoDrawable = getResources().getDrawable(R.drawable.icon_info);
        senerioDrawable = getResources().getDrawable(R.drawable.icon_senerio);
        homeSelectedDrawable = getResources().getDrawable(R.drawable.icon_home_selected);
        msgSelectedDrawable = getResources().getDrawable(R.drawable.icon_msg_selected);
        infoSelectedDrawable = getResources().getDrawable(R.drawable.icon_info_selected);
        senerioSelectedDrawable = getResources().getDrawable(R.drawable.icon_senerio_selected);
        
        mFragmentManager = getSupportFragmentManager();
        if(savedInstanceState != null){
        	LogUtil.i(TAG, "savedInstanceState is not null, currentFragment is " + currentFragment);
		}else {
			devicesFragment = new DevicesFragment(getApplicationContext());
			currentFragment = "device";
			mFragmentManager.beginTransaction().add(R.id.container, devicesFragment).commitAllowingStateLoss();
		}
        
		
		SharedPreferences preferences = this.getSharedPreferences(ParamsUtils.USER_INFO, MODE_PRIVATE);
		Session.setUsername(preferences.getString(ParamsUtils.USER_NAME, null));
		Session.setSessionId(preferences.getString(ParamsUtils.SESSIONID, null));
	}
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		outState.putString("currentFragment", currentFragment);
		//super.onSaveInstanceState(outState);
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		currentFragment = savedInstanceState.getString("currentFragment");
		devicesFragment = new DevicesFragment();
		addFragment(devicesFragment, "device");
		setCurrentFragment(devicesFragment, "device");
		super.onRestoreInstanceState(savedInstanceState);
	}
	
	private Map<String, List<Device>> getDeviceMap(){
		
		/*Get binded gateways*/
		SharedPreferences gatewayPreferences = this.getSharedPreferences(ParamsUtils.GATEWAY_COUNT, MODE_PRIVATE);
		gatewayCount = gatewayPreferences.getInt(ParamsUtils.GATEWAY_COUNT_CURRENT, 0);
		ACache mCache = ACache.get(this);
		Map<String, List<Device>> deviceMap = new HashMap<String, List<Device>>();

		if(gatewayCount == 0){
			/*Get binded gateways from server*/
			
		}else {
			for (int i = 1; i <= gatewayCount; i++) {
				String gatewaySN = mCache.getAsString("gateway" + i);
				DeviceControl deviceControl = new DeviceControl();
				List<Device> deviceList = deviceControl.getDevices(gatewaySN);
				deviceMap.put(gatewaySN, deviceList);
			}
		}
		
		return deviceMap;
	}
	
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			if(msg.arg1 == 1)
				devicesFragment = new DevicesFragment(getApplicationContext());
				mFragmentManager = getSupportFragmentManager();
				mFragmentManager.beginTransaction()
						.add(R.id.container, devicesFragment)
						.commitAllowingStateLoss();
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		//if (id == R.id.action_settings) {
			Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
		//}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_home, container,
					false);
			return rootView;
		}
	}
	
	private void setCurrentFragment(Fragment fragment, String tag){
		FragmentTransaction transaction = mFragmentManager.beginTransaction();
		hideFragments(transaction);
		//transaction.replace(R.id.container, fragment);
		currentFragment = tag;
		transaction.show(fragment);
		transaction.commit();
	}
	
	private void addFragment(Fragment fragment, String tag){
		FragmentTransaction transaction = mFragmentManager.beginTransaction();
		transaction.add(R.id.container, fragment, tag);
		transaction.commit();
	}
	
	private void hideFragments(FragmentTransaction ft){
		if(devicesFragment != null)
			ft.hide(devicesFragment);
		if(senerioFragment != null)
			ft.hide(senerioFragment);
		if(infoFragment != null)
			ft.hide(infoFragment);
		if(msgFragment != null)
			ft.hide(msgFragment);
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		clearChioce();
		switch (view.getId()) {
		case R.id.id_senerio_nav:
			senerioSelectedDrawable.setBounds(0, 0, 
					senerioSelectedDrawable.getMinimumWidth(), senerioSelectedDrawable.getMinimumHeight());
			senTextView.setCompoundDrawables(null, senerioSelectedDrawable, null, null);
			if(senerioFragment == null){
				senerioFragment = new SenerioFragment();
				addFragment(senerioFragment, "senerio");
			}
		    setCurrentFragment(senerioFragment, "senerio");
			custActionBar.setIconVisible(View.GONE);
			custActionBar.setTitleVisible(View.VISIBLE);
			custActionBar.setMenuVisible(View.GONE);
			custActionBar.setTitle(R.string.senerio_str);
			break;
		case R.id.id_home_nav:
			homeSelectedDrawable.setBounds(0, 0, homeSelectedDrawable.getMinimumWidth(), homeSelectedDrawable.getMinimumHeight());
			homeTextView.setCompoundDrawables(null, homeSelectedDrawable, null, null);
			if(devicesFragment == null){
				LogUtil.d("MainActivity", "DevicesFragment is null !");
				devicesFragment = new  DevicesFragment(allDeviceList);
				addFragment(devicesFragment, "device");
			}
			setCurrentFragment(devicesFragment, "device");
			
			custActionBar.setMenuVisible(View.VISIBLE);
			custActionBar.setIconVisible(View.VISIBLE);
			custActionBar.setTitleVisible(View.GONE);
			break;
			
		case R.id.id_msg_nav:
			//msgSelectedDrawable.setBounds(0, 0, msgSelectedDrawable.getMinimumWidth(), msgSelectedDrawable.getMinimumHeight());
			//msgTextView.setCompoundDrawables(null, msgSelectedDrawable, null, null);
			msgTextView.setHostImageDrawable(msgSelectedDrawable);
			msgTextView.invisbleHintView();
			if(msgFragment == null){
				msgFragment = new MessageFragment();
				addFragment(msgFragment, "message");
			}
			setCurrentFragment(msgFragment, "message");
			
			custActionBar.setIconVisible(View.GONE);
			custActionBar.setTitleVisible(View.VISIBLE);
			custActionBar.setMenuVisible(View.GONE);
			custActionBar.setTitle(R.string.msg_str);
			break;
			
		case R.id.id_info_nav:
			infoSelectedDrawable.setBounds(0, 0, infoSelectedDrawable.getMinimumWidth(), infoSelectedDrawable.getMinimumHeight());
			infoTextView.setCompoundDrawables(null, infoSelectedDrawable, null, null);
			if(infoFragment == null){
				infoFragment = new InfoFragment();
				addFragment(infoFragment, "info");
			}
			setCurrentFragment(infoFragment, "info");
			custActionBar.setIconVisible(View.GONE);
			custActionBar.setTitleVisible(View.VISIBLE);
			custActionBar.setMenuVisible(View.GONE);
			custActionBar.setTitle(R.string.info_str);
			break;

		default:
			break;
		}
	}
	
	public void clearChioce()
	{
		//homeDrawable = getResources().getDrawable(R.drawable.icon_home);
		homeDrawable.setBounds(0, 0, homeDrawable.getMinimumWidth(), homeDrawable.getMinimumHeight());
		homeTextView.setCompoundDrawables(null, homeDrawable, null, null);
		senerioDrawable.setBounds(0, 0, senerioDrawable.getMinimumWidth(), senerioDrawable.getMinimumHeight());
		senTextView.setCompoundDrawables(null, senerioDrawable, null, null);
		msgDrawable.setBounds(0, 0, msgDrawable.getMinimumWidth(), msgDrawable.getMinimumHeight());
		//msgTextView.setCompoundDrawables(null, msgDrawable, null, null);
		msgTextView.setHostImageDrawable(msgDrawable);
		infoDrawable.setBounds(0, 0, infoDrawable.getMinimumWidth(), infoDrawable.getMinimumHeight());
		infoTextView.setCompoundDrawables(null, infoDrawable, null, null);
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
	
	private void startMQTTService() {

        final Intent intent = new Intent(this, MqttService.class);
        startService(intent);
    }

    private void stopMQTTService() {

        final Intent intent = new Intent(this, MqttService.class);
        stopService(intent);
    }
	
	private boolean serviceIsRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (SERVICE_CLASSNAME.equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		LogUtil.i(TAG, "Get back pressed event here");
		if(senerioFragment != null){
			if(senerioFragment.getGroupAdapter() != null){
				PopupWindow popupWindow = senerioFragment.getGroupAdapter().getPopupWindow();
				if(popupWindow != null && popupWindow.isShowing()){
					popupWindow.dismiss();
				}else {
					backPressedCount ++;
					if(backPressedCount >= 2){
						super.onBackPressed();
					}else {
						CustUtils.showToast(this, R.string.back_exit);
					}
				}
			}else {
				backPressedCount ++;
				if(backPressedCount >= 2){
					super.onBackPressed();
				}else {
					CustUtils.showToast(this, R.string.back_exit);
				}
			}
		}else {
			backPressedCount ++;
			if(backPressedCount >= 2){
				super.onBackPressed();
			}else {
				CustUtils.showToast(this, R.string.back_exit);
			}
		}
		
	}
	
}
