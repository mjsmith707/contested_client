package com.edgecase.contested;



import android.app.FragmentManager;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * page adapter for contest view.
 *
 */
public class contest_view_page_adapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 2;

    public contest_view_page_adapter(FragmentManager fm) {
        super(fm);
    }


    @Override
    public Fragment getItem(int arg0)
    {
        contest_view cv = new contest_view();
        Bundle data = new Bundle();
        data.putInt("current_page", arg0+1);
        cv.setArguments(data);
        return cv;
    }
    @Override
    public int getCount()
    {
        return PAGE_COUNT;
    }


}
