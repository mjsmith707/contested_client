package com.edgecase.contested.ActivityFragments;

/**
 * Created by reubenromandy on 9/3/14.
 */

import android.app.ProgressDialog;
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
import com.android.volley.toolbox.JsonArrayRequest;
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
    private static final String url = "http://10.0.2.2:3000/contests";
    private ProgressDialog pDialog;
    private List<Contest> contestList = new ArrayList<Contest>();
    private ListView listView;
    private CustomListAdapter adapter;

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
        JsonArrayRequest contestReq = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        hidePDialog();
                        // Parsing json
                        for (int i = 0; i < response.length(); i++) {
                            try {

                                JSONObject obj = response.getJSONObject(i);
                                Contest contest = new Contest();
                                contest.setContestName(obj.getString("contestName"));
                                contest.setThumbnailUrl(obj.getString("image1"));
                                contest.setThumbnailUrlTwo(obj.getString("image2"));
                                contest.setUserOne(obj.getString("userOne"));
                                contest.setUserTwo(obj.getString("userTwo"));

// adding contests to contest array
                                contestList.add(contest);


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

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