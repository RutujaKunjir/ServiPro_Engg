package com.example.servipro_engg.Activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.CompoundButtonCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
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
import com.example.servipro_engg.Adapter.ProductAddAdapter;
import com.example.servipro_engg.Adapter.ServiceAdapter;
import com.example.servipro_engg.List.ProductAddList;
import com.example.servipro_engg.List.ServiceList;
import com.example.servipro_engg.List.TaskList;
import com.example.servipro_engg.R;
import com.example.servipro_engg.app.BaseApp;
import com.example.servipro_engg.pref.ReadPref;
import com.example.servipro_engg.utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ServiceDetails extends AppCompatActivity
{
    private TextView cust_name_txt,cust_email_txt,cust_mobile_txt,cust_address_txt,
            service_call_txt,service_date_txt,service_time_txt,service_detail_txt,service_amc_txt,
            productName,productPrice,productQty,totalAmount,total_amount_collection;
    private EditText model_name,rouv_name,service_update_time_txt,service_update_date_txt,from_date_txt,comment_txt;
    private TaskList mData;
    private Switch switch1;
    private RecyclerView product_listRecyclerView,service_list_recycle;
    private LinearLayout totCalDe;
    private CardView amcCard;
    private ImageView custSignImg;
    private View viewBB;
    String DyUrl,custId,serveCallNo;
    private List<ProductAddList> productAddList;
    private List<ServiceList> serviceLists;
    private ProgressDialog dialog;
    private ProductAddAdapter productAddAdapter;
    private ServiceAdapter serviceAdapter;
    private CheckBox oneTime,amcDetial;
    ColorStateList colorStateList;
    private ReadPref mReadPref;
    private Spinner year_spinner,freq_spinner;
    private LinearLayout llPdf;
    private Bitmap bitmap;
    private String path,AMcADD,AMCPOSITION;
    private File dir;
    private File filePath;

    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_details);

        verifyStoragePermissions(ServiceDetails.this);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        dialog = new ProgressDialog(this);
        dialog.setMessage("please wait..");
        dialog.setCancelable(false);
        dialog.show();

        Log.v("AAAAAAAAA",""+ BaseApp.getInstance());
        mReadPref = BaseApp.getInstance().getReadPref();

        Intent intent = getIntent();
        mData = intent.getParcelableExtra(Constants.TASK_DATA);

        llPdf = findViewById(R.id.llPdf);

        //creating new file path
        Log.v("A Path",""+Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS));

        //  path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + "/ServiPro/PDF Reports";
        path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/ServiPro/PDF Reports";

        dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        totCalDe = findViewById(R.id.totCalDe);
        year_spinner = findViewById(R.id.year_spinner);
        freq_spinner = findViewById(R.id.freq_spinner);
        viewBB = findViewById(R.id.viewBB);
        amcCard  = findViewById(R.id.amcCard);
        custSignImg  = findViewById(R.id.custSignImg);
        service_update_date_txt = findViewById(R.id.service_update_date_txt);
        from_date_txt = findViewById(R.id.from_date_txt);
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
        oneTime = findViewById(R.id.oneTime);
        amcDetial = findViewById(R.id.amcDetial);
        rouv_name = findViewById(R.id.rouv_name);
        comment_txt = findViewById(R.id.comment_txt);
        switch1 = findViewById(R.id.switch1);
        total_amount_collection = findViewById(R.id.total_amount_collection);

        model_name.setEnabled(false);  rouv_name.setEnabled(false); comment_txt.setEnabled(false);
        // oneTime.setEnabled(false);  amcDetial.setEnabled(false);
        service_update_date_txt.setEnabled(false); service_update_time_txt.setEnabled(false);

        getPrevData();

        try {
            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("MyPref", 0);
            DyUrl = sharedPreferences.getString("NewUrl", null);
        } catch (Exception e){
            Log.d("HHHHHHH = ",""+e);
            Toast.makeText(this, "Error Login= " + e, Toast.LENGTH_SHORT).show();
        }

        productAddList = new ArrayList<>();
        serviceLists = new ArrayList<>();

        product_listRecyclerView = findViewById(R.id.product_list);
        product_listRecyclerView.setHasFixedSize(true);
        product_listRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        service_list_recycle = findViewById(R.id.service_list_recycle);
        service_list_recycle.setHasFixedSize(true);
        service_list_recycle.setLayoutManager(new LinearLayoutManager(this));


        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    product_listRecyclerView.setVisibility(View.VISIBLE);
                } else {
                    product_listRecyclerView.setVisibility(View.GONE);
                }
            }
        });

        getTaskList();

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

    }

    private void getPrevData()
    {
        Intent intent = getIntent();
        custId = intent.getStringExtra("Cust_id");
        serveCallNo = intent.getStringExtra("Service_call_no");
        AMcADD = intent.getStringExtra("ADDAMC");
        AMCPOSITION = intent.getStringExtra("AMCPOSITION");
        Log.v("Activity ADDAMC = ",""+AMcADD);
        Log.v("Activity AMCPOSITION = ",""+AMCPOSITION);

        Log.v("AAAAaaa = ","");
    }

    private void getTaskList()
    {
        final ReadPref readPref = BaseApp.getInstance().getReadPref();
        String URL=DyUrl+"getServiceDetails.php";
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    Log.v("RESPONSE==",""+response);
                    JSONObject objres=new JSONObject(response);

                    String status = objres.getString("Status");
                    String data = objres.getString("tax_data");

                    if (status.equals("200")){
                        dialog.dismiss();
                        // if((response.get(i).getAlert_new()).equals("1") || (response.get(i).getAlert_new()).equals("2")){
                        JSONArray jsonArray = new JSONArray(data);
                        JSONObject object3 = jsonArray.getJSONObject(0);

                        Glide.with(getApplicationContext())
                                .load(object3.getString("Cust_Sign"))
                                .into(custSignImg);

                        cust_name_txt.setText(""+object3.getString("Cust_Name"));
                        cust_email_txt.setText(""+object3.getString("Cust_Email"));
                        cust_mobile_txt.setText(""+object3.getString("Cust_Mobile1"));
                        cust_address_txt.setText(""+object3.getString("Cust_Address"));
                        service_call_txt.setText(""+object3.getString("Service_call_no"));
                        service_date_txt.setText(""+object3.getString("Service_Date"));
                        service_time_txt.setText(""+object3.getString("Service_Time"));
                        service_detail_txt.setText(""+object3.getString("Amc_Details"));
                        model_name.setText(""+object3.getString("Model_Name"));
                        rouv_name.setText(""+object3.getString("ROUV"));
                        comment_txt.setText(""+object3.getString("Comment"));
                        service_update_date_txt.setText(""+object3.getString("Service_Date"));
                        service_update_time_txt.setText(""+object3.getString("Service_Time"));

                        service_amc_txt.setText(""+object3.getString("Amc_Details"));
                        if (object3.getString("Amc_Id").equals("1")){
                            amcDetial.setChecked(true);
                            amcCard.setVisibility(View.VISIBLE);
                        }
                        if (object3.getString("Amc_Id").equals("2")){
                            oneTime.setChecked(true);
                            amcCard.setVisibility(View.GONE);
                        }

                        String product_detail = object3.getString("product_detail");
                        String Service_detail = object3.getString("Service_detail");
                        JSONArray jsonArray11 = new JSONArray(product_detail);
                        for (int i=0; i<jsonArray11.length();i++){
                            JSONObject object2 = jsonArray11.getJSONObject(i);
                            productAddList.add(new ProductAddList(
                                    ""+object2.getString("Product_Name"),
                                    ""+object2.getString("Product_Price"),
                                    ""+object2.getString("Product_Qunty"),
                                    ""+object2.getString("Single_Pro_Tot")
                            ));
                        }

                        JSONArray jsonArray33 = new JSONArray(Service_detail);
                        boolean isCheck=false;
                        for (int i=0; i<jsonArray33.length();i++){
                            JSONObject object22 = jsonArray33.getJSONObject(i);
                            if (object22.getString("AmcCheck").equals("0")){
                                isCheck = false;
                            }

                            if (object22.getString("AmcCheck").equals("1")){
                                isCheck = true;
                            }

                            serviceLists.add(new ServiceList(
                                    ""+object22.getString("SrNo"),
                                    ""+object22.getString("AmcDate"),
                                    ""+object22.getString("Service_call_no"),
                                    ""+object3.getString("Cust_id"),
                                    isCheck
                            ));
                        }
                    }
                    else {
                        dialog.dismiss();
                        // Toast.makeText(getContext(),"Item Not Available",Toast.LENGTH_SHORT).show();
                    }

                    productAddAdapter = new ProductAddAdapter(productAddList, ServiceDetails.this);
                    product_listRecyclerView.setAdapter(productAddAdapter);

                    serviceAdapter = new ServiceAdapter(serviceLists, ServiceDetails.this);
                    service_list_recycle.setAdapter(serviceAdapter);

                    if(productAddList.size()>0){
                        calculateData();
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
                params.put("Cust_id", ""+custId);
                params.put("Service_call_no", ""+serveCallNo);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy( 5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
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

        total_amount_collection.setText(""+sum);
    }

    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(this, CustAmcDetails.class);
        intent.putExtra("ADDAMC",""+AMcADD);
        intent.putExtra("AMCPOSITION",""+AMCPOSITION);
        intent.putExtra(Constants.TASK_DATA,mData);
        startActivity(intent);
        finish();
    }

    public void generatePdf(View view)
    {
        /*File outputFile = new File(Environment.getExternalStoragePublicDirectory
                (Environment.DIRECTORY_DOWNLOADS), "Rutuja Resume 3 Jan 2020.pdf");
        Uri uri = Uri.fromFile(outputFile);

        Intent share = new Intent();
        share.setAction(Intent.ACTION_SEND);
        share.setType("application/pdf");
        share.putExtra(Intent.EXTRA_STREAM, uri);
        //share.setPackage("com.whatsapp");
        startActivity(Intent.createChooser(share,"Choose an email client"));
        //startActivity(share);*/

        Log.d("size"," "+llPdf.getWidth() +"  "+llPdf.getWidth());
        bitmap = loadBitmapFromView(llPdf, llPdf.getWidth(), llPdf.getHeight());
        createPdf();

    }

    public static Bitmap loadBitmapFromView(View v, int width, int height) {
        Bitmap b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.draw(c);

        return b;
    }

    private void createPdf(){
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        //  Display display = wm.getDefaultDisplay();
        DisplayMetrics displaymetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        float hight = displaymetrics.heightPixels ;
        float width = displaymetrics.widthPixels ;

        int convertHighet = (int) hight, convertWidth = (int) width;

//        Resources mResources = getResources();
//        Bitmap bitmap = BitmapFactory.decodeResource(mResources, R.drawable.screenshot);

        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(convertWidth, convertHighet, 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);

        Canvas canvas = page.getCanvas();

        Paint paint = new Paint();
        canvas.drawPaint(paint);

        bitmap = Bitmap.createScaledBitmap(bitmap, convertWidth, convertHighet, true);

        paint.setColor(Color.BLUE);
        canvas.drawBitmap(bitmap, 0, 0 , null);
        document.finishPage(page);

        // write the document content

        Log.e("PDFCreator", "PDF Path: " + path);
        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
        filePath = new File(dir, ""+cust_name_txt.getText().toString()+"_"+service_update_date_txt.getText().toString()
                +".pdf");

        try {
            document.writeTo(new FileOutputStream(filePath));

        } catch (IOException e) {
            e.printStackTrace();
            // Toast.makeText(this, "Something wrong: " + e.toString(), Toast.LENGTH_LONG).show();
        }

        // close the document
        document.close();
        Toast.makeText(this, "PDF is created!!!", Toast.LENGTH_SHORT).show();

        openGeneratedPDF();

    }

    private void openGeneratedPDF(){
        if (filePath.exists())
        {
            Intent intent=new Intent(Intent.ACTION_VIEW);
            Uri uri = Uri.fromFile(filePath);
            intent.setDataAndType(uri, "application/pdf");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            try
            {
                startActivity(intent);
            }
            catch(ActivityNotFoundException e)
            {
                Toast.makeText(ServiceDetails.this, "No Application available to view pdf", Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * Checks if the app has permission to write to device storage
     *
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }

    }


}
