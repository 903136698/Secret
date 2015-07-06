package com.jikexueyuan.secret.atys;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jikexueyuan.secret.R;
import com.jikexueyuan.secret.net.Message;

import java.util.ArrayList;
import java.util.List;

public class AtyTimelineMessageListAdapter extends BaseAdapter{


	private List<Message> data = new ArrayList<Message>();
	private Context context=null;

	// 构造函数，传递context对象

	public AtyTimelineMessageListAdapter(Context context) {
		this.context = context;
	}
	
	@Override
	public int getCount() {
		return data.size();
	}
	
	@Override
	public Message getItem(int position) {
		return data.get(position);
	}
	
	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			// 生成一个convertView
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.aty_timeline_list_cell, null);
			convertView.setTag(new ListCell((TextView) convertView.findViewById(R.id.tvCellLabel)));
		}
		
		ListCell lc = (ListCell) convertView.getTag();
		
		Message msg = getItem(position);
		
		lc.getTvCellLabel().setText(msg.getMsg());
		
		return convertView;
	}
	
	public Context getContext() {
		return context;
	}
	
	public void addAll(List<Message> data){
		this.data.addAll(data);
		notifyDataSetChanged();		// 数据改变，更新列表
	}

	// 清空
	public void clear(){
		data.clear();
		notifyDataSetChanged();
	}

	
	private static class ListCell{

		private TextView tvCellLabel;

		public TextView getTvCellLabel() {
			return tvCellLabel;
		}
		// 构造方法
		public ListCell(TextView tvCellLabel) {
			this.tvCellLabel = tvCellLabel;
		}

	}
}
