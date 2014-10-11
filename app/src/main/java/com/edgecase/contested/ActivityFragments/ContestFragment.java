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

import com.edgecase.contested.R;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class ContestFragment extends Fragment {;
    private ImageButton entry;
    private Bitmap image;


    int mCurrentPage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /** Getting the arguments to the Bundle object */
        Bundle data = getArguments();

        /** Getting integer data of the key current_page from the bundle */
        mCurrentPage = data.getInt("current_page", 0);
        image = setImage(data.getString("IMAGE"));


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.contest_view_fragment, container,false);
        entry = (ImageButton) v.findViewById(R.id.entry);
        setContent();
        return v;
    }

    public Bitmap setImage (String image){

        byte[] decodeImage = Base64.decode(image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodeImage, 0, decodeImage.length);

    }

    public void setContent (){

        entry.setImageBitmap(image);
    }
}