package com.edgecase.contested.ActivityFragments;

/**
 * Created by reubenromandy on 9/21/14.
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.edgecase.contested.R;
import com.edgecase.contested.app.AppController;

import org.json.JSONException;
import org.json.JSONObject;
public class RegisterActivity extends Activity {
    private static final String TAG = RegisterActivity.class.getSimpleName();

    /**
     * Defining layout items.
     */
    EditText inputFirstName;
    EditText inputLastName;
    EditText inputUsername;
    EditText inputEmail;
    EditText inputPassword;
    Button btnRegister;
    TextView registerErrorMsg;
    JSONObject json;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        /**
         * Defining all layout items
         **/
        inputFirstName = (EditText) findViewById(R.id.fname);
        inputLastName = (EditText) findViewById(R.id.newContestName);
        inputUsername = (EditText) findViewById(R.id.uname);
        inputEmail = (EditText) findViewById(R.id.username);
        inputPassword = (EditText) findViewById(R.id.pword);
        btnRegister = (Button) findViewById(R.id.register);
        registerErrorMsg = (TextView) findViewById(R.id.register_error);
        final String url = ((AppController) this.getApplication()).getUrl();

/**
 * Button which Switches back to the login screen on clicked
 **/
        Button login = (Button) findViewById(R.id.bktologin);
        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), LoginActivity.class);
                startActivityForResult(myIntent, 0);
                finish();
            }
        });
        /**
         * Register Button click event.
         * A Toast is set to alert when the fields are empty.
         * Another toast is set to alert Username must be 5 characters.
         **/
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((!inputUsername.getText().toString().equals("")) && (!inputPassword.getText().toString().equals("")) && (!inputFirstName.getText().toString().equals("")) && (!inputLastName.getText().toString().equals("")) && (!inputEmail.getText().toString().equals(""))) {
                    if (inputUsername.getText().toString().length() > 4) {
                        ConnectivityManager connMgr = (ConnectivityManager)
                                getSystemService(Context.CONNECTIVITY_SERVICE);
                        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                        if (networkInfo != null && networkInfo.isConnected()) {
                            final ProgressDialog pDialog;
                            String email, password, uname;

                            inputUsername = (EditText) findViewById(R.id.uname);
                            inputPassword = (EditText) findViewById(R.id.pword);
                            email = inputEmail.getText().toString();
                            uname = inputUsername.getText().toString();
                            password = inputPassword.getText().toString();
                            pDialog = new ProgressDialog(RegisterActivity.this);
                            pDialog.setTitle("Contacting Servers");
                            pDialog.setMessage("Registering ...");
                            pDialog.setIndeterminate(false);
                            pDialog.setCancelable(true);
                            pDialog.show();

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


                            JsonObjectRequest contestCreateReq = new JsonObjectRequest(url, params,
                                    new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response) {
                                            Log.d(TAG, response.toString());
                                            json = response;
                                            try {
                                                if (json.getString("RESULT") != null) {
                                                    registerErrorMsg.setText("");
                                                    String res = json.getString("RESULT");
                                                    // String red = json.getString(KEY_ERROR);
                                                    if (Integer.parseInt(res) == 1005) {
                                                        pDialog.setTitle("Getting Data");
                                                        pDialog.setMessage("Loading Info");
                                                        registerErrorMsg.setText("Successfully Registered");
                                                        // DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                                                        // JSONObject json_user = json.getJSONObject("user");
                                                        /**
                                                         * Removes all the previous data in the SQlite database
                                                         **/
                                                        // UserFunctions logout = new UserFunctions();
                                                        //   logout.logoutUser(getApplicationContext());
                                                        //  db.addUser(json_user.getString(KEY_FIRSTNAME),json_user.getString(KEY_LASTNAME),json_user.getString(KEY_EMAIL),json_user.getString(KEY_USERNAME),json_user.getString(KEY_UID),json_user.getString(KEY_CREATED_AT));
                                                        /**
                                                         * Stores registered data in SQlite Database
                                                         * Launch Registered screen
                                                         **/
                                                        Intent login = new Intent(getApplicationContext(), LoginActivity.class);
                                                        /**
                                                         * Close all views before launching Registered screen
                                                         **/
                                                        login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                        pDialog.dismiss();
                                                        startActivity(login);
                                                        finish();
                                                    } else if (Integer.parseInt(res) == 1006) {
                                                        pDialog.dismiss();
                                                        registerErrorMsg.setText("User already exists");
                                                    } else if (Integer.parseInt(res) == 1003) {
                                                        pDialog.dismiss();
                                                        registerErrorMsg.setText("Invalid Email id");
                                                    }
                                                } else {
                                                    pDialog.dismiss();
                                                    registerErrorMsg.setText("Error occured in registration");
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

                            // Adding request to request queue
                            AppController.getInstance().addToRequestQueue(contestCreateReq);


                        } else {
                            Toast.makeText(getApplicationContext(),
                                    "No network Connection available", Toast.LENGTH_LONG).show();

                        }
                    } else {
                        Toast.makeText(getApplicationContext(),
                                "Username should be minimum 5 characters", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(),
                            "One or more fields are empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
