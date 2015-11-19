package com.project.group7.onga;

import android.app.Activity;
import android.app.ListFragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
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
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by israel.agyeman.prempeh@gmail.com on 18/11/15.
 */
public class Meal extends ListFragment implements View.OnClickListener{


    JSONArray meals = null;
    int opt=1;
    Button add_btn;
    SessionManager sessionManager;

    ArrayList<HashMap<String, String>> usersList;
    private static final String RESULT= "result";
    private static final String AVAILABLE_FOODS = "meals";
    private static final String MEAL= "meal_name";
    private static final String PRICE = "meal_price";
    private static final String MEAL_STATUS = "meal_status";


    public Meal() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sessionManager = new SessionManager(getActivity().getApplicationContext());

        HashMap<String, String> user = sessionManager.getUserDetails();


        String user_id = user.get(SessionManager.KEY_STUDENTID);

        usersList = new ArrayList<>();
        DownloadAvailableFood task = new DownloadAvailableFood();
        if(opt==1){
            task.execute("http://cs.ashesi.edu.gh/~csashesi/class2016/fredrick-abayie/mobileweb/onga_mwc/php/onga.php?cmd=onga_mwc_meals_all");
        }



    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_meal, container, false);
        // Inflate the layout for this fragment

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }



    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onClick(View v) {

    }


    /**
     *
     */
    private class DownloadAvailableFood extends AsyncTask<String, Void, String> {

        /**
         * Function to open an http connection
         *
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
            if(response != null) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String resultID = jsonObject.getString(RESULT);
                    if(resultID.equals("1")) {
                        meals = jsonObject.getJSONArray(AVAILABLE_FOODS);
                        for (int i = 0; i < meals.length(); i++) {
                            JSONObject jObj = meals.getJSONObject(i);

                            String meal_name = jObj.getString(MEAL);
                            String meal_price = jObj.getString(PRICE);
                            String status = convertStatus(jObj.getString(MEAL_STATUS));

                            HashMap<String, String> user = new HashMap<>();

                            user.put(MEAL, meal_name);
                            user.put(PRICE, meal_price);
                            user.put(MEAL_STATUS, status);

                            usersList.add(user);
                            System.out.println(usersList);
                        }
                    }

                } catch (JSONException jsonex) {
                    jsonex.printStackTrace();
                }

            }

            return response;

        }


        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            System.out.println(values[0]);
        }

        private String convertStatus(String mealId){
            String status = "Not Ready";
            if(mealId.equals("2")){
                status = "Ready";
            }
            return status;
        }


        /**
         * Function to get result from the http post
         *
         * @param result Result from the post
         */
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ListAdapter adapter = new SimpleAdapter (
                    getActivity(), usersList,
                    R.layout.list_row, new String[] { MEAL, PRICE, MEAL_STATUS},
                    new int[] {R.id.item_name, R.id.item_price, R.id.item_status} );
            setListAdapter(adapter);

        }
    }




}
