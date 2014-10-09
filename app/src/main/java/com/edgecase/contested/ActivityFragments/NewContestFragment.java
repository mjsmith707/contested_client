package com.edgecase.contested.ActivityFragments;

/**
 * Created by reubenromandy on 9/16/14.
 */

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
    private static final String TAG = NewContestFragment.class.getSimpleName();

    // Contests json url
    private static final String url = AppController.getInstance().getUrl();
    EditText opponentUName;
    EditText contestName;
    Button startContest;
    private String contestNameString;
    private String contestOpponent = null;
    private String uname;
    private String pass;
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
        contestName = (EditText) getView().findViewById(R.id.newContestName);
        opponentUName = (EditText) getView().findViewById(R.id.friend);
        startContest = (Button) getView().findViewById(R.id.createContest);
        SharedPreferences prefs = this.getActivity().getSharedPreferences(MYPREFS, getActivity().MODE_PRIVATE);
        pass = prefs.getString("Password", "not working");
        uname = prefs.getString("Username", "not working");


        startContest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contestNameString = contestName.getText().toString();
                contestOpponent = opponentUName.getText().toString();

                JSONObject params = new JSONObject();
                try {
                    params.put("password", pass);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    params.put("username", uname);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    params.put("requestid", "createcontest");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    params.put("reqparam1", contestNameString);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    params.put("reqparam2", contestType);
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

            }

        });


}
    @Override
    public void onDestroy() {
        super.onDestroy();
    }



}

