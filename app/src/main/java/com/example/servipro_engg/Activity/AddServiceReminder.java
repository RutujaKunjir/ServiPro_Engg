package com.example.servipro_engg.Activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.widget.CompoundButtonCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
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
import android.widget.Spinner;
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
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.servipro_engg.Adapter.ProductAddAdapter;
import com.example.servipro_engg.Adapter.ServiceAdapter;
import com.example.servipro_engg.List.ProductAddList;
import com.example.servipro_engg.List.ServiceList;
import com.example.servipro_engg.List.TaskList;
import com.example.servipro_engg.MySingletone;
import com.example.servipro_engg.R;
import com.example.servipro_engg.app.BaseApp;
import com.example.servipro_engg.pref.ReadPref;
import com.example.servipro_engg.utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AddServiceReminder extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener
{
    private TextView cust_name_txt,cust_email_txt,cust_mobile_txt,cust_address_txt,
            service_call_txt,service_date_txt,service_time_txt,service_detail_txt,service_amc_txt,
            productName,productPrice,productQty,totalAmount,total_amount_collection;
    private Button btn_Add;
    private EditText model_name,rouv_name,service_update_time_txt,service_update_date_txt;
    private TaskList mData;
    private Switch switch1;
    private RecyclerView product_listRecyclerView;
    private LinearLayout totCalDe;
    private View viewBB;
    String DyUrl,proName,proPrice,proQty,proSingTot;
    private List<ProductAddList> productAddList;
    private ProgressDialog dialog;
    private ProductAddAdapter productAddAdapter;
    private Calendar activeCalendar;
    private EditText activeEditText,comment_txt;
    private Calendar calendarFrom;
    private SimpleDateFormat sdf;
    Animation blinkText;
    Calendar calendar;
    int currentHour;
    String amPm="",myFormat,Amc_Id = "1",Amc__type_Id;
    int currentMinute,subTotr=0;
    TimePickerDialog timePickerDialog;
    private Spinner year_spinner,freq_spinner;
    private ReadPref mReadPref;
    public static Button attend_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_service_reminder);

        /*dialog = new ProgressDialog(this);
        dialog.setMessage("please wait..");
        dialog.setCancelable(false);
        dialog.show();*/

        Log.v("AAAAAAAAA",""+ BaseApp.getInstance());
        mReadPref = BaseApp.getInstance().getReadPref();

        totCalDe = findViewById(R.id.totCalDe);
        year_spinner = findViewById(R.id.year_spinner);
        freq_spinner = findViewById(R.id.freq_spinner);
        viewBB = findViewById(R.id.viewBB);
        attend_btn = findViewById(R.id.attend_btn);
        service_update_date_txt = findViewById(R.id.service_update_date_txt);
        service_update_time_txt = findViewById(R.id.service_update_time_txt);
        cust_name_txt = findViewById(R.id.cust_name_txt);
        cust_email_txt = findViewById(R.id.cust_email_txt);
        cust_mobile_txt = findViewById(R.id.cust_mobile_txt);
        cust_address_txt = findViewById(R.id.cust_address_txt);
        service_call_txt = findViewById(R.id.service_call_txt);
        service_date_txt = findViewById(R.id.service_date_txt);
        service_time_txt = findViewById(R.id.service_time_txt);
        service_detail_txt = findViewById(R.id.service_detail_txt);
        service_amc_txt = findViewById(R.id.service_amc_txt);
        model_name = findViewById(R.id.model_name);
        rouv_name = findViewById(R.id.rouv_name);
        comment_txt = findViewById(R.id.comment_txt);
        switch1 = findViewById(R.id.switch1);
        total_amount_collection = findViewById(R.id.total_amount_collection);

        blinkText = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.blink);

        getPrevData();

        model_name.setEnabled(false); rouv_name.setEnabled(false);

        calendarFrom = Calendar.getInstance();
        myFormat="yyyy-MM-dd";
        sdf = new SimpleDateFormat(myFormat, Locale.US);
        try {
            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("MyPref", 0);
            DyUrl = sharedPreferences.getString("NewUrl", null);
        } catch (Exception e){
            Log.d("HHHHHHH = ",""+e);
            Toast.makeText(this, "Error Login= " + e, Toast.LENGTH_SHORT).show();
        }

        productAddList = new ArrayList<>();

        product_listRecyclerView = findViewById(R.id.product_list);
        product_listRecyclerView.setHasFixedSize(true);
        product_listRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        service_update_date_txt.setOnClickListener(this);
        service_update_time_txt.setOnClickListener(this);

        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    product_listRecyclerView.setVisibility(View.VISIBLE);
                } else {
                    product_listRecyclerView.setVisibility(View.GONE);
                }
            }
        });

       // getTaskList();

        cust_email_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(Intent.ACTION_SEND);
                intent1.setData(Uri.parse("email:"+cust_email_txt.getText().toString()));
                intent1.setType("message/rfc822");
                getApplicationContext().startActivity(Intent.createChooser(intent1,"Choose an email client"));
            }
        });

        cust_mobile_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+ cust_mobile_txt.getText().toString()));
                getApplicationContext().startActivity(intent);
            }
        });

        cust_address_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent (Intent.ACTION_VIEW);
                intent2.setData(Uri.parse("http://maps.google.co.in/maps?q=" + cust_address_txt.getText().toString()));
                getApplicationContext().startActivity(intent2);

            }
        });

    }

    private void getPrevData()
    {
        Intent intent = getIntent();
        mData = intent.getParcelableExtra(Constants.TASK_DATA);

       // Log.v("Date & Time",""+mData.getService_Date()+" T = "+mData.getService_Time());

        cust_name_txt.setText(mData.getCust_Name());
        cust_email_txt.setText(mData.getCust_Email());
        cust_mobile_txt.setText(mData.getCust_Mobile1());
        cust_address_txt.setText(mData.getCust_Address());
        service_call_txt.setText(mData.getCall_No());
        service_date_txt.setText(mData.getService_Date());
        service_time_txt.setText(mData.getService_Time());
        service_detail_txt.setText(mData.getComment());
        model_name.setText(mData.getModel_Name());
        service_amc_txt.setText(mData.getAmc_Details());
        rouv_name.setText(mData.getROUV());
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
      //  alertDialog.setCancelable(false);

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
                    //  sum = sum + Integer.parseInt(totalAmount.getText().toString());
                    ProductAddList productList = new ProductAddList(
                            ""+productName.getText().toString(),
                            ""+productPrice.getText().toString(),
                            ""+productQty.getText().toString(),
                            ""+totalAmount.getText().toString()
                    );
                    productAddList.add(productList);

                    calculateData();

                    productAddAdapter = new ProductAddAdapter(productAddList, AddServiceReminder.this);
                    product_listRecyclerView.setAdapter(productAddAdapter);

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
                    // total_amount_collection.setText(""+sum);
                }
            }
        });

    }

    public void calculateData(){
        String pName = ""; String pPrice = "";  String pQty = "";  String psinTot = "";
        int sum=0;

        for (int ik = 0; ik < productAddList.size(); ik++) {

            pQty = productAddList.get(ik).getProductQty() + "%" +pQty;
            pPrice = productAddList.get(ik).getProductPrice()+ "%" +pPrice;
            pName = productAddList.get(ik).getProductName()+ "%" +pName;
            psinTot = productAddList.get(ik).getTotalAmount()+ "%" +psinTot;

            sum = sum + Integer.parseInt(productAddList.get(ik).getTotalAmount());
        }

        if (pQty.contains("%")){
            proQty = pQty.substring(0,pQty.lastIndexOf("%"));
            proPrice = pPrice.substring(0,pPrice.lastIndexOf("%"));
            proName = pName.substring(0,pName.lastIndexOf("%"));
            proSingTot = psinTot.substring(0,psinTot.lastIndexOf("%"));
        }


        total_amount_collection.setText(""+sum);

        Log.v("proQty=",""+proQty);
        Log.v("proPrice=",""+proPrice);
        Log.v("proName=",""+proName);
        Log.v("proSingTot=",""+proSingTot);

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

    @Override
    public void onClick(View view) {
        if (view == service_update_date_txt){
            activeCalendar = calendarFrom;
            service_update_date_txt.setTextColor(Color.BLACK);
            service_update_date_txt.clearAnimation();
            activeEditText = service_update_date_txt;

            new DatePickerDialog(AddServiceReminder.this, this,
                    activeCalendar.get(Calendar.YEAR),
                    activeCalendar.get(Calendar.MONTH),
                    activeCalendar.get(Calendar.DAY_OF_MONTH)).show();
        }

        if (view == service_update_time_txt){
            calendar = Calendar.getInstance();
            currentHour = calendar.get(Calendar.HOUR_OF_DAY);
            currentMinute = calendar.get(Calendar.MINUTE);

            timePickerDialog = new TimePickerDialog(AddServiceReminder.this, new TimePickerDialog.OnTimeSetListener() {
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
                    service_update_time_txt.setTextColor(Color.BLACK);
                    service_update_time_txt.clearAnimation();
                    service_update_time_txt.setText(String.format("%02d:%02d", hourOfDay, minutes) + amPm);
                    // pickTime.setText(String.format("%02d:%02d:", hourOfDay, minutes) + "00");
                    // time = String.format("%02d:%02d:", hourOfDay, minutes) + "00";

                    //  Toast.makeText(getApplicationContext(),"Time = "+time,Toast.LENGTH_SHORT).show();
                }
            }, currentHour, currentMinute, false);

            timePickerDialog.show();
        }

    }

    public static Date addMonth(Date date, int i) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, i);
        return cal.getTime();
    }

    public void addMachineAmc(View view)
    {
        if (model_name.getText().toString().length() == 0){
            model_name.setError("Enter Model");
            model_name.requestFocus();
        }
        else if (rouv_name.getText().toString().length() == 0){
            rouv_name.setError("Enter Sub Model");
            rouv_name.requestFocus();
        }
        else if (service_update_date_txt.getText().toString().length() == 0){
            service_update_date_txt.setTextColor(Color.RED);
            service_update_date_txt.setText("Select Date");
            service_update_date_txt.startAnimation(blinkText);
        }
        else if (service_update_time_txt.getText().toString().length() == 0){
            service_update_time_txt.setTextColor(Color.RED);
            service_update_time_txt.setText("Select Time");
            service_update_time_txt.startAnimation(blinkText);
        }
        else if (comment_txt.getText().toString().length() == 0){
            comment_txt.setError("Enter Comment");
            comment_txt.requestFocus();
        }
        else
        {
            addRequestAmc();
        }
    }

    private void addRequestAmc()
    {
        final ProgressDialog loading = ProgressDialog.show(AddServiceReminder.this, "Authenticating", "Please wait while we check the entered code", false,false);
        String URL=DyUrl+"addAmcServicing.php";
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
                        Intent intentTab = new Intent(AddServiceReminder.this, Signature.class);
                        intentTab.putExtra("Name",mData.getCust_Name());
                        intentTab.putExtra("service_call_no",mData.getCall_No());
                        startActivity(intentTab);
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
                params.put("Cust_id",""+mData.getCust_id());
                params.put("Model_Id",""+mData.getModel_Id());
                params.put("Amc_Id",""+Amc_Id);
                params.put("Status_id","3");
                params.put("Status_Of_Work","Attended");
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
                params.put("Comment",comment_txt.getText().toString());
                params.put("User_Name",""+mReadPref.getUser_Name());
                params.put("Service_Date",""+service_update_date_txt.getText().toString());
                params.put("Service_Time",""+service_update_time_txt.getText().toString());
                params.put("Amc__type_Id",""+Amc__type_Id);
                params.put("Service_call_no",""+mData.getCall_No());
                params.put("Amc_Call_No",""+mData.getComment());
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy( 5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingletone.getInstance(AddServiceReminder.this).addToRequestQueue(stringRequest);

    }

    @Override
    public void onBackPressed()
    {
        Intent intent2 = new Intent(this, ServiceReminder.class);
        startActivity(intent2);
        finish();
    }

}

