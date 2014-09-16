package com.edgecase.contested.adapter;


/**
 * page adapter for contest view.
 *

public class Contest_view_page_adapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 2;

    public Contest_view_page_adapter(FragmentManager fm) {
        super(fm);
    }


    @Override
    public Fragment getItem(int arg0)
    {
        Contest_view cv = new Contest_view();
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
 */