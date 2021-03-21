package com.example.servipro_engg.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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
import com.example.servipro_engg.MySingletone;
import com.example.servipro_engg.R;
import com.example.servipro_engg.app.BaseApp;
import com.example.servipro_engg.pref.WritePref;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity
{
    //Creating views
    private TextInputEditText input_username,input_email,input_mobile,input_password;
    private EditText editTextOtpEmail,editTextOtpMobile;
    private TextView verifyEmail,verifyMobile;
    private ImageView emailCheck,mobileCheck;
    private AppCompatButton btn_register,buttonConfirm;
    private Boolean exit = false;

    String DyUrl;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    String email;String UserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        try {
            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("MyPref", 0);
            DyUrl = sharedPreferences.getString("NewUrl", null);
        } catch (Exception e){
            Log.d("HHHHHHH = ",""+e);
            Toast.makeText(this, "Error Login= " + e, Toast.LENGTH_SHORT).show();
        }

        input_username = findViewById(R.id.input_username);
        input_email = findViewById(R.id.input_email);
        input_mobile = findViewById(R.id.input_mobile);
        input_password = findViewById(R.id.input_password);
        btn_register = findViewById(R.id.btn_register);
    }

    public void onRegister(View view)
    {
        if (input_username.getText().toString().length() == 0){
            input_username.setError("Enter Username");
            input_username.requestFocus();
        }
        else if (input_email.getText().toString().length() == 0){
            input_email.setError("Enter Email");
            input_email.requestFocus();
        }
        else if (input_mobile.getText().toString().length() == 0){
            input_mobile.setError("Enter Mobile No");
            input_mobile.requestFocus();
        }
        else if (input_mobile.getText().toString().length() != 10){
            input_mobile.setError("Enter Ten Digit Number");
            input_mobile.requestFocus();
        }
        else if (input_password.getText().toString().length() == 0){
            input_password.setError("Enter Password");
            input_password.requestFocus();
        }
        else
        {
            email = input_email.getText().toString().trim();

            if (email.matches(emailPattern))
            {
                registerUser();
            }
            else
            {
                Toast.makeText(getApplicationContext(),"Invalid email",Toast.LENGTH_SHORT).show();
                input_email.setError("Enter Valid Email");
                input_email.requestFocus();
            }
        }
    }

    private void registerUser()
    {
        final ProgressDialog loading = ProgressDialog.show(Register.this, "Authenticating", "Please wait while we check the entered code", false,false);
        String URL=DyUrl+"user_register.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject objres = new JSONObject(response);
                    String status = objres.getString("Status");

                    if (status.equals("200")){
                        loading.dismiss();
                        UserId = objres.getString("User_id");
                        Toast.makeText(getApplicationContext(),"The OTP Send to Respected Mobile No and Email, Please Authenticate it..",Toast.LENGTH_LONG).show();
                        ConfirmOtp();
                    }
                    else if (status.equals("600")){
                        loading.dismiss();
                        Toast.makeText(getApplicationContext(),"Already Registered Email...",Toast.LENGTH_SHORT).show();
                    }
                    else if (status.equals("400")){
                        loading.dismiss();
                        Toast.makeText(getApplicationContext(),"Registration  Failed...",Toast.LENGTH_SHORT).show();
                    }
                    else if (status.equals("800")){
                        loading.dismiss();
                        Toast.makeText(getApplicationContext(),"Already Registered Mobile No...",Toast.LENGTH_SHORT).show();
                    }
                    else if (status.equals("2100")){
                        loading.dismiss();
                        Toast.makeText(getApplicationContext(),"Register Failed!!! Limited User access...",Toast.LENGTH_SHORT).show();
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
                params.put("User_Name",input_username.getText().toString());
                params.put("User_Email",input_email.getText().toString());
                params.put("User_Mobile",input_mobile.getText().toString());
                params.put("User_Password",input_password.getText().toString());
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy( 5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingletone.getInstance(Register.this).addToRequestQueue(stringRequest);

    }

    private void ConfirmOtp() {
        LayoutInflater li = LayoutInflater.from(this);
        View confirmDialog = li.inflate(R.layout.dialog_confirm, null);

        editTextOtpEmail = confirmDialog.findViewById(R.id.editTextOtpEmail);
        editTextOtpMobile = confirmDialog.findViewById(R.id.editTextOtpMobile);
        verifyEmail = confirmDialog.findViewById(R.id.verifyEmail);
        verifyMobile = confirmDialog.findViewById(R.id.verifyMobile);
        emailCheck = confirmDialog.findViewById(R.id.emailCheck);
        mobileCheck = confirmDialog.findViewById(R.id.mobileCheck);
        buttonConfirm = confirmDialog.findViewById(R.id.buttonConfirm);

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setView(confirmDialog);
        final AlertDialog alertDialog = alert.create();
        alertDialog.show();
        alertDialog.setCancelable(false);

        verifyEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextOtpEmail.getText().toString().length() == 0){
                    editTextOtpEmail.setError("Enter OTP");
                    editTextOtpEmail.requestFocus();
                }
                else {
                    final ProgressDialog loading = ProgressDialog.show(Register.this, "Authenticating", "Please wait while we check the entered code", false,false);
                    String URL=DyUrl+"Email_Verify.php";
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject objres = new JSONObject(response);
                                String status = objres.getString("Status");

                                if (status.equals("200")){
                                    loading.dismiss();
                                    Toast.makeText(getApplicationContext(),"Email Verify Success...",Toast.LENGTH_SHORT).show();
                                    editTextOtpEmail.setEnabled(false);
                                    verifyEmail.setVisibility(View.GONE);
                                    emailCheck.setVisibility(View.VISIBLE);
                                    if ((mobileCheck.getVisibility()==View.VISIBLE) && (emailCheck.getVisibility()==View.VISIBLE))
                                    {
                                        buttonConfirm.setVisibility(View.VISIBLE);
                                    }
                                }
                                else if (status.equals("400")){
                                    loading.dismiss();
                                    Toast.makeText(getApplicationContext(),"Verify Failed...",Toast.LENGTH_SHORT).show();
                                }
                                else if (status.equals("600")){
                                    loading.dismiss();
                                    Toast.makeText(getApplicationContext(),"Wrong OTP,Please Write correct one...",Toast.LENGTH_SHORT).show();
                                }
                                else if (status.equals("800")){
                                    loading.dismiss();
                                    Toast.makeText(getApplicationContext(),"Parameter Missing!!!",Toast.LENGTH_SHORT).show();
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
                            params.put("User_Email",input_email.getText().toString());
                            params.put("User_id",""+UserId);
                            params.put("Email_Otp",editTextOtpEmail.getText().toString());
                            return params;
                        }
                    };
                    stringRequest.setRetryPolicy(new DefaultRetryPolicy( 5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    MySingletone.getInstance(Register.this).addToRequestQueue(stringRequest);
                }

            }
        });
        verifyMobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextOtpMobile.getText().toString().length() == 0){
                    editTextOtpMobile.setError("Enter OTP");
                    editTextOtpMobile.requestFocus();
                }
                else{
                    final ProgressDialog loading = ProgressDialog.show(Register.this, "Authenticating", "Please wait while we check the entered code", false,false);
                    String URL=DyUrl+"Mobile_Verify.php";
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject objres = new JSONObject(response);
                                String status = objres.getString("Status");

                                if (status.equals("200")){
                                    loading.dismiss();
                                    Toast.makeText(getApplicationContext(),"Mobile No Verify Success...",Toast.LENGTH_SHORT).show();
                                    editTextOtpMobile.setEnabled(false);
                                    verifyMobile.setVisibility(View.GONE);
                                    mobileCheck.setVisibility(View.VISIBLE);

                                    if ((mobileCheck.getVisibility()==View.VISIBLE) && (emailCheck.getVisibility()==View.VISIBLE))
                                    {
                                        buttonConfirm.setVisibility(View.VISIBLE);
                                    }
                                }
                                else if (status.equals("400")){
                                    loading.dismiss();
                                    Toast.makeText(getApplicationContext(),"Verify Failed...",Toast.LENGTH_SHORT).show();
                                }
                                else if (status.equals("600")){
                                    loading.dismiss();
                                    Toast.makeText(getApplicationContext(),"Wrong OTP,Please Write correct one...",Toast.LENGTH_SHORT).show();
                                }
                                else if (status.equals("800")){
                                    loading.dismiss();
                                    Toast.makeText(getApplicationContext(),"Parameter Missing!!!",Toast.LENGTH_SHORT).show();
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
                            params.put("User_Email",input_email.getText().toString());
                            params.put("User_id",""+UserId);
                            params.put("Mobile_Otp",editTextOtpMobile.getText().toString());
                            return params;
                        }
                    };
                    stringRequest.setRetryPolicy(new DefaultRetryPolicy( 5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    MySingletone.getInstance(Register.this).addToRequestQueue(stringRequest);
                }
            }
        });
        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                final ProgressDialog loading = ProgressDialog.show(Register.this, "Authenticating", "Please wait while we check the entered code", false,false);
                String URL=DyUrl+"Active_User.php";
                StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject objres = new JSONObject(response);
                            String status = objres.getString("Status");

                            if (status.equals("200")){
                                loading.dismiss();
                                Toast.makeText(getApplicationContext(),"Register Successfully...",Toast.LENGTH_SHORT).show();

                                WritePref writePref = BaseApp.getInstance().getWritePref();
                                writePref.saveUserRegisterStatus(true);

                                Intent intent = new Intent(Register.this, Login.class);
                                startActivity(intent);
                                finish();
                            }
                            else if (status.equals("400")){
                                loading.dismiss();
                                Toast.makeText(getApplicationContext(),"Registration Failed...",Toast.LENGTH_SHORT).show();
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
                        params.put("User_Email",input_email.getText().toString());
                        params.put("User_id",""+UserId);
                        return params;
                    }
                };
                stringRequest.setRetryPolicy(new DefaultRetryPolicy( 5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                MySingletone.getInstance(Register.this).addToRequestQueue(stringRequest);

            }
        });
    }

    public void isLoginAll(View view)
    {
        WritePref writePref = BaseApp.getInstance().getWritePref();
        writePref.saveUserRegisterStatus(true);

        Intent intent = new Intent(Register.this, Login.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
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
