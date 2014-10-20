package com.edgecase.contested.library;

/**
 * Created by reubenromandy on 9/21/14.
 */

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.edgecase.contested.app.AppController;

import org.json.JSONObject;
public class JSONParser {
    static JSONObject jObj = null;
    static String TAG;
    // constructor
    public JSONParser(String Tag) {
        TAG = Tag;
        Log.e(Tag, "In Parser");
    }
    public JSONObject getJSONFromUrl(String url, JSONObject params) {


        JsonObjectRequest contestCreateReq = new JsonObjectRequest(url, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        jObj = response;
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(contestCreateReq);

        // return JSON String
        return jObj;
    }
}