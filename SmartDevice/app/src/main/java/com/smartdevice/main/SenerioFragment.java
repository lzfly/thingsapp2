package com.smartdevice.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.smartdevice.control.DeviceControl;
import com.smartdevice.control.GatewayControl;
import com.smartdevice.control.GroupControl;
import com.smartdevice.mode.Device;
import com.smartdevice.mode.Group;
import com.smartdevice.mode.GroupAdpater;
import com.smartdevice.mode.GroupBindDevice;
import com.smartdevice.mode.Session;
import com.smartdevice.ui.SwipeListView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;

public class SenerioFragment extends Fragment{

	List<Group> groupList = new ArrayList<>();
	ListView groupsView;
	GroupAdpater groupAdpater;
	private View view;
	private static final int SCENE_INFO_REQUEST_CODE = 0x0;
	private static final int SCENE_ADD_REQUEST_CODE  = 0x1;
	
	private Map<String, List<GroupBindDevice>> groupBindDeviceMap =  new HashMap<String, List<GroupBindDevice>>();
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.fragment_senerio_layout, container, false);
		ImageView addSceneView = (ImageView) view.findViewById(R.id.id_add_scene_img);
		//groupsView = (ListView) view.findViewById(R.id.id_groups);
		groupsView = (SwipeListView) view.findViewById(R.id.id_groups);
		
		addSceneView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(v.getContext(), AddSceneActivity.class);
				startActivityForResult(intent, SCENE_ADD_REQUEST_CODE);
				
			}
		});
		
		groupsView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(), GroupInfoActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("com.smartdevice.mode.group", groupList.get(position));
				intent.putExtras(bundle);
				startActivityForResult(intent, SCENE_INFO_REQUEST_CODE);
			}
			
		});		
        getGroupList();		
		   
		return view;
		
	}
	
	private void getGroupList(){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				GroupControl groupControl = new GroupControl();
				groupList = groupControl.getGroups(Session.getUsername());
				Message msg = new Message();
				msg.obj = groupList;
				groupHandler.sendMessage(msg);
			}
		}).start();	
	}
	
	private Handler groupHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			List<Group> groupList = (List<Group>) msg.obj;
			if(groupList != null && groupList.size() > 0){
				if(groupAdpater != null){
					groupAdpater.setGroupList(groupList);
					groupAdpater.notifyDataSetChanged();
				}else {
					groupAdpater = new GroupAdpater(getActivity(), groupList);
					/*设置一个parent view, 好让PopupWindow显示出来*/
					//groupAdpater.setParentView(view);
					groupsView.setAdapter(groupAdpater);
				}
			}
		};
	};
	
	public GroupAdpater getGroupAdapter(){
		return groupAdpater;
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		//super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == Activity.RESULT_OK){
			if(requestCode == SCENE_ADD_REQUEST_CODE){
				/*update the group list*/
				getGroupList();
			}
		}
	}
	
}
