package com.smartdevice.mode;

import java.util.ArrayList;
import java.util.List;

import com.smartdevice.main.R;
import com.smartdevice.mode.SceneDeviceAdapter.ViewHolder;
import com.smartdevice.utils.CustUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TriggerDeviceAdapter extends BaseAdapter{

	private Context context;
	private List<Device> triggerList;
	
	public TriggerDeviceAdapter(Context context, List<Device> triggerList){
		this.context = context;
		this.triggerList = triggerList;
	}
	
	public TriggerDeviceAdapter(Context context){
		this(context, new ArrayList<Device>());
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return triggerList.size();
	}

	@Override
	public Device getItem(int position) {
		// TODO Auto-generated method stub
		return triggerList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	class ViewHolder{
		ImageView iconView;
		TextView namView;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		if(convertView == null){
			holder = new ViewHolder();
			LayoutInflater inflater = LayoutInflater.from(context);
			convertView = inflater.inflate(R.layout.link_device_choose_list_layout, null);
			holder.iconView = (ImageView) convertView.findViewById(R.id.id_link_device_trigger_icon);
			holder.namView = (TextView) convertView.findViewById(R.id.id_link_device_trigger_name);
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		int typeId = getItem(position).getDeviceType().getTypeCode();
		CustUtils.setDeviceIcon(typeId, holder.iconView);
		holder.namView.setText(getItem(position).getDeviceName());
		return convertView;
	}

}
