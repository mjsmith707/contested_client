package com.edgecase.contested.ActivityFragments;

/**
 * Created by msmith on 10/17/14.
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.edgecase.contested.R;
import com.edgecase.contested.adapter.CustomListAdapter;
import com.edgecase.contested.adapter.FriendsListAdapter;
import com.edgecase.contested.app.AppController;
import com.edgecase.contested.model.Friend;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class FriendsFragment extends Fragment implements AdapterView.OnItemClickListener, PopupMenu.OnMenuItemClickListener {
    // Log tag
    private static final String TAG = FriendsFragment.class.getSimpleName();

    // Contests json url
    private String url = AppController.getInstance().getUrl();
    private ProgressDialog pDialog;
    private List<Friend> friendsList = new ArrayList<Friend>();
    private List<Friend> friendRequestsList = new ArrayList<Friend>();
    private ListView friendsListView;
    private ListView friendRequestsListView;
    private Button addFriendButton;
    private int friendsListViewID;
    private int friendRequestsListViewID;
    private FriendsListAdapter friendRequestAdapter;
    private FriendsListAdapter friendsListAdapter;
    private PopupMenu requestsMenu;
    OnDetailView mListener;
    Context context;
    private String uname;
    private String pass;
    private String contestID;
    private String friendName = "";
    public static final String MYPREFS = "mySharedPreferences";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /** Getting the arguments to the Bundle object */
        Bundle data = getArguments();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_friends, container, false);

        friendRequestsListView = (ListView) rootView.findViewById(R.id.friendRequestsListView);
        friendRequestAdapter = new FriendsListAdapter(getActivity(), friendRequestsList);
        friendRequestsListView.setAdapter(friendRequestAdapter);
        friendRequestsListView.setOnItemClickListener(this);

        friendsListView = (ListView) rootView.findViewById(R.id.friendsListView);
        friendsListAdapter = new FriendsListAdapter(getActivity(), friendsList);
        friendsListView.setAdapter(friendsListAdapter);
        friendsListView.setOnItemClickListener(this);

        Button addFriendButton = (Button) rootView.findViewById(R.id.addFriendButton);
        addFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addFriendClickIntent = new Intent(getActivity(), AddFriendActivity.class);
                startActivity(addFriendClickIntent);
                updateRequestsList();
                updateFriendsList();
            }
        });


        pDialog = new ProgressDialog(getActivity());
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.show();

        updateRequestsList();
        updateFriendsList();

        return rootView;
    }

    private void updateRequestsList() {
        // Creating volley request obj
        friendRequestsList.clear();
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
            params.put("requestid", "getfriendrequests");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            params.put("username", uname);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest friendRequestReq = new JsonObjectRequest(url, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        hidePDialog();

                        try {
                            JSONArray result = new JSONArray();
                            result = response.getJSONArray("friends");

                            // Parsing json
                            for (int i = 0; i < result.length(); i++) {
                                JSONObject obj = result.getJSONObject(i);
                                Friend friendRequest = new Friend();
                                friendRequest.setFriendName(obj.getString("name"));
                                friendRequest.setFriendImage("NULL");
                                friendRequestsList.add(friendRequest);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        // notifying list adapter about data changes
                        // so that it renders the list view with updated data
                        friendRequestAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                hidePDialog();
            }
        });
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(friendRequestReq);
    }

    private void updateFriendsList() {
        // Creating volley request obj
        friendsList.clear();
        SharedPreferences prefs = this.getActivity().getSharedPreferences(MYPREFS, getActivity().MODE_PRIVATE);
        pass = prefs.getString("Password", "not working");
        uname = prefs.getString("Username", "not working");

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
                        hidePDialog();

                        try {
                            JSONArray result = new JSONArray();
                            result = response.getJSONArray("friends");

                            // Parsing json
                            for (int i = 0; i < result.length(); i++) {
                                JSONObject obj = result.getJSONObject(i);
                                Friend friend = new Friend();
                                friend.setFriendName(obj.getString("name"));
                                friend.setFriendImage("NULL");
                                friendsList.add(friend);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        // notifying list adapter about data changes
                        // so that it renders the list view with updated data
                        friendsListAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                hidePDialog();
            }
        });

        AppController.getInstance().addToRequestQueue(friendsListReq);
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
        if (adapterView.getId() == R.id.friendsListView) {
            Log.w("friends list", "on click listener launched");
            Friend friend = friendsList.get(position);

            requestsMenu = new PopupMenu(getActivity(), friendsListView);
            requestsMenu.getMenu().add(Menu.NONE, 1, Menu.NONE, "View Contests");
            requestsMenu.getMenu().add(Menu.NONE, 2, Menu.NONE, "Remove Friend");
            requestsMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    if (menuItem.getItemId() == 1) {
                        if (friendName.isEmpty()) {
                            return false;
                        }
                        JSONObject params = new JSONObject();
                        try {
                            params.put("username", uname);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            params.put("password", pass);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            params.put("requestid", "getfriendscontests");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            params.put("reqparam1", friendName);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        JsonObjectRequest addFriendRequest = new JsonObjectRequest(url, params,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        Log.d(TAG, response.toString());
                                        hidePDialog();

                                        try {
                                            int resultid = response.getInt("RESULT");
                                            if (resultid == 1000) {
                                                pDialog = new ProgressDialog(getActivity());
                                                // Showing progress dialog before making http request
                                                pDialog.setMessage("Loading...");
                                                pDialog.show();
                                                updateRequestsList();
                                                updateFriendsList();
                                                hidePDialog();
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                VolleyLog.d(TAG, "Error: " + error.getMessage());
                                hidePDialog();
                            }
                        });
                        AppController.getInstance().addToRequestQueue(addFriendRequest);
                    }
                    else if (menuItem.getItemId() == 2) {
                        if (friendName.isEmpty()) {
                            return false;
                        }
                        JSONObject params = new JSONObject();
                        try {
                            params.put("username", uname);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            params.put("password", pass);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            params.put("requestid", "removefriend");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            params.put("reqparam1", friendName);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        JsonObjectRequest addFriendRequest = new JsonObjectRequest(url, params,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        Log.d(TAG, response.toString());
                                        hidePDialog();

                                        try {
                                            int resultid = response.getInt("RESULT");
                                            if (resultid == 1000) {
                                                pDialog = new ProgressDialog(getActivity());
                                                // Showing progress dialog before making http request
                                                pDialog.setMessage("Loading...");
                                                pDialog.show();
                                                updateRequestsList();
                                                updateFriendsList();
                                                hidePDialog();
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                        // notifying list adapter about data changes
                                        // so that it renders the list view with updated data
                                        friendRequestAdapter.notifyDataSetChanged();
                                        friendsListAdapter.notifyDataSetChanged();
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                VolleyLog.d(TAG, "Error: " + error.getMessage());
                                hidePDialog();
                            }
                        });
                        AppController.getInstance().addToRequestQueue(addFriendRequest);
                    }
                    return true;
                }
            });
            friendName = friend.getFriendName();
            requestsMenu.show();
        }
        else if (adapterView.getId() == R.id.friendRequestsListView) {
            Log.w("friends requests", "on click listener launched");
            Friend friend = friendRequestsList.get(position);

            requestsMenu = new PopupMenu(getActivity(), friendRequestsListView);
            requestsMenu.getMenu().add(Menu.NONE, 1, Menu.NONE, "Accept Request");
            requestsMenu.getMenu().add(Menu.NONE, 2, Menu.NONE, "Ignore Request");
            requestsMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    if (menuItem.getItemId() == 1) {
                        if (friendName.isEmpty()) {
                            return false;
                        }
                        JSONObject params = new JSONObject();
                        try {
                            params.put("username", uname);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            params.put("password", pass);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            params.put("requestid", "addfriend");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            params.put("reqparam1", friendName);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        JsonObjectRequest addFriendRequest = new JsonObjectRequest(url, params,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        Log.d(TAG, response.toString());
                                        hidePDialog();

                                        try {
                                            int resultid = response.getInt("RESULT");
                                            if (resultid == 1000) {
                                                pDialog = new ProgressDialog(getActivity());
                                                // Showing progress dialog before making http request
                                                pDialog.setMessage("Loading...");
                                                pDialog.show();
                                                updateRequestsList();
                                                updateFriendsList();
                                                hidePDialog();
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                        // notifying list adapter about data changes
                                        // so that it renders the list view with updated data
                                        friendRequestAdapter.notifyDataSetChanged();
                                        friendsListAdapter.notifyDataSetChanged();
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                VolleyLog.d(TAG, "Error: " + error.getMessage());
                                hidePDialog();
                            }
                        });
                        AppController.getInstance().addToRequestQueue(addFriendRequest);
                    }
                    else if (menuItem.getItemId() == 2) {
                        if (friendName.isEmpty()) {
                            return false;
                        }
                        JSONObject params = new JSONObject();
                        try {
                            params.put("username", uname);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            params.put("password", pass);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            params.put("requestid", "removefriend");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            params.put("reqparam1", friendName);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        JsonObjectRequest addFriendRequest = new JsonObjectRequest(url, params,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        Log.d(TAG, response.toString());
                                        hidePDialog();

                                        try {
                                            int resultid = response.getInt("RESULT");
                                            if (resultid == 1000) {
                                                pDialog = new ProgressDialog(getActivity());
                                                // Showing progress dialog before making http request
                                                pDialog.setMessage("Loading...");
                                                pDialog.show();
                                                updateRequestsList();
                                                updateFriendsList();
                                                hidePDialog();
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                        // notifying list adapter about data changes
                                        // so that it renders the list view with updated data
                                        friendRequestAdapter.notifyDataSetChanged();
                                        friendsListAdapter.notifyDataSetChanged();
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                VolleyLog.d(TAG, "Error: " + error.getMessage());
                                hidePDialog();
                            }
                        });
                        AppController.getInstance().addToRequestQueue(addFriendRequest);
                    }
                    return true;
                }
            });
            friendName = friend.getFriendName();
            requestsMenu.show();
        }

    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        return false;
    }


    public interface OnDetailView {
        public void onDetailViewUpdate(Friend friend);
    }



}