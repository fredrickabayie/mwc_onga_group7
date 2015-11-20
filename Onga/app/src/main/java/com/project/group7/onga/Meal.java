package com.project.group7.onga;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by israel.agyeman.prempeh@gmail.com on 18/11/15.
 */
public class Meal extends ListFragment implements OnClickListener, SwipeRefreshLayout.OnRefreshListener{


    JSONArray meals = null;
    String userid;
    SessionManager sessionManager;
    DownloadAvailableFood task;

    private SwipeRefreshLayout swipeRefreshLayout;

    ArrayList<HashMap<String, String>> usersList;
    private static final String RESULT= "result";
    private static final String AVAILABLE_FOODS = "meals";
    private static final String MEAL= "meal_name";
    private static final String PRICE = "meal_price";
    private static final String MEAL_ID = "meal_id";
    private static final String MEAL_STATUS = "meal_status";


    public Meal() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sessionManager = new SessionManager(getActivity().getApplicationContext());

        HashMap<String, String> user = sessionManager.getUserDetails();


        userid = user.get(SessionManager.KEY_STUDENTID);

        usersList = new ArrayList<>();
//        task = new DownloadAvailableFood();
//        operations("fetch_all_food",task);



    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_meal, container, false);
        // Inflate the layout for this fragment

        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_meals);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.login_btn, R.color.colorPrimaryDark, R.color.black);
        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeRefreshLayout.setRefreshing(true);

                                        task = new DownloadAvailableFood();
                                        operations("fetch_all_food", task);
                                    }
                                }
        );

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
     * A function that executes operations of the database
     * @param cmd String of the command name
     * @param task DownloadAvailableFood instance
     */
    private void operations(String cmd, DownloadAvailableFood task) {
        switch (cmd) {
            case "fetch_all_food":
                task.execute("http://cs.ashesi.edu.gh/~csashesi/class2016/fredrick-abayie/mobileweb/onga_mwc/php/onga.php?cmd=onga_mwc_meals_all");
                break;
            case "refresh":
                //TODO add a function to refresh the DB
                break;
            default:
                System.out.println("Error");
        }
    }

    /**
     * A function that executes operations of the database
     * @param hashMap String of the command name
     * @param task DownloadAvailableFood instance
     */
    private void addfood(DownloadAvailableFood task, HashMap<String, String> hashMap) {
        int meal_id = Integer.parseInt(hashMap.get(MEAL_ID));
        task.execute("http://cs.ashesi.edu.gh/~csashesi/class2016/fredrick-abayie/mobileweb/onga_mwc/php/onga.php?cmd=onga_mwc_purchase"+
                "&order_id="+generateRandom()+"&meal_id="+meal_id+"&user_id="+Integer.parseInt(userid));
        Toast.makeText(getActivity(),"Succesfully Ordered",Toast.LENGTH_SHORT).show();

    }

    public int generateRandom(){
        return (int)(Math.random()*9000)+1000;
    }



    @Override
    public void onRefresh() {
        task = new DownloadAvailableFood();
        usersList.clear();
        operations("fetch_all_food", task);
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
                            String meal_id = jObj.getString(MEAL_ID);
                            String status = convertStatus(jObj.getString(MEAL_STATUS));

                            HashMap<String, String> foodDetails = new HashMap<>();

                            foodDetails.put(MEAL, meal_name);
                            foodDetails.put(MEAL_ID, meal_id);
                            foodDetails.put(PRICE, meal_price);
                            foodDetails.put(MEAL_STATUS, status);

                            usersList.add(foodDetails);
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
            String status = "Available";
            if(mealId.equals("1")){
                status = "Not Available";
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
            CustomListAdapter cadapter = new CustomListAdapter(getActivity(),usersList);
            setListAdapter(cadapter);
            swipeRefreshLayout.setRefreshing(false);

        }

        private class CustomListAdapter extends BaseAdapter {

            private Activity activity;
            private List<HashMap<String, String>> data;
            private LayoutInflater inflater=null;

            public CustomListAdapter (Activity a, List<HashMap<String, String>> d) {
                activity = a;
                data=d;
                inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            }

            public int getCount() {
                return data.size();
            }

            public Object getItem(int position) {
                return position;
            }

            public long getItemId(int position) {
                return position;
            }

            public class ViewHolder{

                public TextView itemName;
                public TextView itemPrice;
                public TextView itemStatus;
                public ImageView icon;
                public FloatingActionButton addBtn;

            }



            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                View vi=convertView;
                ViewHolder holder;
                System.out.println(parent);
                if(convertView==null){
                    vi = inflater.inflate(R.layout.list_row, null);

                    holder = new ViewHolder();
                    holder.itemName = (TextView) vi.findViewById(R.id.item_name);
                    holder.itemPrice = (TextView) vi.findViewById(R.id.item_price);
                    holder.itemStatus = (TextView) vi.findViewById(R.id.item_status);
                    holder.icon = (ImageView) vi.findViewById(R.id.icon);
                    holder.addBtn = (FloatingActionButton) vi.findViewById(R.id.add_btn);

                    vi.setTag(holder);
                }else {
                    holder = (ViewHolder)vi.getTag();
                }

                if(data.size() <= 0){
                    holder.itemName.setText("No Data");
                }else {
                    HashMap<String, String> listItem ;
                    listItem = data.get(position);

                    holder.itemName.setText(listItem.get(MEAL));
                    holder.itemPrice.setText(listItem.get(PRICE));
                    holder.itemStatus.setText(listItem.get(MEAL_STATUS));


                    // Handling event on button click
                    holder.addBtn.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            addfood(new DownloadAvailableFood(),usersList.get(position));
                        }
                    });

                }
                return vi;
            }

        }


    }




}
