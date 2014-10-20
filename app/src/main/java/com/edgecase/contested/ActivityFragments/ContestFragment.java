package com.edgecase.contested.ActivityFragments;


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
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.edgecase.contested.R;
import com.edgecase.contested.app.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class ContestFragment extends Fragment {;
    private static final String TAG = ContestFragment.class.getSimpleName();
    private ImageButton entry;
    private Bitmap image;
    private String encodedString;
    private String uname;
    private String pass;
    private String contestID;
    private String result;
    private String url = AppController.getInstance().getUrl();
    public static final String MYPREFS = "mySharedPreferences";


    Integer mCurrentPage;





    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /** Getting the arguments to the Bundle object */
        Bundle data = getArguments();

        /** Getting integer data of the key current_page from the bundle */
        mCurrentPage = data.getInt("current_page", 0);
        encodedString = data.getString("IMAGE");
        contestID = data.getString("CONTESTID");
        longInfo(encodedString);
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
        String filename = extras.getString("filename");
            try {
                image = BitmapFactory.decodeFile(filename);
            } catch (Exception e) {
                e.printStackTrace();
            }
            InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(filename);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        byte[] bytes;
        byte[] buffer = new byte[8192];
        int bytesRead;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        bytes = output.toByteArray();
        String encodedString = Base64.encodeToString(bytes, Base64.DEFAULT);
            setContent();
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
<<<<<<< HEAD
                Log.e("image", encodedString);
=======
                Log.e("image", "4");
>>>>>>> d2c5aa03e85d6646b8a73d7819efb427ee5b8136
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