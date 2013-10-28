package com.kna.wifikeyreminder.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kna.wifikeyreminder.R;
import com.kna.wifikeyreminder.model.Wifi;

public class WifiListAdapter extends BaseAdapter {

	private List<Wifi> wifis;
	private LayoutInflater inflater = null;

	public WifiListAdapter(List<Wifi> wifis, Context context) {
		super();
		this.wifis = wifis;
		this.inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return wifis.size();
	}

	@Override
	public Object getItem(int position) {
		return wifis.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View item = convertView;
		ViewHolder holder;
		
		if (item == null) {
			item = inflater.inflate(R.layout.list_item, null);
			
			holder = new ViewHolder();
			holder.textViewListItem = (TextView) item.findViewById(R.id.textViewListItem);
			
			item.setTag(holder);
		}else{
			holder = (ViewHolder)item.getTag();
		}

		holder.textViewListItem.setText(wifis.get(position).getBssid());
		
		return item;
	}
	
	static class ViewHolder {
	    TextView textViewListItem;
	}
}
