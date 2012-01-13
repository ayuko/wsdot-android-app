/*
 * Copyright 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package gov.wa.wsdot.android.wsdot.util;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

/**
 * An extension of {@link com.ActivityHelper.android.actionbarcompat.ActionBarHelper} that provides Android
 * 4.0-specific functionality for IceCreamSandwich devices. It thus requires API level 14.
 */
public class ActivityHelperICS extends ActivityHelperHoneycomb {
    protected ActivityHelperICS(Activity activity) {
        super(activity);
    }

    @Override
    protected Context getActionBarThemedContext() {
        return mActivity.getActionBar().getThemedContext();
    }
    
    /*
    public void onCreate(Bundle savedInstance) {
    	super.onCreate(savedInstance);
    	mActivity.getActionBar().setHomeButtonEnabled(true);
    }
    */
}