package com.example.servipro_engg.pref;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.servipro_engg.utils.Constants;

public class ReadPref
{
    private Context context;
    private SharedPreferences mPref;
    private static ReadPref mReadPref;

    private ReadPref(Context context){
        this.context = context;
        createReadPref();
    }


    public static ReadPref getInstance(Context context){
        if (mReadPref == null){
            mReadPref = new ReadPref(context);
        }
        return mReadPref;
    }

    private void createReadPref(){
        mPref = context.getSharedPreferences(Constants.PREF_NAME,context.MODE_PRIVATE);
    }

    //return login status
    public boolean isLogin(){
        return mPref.getBoolean(Constants.PREF_USER_LOGIN_STATUS,false);
    }

    public boolean isRegister(){
        return mPref.getBoolean(Constants.PREF_USER_REGISTER_STATUS,false);
    }

    public String getUser_id(){
        return mPref.getString(Constants.User_id,"");
    }
    public String getUser_Name(){
        return mPref.getString(Constants.User_Name,"");
    }
    public String getUser_Email(){
        return mPref.getString(Constants.User_Email,"");
    }
    public String getUser_Mobile(){
        return mPref.getString(Constants.User_Mobile,"");
    }
    public String getUserRole_id(){
        return mPref.getString(Constants.Role_id,"");
    }
    public String getUserisActive(){ return mPref.getString(Constants.isActive,""); }
    public String getUserAddress(){
        return mPref.getString(Constants.Address,"");
    }
    public String getUserProfile(){
        return mPref.getString(Constants.Profile,"");
    }

}
