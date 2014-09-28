package com.edgecase.contested.library;

/**
 * Created by reubenromandy on 9/21/14.
 */

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;
public class UserFunctions {
    private JSONParser jsonParser;
    //URL of the contested API
    private static String loginURL = "http://24.130.89.93:1234/";
    private static String registerURL = "http://24.130.89.93:1234/";
    private static String forpassURL = "http://10.0.2.2:80/";
    private static String chgpassURL = "http://10.0.2.2:80/";
    private static String login_tag = "login";
    private static String register_tag = "createuser";
    private static String forpass_tag = "forpass";
    private static String chgpass_tag = "chgpass";
    // constructor
    public UserFunctions(){
        jsonParser = new JSONParser();
    }
    /**
     * Function to Login
     **/
    public JSONObject loginUser(String user, String password){
        // Building Parameters
        JSONObject params = new JSONObject();
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
        JSONObject json = jsonParser.getJSONFromUrl(loginURL, params);
        return json;
    }
    /**
     * Function to change password
     **/
    public JSONObject chgPass(String newpas, String email){
        JSONObject params = new JSONObject();
        try {
            params.put("tag", chgpass_tag);
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
        JSONObject json = jsonParser.getJSONFromUrl(chgpassURL, params);
        return json;
    }
    /**
     * Function to reset the password
     **/
    public JSONObject forPass(String forgotpassword){
        JSONObject params = new JSONObject();
        try {
            params.put("tag", forpass_tag);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            params.put("forgotpassword", forgotpassword);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONObject json = jsonParser.getJSONFromUrl(forpassURL, params);
        return json;
    }
    /**
     * Function to  Register
     **/
    public JSONObject registerUser(/*String fname, String lname,*/ String email, String uname, String password){
        // Building Parameters
        JSONObject params = new JSONObject();
        //params.add(new BasicNameValuePair("fname", fname));
       // params.add(new BasicNameValuePair("lname", lname));
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
            params.put("requestid", register_tag);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            params.put("reqparam1", email);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONObject json = jsonParser.getJSONFromUrl(registerURL,params);
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