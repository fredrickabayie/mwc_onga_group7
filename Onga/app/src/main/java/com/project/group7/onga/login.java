package com.project.group7.onga;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Blob;

public class Login extends AppCompatActivity implements View.OnClickListener{

    private Button login_btn;
    private EditText username, password;
    SessionManager sessionManager;

//    JSONArray user = null;
    private static final String TAG_RESULT = "result";
    private static final String TAG_USERNAME = "username";
    private static final String TAG_USERDP = "dp";
    private static final String TAG_ID = "id";

    private ProgressDialog pDialog;

    private RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sessionManager = new SessionManager(getApplicationContext());

        login_btn = (Button) findViewById(R.id.login_btn);
        login_btn.setOnClickListener(this);

        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);

        relativeLayout = (RelativeLayout) findViewById(R.id.relativelayout);

    }

    /**
     * Function to handle the Login button click
     * @param view The current view
     */
    @Override
    public void onClick (View view) {
        if (view == login_btn) {
//            Toast.makeText(getApplicationContext(), username.getText().toString()+" "+password.getText().toString(), Toast.LENGTH_LONG).show();
            readWebpage(view);
        }

    }


    /**
     *
     */
    private class DownloadWebPageTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(Login.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         *Functiont to open an http connection
         * @param urls The url to be sent
         * @return Returning the response
         */
        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            HttpURLConnection urlConnection = null;
            for (String url1 : urls) {
                try {
                    URL url = new URL(url1);
                    urlConnection = (HttpURLConnection) url.openConnection();
                    System.out.println(url);
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    BufferedReader buffer = new BufferedReader(
                            new InputStreamReader(in));
                    String s = "";
                    while ((s = buffer.readLine()) != null) {
                        System.out.println(s);
                        response += s;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    urlConnection.disconnect();
                }
            }

            return response;

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            System.out.println(values[0]);
        }


        /**
         *Function to get result from the http post
         * @param result Result from the post
         */
        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject jsonObj = new JSONObject(result);

                String res = jsonObj.getString(TAG_RESULT);
                System.out.print(res + "\n");
                if(res.equals("1")) {

                    String user = jsonObj.getString(TAG_USERNAME);
                    String id = jsonObj.getString(TAG_ID);
                    String pic = jsonObj.getString(TAG_USERDP);
//                    System.out.println(pic);

                    if (pDialog.isShowing())
                        pDialog.dismiss();

                    sessionManager.createLoginSession(user, id, pic);

                    Intent home = new Intent(Login.this, MainActivity.class);
                    startActivity(home);
                    finish();
                }
                else {

                    if (pDialog.isShowing())
                        pDialog.dismiss();
                    String msg = jsonObj.getString("message");
//                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();

                    Snackbar snackbar = Snackbar
                            .make(relativeLayout, msg, Snackbar.LENGTH_SHORT);

                    // Changing message text color
//                    snackbar.setActionTextColor(Color.RED);

                    // Changing action button text color
                    View sbView = snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(Color.RED);

                    snackbar.show();





                }

            } catch (JSONException jsonex) {
                jsonex.printStackTrace();
            }
        }
    }



    /**
     * A function to read the web page
     * @param view
     */
    public void readWebpage(View view) {
        DownloadWebPageTask task = new DownloadWebPageTask();

        if(username.getText().toString().trim().length() > 0 && password.getText().toString().trim().length() > 0) {
            task.execute("http://cs.ashesi.edu.gh/~csashesi/class2016/fredrick-abayie/mobileweb/onga_mwc/php/onga.php?cmd=onga_mwc_users&username="
                    +username.getText().toString().trim()+"&password="+password.getText().toString().trim());
        } else {
            username.setError("Please enter your username");
            password.setError("Please enter your password");
//            password.setError("");
        }
    }
}
