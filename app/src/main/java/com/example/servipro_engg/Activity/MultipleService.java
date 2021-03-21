package com.example.servipro_engg.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.v4.widget.CompoundButtonCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
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
import com.example.servipro_engg.Adapter.MultipleAdapter;
import com.example.servipro_engg.List.TaskList;
import com.example.servipro_engg.R;
import com.example.servipro_engg.app.BaseApp;
import com.example.servipro_engg.pref.ReadPref;
import com.example.servipro_engg.utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MultipleService extends AppCompatActivity
{
    private SearchView svMovies;
    private Toolbar toolbar;
    private ProgressDialog dialog;
    String DyUrl;
    private List<TaskList> mTaskList;
    RecyclerView recyclerView;
    private MultipleAdapter taskAddAdapter;
    private CheckBox allCust,oneTime,amcDetial;
    private TaskList mData;
    ColorStateList colorStateList;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiple_service_list);

        Intent intent = getIntent();
        mData = intent.getParcelableExtra(Constants.TASK_DATA);

        svMovies = findViewById(R.id.svMovies);
        allCust = findViewById(R.id.allCust);
        oneTime = findViewById(R.id.oneTime);
        amcDetial = findViewById(R.id.amcDetial);
        toolbar = findViewById(R.id.toolbar);

        dialog = new ProgressDialog(this);
        dialog.setMessage("please wait..");
        dialog.setCancelable(false);
        dialog.show();

        try {
            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("MyPref", 0);
            DyUrl = sharedPreferences.getString("NewUrl", null);
        } catch (Exception e){
            Log.d("HHHHHHH = ",""+e);
            // Toast.makeText(LoginActivity.this, "Error Login= " + e, Toast.LENGTH_SHORT).show();
        }

        mTaskList = new ArrayList<>();

        recyclerView = findViewById(R.id.customer_list_detail);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(MultipleService.this,2));
        /*
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);*/


        getCustomerList();

        EditText searchEditText = (EditText) svMovies.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(getResources().getColor(R.color.white));
        searchEditText.setHintTextColor(getResources().getColor(R.color.white));

        svMovies.setActivated(true);
        svMovies.setQueryHint("Enter Text");
        svMovies.onActionViewExpanded();
        svMovies.setIconified(false);
        svMovies.clearFocus();

        svMovies.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                taskAddAdapter.getFilter().filter(newText);
                return false;
            }
        });


        colorStateList = new ColorStateList(
                new int[][]{
                        new int[]{-android.R.attr.state_checked}, // unchecked
                        new int[]{android.R.attr.state_checked} , // checked
                },
                new int[]{
                        Color.parseColor("#868686"),
                        Color.parseColor("#ffa900"),
                }
        );

        oneTime.setTextColor(getResources().getColor(R.color.dark_gray));
        CompoundButtonCompat.setButtonTintList(oneTime,colorStateList);
        amcDetial.setTextColor(getResources().getColor(R.color.dark_gray));
        CompoundButtonCompat.setButtonTintList(amcDetial,colorStateList);
        allCust.setTextColor(getResources().getColor(R.color.dark_gray));
        CompoundButtonCompat.setButtonTintList(allCust,colorStateList);

       /* oneTime.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if ( isChecked )
                {
                    getCustomerList();
                    // Toast.makeText(getApplicationContext(), "isChecked - " + checkBox.isChecked(), Toast.LENGTH_SHORT).show();
                }
                *//*else {
                    getCustomerList();
                }*//*

            }
        });
        amcDetial.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if ( isChecked )
                {
                    getCustomerList();
                    // Toast.makeText(getApplicationContext(), "isChecked - " + checkBox.isChecked(), Toast.LENGTH_SHORT).show();
                }
                *//*else {
                    getCustomerList();
                }*//*

            }
        });
        allCust.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if ( isChecked )
                {
                    getCustomerList();
                    // Toast.makeText(getApplicationContext(), "isChecked - " + checkBox.isChecked(), Toast.LENGTH_SHORT).show();
                }
                *//*else {
                    getCustomerList();
                }*//*

            }
        });*/

        oneTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (oneTime.isChecked()){
                    getCustomerList();
                    oneTime.setTextColor(getResources().getColor(R.color.dusk_yellow));
                    amcDetial.setChecked(false);
                    amcDetial.setTextColor(getResources().getColor(R.color.dark_gray));
                    allCust.setChecked(false);
                    allCust.setTextColor(getResources().getColor(R.color.dark_gray));
                }
                else {
                    oneTime.setTextColor(getResources().getColor(R.color.dark_gray));
                }
            }
        });

        allCust.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (allCust.isChecked()){
                    getCustomerList();
                    allCust.setTextColor(getResources().getColor(R.color.dusk_yellow));
                    amcDetial.setChecked(false);
                    amcDetial.setTextColor(getResources().getColor(R.color.dark_gray));
                    oneTime.setChecked(false);
                    oneTime.setTextColor(getResources().getColor(R.color.dark_gray));
                }
                else {
                    allCust.setTextColor(getResources().getColor(R.color.dark_gray));
                }
            }
        });

        amcDetial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (amcDetial.isChecked()){
                    getCustomerList();
                    amcDetial.setTextColor(getResources().getColor(R.color.dusk_yellow));
                    oneTime.setChecked(false);
                    oneTime.setTextColor(getResources().getColor(R.color.dark_gray));
                    allCust.setChecked(false);
                    allCust.setTextColor(getResources().getColor(R.color.dark_gray));
                }
                else {
                    amcDetial.setTextColor(getResources().getColor(R.color.dark_gray));
                }
            }
        });


    }

    public void showSerch(View view)
    {
        toolbar.setVisibility(View.GONE);
        svMovies.setVisibility(View.VISIBLE);
    }


    private void getCustomerList()
    {
        final ReadPref readPref = BaseApp.getInstance().getReadPref();
        String URL=DyUrl+"getCustmerAmcList.php";
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

                            if (allCust.isChecked()){
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
                            else if (oneTime.isChecked()){
                                if (object2.getString("Amc_Id").equals("2")){
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
                            }
                            else if (amcDetial.isChecked()){
                                if (object2.getString("Amc_Id").equals("1")){
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
                            }
                            else {
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

                        }
                    }
                    else {
                        dialog.dismiss();
                        // Toast.makeText(getContext(),"Item Not Available",Toast.LENGTH_SHORT).show();
                    }

                    taskAddAdapter = new MultipleAdapter(MultipleService.this,mTaskList);
                    recyclerView.setAdapter(taskAddAdapter);


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
                params.put("Cust_id",""+mData.getCust_id());
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy( 5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(MultipleService.this, CustomerList.class);
        startActivity(intent);
        finish();
    }

    public void deleteCustomer(View view)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(MultipleService.this);

        builder.setTitle("Alert");
        builder.setMessage("The Customer Record will delete permanently. Do you want to delete the Customer ??");
        builder.setCancelable(false);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deleteCustomer();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();

            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void deleteCustomer()
    {
        final ReadPref readPref = BaseApp.getInstance().getReadPref();
        String URL=DyUrl+"delete_customer.php";
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    Log.v("RESPONSE",""+response);
                    JSONObject objres=new JSONObject(response);


                    String status = objres.getString("Status");

                    if (status.equals("200")){
                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(),"Delete Success !!!",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MultipleService.this, CustomerList.class);
                        startActivity(intent);
                        finish();
                    }
                    else {
                        dialog.dismiss();
                         Toast.makeText(getApplicationContext(),"Delete Failed !!!",Toast.LENGTH_SHORT).show();
                    }

                    taskAddAdapter = new MultipleAdapter(MultipleService.this,mTaskList);
                    recyclerView.setAdapter(taskAddAdapter);


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
                params.put("Cust_id",""+mData.getCust_id());
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy( 5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

}
