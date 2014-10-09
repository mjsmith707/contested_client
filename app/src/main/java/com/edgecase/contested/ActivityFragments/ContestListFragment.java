package com.edgecase.contested.ActivityFragments;

/**
 * Created by reubenromandy on 9/3/14.
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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




public class ContestListFragment extends Fragment implements AdapterView.OnItemClickListener {
    // Log tag
    private static final String TAG = ContestListFragment.class.getSimpleName();

    // Contests json url
    private String url = AppController.getInstance().getUrl();
    private ProgressDialog pDialog;
    private List<Contest> contestList = new ArrayList<Contest>();
    private ListView listView;
    private CustomListAdapter adapter;
    OnDetailView mListener;
    Context context;
    private String uname;
    private String pass;
    private String contestID;
    public static final String MYPREFS = "mySharedPreferences";

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        context = activity;
        if (activity instanceof OnDetailView)
        {
            mListener = (OnDetailView) activity;
        } else {
            throw new  ClassCastException(activity.toString());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_contest_list, container, false);


        listView = (ListView) rootView.findViewById(R.id.ContestListView);
        adapter = new CustomListAdapter(getActivity(), contestList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
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
                                contest.setContestID(obj.getString("contestID"));

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

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Log.w("contested", "on click listener launched");
        mListener.onDetailViewUpdate(contestList.get(position));
    }

    public interface OnDetailView {
        public void onDetailViewUpdate(Contest contest);
    }
}