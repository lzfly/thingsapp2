package com.smartdevice.main;

import java.util.List;

import com.smartdevice.control.GroupControl;
import com.smartdevice.mode.Device;
import com.smartdevice.mode.DeviceAttrType;
import com.smartdevice.mode.DeviceAttribute;
import com.smartdevice.mode.DeviceType;
import com.smartdevice.ui.CustActionBarV3;
import com.smartdevice.ui.CustSeekBar;
import com.smartdevice.utils.DensityUtil;
import com.smartdevice.utils.ParamsUtils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;

public class DeviceAttributeActivity extends ActionBarActivity{

	String[] curtainStateItem = {"打开","关上"};
	
	public String globalCurrentValue;
	private Device device;
	private int fromFlag;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		CustActionBarV3 actionBar = new CustActionBarV3(getSupportActionBar());
		actionBar.setTitle(R.string.scene_device_attribte);
		actionBar.getMenuView().setText(R.string.scene_ok);
		actionBar.getIconView().setText(R.string.scene_cancel);
		actionBar.getIconView().setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		setContentView(R.layout.add_scene_device_attribute);
		TextView nameView = (TextView) findViewById(R.id.id_device_attribute_name);
		TextView typeView = (TextView) findViewById(R.id.id_device_attribute_type);
		LinearLayout linearLayout = (LinearLayout) findViewById(R.id.id_device_attribute);
		Intent intent = getIntent();
		device = (Device)intent.getSerializableExtra("com.smartdevice.mode.Device");
		fromFlag = intent.getIntExtra(ParamsUtils.FROM_SCENE_INFO_ACTIVITY_START, 0x0);
		nameView.setText(device.getDeviceName());
		if(device.getDeviceType() != null && 
				device.getDeviceType().getTypeName() != null){
			typeView.setText(device.getDeviceType().getTypeName());
		}else {
			typeView.setVisibility(View.GONE);
		}
		
		List<DeviceAttribute> attrList = device.getAttrList();
		
