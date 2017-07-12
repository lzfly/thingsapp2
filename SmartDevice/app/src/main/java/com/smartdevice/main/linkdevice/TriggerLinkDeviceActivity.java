package com.smartdevice.main.linkdevice;

import java.util.ArrayList;
import java.util.List;

import com.smartdevice.main.R;
import com.smartdevice.mode.Device;
import com.smartdevice.mode.DeviceAdapter;
import com.smartdevice.mode.DeviceAttribute;
import com.smartdevice.mode.DeviceType;
import com.smartdevice.mode.LinkDeviceAttribute;
import com.smartdevice.mode.TriggerDeviceAdapter;
import com.smartdevice.mode.TriggerDeviceAttrAdapter;
import com.smartdevice.ui.HorizontalListView;
import com.smartdevice.utils.CustActionBar;
import com.smartdevice.utils.CustUtils;
import com.smartdevice.utils.DensityUtil;
import com.smartdevice.utils.LogUtil;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

public class TriggerLinkDeviceActivity extends ActionBarActivity implements OnClickListener{
    private final static String TAG = "TriggerLinkDeviceActivity";
	private HorizontalListView triggerListView;
	private List<Device> triggerList; 
	private TextView nameTextView;
	private ListView triggerAttrlListView;
	private List<DeviceAttribute> attrList;
	private TriggerDeviceAttrAdapter attrAdapter;
	private LinkDeviceAttribute linkDeviceAttr;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		init();
	}
	
	private void init(){
		setContentView(R.layout.trigger_link_device_layout);
		CustActionBar actionBar = new CustActionBar(getSupportActionBar());
        actionBar.setTitle(R.string.link_device_trigger_event);
        actionBar.getIconView().setOnClickListener(this);
		triggerListView = (HorizontalListView) findViewById(R.id.id_trigger_device_list);
		nameTextView = (TextView) findViewById(R.id.id_trigger_device_name);
		triggerAttrlListView = (ListView) findViewById(R.id.id_trigger_device_attr_list);
		triggerList = DeviceAdapter.getDeviceList();
		linkDeviceAttr = new LinkDeviceAttribute();
		if(triggerList == null || triggerList.size() == 0){
			CustUtils.showToast(this, R.string.no_devices);
		}else {
			TriggerDeviceAdapter adapter = new TriggerDeviceAdapter(this, triggerList);
			triggerListView.setAdapter(adapter);
		}
	
		triggerListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				nameTextView.setText(triggerList.get(position).getDeviceName());
				linkDeviceAttr.setGatewaySN(triggerList.get(position).getGatewaySN());
				linkDeviceAttr.setDeviceName(triggerList.get(position).getDeviceName());
				linkDeviceAttr.setDeviceSN(triggerList.get(position).getDeviceSN());
				linkDeviceAttr.setDeviceType(triggerList.get(position).getDeviceType());
				attrList = triggerList.get(position).getAttrList();
				if(attrList == null){
					attrList = new ArrayList<>();
				}
				if(attrAdapter == null){
					attrAdapter = new TriggerDeviceAttrAdapter(attrList, view.getContext());
				}else {
					attrAdapter.setAttrList(attrList);
					attrAdapter.notifyDataSetChanged();
				}
				triggerAttrlListView.setAdapter(attrAdapter);
			}
		});
		triggerAttrlListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				DeviceAttribute attribute = new DeviceAttribute();
				attribute.setAttrCode(attrList.get(position).getAttrCode());
				attribute.setAttrName(attrList.get(position).getAttrName());
				linkDeviceAttr.setDeviceAttr(attribute);
				Bundle bundle = new Bundle();
				bundle.putSerializable("com.smartdevice.mode.LinkDeviceAttribute", linkDeviceAttr);
				Intent intent = new Intent(getApplicationContext(), TriggerDeviceAttrActivity.class);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
	}
	
	@Override
	public void onAttachedToWindow() {
		// TODO Auto-generated method stub
		super.onAttachedToWindow();
		LogUtil.d(TAG, "onAttachedToWindow is called");
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.id_actionbar_icon:
			finish();
			break;

		default:
			break;
		}
		
	}

}
