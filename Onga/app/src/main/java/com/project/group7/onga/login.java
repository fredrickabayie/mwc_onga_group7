package com.project.group7.onga;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class login extends AppCompatActivity implements View.OnClickListener{

    private Button login_btn;
    private EditText username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login_btn = (Button) findViewById(R.id.login_btn);
        login_btn.setOnClickListener(this);

        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);

    }

    /**
     * Function to handle the login button click
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

//        @Override
//        protected void onProgressUpdate(Void... values) {
//            super.onProgressUpdate(values);
//            System.out.println(values[0]);
//        }


        /**
         *Function to get result from the http post
         * @param result Result from the post
         */
        @Override
        protected void onPostExecute(String result) {
            if (result.equals("1")) {
                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
                Intent home = new Intent(login.this, MainActivity.class);
                startActivity(home);
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_LONG).show();
            }
        }
    }



    /**
     * A function to read the web page
     * @param view
     */
    public void readWebpage(View view) {
        DownloadWebPageTask task = new DownloadWebPageTask();

        task.execute("http://cs.ashesi.edu.gh/~csashesi/class2016/fredrick-abayie/mobileweb/onga_mwc/php/onga.php?cmd=onga_mwc_users&username="
                +username.getText().toString()+"&password="+password.getText().toString());
    }
}
