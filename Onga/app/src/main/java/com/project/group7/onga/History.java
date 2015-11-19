package com.project.group7.onga;

import android.app.Activity;
import android.app.Fragment;
import android.app.ListActivity;
import android.app.ListFragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
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
 * Created by fredrickabayie on 17/11/15.
 */
public class History extends ListFragment {

    JSONArray users = null;
    ListView listView;
    ArrayAdapter<String>adapter;

    SessionManager sessionManager;

    ArrayList<HashMap<String, String>> usersList;
    private static final String TAG_RESULTID = "result";
    private static final String TAG_HISTORY = "history";
    private static final String TAG_MEALNAME = "meal_name";
    private static final String TAG_MEALPRICE = "meal_price";
    private static final String TAG_ORDERDATE = "order_date";
    private static final String TAG_ORDERTIME = "order_time";


    public History() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sessionManager = new SessionManager(getActivity().getApplicationContext());

        HashMap<String, String> user = sessionManager.getUserDetails();

        String user_id = user.get(SessionManager.KEY_STUDENTID);

        usersList = new ArrayList<>();
        DownloadWebPageTask task = new DownloadWebPageTask();
        task.execute("http://cs.ashesi.edu.gh/~csashesi/class2016/fredrick-abayie/mobileweb/onga_mwc/php/onga.php?cmd=onga_mwc_orders_history&user_id="+user_id);
//        ListView lv = getListView();
//        lv.setOnClickListener(this);
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_history, container, false);

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



    /**
     *
     */
    private class DownloadWebPageTask extends AsyncTask<String, Void, String> {

//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            // Showing progress dialog
//            pDialog = new ProgressDialog(Login.this);
//            pDialog.setMessage("Please wait...");
//            pDialog.setCancelable(false);
//            pDialog.show();
//        }

        /**
         * Functiont to open an http connection
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
                    String resultID = jsonObject.getString(TAG_RESULTID);
                    if(resultID.equals("1")) {
                        users = jsonObject.getJSONArray(TAG_HISTORY);
                        for (int i = 0; i < users.length(); i++) {
                            JSONObject jObj = users.getJSONObject(i);

                            String meal_name = jObj.getString(TAG_MEALNAME);
                            String meal_price = "GH\u20B5 "+jObj.getString(TAG_MEALPRICE);
                            String order_date = jObj.getString(TAG_ORDERDATE);
                            String order_time = jObj.getString(TAG_ORDERTIME);

                            HashMap<String, String> user = new HashMap<>();

                            user.put(TAG_MEALNAME, meal_name);
                            user.put(TAG_MEALPRICE, meal_price);
                            user.put(TAG_ORDERDATE, order_date);
                            user.put(TAG_ORDERTIME, order_time);


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
<<<<<<< HEAD
                    R.layout.history_list, new String[] { TAG_MEALNAME, TAG_MEALPRICE, TAG_ORDERDATE, TAG_ORDERTIME },
                    new int[] { R.id.list_meal_name, R.id.list_meal_price, R.id.list_order_date, R.id.list_order_time } );

=======
                    R.layout.history_list, new String[] { TAG_MEALNAME, TAG_MEALPRICE, TAG_ORDERDATE },
                    new int[] { R.id.list_meal_name, R.id.list_meal_price, R.id.list_order_date } );
>>>>>>> origin/codewreaker-branch
            setListAdapter(adapter);

        }
    }



//    /**
//     * A function to read the web page
//     * @param view
//     */
//    public void readWebpage(View view) {
//        DownloadWebPageTask task = new DownloadWebPageTask();
//
//        task.execute("http://cs.ashesi.edu.gh/~csashesi/class2016/fredrick-abayie/mobileweb/onga_mwc/php/onga.php?cmd=onga_mwc_users_all");
//    }

}
