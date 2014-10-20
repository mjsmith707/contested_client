package com.edgecase.contested.adapter;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.edgecase.contested.ActivityFragments.ContestFragment;
import com.edgecase.contested.model.Contest;

/**
 * page adapter for contest view.
 */

public class Contest_view_page_adapter extends FragmentPagerAdapter {
    static final int PAGE_COUNT = 2;

    private String name;
    private String image1;
    private String image2;
    private String contestID;
    private ContestFragment cv;
    private Contest contest = new Contest();

    public Contest_view_page_adapter(FragmentManager fm) {
        super(fm);
    }


    @Override
    public Fragment getItem(int arg0)
    {
        cv = new ContestFragment();
        Bundle data = new Bundle();
        data.putInt("current_page", arg0+1);
        data.putString("CONTESTNAME", name);
        data.putString("CONTESTID", contestID);
        if (arg0 == 0) {
            data.putString("IMAGE", image1);
        }else{
            data.putString("IMAGE", image2);
        }

        cv.setArguments(data);
        return cv;
    }



    @Override
    public int getCount()
    {
        return PAGE_COUNT;
    }

    public void setContestID (String ID){
        contestID = ID;
    }

    public void setContestName (String n) {
        name = n;
    }

    public void setImage1 (String image) {
        image1 = image;
    }

    public void setImage2 (String image) {
        image2 = image;
    }
}