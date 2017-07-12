package com.smartdevice.main;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import com.smartdevice.control.DeviceControl;
import com.smartdevice.mode.Device;
import com.smartdevice.mode.DeviceAdapter;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class MeterFragment extends Fragment{

	private ListView deviceListView;
	private DeviceAdapter deviceAdapter;
	private LinearLayout linearLayout;
	private TextView waitView;
	private List<Device> allDeviceList;
	
	public MeterFragment(List<Device> devices){
		allDeviceList = new ArrayList<>();
		allDeviceList = devices;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		linearLayout = (LinearLayout) inflater.inflate(R.layout.device_list_layout, container, false);
        
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Message msg = new Message();
				try {
					msg.obj =  getMeterDevice();
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
			deviceListView = (ListView) linearLayout.findViewById(R.id.id_device_list);
	        deviceAdapter = new DeviceAdapter(getActivity().getApplicationContext());
			deviceAdapter.setDeviceList((List<Device>)msg.obj);
			waitView = (TextView) linearLayout.findViewById(R.id.id_wait);
			waitView.setVisibility(View.GONE);
			deviceListView.setAdapter(deviceAdapter);	
			Log.i(AllDevicesFragment.class.getSimpleName(), "after set adapter...");
			
		}
		
	};
	
	
	public List<Device> getMeterDevice() throws JSONException{
		/*Read all of remote the meters' value*/
		DeviceControl control = new DeviceControl();
		return control.getMeterDeviceList(allDeviceList);
	}

}
