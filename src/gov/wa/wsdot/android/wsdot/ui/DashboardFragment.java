/*
 * Copyright 2011 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package gov.wa.wsdot.android.wsdot.ui;

import gov.wa.wsdot.android.wsdot.R;
import gov.wa.wsdot.android.wsdot.ui.phone.BorderWaitActivity;
import gov.wa.wsdot.android.wsdot.ui.phone.FerriesActivity;
import gov.wa.wsdot.android.wsdot.ui.phone.MountainPassActivity;
import gov.wa.wsdot.android.wsdot.ui.phone.SocialMediaActivity;
import gov.wa.wsdot.android.wsdot.ui.phone.TollRatesActivity;
import gov.wa.wsdot.android.wsdot.ui.tablet.MountainPassMultiPaneActivity;
import gov.wa.wsdot.android.wsdot.util.UIUtils;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class DashboardFragment extends Fragment {

	/*
    public void fireTrackerEvent(String label) {
        AnalyticsUtils.getInstance(getActivity()).trackEvent(
                "Home Screen Dashboard", "Click", label, 0);
    }
    */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dashboard, container);

        // Attach event handlers
        root.findViewById(R.id.home_btn_traffic).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	startActivity(new Intent(getActivity(), TrafficMapActivity.class));
            }            
        });

        root.findViewById(R.id.home_btn_ferries).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	startActivity(new Intent(getActivity(), FerriesActivity.class));
            }
        });

        root.findViewById(R.id.home_btn_passes).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	if (UIUtils.isHoneycombTablet(getActivity())) {
            		startActivity(new Intent(getActivity(), MountainPassMultiPaneActivity.class));
            	}
            	startActivity(new Intent(getActivity(), MountainPassActivity.class));
            }
        });
        
        root.findViewById(R.id.home_btn_social).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	startActivity(new Intent(getActivity(), SocialMediaActivity.class));
            }
        });
        
        root.findViewById(R.id.home_btn_tolling).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	startActivity(new Intent(getActivity(), TollRatesActivity.class));
            }
        });        

        root.findViewById(R.id.home_btn_border).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), BorderWaitActivity.class));                
            }
        });
        
        return root;

    }

}
