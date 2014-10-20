package com.edgecase.contested.adapter;

/**
 * Created by msmith on 10/17/14
 */

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.edgecase.contested.R;
import com.edgecase.contested.app.AppController;
import com.edgecase.contested.model.Friend;

import java.util.List;
public class FriendsListAdapter extends BaseAdapter {
    private Activity activity = null;
    private LayoutInflater inflater = null;
    private List<Friend> friendsItems = null;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public FriendsListAdapter(Activity activity, List<Friend> friendsItems) {
        this.activity = activity;
        this.friendsItems = friendsItems;
    }

    @Override
    public int getCount() {
        return friendsItems.size();
    }

    @Override
    public Object getItem(int location) {
        return friendsItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null) {
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.friends_list_row_layout, null);
        }

        if (imageLoader == null) {
            imageLoader = AppController.getInstance().getImageLoader();
        }

        ImageView friendImage = (ImageView) convertView.findViewById(R.id.friendImageView);
        TextView friendName = (TextView) convertView.findViewById(R.id.friendTextView);

        // getting friend data for the row
        Friend f = friendsItems.get(position);

        // thumbnail image
        byte[] decodedString = new byte[0];
        try {
            decodedString = Base64.decode(f.getFriendImage(), Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Bitmap imageFromDecodedByte = null;
        try {
            if (decodedString != null) {
                imageFromDecodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                //friendImage.setImageBitmap(imageFromDecodedByte);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        // Friend name
        friendName.setText(f.getFriendName());

        return convertView;
    }

}