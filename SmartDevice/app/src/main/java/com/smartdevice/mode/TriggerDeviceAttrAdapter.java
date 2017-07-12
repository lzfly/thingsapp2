package com.smartdevice.mode;

import java.text.AttributedCharacterIterator.Attribute;
import java.util.ArrayList;
import java.util.List;

import com.smartdevice.main.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TriggerDeviceAttrAdapter extends BaseAdapter{

	private List<DeviceAttribute> attrList;
	private Context context;
	
	public TriggerDeviceAttrAdapter(List<DeviceAttribute> attrList, Context context) {
		// TODO Auto-generated constructor stub
		this.attrList = attrList;
		this.context = context;
	}
	
	public void setAttrList(List<DeviceAttribute> attrList){
		this.attrList = attrList;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return attrList.size();
	}

	@Override
	public DeviceAttribute getItem(int position) {
		// TODO Auto-generated method stub
		return attrList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	class ViewHolder{
		TextView nameView;
		TextView descView;
		ImageView addView;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		if(convertView == null){
			holder = new ViewHolder();
			LayoutInflater inflater = LayoutInflater.from(context);
			convertView = inflater.inflate(R.layout.trigger_attr_link_device_layout, null);
			holder.nameView = (TextView) convertView.findViewById(R.id.id_trigger_attr_name);
			holder.addView = (ImageView) convertView.findViewById(R.id.id_trigger_attr_add);
			holder.descView = (TextView) convertView.findViewById(R.id.id_trigger_attr_desc);
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.nameView.setText(getItem(position).getAttrName());
		setTriggerDesc(holder.descView, getItem(position).getAttrCode());
		return convertView;
	}
	
	private void setTriggerDesc(TextView descView, int attrCode){
		switch (attrCode) {
		case DeviceAttrType.ATTR_TYPE_OPEN:
			descView.setText(R.string.link_device_attr_desc_open);
			break;
		case DeviceAttrType.ATTR_TYPE_CURTAIN_STATE:
			descView.setText(R.string.link_device_attr_desc_curtain);
			break;
		case DeviceAttrType.ATTR_TYPE_IS_ALARM:
			descView.setText(R.string.link_device_attr_desc_alarm);
			break;
		case DeviceAttrType.ATTR_TYPE_BRIGHTNESS:
			descView.setText(R.string.link_device_attr_desc_brightness);
			break;
		case DeviceAttrType.ATTR_TYPE_COLOR_TEMPER:
			descView.setText(R.string.link_device_attr_desc_color_temper);
			break;
		case DeviceAttrType.ATTR_TYPE_TEMPERATURE:
			descView.setText(R.string.link_device_attr_desc_temper);
			break;
		case DeviceAttrType.ATTR_TYPE_HUMIDITY:
			descView.setText(R.string.link_device_attr_desc_humidity);
			break;
		default:
			break;
		}
	}

}
