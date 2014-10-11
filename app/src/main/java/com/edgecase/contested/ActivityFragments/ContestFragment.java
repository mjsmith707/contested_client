package com.edgecase.contested.ActivityFragments;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.edgecase.contested.R;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class ContestFragment extends Fragment {
    private TextView contestName ;
    private ImageButton entry;
    private String name;
    private Bitmap image;


    int mCurrentPage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /** Getting the arguments to the Bundle object */
        Bundle data = getArguments();

        /** Getting integer data of the key current_page from the bundle */
        mCurrentPage = data.getInt("current_page", 0);
        name = data.getString("CONTESTNAME");
        image = setImage(data.getString("IMAGE"));


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.contest_view_fragment, container,false);
        contestName = (TextView) v.findViewById(R.id.contestName);
        entry = (ImageButton) v.findViewById(R.id.entry);
        setContent();
        return v;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Bitmap setImage (String image){

        byte[] decodeImage = Base64.decode(image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodeImage, 0, decodeImage.length);

    }

    public void setContent (){

        contestName.setText(name);
        entry.setImageBitmap(image);
    }
}