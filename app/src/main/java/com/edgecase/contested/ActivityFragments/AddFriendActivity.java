package com.edgecase.contested.ActivityFragments;

/**
 * Created by msmith on 10/18/14.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.edgecase.contested.R;
import com.edgecase.contested.app.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AddFriendActivity extends Activity {
    private static final String TAG = FriendsFragment.class.getSimpleName();
    public static final String MYPREFS = "mySharedPreferences";
    private String url = AppController.getInstance().getUrl();

    private ProgressDialog pDialog;
    private AlertDialog.Builder aDialog;
    private EditText addFriendTextField;
    private Button addFriendButton;

    private String uname;
    private String pass;
    private String friendName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);

        addFriendTextField = (EditText)getWindow().getDecorView().findViewById(R.id.addFriendTextField);
        addFriendButton = (Button)getWindow().getDecorView().findViewById(R.id.addFriendButton);
        addFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = addFriendTextField.getText().toString();
                if (username.isEmpty()) {
                    return;
                }
                else {
                    friendName = username;
                    addFriend();
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hidePDialog();
    }

    private void showPDialog() {
        pDialog = new ProgressDialog(this);
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.show();
    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }

    private void successADialog() {
        if (aDialog == null) {
            deleteADialog();
        }
        aDialog = new AlertDialog.Builder(this);
        aDialog.setTitle("Success");
        aDialog.setMessage("Friend Request Sent");
        aDialog.setCancelable(false);
        aDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        aDialog.show();
    }

    private void unknownUsernameADialog() {
        if (aDialog == null) {
            deleteADialog();
        }
        aDialog = new AlertDialog.Builder(this);
        aDialog.setTitle("Failure");
        aDialog.setMessage("This username does not exist");
        aDialog.setCancelable(false);
        aDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //
            }
        });
        aDialog.show();
    }

    private void failureADialog() {
        if (aDialog == null) {
            deleteADialog();
        }
        aDialog = new AlertDialog.Builder(this);
        aDialog.setTitle("Failure");
        aDialog.setMessage("An error occured");
        aDialog.setCancelable(false);
        aDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //
            }
        });
        aDialog.show();
    }

    private void alreadyAddedAdialog() {
        if (aDialog == null) {
            deleteADialog();
        }
        aDialog = new AlertDialog.Builder(this);
        aDialog.setTitle("Failure");
        aDialog.setMessage("You have already sent a request to this user.");
        aDialog.setCancelable(false);
        aDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //
            }
        });
        aDialog.show();
    }

    private void deleteADialog() {
        if (aDialog != null) {
            aDialog = null;
        }
    }
    private void addFriend() {
        SharedPreferences prefs = this.getSharedPreferences(MYPREFS, this.MODE_PRIVATE);
        pass = prefs.getString("Password", "not working");
        uname = prefs.getString("Username", "not working");

        if (friendName.isEmpty()) {
            return;
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
                                successADialog();
                            }
                            else if (resultid == 1013) {
                                unknownUsernameADialog();
                            }
                            else if (resultid == 1003) {
                                alreadyAddedAdialog();
                            }
                            else {
                                failureADialog();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            failureADialog();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        });
        showPDialog();
        AppController.getInstance().addToRequestQueue(addFriendRequest);
    }


}
