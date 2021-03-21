package com.example.servipro_engg.Activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.widget.CompoundButtonCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.servipro_engg.Adapter.ProductAddAdapter;
import com.example.servipro_engg.List.ProductAddList;
import com.example.servipro_engg.MySingletone;
import com.example.servipro_engg.R;
import com.example.servipro_engg.app.BaseApp;
import com.example.servipro_engg.pref.ReadPref;
import com.example.servipro_engg.pref.WritePref;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AddRequest extends AppCompatActivity implements View.OnClickListener,DatePickerDialog.OnDateSetListener
{
    private EditText custoName,custoMobile1,custoMobile2,custoEmail,custoAddress;
    private EditText modelName,subModelName,productName,productPrice,productQty,totalAmount,comment_service;
    private TextView total_amount_collection;
    private Button btn_Add;
    private CheckBox oneTime,amcDetial;
    private RecyclerView productRecyclerView;
    private Switch switch1;
    private LinearLayout amcdetails,totCalDe;
    View viewBB;
    private ProductAddAdapter productAddAdapter;
    private List<ProductAddList>  productAddList;
    String Amc_Id, DyUrl, proName,proPrice,proQty,proSingTot,myFormat;
    private ReadPref mReadPref;
    int sum=0;
    private Calendar activeCalendar;
    private EditText activeEditText,service_date_txt,service_time_txt;
    private Calendar calendarFrom;
    private SimpleDateFormat sdf;
    Animation blinkText;
    Calendar calendar;
    int currentHour;
    String amPm="";
    int currentMinute,subTotr=0;
    TimePickerDialog timePickerDialog;
    ColorStateList colorStateList;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_request);

        Log.v("AAAAAAAAA",""+BaseApp.getInstance());
        mReadPref = BaseApp.getInstance().getReadPref();

        calendarFrom = Calendar.getInstance();
        myFormat="yyyy-MM-dd";
        sdf = new SimpleDateFormat(myFormat, Locale.US);
        blinkText = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.blink);

        try {
            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("MyPref", 0);
            DyUrl = sharedPreferences.getString("NewUrl", null);
        } catch (Exception e){
            Log.d("HHHHHHH = ",""+e);
            Toast.makeText(this, "Error Login= " + e, Toast.LENGTH_SHORT).show();
        }

        productAddList = new ArrayList<>();

        productRecyclerView = findViewById(R.id.product_list_detail);
        productRecyclerView.setHasFixedSize(true);
        productRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        modelName = findViewById(R.id.model_name);
        subModelName = findViewById(R.id.rouv_name);
        oneTime = findViewById(R.id.oneTime);
        amcDetial = findViewById(R.id.amcDetial);
        service_date_txt = findViewById(R.id.service_date_txt);
        service_time_txt = findViewById(R.id.service_time_txt);

        switch1 = findViewById(R.id.switch1);
        amcdetails = findViewById(R.id.amcdetails);
        totCalDe = findViewById(R.id.totCalDe);
        viewBB = findViewById(R.id.viewBB);
        comment_service = findViewById(R.id.comment_service);
        total_amount_collection = findViewById(R.id.total_amount_collection);
        custoName = findViewById(R.id.custoName);
        custoMobile1 = findViewById(R.id.custoMobile1);
        custoMobile2 = findViewById(R.id.custoMobile2);
        custoEmail = findViewById(R.id.custoEmail);
        custoAddress = findViewById(R.id.custoAddress);

        service_date_txt.setOnClickListener(this);
        service_time_txt.setOnClickListener(this);
        //service_date_txt.setEnabled(false);

        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    productRecyclerView.setVisibility(View.VISIBLE);
                } else {
                    productRecyclerView.setVisibility(View.GONE);
                }
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

        oneTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (oneTime.isChecked()){
                    oneTime.setTextColor(getResources().getColor(R.color.dusk_yellow));
                    amcDetial.setChecked(false);
                    amcDetial.setTextColor(getResources().getColor(R.color.dark_gray));
                }
                else {
                    oneTime.setTextColor(getResources().getColor(R.color.dark_gray));
                }
            }
        });

        amcDetial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (amcDetial.isChecked()){
                    amcDetial.setTextColor(getResources().getColor(R.color.dusk_yellow));
                    oneTime.setChecked(false);
                    oneTime.setTextColor(getResources().getColor(R.color.dark_gray));
                }
                else {
                    amcDetial.setTextColor(getResources().getColor(R.color.dark_gray));
                }
            }
        });


    }

    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(AddRequest.this, Dashboard.class);
        startActivity(intent);
        finish();
    }

    public void addProduct(View view)
    {
        LayoutInflater li = LayoutInflater.from(this);
        View confirmDialog = li.inflate(R.layout.add_product, null);

        productName = confirmDialog.findViewById(R.id.productName);
        productPrice = confirmDialog.findViewById(R.id.productPrice);
        productQty = confirmDialog.findViewById(R.id.productQty);
        totalAmount = confirmDialog.findViewById(R.id.totalAmount);
        btn_Add = confirmDialog.findViewById(R.id.btn_Add);

        totalAmount.setEnabled(false);

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setView(confirmDialog);
        final AlertDialog alertDialog = alert.create();
        alertDialog.show();
       // alertDialog.setCancelable(false);

        productPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try{
                    int price = Integer.parseInt(productPrice.getText().toString());
                    int qty = Integer.parseInt(productQty.getText().toString());
                    int tot = price * qty;
                    totalAmount.setText(""+tot);
                }
                catch (Exception e){e.printStackTrace();}
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        productQty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try{
                    int price = Integer.parseInt(productPrice.getText().toString());
                    int qty = Integer.parseInt(productQty.getText().toString());
                    int tot = price * qty;
                    totalAmount.setText(""+tot);
                }
                catch (Exception e){e.printStackTrace();}
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        btn_Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (productName.getText().toString().length() == 0){
                    productName.setError("Enter Product");
                    productName.requestFocus();
                }
                else if (productPrice.getText().toString().length() == 0){
                    productPrice.setError("Enter Price");
                    productPrice.requestFocus();
                }
                else if (productQty.getText().toString().length() == 0){
                    productQty.setError("Enter Quantity");
                    productQty.requestFocus();
                }
                else
                {
                    sum = sum + Integer.parseInt(totalAmount.getText().toString());
                    ProductAddList productList = new ProductAddList(
                           ""+productName.getText().toString(),
                            ""+productPrice.getText().toString(),
                            ""+productQty.getText().toString(),
                            ""+totalAmount.getText().toString()
                    );
                    productAddList.add(productList);

                    calculateData();

                    productAddAdapter = new ProductAddAdapter(productAddList, AddRequest.this);
                    productRecyclerView.setAdapter(productAddAdapter);

                    if(productAddList.size()>0){
                        switch1.setVisibility(View.VISIBLE);
                       // amcdetails.setVisibility(View.VISIBLE);
                        totCalDe.setVisibility(View.VISIBLE);
                        viewBB.setVisibility(View.VISIBLE);
                    }
                    else {
                        switch1.setVisibility(View.GONE);
                       // amcdetails.setVisibility(View.GONE);
                        totCalDe.setVisibility(View.GONE);
                        viewBB.setVisibility(View.GONE);
                    }

                    Toast.makeText(getApplicationContext(),"Product Added...",Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();
                    total_amount_collection.setText(""+sum);
                }
            }
        });

    }

    public void calculateData(){
        String pName = ""; String pPrice = "";  String pQty = "";  String psinTot = "";

        for (int ik = 0; ik < productAddList.size(); ik++) {

            pQty = productAddList.get(ik).getProductQty() + "%" +pQty;
            pPrice = productAddList.get(ik).getProductPrice()+ "%" +pPrice;
            pName = productAddList.get(ik).getProductName()+ "%" +pName;
            psinTot = productAddList.get(ik).getTotalAmount()+ "%" +psinTot;
        }

        if (pQty.contains("%")){
            proQty = pQty.substring(0,pQty.lastIndexOf("%"));
            proPrice = pPrice.substring(0,pPrice.lastIndexOf("%"));
            proName = pName.substring(0,pName.lastIndexOf("%"));
            proSingTot = psinTot.substring(0,psinTot.lastIndexOf("%"));
        }

        Log.v("proQty=",""+proQty);
        Log.v("proPrice=",""+proPrice);
        Log.v("proName=",""+proName);
        Log.v("proSingTot=",""+proSingTot);

    }

    public void onAddRequest(View view)
    {
        if (custoName.getText().toString().length() == 0){
            custoName.setError("Enter Customer Name");
            custoName.requestFocus();
        }
        else if (custoMobile1.getText().toString().length() == 0){
            custoMobile1.setError("Enter Customer Mobile No");
            custoMobile1.requestFocus();
        }
        else if (custoAddress.getText().toString().length() == 0){
            custoAddress.setError("Enter Customer Address");
            custoAddress.requestFocus();
        }
        else if (modelName.getText().toString().length() == 0){
            modelName.setError("Enter Model");
            modelName.requestFocus();
        }
        else if (subModelName.getText().toString().length() == 0){
            subModelName.setError("Enter Sub Model");
            subModelName.requestFocus();
        }
        else if((!oneTime.isChecked()) && (!amcDetial.isChecked()) ){
            Toast.makeText(getApplicationContext(),"Select Service Type...",Toast.LENGTH_SHORT).show();
        }
        else if (service_date_txt.getText().toString().length() == 0){
            service_date_txt.setTextColor(Color.RED);
            service_date_txt.setText("Select Date");
            service_date_txt.startAnimation(blinkText);
        }
        else if (service_time_txt.getText().toString().length() == 0){
            service_time_txt.setTextColor(Color.RED);
            service_time_txt.setText("Select Time");
            service_time_txt.startAnimation(blinkText);
        }
        else if (comment_service.getText().toString().length() == 0){
            comment_service.setError("Enter Comment");
            comment_service.requestFocus();
        }
        else
        {
            if (oneTime.isChecked()){ Amc_Id = "2";}
            if (amcDetial.isChecked()){ Amc_Id = "1";}

            addRequest();

        }
    }

    private void addRequest()
    {
        final ProgressDialog loading = ProgressDialog.show(AddRequest.this, "Authenticating", "Please wait while we check the entered code", false,false);
        String URL=DyUrl+"addRequest.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject objres = new JSONObject(response);
                    String status = objres.getString("Status");
                    Log.v("Response = ",""+response);

                    if (status.equals("200")){
                        loading.dismiss();
                        Toast.makeText(getApplicationContext(),"Add Request Successfully!!!",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AddRequest.this, Dashboard.class);
                        startActivity(intent);
                        finish();
                    }
                    else if (status.equals("400")){
                        loading.dismiss();
                        Toast.makeText(getApplicationContext(),"Add Request Failed.. Please try again!!!",Toast.LENGTH_SHORT).show();
                    }
                    else if (status.equals("100")){
                        loading.dismiss();
                        Toast.makeText(getApplicationContext(),"Parameter Missing!!! Please try again!!!",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        loading.dismiss();
                        Toast.makeText(getApplicationContext(),"Invalid Credentials",Toast.LENGTH_SHORT).show();
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
                params.put("Cust_Name",custoName.getText().toString());
                params.put("Cust_Email",custoEmail.getText().toString());
                params.put("Cust_Mobile1",custoMobile1.getText().toString());
                params.put("Cust_Mobile2",custoMobile2.getText().toString());
                params.put("Cust_Address",custoAddress.getText().toString());
                params.put("Model_Name",modelName.getText().toString());
                params.put("ROUV",subModelName.getText().toString());
                params.put("Amc_Id",""+Amc_Id);
                params.put("Status_id","1");
                params.put("Status_Of_Work","Open");
                if (productAddList.size()==0){
                    params.put("Product_Name","");
                    params.put("Product_Price","");
                    params.put("Product_Qunty","");
                    params.put("Single_Pro_Tot","");
                }
                else{
                    params.put("Product_Name",""+proName);
                    params.put("Product_Price",""+proPrice);
                    params.put("Product_Qunty",""+proQty);
                    params.put("Single_Pro_Tot",""+proSingTot);
                }
                params.put("Total_Collection",""+total_amount_collection.getText().toString());
                params.put("Comment",comment_service.getText().toString());
                params.put("User_Name",""+mReadPref.getUser_Name());
                params.put("Service_Date",""+service_date_txt.getText().toString());
                params.put("Service_Time",""+service_time_txt.getText().toString());
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy( 5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingletone.getInstance(AddRequest.this).addToRequestQueue(stringRequest);

    }

    @Override
    public void onClick(View view) {
        if (view == service_date_txt){
            activeCalendar = calendarFrom;
            service_date_txt.setTextColor(Color.BLACK);
            service_date_txt.clearAnimation();
            activeEditText = service_date_txt;

            new DatePickerDialog(AddRequest.this, this,
                    activeCalendar.get(Calendar.YEAR),
                    activeCalendar.get(Calendar.MONTH),
                    activeCalendar.get(Calendar.DAY_OF_MONTH)).show();
        }

        if (view == service_time_txt){
            calendar = Calendar.getInstance();
            currentHour = calendar.get(Calendar.HOUR_OF_DAY);
            currentMinute = calendar.get(Calendar.MINUTE);

            timePickerDialog = new TimePickerDialog(AddRequest.this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                    String time = String.format("%02d:%02d:", hourOfDay, minutes) + "00";
                    if (hourOfDay == 0) {
                        hourOfDay += 12;
                        amPm = "AM";
                    } else if (hourOfDay == 12) {
                        amPm = "PM";
                    } else if (hourOfDay > 12) {
                        hourOfDay -= 12;
                        amPm = "PM";
                    } else {
                        amPm = "AM";
                    }
                    service_time_txt.setTextColor(Color.BLACK);
                    service_time_txt.clearAnimation();
                    service_time_txt.setText(String.format("%02d:%02d", hourOfDay, minutes) + amPm);
                    // pickTime.setText(String.format("%02d:%02d:", hourOfDay, minutes) + "00");
                    // time = String.format("%02d:%02d:", hourOfDay, minutes) + "00";

                    //  Toast.makeText(getApplicationContext(),"Time = "+time,Toast.LENGTH_SHORT).show();
                }
            }, currentHour, currentMinute, false);

            timePickerDialog.show();
        }

    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
        // TODO Auto-generated method stub
        activeCalendar.set(Calendar.YEAR, year);
        activeCalendar.set(Calendar.MONTH, monthOfYear);
        activeCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        if (activeEditText != null) {
            activeEditText.setText(sdf.format(activeCalendar.getTime()));
        }
    }
}
