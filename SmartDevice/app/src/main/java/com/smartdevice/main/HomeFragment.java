package com.smartdevice.main;

import com.smartdevice.mode.CustPagerAdapter;
import com.smartdevice.utils.DensityUtil;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class HomeFragment extends Fragment implements OnPageChangeListener{
	
	private ViewGroup viewGroup;
	private ImageView imageView; 
	private ImageView[] imageViews;
	private LinearLayout deviceLayout;
	private LinearLayout statisticLayout;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_home, container, false);
		
	    initHomeFragment(view);
	    PlateClickListener plateClickListener = new PlateClickListener();
	    ImageView deviceView = (ImageView) view.findViewById(R.id.id_devices);
	    deviceView.setOnClickListener(plateClickListener);
	    deviceLayout = (LinearLayout) view.findViewById(R.id.id_devices_ly);
	    //deviceLayout.setOnClickListener(new PlateClickListener());
	    statisticLayout = (LinearLayout) view.findViewById(R.id.id_statistics_ly);
	    ImageView senerioView = (ImageView) view.findViewById(R.id.id_senerio);
	    senerioView.setOnClickListener(plateClickListener);
	    ImageView statisticView = (ImageView) view.findViewById(R.id.id_statistics);
	    statisticView.setOnClickListener(plateClickListener);
	    
		return view;
	}
	
	class PlateClickListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent;
			switch (v.getId()) {
			case R.id.id_devices_ly:
			
			case R.id.id_devices:
				deviceLayout.setPressed(true);
				deviceLayout.setSelected(true);
				deviceLayout.setBackgroundResource(R.drawable.main_plate_selector);
				intent = new Intent(getActivity(), DeviceManageActivity.class);
				startActivity(intent);
				break;
			
			case R.id.id_senerio:
				intent = new Intent(getActivity(), SenerioActivity.class);
				startActivity(intent);
				break;
				
			case R.id.id_statistics:
				statisticLayout.setPressed(true);
				statisticLayout.setBackgroundResource(R.drawable.main_plate_selector);
				intent = new Intent(getActivity(), StatisticsActivity.class);
				startActivity(intent);
				break;

			default:
				break;
			}
		}
	}
		
	
	private void initHomeFragment(View view){
		ViewPager viewPager = (ViewPager) view.findViewById(R.id.id_adv_viewpager);
		CustPagerAdapter mPagerAdapter = new CustPagerAdapter();
		ImageView imageView1 = new ImageView(view.getContext());
		ImageView imageView2 = new ImageView(view.getContext());
		imageView1.setImageResource(R.drawable.view_pager_1);
		imageView2.setImageResource(R.drawable.view_pager_2);
		imageView1.setScaleType(ImageView.ScaleType.FIT_XY);
		imageView2.setScaleType(ImageView.ScaleType.FIT_XY);
		DensityUtil densityUtil= new DensityUtil(view.getContext());
		int margin = densityUtil.dip2px(3);
		mPagerAdapter.addItem(imageView1);
		mPagerAdapter.addItem(imageView2);
		//mPagerAdapter.addItem(object);	
		viewPager.setAdapter(mPagerAdapter);
		viewPager.setOnPageChangeListener(this);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(densityUtil.dip2px(8),densityUtil.dip2px(8));
		params.setMargins(margin, 0, margin, 0);
		imageViews = new ImageView[mPagerAdapter.getCount()];  
		viewGroup = (ViewGroup) view.findViewById(R.id.id_flipper);
		for(int i=0; i < mPagerAdapter.getCount(); i++){
			imageView = new ImageView(view.getContext());
            imageView.setLayoutParams(params);
            
			//imageView.setPadding(padding, padding,
			//		padding, padding);
            imageViews[i] = imageView;   
            
            if (i == 0) {  
                imageViews[i].setBackgroundResource(R.drawable.icon_bullet_red);  
            } else {  
                imageViews[i].setBackgroundResource(R.drawable.icon_bullet_grey);  
            }  
            
            viewGroup.addView(imageViews[i]); 
		}
	}

	@Override
	public void onPageScrolled(int position, float positionOffset,
			int positionOffsetPixels) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageSelected(int position) {
		// TODO Auto-generated method stub
		for(int i = 0; i < imageViews.length; i++){
			imageViews[position].setBackgroundResource(R.drawable.icon_bullet_red);
			if(position != i){
				imageViews[i].setBackgroundResource(R.drawable.icon_bullet_grey);
			}
		}
	}

	@Override
	public void onPageScrollStateChanged(int state) {
		// TODO Auto-generated method stub
		
	}
}
