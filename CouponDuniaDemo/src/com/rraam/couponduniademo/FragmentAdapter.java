package com.rraam.couponduniademo;

import java.util.Locale;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.rraam.couponduniademo.MainActivity.PlaceholderFragment;

public class FragmentAdapter extends FragmentPagerAdapter{
	Context mContext;
	public FragmentAdapter(FragmentManager fm, Context mContext) {
		super(fm);
		this.mContext = mContext;
	}

	@Override
	public Fragment getItem(int position) {
		return PlaceholderFragment.newInstance(position + 1);
	}

	@Override
	public int getCount() {
		return 1;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		Locale l = Locale.getDefault();
		switch (position) {
		case 0:
			return mContext.getString(R.string.title_section2).toUpperCase(l);
		}
		return null;
	}
}
