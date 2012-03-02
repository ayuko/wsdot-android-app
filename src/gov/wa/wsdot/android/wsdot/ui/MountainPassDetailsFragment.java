/*
 * Copyright (c) 2012 Washington State Department of Transportation
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 *
 */

package gov.wa.wsdot.android.wsdot.ui;

import gov.wa.wsdot.android.wsdot.R;
import gov.wa.wsdot.android.wsdot.shared.CameraItem;
import gov.wa.wsdot.android.wsdot.shared.ForecastItem;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TabHost;
import android.widget.TextView;

public class MountainPassDetailsFragment extends BaseActivity implements
	CompoundButton.OnCheckedChangeListener {
	
	private static final String DEBUG_TAG = "MountainPassDetailsFragment";
	DateFormat parseDateFormat = new SimpleDateFormat("yyyy,M,d,H,m"); //e.g. [2010, 11, 2, 8, 22, 32, 883, 0, 0]
	DateFormat displayDateFormat = new SimpleDateFormat("MMMM d, yyyy h:mm a");

	private TabHost mTabHost;
    private TabManager mTabManager;
	private String mTitle;
	private CompoundButton mStarred;
	private String mDateUpdated;
	
	private ArrayList<CameraItem> cameraItems;
	private ArrayList<ForecastItem> forecastItems;
		
	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.fragment_mountainpass_details);
		
        mTabHost = (TabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup();
        
		Bundle args = getIntent().getExtras();
		if (args != null) {
		    cameraItems = (ArrayList<CameraItem>)getIntent().getSerializableExtra("Cameras");
		    forecastItems = (ArrayList<ForecastItem>)getIntent().getSerializableExtra("Forecasts");
			
			String tempDate = args.getString("DateUpdated");
			mTitle = args.getString("MountainPassName");
			
			try {
				tempDate = tempDate.replace("[", "");
				tempDate = tempDate.replace("]", "");
				
				String[] a = tempDate.split(",");
				StringBuilder result = new StringBuilder();
				for (int i=0; i < 5; i++) {
					result.append(a[i]);
					result.append(",");
				}
				tempDate = result.toString().trim();
				tempDate = tempDate.substring(0, tempDate.length()-1);
				Date date = parseDateFormat.parse(tempDate);
				mDateUpdated = displayDateFormat.format(date);
			} catch (Exception e) {
				Log.e(DEBUG_TAG, "Error parsing date: " + tempDate, e);
			}
		}    
        
        ((TextView) findViewById(R.id.mountainpass_title)).setText(mTitle);
        ((TextView) findViewById(R.id.mountainpass_subtitle)).setText(mDateUpdated);
        mStarred = (CompoundButton) findViewById(R.id.star_button);
        
        mStarred.setFocusable(true);
        mStarred.setClickable(true);
        
        mTabManager = new TabManager(this, mTabHost, R.id.realtabcontent);

        mTabManager.addTab(mTabHost.newTabSpec("report")
        		.setIndicator(buildIndicator("Report")),
                MountainPassItemDetailsFragment.class, null);
        
        if (!cameraItems.isEmpty()) {
	        mTabManager.addTab(mTabHost.newTabSpec("cameras")
	        		.setIndicator(buildIndicator("Cameras")),
	                MountainPassItemCameraFragment.class, null);
        }
        if (!forecastItems.isEmpty()) {
	        mTabManager.addTab(mTabHost.newTabSpec("forecast")
	        		.setIndicator(buildIndicator("Forecast")),
	                MountainPassItemForecastFragment.class, null);
        }
        /*
        mTabManager.addTab(mTabHost.newTabSpec("map")
        		.setIndicator(buildIndicator("Map")),
                MountainPassItemMapFragment.class, null);
        */
        if (savedInstanceState != null) {
            mTabHost.setCurrentTabByTag(savedInstanceState.getString("tab"));
        }
        
	}

    /**
     * Build a {@link View} to be used as a tab indicator, setting the requested string resource as
     * its label.
     *
     * @param textRes
     * @return View
     */
    private View buildIndicator(String textRes) {
        final TextView indicator = (TextView) getLayoutInflater()
                .inflate(R.layout.tab_indicator,
                        (ViewGroup) findViewById(android.R.id.tabs), false);
        indicator.setText(textRes);
        return indicator;
    }	
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("tab", mTabHost.getCurrentTabTag());
    }	
		
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO Auto-generated method stub
	}
	
    /**
     * This is a helper class that implements a generic mechanism for
     * associating fragments with the tabs in a tab host.  It relies on a
     * trick.  Normally a tab host has a simple API for supplying a View or
     * Intent that each tab will show.  This is not sufficient for switching
     * between fragments.  So instead we make the content part of the tab host
     * 0dp high (it is not shown) and the TabManager supplies its own dummy
     * view to show as the tab content.  It listens to changes in tabs, and takes
     * care of switch to the correct fragment shown in a separate content area
     * whenever the selected tab changes.
     */
    public static class TabManager implements TabHost.OnTabChangeListener {
        private final FragmentActivity mActivity;
        private final TabHost mTabHost;
        private final int mContainerId;
        private final HashMap<String, TabInfo> mTabs = new HashMap<String, TabInfo>();
        TabInfo mLastTab;

        static final class TabInfo {
            private final String tag;
            private final Class<?> clss;
            private final Bundle args;
            private Fragment fragment;

            TabInfo(String _tag, Class<?> _class, Bundle _args) {
                tag = _tag;
                clss = _class;
                args = _args;
            }
        }

        static class DummyTabFactory implements TabHost.TabContentFactory {
            private final Context mContext;

            public DummyTabFactory(Context context) {
                mContext = context;
            }

            public View createTabContent(String tag) {
                View v = new View(mContext);
                v.setMinimumWidth(0);
                v.setMinimumHeight(0);
                return v;
            }
        }

        public TabManager(FragmentActivity activity, TabHost tabHost, int containerId) {
            mActivity = activity;
            mTabHost = tabHost;
            mContainerId = containerId;
            mTabHost.setOnTabChangedListener(this);
        }

        public void addTab(TabHost.TabSpec tabSpec, Class<?> clss, Bundle args) {
            tabSpec.setContent(new DummyTabFactory(mActivity));
            String tag = tabSpec.getTag();

            TabInfo info = new TabInfo(tag, clss, args);

            // Check to see if we already have a fragment for this tab, probably
            // from a previously saved state.  If so, deactivate it, because our
            // initial state is that a tab isn't shown.
            info.fragment = mActivity.getSupportFragmentManager().findFragmentByTag(tag);
            if (info.fragment != null && !info.fragment.isDetached()) {
                FragmentTransaction ft = mActivity.getSupportFragmentManager().beginTransaction();
                ft.detach(info.fragment);
                ft.commit();
            }

            mTabs.put(tag, info);
            mTabHost.addTab(tabSpec);
        }

        public void onTabChanged(String tabId) {
            TabInfo newTab = mTabs.get(tabId);
            if (mLastTab != newTab) {
                FragmentTransaction ft = mActivity.getSupportFragmentManager().beginTransaction();
                if (mLastTab != null) {
                    if (mLastTab.fragment != null) {
                        ft.detach(mLastTab.fragment);
                    }
                }
                if (newTab != null) {
                    if (newTab.fragment == null) {
                        newTab.fragment = Fragment.instantiate(mActivity,
                                newTab.clss.getName(), newTab.args);
                        ft.add(mContainerId, newTab.fragment, newTab.tag);
                    } else {
                        ft.attach(newTab.fragment);
                    }
                }

                mLastTab = newTab;
                ft.commit();
                mActivity.getSupportFragmentManager().executePendingTransactions();
            }
        }
    }	
	
}
