package com.smartdevice.main;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;

import android.R.integer;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RelativeLayout.LayoutParams;

import com.smartdevice.control.BaseDeviceControl;
import com.smartdevice.control.DeviceControl;
import com.smartdevice.control.GatewayControl;
import com.smartdevice.control.RoomControl;
import com.smartdevice.main.LightFragment.lightItemClickListener;
import com.smartdevice.main.room.AddRoomActivity;
import com.smartdevice.main.room.RoomInfoActivity;
import com.smartdevice.mode.Device;
import com.smartdevice.mode.DeviceAdapter;
import com.smartdevice.mode.DeviceType;
import com.smartdevice.mode.Gateway;
import com.smartdevice.mode.Room;
import com.smartdevice.mode.Session;
import com.smartdevice.utils.ACache;
import com.smartdevice.utils.Network;
import com.smartdevice.utils.ParamsUtils;

public class AllDevicesFragment extends Fragment implements OnClickListener{
	
	private ListView deviceListView;
	private DeviceAdapter deviceAdapter;
	private DeviceControl deviceControl;
	private LinearLayout linearLayout;
	private TextView waitView;
	private List<Device> allDeviceList;
	private Map<String, List<Device>> deviceMap;
	private Context context;
	RelativeLayout waitLayout;
	
	private TextView deviceView;
	private TextView deviceTypeView;
	private View deviceTypeLayout;
	private View deviceListLayout;
	private TextView testTextView;
	TextView waitIcon;
	ProgressBar loadingBar;
	
	private int gatewayCount;
	private int cacheGatewayCount;
	List<Gateway> bindedGatewayList;
	public static final String deviceIntentFilter = "com.smartdevice.main.device.update";
	private static final int GET_DEVICE_INFO_REQUEST = 0x0;
	private static final int ADD_ROOM_REQUEST = 0x1;
	private static final int GET_ROOM_INFO_REQUEST = 0x2;
	private List<Room> roomList;
	GridView roomGridView;
	
	private IntentFilter filter = 
			  new IntentFilter(deviceIntentFilter);
			 
	private MyBroadcastReceiver receiver = 
			  new MyBroadcastReceiver();
	
	public class MyBroadcastReceiver extends BroadcastReceiver { 
		   @Override
		   public void onReceive(Context context, Intent intent) { 
		     //TODO: React to the Intent received. 
			   Log.i(AllDevicesFragment.class.getSimpleName(), "MyBroadcastReceiver");
			   updateDeiveList();
		   } 
		}
	
	public AllDevicesFragment(List<Device> devices){
		allDeviceList = new ArrayList<>();
		allDeviceList = devices;
	}
	
	public AllDevicesFragment(Context context){
		this.context = context;
		deviceControl = new DeviceControl();
	}
	
