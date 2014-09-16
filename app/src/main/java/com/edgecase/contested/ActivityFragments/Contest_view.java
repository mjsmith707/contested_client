package com.edgecase.contested.ActivityFragments;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.edgecase.contested.R;


/**
 * Created by Grant Keller
 * Team Edge Case
 * App Contested
 * Fragment used to display user contests, which were selected via list page.
 * Image button allows voting on selected image, and the images can be switched
 * by swiping.
 *
 */
public class Contest_view extends Fragment {
    int mCurrentPage;

    public Contest_view() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Bundle data = getArguments();

        mCurrentPage = data.getInt("current_page", 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.contest_view_fragment, container, false);
        TextView name = (TextView) v.findViewById(R.id.contestName);
        name.setText("contest name, entry" + mCurrentPage );
        return v;
    }


}
