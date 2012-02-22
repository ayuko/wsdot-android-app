package gov.wa.wsdot.android.wsdot.ui;

import gov.wa.wsdot.android.wsdot.R;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.viewpagerindicator.TitlePageIndicator;
import com.viewpagerindicator.TitleProvider;

public class MountainPassDetailsFragment extends Fragment {

	private static String[] titles = new String[] { "Cameras", "Report", "Forecast" }; // Map
	ViewPagerAdapter adapter;
	ViewPager pager;
	TitlePageIndicator indicator;	
		
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	ViewGroup root = (ViewGroup) inflater.inflate(R.layout.simple_titles, null);
        root.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.FILL_PARENT));

	    adapter = new ViewPagerAdapter();
	    pager = (ViewPager) root.findViewById(R.id.pager);
	    indicator = (TitlePageIndicator) root.findViewById(R.id.indicator);
	    
		return root;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
	    pager.setAdapter(adapter);
	    indicator.setViewPager(pager);
	    pager.setCurrentItem(1);
	}
	
    private class ViewPagerAdapter extends PagerAdapter implements TitleProvider {

    	public String getTitle(int position) {
    		return titles[position];
    	}

    	public int getCount() {
    		return titles.length;
    	}

        public Object instantiateItem(View collection, int position) {
			LayoutInflater inflater = (LayoutInflater) collection.getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			
			int resId = 0;
			switch (position) {
			case 0:
				resId = R.layout.gallery;
				break;
			case 1:
				resId = R.layout.fragment_mountainpass_item_report;
				break;
			case 2:
				resId = R.layout.simple_list_item;
				break;
				/*
			case 3:
				resId = R.layout.map;
				break;
				*/
			}

			View view = inflater.inflate(resId, null);
			((ViewPager) collection).addView(view, 0);

			return view;
        }

        @Override
        public void destroyItem(View collection, int position, Object view) {
        	((ViewPager) collection).removeView((View) view);
        }

        @Override
        public void finishUpdate(View arg0) {
        	// TODO Auto-generated method stub
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
        	return arg0 == ((View) arg1);

        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {
        	// TODO Auto-generated method stub
        }

        @Override
        public Parcelable saveState() {
        	// TODO Auto-generated method stub
        	return null;
        }

        @Override
        public void startUpdate(View arg0) {
        	// TODO Auto-generated method stub
        }

    }	

}
