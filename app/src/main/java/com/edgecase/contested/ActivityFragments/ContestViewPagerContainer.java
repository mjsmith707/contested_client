package com.edgecase.contested.ActivityFragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;
import android.widget.TextView;

import com.edgecase.contested.R;
import com.edgecase.contested.adapter.Contest_view_page_adapter;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class ContestViewPagerContainer extends FragmentActivity{
    Contest_view_page_adapter pagerAdapter;
    private String contestID;
    private String image1;
    private String image2;
    private String contestName;
    private TextView contestname ;
    public ContestViewPagerContainer() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        contestID = intent.getStringExtra("CONTESTID");
        image1 = intent.getStringExtra("IMAGE1File");
        image2 = intent.getStringExtra("IMAGE2File");
        contestName = intent.getStringExtra("CONTESTNAME");
        setContentView(R.layout.activity_contest_view);
        contestname = (TextView) findViewById(R.id.contestName);
        contestname.setText(contestName);
        ViewPager pager = (ViewPager) findViewById(R.id.contestPager);
        FragmentManager fm = getSupportFragmentManager();
        pagerAdapter =  new Contest_view_page_adapter(fm);
        pagerAdapter.setContestID(contestID);
        pagerAdapter.setImage1(image1);
        pagerAdapter.setImage2(image2);
        pager.setAdapter(pagerAdapter);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // This is called when the Home (Up) button is pressed in the action bar.
                Intent upIntent = new Intent(this, MainActivity.class);
                upIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                upIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(upIntent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