	public AllDevicesFragment(){
		this.context = getActivity();
		deviceControl = new DeviceControl();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		//container = (LinearLayout) inflater.inflate(R.layout.device_list_layout, container, false);
		linearLayout = (LinearLayout) container; 
		deviceListLayout = linearLayout.findViewById(R.id.id_device_list_layout);
		deviceListView = (ListView) linearLayout.findViewById(R.id.id_device_list);
		waitLayout = (RelativeLayout) linearLayout.findViewById(R.id.id_wait_view);
		deviceListView.setOnItemClickListener(new DeviceItemClickListener());
		deviceView = (TextView) linearLayout.findViewById(R.id.id_alldevice);
		deviceTypeView = (TextView) linearLayout.findViewById(R.id.id_devicetype);
		deviceTypeLayout = linearLayout.findViewById(R.id.id_device_type_layout);
		loadingBar = (ProgressBar) linearLayout.findViewById(R.id.id_loading_devices_bar);
		deviceView.setOnClickListener(this);
		deviceTypeView.setOnClickListener(this);
		if(context == null)
			context = getActivity();
	    deviceAdapter = new DeviceAdapter(context);
        
		waitView = (TextView) linearLayout.findViewById(R.id.id_wait);
		waitIcon = (TextView) linearLayout.findViewById(R.id.id_wait_icon);
		Network network = new Network(context);
		if(!network.isConnected()){
			loadingBar.setVisibility(View.GONE);
			waitLayout.setVisibility(View.VISIBLE);
			waitView.setText(R.string.network_fail);
		}else {
			/*SharedPreferences preferences = context.getSharedPreferences(
					ParamsUtils.GATEWAY_COUNT, context.MODE_PRIVATE);
			cacheGatewayCount = preferences.getInt(ParamsUtils.GATEWAY_COUNT_CURRENT, 0);*/
			cacheGatewayCount = 0;
			if(cacheGatewayCount == 0){
				
			   new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					GatewayControl gatewayControl = new GatewayControl();
					bindedGatewayList = gatewayControl.getBindedGateways(Session.getUsername());
					Message message = new Message();
					if(bindedGatewayList != null){
						gatewayCount = bindedGatewayList.size();
					}else {
						gatewayCount = 0;
					}
					message.arg1 = gatewayCount;
					gatewayhHandler.sendMessage(message);
				}
			}).start();	
		   }else {
			//....
		 }
		}

		//LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(getActivity());

		context.registerReceiver(receiver, filter);
         
		Log.i(AllDevicesFragment.class.getSimpleName(), "Before return view...");
		initRoom();
		return linearLayout;
	}
	
	private void initRoom(){
		View addRoomView = linearLayout.findViewById(R.id.id_add_room);
		addRoomView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(), AddRoomActivity.class);
				startActivityForResult(intent, ADD_ROOM_REQUEST);
			}
		});
		
		roomGridView = (GridView) linearLayout.findViewById(R.id.id_room_grid);
		roomGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(), RoomInfoActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("com.smartdevice.mode.Room", roomList.get(position));
				intent.putExtras(bundle);
				startActivityForResult(intent, GET_ROOM_INFO_REQUEST);
			}
		});
	}
	
	public  void updateDeiveList(){

		   new Thread(new Runnable() {
				
			@Override
			public void run() {
				// TODO Auto-generated method stub
				GatewayControl gatewayControl = new GatewayControl();
				bindedGatewayList = gatewayControl.getBindedGateways(Session.getUsername());
				Message message = new Message();
				if(bindedGatewayList != null){
					gatewayCount = bindedGatewayList.size();
				}else {
					gatewayCount = 0;
				}
				message.arg1 = gatewayCount;
				gatewayhHandler.sendMessage(message);
			}
		}).start();
		
	}
	
	private void getRoomList(){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				RoomControl control = new RoomControl();
				roomList = control.getRooms(Session.getUsername());
				roomHandler.sendEmptyMessage(0);
			}
		}).start();
	}
	
	private Handler roomHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			if(roomList != null && roomList.size() > 0){
				List<Map<String, ?>> tempRoomList = new ArrayList<>();
				for (int i = 0; i < roomList.size(); i++) {
					Map<String, Object> roomMap = new HashMap<>();
					roomMap.put("icon", R.drawable.icon_living_room);
					roomMap.put("name", roomList.get(i).getRoomName());
					tempRoomList.add(roomMap);
				}
				SimpleAdapter simpleAdapter = new SimpleAdapter(getActivity(),
						(List<? extends Map<String, ?>>) tempRoomList, 
						R.layout.room_item_layout, 
						new String[]{"icon", "name"}, 
						new int[]{R.id.id_room_icon, R.id.id_room_name});
				roomGridView.setAdapter(simpleAdapter);
			}else {
				List<Map<String, ?>> tempRoomList = new ArrayList<>();
				Map<String, Object> roomMap = new HashMap<>();
//				roomMap.put("icon", R.drawable.icon_living_room);
//				roomMap.put("name", roomList.get(i).getRoomName());
				tempRoomList.add(roomMap);
				SimpleAdapter simpleAdapter = new SimpleAdapter(getActivity(),
						tempRoomList,
						R.layout.room_item_layout, 
						new String[]{"icon", "name"}, 
						new int[]{R.id.id_room_icon, R.id.id_room_name});
				roomGridView.setAdapter(simpleAdapter);
			}
		};
	};
	
	private Handler gatewayhHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
		gatewayCount = msg.arg1;
        if(gatewayCount == 0){
        	loadingBar.setVisibility(View.GONE);
			waitLayout.setVisibility(View.VISIBLE);
				waitView.setText(R.string.bind_gatway_firstly);
				waitIcon.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(context, AddGatewayActivity.class);
						startActivity(intent);
					}
				});
			}else {
				/*Should run net access in child thread, in main thread will be throw 
				  error after android v4.0*/
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						Message msg = new Message();
						
						if(deviceMap != null){
							deviceMap.clear();
						}

						getDeviceMap();
						
						msg.obj = deviceMap;
						handler.sendMessage(msg);
					}
				}).start();
			}
		};
	};
	
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			deviceListView = (ListView) linearLayout.findViewById(R.id.id_device_list);
			//deviceAdapter.setDeviceList((List<Device>)msg.obj);
			if(msg.obj != null &&
					((Map<String, List<Device>>)msg.obj).size() != 0){

				deviceAdapter.setDeviceMap((Map<String, List<Device>>)msg.obj);
				deviceAdapter.getAllDeviceList();
				waitView = (TextView) linearLayout.findViewById(R.id.id_wait);
				Log.i(AllDevicesFragment.class.getSimpleName(), "Device map size " + deviceMap.size());
				loadingBar.setVisibility(View.GONE);
				if(deviceMap.size() != 0){
					waitLayout.setVisibility(View.GONE);
				}else {
					waitLayout.setVisibility(View.VISIBLE);
					waitView.setText(R.string.device_not_find);
				}

				deviceListView.setAdapter(deviceAdapter);	


			}else {
				/**/
				loadingBar.setVisibility(View.GONE);
				waitLayout.setVisibility(View.VISIBLE);
				waitIcon.setVisibility(View.GONE);
				waitView.setText(R.string.device_not_find);
			}
		}
		
	};

	class DeviceItemClickListener implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			Device device = (Device) view.getTag();
			if(device.getDeviceType().getTypeCode() == DeviceType.DEVICE_TYPE_CAMERA)
			{
				Intent intent = new Intent(getActivity(), CameraPlayActivity.class);
				startActivity(intent);
			}
			else
			{
				Intent intent = new Intent(getActivity(), DeviceInfoActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("com.smartdevice.mode.Device", (Device) view.getTag());
				intent.putExtras(bundle);
				startActivityForResult(intent, GET_DEVICE_INFO_REQUEST);
			}
		}
		
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if(data != null){
			Device device = (Device) data.getSerializableExtra("com.smartdevice.mode.Device");
			if(device != null && device.getAttrList() != null){
				for (int i = 0; i < device.getAttrList().size(); i++) {
					deviceAdapter.updateDeviceAttrValue(device.getDeviceSN(), 
							device.getAttrList().get(i).getAttrCode(), 
							device.getAttrList().get(i).getAttrCurrentValue());
				}
			}
			deviceAdapter.notifyDataSetChanged();
		}else {
			if(resultCode == getActivity().RESULT_OK
					&& (requestCode == GET_ROOM_INFO_REQUEST
					|| requestCode == ADD_ROOM_REQUEST)){
				getRoomList();
			}
		}
			
	}
	
     private Map<String, List<Device>> getDeviceMap(){
		
		/*Get binded gateways*/
		//ACache mCache = ACache.get(getActivity().getApplicationContext());
		deviceMap = new HashMap<String, List<Device>>();

		if(gatewayCount == 0){
			/*Get binded gateways from server*/
			
			
		}else {
			for (int i = 1; i <= gatewayCount; i++) {
				//String gatewaySN = mCache.getAsString("gateway" + i);
				String gatewaySN = bindedGatewayList.get(i-1).getGatewaySN();
				DeviceControl deviceControl = new DeviceControl();
				List<Device> deviceList = deviceControl.getDevices(gatewaySN);
				if(deviceList != null && deviceList.size() != 0){
					deviceMap.put(gatewaySN, deviceList);
				}
			}
		}
		Log.i("AllDeviceFragment", "deviceMap size " + deviceMap.size());
		return deviceMap;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.id_devicetype:
			getRoomList();
			deviceTypeView.setBackgroundResource(R.drawable.device_type_button_selected_shape);
			deviceTypeView.setTextColor(getResources().getColor(R.color.main_turquoise_bg));
			deviceView.setBackgroundResource(R.drawable.device_button_shape);
			deviceView.setTextColor(getResources().getColor(R.color.white));
			deviceListLayout.setVisibility(View.GONE);
			deviceTypeLayout.setVisibility(View.VISIBLE);
			break;
		case R.id.id_alldevice:
			deviceView.setBackgroundResource(R.drawable.device_button_selected_shape);
			deviceTypeView.setBackgroundResource(R.drawable.device_type_button_shape);
			deviceView.setTextColor(getResources().getColor(R.color.main_turquoise_bg));
			deviceTypeView.setTextColor(getResources().getColor(R.color.white));
			deviceListLayout.setVisibility(View.VISIBLE);
			deviceTypeLayout.setVisibility(View.GONE);
			break;
		default:
			break;
		}
	}
}
