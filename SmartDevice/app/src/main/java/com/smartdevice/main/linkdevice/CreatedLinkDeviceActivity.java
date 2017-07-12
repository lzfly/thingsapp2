package com.smartdevice.main.linkdevice;

import java.util.ArrayList;
import java.util.List;

import com.smartdevice.control.LogicEntigyControl;
import com.smartdevice.main.R;
import com.smartdevice.mode.CreatedLinkDeviceAdapter;
import com.smartdevice.mode.LogicEntity;
import com.smartdevice.mode.Session;
import com.smartdevice.utils.CustActionBar;
import com.smartdevice.utils.CustUtils;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class CreatedLinkDeviceActivity extends ActionBarActivity{

	private List<LogicEntity> allEntityList;
	private ListView createdListView;
	private CreatedLinkDeviceAdapter adapter;
	
	private final static int LINK_DEVICE_INFO_REQUEST = 0x0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		CustActionBar actionBar = new CustActionBar(getSupportActionBar());
		actionBar.setTitle(R.string.link_device_all);
		actionBar.getIconView().setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		init();
		getCreatedLinkDevice();
	}
	
	private void init(){
		setContentView(R.layout.created_link_device_layout);
		createdListView = (ListView) findViewById(R.id.id_created_link_device_list);
		
		createdListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(), CreatedLinkDeviceInfoActivity.class);
				intent.putExtra("com.smartdevice.mode.LogicEntity", allEntityList.get(position));
				startActivityForResult(intent, LINK_DEVICE_INFO_REQUEST);
			}
			
		});
	}
	
	private void getCreatedLinkDevice(){
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				LogicEntigyControl control = new LogicEntigyControl();
				allEntityList = control.getLogicEntitys(Session.getUsername());
				handler.sendEmptyMessage(0);
			}
		}
		).start();
	}
	
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(android.os.Message msg) {
			if(allEntityList != null && allEntityList.size() > 0){
				adapter = new CreatedLinkDeviceAdapter(getApplicationContext(), allEntityList);
				createdListView.setAdapter(adapter);
			}else {
				allEntityList = new ArrayList<>();
				adapter = new CreatedLinkDeviceAdapter(getApplicationContext(), allEntityList);
				createdListView.setAdapter(adapter);
				CustUtils.showToast(getApplicationContext(), R.string.link_device_none);
			}
		};
	};
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == RESULT_OK){
			getCreatedLinkDevice();
		}
	};
}
