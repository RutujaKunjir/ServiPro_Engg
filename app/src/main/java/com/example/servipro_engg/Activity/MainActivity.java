package com.example.servipro_engg.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.servipro_engg.R;
import com.example.servipro_engg.app.BaseApp;
import com.example.servipro_engg.pref.ReadPref;

public class MainActivity extends AppCompatActivity
{
    String LoUrl,ProUrl;
    private ReadPref mReadPref;;
    public static Context contextOfApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contextOfApplication = getApplicationContext();

        Log.v("AAAAAAAAA",""+BaseApp.getInstance());
        mReadPref = BaseApp.getInstance().getReadPref();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    LoUrl = "http://aiproductstore.com/servipro/servipro_engg/api/"; // Development
                    ProUrl = "http://aiproductstore.com/servipro/servipro_engg/uploads/";

                   /* LoUrl = "http://aiproductstore.com/servipro/servipro_engg_ajay_live/api/"; // ajay_live
                    ProUrl = "http://aiproductstore.com/servipro/servipro_engg_ajay_live/uploads/";*/


                    SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("MyPref",0);
                    SharedPreferences.Editor editor =sharedPreferences.edit();
                    editor.putString("NewUrl",LoUrl);
                    editor.putString("ProUrl",ProUrl);
                    editor.commit();

                    Thread.sleep(2000);

                    if (mReadPref.isRegister()){

                        if (mReadPref.isLogin()){
                            Intent intent = new Intent(MainActivity.this, Dashboard.class);
                            startActivity(intent);
                            finish();
                        }
                        else {
                            Intent intent = new Intent(MainActivity.this, Login.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                    else {
                        Intent intent = new Intent(MainActivity.this, Register.class);
                        startActivity(intent);
                        finish();
                    }


                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }
}
