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

package gov.wa.wsdot.android.wsdot.ui.phone;

import gov.wa.wsdot.android.wsdot.ui.BaseSinglePaneActivity;
import gov.wa.wsdot.android.wsdot.ui.RouteAlertsFragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;

public class RouteAlertsActivity extends BaseSinglePaneActivity {

	@Override
	protected Fragment onCreatePane() {
		return new RouteAlertsFragment();
	}
	
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		//getActivityHelper().setupSubActivity();
	}

}
