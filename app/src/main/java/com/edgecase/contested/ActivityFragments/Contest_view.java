package com.edgecase.contested.ActivityFragments;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

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
    private Bitmap entry;

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

    public void setEntry(Bitmap imageBitmap)
    {
        this.entry = imageBitmap;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.contest_view_fragment, container, false);
        TextView name = (TextView) v.findViewById(R.id.contestName);
        name.setText("contest name, entry" + mCurrentPage );
        ImageButton thisEntry = (ImageButton) v.findViewById(R.id.entry);
        thisEntry.setImageBitmap(entry);
        thisEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Vote cast!", Toast.LENGTH_SHORT).show();
            }
        });
        return v;
    }


}
