package com.smartdevice.mode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.smartdevice.main.R;
import com.smartdevice.utils.LogUtil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class SceneDeviceAdapter extends BaseAdapter{
	
	private LayoutInflater inflater;
	private Context context;
	private List<Device> deviceChooseList;
	private HashMap<Integer, Boolean> choosedMap;

	public SceneDeviceAdapter(List<Device> deviceList, Context context){
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.context = context;
		deviceChooseList = deviceList;
		choosedMap = new HashMap<>();
		for (int i = 0; i < deviceChooseList.size(); i++) {
			choosedMap.put(i, false);
		}
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return deviceChooseList.size();
	}

	@Override
	public Device getItem(int position) {
		// TODO Auto-generated method stub
		return deviceChooseList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	class ViewHolder {
		ImageView iconView;
		TextView nameView;
		CheckBox chooseView;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		if(convertView == null){
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.add_scene_device_choose_layout, null);
			holder.iconView = (ImageView) convertView.findViewById(R.id.id_device_choose_icon);
			holder.nameView = (TextView) convertView.findViewById(R.id.id_device_choose_name);
			holder.chooseView = (CheckBox) convertView.findViewById(R.id.id_device_choose_check);
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.nameView.setText(deviceChooseList.get(position).getDeviceName());
		setDeviceIcon(getItem(position).getDeviceType().getTypeCode(), holder.iconView);
	    final int location = position;
		holder.chooseView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (choosedMap.get(location)) {
				    LogUtil.i("SceneDeviceAdapter", "Postion " + location);
					choosedMap.put(location, false);
				}else {
					choosedMap.put(location, true);
				}
			}
		});
		holder.chooseView.setChecked(choosedMap.get(position));
		return convertView;
	}

	public List<Device> getDeviceChooseList() {
		return deviceChooseList;
	}

	public HashMap<Integer, Boolean> getChoosedMap() {
		return choosedMap;
	}

	private void setDeviceIcon(int typeId, ImageView devIcon){
		switch (typeId) {
		case DeviceType.DEVICE_TYPE_ADJUST_BRG_LIGHT:
		case DeviceType.DEVICE_TYPE_COLOR_TEMPERATURE_LIGHT:
			devIcon.setImageResource(R.drawable.icon_light);
			break;
		case DeviceType.DEVICE_TYPE_SWITCH:
			devIcon.setImageResource(R.drawable.icon_switcher);
			break;
		case DeviceType.DEVICE_TYPE_CURTAIN:
			devIcon.setImageResource(R.drawable.icon_curtain);
			break;
		case DeviceType.DEVICE_TYPE_ELETRIC_METER:
			devIcon.setImageResource(R.drawable.icon_eletric_meter);
			break;
		case DeviceType.DEVICE_TYPE_WATER_METER:
			devIcon.setImageResource(R.drawable.icon_water_meter);
			break;
		case DeviceType.DEVICE_TYPE_HEAT_METER:
			devIcon.setImageResource(R.drawable.icon_heat);
			break;
		case DeviceType.DEVICE_TYPE_SWITCH_SOCKET:
			devIcon.setImageResource(R.drawable.icon_socket);
			break;
		case DeviceType.DEVICE_TYPE_GAS_METER:
			devIcon.setImageResource(R.drawable.icon_gas_meter);
			break;
		case DeviceType.DEVICE_TYPE_BODY_INFARE:
			devIcon.setImageResource(R.drawable.icon_body_infare);
			break;
		case DeviceType.DEVICE_TYPE_TEMPERATURE_HUMIDITY_SENSOR:
		    devIcon.setImageResource(R.drawable.icon_thermometer);
		    break;
		case DeviceType.DEVICE_TYPE_MAGNET:
			devIcon.setImageResource(R.drawable.icon_magnatic);
			break;
		case DeviceType.DEVICE_TYPE_CAMERA:
			devIcon.setImageResource(R.drawable.icon_camera);
			break;
		default:
			devIcon.setImageResource(R.drawable.icon_unknow_device);
			break;
		}
	}
}
