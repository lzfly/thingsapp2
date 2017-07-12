package com.smartdevice.main;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;

import com.smartdevice.control.DeviceControl;
import com.smartdevice.mode.BaseDeviceType;
import com.smartdevice.mode.Device;
import com.smartdevice.mode.DeviceAdapter;
import com.smartdevice.mode.DeviceType;

public class BaseDeviceFragment extends Fragment{

	public ListView deviceListView;
	public DeviceAdapter deviceAdapter;
	public DeviceControl deviceControl;
	public LinearLayout linearLayout;
	public TextView waitView;
	public int devType;
	public DeviceControl control;
	private List<Device> allDeviceList;
	
	public BaseDeviceFragment(int devType, List<Device> devices){
		this.devType = devType;
		control = new DeviceControl();
		allDeviceList = new ArrayList<>();
		allDeviceList = devices;
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		linearLayout = (LinearLayout) inflater.inflate(R.layout.device_list_layout, container, false);
		deviceListView = (ListView) linearLayout.findViewById(R.id.id_device_list);
        deviceAdapter = new DeviceAdapter(getActivity().getApplicationContext());
		waitView = (TextView) linearLayout.findViewById(R.id.id_wait);
		
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Message msg = new Message();
				try {
					switch (devType) {
					case BaseDeviceType.BASE_DEVICE_TYPE_ALARM:
						msg.obj =  control.getAlarmDeviceList(allDeviceList);
						break;
                    
					case BaseDeviceType.BASE_DEVICE_TYPE_SENSOR:
						msg.obj =  control.getSensorDeviceList(allDeviceList);
						break;
					case BaseDeviceType.BASE_DEVICE_TYPE_SWITCH:
						msg.obj =  control.getSwitchDeviceList(allDeviceList);
						break;
					case BaseDeviceType.BASE_DEVICE_TYPE_CAMERA:
						msg.obj =  control.getCameraDeviceList(allDeviceList);
						break;
					case BaseDeviceType.BASE_DEVICE_TYPE_OTHERS:
						msg.obj =  control.getOtherDeviceList(allDeviceList);
						break;
					default:
						break;
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				handler.sendMessage(msg);
			}
		}).start();
		
		return linearLayout;
	}
	
	
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			deviceAdapter.setDeviceList((List<Device>)msg.obj);
			waitView.setVisibility(View.GONE);
			deviceListView.setAdapter(deviceAdapter);	
			Log.i(AllDevicesFragment.class.getSimpleName(), "after set adapter...");
			
		}
		
	};
	
	
}
