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
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.edgecase.contested.R;
import com.edgecase.contested.library.UserFunctions;

import org.json.JSONException;
import org.json.JSONObject;
public class LoginActivity extends Activity {
    Button btnLogin;
    Button Btnregister;
    Button passreset;
    EditText inputUser;
    EditText inputPassword;
    private TextView loginErrorMsg;
    /**
     * Called when the activity is first created.
     */
    private static String RESULT = "success";
    private static String KEY_UID = "uid";
    private static String KEY_USERNAME = "uname";
    private static String KEY_FIRSTNAME = "fname";
    private static String KEY_LASTNAME = "lname";
    private static String KEY_EMAIL = "email";
    private static String KEY_CREATED_AT = "created_at";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_main);
        inputUser = (EditText) findViewById(R.id.username);
        inputPassword = (EditText) findViewById(R.id.pword);
        Btnregister = (Button) findViewById(R.id.registerbtn);
        btnLogin = (Button) findViewById(R.id.login);
        passreset = (Button)findViewById(R.id.passres);
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
            }});
/**
 * Login button click event
 * A Toast is set to alert when the Email and Password field is empty
 **/
        btnLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (  ( !inputUser.getText().toString().equals("")) && ( !inputPassword.getText().toString().equals("")) )
                {
                    ConnectivityManager connMgr = (ConnectivityManager)
                            getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                    if (networkInfo != null && networkInfo.isConnected()) {
                        new ProcessLogin().execute();
                    } else {
                        Toast.makeText(getApplicationContext(),
                                "No network Connection available", Toast.LENGTH_LONG).show();

                    }
                }
                else if ( ( !inputUser.getText().toString().equals("")) )
                {
                    Toast.makeText(getApplicationContext(),
                            "Password field empty", Toast.LENGTH_SHORT).show();
                }
                else if ( ( !inputPassword.getText().toString().equals("")) )
                {
                    Toast.makeText(getApplicationContext(),
                            "Email field empty", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),
                            "Email and Password field are empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

/**
 * Async Task to get and send data to My Sql database through JSON response.
 **/
private class ProcessLogin extends AsyncTask <String, Void , JSONObject>{
    private ProgressDialog pDialog;
    String user,password;
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        inputUser = (EditText) findViewById(R.id.username);
        inputPassword = (EditText) findViewById(R.id.pword);
        user = inputUser.getText().toString();
        password = inputPassword.getText().toString();
        pDialog = new ProgressDialog(LoginActivity.this);
        pDialog.setTitle("Contacting Servers");
        pDialog.setMessage("Logging in ...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();
    }
    @Override
    protected JSONObject doInBackground(String... args) {
        UserFunctions userFunction = new UserFunctions();
        JSONObject json = userFunction.loginUser(user, password);
        return json;
    }
    @Override
    protected void onPostExecute(JSONObject json) {
        try {
            if (json.getString("RESULT") != null) {
                String res = json.getString("RESULT");
                if(Integer.parseInt(res) == 1009){
                    pDialog.setMessage("Loading User Space");
                    pDialog.setTitle("Getting Data");
                   // DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                   // JSONObject json_user = json.getJSONObject("user");
                    /**
                     * Clear all previous data in SQlite database.
                     **/
                   // UserFunctions logout = new UserFunctions();
                   // logout.logoutUser(getApplicationContext());
                   // db.addUser(json_user.getString(KEY_FIRSTNAME),json_user.getString(KEY_LASTNAME),json_user.getString(KEY_EMAIL),json_user.getString(KEY_USERNAME),json_user.getString(KEY_UID),json_user.getString(KEY_CREATED_AT));
                    /**
                     *If JSON array details are stored in SQlite it launches the main activity.
                     **/
                    Intent startMain = new Intent(getApplicationContext(), MainActivity.class);

                    pDialog.dismiss();
                    startActivity(startMain);
                    /**
                     * Close Login Screen
                     **/
                    finish();
                }else{
                    pDialog.dismiss();
                    loginErrorMsg.setText("Incorrect username/password");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

}