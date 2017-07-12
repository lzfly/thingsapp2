package com.smartdevice.mode;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import com.smartdevice.control.GatewayControl;
import com.smartdevice.main.AddGatewayActivity;
import com.smartdevice.main.R;
import com.smartdevice.utils.Params;

import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GatewayAdapter extends BaseAdapter{

	private Context context;
	private List<Gateway> gatewayList;
	private Gateway gateway;
	
	public GatewayAdapter(Context context){
		this.context = context;
		gatewayList = new ArrayList<>();
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return gatewayList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return gatewayList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public void setGatewayList(List<Gateway> gatewayList){
		this.gatewayList = gatewayList;
	}

	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.binded_gateways_layout, null);
		TextView gatewayIdView = (TextView) view.findViewById(R.id.id_binded_gateway_id);
		ImageView deleleView = (ImageView) view.findViewById(R.id.id_delete_binded_gateway);
		final int location = position;

		deleleView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				gateway = gatewayList.get(location);
				gatewayList.remove(location);
				notifyDataSetChanged();
				
				new Thread(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						String result = new String();
						GatewayControl control = new GatewayControl(Params.base_uri + Params.bind_gateway);
						try {
							
							Log.i(GatewayAdapter.class.getSimpleName(), "gatewayId " + gateway.getGatewaySN());
							Log.i(GatewayAdapter.class.getSimpleName(), "username " + Session.getUsername());
							result = control.unbind(gateway.getGatewaySN(), Session.getUsername());
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						Log.i(GatewayAdapter.class.getSimpleName(), "sendBroadcast");
						Intent intent = new Intent("com.smartdevice.main.device.update"); 
						
						//LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(context);
						context.sendBroadcast(intent);
					}
				}).start();
				
			}
		});
		
		gatewayIdView.setText(((Gateway)getItem(position)).getGatewaySN());
		return view;
	}

}