        for (int i = 0; i < attrList.size(); i++) {
			DeviceAttribute attribute = attrList.get(i);
			final int attrCode = attribute.getAttrCode();
			String attrName = attribute.getAttrName();
            if(attrName == null){
            	attrName = DeviceAttrType.AttrType.getName(attrCode);
            }
			String currentValue = attrList.get(i).getAttrCurrentValue();
			DeviceAttributeView attributeView = null;
			switch (attrCode) {
			case DeviceAttrType.ATTR_TYPE_OPEN:
				ImageView imageView = new ImageView(this);
				if(currentValue.equals("1")){
					imageView.setSelected(true);
					imageView.setBackgroundResource(R.drawable.icon_switch_on);
				}else {
					imageView.setSelected(false);
					imageView.setBackgroundResource(R.drawable.icon_switch_off);
				}
				attributeView = new DeviceAttributeView(attrName, imageView);
				imageView.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if(v.isSelected()){
							v.setSelected(false);
							((ImageView)v).setBackgroundResource(R.drawable.icon_switch_off);
							globalCurrentValue = "0";
						}else {
							v.setSelected(true);
							((ImageView)v).setBackgroundResource(R.drawable.icon_switch_on);
							globalCurrentValue = "1";
						}
						updateAttribute(attrCode, globalCurrentValue);
					}
				});
				break;
			case DeviceAttrType.ATTR_TYPE_CURTAIN_STATE:
				Spinner spinner = new Spinner(this);
				ArrayAdapter<String> curtainStates = new ArrayAdapter<>
				           (this, android.R.layout.simple_spinner_item, curtainStateItem);
				spinner.setAdapter(curtainStates);
				int curtainState = Integer.valueOf(currentValue);
				if(curtainState == 0 || curtainState == 1){
					spinner.setSelection(0);
				}else {
					spinner.setSelection(1);
				}
				
				attributeView = new DeviceAttributeView(attrName, spinner);
				spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						// TODO Auto-generated method stub
						updateAttribute(attrCode, String.valueOf(position + 1));
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {
						// TODO Auto-generated method stub
						
					}
					
				});
				break;
			case DeviceAttrType.ATTR_TYPE_BRIGHTNESS:
			case DeviceAttrType.ATTR_TYPE_COLOR_TEMPER:
			case DeviceAttrType.ATTR_TYPE_TEMPERATURE:
				final CustSeekBar seekBarView = new CustSeekBar(this);
				seekBarView.getIconView().setVisibility(View.GONE);
				DensityUtil densityUtil = new DensityUtil(this);
				seekBarView.setName(attrName);
				seekBarView.getNameView().setPadding(densityUtil.dip2px(10), 0, 0, 0);
				seekBarView.createSeekBar().setBackgroundColor(getResources().getColor(R.color.white));
				attributeView = new DeviceAttributeView(null, seekBarView.createSeekBar());
				seekBarView.setCurrentValue(Float.valueOf(currentValue));
			    final SeekBar mSeekBar = seekBarView.getmSeekBar();
			    mSeekBar.setProgress(Integer.valueOf(currentValue));
			    mSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
					
					@Override
					public void onStopTrackingTouch(SeekBar seekBar) {
						// TODO Auto-generated method stub
						updateAttribute(attrCode, String.valueOf(seekBar.getProgress()));
					}
					
					@Override
					public void onStartTrackingTouch(SeekBar seekBar) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onProgressChanged(SeekBar seekBar, int progress,
							boolean fromUser) {
						// TODO Auto-generated method stub
						seekBar.setProgress(progress);
						seekBarView.setCurrentValue((float)progress);
					}
				});
				break;

			default:
				break;
			}
			if(attributeView != null){
				linearLayout.addView(attributeView.getView());
			}
		}
        
        actionBar.getMenuView().setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(fromFlag == 0x1){
					//edit attribute
					saveAttribute();
				}else {
					Intent intent = new Intent();
					Bundle bundle = new Bundle();
					bundle.putSerializable("com.smartdevice.mode.Device", device);
					intent.putExtras(bundle);
					setResult(RESULT_OK, intent);
					finish();
				}
			}
		});
	}
	
	private void saveAttribute(){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				List<DeviceAttribute> attrList = device.getAttrList();
				GroupControl groupControl = new GroupControl();
				for (int i = 0; i < attrList.size(); i++) {
				}
				Message msg = new Message();
				saveHandler.sendMessage(msg);
			}
		}).start();
	}
	
	private Handler saveHandler = new Handler(){
		@Override
		public void handleMessage(android.os.Message msg) {
			Intent intent = new Intent();
			Bundle bundle = new Bundle();
			bundle.putSerializable("com.smartdevice.mode.Device", device);
			intent.putExtras(bundle);
			setResult(RESULT_OK, intent);
			finish();
		};
	};
	
	public void updateAttribute(int attrCode, String attrValue){
		List<DeviceAttribute> attrList = device.getAttrList();
		for (int i = 0; i < attrList.size(); i++) {
			DeviceAttribute attribute = attrList.get(i);
			if(attribute.getAttrCode() == attrCode){
				attrList.get(i).setAttrCurrentValue(attrValue);
			}
		}
	}
	
	class DeviceAttributeView {
		LayoutInflater inflater;
		TextView nameView;
		LinearLayout controlLayout;
		ViewGroup view;
		
		public DeviceAttributeView(String name, View controlView) {
			// TODO Auto-generated constructor stub
			inflater = (LayoutInflater)
					getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = (ViewGroup) inflater.inflate(R.layout.device_attribute_setting, null);
			nameView = (TextView) view.findViewById(R.id.id_attribute_name);
			if(name == null){
				nameView.setVisibility(View.GONE);
			}else {
				nameView.setText(name);
			}
			controlLayout = (LinearLayout) view.findViewById(R.id.id_attribute_control);
			controlLayout.addView(controlView);
		}

		public ViewGroup getView() {
			return view;
		}
	   
	}
}
