package com.smartdevice.service;

import java.io.IOException;
import java.sql.Timestamp;

import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MqttDefaultFilePersistence;

import android.content.Context;

public class MqttConnector {
	
	// Private instance variables
		//private MqttClient 			client;
	    private MqttClient 			client;
		private String 				brokerUrl;
		private boolean 			quietMode;
		private MqttConnectOptions 	conOpt;
		private boolean 			clean;
		private String password;
		private String userName;

		/**
		 * Constructs an instance of the sample client wrapper
		 * @param brokerUrl the url of the server to connect to
		 * @param clientId the client id to connect with
		 * @param cleanSession clear state at end of connection or not (durable or non-durable subscriptions)
		 * @param quietMode whether debug should be printed to standard out
	   * @param userName the username to connect with
	   * @param password the password for the user
		 * @throws MqttException
		 */
	    public MqttConnector(String brokerUrl, 
	    		String clientId, 
	    		boolean cleanSession, 
	    		boolean quietMode, 
	    		String userName, 
	    		String password) throws MqttException {
	    	this.brokerUrl = brokerUrl;
	    	this.quietMode = quietMode;
	    	this.clean 	   = cleanSession;
	    	this.password = password;
	    	this.userName = userName;
	    	//This sample stores in a temporary directory... where messages temporarily
	    	// stored until the message has been delivered to the server.
	    	//..a real application ought to store them somewhere
	    	// where they are not likely to get deleted or tampered with
	    	String tmpDir = System.getProperty("java.io.tmpdir");
	    	log(tmpDir);
	    	MqttDefaultFilePersistence dataStore = new MqttDefaultFilePersistence(tmpDir);

	    	try {
	    		// Construct the connection options object that contains connection parameters
	    		// such as cleanSession and LWT
		    	conOpt = new MqttConnectOptions();
		    	conOpt.setCleanSession(clean);
		    	if(password != null ) {
		    	  conOpt.setPassword(this.password.toCharArray());
		    	}
		    	if(userName != null) {
		    	  conOpt.setUserName(this.userName);
		    	}

	    		// Construct an MQTT blocking mode client
				client = new MqttClient(this.brokerUrl,clientId, dataStore);

			} catch (MqttException e) {
				e.printStackTrace();
				log("Unable to set up client: "+e.toString());
				System.exit(1);
			}
	    }
	    
	    public void setCallback(MqttCallback mqttCallback){
			// Set this wrapper as the callback handler
	    	client.setCallback(mqttCallback);
	    }

	    /**
	     * connect to an MQTT server
         * @throws MqttException
	     */
	    public void connect() throws MqttException {
	    	// Connect to the MQTT server
	    	log("Connecting to "+brokerUrl + " with client ID "+client.getClientId());
	    	client.connect(conOpt);
	    	log("Connected");
	    }
	    
	    /**
	     * disconnect from an MQTT server
         * @throws MqttException
	     */
	    public void disconnect() throws MqttException {
	    	// Disconnect the client
	    	client.disconnect();
	    	log("Disconnected");

	    }
	    
	    /**
	     * isconnected from an MQTT server
         * @throws MqttException
	     */
	    public boolean isconnected() throws MqttException {
	    	// isconnect 
	    	return client.isConnected();

	    }
	    
	    /**
	     * Publish / send a message to an MQTT server
	     * @param topicName the name of the topic to publish to
	     * @param qos the quality of service to delivery the message at (0,1,2)
	     * @param payload the set of bytes to send to the MQTT server
	     * @throws MqttException
	     */
	    public void publish(String topicName, int qos, byte[] payload) throws MqttException {

	    	String time = new Timestamp(System.currentTimeMillis()).toString();
	    	log("Publishing at: "+time+ " to topic \""+topicName+"\" qos "+qos);

	    	// Create and configure a message
	   		MqttMessage message = new MqttMessage(payload);
	    	message.setQos(qos);

	    	// Send the message to the server, control is not returned until
	    	// it has been delivered to the server meeting the specified
	    	// quality of service.
	    	client.publish(topicName, message);

	    }

	    /**
	     * Subscribe to a topic on an MQTT server
	     * Once subscribed this method waits for the messages to arrive from the server
	     * that match the subscription. It continues listening for messages until the enter key is
	     * pressed.
	     * @param topicName to subscribe to (can be wild carded)
	     * @param qos the maximum quality of service to receive messages at for this subscription
	     * @throws MqttException
	     */
	    public void subscribe(String topicName, int qos) throws MqttException {

	    	// Subscribe to the requested topic
	    	// The QoS specified is the maximum level that messages will be sent to the client at.
	    	// For instance if QoS 1 is specified, any messages originally published at QoS 2 will
	    	// be downgraded to 1 when delivering to the client but messages published at 1 and 0
	    	// will be received at the same level they were published at.
	    	log("Subscribing to topic \""+topicName+"\" qos "+qos);
	    	client.subscribe(topicName, qos);

	    }
	    
	   
	    
	    /**
	     * Utility method to handle logging. If 'quietMode' is set, this method does nothing
	     * @param message the message to log
	     */
	    private void log(String message) {
	    	if (!quietMode) {
	    		System.out.println(message);
	    	}
	    }

}
