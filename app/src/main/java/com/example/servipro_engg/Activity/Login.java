package com.example.servipro_engg.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

public class Login extends AppCompatActivity
{
    private TextInputEditText input_email,input_password;
    String DyUrl;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    String email;
    private Boolean exit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        try {
            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("MyPref", 0);
            DyUrl = sharedPreferences.getString("NewUrl", null);
        } catch (Exception e){
            Log.d("HHHHHHH = ",""+e);
            Toast.makeText(this, "Error Login= " + e, Toast.LENGTH_SHORT).show();
        }

        input_email = findViewById(R.id.input_email);
        input_password = findViewById(R.id.input_password);

    }

    public void forgotPassword(View view)
    {
        Intent intent = new Intent(Login.this, ForgotPassword.class);
        startActivity(intent);
        finish();
    }

    public void onLogin(View view)
    {
        if (input_email.getText().toString().length() == 0){
            input_email.setError("Enter Email");
            input_email.requestFocus();
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
                loginUser();
            }
            else
            {
                Toast.makeText(getApplicationContext(),"Invalid email",Toast.LENGTH_SHORT).show();
                input_email.setError("Enter Valid Email");
                input_email.requestFocus();
            }
        }
    }

    private void loginUser()
    {
        final ProgressDialog loading = ProgressDialog.show(Login.this, "Authenticating", "Please wait while we check the entered code", false,false);
        String URL=DyUrl+"user_login.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject objres = new JSONObject(response);
                    String status = objres.getString("Status");
                    Log.v("Response = ",""+response);

                    if (status.equals("200")){
                        loading.dismiss();
                        Toast.makeText(getApplicationContext(),"Login Successfully!!!",Toast.LENGTH_SHORT).show();

                        WritePref writePref = BaseApp.getInstance().getWritePref();
                        writePref.saveUserLoginStatus(true);
                        writePref.saveUserId(""+objres.getString("User_id"));
                        writePref.saveUserName(""+objres.getString("User_Name"));
                        writePref.saveUserEmail(""+objres.getString("User_Email"));
                        writePref.saveUserMobile(""+objres.getString("User_Mobile"));
                        writePref.saveroleId(""+objres.getString("Role_id"));
                        writePref.saveisActive(""+objres.getString("isActive"));
                        writePref.saveUserAddress(""+objres.getString("Address"));
                        writePref.saveUserProfile(""+objres.getString("Profile"));

                        Intent intent = new Intent(Login.this, Dashboard.class);
                        startActivity(intent);
                        finish();
                    }
                    else if (status.equals("600")){
                        loading.dismiss();
                        Toast.makeText(getApplicationContext(),"User is not active yet... Please Contact Admin!!!",Toast.LENGTH_SHORT).show();
                    }
                    else if (status.equals("400")){
                        loading.dismiss();
                        Toast.makeText(getApplicationContext(),"Wrong Password... Please try again!!!",Toast.LENGTH_SHORT).show();
                    }
                    else if (status.equals("800")){
                        loading.dismiss();
                        Toast.makeText(getApplicationContext(),"Login credentials are wrong. Please try again!!!",Toast.LENGTH_SHORT).show();
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
                params.put("User_Password",input_password.getText().toString());
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy( 5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingletone.getInstance(Login.this).addToRequestQueue(stringRequest);

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
