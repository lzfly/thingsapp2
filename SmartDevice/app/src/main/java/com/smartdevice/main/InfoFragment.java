package com.smartdevice.main;

import com.smartdevice.main.linkdevice.LinkDeviceActivity;
import com.smartdevice.ui.CustActionBar;
import com.smartdevice.ui.LinearView;
import com.smartdevice.utils.ParamsUtils;

import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class InfoFragment extends Fragment {

	private static final String TAG = InfoFragment.class.getSimpleName();
	private View mView;
	private LinearLayout mInfoLayout;
	private LinearLayout mInfoSettingsLayout;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mView = inflater.inflate(R.layout.fragment_info, container, false);
		mInfoLayout = (LinearLayout) mView.findViewById(R.id.id_info_ly);
		mInfoSettingsLayout = (LinearLayout) mView.findViewById(R.id.id_info_settings_ly);
		init();
		LinearView bindView= new LinearView(getActivity().getApplicationContext(),
				R.drawable.icon_binding, R.string.binding_gateway);
		mInfoSettingsLayout.addView(bindView.createLinearView());
		
		bindView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.i(TAG, "Info view was on clicked.");
				Intent intent = new Intent(getActivity(), BindGatewayActivity.class);
				startActivity(intent);
			}
		});

//		LinearView settingsView= new LinearView(getActivity().getApplicationContext());
//		mInfoSettingsLayout.addView(settingsView.createLinearView());
		
		return mView;
	}
	
	public void init(){
        View settingsView =  mView.findViewById(R.id.id_system_settings);
        View linkdeviceView = mView.findViewById(R.id.id_link_device); 
		TextView logoutView = (TextView) mView.findViewById(R.id.id_logout_view);
		mOnClickListener listener = new mOnClickListener();
		logoutView.setOnClickListener(listener);
        settingsView.setOnClickListener(listener);
        linkdeviceView.setOnClickListener(listener);
	}
	
	class mOnClickListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.id_logout_view:
				logout();
				break;
			case R.id.id_system_settings:
				Intent intent = new Intent(getActivity(), SettingsActivity.class);
				startActivity(intent);
				break;
				
			case R.id.id_link_device:
				Intent linkIntent = new Intent(getActivity(), LinkDeviceActivity.class);
				startActivity(linkIntent);
				break;
			default:
				break;
			}
		}
	}
	
	public void logout(){
		/*remove user information*/
		SharedPreferences preferences = getActivity().getSharedPreferences(ParamsUtils.USER_INFO,
				getActivity().MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.remove(ParamsUtils.USER_NAME);
		editor.remove(ParamsUtils.PASSWORD);
		editor.remove(ParamsUtils.SESSIONID);
		editor.commit();
		getActivity().finish();
	}
	
	
}
