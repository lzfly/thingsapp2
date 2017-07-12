package com.smartdevice.mode;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.smartdevice.control.GroupControl;
import com.smartdevice.main.R;
import com.smartdevice.ui.SwipeItemLayout;
import com.smartdevice.utils.CustUtils;
import com.smartdevice.utils.LogUtil;
import com.smartdevice.utils.ParamsUtils;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

public class GroupAdpater extends BaseAdapter{
	private final static String TAG = GroupAdpater.class.getSimpleName();
	public List<Group> groupList;
	LayoutInflater inflater;
	private View parentView;
	private Context context;
    private int delPostion;
    private PopupWindow delpPopupWindow;
	
	public GroupAdpater(Context context, List<Group> groupList){
		this.context = context;
		this.groupList = groupList;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return groupList.size();
	}

	@Override
	public Group getItem(int position) {
		// TODO Auto-generated method stub
		return groupList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	class ViewHolder {
		ImageView iconView;
		TextView nameView;
		TextView delView;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder;
		if(convertView == null){
			viewHolder = new ViewHolder();
			View contentView = inflater.inflate(R.layout.group_layout, null);
			View menuView = inflater.inflate(R.layout.swipe_list_menu_layout, null);
			convertView = new SwipeItemLayout(contentView, menuView, null, null);
			viewHolder.iconView = (ImageView) contentView.findViewById(R.id.id_group_icon);
			viewHolder.nameView = (TextView) contentView.findViewById(R.id.id_group_name);
			viewHolder.delView = (TextView) menuView.findViewById(R.id.id_swipe_delete);
			convertView.setTag(viewHolder);
		}else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.nameView.setText(getItem(position).getGroupName());
		final int location = position;
		viewHolder.delView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				delPostion = location;
				LogUtil.i(TAG, "Delete group postion " + delPostion);
				final View delView = inflater.inflate(R.layout.delete_pop_window_layout, null);
				TextView delTextView = (TextView) delView.findViewById(R.id.id_delete);
				TextView cancelView = (TextView) delView.findViewById(R.id.id_cancel);
				View delLayout = delView.findViewById(R.id.id_delete_layout);
				
				delpPopupWindow = new PopupWindow(delView,
						WindowManager.LayoutParams.MATCH_PARENT,
				        WindowManager.LayoutParams.MATCH_PARENT);
				delpPopupWindow.showAtLocation(v, Gravity.NO_GRAVITY, 0, 0);
				ColorDrawable drawable = new ColorDrawable(0xb0000000);
				delpPopupWindow.setFocusable(true);
				delpPopupWindow.setOutsideTouchable(true);
				delpPopupWindow.setBackgroundDrawable(drawable);
				delView.setOnTouchListener(new OnTouchListener() {
					
					@Override
					public boolean onTouch(View v, MotionEvent event) {
						// TODO Auto-generated method stub
						if(delpPopupWindow != null && delpPopupWindow.isShowing()){
							   delpPopupWindow.dismiss();
						}
						return false;
					}
				});
				
                cancelView.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						delpPopupWindow.dismiss();
					}
				});
                
                delTextView.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if (groupList != null && groupList.size() > 0) {
							delpPopupWindow.dismiss();
							new Thread(new Runnable() {
								
								@Override
								public void run() {
									// TODO Auto-generated method stub
									//delete group here
									GroupControl groupControl = new GroupControl();
									String result = null;
									android.os.Message msg = new android.os.Message();
									try {
										result = groupControl.DeleteGroup(
												groupList.get(delPostion).getId());
									} catch (JSONException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
										result = null;
									}
									msg.obj = result;
									delHandler.sendMessage(msg);
								}
							}).start();
						}
					}
				});
			}
		});
		
		return convertView;
	}
	
	private Handler delHandler = new Handler(){
		@Override
		public void handleMessage(android.os.Message msg) {
			String result = (String) msg.obj;
			groupList.remove(delPostion);
			notifyDataSetChanged();
			CustUtils.showToast(context, R.string.grobal_delete_successful);
//			try {
//				JSONObject object = new JSONObject(result);
//				int status = object.getInt("status");
//				if(status == ParamsUtils.SUCCESS_CODE){
//					groupList.remove(delPostion);
//					notifyDataSetChanged();
//					CustUtils.showToast(context, R.string.grobal_delete_successful);
//				}else {
//					CustUtils.showToast(context, R.string.grobal_delete_fail);
//				}
//			} catch (JSONException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//				LogUtil.e(TAG, e.toString());
//				CustUtils.showToast(context, R.string.grobal_delete_fail);
//			}
		};
	};
	
	public void setParentView(View parentView){
		this.parentView = parentView;
	}
	
	public PopupWindow getPopupWindow(){
		return delpPopupWindow;
	}
	
	public void setGroupList(List<Group> groupList){
		this.groupList = groupList;
	}

}
