package com.smartdevice.main;

import com.smartdevice.service.MqttService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class BootBroadcastReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		System.out.println("Boot completed received!");
		final Intent mqttIntent = new Intent(context, MqttService.class);
        context.startService(mqttIntent);
        Toast.makeText(context, "MQTT Service started", Toast.LENGTH_LONG).show();
	}

}
