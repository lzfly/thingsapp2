package com.smartdevice.main;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import android.content.Intent;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;

import com.smartdevice.control.DeviceControl;
import com.smartdevice.mode.Device;
import com.smartdevice.mode.DeviceAdapter;

public class LightFragment extends Fragment{

	public ListView deviceListView;
	public DeviceAdapter deviceAdapter;
	public DeviceControl deviceControl;
	public LinearLayout linearLayout;
	public TextView waitView;
	
	private List<Device> allDeviceList;
	
	public LightFragment(List<Device> devices){
		allDeviceList = new ArrayList<>();
		allDeviceList = devices;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stubs
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
					msg.obj =  getLightDevice();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				handler.sendMessage(msg);
			}
		}).start();
		
		deviceListView.setOnItemClickListener(new lightItemClickListener());
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
	
	
	public List<Device> getLightDevice() throws JSONException{
		/*Read all of remote the meters' value*/
		DeviceControl control = new DeviceControl();
		return control.getLightDeviceList(allDeviceList);
	}
	
	class lightItemClickListener implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(getActivity(), DeviceInfoActivity.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable("com.smartdevice.mode.Device", (Serializable) view.getTag());
			intent.putExtras(bundle);
			startActivity(intent);
		}
		
	}
}
