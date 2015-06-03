package com.rraam.couponduniademo;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity implements ActionBar.TabListener {
	FragmentAdapter mSectionsPagerAdapter;
	ViewPager mViewPager;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		final ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		mSectionsPagerAdapter = new FragmentAdapter(getSupportFragmentManager(), MainActivity.this);
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
		mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				actionBar.setSelectedNavigationItem(position);
			}
		});
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			actionBar.addTab(actionBar.newTab().setText(mSectionsPagerAdapter.getPageTitle(i)).setTabListener(this));
		}
	}
	@Override
	public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
		mViewPager.setCurrentItem(tab.getPosition());
	}
	@Override
	public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {}
	@Override
	public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {}
	
	public static class PlaceholderFragment extends Fragment {
		private static final String ARG_SECTION_NUMBER = "section_number";
		private ProgressDialog pDialog;
		private ListView listView;
		private List<Outlet> outlet = new ArrayList<Outlet>();
		public static PlaceholderFragment newInstance(int sectionNumber) {
			PlaceholderFragment fragment = new PlaceholderFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}
		public PlaceholderFragment() {}
		@SuppressLint("NewApi")
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
			try{
				if(getArguments().getInt(ARG_SECTION_NUMBER)==1){
					View view = inflater.inflate(R.layout.nearby, container, false);
					listView = (ListView) view.findViewById(R.id.list);
					pDialog = new ProgressDialog(view.getContext());
					pDialog.setMessage("Loading...");
					pDialog.show();
					getActivity().getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#1b1b1b")));
					new LoadJSON().execute("http://staging.couponapitest.com/task_data.txt");
					return view;
				}else{
					View rootView = inflater.inflate(R.layout.fragment_main, container,	false);
					return rootView;
				}
			}catch(Exception e){
				Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
				return null;
			}
		}

		public class LoadJSON extends AsyncTask<String, String, String> {
			@Override
			protected String doInBackground(String... params) {
				try{
					GetHttpData obj = new GetHttpData();
					return obj.getData(new URI(params[0]));
				}catch(Exception e){
					return null;
				}
			}
			@Override
			protected void onPostExecute(String result) {
				 if(result!=null){
					 pDialog.dismiss();
					 try{
						 JSONObject json = new JSONObject(new JSONObject(result).getString("data"));
						 int n = json.length();
						 for (int i=0; i<n; i++) {
							 try{
								JSONObject jsonCurr = json.getJSONObject(String.valueOf(i));
								Outlet outletTemp = new Outlet();
								outletTemp.setOutletId(Integer.parseInt(jsonCurr.getString("OutletID")));
								outletTemp.setDistance(jsonCurr.getDouble("Distance"));
								outletTemp.setNumCoupons(jsonCurr.getInt("NumCoupons"));
								outletTemp.setLongitude(Double.parseDouble(jsonCurr.getString("Longitude")));
								outletTemp.setLatitude(Double.parseDouble(jsonCurr.getString("Latitude")));
								outletTemp.setOutletName(jsonCurr.getString("OutletName"));
								outletTemp.setLogoURL(new URI(jsonCurr.getString("LogoURL")));
								outletTemp.setNeighbourhoodName(jsonCurr.getString("NeighbourhoodName"));
								List<String> category = new ArrayList<String>();
								for(int j=0; j<jsonCurr.getJSONArray("Categories").length(); j++){
									JSONObject jsonCurrObject = jsonCurr.getJSONArray("Categories").getJSONObject(j);
									category.add(jsonCurrObject.getString("Name"));
								}
								outletTemp.setCategories(category);
								outlet.add(outletTemp);
								ListAdapter adapter = new ListAdapter(getActivity(), outlet);
								listView.setAdapter(adapter);
								outletTemp.loadLogoImage(adapter);
							 }catch(Exception e){
								 n++;
								 continue;
							 }
						 }
						ListAdapter adapter = new ListAdapter(getActivity(), outlet);
						listView.setAdapter(adapter);
						for (Outlet outletTemp : outlet) {
							outletTemp.loadLogoImage(adapter);
						}
					 }catch(Exception e){ 
						 Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
					 }
				 }else{
					 Toast.makeText(getActivity(), "Some Error", Toast.LENGTH_LONG).show();
				 }
			}
		}
	}
}