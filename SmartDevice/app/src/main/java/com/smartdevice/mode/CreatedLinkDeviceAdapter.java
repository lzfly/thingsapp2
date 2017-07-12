package com.smartdevice.mode;

import java.util.ArrayList;
import java.util.List;

import com.smartdevice.main.R;
import com.smartdevice.utils.CustUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CreatedLinkDeviceAdapter extends BaseAdapter{
	
	private Context context;
	private List<LogicEntity> entityList;

	public CreatedLinkDeviceAdapter(Context context, List<LogicEntity> entityList) {
		// TODO Auto-generated constructor stub
		this.context = context;
		if(entityList != null){
			this.entityList = entityList;
		}else {
			this.entityList = new ArrayList<>();
		}
		
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return entityList.size();
	}

	@Override
	public LogicEntity getItem(int position) {
		// TODO Auto-generated method stub
		return entityList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	class ViewHolder{
		ImageView ifImageView;
		ImageView thenImageView;
		ImageView switchImageView;
		TextView descTextView;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		if(convertView == null){
			holder = new ViewHolder();
			LayoutInflater inflater = LayoutInflater.from(context);
			convertView = inflater.inflate(R.layout.created_link_device_item_layout, null);
			holder.ifImageView = (ImageView) convertView.findViewById(R.id.id_created_link_device_if);
			holder.thenImageView = (ImageView) convertView.findViewById(R.id.id_created_link_device_then);
			holder.descTextView = (TextView) convertView.findViewById(R.id.id_created_link_device_desc);
			holder.switchImageView = (ImageView) convertView.findViewById(R.id.id_created_link_device_turn_on);
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		CustUtils.setDeviceIcon(getItem(position).getIfTypeCode(), holder.ifImageView);
		CustUtils.setDeviceIcon(getItem(position).getThTypeCode(), holder.thenImageView);
		LogicEntity entity = getItem(position);
		String descEvent = "如果" + entity.getIfDeviceName() + 
				CustUtils.getAttrName(entity.getIfAttrCode()) + 
				CustUtils.getOperateName(entity.getIfAttrCode(), entity.getIfOperateCode()) + 
				CustUtils.getControlName(entity.getIfAttrCode(), entity.getIfAttrValue());
		String descAction = "然后" + entity.getThDeviceName() + 
				CustUtils.getAttrName(entity.getThAttrCode()) + 
				"设为" + 
				CustUtils.getControlName(entity.getThAttrCode(), entity.getThAttrValue());
		holder.descTextView.setText(descEvent + ", " + descAction);
		return convertView;
	}
}
