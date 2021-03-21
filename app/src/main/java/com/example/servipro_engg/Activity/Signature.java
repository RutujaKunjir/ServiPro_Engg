package com.example.servipro_engg.Activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.kyanogen.signatureview.SignatureView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Signature extends AppCompatActivity
{
    private SignatureView signatureView;
    Button btnClear,btnUpload;
    String name,Image_Name,service_call_no,path,order_id,imageName;

    String phpFile = "insertSign.php";
    String DyUrl;
    private Bitmap bitmaps;
    //storage permission code
    private static final int STORAGE_PERMISSION_CODE = 123;
    // create instance of Random class
    Random rand = new Random();



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signature);

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("MyPref",0);

        DyUrl = sharedPreferences.getString("NewUrl",null);

        signatureView = findViewById(R.id.signature_view);

        int colorPrimary = ContextCompat.getColor(this, R.color.colorAccent);
        signatureView.setPenColor(colorPrimary);
        // or like signatureView.setPenColor(Color.RED);

        // Generate random integers in range 0 to 999
        int rand_int1 = rand.nextInt(1000);

        Intent getIntent = getIntent();
        name = getIntent.getStringExtra("Name");
        service_call_no = getIntent.getStringExtra("service_call_no");
        Image_Name = name+"_"+service_call_no+"_"+rand_int1;
        Log.v("Image_Name = ",""+Image_Name);

        btnClear = (Button)findViewById(R.id.clear);
        btnUpload = (Button)findViewById(R.id.btnUpload);

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signatureView.clearCanvas();//Clear SignatureView
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSigns();
                imageName = Image_Name+".png";
                uploadMultipart2();
               /* uploadMultipart();
                Intent savelIntent = new Intent(SignatureActivity.this,TabActivit.class);
                startActivity(savelIntent);
                finish(); */
            }
        });

    }

    private void uploadMultipart2()
    {
        String URL=DyUrl+phpFile;
        Log.v("file",imageToString(bitmaps)); //Adding file
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    Log.v("SIGN_RESPONSE",""+response);
                    JSONObject objres = new JSONObject(response);
                    String status = objres.getString("Status");

                    if (status.equals("200")){
                        Toast.makeText(getApplicationContext(),"Upload Success...",Toast.LENGTH_SHORT).show();
                        Intent savelIntent = new Intent(Signature.this,Dashboard.class);
                        savelIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        savelIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        savelIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(savelIntent);
                        finish();
                    }
                    else {
                        Toast.makeText(getApplicationContext(),"Upload Failed...",Toast.LENGTH_SHORT).show();
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
                params.put("service_call_no",service_call_no);
                params.put("file",imageToString(bitmaps)); //Adding file
                params.put("imageName",""+imageName);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy( 5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingletone.getInstance(Signature.this).addToRequestQueue(stringRequest);

    }

    private String imageToString(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte[] imgBytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgBytes,Base64.DEFAULT);
    }

   /* public void uploadMultipart() {
        //Uploading code
        try {
            String UPLOAD_URL = DyUrl+phpFile;
            String uploadId = UUID.randomUUID().toString();

            //Creating a multi part request
            new MultipartUploadRequest(this, uploadId, UPLOAD_URL)
                    .addFileToUpload(path, "file") //Adding file
                    .addParameter("order_id",order_id)
                    .addParameter("imageName",imageName)
                    .addParameter("service_call_no",service_call_no)
                    .setNotificationConfig(new UploadNotificationConfig())
                    .setMaxRetries(2)
                    .startUpload(); //Starting the upload

            Log.v("IMAGE PATH",""+path);
            Toast.makeText(this,"Upload Successfully...",Toast.LENGTH_SHORT).show();

        } catch (Exception exc) {
            Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }  */

    public void getSigns()
    {
        File directory = Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File file = new File(directory, Image_Name + ".png");
        FileOutputStream out = null;
        Bitmap bitmap = signatureView.getSignatureBitmap();
        bitmaps = signatureView.getSignatureBitmap();
        try {
            out = new FileOutputStream(file);
            if (bitmap != null) {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            } else {
                throw new FileNotFoundException();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.flush();
                    out.close();

                    if (bitmap != null) {
                        path = file.getPath();
                        // Toast.makeText(this,"Path = "+path,Toast.LENGTH_SHORT).show();
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                            new MyMediaScanner(Signature.this, file);
                        } else {
                            ArrayList<String> toBeScanned = new ArrayList<String>();
                            toBeScanned.add(file.getAbsolutePath());
                            String[] toBeScannedStr = new String[toBeScanned.size()];
                            toBeScannedStr = toBeScanned.toArray(toBeScannedStr);
                            MediaScannerConnection.scanFile(Signature.this, toBeScannedStr, null,
                                    null);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class MyMediaScanner implements
            MediaScannerConnection.MediaScannerConnectionClient {

        private MediaScannerConnection mSC;
        private File file;

        MyMediaScanner(Context context, File file) {
            this.file = file;
            mSC = new MediaScannerConnection(context, this);
            mSC.connect();
        }

        @Override
        public void onMediaScannerConnected() {
            mSC.scanFile(file.getAbsolutePath(), null);
        }

        @Override
        public void onScanCompleted(String path, Uri uri) {
            mSC.disconnect();
        }
    }

    //Requesting permission
    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }


    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
                Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onBackPressed()
    {

    }
}


