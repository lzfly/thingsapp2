package com.smartdevice.mode;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import com.smartdevice.control.GroupControl;
import com.smartdevice.main.R;
import com.smartdevice.mode.SceneDeviceAdapter.ViewHolder;
import com.smartdevice.ui.SwipeItemLayout;
import com.smartdevice.utils.CustUtils;
import com.smartdevice.utils.LogUtil;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

public class SceneDeviceChoosedAdapter extends BaseAdapter{
	
	private List<Device> deviceChoosedList;
	private LayoutInflater inflater;
	private int delPostion;
	private String sceneName;
	private PopupWindow delpPopupWindow;
	private Context context;
	
	public SceneDeviceChoosedAdapter(List<Device> choosedDeviceList, Context context){
		this.deviceChoosedList = choosedDeviceList;
		this.context = context;
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return deviceChoosedList.size();
	}

	@Override
	public Device getItem(int position) {
		// TODO Auto-generated method stub
		return deviceChoosedList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public String getSceneName() {
		return sceneName;
	}

	public void setSceneName(String sceneName) {
		this.sceneName = sceneName;
	}

	public void setDeviceChoosedList(List<Device> deviceList){
		if(deviceChoosedList == null){
			deviceChoosedList = new ArrayList<>();
		}
		deviceChoosedList = deviceList;
	}
	
	public void updateDeviceList(Device device){
		if(deviceChoosedList != null && deviceChoosedList.size() > 0){
		  for (int i = 0; i < deviceChoosedList.size(); i++) {
			if (deviceChoosedList.get(i).getDeviceSN().equals(device.getDeviceSN())) {
				deviceChoosedList.set(i, device);
			}
		  }
		  notifyDataSetChanged();
		}
	}
	
	class ViewHolder {
		ImageView iconView;
		TextView nameView;
		ImageView indicatorView;
		TextView delView;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		if(convertView == null){
			holder = new ViewHolder();
			//convertView = inflater.inflate(R.layout.add_scene_device_choosed_layout, null);
			View contentView = inflater.inflate(R.layout.add_scene_device_choosed_layout, null);
			View menuView = inflater.inflate(R.layout.swipe_list_menu_layout, null);
			convertView = new SwipeItemLayout(contentView, menuView, null, null);
			holder.iconView = (ImageView) contentView.findViewById(R.id.id_device_choosed_icon);
			holder.nameView = (TextView) contentView.findViewById(R.id.id_device_choosed_name);
			holder.indicatorView = (ImageView) contentView.findViewById(R.id.id_device_choosed_indicator);
			holder.delView = (TextView) menuView.findViewById(R.id.id_swipe_delete);
			convertView.setTag(R.string.scene_add_device_tag, holder);
		}else {
			holder = (ViewHolder) convertView.getTag(R.string.scene_add_device_tag);
		}
		ClickListener listener = new ClickListener();
		listener.setPosition(position);
		holder.delView.setOnClickListener(listener);
		holder.nameView.setText(deviceChoosedList.get(position).getDeviceName());
		holder.nameView.setTextColor(Color.GRAY);
		if(getItem(position).getDeviceType() != null){
			setDeviceIcon(getItem(position).getDeviceType().getTypeCode(), holder.iconView);	
		}
		convertView.setTag(getItem(position));
		return convertView;
	}
	
	class ClickListener  implements OnClickListener{
         int position;
         
         public void setPosition(int pos){
        	 position = pos;
         }
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.id_swipe_delete:
				// TODO Auto-generated method stub
				delPostion = position;
				LogUtil.i("SceneDeviceChoosedAdapter", "Delete group postion " + delPostion);
				final View delView = inflater.inflate(R.layout.delete_pop_window_layout, null);
				TextView delTextView = (TextView) delView.findViewById(R.id.id_delete);
				TextView cancelView = (TextView) delView.findViewById(R.id.id_cancel);
				View delLayout = delView.findViewById(R.id.id_delete_layout);
				
				delpPopupWindow = new PopupWindow(delView,
						WindowManager.LayoutParams.MATCH_PARENT,
				        WindowManager.LayoutParams.MATCH_PARENT);
				delpPopupWindow.showAtLocation(v, Gravity.NO_GRAVITY, 0, 0);
				ColorDrawable drawable = new ColorDrawable(0xb0000000);
				delpPopupWindow.setFocusable(true);
				delpPopupWindow.setOutsideTouchable(true);
				delpPopupWindow.setBackgroundDrawable(drawable);
				delView.setOnTouchListener(new OnTouchListener() {
					
					@Override
					public boolean onTouch(View v, MotionEvent event) {
						// TODO Auto-generated method stub
						if(delpPopupWindow != null && delpPopupWindow.isShowing()){
							   delpPopupWindow.dismiss();
						}
						return false;
					}
				});
				
                cancelView.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						delpPopupWindow.dismiss();
					}
				});
                
                delTextView.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if (deviceChoosedList != null && deviceChoosedList.size() > 0) {
							delpPopupWindow.dismiss();
							new Thread(new Runnable() {
								
								@Override
								public void run() {
									// TODO Auto-generated method stub
									//delete group here
									GroupControl groupControl = new GroupControl();
									String result = null;
									android.os.Message msg = new android.os.Message();
									try {
										List<DeviceAttribute> attrList = deviceChoosedList.get(delPostion).getAttrList();
										for (int i = 0; i < attrList.size(); i++) {
											result = groupControl.DeleteGroupBindDevice(Session.getUsername(),
													getSceneName(), 
													deviceChoosedList.get(delPostion).getDeviceSN(), 
													attrList.get(i).getAttrCode());
										}
										
									} catch (JSONException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
										result = null;
									}
									msg.obj = result;
									delHandler.sendMessage(msg);
								}
							}).start();
						}
					}
				}
                );
			
				break;

			default:
				break;
			}
		}
		
	}
	
	private Handler delHandler = new Handler(){
		@Override
		public void handleMessage(android.os.Message msg) {
			String result = (String) msg.obj;
			deviceChoosedList.remove(delPostion);
			notifyDataSetChanged();
			CustUtils.showToast(context, R.string.grobal_delete_successful);
//			try {
//				JSONObject object = new JSONObject(result);
//				int status = object.getInt("status");
//				if(status == ParamsUtils.SUCCESS_CODE){
//					groupList.remove(delPostion);
//					notifyDataSetChanged();
//					CustUtils.showToast(context, R.string.grobal_delete_successful);
//				}else {
//					CustUtils.showToast(context, R.string.grobal_delete_fail);
//				}
//			} catch (JSONException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//				LogUtil.e(TAG, e.toString());
//				CustUtils.showToast(context, R.string.grobal_delete_fail);
//			}
		};
	};
	
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
