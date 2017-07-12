package com.smartdevice.mode;

import java.util.ArrayList;
import java.util.List;

import com.smartdevice.main.R;
import com.smartdevice.utils.DensityUtil;

import android.R.integer;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;

public class DeviceTypeAdapter extends BaseAdapter implements ListAdapter{
	
	List<Object> typeList = new ArrayList<Object>();
    LayoutInflater layoutInflater;
	Context context;
	DensityUtil densityUtil;
	
	
	public DeviceTypeAdapter(Context context){
		layoutInflater = LayoutInflater.from(context);
		densityUtil = new DensityUtil(context);
		
		this.context = context;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return typeList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return typeList.get(position);
	}
	
	public void setItem(Object item) {
		this.typeList.add(item);
	}
	
	public void setArrayList(List<Object> typeList){
		this.typeList = typeList;
	}
	
	public String getItemName(int position) {
		return ((BaseDeviceType)getItem(position)).getBaseTypeName();
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return ((BaseDeviceType)getItem(position)).getBaseTypeId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		//if(convertView == null){
//		    AbsListView.LayoutParams params = new AbsListView.LayoutParams(
//					AbsListView.LayoutParams.MATCH_PARENT,
//					AbsListView.LayoutParams.MATCH_PARENT);;
			convertView = layoutInflater.inflate(R.layout.device_type_layout, null);
			TextView typeView = (TextView) convertView.findViewById(R.id.id_device_type);
			//typeView.setLayoutParams(params);
			typeView.setText(getItemName(position));
			
			
			typeView.setHeight(densityUtil.dip2px(50));
			
		   // return typeView;
		   // return convertView;	
		//}
		
		return convertView;
	}

}
