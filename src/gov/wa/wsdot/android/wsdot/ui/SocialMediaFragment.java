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

import gov.wa.wsdot.android.wsdot.Blog;
import gov.wa.wsdot.android.wsdot.News;
import gov.wa.wsdot.android.wsdot.Photos;
import gov.wa.wsdot.android.wsdot.Twitter;
import gov.wa.wsdot.android.wsdot.Video;
import gov.wa.wsdot.android.wsdot.util.AnalyticsUtils;
import android.os.Bundle;

public class SocialMediaFragment extends MainMenuFragment {

	@Override
	public void prepareMenu() {
		addMenuItem("News", News.class);
		addMenuItem("Twitter", Twitter.class);
		addMenuItem("Photos", Photos.class);
		addMenuItem("Blog", Blog.class);
		addMenuItem("Video", Video.class);
	}

	@Override
	public void analyticsTracker() {
		AnalyticsUtils.getInstance(getActivity()).trackPageView("/News & Social Media");		
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
}
