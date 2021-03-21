package com.example.servipro_engg.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.servipro_engg.List.TaskList;
import com.example.servipro_engg.R;
import com.example.servipro_engg.app.BaseApp;
import com.example.servipro_engg.pref.ReadPref;
import com.example.servipro_engg.pref.WritePref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Dashboard extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener
{
    private CardView card_1,card_2,card_3,card_4;
    private ReadPref mReadPref;
    private CircleImageView userprofile;
    private TextView username,notificationTxt,notification_count_reminder;
    private String ProUrl,DyUrl;
    private Boolean exit = false;
    private ProgressDialog dialog,progressDialog;
    private List<TaskList> mTaskList = new ArrayList<>();
    Integer Count,Count1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Log.v("AAAAAAAAA",""+BaseApp.getInstance());
        mReadPref = BaseApp.getInstance().getReadPref();

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.show();

        dialog = new ProgressDialog(this);
        dialog.setMessage("please wait..");
        dialog.setCancelable(false);
        dialog.show();

        checkActiveUser();

        try {
            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("MyPref", 0);
            ProUrl = sharedPreferences.getString("ProUrl", null);
            DyUrl = sharedPreferences.getString("NewUrl", null);
        } catch (Exception e){
            Log.d("HHHHHHH = ",""+e);
            // Toast.makeText(getApplicationContext(), "Error Login= " + e, Toast.LENGTH_SHORT).show();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);

        userprofile = (CircleImageView) header.findViewById(R.id.userprofile);
        username = (TextView) header.findViewById(R.id.username);
        notificationTxt = findViewById(R.id.notification_count_serv);
        notification_count_reminder = findViewById(R.id.notification_count_reminder);

        username.setText(mReadPref.getUser_Name());

        Log.v("IMAGE_NOT",""+ProUrl);
        Log.v("IMAGE_Yes",""+mReadPref.getUserProfile());

        if (mReadPref.getUserProfile().equals(""+ProUrl)) {
            // myProfile.setImageDrawable(R.drawable.profile);
            userprofile.setImageResource(R.drawable.profile);
            Log.v("IMAGE_NOT",""+ProUrl);
        }
        else {
            Glide.with(getApplicationContext())
                    .load(mReadPref.getUserProfile())
                    .into(userprofile);
        }

        card_1 = findViewById(R.id.card_1);
        card_2 = findViewById(R.id.card_2);
        card_3 = findViewById(R.id.card_3);
        card_4 = findViewById(R.id.card_4);

        card_1.setOnClickListener(this);
        card_2.setOnClickListener(this);
        card_3.setOnClickListener(this);
        card_4.setOnClickListener(this);

        getTaskList();
        getCustomerList();
    }

    @Override
    protected void onResume() {
        super.onResume();
        dialog.setMessage("please wait..");
        dialog.setCancelable(false);
        dialog.show();
        checkActiveUser();
        getTaskList();
        getCustomerList();
    }

    private void checkActiveUser()
    {
        final ReadPref readPref = BaseApp.getInstance().getReadPref();
        String URL=DyUrl+"isEngActive.php";
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try
                {
                    Log.v("RESPONSE_OPTION",""+response);
                    mTaskList.clear();
                    JSONObject objres=new JSONObject(response);

                    String status = objres.getString("Status");

                    if (status.equals("200")){
                        String isActive = objres.getString("isActive");
                        if (isActive.equals("1")){
                            progressDialog.dismiss();
                            Log.v("Active = ","T=Yes");
                        }
                        else{
                            progressDialog.setMessage("Your User Id is Currently Inactive... Please Contact Aceinvent!!!");
                            progressDialog.setCancelable(false);
                            progressDialog.show();
                           // Toast.makeText(getApplicationContext(),"Please Contact Aceinvent...", Toast.LENGTH_SHORT).show();
                        }
                    }
                    if (status.equals("400"))
                    {
                        //Toast.makeText(getApplicationContext(),"Send Failed", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    Log.v("ERROR SERVER", e.toString());
                    Toast.makeText(getApplicationContext(),"JSON Error",Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_SHORT).show();
                Log.v("VOLLEY", error.toString());
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    //  Toast.makeText(getApplicationContext(), "Timout Error", Toast.LENGTH_SHORT).show();
                } else if (error instanceof AuthFailureError) {
                    Toast.makeText(getApplicationContext(), "Requested Data is not Valid...", Toast.LENGTH_SHORT).show();
                } else if (error instanceof ServerError) {
                    Toast.makeText(getApplicationContext(), "Server take time to response, Please wait...", Toast.LENGTH_SHORT).show();
                } else if (error instanceof NetworkError) {
                    Toast.makeText(getApplicationContext(), "Network is Slow down, Please Wait...", Toast.LENGTH_SHORT).show();
                } else if (error instanceof ParseError) {
                    Toast.makeText(getApplicationContext(), "Parsering Problem, Please Wait...", Toast.LENGTH_SHORT).show();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params=new HashMap<String, String>();
                params.put("User_Id", ""+mReadPref.getUser_id());
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy( 5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else {
            if (exit) {
                finish(); // finish activity
            } else {
                Toast.makeText(this, "Press Back again to Exit.",
                        Toast.LENGTH_SHORT).show();
                exit = true;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        exit = false;
                    }
                }, 3 * 1000);

            }
        }
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            Intent intent = new Intent(this, Profile.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_setting)
        {

        }
        else if (id == R.id.nav_addreq)
        {
            Intent intent = new Intent(this, AddRequest.class);
            startActivity(intent);
            finish();
        }
        else if (id == R.id.nav_cust_list)
        {
            Intent intent2 = new Intent(this, CustomerList.class);
            startActivity(intent2);
            finish();
        }
        else if (id == R.id.nav_remind)
        {
            Intent intent3 = new Intent(this, ServiceReminder.class);
            //intent3.putExtra("TabOpen", "1");// One is your argument
            startActivity(intent3);
            finish();
        }
        else if (id == R.id.nav_pending)
        {
            Intent intent2 = new Intent(this, PendingCall.class);
            startActivity(intent2);
            finish();
        }
        else if (id == R.id.nav_logout)
        {
            WritePref writePref = BaseApp.getInstance().getWritePref();
            writePref.saveUserLoginStatus(false);
            Intent loginIntent = new Intent(this, Login.class);
            loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(loginIntent);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.card_1:
                Intent intent = new Intent(this, AddRequest.class);
                startActivity(intent);
                finish();
                break;
            case R.id.card_2:
                Intent intent2 = new Intent(this, PendingCall.class);
                startActivity(intent2);
                finish();
                break;
            case R.id.card_3:
                Intent intent3 = new Intent(this, ServiceReminder.class);
                //intent3.putExtra("TabOpen", "1");// One is your argument
                startActivity(intent3);
                finish();
                break;
            case R.id.card_4:
                Intent intent4 = new Intent(this, CustomerList.class);
                startActivity(intent4);
                finish();
                break;

        }
    }

    private void getTaskList()
    {
        final ReadPref readPref = BaseApp.getInstance().getReadPref();
        String URL=DyUrl+"getService.php";
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try
                {
                    Log.v("RESPONSE_OPTION",""+response);
                    dialog.dismiss();
                    mTaskList.clear();
                    JSONObject objres=new JSONObject(response);

                    String status = objres.getString("Status");

                    if (status.equals("200")){
                        String data = objres.getString("tax_data");
                        JSONArray jsonArray = new JSONArray(data);
                        for (int i=0; i<jsonArray.length();i++){
                            JSONObject object2 = jsonArray.getJSONObject(i);

                            mTaskList.add(new TaskList(
                                    ""+object2.getString("Call_No"),
                                    ""+object2.getString("Cust_id"),
                                    ""+object2.getString("Created_Date"),
                                    ""+object2.getString("Cust_Name"),
                                    ""+object2.getString("Cust_Email"),
                                    ""+object2.getString("Cust_Mobile1"),
                                    ""+object2.getString("Cust_Mobile2"),
                                    ""+object2.getString("Cust_Address"),
                                    ""+object2.getString("Model_Id"),
                                    ""+object2.getString("Model_Name"),
                                    ""+object2.getString("ROUV"),
                                    ""+object2.getString("Amc_Id"),
                                    ""+object2.getString("Amc_Details"),
                                    ""+object2.getString("Status_id"),
                                    ""+object2.getString("Status_Of_Work"),
                                    ""+object2.getString("Total_Collection"),
                                    ""+object2.getString("Comment"),
                                    ""+object2.getString("Service_Date"),
                                    ""+object2.getString("Service_Time")
                            ));

                        }

                        Count = mTaskList.size();
                        Integer cnt = 0;
                        if (Count > 0) {
                            for (int i = 0; i < Count; i++) {
                                cnt++;
                            }

                            if (cnt > 0) {
                                notificationTxt.setVisibility(View.VISIBLE);
                                notificationTxt.setText("" + cnt);
                                // notification_count_serv.setText(""+cnt);
                            }

                        }
                    }
                    if (status.equals("400"))
                    {
                        //Toast.makeText(getApplicationContext(),"Send Failed", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    Log.v("ERROR SERVER", e.toString());
                    Toast.makeText(getApplicationContext(),"JSON Error",Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_SHORT).show();
                Log.v("VOLLEY", error.toString());
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    //  Toast.makeText(getApplicationContext(), "Timout Error", Toast.LENGTH_SHORT).show();
                } else if (error instanceof AuthFailureError) {
                    Toast.makeText(getApplicationContext(), "Requested Data is not Valid...", Toast.LENGTH_SHORT).show();
                } else if (error instanceof ServerError) {
                    Toast.makeText(getApplicationContext(), "Server take time to response, Please wait...", Toast.LENGTH_SHORT).show();
                } else if (error instanceof NetworkError) {
                    Toast.makeText(getApplicationContext(), "Network is Slow down, Please Wait...", Toast.LENGTH_SHORT).show();
                } else if (error instanceof ParseError) {
                    Toast.makeText(getApplicationContext(), "Parsering Problem, Please Wait...", Toast.LENGTH_SHORT).show();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params=new HashMap<String, String>();
                params.put("Status_id", "1");
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy( 5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    private void getCustomerList()
    {
        final ReadPref readPref = BaseApp.getInstance().getReadPref();
        String URL=DyUrl+"getServviceReminder.php";
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    Log.v("RESPONSE",""+response);
                    JSONObject objres=new JSONObject(response);

                    if(mTaskList!=null && mTaskList.size()>0){
                        mTaskList.clear();
                    }

                    String status = objres.getString("Status");
                    String data = objres.getString("tax_data");

                    if (status.equals("200")){
                        dialog.dismiss();
                        JSONArray jsonArray = new JSONArray(data);
                        for (int i=0; i<jsonArray.length();i++){
                            JSONObject object2 = jsonArray.getJSONObject(i);
                            mTaskList.add(new TaskList(
                                    ""+object2.getString("Call_No"),
                                    ""+object2.getString("Cust_id"),
                                    ""+object2.getString("Created_Date"),
                                    ""+object2.getString("Cust_Name"),
                                    ""+object2.getString("Cust_Email"),
                                    ""+object2.getString("Cust_Mobile1"),
                                    ""+object2.getString("Cust_Mobile2"),
                                    ""+object2.getString("Cust_Address"),
                                    ""+object2.getString("Model_Id"),
                                    ""+object2.getString("Model_Name"),
                                    ""+object2.getString("ROUV"),
                                    ""+object2.getString("Amc_Id"),
                                    ""+object2.getString("Amc_Details"),
                                    ""+object2.getString("Status_id"),
                                    ""+object2.getString("Status_Of_Work"),
                                    ""+object2.getString("Status_id"),// not available
                                    ""+object2.getString("Amc_Call_No"),// pass Amc_Call_No
                                    ""+object2.getString("Service_Date"),
                                    ""+object2.getString("Service_Time")
                            ));
                        }

                        Count1 = mTaskList.size();
                        Integer cnt1 = 0;
                        if (Count1 > 0) {
                            for (int i = 0; i < Count1; i++) {
                                cnt1++;
                            }

                            if (cnt1 > 0) {
                                notification_count_reminder.setVisibility(View.VISIBLE);
                                notification_count_reminder.setText("" + cnt1);
                                // notification_count_serv.setText(""+cnt);
                            }

                        }

                    }
                    else {
                        dialog.dismiss();
                        // Toast.makeText(getContext(),"Item Not Available",Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    Log.v("ERROR SERVER", e.toString());
                    Toast.makeText(getApplicationContext(),"JSON Error",Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_SHORT).show();
                Log.v("VOLLEY", error.toString());
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    //  Toast.makeText(getApplicationContext(), "Timout Error", Toast.LENGTH_SHORT).show();
                } else if (error instanceof AuthFailureError) {
                    Toast.makeText(getApplicationContext(), "Requested Data is not Valid...", Toast.LENGTH_SHORT).show();
                } else if (error instanceof ServerError) {
                    Toast.makeText(getApplicationContext(), "Server take time to response, Please wait...", Toast.LENGTH_SHORT).show();
                } else if (error instanceof NetworkError) {
                    Toast.makeText(getApplicationContext(), "Network is Slow down, Please Wait...", Toast.LENGTH_SHORT).show();
                } else if (error instanceof ParseError) {
                    Toast.makeText(getApplicationContext(), "Parsering Problem, Please Wait...", Toast.LENGTH_SHORT).show();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params=new HashMap<String, String>();
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy( 5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

}
