package com.edgecase.contested.ActivityFragments;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.edgecase.contested.R;

import java.io.ByteArrayOutputStream;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class ContestFragment extends Fragment {
    private TextView contestName;
    private ImageButton entry;
    private String name;
    private Bitmap image;
    private Bundle data;

    int mCurrentPage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /** Getting the arguments to the Bundle object */
        data = getArguments();

        /** Getting integer data of the key current_page from the bundle */


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.contest_view_fragment, container, false);

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mCurrentPage = data.getInt("current_page", 0);
        name = data.getString("CONTESTNAME");
        contestName = (TextView) getView().findViewById(R.id.contestName);
        entry = (ImageButton) getView().findViewById(R.id.entry);
       /* if (data.getString(("IMAGE")) != null) {
            image = setImage(data.getString("IMAGE"));
        }
        else {*/


        entry = (ImageButton) getView().findViewById(R.id.entry);
        entry.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),
                        ImagePickerActivity.class);
                Log.e("String2", "String");
                startActivityForResult(intent, 0);
            }
        });

    }

    public void setName(String name) {
        this.name = name;
    }

    public Bitmap setImage(String image) {

        byte[] decodeImage = Base64.decode(image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodeImage, 0, decodeImage.length);

    }

    public void setContent() {

        contestName.setText(name);
        entry.setImageBitmap(image);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        setContent();


                Bundle extras = data.getExtras();


                String filename = extras.getString("filename");
        Bitmap bm = BitmapFactory.decodeFile(filename);
        image = bm;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] b = baos.toByteArray();
        String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);

        setContent();
        }



    }
