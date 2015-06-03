package com.rraam.couponduniademo;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressLint("InflateParams")
public class ListAdapter extends BaseAdapter {
	private Activity activity;
	private LayoutInflater inflater;
	private List<Outlet> outlets;
	public ListAdapter(Activity activity, List<Outlet> outlets) {
		this.activity = activity;
		this.outlets = outlets;
	}

	@Override
	public int getCount() {
		return outlets.size();
	}

	@Override
	public Object getItem(int location) {
		return outlets.get(location);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (inflater == null)
			inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null)
			convertView = inflater.inflate(R.layout.list_view, null);
		ImageView thumbnail = (ImageView) convertView.findViewById(R.id.thumbnail);
		TextView title = (TextView) convertView.findViewById(R.id.title);
		TextView offer = (TextView) convertView.findViewById(R.id.offer);
		TextView category = (TextView) convertView.findViewById(R.id.category);
		TextView location = (TextView) convertView.findViewById(R.id.location);
		Outlet outlet = outlets.get(position);
		thumbnail.setImageBitmap(outlet.getLogoImage());
		title.setText(outlet.getOutletName());
		offer.setText(outlet.getNumCoupons() + " offers");
		String temp = "";
		for(String cat : outlet.getCategories()){
			temp += "*" + cat + "	";
		}
		category.setText(temp);
		temp="";
		location.setText(outlet.getDistance() + " m " + outlet.getNeighbourhoodName());
		return convertView;
	}
}