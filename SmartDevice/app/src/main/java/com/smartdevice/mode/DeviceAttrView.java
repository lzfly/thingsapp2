package com.smartdevice.mode;

import java.util.List;
import java.util.Map;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;
import org.json.JSONException;
import org.json.JSONObject;

import com.smartdevice.control.DeviceControl;
import com.smartdevice.main.R;
import com.smartdevice.service.MqttSend;
import com.smartdevice.ui.CustCurtainControlView;
import com.smartdevice.ui.CustLinearView;
import com.smartdevice.ui.CustSeekBar;
import com.smartdevice.ui.LinearView;
import com.smartdevice.utils.DensityUtil;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class DeviceAttrView {

	private DeviceAttribute attribute;
	private LinearLayout linearLayout;
	private Context context;
	LayoutParams params;
	private DensityUtil densityUtil;
	private Device device;
	
	private static int index = 1;
	private String value;
	private String newValue;
	private String deviceSN;
	private int attrCode;
	private LinearView choosedView;
	private String type;
	private Drawable openDrawable;
	private Drawable openSelectedDrawable;
	private Drawable stopDrawable;
	private Drawable stopSelectedDrawable;
	private Drawable closeDrawable;
	private Drawable closeSelectedDrawable;
	
	public DeviceAttrView(Context context, Device device){
		this.device = device;
		this.context = context;
		linearLayout = new LinearLayout(context);
		densityUtil = new DensityUtil(context);
		params = new LayoutParams(LayoutParams.MATCH_PARENT, 
				densityUtil.dip2px(40));
		linearLayout.setOrientation(LinearLayout.VERTICAL);
		openDrawable = context.getResources().getDrawable(R.drawable.icon_curtain_open);
		openSelectedDrawable = context.getResources().getDrawable(R.drawable.icon_curtain_open_selected);
		stopDrawable = context.getResources().getDrawable(R.drawable.icon_curtain_stop);
		stopSelectedDrawable = context.getResources().getDrawable(R.drawable.icon_curtain_stop_selected);
		closeDrawable = context.getResources().getDrawable(R.drawable.icon_curtain_close);
		closeSelectedDrawable = context.getResources().getDrawable(R.drawable.icon_curtain_close_selected);
		openDrawable.setBounds(0, 0, openDrawable.getMinimumWidth(), openDrawable.getMinimumHeight());
		openSelectedDrawable.setBounds(0, 0, openSelectedDrawable.getMinimumWidth(), openSelectedDrawable.getMinimumHeight());
		stopDrawable.setBounds(0, 0, stopDrawable.getMinimumWidth(), stopDrawable.getMinimumHeight());
		stopSelectedDrawable.setBounds(0, 0, stopSelectedDrawable.getMinimumWidth(),stopSelectedDrawable.getMinimumHeight());
		closeDrawable.setBounds(0, 0,closeDrawable.getMinimumWidth(),closeDrawable.getMinimumHeight());
		closeSelectedDrawable.setBounds(0, 0,closeSelectedDrawable.getMinimumWidth(),closeSelectedDrawable.getMinimumHeight());
		
	}
	
	public ViewGroup getAttrViews(){
		//Map<DeviceAttrType, String> attrMap = attribute.getAttributeMap();
		List<DeviceAttribute> attrList = device.getAttrList();
		deviceSN = device.getDeviceSN();
		if(attrList == null || attrList.size() < 1){
			return linearLayout;
		}
		for (int i = 0; i < attrList.size(); i++) {
			
		//for(DeviceAttrType key:attrMap.keySet()){
			//TextView valueView = new TextView(context);
			final LinearView valueView = new LinearView(context);
			SeekBar mSeekBar = null;
			CustSeekBar seekBarView = null;
			CustCurtainControlView curtainControlView = null;
			//valueView.setLayoutParams(params);
			//valueView.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
			//valueView.setText(key.getAttrTypeName() + ": " + attrMap.get(key));
			//Log.i("DeviceAttrView", key.getAttrTypeName());
			valueView.setTitle(attrList.get(i).getAttrName());
			valueView.setContent(attrList.get(i).getAttrCurrentValue());
			valueView.setTitlePaddingLeft(densityUtil.dip2px(5));
			valueView.getIconView().setVisibility(View.GONE);
			valueView.getIndicatorView().setTag(R.string.device_info_view_tag, valueView);
			valueView.getIndicatorView().setTag(R.string.device_attr_type_tag,  attrList.get(i));
			final int attributeCode = attrList.get(i).getAttrCode();
			String currentValue = attrList.get(i).getAttrCurrentValue();
			if(currentValue.length() == 0)
				currentValue = "0";
			
			switch (attributeCode) {
			case DeviceAttrType.ATTR_TYPE_OPEN:
				if(currentValue.equals("1")){
					valueView.getIndicatorView().setImageResource(R.drawable.icon_switch_on);
					valueView.getIndicatorView().setSelected(true);
				}else {
					valueView.getIndicatorView().setImageResource(R.drawable.icon_switch_off);
					valueView.getIndicatorView().setSelected(false);
				}
				valueView.getIndicatorView().setTag(R.string.indicator_tag, new String("1001"));
				valueView.getIndicatorView().setOnClickListener(new AttrOnClickListener());
				break;
			case DeviceAttrType.ATTR_TYPE_CURTAIN_STATE:
				valueView.getIndicatorView().setVisibility(View.INVISIBLE);
				curtainControlView = new CustCurtainControlView(context);
				if(currentValue.equals("0")){
					curtainControlView.getStopView().setCompoundDrawables(null, closeSelectedDrawable, null, null);
				}else if(currentValue.equals("1")){
					curtainControlView.getOpenView().setCompoundDrawables(null, openSelectedDrawable, null, null);
				}else if(currentValue.equals("2")){
					curtainControlView.getCloseView().setCompoundDrawables(null, stopSelectedDrawable, null, null);
				}
				final CustCurtainControlView controlView = curtainControlView;
				curtainControlView.getOpenView().setSelected(false);
				curtainControlView.getOpenView().setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
//						if(v.isSelected()){
//							
//							((TextView)v).setCompoundDrawables(null, openDrawable, null, null);
//							v.setSelected(false);
//						}else {
							valueView.setContent("1");
							((TextView)v).setCompoundDrawables(null, openSelectedDrawable, null, null);
							newValue = "1";
							attrCode = attributeCode;
							new Thread(runnable).start();
							controlView.getStopView().setCompoundDrawables(null, stopDrawable, null, null);
							controlView.getCloseView().setCompoundDrawables(null, closeDrawable, null, null);
							v.setSelected(true);
						//}
					}
				});
				curtainControlView.getStopView().setOnClickListener(
						new OnClickListener() {

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								valueView.setContent("2");
								if (v.isSelected()) {
									((TextView) v).setCompoundDrawables(null,
											stopDrawable, null, null);
									v.setSelected(false);
								} else {
									((TextView) v).setCompoundDrawables(null,
											stopSelectedDrawable, null, null);
									newValue = "2";
									attrCode = attributeCode;
									new Thread(runnable).start();
									controlView.getOpenView().setCompoundDrawables(null, openDrawable, null, null);
									controlView.getCloseView().setCompoundDrawables(null, closeDrawable, null, null);
									v.setSelected(true);
								}
							}
						});
				curtainControlView.getCloseView().setOnClickListener(
						new OnClickListener() {

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								valueView.setContent("0");
								((TextView) v).setCompoundDrawables(null,
										closeSelectedDrawable, null, null);
								newValue = "0";
								attrCode = attributeCode;
								new Thread(runnable).start();
								controlView.getStopView().setCompoundDrawables(
										null, stopDrawable, null, null);
								controlView.getOpenView().setCompoundDrawables(
										null, openDrawable, null, null);
								v.setSelected(true);
							}
						});
				break;
				
			case DeviceAttrType.ATTR_TYPE_BRIGHTNESS:
			case DeviceAttrType.ATTR_TYPE_COLOR_TEMPER:
			case DeviceAttrType.ATTR_TYPE_TEMPERATURE:
				valueView.getIndicatorView().setImageResource(R.drawable.icon_down);
				valueView.getIndicatorView().setTag(R.string.indicator_tag, new String("1005"));
				valueView.getIndicatorView().setOnClickListener(new AttrOnClickListener());
				float progress = Float.valueOf(currentValue);
				seekBarView = new CustSeekBar(context);
				seekBarView.getIconView().setVisibility(View.GONE);
				//seekBarView.setMax(100*100);
				//seekBarView.setProgress(progress*100);
				seekBarView.setMax(100);
				seekBarView.setProgress(progress);
				
