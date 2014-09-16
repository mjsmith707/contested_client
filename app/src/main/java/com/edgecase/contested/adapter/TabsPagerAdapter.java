package com.edgecase.contested.adapter;

/**
 * Created by reubenromandy on 9/16/14.
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.edgecase.contested.ActivityFragments.ContestListFragment;
import com.edgecase.contested.ActivityFragments.FriendsFragment;
import com.edgecase.contested.ActivityFragments.NewContestFragment;
import com.edgecase.contested.ActivityFragments.TopContestsFragment;

public class TabsPagerAdapter extends FragmentPagerAdapter {

    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int index) {

        switch (index) {
            case 0:
                // My Contests fragment activity
                return new ContestListFragment();
            case 1:
                // Friends fragment activity
                return new FriendsFragment();
            case 2:
                // NewContest fragment activity
                return new NewContestFragment();
            case 3:
                // Top Contests fragment activity
                return new TopContestsFragment();
        }

        return null;
    }

    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 4;
    }

}