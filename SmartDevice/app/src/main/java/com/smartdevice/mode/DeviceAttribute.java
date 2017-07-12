package com.smartdevice.mode;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class DeviceAttribute implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/*TRUE 开, FALSE 关*/
	private boolean openAttr;
	/*0-100%*/
	private float openExtentAttr;
    /*浓度 */
	private float concentrationAttr;
	/*表值*/
	private float valueAttr;
	/*亮度*/
	private long brightnessAttr;
	
	private long colorAttr;
	
	/*color temperature*/
	private long colorTemperAttr;
	
	private float temperaturAttr;
	
	private float humidityAttr;
	
	/*int, 关-0 开-1 停-2*/
	private int curtainState;
	
	private boolean isAlarm;
	
	private Map<String, String> attrMap;
	
	private Map<DeviceAttrType, String> attributeMap;
	
	private int id;
	private String deviceSN;
	private DeviceAttrType deviceAttrType;
	private int attrCode;   //attribute type
	private String attrName;
	private String attrControlValue;
	private int isControl;
	private String attrCurrentValue;
	private String gatewaySN;
	private String permission;
	
	public DeviceAttribute(){
		attrMap = new HashMap<>();
		attributeMap = new HashMap<>();
	}
	
	public Map<String, String> getAttrMap(){
		return attrMap;
	}
	
	public void setAttrMap(Map<String, String> attrMap) {
		this.attrMap = attrMap;
	}

	public void setAttrMap(String key, String value){
		attrMap.put(key, value);
	}
	
	public void setAttributeMap(String key, String value){
		DeviceAttrType attrType = new DeviceAttrType();
		attrType.setAttrTypeName(DeviceAttrType.AttrType.getName(Integer.valueOf(key)));
		attrType.setAttrTypeId(key);
		attributeMap.put(attrType, value);
	}
	
	public Map<DeviceAttrType, String> getAttributeMap() {
		return attributeMap;
	}
	
	public String getAttrValue(String attrTypeId){
		DeviceAttrType attrType = new DeviceAttrType();
		attrType.setAttrTypeName(String.valueOf(DeviceAttrType.AttrType.getName(Integer.valueOf(attrTypeId))));
		attrType.setAttrTypeId(attrTypeId);
		return attributeMap.get(attrType);
	}

	public void setAttributeMap(Map<DeviceAttrType, String> attributeMap) {
		this.attributeMap = attributeMap;
	}

	public boolean isOpenAttr() {
		return openAttr;
	}

	public void setOpenAttr(boolean openAttr) {
		this.openAttr = openAttr;
	}

	public float getOpenExtentAttr() {
		return openExtentAttr;
	}

	public void setOpenExtentAttr(float openExtentAttr) {
		this.openExtentAttr = openExtentAttr;
	}

	public float getConcentrationAttr() {
		return concentrationAttr;
	}

	public void setConcentrationAttr(float concentrationAttr) {
		this.concentrationAttr = concentrationAttr;
	}

	public float getValueAttr() {
		return valueAttr;
	}

	public void setValueAttr(float valueAttr) {
		this.valueAttr = valueAttr;
	}

	public long getBrightnessAttr() {
		return brightnessAttr;
	}

	public void setBrightnessAttr(long brightnessAttr) {
		this.brightnessAttr = brightnessAttr;
	}

	public long getColorAttr() {
		return colorAttr;
	}

	public void setColorAttr(long colorAttr) {
		this.colorAttr = colorAttr;
	}

	public long getColorTemperAttr() {
		return colorTemperAttr;
	}

	public void setColorTemperAttr(long colorTemperAttr) {
		this.colorTemperAttr = colorTemperAttr;
	}

	public float getTemperaturAttr() {
		return temperaturAttr;
	}

	public void setTemperaturAttr(float temperaturAttr) {
		this.temperaturAttr = temperaturAttr;
	}

	public float getHumidityAttr() {
		return humidityAttr;
	}

	public void setHumidityAttr(float humidityAttr) {
		this.humidityAttr = humidityAttr;
	}

	public int getCurtainState() {
		return curtainState;
	}

	public void setCurtainState(int curtainState) {
		this.curtainState = curtainState;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDeviceSN() {
		return deviceSN;
	}

	public void setDeviceSN(String deviceSN) {
		this.deviceSN = deviceSN;
	}

	public DeviceAttrType getDeviceAttrType() {
		return deviceAttrType;
	}

	public void setDeviceAttrType(DeviceAttrType deviceAttrType) {
		this.deviceAttrType = deviceAttrType;
	}

	public int getAttrCode() {
		return attrCode;
	}

	public void setAttrCode(int attrCode) {
		this.attrCode = attrCode;
	}

	public String getAttrName() {
		return attrName;
	}

	public void setAttrName(String attrName) {
		this.attrName = attrName;
	}

	public String getAttrConrolValue() {
		return attrControlValue;
	}

	public void setAttrControlValue(String attrControlValue) {
		this.attrControlValue = attrControlValue;
	}

	public int getIsControl() {
		return isControl;
	}

	public void setIsControl(int isControl) {
		this.isControl = isControl;
	}

	public String getAttrCurrentValue() {
		return attrCurrentValue;
	}

	public void setAttrCurrentValue(String attrCurrentValue) {
		this.attrCurrentValue = attrCurrentValue;
	}

	public String getGatewaySN() {
		return gatewaySN;
	}

	public void setGatewaySN(String gatewaySN) {
		this.gatewaySN = gatewaySN;
	}

	public boolean isAlarm() {
		return isAlarm;
	}

	public void setAlarm(boolean isAlarm) {
		this.isAlarm = isAlarm;
	}

	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}
	
}
