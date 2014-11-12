package com.edgecase.contested.ActivityFragments;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.edgecase.contested.R;
import com.edgecase.contested.app.AppController;
import com.edgecase.contested.library.DecodeImageResize;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class ContestFragment extends Fragment {;
    private static final String TAG = ContestFragment.class.getSimpleName();
    private ImageButton entry;
    private Bitmap image;
    private Bitmap thumbnail;
    Context context;
    private String encodedString = "";
    private String uname;
    private String pass;
    private String contestID;
    private String result;
    private FileInputStream fis;
    private String url = AppController.getInstance().getUrl();
    public static final String MYPREFS = "mySharedPreferences";


    Integer mCurrentPage;
    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        context = activity;

    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /** Getting the arguments to the Bundle object */
        Bundle data = getArguments();
        String imageFileName = data.getString("IMAGE");
    if(imageFileName != null) {
        BufferedReader bufferedReader = null;
        try {
            if (imageFileName.equals("image1FileName")) {
                bufferedReader = new BufferedReader(new FileReader(new
                        File(context.getFilesDir() + File.separator + "image1FileName")));
            } else {
                bufferedReader = new BufferedReader(new FileReader(new
                        File(context.getFilesDir() + File.separator + "image2FileName")));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        StringBuilder builder = new StringBuilder("");

        if (bufferedReader != null) {
            try {
                while ((encodedString = bufferedReader.readLine()) != null) {
                    builder.append(encodedString);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        encodedString = builder.toString();
        Log.e("Output", builder.toString());
        if (bufferedReader != null) {
            try {
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
        /** Getting integer data of the key current_page from the bundle */
        mCurrentPage = data.getInt("current_page", 0);
        contestID = data.getString("CONTESTID");
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.contest_view_fragment, container,false);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle saved){
        super.onActivityCreated(saved);
        entry = (ImageButton) getView().findViewById(R.id.entry);
        if(encodedString.length() < 25) {
            entry.setImageResource(R.drawable.add_photo_placeholder);
            entry.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(),
                            ImagePickerActivity.class);
                    startActivityForResult(intent, 0);
                }
            });
        }
        else {
            image = setImage(encodedString);
            setContent();
            entry.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    castVote(contestID, mCurrentPage);
                }
            });
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_CANCELED ) {
        }
        else{
            Bundle extras = data.getExtras();
            //doing main image
            String filename = extras.getString("filename");
            try {
                DecodeImageResize resizedImage = new DecodeImageResize();

                image = resizedImage.decodeSampledBitmapFromUri(filename, 500, 500);
            } catch (Exception e) {
                e.printStackTrace();
            }
            byte [] bytes;
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.PNG, 80, output);
            bytes = output.toByteArray();
            String encodedString = null;
            try {
                encodedString = Base64.encodeToString(bytes, Base64.DEFAULT);
            } catch (Exception e) {
                e.printStackTrace();
            }
            setContent();
            //thumbnail
            try {
                DecodeImageResize resizedImage = new DecodeImageResize();

                thumbnail = resizedImage.decodeSampledBitmapFromUri(filename, 100, 100);
            } catch (Exception e) {
                e.printStackTrace();
            }
            ByteArrayOutputStream outputThumbnail = new ByteArrayOutputStream();
            thumbnail.compress(Bitmap.CompressFormat.PNG, 80, outputThumbnail);
            bytes = outputThumbnail.toByteArray();
            String encodedStringThumbnail = null;
            try {
                encodedStringThumbnail = Base64.encodeToString(bytes, Base64.DEFAULT);
            } catch (Exception e) {
                e.printStackTrace();
            }
            SharedPreferences prefs = this.getActivity().getSharedPreferences(MYPREFS, getActivity().MODE_PRIVATE);
            pass = prefs.getString("Password", "not working");
            uname = prefs.getString("Username", "not working");


            JSONObject params = new JSONObject();
            try {
                Log.e("uname", uname);
                params.put("username", uname);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                Log.e("pass", pass);
                params.put("password", pass);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                params.put("requestid", "updateimage");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                Log.e("contestID", contestID);
                params.put("reqparam1", contestID);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                params.put("reqparam2", encodedString);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                Log.e("page", mCurrentPage.toString());
                params.put("reqparam3", mCurrentPage.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                params.put("reqparam4", encodedStringThumbnail);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            longInfo(params.toString());
         /*   JSON Format:
            {"username":"msmith","password":"hello","requestid":"updateimage","reqparam1":"int contestid",
                    "reqparam2":"base64image","reqparam3":"slot (1 or 2, left or right)"}*/

            JsonObjectRequest updateImageReq = new JsonObjectRequest(url, params,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d(TAG, response.toString());


                            try {


                                result = response.getString("RESULT");
                                Log.e("result", result);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d(TAG, "Error: " + error.getMessage());
                    Log.e("error", "ohno");
                }
            });

            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(updateImageReq);
        }
    }

    public static void longInfo(String str) {
        if(str.length() > 4000) {
            Log.i(TAG, str.substring(0, 4000));
            longInfo(str.substring(4000));
        } else
            Log.i(TAG, str);
    }

    public Bitmap setImage(String image) {
        byte[] decodeImage = new byte[0];
        try {
            decodeImage = Base64.decode(image, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Bitmap bm = null;
        try {
            bm = BitmapFactory.decodeByteArray(decodeImage, 0, decodeImage.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bm;
    }



    public void setContent (){
        entry.setScaleType(ImageView.ScaleType.FIT_CENTER);
        entry.setImageBitmap(image);
    }

    private void castVote(String contestID, int page) {
        SharedPreferences prefs = this.getActivity().getSharedPreferences(MYPREFS, getActivity().MODE_PRIVATE);
        pass = prefs.getString("Password", "not working");
        uname = prefs.getString("Username", "not working");


        JSONObject params = new JSONObject();
        try {
            Log.e("uname", uname);
            params.put("username", uname);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            Log.e("pass", pass);
            params.put("password", pass);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            params.put("requestid", "vote");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            Log.e("contestID", contestID);
            params.put("reqparam1", contestID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            Log.e("entrant", Integer.toString(page));
            params.put("reqparam2", Integer.toString(page));
        } catch (JSONException e) {
            e.printStackTrace();
        }


         /*   {"username":"msmith","password":"hello","requestid":"vote","reqparam1":"contestid","reqparam2":"slot"} */

        JsonObjectRequest updateImageReq = new JsonObjectRequest(url, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());


                        try {


                            result = response.getString("RESULT");
                            Log.e("result", result);
                            Context context = getActivity();
                            int duration = Toast.LENGTH_SHORT;
                            //TODO: at some point this should return the score
                            CharSequence text;
                            if (result.equals("1000")) {
                                text = "Vote Cast!";
                            }else {
                                text = "Unable to cast vote!";
                            }
                            Toast toast = Toast.makeText(context, text, duration);
                            toast.show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Log.e("error","Error: " + error.getMessage() );
            }
        });
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(updateImageReq);

    }
}