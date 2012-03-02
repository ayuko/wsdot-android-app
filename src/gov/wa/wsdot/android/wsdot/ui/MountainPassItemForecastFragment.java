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
import gov.wa.wsdot.android.wsdot.shared.ForecastItem;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class MountainPassItemForecastFragment extends ListFragment {
	private ArrayList<ForecastItem> forecastItems;
	private MountainPassItemForecastAdapter adapter;

	@SuppressWarnings("unchecked")
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		forecastItems = (ArrayList<ForecastItem>)activity.getIntent().getSerializableExtra("Forecasts");
	}	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_list_with_spinner, null);

        // For some reason, if we omit this, NoSaveStateFrameLayout thinks we are
        // FILL_PARENT / WRAP_CONTENT, making the progress bar stick to the top of the activity.
        root.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.FILL_PARENT));
    	
    	return root;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
        this.adapter = new MountainPassItemForecastAdapter(getActivity(), R.layout.simple_list_item, forecastItems);
        setListAdapter(this.adapter);
	}

	private class MountainPassItemForecastAdapter extends ArrayAdapter<ForecastItem> {
        private ArrayList<ForecastItem> items;

        public MountainPassItemForecastAdapter(Context context, int textViewResourceId, ArrayList<ForecastItem> items) {
                super(context, textViewResourceId, items);
                this.items = items;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
	        if (convertView == null) {
	            convertView = getActivity().getLayoutInflater().inflate(R.layout.simple_list_item, null);
	        }
	        ForecastItem o = items.get(position);
	        if (o != null) {
	            TextView tt = (TextView) convertView.findViewById(R.id.title);
	            TextView bt = (TextView) convertView.findViewById(R.id.description);
	            if (tt != null) {
	            	tt.setText(o.getDay());
	            }
	            if(bt != null) {
            		bt.setText(o.getForecastText());
	            }
	        }
	        return convertView;
        }
	}
}
