package com.smartdevice.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;


import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;

import com.smartdevice.control.DBHelper;
import com.smartdevice.control.DeviceControl;
import com.smartdevice.control.GatewayControl;
import com.smartdevice.main.AllDevicesFragment;
import com.smartdevice.main.AllDevicesFragment.MyBroadcastReceiver;
import com.smartdevice.mode.Device;
import com.smartdevice.mode.DeviceAdapter;
import com.smartdevice.mode.DeviceAttribute;
import com.smartdevice.mode.Gateway;
import com.smartdevice.mode.GatewayAdapter;
import com.smartdevice.mode.Session;
import com.smartdevice.utils.ParamsUtils;


/**
 * @author Dominik Obermaier
 */
public class MqttService extends Service {

    public static final String BROKER_URL = "tcp://123.57.206.2:8020";
    //public static final String BROKER_URL = "tcp://test.mosquitto.org:1883";

    /* In a real application, you should get an Unique Client ID of the device and use this, see
    http://android-developers.blogspot.de/2011/03/identifying-app-installations.html */
    public static String clientId = "username_receive";

    public static final String TOPIC_DEVICE = "/v1/from_device/";
    public static final String TOPIC_GATEWAY = "/v1/from_gateway/";
    
    List<String>  sublist = new ArrayList<>();
    
    private MqttConnector connector;
    
    
	public static final String deviceIntentFilter = "com.smartdevice.main.device.update";
	
	private IntentFilter filter = 
			  new IntentFilter(deviceIntentFilter);
			 
	private MyBroadcastReceiver receiver = 
			  new MyBroadcastReceiver();
	
	public class MyBroadcastReceiver extends BroadcastReceiver { 
		   @Override
		   public void onReceive(Context context, Intent intent) { 
		     //TODO: React to the Intent received. 
			   Log.i(MqttService.class.getSimpleName(), "MyBroadcastReceiver");
			   
	            new Thread(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub;
						   updateSubInfo();
						   SystemClock.sleep(1000*2);
	
							try {
							 	   if(connector.isconnected())
							 	    {
							 	    	connector.disconnect();
							 	    }
							} catch (MqttSecurityException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (MqttException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								
							}
							
						   reConnectMqtt();
					}
			}).start(); 
			   
	      }	  
		}
    
    
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
    	// TODO Auto-generated method stub
    	//buildMessageDB();
    	
		Random ran =new Random(System.currentTimeMillis());
		clientId = "username_receive" + "_" + ran.nextInt();
		Log.i("MqttService", "clientId = " + clientId);
		
		updateSubInfo();
		SystemClock.sleep(1000*2);
		
    	try {
    		Log.i("MqttService", System.getProperty("user.dir"));
            connector = new MqttConnector(BROKER_URL, clientId, true, false, null, null);
            connector.setCallback(new PushCallback(this));
           

            new Thread(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub;
					   while(true){
						 try {

							Log.i("MqttService", "Before connecting....");
							connector.connect();
							for (int i = 0; i < sublist.size(); i++) {
								connector.subscribe(sublist.get(i), 2);
							}
							Log.i("MqttService", "connect success");
							return;
						} catch (MqttSecurityException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (MqttException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							
						}
						Log.i("MqttService", "after connecting....");
						SystemClock.sleep(1000*5);

					 }
				}
			}).start();
                       
        } catch (MqttException e) {
            Toast.makeText(getApplicationContext(), "Something went wrong!" + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    	
    	registerReceiver(receiver, filter);
    	
    	return START_STICKY;
    }
    
    public int reConnectMqtt() {
    	// TODO Auto-generated method stub
    	
    	try {
 		   while(true){
 	            new Thread(new Runnable() {
 					@Override
 					public void run() {
 						// TODO Auto-generated method stub;
 							 try {
 								Log.i("reConnectMqtt", "Before connecting....");
 								connector.connect();
 								for (int i = 0; i < sublist.size(); i++) {
 									connector.subscribe(sublist.get(i), 2);
 								}
 								Log.i("reConnectMqtt", "connect success");
 							} catch (MqttSecurityException e) {
 								// TODO Auto-generated catch block
 								e.printStackTrace();
 							} catch (MqttException e) {
 								// TODO Auto-generated catch block
 								e.printStackTrace();
 								
 							}
 							 Log.i("reConnectMqtt", "after connecting....");
 					}
 				}).start();
 	            
 	            SystemClock.sleep(1000*10);
 	            
 	    	    if(connector.isconnected())
 	    	    {
 	    	    	Log.i("reConnectMqtt", "has connected");
 	    	    	break;
 	    	    }
 			 }
        } catch (MqttException e) {
            Toast.makeText(getApplicationContext(), "Something went wrong!" + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    	return START_STICKY;
    }
    
    
    public void updateSubInfo() {
    	// TODO Auto-generated method stub
    	
        new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub;
				            sublist.clear();
							Log.i("MqttService", "getGatewaySN");
							GatewayControl control = new GatewayControl();
							List<Gateway> gatewayList = control.getBindedGateways(Session.getUsername());
							
							if(gatewayList != null){
								Log.i("MqttService", "getGateway size=" + gatewayList.size());
								for (int i = 0; i < gatewayList.size(); i++) {
									
								    sublist.add(TOPIC_GATEWAY + gatewayList.get(i).getGatewaySN() + "/#");
								    DeviceControl deviceControl = new DeviceControl();
									List<Device> deviceList = deviceControl.getDevices(gatewayList.get(i).getGatewaySN());
					               
									if(deviceList != null)
									{
										Log.i("MqttService", "deviceList size=" + deviceList.size());
										for (int k = 0; k < deviceList.size(); k++) {
											List<DeviceAttribute> attrlist = deviceList.get(k).getAttrList();
											if(attrlist != null){
												for (int j = 0; j < attrlist.size(); j++) {
												    if(attrlist.get(j).getPermission()!= null && attrlist.get(j).getPermission().contains("I")){
												    	sublist.add(TOPIC_DEVICE + deviceList.get(k).getDeviceSN()+ "/#");
												    	break;
												    }
												}
											}
										}
									}
								}

				 }
			}
		}).start();

    }
    
    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    @Override
    public void onDestroy() {
        try {
            connector.disconnect();
        } catch (MqttException e) {
            Toast.makeText(getApplicationContext(), "Something went wrong!" + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
    
    private void buildMessageDB(){
    	DBHelper dbHelper = new DBHelper(this, ParamsUtils.DB_NAME);
    	dbHelper.setSqlString("create table IF NOT EXISTS sd_message(id integer primary key autoincrement, fromname varchar(20), fromtype varchar(20)," +
    			"title varchar(60), time varchar(20), content varchar(200));");
    }
}
