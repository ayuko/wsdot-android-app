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
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A fragment used in {@link HomeActivity} that shows either a countdown, 'now playing' link to
 * current sessions, or 'thank you' text, at different times (before/during/after the conference).
 * It also shows a 'Realtime Search' button on phones, as a replacement for the
 * {@link TagStreamFragment} that is visible on tablets on the home screen.
 */
public class WhatsHappeningFragment extends Fragment {

    private ViewGroup mRootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        mRootView = (ViewGroup) inflater.inflate(R.layout.fragment_whats_happening, container);
        refresh();
        return mRootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void refresh() {
        mRootView.removeAllViews();

        setupDefault();

        /*
        if (!UIUtils.isHoneycombTablet(getActivity())) {
            View separator = new View(getActivity());
            separator.setLayoutParams(
                    new ViewGroup.LayoutParams(1, ViewGroup.LayoutParams.FILL_PARENT));
            separator.setBackgroundResource(R.drawable.whats_on_separator);
            mRootView.addView(separator);

            View view = getActivity().getLayoutInflater().inflate(
                    R.layout.whats_on_stream, mRootView, false);
            view.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    AnalyticsUtils.getInstance(getActivity()).trackEvent(
                            "Home Screen Dashboard", "Click", "Realtime Stream", 0);
                    Intent intent = new Intent(getActivity(), TagStreamActivity.class);
                    startActivity(intent);
                }
            });
            mRootView.addView(view);
        }
        */
    }

    private void setupDefault() {
        getActivity().getLayoutInflater().inflate(
                R.layout.whats_happening_default, mRootView, true);
    }

}