//				mSeekBar = new SeekBar(context);
//				mSeekBar.setMax(100);
//				mSeekBar.setProgress((int)progress);
				//mSeekBar.setVisibility(View.GONE);
					
				seekBarView.setCurrentValue(progress);
				final CustSeekBar custSeekBar = seekBarView;
				seekBarView.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
					
					@Override
					public void onStopTrackingTouch(SeekBar seekBar) {
						// TODO Auto-generated method stub
						//newValue = String.valueOf((float)(seekBar.getProgress()/100));
						newValue = String.valueOf(seekBar.getProgress());
						attrCode = attributeCode;
						new Thread(runnable).start();
					}
					
					@Override
					public void onStartTrackingTouch(SeekBar seekBar) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onProgressChanged(SeekBar seekBar, int progress,
							boolean fromUser) {
						// TODO Auto-generated method stub
						newValue = String.valueOf(progress);
						//newValue = String.valueOf((float)progress/100);
						//Log.i("DeviceAttrView", "progress " + progress/100 + "  newValue " + newValue);
						custSeekBar.setCurrentValue((float)progress);
						valueView.setContent(newValue);
					}
				});
				
				valueView.getIndicatorView().setTag(R.string.device_info_seekbar_tag, seekBarView);
				break;

			default:
				valueView.getIndicatorView().setVisibility(View.GONE);
				break;
			}
			linearLayout.addView(valueView.createLinearView());
			if(seekBarView != null)
			    linearLayout.addView(seekBarView.createSeekBar());
			if(curtainControlView != null)
				linearLayout.addView(curtainControlView);
		}
		return linearLayout;
	}
	
	class AttrOnClickListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			choosedView = (LinearView) v.getTag(R.string.device_info_view_tag);
			attribute = (DeviceAttribute) v.getTag(R.string.device_attr_type_tag);
			type = (String) v.getTag(R.string.indicator_tag);
			value = attribute.getAttrCurrentValue();
			attrCode = attribute.getAttrCode();
			if(type.equals("1001")){
				if(v.isSelected()){
					v.setSelected(false);
					newValue = "0";
					new Thread(runnable).start();
				}else {
					v.setSelected(true);
					newValue = "1";
					new Thread(runnable).start();
				}
			}else if(type.equals("1005")){
				CustSeekBar mSeekBar = (CustSeekBar) v.getTag(R.string.device_info_seekbar_tag);
				if(!v.isSelected()){
					mSeekBar.setVisibility(View.GONE);
					v.setSelected(true);
					choosedView.getIndicatorView().setImageResource(R.drawable.icon_indicator);
				}else {
					mSeekBar.setVisibility(View.VISIBLE);
					v.setSelected(false);
					choosedView.getIndicatorView().setImageResource(R.drawable.icon_down);
				}
				
				
			}
			
		}
	}
	
	private Runnable runnable = new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			DeviceControl control = new DeviceControl();
			String result = control.pushDeviceAttribute(deviceSN, attrCode, newValue);		
			
			Message msg = new Message();
			msg.obj = result;
			mqttHandler.sendMessage(msg);
		}
	};
	
	private Handler mqttHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			String result = (String) msg.obj;
			if(result != null && result.equals("0")){
				updateDeviceInfo(attrCode);
				if(type != null){
					if(type.equals("1001")){
						//choosedView.setTitle(attribute.getAttrName() + "    " + newValue);
						choosedView.setContent(newValue);
						
						if(newValue == "0"){
							choosedView.getIndicatorView().setImageResource(R.drawable.icon_switch_off);
						}else {
							choosedView.getIndicatorView().setImageResource(R.drawable.icon_switch_on);
						}
						type = null;
					}else {
						
					}
				}
			}
		};
	};
	
	private Handler handler = new Handler(){
		
		@Override
		public void handleMessage(Message msg) {
			String result = (String) msg.obj;
			if(result != null){
				try {
					JSONObject object = new JSONObject(result);
					int status = object.getInt("status");
					if(status == 0){
						updateDeviceInfo(attrCode);
						if(type != null){
							if(type.equals("1001")){
								//choosedView.setTitle(attribute.getAttrName() + "    " + newValue);
								choosedView.setContent(newValue);
								
								if(newValue == "0"){
									choosedView.getIndicatorView().setImageResource(R.drawable.icon_switch_off);
								}else {
									choosedView.getIndicatorView().setImageResource(R.drawable.icon_switch_on);
								}
								type = null;
							}else {
								
							}
						}
						
					}else {
						
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
	};
	
	public void updateDeviceInfo(int attrCode){
		for (int i = 0; i < device.getAttrList().size(); i++) {
			if(attrCode == device.getAttrList().get(i).getAttrCode()){
				device.getAttrList().get(i).setAttrCurrentValue(newValue);
			}
		}
	}
	
	public Device getDeviceInfo(){
		return device;
	}
	
}
