package com.edgecase.contested.library;

/**
 * Created by reubenromandy on 9/21/14.
 */

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
public class UserFunctions {
    private JSONParser jsonParser;
    //URL of the contested API
    private static String URL = "http://contested.grantkeller.org:1234/";
    private static String TAG;

    // constructor
    public UserFunctions(String Tag){
        TAG = Tag;
        Log.e(TAG, "in");
        jsonParser = new JSONParser(TAG);
    }
    /**
     * Function to Login
     **/
    public JSONObject loginUser(String user, String password){
        // Building Parameters
        JSONObject params = new JSONObject();
        Log.e(TAG, "in further");
       /* try {
            params.put("tag", login_tag);
        } catch (JSONException e) {
            e.printStackTrace();
        }*/
        try {
            params.put("username", user);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            params.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            params.put("requestid", "authenticate");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONObject json = jsonParser.getJSONFromUrl(URL, params);
        return json;
    }
    /**
     * Function to change password
     **/
    public JSONObject chgPass(String newpas, String email){
        JSONObject params = new JSONObject();


        try {
            params.put("newpas", newpas);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            params.put("email", email);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONObject json = jsonParser.getJSONFromUrl(URL, params);
        return json;
    }
    /**
     * Function to reset the password
     **/
    public JSONObject forPass(String forgotpassword){
        JSONObject params = new JSONObject();

        try {
            params.put("forgotpassword", forgotpassword);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONObject json = jsonParser.getJSONFromUrl(URL, params);
        return json;
    }
    /**
     * Function to  Register
     **/
    public JSONObject registerUser(/*String fname, String lname,*/ String email, String uname, String password){
        // Building Parameters
        JSONObject params = new JSONObject();
        try {
            params.put("username", uname);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            params.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            params.put("requestid", "createuser");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            params.put("reqparam1", email);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONObject json = jsonParser.getJSONFromUrl(URL,params);
        return json;
    }

    public JSONObject createContest(String uname, String pass, String contestNameString, String contestType){

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
        params.put("reqparam2", "contestType");
    } catch (JSONException e) {
        e.printStackTrace();
    }
        JSONObject json = jsonParser.getJSONFromUrl(URL,params);
        return json;
    }

    /**
     * Function to logout user
     * Resets the temporary data stored in SQLite Database
     * */
    public boolean logoutUser(Context context){
        DatabaseHandler db = new DatabaseHandler(context);
        db.resetTables();
        return true;
    }
}