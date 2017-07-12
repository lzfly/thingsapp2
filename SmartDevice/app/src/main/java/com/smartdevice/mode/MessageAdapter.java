package com.smartdevice.mode;

import java.util.List;

import com.smartdevice.main.R;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MessageAdapter extends BaseAdapter{

	public List<Message> messageList;
	private Context context;
	private LayoutInflater inflater;
	
	public MessageAdapter(Context context){
		this.context = context;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return messageList.size();
	}

	@Override
	public Message getItem(int position) {
		// TODO Auto-generated method stub
		return messageList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	public List<Message> getMessageList() {
		return messageList;
	}

	public void setMessageList(List<Message> messageList) {
		this.messageList = messageList;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.message_layout, null);
		ImageView isReadIcon = (ImageView) view.findViewById(R.id.id_message_icon);
		TextView titleView = (TextView) view.findViewById(R.id.id_message_title);
		TextView fromView = (TextView) view.findViewById(R.id.id_message_from);
		TextView timeView = (TextView) view.findViewById(R.id.id_message_time);
		TextView contentView = (TextView) view.findViewById(R.id.id_message_content);
		titleView.setText(getItem(position).getTitle());
		fromView.setText(getItem(position).getFromName());
		timeView.setText(getItem(position).getTime());
		contentView.setText(getItem(position).getContent());
		contentView.setTextColor(Color.GRAY);
//		Message message = new Message();
//		message.setContent("收到报警信息，家里可能被盗，请确认财产安全请确认财产安全请确认财产安全请确认财产安全请确认财产安全请确认财产安全请确认财产安全请确认财产安全请确认财产安全请确认财产安全请确认财产安全。");
//		message.setFromName("人体红外");
//		message.setTime("2015-10-20 20:09:32");
//		message.setFromType("红外传感器");
//		message.setTitle("传感器报警信息");
		view.setTag(getItem(position));
		return view;
	}
	
	

}
