package com.smartdevice.main;

import com.smartdevice.ui.CustActionBarV3;
import com.smartdevice.utils.CustUtils;
import com.smartdevice.utils.ParamsUtils;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class SetSceneNameActivity extends ActionBarActivity{
	
	private Toast mToast;  
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.set_scene_name_layout);
		CustActionBarV3 actionBar = new CustActionBarV3(getSupportActionBar());
		actionBar.setTitle(R.string.scene_name);
		actionBar.getMenuView().setText(R.string.scene_ok);
		actionBar.getIconView().setText(R.string.scene_cancel);
		ImageView clearView = (ImageView) findViewById(R.id.id_scene_name_clear);
		final EditText sceneNameView = (EditText) findViewById(R.id.id_scene_name);
		clearView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				sceneNameView.setText("");
			}
		});
		
		actionBar.getMenuView().setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String sceneName = sceneNameView.getText().toString().trim();
				sceneName = sceneName.replace(" ", "");
				if(sceneName.equals("")){
					showToast(getString(R.string.scene_name_is_empty));
				}else {
					boolean legal = CustUtils.checkLegalString(sceneName);
					if(!legal){
						CustUtils.showToast(v.getContext(), R.string.grobal_illegal_string);
					}else {
						Intent intent = new Intent();
						intent.putExtra(ParamsUtils.SCENE_NAME, sceneName);
						setResult(RESULT_OK, intent);
						finish();
					}
					
				}
			}
		});
		
		actionBar.getIconView().setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
	}

	private void showToast(String toast){
		if(mToast == null){
			mToast = Toast.makeText(this, toast, Toast.LENGTH_LONG);
		}else {
			mToast.setText(toast);
		}
		mToast.show();
	}
}
