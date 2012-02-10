/*
 * Copyright (C) 2006 The Android Open Source Project
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

package gov.wa.wsdot.android.wsdot.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;

/**
 * The model for a sub menu, which is an extension of the menu.  Most methods are proxied to
 * the parent menu.
 */
public class SimpleSubMenu extends SimpleMenu implements SubMenu {
    private SimpleMenu mParentMenu;
    private SimpleMenuItem mItem;
    
    public SimpleSubMenu(Context context, SimpleMenu parentMenu, SimpleMenuItem item) {
        super(context);

        mParentMenu = parentMenu;
        mItem = item;
    }

    @Override
    public void setQwertyMode(boolean isQwerty) {
        mParentMenu.setQwertyMode(isQwerty);
    }

    public Menu getParentMenu() {
        return mParentMenu;
    }

    public MenuItem getItem() {
        return mItem;
    }

    public SubMenu setIcon(Drawable icon) {
        mItem.setIcon(icon);
        return this;
    }

    public SubMenu setIcon(int iconRes) {
        mItem.setIcon(iconRes);
        return this;
    }

	public void clearHeader() {
		// TODO Auto-generated method stub
		
	}

	public SubMenu setHeaderIcon(int iconRes) {
		// TODO Auto-generated method stub
		return null;
	}

	public SubMenu setHeaderIcon(Drawable icon) {
		// TODO Auto-generated method stub
		return null;
	}

	public SubMenu setHeaderTitle(int titleRes) {
		// TODO Auto-generated method stub
		return null;
	}

	public SubMenu setHeaderTitle(CharSequence title) {
		// TODO Auto-generated method stub
		return null;
	}

	public SubMenu setHeaderView(View view) {
		// TODO Auto-generated method stub
		return null;
	}
    
}
