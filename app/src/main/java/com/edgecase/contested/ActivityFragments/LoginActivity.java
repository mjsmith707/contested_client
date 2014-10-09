package com.edgecase.contested.ActivityFragments;

/**
 * Created by reubenromandy on 9/21/14.
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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



public class LoginActivity extends Activity {
    private static final String TAG = LoginActivity.class.getSimpleName();
    Button btnLogin;
    Button Btnregister;
    Button passreset;
    EditText inputUser;
    EditText inputPassword;
    private TextView loginErrorMsg;
    public static final String MYPREFS = "mySharedPreferences";
    JSONObject json;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_main);
        final String url = ((AppController) this.getApplication()).getUrl();
        inputUser = (EditText) findViewById(R.id.username);
        inputPassword = (EditText) findViewById(R.id.pword);
        Btnregister = (Button) findViewById(R.id.registerbtn);
        btnLogin = (Button) findViewById(R.id.login);
        passreset = (Button) findViewById(R.id.passres);
        loginErrorMsg = (TextView) findViewById(R.id.loginErrorMsg);
        /*passreset.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), PasswordReset.class);
                startActivityForResult(myIntent, 0);
                finish();
            }});*/
        Btnregister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), RegisterActivity.class);
                startActivityForResult(myIntent, 0);
                finish();
            }
        });
/**
 * Login button click event
 * A Toast is set to alert when the Email and Password field is empty
 **/
        btnLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if ((!inputUser.getText().toString().equals("")) && (!inputPassword.getText().toString().equals(""))) {
                    ConnectivityManager connMgr = (ConnectivityManager)
                            getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                    if (networkInfo != null && networkInfo.isConnected()) {

                        final ProgressDialog pDialog;
                        String user, password;
                        user = inputUser.getText().toString();
                        password = inputPassword.getText().toString();

                        SharedPreferences prefs = getSharedPreferences(MYPREFS, MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("Username", user);
                        editor.putString("Password", password);
                        editor.apply();

                        pDialog = new ProgressDialog(LoginActivity.this);
                        pDialog.setTitle("Contacting Servers");
                        pDialog.setMessage("Logging in ...");
                        pDialog.setIndeterminate(false);
                        pDialog.setCancelable(true);
                        pDialog.show();

                        JSONObject params = new JSONObject();

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

                        JsonObjectRequest contestCreateReq = new JsonObjectRequest(url, params,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        Log.d(TAG, response.toString());
                                        json = response;
                                        try {
                                            if (json.getString("RESULT") != null) {
                                                String res = json.getString("RESULT");
                                                if (Integer.parseInt(res) == 1009) {
                                                    pDialog.setMessage("Loading User Space");
                                                    pDialog.setTitle("Getting Data");

                                                    Intent startMain = new Intent(getApplicationContext(), MainActivity.class);

                                                    pDialog.dismiss();
                                                    startActivity(startMain);
                                                    /**
                                                     * Close Login Screen
                                                     **/
                                                    finish();
                                                } else {
                                                    pDialog.dismiss();
                                                    loginErrorMsg.setText("Incorrect username/password");
                                                }
                                            }
                                        }
                                        catch (JSONException e) {
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
                    else {
                        Toast.makeText(getApplicationContext(),
                                "No network Connection available", Toast.LENGTH_LONG).show();

                    }
                } else if ((!inputUser.getText().toString().equals(""))) {
                    Toast.makeText(getApplicationContext(),
                            "Password field empty", Toast.LENGTH_SHORT).show();
                } else if ((!inputPassword.getText().toString().equals(""))) {
                    Toast.makeText(getApplicationContext(),
                            "Email field empty", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Email and Password field are empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}



