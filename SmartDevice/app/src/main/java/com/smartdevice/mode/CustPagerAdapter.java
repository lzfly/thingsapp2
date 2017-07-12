package com.smartdevice.mode;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

public class CustPagerAdapter extends PagerAdapter {
   
	private  List<Object> mItemsList;
	
	public CustPagerAdapter() {
		super();
		mItemsList = new ArrayList<Object>();
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mItemsList.size();
	}
	
	public Object getItem(int position){
		return mItemsList.get(position);
	}
	
	public void addItem(Object object){
		mItemsList.add(object);
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		// TODO Auto-generated method stub
		return view == object;
	}
	
	 @Override
     public void destroyItem(ViewGroup container, int position, Object object) {
         // TODO Auto-generated method stub
		 container.removeViewAt(position);
     }
     
     @Override
     public Object instantiateItem(ViewGroup container, int position) {
         // TODO Auto-generated method stub
    	 container.addView((View)getItem(position));
         return getItem(position);
     }

}
