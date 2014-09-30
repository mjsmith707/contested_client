package com.edgecase.contested.ActivityFragments;

/**
 * Created by reubenromandy on 9/16/14.
 */

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.edgecase.contested.R;
import com.edgecase.contested.app.AppController;

import org.json.JSONException;
import org.json.JSONObject;

public class NewContestFragment extends Fragment {
    // Log tag
    private static final String TAG = ContestListFragment.class.getSimpleName();

    // Contests json url
    private static final String url = "http://24.130.89.93:1234/";
    private ProgressDialog pDialog;
    EditText opponentUName;
    EditText contestName;
    Button getPhoto;
    Button startContest;
    private String contestNameString;
    private String uname;
    private String pass;
    private String base64Photo = "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";
    private String resultFromContest;
    public static final String MYPREFS = "mySharedPreferences";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_new_contest, container, false);

        return view;
    }

    @Override
        public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    // Creating volley request obj
        opponentUName = (EditText) getView().findViewById(R.id.opponentUserName);
        contestName = (EditText) getView().findViewById(R.id.newContestName);
        getPhoto = (Button) getView().findViewById(R.id.choosePhotoButton);
        startContest = (Button) getView().findViewById(R.id.createContest);
        SharedPreferences prefs = this.getActivity().getSharedPreferences(MYPREFS, getActivity().MODE_PRIVATE);
        pass = prefs.getString("Password", "not working");
        uname = prefs.getString("Username", "not working");


        getPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }

        });
        startContest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contestNameString = contestName.getText().toString();

                final JSONObject params = new JSONObject();
                try {
                    params.put("password", pass);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    params.put("requestid", "createcontest");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    params.put("username", uname);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    params.put("reqparam1", contestNameString);
                } catch (JSONException e) {
                    e.printStackTrace();
                }





                JsonObjectRequest contestCreateReq = new JsonObjectRequest(url, params,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.d(TAG, response.toString());


                                try {


                                    resultFromContest = response.getString("CONTESTKEY");


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(TAG, "Error: " + error.getMessage());
                    }
                });
                // Adding request to request queue
                AppController.getInstance().addToRequestQueue(contestCreateReq);






                final JSONObject paramsPhoto = new JSONObject();
                try {
                    paramsPhoto.put("password", pass);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    paramsPhoto.put("requestid", "updateimage");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    paramsPhoto.put("username", uname);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    paramsPhoto.put("reqparam1", resultFromContest);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    paramsPhoto.put("reqparam2", base64Photo);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    paramsPhoto.put("reqparam1", "1");
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                JsonObjectRequest addPhoto = new JsonObjectRequest(url, paramsPhoto,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.d(TAG, response.toString());


                                try {
                                    JSONObject result;
                                    result = response.getJSONObject("");
                                    String resultFromContest = result.getString("RESULT");
                                    if(resultFromContest.isEmpty());



                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(TAG, "Error: " + error.getMessage());
                    }
                });




                AppController.getInstance().addToRequestQueue(addPhoto);
            }

        });


}
    @Override
    public void onDestroy() {
        super.onDestroy();
    }



}

