package com.edgecase.contested.ActivityFragments;

/**
 * Created by reubenromandy on 9/16/14.
 */

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.edgecase.contested.R;
import com.edgecase.contested.app.AppController;
import com.edgecase.contested.model.Contest;

import org.json.JSONException;
import org.json.JSONObject;

public class NewContestFragment extends Fragment {
    // Log tag
    private static final String TAG = NewContestFragment.class.getSimpleName();

    // Contests json url
    EditText opponentUName;
    EditText contestName;
    Button startContest;
    RadioGroup rGroup;
    EditText friend;
    OnCreateContest mListener;
    Context context;
    private String contestNameString = "";
    private String contestOpponent = "";
    private String uname;
    private String pass;
    private String type;
    private String contestTypeID = "0";
    private String resultFromContest;
    private Boolean privateCon = false;
    public static final String MYPREFS = "mySharedPreferences";

    public NewContestFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_new_contest, container, false);

        return view;
    }

    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        context = activity;
        if (activity instanceof OnCreateContest)
        {
            mListener = (OnCreateContest) activity;
        } else {
            throw new  ClassCastException(activity.toString());
        }
    }

    @Override
        public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final String url = AppController.getInstance().getUrl();
        rGroup = (RadioGroup) getView().findViewById(R.id.radioGroup);
        contestName = (EditText) getView().findViewById(R.id.newContestName);
        startContest = (Button) getView().findViewById(R.id.createContest);
        friend = (EditText) getView().findViewById(R.id.friend);
        opponentUName = (EditText) getView().findViewById(R.id.friend);
        startContest = (Button) getView().findViewById(R.id.createContest);
        SharedPreferences prefs = this.getActivity().getSharedPreferences(MYPREFS, getActivity().MODE_PRIVATE);
        pass = prefs.getString("Password", "not working");
        uname = prefs.getString("Username", "not working");


        rGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup rGroup, int checkedId)
            {
                Integer selectedId = rGroup.getCheckedRadioButtonId();
                RadioButton contestType = (RadioButton) getView().findViewById(selectedId);
                type = contestType.getText().toString();


                if (type.equals("Private Contest")) {
                    privateCon = true;
                    friend.setVisibility(View.VISIBLE);
                    Log.e("privateCon", privateCon.toString());
                }
                else if (type.equals("Public Contest")){
                    privateCon = false;
                    friend.setVisibility(View.INVISIBLE);
                    contestTypeID = "-1";
                    Log.e("publicCon", privateCon.toString());
                }
                else if (type.equals("Contest With Friends")){
                    privateCon = false;
                    friend.setVisibility(View.INVISIBLE);
                    contestTypeID = "0";
                    Log.e("friendCon", privateCon.toString());
                }


            }
        });





        startContest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contestNameString = contestName.getText().toString();
                contestOpponent = opponentUName.getText().toString();

                if (privateCon) {
                    contestTypeID = contestOpponent;
                }
                if (contestTypeID.isEmpty() || contestNameString.isEmpty()) {
                    Toast.makeText(getActivity().getApplicationContext(),
                            "One or more fields are empty", Toast.LENGTH_SHORT).show();
                } else {
                    JSONObject params = new JSONObject();
                    try {
                        params.put("password", pass);
                        Log.e("pass", pass);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        params.put("username", uname);
                        Log.e("uname", uname);
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
                        Log.e("contestname", contestNameString);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        params.put("reqparam2", contestTypeID);
                        Log.e("contestType", contestTypeID);
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
                                        Contest contest = new Contest(contestNameString, "", "",uname, contestOpponent, resultFromContest);
                                        mListener.onCreateNewContest(contest);

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
        }
        });


}
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public interface OnCreateContest {
        public void onCreateNewContest(Contest contest);
    }

}

