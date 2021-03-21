package com.example.servipro_engg.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
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
import com.bumptech.glide.Glide;
import com.example.servipro_engg.MySingletone;
import com.example.servipro_engg.R;
import com.example.servipro_engg.app.BaseApp;
import com.example.servipro_engg.pref.ReadPref;
import com.example.servipro_engg.pref.WritePref;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile extends AppCompatActivity
{
    private CircleImageView profile;
    private TextView userTname,mobileTName;
    private EditText userNamePro,useremailPro,userMobilepro,userAddres;
    String DyUrl,ProUrl;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    String email;
    private ReadPref mReadPref;
    String imagePath = null;
    private Bitmap bitmap;
    private static int RESULT_LOAD_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Log.v("AAAAAAAAA",""+BaseApp.getInstance());
        mReadPref = BaseApp.getInstance().getReadPref();

        try {
            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("MyPref", 0);
            DyUrl = sharedPreferences.getString("NewUrl", null);
            ProUrl = sharedPreferences.getString("ProUrl", null);
        } catch (Exception e){
            Log.d("HHHHHHH = ",""+e);
            Toast.makeText(this, "Error Login= " + e, Toast.LENGTH_SHORT).show();
        }

        profile = findViewById(R.id.profile);
        userTname = findViewById(R.id.userTname);
        mobileTName = findViewById(R.id.mobileTName);
        userNamePro = findViewById(R.id.userNamePro);
        useremailPro = findViewById(R.id.useremailPro);
        userMobilepro = findViewById(R.id.userMobilepro);
        userAddres = findViewById(R.id.userAddres);

        userTname.setText(mReadPref.getUser_Name());
        userNamePro.setText(mReadPref.getUser_Name());
        mobileTName.setText(mReadPref.getUser_Mobile());
        userMobilepro.setText(mReadPref.getUser_Mobile());
        useremailPro.setText(mReadPref.getUser_Email());
        userAddres.setText(mReadPref.getUserAddress());

        Log.v("IMAGE_NOT",""+ProUrl);
        Log.v("IMAGE_Yes",""+mReadPref.getUserProfile());

        if (mReadPref.getUserProfile().equals(""+ProUrl)) {
            // myProfile.setImageDrawable(R.drawable.profile);
            profile.setImageResource(R.drawable.profile);
            Log.v("IMAGE_NOT",""+ProUrl);
        }
        else {
            Glide.with(getApplicationContext())
                    .load(mReadPref.getUserProfile())
                    .into(profile);
        }

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, Dashboard.class);
        startActivity(intent);
        finish();
    }

    public void onUpdate(View view)
    {
        if (userNamePro.getText().toString().length() == 0){
            userNamePro.setError("Enter Username");
            userNamePro.requestFocus();
        }
        else if (useremailPro.getText().toString().length() == 0){
            useremailPro.setError("Enter Email");
            useremailPro.requestFocus();
        }
        else if (userMobilepro.getText().toString().length() == 0){
            userMobilepro.setError("Enter Mobile No");
            userMobilepro.requestFocus();
        }
        else if (userAddres.getText().toString().length() == 0){
            userAddres.setError("Enter Address");
            userAddres.requestFocus();
        }
        else
        {
            email = useremailPro.getText().toString().trim();

            if (email.matches(emailPattern))
            {
                updateUser();
            }
            else
            {
                Toast.makeText(getApplicationContext(),"Invalid email",Toast.LENGTH_SHORT).show();
                useremailPro.setError("Enter Valid Email");
                useremailPro.requestFocus();
            }
        }
    }

    private void updateUser()
    {
        final ProgressDialog loading = ProgressDialog.show(Profile.this, "Authenticating", "Please wait while we check the entered code", false,false);
        String URL=DyUrl+"updateProfile.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.v("Response : ",""+response);
                    JSONObject objres = new JSONObject(response);
                    String status = objres.getString("Status");

                    if (status.equals("200")){
                        loading.dismiss();
                        WritePref writePref = BaseApp.getInstance().getWritePref();
                        writePref.saveUserLoginStatus(true);
                        writePref.saveUserId(""+objres.getString("User_id"));
                        writePref.saveUserName(""+objres.getString("User_Name"));
                        writePref.saveUserEmail(""+objres.getString("User_Email"));
                        writePref.saveUserMobile(""+objres.getString("User_Mobile"));
                        writePref.saveroleId(""+objres.getString("Role_id"));
                        writePref.saveisActive(""+objres.getString("isActive"));
                        writePref.saveUserAddress(""+objres.getString("Address"));

                        Toast.makeText(getApplicationContext(),"Update Success...",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(),Dashboard.class);
                        startActivity(intent);
                        finish();
                    }
                    else if (status.equals("400")){
                        loading.dismiss();
                        Toast.makeText(getApplicationContext(),"Update Failed...",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        loading.dismiss();
                        Toast.makeText(getApplicationContext(),"Invalid Credentials",Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Log.v("ERROR SERVER", e.toString());
                    loading.dismiss();
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
                params.put("User_id",""+mReadPref.getUser_id());
                params.put("User_Name",""+userNamePro.getText().toString());
                params.put("User_Email",""+useremailPro.getText().toString());
                params.put("User_Mobile",""+userMobilepro.getText().toString());
                params.put("isActive",""+mReadPref.getUserisActive());
                params.put("Address",""+userAddres.getText().toString());
                params.put("Role_id",""+mReadPref.getUserRole_id());
                if (imagePath==null){
                    params.put("file",""); //Adding file
                }
                else {
                    params.put("file",imageToString(bitmap)); //Adding file
                    WritePref writePref = BaseApp.getInstance().getWritePref();
                    writePref.saveUserProfile(""+imagePath);
                }
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy( 5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingletone.getInstance(Profile.this).addToRequestQueue(stringRequest);

    }

    private String imageToString(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte[] imgBytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgBytes,Base64.DEFAULT);
    }

    public void addPhotoPro(View view) {
        Intent i = new Intent(
                Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();

            try
            {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),selectedImage);
            }
            catch (IOException e) {
                e.printStackTrace();
            }

            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            imagePath = cursor.getString(columnIndex);
            cursor.close();

            profile.setImageBitmap(BitmapFactory.decodeFile(picturePath));

        }
    }


}
