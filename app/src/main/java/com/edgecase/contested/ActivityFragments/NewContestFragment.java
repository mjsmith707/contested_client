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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.edgecase.contested.R;
import com.edgecase.contested.app.AppController;
import com.edgecase.contested.model.Contest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NewContestFragment extends Fragment {
    // Log tag
    private static final String TAG = NewContestFragment.class.getSimpleName();

    // Contests json url
    EditText contestName;
    Button startContest;
    RadioGroup rGroup;
    RadioButton rButton;
    Spinner friend;
    OnCreateContest mListener;
    Context context;
    List<String> friendsList;
    private String contestNameString = "";
    private String contestOpponent = "";
    private String url;
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
        url = AppController.getInstance().getUrl();
        rGroup = (RadioGroup) getView().findViewById(R.id.radioGroup);
        rButton = (RadioButton) getView().findViewById(R.id.radioButtonFriends);
        contestName = (EditText) getView().findViewById(R.id.newContestName);
        startContest = (Button) getView().findViewById(R.id.createContest);
        friend = (Spinner) getView().findViewById(R.id.friend);
        startContest = (Button) getView().findViewById(R.id.createContest);
        SharedPreferences prefs = this.getActivity().getSharedPreferences(MYPREFS, getActivity().MODE_PRIVATE);
        pass = prefs.getString("Password", "not working");
        uname = prefs.getString("Username", "not working");
        friendsList = new ArrayList<String>();
        updateFriendsList();
        friendsList.add(0,"Pick an opponent");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this.getActivity(),
                android.R.layout.simple_spinner_item, friendsList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        friend.setAdapter(dataAdapter);


        friend.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });


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
                contestOpponent = friend.getSelectedItem().toString();
                Log.e("friend", contestOpponent);

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

                    contestName.setText("");
                    friend.setSelection(0);
                    rButton.setChecked(true);
            }
        }
        });

}

    private void updateFriendsList() {
        // Creating volley request obj
        friendsList.clear();

        JSONObject params2 = new JSONObject();
        try {
            params2.put("password", pass);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            params2.put("requestid", "getmyfriends");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            params2.put("username", uname);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest friendsListReq = new JsonObjectRequest(url, params2,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());

                        try {
                            JSONArray result = new JSONArray();
                            result = response.getJSONArray("friends");

                            // Parsing json
                            for (int i = 0; i < result.length(); i++) {
                                JSONObject obj = result.getJSONObject(i);
                                String friend = (obj.getString("name"));
                                friendsList.add(friend);
                            }
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

        AppController.getInstance().addToRequestQueue(friendsListReq);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public interface OnCreateContest {
        public void onCreateNewContest(Contest contest);
    }

}

