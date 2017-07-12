package com.smartdevice.main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.smartdevice.control.DBHelper;
import com.smartdevice.mode.Device;
import com.smartdevice.mode.Message;
import com.smartdevice.mode.MessageAdapter;
import com.smartdevice.utils.ACache;
import com.smartdevice.utils.CustActionBar;
import com.smartdevice.utils.LogUtil;
import com.smartdevice.utils.ParamsUtils;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;

public class MessageFragment extends Fragment implements OnItemClickListener{

	private View mView;
	private LinearLayout mMsgLayout;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mView =  inflater.inflate(R.layout.fragment_msg, container, false);
		mMsgLayout = (LinearLayout) mView.findViewById(R.id.id_msg_ly);
		ListView messageListView = (ListView) mView.findViewById(R.id.id_message_list);
		messageListView.setOnItemClickListener(this);
		MessageAdapter messageAdapter = new MessageAdapter(getActivity());
		messageAdapter.setMessageList(getMessages());
		messageListView.setAdapter(messageAdapter);
		return mView;
	}
	
	private void buildMessageDB(){
    	DBHelper dbHelper = new DBHelper(getActivity(), ParamsUtils.DB_NAME);
    	String sql = "CREATE TABLE  IF NOT EXISTS sd_message(" +
    			"id INT PRIMARY KEY AUTOINCREMENT," +
    			"fromname VARCHAR(20)," +
    			"fromtype VARCHAR(20)," +
    			"title VARCHAR(60)," +
    			"time VARCHAR(20)," +
    			"content VARCHAR(200));";
    	LogUtil.d("MessageFragment", "create sql: " + sql);
    	dbHelper.setSqlString(sql);
    }
	
	private List<Message> getMessages(){
		//Read the message database
		List<Message> messageList = new ArrayList<>();
		DBHelper dbHelper = new DBHelper(getActivity(), ParamsUtils.DB_NAME);
		//String sql = "select * from sd_message";
		buildMessageDB();
		Cursor mCursor =  dbHelper.query("sd_message", null, null, null, null, null, null);
		if((mCursor != null) && (mCursor.getCount() > 0)){
			if(mCursor.moveToFirst()){
				LogUtil.i("MessageFragment", "message count " + mCursor.getCount());
				for (int i = 0; i < mCursor.getCount(); i++) {
					Message message = new Message();
					mCursor.moveToPosition(i);
					message.setId(mCursor.getInt(mCursor.getColumnIndex("id")));
					message.setFromName(mCursor.getString(mCursor.getColumnIndex("fromname")));
					message.setFromType(mCursor.getString(mCursor.getColumnIndex("fromtype")));
					message.setTitle(mCursor.getString(mCursor.getColumnIndex("title")));
					message.setTime(mCursor.getString(mCursor.getColumnIndex("time")));
					message.setContent(mCursor.getString(mCursor.getColumnIndex("content")));
					messageList.add(message);
					
				}
			}
		}
		Collections.reverse(messageList);
		return messageList;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(getActivity(), MessageDetailActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable("com.smartdevice.mode.Message", (Message) view.getTag());
		intent.putExtras(bundle);
		//startActivityForResult(intent, 0);
	}
}
