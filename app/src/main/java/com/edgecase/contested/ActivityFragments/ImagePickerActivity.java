package com.edgecase.contested.ActivityFragments;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.edgecase.contested.R;
import com.edgecase.contested.library.DecodeImageResize;


public class ImagePickerActivity extends Activity {
    /** Called when the activity is first created. */

    private Cursor imagecursor;
    private Integer image_column_index;
    GridView imagegrid;
    private Integer count;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_picker);
    }

    @Override
    public void onResume() {
        super.onResume();
        init_phone_image_grid();
    }

    private void init_phone_image_grid() {
        String[] img = { MediaStore.Images.Media.DATA };
        imagecursor = managedQuery(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, img, null,
                null, null);
        image_column_index = imagecursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        count = imagecursor.getCount();
        imagegrid = (GridView) findViewById(R.id.gridview);
        imagegrid.setAdapter(new ImageAdapter(getApplicationContext()));
        imagegrid.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView parent, View v, int position,
                                    long id) {

                imagecursor.moveToPosition(position);
                String i = imagecursor
                        .getString(image_column_index);
                Log.e("imagecursorGetString", i);

                Intent mIntent = new Intent(getApplicationContext(),
                        ContestFragment.class);
                mIntent.putExtra("filename", i);
                setResult(RESULT_OK, mIntent);
                finish();
            }
        });
    }

    public class ImageAdapter extends BaseAdapter {

        private Context mContext;

        public ImageAdapter(Context c) {
            mContext = c;
        }

        @Override
        public int getCount() {
            return count;
        }

        @Override
        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            if (convertView == null) {  // if it's not recycled, initialize some attributes
                imageView = new ImageView(mContext);
                imageView.setLayoutParams(new GridView.LayoutParams(310, 310));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            } else {
                imageView = (ImageView) convertView;
            }
            imagecursor.moveToPosition(position);
            DecodeImageResize resizedImage = new DecodeImageResize();

            Bitmap bm = resizedImage.decodeSampledBitmapFromUri(imagecursor.getString(image_column_index), 310, 310);

            imageView.setImageBitmap(bm);
            return imageView;
        }



    }

    public void onPause() {
        super.onPause();

    }
}
