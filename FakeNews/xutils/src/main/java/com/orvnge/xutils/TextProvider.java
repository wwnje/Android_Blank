package com.orvnge.xutils;

import android.support.v4.app.Fragment;

/**
 * Created by wwnje on 2017/2/27.
 */

public interface TextProvider {
    public String getTextForPosition(int position);
    public int getCount();
    public Fragment getTrag(int position);
}