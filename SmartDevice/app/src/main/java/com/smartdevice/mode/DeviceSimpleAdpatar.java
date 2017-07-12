package com.smartdevice.mode;

import java.util.ArrayList;
import java.util.List;

import com.smartdevice.main.R;
import com.smartdevice.utils.CustUtils;
import com.smartdevice.utils.LogUtil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DeviceSimpleAdpatar extends BaseAdapter{
	
	private List<RoomBindDevice> deviceList;
	private Context context;
	
	public DeviceSimpleAdpatar(Context context, List<RoomBindDevice> deviceList) {
		// TODO Auto-generated constructor stub
		this.deviceList = deviceList;
		this.context = context;
		addLastButton();
	}
	
	public void setDeviceList(List<RoomBindDevice> deviceList){
		this.deviceList = deviceList;
		addLastButton();
	}
	
	public void updateDeviceList(RoomBindDevice device){
		if(deviceList == null){
			deviceList = new ArrayList<>();
		}
		deviceList.remove(getCount() - 1);
		deviceList.add(device);
		addLastButton();
	}
	
	public void addLastButton(){
		if(deviceList == null){
			deviceList = new ArrayList<>();
		}
		RoomBindDevice addDevice = new RoomBindDevice();
		addDevice.setFlag(false);
		deviceList.add(addDevice);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return deviceList.size();
	}

	@Override
	public RoomBindDevice getItem(int position) {
		// TODO Auto-generated method stub
		return deviceList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	private class ViewHolder{
		private ImageView iconView;
		private TextView nameView;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		if(convertView == null){
			holder = new ViewHolder();
			LayoutInflater inflater = LayoutInflater.from(context);
			convertView = inflater.inflate(R.layout.room_device_item_layout, null);
			holder.iconView = (ImageView) convertView.findViewById(R.id.id_room_device_icon);
			holder.nameView = (TextView) convertView.findViewById(R.id.id_room_device_name);
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		if(!getItem(position).isFlag()){
			holder.iconView.setImageResource(R.drawable.icon_add4);
			holder.nameView.setText(R.string.grobal_add);
			convertView.setTag(R.string.room_add_device_tag, "ADD_ITEM");
		}else {
			CustUtils.setDeviceIcon(getItem(position).getTypeCode(), holder.iconView);
			holder.nameView.setText(getItem(position).getDeviceName());
		}
		return convertView;
	}

}
