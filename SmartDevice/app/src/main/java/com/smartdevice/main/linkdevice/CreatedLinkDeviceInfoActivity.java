package com.smartdevice.main.linkdevice;

import org.json.JSONException;

import com.smartdevice.control.LogicEntigyControl;
import com.smartdevice.main.R;
import com.smartdevice.mode.LogicEntity;
import com.smartdevice.mode.Session;
import com.smartdevice.utils.CustActionBar;
import com.smartdevice.utils.CustUtils;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class CreatedLinkDeviceInfoActivity extends ActionBarActivity{

	private LogicEntity entity;
	private String result = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.created_link_device_info_layout);
		entity = (LogicEntity) getIntent().getSerializableExtra("com.smartdevice.mode.LogicEntity");
		init();
	}
	
	private void init(){
		ImageView ifImageView = (ImageView) findViewById(R.id.id_created_link_device_info_if);
		ImageView thenImageView = (ImageView) findViewById(R.id.id_created_link_device_info_then);
		TextView descTextView = (TextView) findViewById(R.id.id_created_link_device_info_desc);
		ImageView switchImageView = (ImageView) findViewById(R.id.id_created_link_device_info_turn_on);
		CustUtils.setDeviceIcon(entity.getIfTypeCode(), ifImageView);
		CustUtils.setDeviceIcon(entity.getThTypeCode(), thenImageView);
		String descEvent = "如果" + entity.getIfDeviceName() + 
				CustUtils.getAttrName(entity.getIfAttrCode()) + 
				CustUtils.getOperateName(entity.getIfAttrCode(), entity.getIfOperateCode()) + 
				CustUtils.getControlName(entity.getIfAttrCode(), entity.getIfAttrValue());
		String descAction = "然后" + entity.getThDeviceName() + 
				CustUtils.getAttrName(entity.getThAttrCode()) + 
				"设为" + 
				CustUtils.getControlName(entity.getThAttrCode(), entity.getThAttrValue());
		descTextView.setText(descEvent + ", " + descAction);
		
		CustActionBar actionBar = new CustActionBar(getSupportActionBar());
		actionBar.setTitle(R.string.link_device_info);
		actionBar.setIconOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		actionBar.getMenuTxView().setVisibility(View.VISIBLE);
		actionBar.getMenuTxView().setText(R.string.link_device_delete);
		actionBar.getMenuTxView().setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlertDialog.Builder alertDialog = new AlertDialog.Builder(CreatedLinkDeviceInfoActivity.this);
				alertDialog.setMessage(R.string.sure_delete);
				alertDialog.setPositiveButton(R.string.grobal_sure, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						deleleLinkDevice();
						dialog.dismiss();
					}
				});
				alertDialog.setNegativeButton(R.string.grobal_cancel, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				});
				
				alertDialog.show();
			}
		});
	}
	
	private void deleleLinkDevice(){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				LogicEntigyControl control = new LogicEntigyControl();
				try {
					result = control.DeleteLogicEntity(entity.getId());
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				delHandler.sendEmptyMessage(0);
			}
		}).start();
	}
	
	private Handler delHandler = new Handler(){
		
		@Override
		public void handleMessage(android.os.Message msg) {
			if(result != null){
				//...Should add something to check the response, but the API(or server code) ...
				CustUtils.showToast(CreatedLinkDeviceInfoActivity.this, R.string.grobal_delete_successful);
				setResult(RESULT_OK);
				finish();
			}else {
				CustUtils.showToast(CreatedLinkDeviceInfoActivity.this, R.string.grobal_delete_fail);
			}
		};
	};
}
