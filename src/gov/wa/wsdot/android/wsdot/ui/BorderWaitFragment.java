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
import gov.wa.wsdot.android.wsdot.shared.BorderWaitItem;
import gov.wa.wsdot.android.wsdot.util.AnalyticsUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class BorderWaitFragment extends ListFragment {
	private static final String DEBUG_TAG = "BorderWait";
	private ArrayList<BorderWaitItem> borderWaitItems = null;
	private BorderWaitItemAdapter adapter;
	
	private HashMap<Integer, Integer> routeImage = new HashMap<Integer, Integer>();
	private View mLoadingSpinner;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AnalyticsUtils.getInstance(getActivity()).trackPageView("/Canadian Border");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_list_with_spinner, null);

        // For some reason, if we omit this, NoSaveStateFrameLayout thinks we are
        // FILL_PARENT / WRAP_CONTENT, making the progress bar stick to the top of the activity.
        root.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.FILL_PARENT));

        mLoadingSpinner = root.findViewById(R.id.loading_spinner);

        return root;
    }    
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        borderWaitItems = new ArrayList<BorderWaitItem>();
        this.adapter = new BorderWaitItemAdapter(getActivity(), R.layout.row, borderWaitItems);
        setListAdapter(this.adapter);
        
        routeImage.put(5, R.drawable.i5);
        routeImage.put(9, R.drawable.sr9);
        routeImage.put(539, R.drawable.sr539);
        routeImage.put(543, R.drawable.sr543);
        routeImage.put(97, R.drawable.us97);        
        
        new GetBorderWaitItems().execute();
    }
    
    private class GetBorderWaitItems extends AsyncTask<String, Integer, String> {

		@Override
		protected void onPreExecute() {
			mLoadingSpinner.setVisibility(View.VISIBLE);
		}
    	
	    protected void onCancelled() {
	        Toast.makeText(getActivity(), "Cancelled", Toast.LENGTH_SHORT).show();
	    }

		@Override
		protected String doInBackground(String... params) {
			try {
				URL url = new URL("http://data.wsdot.wa.gov/mobile/BorderWait.js");
				URLConnection urlConn = url.openConnection();
				BufferedReader in = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
				String jsonFile = "";
				String line;
				
				while ((line = in.readLine()) != null)
					jsonFile += line;
				in.close();
				
				JSONObject obj = new JSONObject(jsonFile);
				JSONObject result = obj.getJSONObject("waittimes");
				JSONArray items = result.getJSONArray("items");
				borderWaitItems = new ArrayList<BorderWaitItem>();
				BorderWaitItem i = null;
							
				for (int j=0; j < items.length(); j++) {
					if (!this.isCancelled()) {
						JSONObject item = items.getJSONObject(j);
						i = new BorderWaitItem();
						i.setTitle(item.getString("title"));
						i.setRoute(item.getInt("route"));
						i.setWait(item.getString("wait"));
						borderWaitItems.add(i);
						publishProgress(1);
					} else {
						break;
					}
				}			

			} catch (Exception e) {
				Log.e(DEBUG_TAG, "Error in network call", e);
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			mLoadingSpinner.setVisibility(View.GONE);
			
            if (borderWaitItems != null && borderWaitItems.size() > 0) {
                adapter.notifyDataSetChanged();
                for(int i=0;i<borderWaitItems.size();i++)
                adapter.add(borderWaitItems.get(i));
            }
            adapter.notifyDataSetChanged();
		}   
    }
    
	private class BorderWaitItemAdapter extends ArrayAdapter<BorderWaitItem> {
        private ArrayList<BorderWaitItem> items;

        public BorderWaitItemAdapter(Context context, int textViewResourceId, ArrayList<BorderWaitItem> items) {
	        super(context, textViewResourceId, items);
	        this.items = items;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
	        if (convertView == null) {
	            convertView = getActivity().getLayoutInflater().inflate(R.layout.row, null);
	        }
	        BorderWaitItem o = items.get(position);
	        if (o != null) {
	            TextView tt = (TextView) convertView.findViewById(R.id.toptext);
	            TextView bt = (TextView) convertView.findViewById(R.id.bottomtext);
	            ImageView iv = (ImageView) convertView.findViewById(R.id.icon);
	            if (tt != null) {
	            	tt.setText(o.getTitle());
	            }
	            if(bt != null) {
            		bt.setText(o.getWait());
	            }
	       		iv.setImageResource(routeImage.get(o.getRoute()));
	        }
	        return convertView;
        }
	}
}