package com.smartdevice.service;

import java.util.List;
import java.util.Random;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;

import com.smartdevice.mode.Device;
import com.smartdevice.mode.DeviceAdapter;

import android.util.Log;

public class MqttSend {
    public static final String BROKER_URL = "tcp://123.57.206.2:8020";
    /* In a real application, you should get an Unique Client ID of the device and use this, see
    http://android-developers.blogspot.de/2011/03/identifying-app-installations.html */
    public static String clientId = "username_send";


    private MqttConnector connector;
    
    /**
     */
    public void MqttSend() throws MqttException {


    }
    
    /**
     * send a message to an MQTT server
     * @param topicName the name of the topic to publish to
     * @param qos the quality of service to delivery the message at (0,1,2)
     * @param payload the set of bytes to send to the MQTT server
     * @throws MqttException
     */
    public void Send(String topicName, int qos, byte[] payload) throws MqttException {

		Random ran =new Random(System.currentTimeMillis());
		clientId = "username_send" + "_" + ran.nextInt();
		
		 try {
			//MqttToken connectToken = (MqttToken) mqttClient.connect();
			 connector = new MqttConnector(BROKER_URL, clientId, true, false, null, null);
			 Log.i("MqttSend", "Before connecting....");
			 connector.connect();
			 connector.publish(topicName, qos, payload);
			 connector.disconnect();
			//MqttToken subscribeToken = (MqttToken) mqttClient.subscribe(TOPIC, 1);
		} catch (MqttSecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MqttException e) {
			// TODO Auto-generated catch block
			Log.i("MqttSend", "send error....");
			e.printStackTrace();
		}
		
    }
}
