package com.smartdevice.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.smartdevice.main.R;
import com.smartdevice.mode.DeviceAttrType;
import com.smartdevice.mode.DeviceType;

import android.content.Context;
import android.widget.ImageView;
import android.widget.Toast;

public class CustUtils {
	
	private static Toast mToast;

	public static void showToast(Context context, int toast){
		if(mToast == null){
			mToast = Toast.makeText(context, toast, Toast.LENGTH_LONG);
		}else {
			mToast.setText(toast);
		}
		mToast.show();
	}
	
	public static void setDeviceIcon(int typeId, ImageView devIcon){
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
	
	public static String getAttrName(int attrCode){
		return DeviceAttrType.AttrType.getName(attrCode);
	}
	
	public static String getControlName(int attrCode, String control){
		if(attrCode == DeviceAttrType.ATTR_TYPE_OPEN
				|| attrCode == DeviceAttrType.ATTR_TYPE_CURTAIN_STATE
				|| attrCode == DeviceAttrType.ATTR_TYPE_IS_ALARM ){
			if(control.equals("1")){
				return new String("打开");
			}else{
				return new String("关闭");
			}
		}
		return control;
	}
	
	public static String getOperateName(int attrCode, String operatorCode){
		if(attrCode == DeviceAttrType.ATTR_TYPE_OPEN
				|| attrCode == DeviceAttrType.ATTR_TYPE_CURTAIN_STATE
				|| attrCode == DeviceAttrType.ATTR_TYPE_IS_ALARM ){
			if(operatorCode.equals("=")){
				operatorCode = "";
			}
		}else {
			if(operatorCode.equals("=")){
				operatorCode = "等于";
			}else if (operatorCode.equals(">")) {
				operatorCode = "大于";
			}else if (operatorCode.equals("<")) {
				operatorCode = "小于";
			}
		}
		
		return operatorCode;
	}
	
	//校验特殊字符
	public static boolean checkLegalString(String str){
		String pattern = "[A-Za-z0-9_\\-\\u4e00-\\u9fa5]+";

		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(str);
		return m.matches();
	}
}
