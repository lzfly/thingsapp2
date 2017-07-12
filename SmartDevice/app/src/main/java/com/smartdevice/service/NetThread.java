package com.smartdevice.service;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;

public class NetThread extends Thread{

	private MqttClient mqttClient;
	
	public NetThread(MqttClient mqttClient){
		this.mqttClient = mqttClient;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		//super.run();
		try {
			mqttClient.connect();
		} catch (MqttException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
