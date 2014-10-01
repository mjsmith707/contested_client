package com.edgecase.contested.ActivityFragments;

/**
 * Created by reubenromandy on 9/3/14.
 */

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.edgecase.contested.R;
import com.edgecase.contested.adapter.CustomListAdapter;
import com.edgecase.contested.app.AppController;
import com.edgecase.contested.model.Contest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class ContestListFragment extends Fragment {
    // Log tag
    private static final String TAG = ContestListFragment.class.getSimpleName();

    // Contests json url
    private static final String url = "http://contested.grantkeller.org:1234/";
    private ProgressDialog pDialog;
    private List<Contest> contestList = new ArrayList<Contest>();
    private ListView listView;
    private CustomListAdapter adapter;
    private String uname;
    private String pass;
    public static final String MYPREFS = "mySharedPreferences";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_contest_list, container, false);


        listView = (ListView) rootView.findViewById(R.id.ContestListView);
        adapter = new CustomListAdapter(getActivity(), contestList);
        listView.setAdapter(adapter);

        pDialog = new ProgressDialog(getActivity());
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.show();

        // Creating volley request obj
        SharedPreferences prefs = this.getActivity().getSharedPreferences(MYPREFS, getActivity().MODE_PRIVATE);
        pass = prefs.getString("Password", "not working");
        uname = prefs.getString("Username", "not working");


        JSONObject params = new JSONObject();
        try {
            params.put("password", pass);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            params.put("requestid", "getmycontests");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            params.put("username", uname);
        } catch (JSONException e) {
            e.printStackTrace();
        }



        JsonObjectRequest contestReq = new JsonObjectRequest(url, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        hidePDialog();


                        try {
                            JSONArray result = new JSONArray();
                            result = response.getJSONArray("contests");




                        // Parsing json
                        for (int i = 0; i < result.length(); i++) {


                                JSONObject obj = result.getJSONObject(i);
                                Contest contest = new Contest();
                                contest.setContestName(obj.getString("contestName"));
                                contest.setThumbnail(obj.getString("image1"));
                                contest.setThumbnailTwo(obj.getString("image2"));
                                contest.setUserOne(obj.getString("userOne"));
                                contest.setUserTwo(obj.getString("userTwo"));

// adding contests to contest array
                                contestList.add(contest);

                            }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }



                        // notifying list adapter about data changes
                        // so that it renders the list view with updated data
                        adapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                hidePDialog();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(contestReq);
    return rootView;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hidePDialog();
    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }
}