package com.edgecase.contested.ActivityFragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;

import com.edgecase.contested.R;
import com.edgecase.contested.adapter.Contest_view_page_adapter;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class ContestViewPagerContainer extends FragmentActivity{
    private static final String TAG = ContestViewPagerContainer.class.getSimpleName();
    public static final String MYPREFS = "mySharedPreferences";
    Contest_view_page_adapter pagerAdapter;
    private String contestID;
    private String image1;
    private String image2;
    private String contestName;
    public ContestViewPagerContainer() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        contestID = intent.getStringExtra("CONTESTID");
        image1 = intent.getStringExtra("IMAGE1");
        image2 = intent.getStringExtra("IMAGE2");
        contestName = intent.getStringExtra("CONTESTNAME");
        setContentView(R.layout.activity_contest_view);
        ViewPager pager = (ViewPager) findViewById(R.id.contestPager);
        FragmentManager fm = getSupportFragmentManager();
        pagerAdapter =  new Contest_view_page_adapter(fm);
        pagerAdapter.setContestID(contestID);
        pagerAdapter.setContestName(contestName);
        pagerAdapter.setImage1(image1);
        pagerAdapter.setImage2(image2);
        pager.setAdapter(pagerAdapter);

    }


}
