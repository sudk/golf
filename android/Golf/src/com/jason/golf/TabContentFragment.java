package com.jason.golf;

import android.support.v4.app.Fragment;

public class TabContentFragment extends Fragment {
	
	protected String mText;

    public TabContentFragment(String text) {
        mText = text;
    }

    public String getText() {
        return mText;
    }

}
