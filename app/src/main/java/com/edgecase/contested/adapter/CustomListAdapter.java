package com.edgecase.contested.adapter;

/**
 * Created by reubenromandy on 9/2/14.
 */


import com.edgecase.contested.R;
import com.edgecase.contested.app.AppController;
import com.edgecase.contested.model.Contest;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
public class CustomListAdapter extends BaseAdapter {
    private Activity activity = null;
    private LayoutInflater inflater = null;
    private List<Contest> contestItems = null;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();


    public CustomListAdapter(Activity activity, List<Contest> contestItems) {
        this.activity = activity;
        this.contestItems = contestItems;
    }

    @Override
    public int getCount() {
        return contestItems.size();
    }

    @Override
    public Object getItem(int location) {
        return contestItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.contest_list_row_layout, null);

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();
        NetworkImageView thumbNail = (NetworkImageView) convertView
                .findViewById(R.id.thumbUserOne);
        NetworkImageView thumbNailTwo = (NetworkImageView) convertView
                .findViewById(R.id.thumbUserTwo);
        TextView contestName = (TextView) convertView.findViewById(R.id.contestName);
        TextView userOne = (TextView) convertView.findViewById(R.id.userOne);
        TextView userTwo = (TextView) convertView.findViewById(R.id.userTwo);

        // getting contest data for the row
        Contest c = contestItems.get(position);

        // thumbnail image
        thumbNail.setImageUrl(c.getThumbnailUrl(), imageLoader);

        //thumbnail image two
        thumbNailTwo.setImageUrl(c.getThumbnailUrlTwo(), imageLoader);

        // contest name
        contestName.setText(c.getContestName());

        // userOne name
        userOne.setText(c.getUserOne());

        // contest name
        userTwo.setText(c.getUserTwo());


        return convertView;
    }

}

