package com.edgecase.contested.ActivityFragments;

/**
 * Created by reubenromandy on 9/15/14.
 */

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import com.edgecase.contested.R;
import com.edgecase.contested.adapter.TabsPagerAdapter;
import com.edgecase.contested.model.Contest;

import java.io.FileOutputStream;

public class MainActivity extends FragmentActivity implements
        ActionBar.TabListener, ContestListFragment.OnDetailView, NewContestFragment.OnCreateContest{

    private ViewPager viewPager;
    private TabsPagerAdapter mAdapter;
    private ActionBar actionBar;
    // Tab titles
    private String[] tabs = { "My Contests", "Friends", "New Contest", "Top Contests" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initilization
        viewPager = (ViewPager) findViewById(R.id.pager);
        actionBar = getActionBar();
        mAdapter = new TabsPagerAdapter(getSupportFragmentManager());

        viewPager.setAdapter(mAdapter);
        actionBar.setHomeButtonEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Adding Tabs
        for (String tab_name : tabs) {
            actionBar.addTab(actionBar.newTab().setText(tab_name)
                    .setTabListener(this));
        }

        /**
         * on swiping the viewpager make respective tab selected
         * */
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                // on changing the page
                // make respected tab selected
                actionBar.setSelectedNavigationItem(position);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }

    @Override
    public void onTabReselected(Tab tab, FragmentTransaction ft) {
    }

    @Override
    public void onTabSelected(Tab tab, FragmentTransaction ft) {
        // on tab selected
        // show respected fragment view
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(Tab tab, FragmentTransaction ft) {
    }


    public void onDetailViewUpdate( Contest contest){
         Intent intent = new Intent(this, ContestViewPagerContainer.class);


        String image1FileName = "image1FileName";
        String image2FileName = "image2FileName";
        String imageOneString = contest.getThumbnail();
        String imageTwoString = contest.getThumbnailTwo();
        FileOutputStream outputStream;

        try {
            outputStream = openFileOutput(image1FileName, Context.MODE_PRIVATE);
            outputStream.write(imageOneString.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            outputStream = openFileOutput(image2FileName, Context.MODE_PRIVATE);
            outputStream.write(imageTwoString.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }



         intent.putExtra("CONTESTID", contest.getContestID());
         intent.putExtra("IMAGE1File", image1FileName);
         intent.putExtra("IMAGE2File", image2FileName);
         intent.putExtra("CONTESTNAME", contest.getContestName());
         startActivity(intent);
    }

    public void onCreateNewContest( Contest contest){
        Intent intent = new Intent(this, ContestViewPagerContainer.class);
        intent.putExtra("CONTESTID", contest.getContestID());
        intent.putExtra("IMAGE1", contest.getThumbnail());
        intent.putExtra("IMAGE2", contest.getThumbnailTwo());
        intent.putExtra("CONTESTNAME", contest.getContestName());
        startActivity(intent);
    }

}
