package com.example.servipro_engg.app;

import android.content.Context;

import com.example.servipro_engg.pref.ReadPref;
import com.example.servipro_engg.pref.WritePref;
import com.singhajit.sherlock.core.Sherlock;

import androidx.multidex.MultiDexApplication;

public class BaseApp extends MultiDexApplication {

    private static ReadPref mReadPref;
    private static WritePref mWritePref;
    private static BaseApp mInstance;
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        context = getApplicationContext();
        Sherlock.init(this);
    }


    public static synchronized BaseApp getInstance() {
        return mInstance;
    }


    public ReadPref getReadPref(){
        if (mReadPref == null){
            mReadPref = ReadPref.getInstance(context);
        }
        return mReadPref;
    }

    public WritePref getWritePref(){
        if (mWritePref == null){
            mWritePref = WritePref.getInstance(context);
        }
        return mWritePref;
    }
}