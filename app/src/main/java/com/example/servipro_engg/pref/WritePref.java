package com.example.servipro_engg.pref;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.servipro_engg.utils.Constants;

public class WritePref
{
    private Context context;
    private SharedPreferences.Editor mEditor;
    private static WritePref mWritePref;

    private WritePref(Context context){
        this.context = context;
        createWritablePref();
    }

    public static WritePref getInstance(Context context){
        if (mWritePref == null){
            mWritePref = new WritePref(context);
        }
        return mWritePref;
    }

    //making the object for write able shared preference
    private void createWritablePref(){
        mEditor = context.getSharedPreferences(Constants.PREF_NAME,context.MODE_PRIVATE).edit();
    }

    public void saveUserRegisterStatus(boolean isRegister){
        mEditor.putBoolean(Constants.PREF_USER_REGISTER_STATUS,isRegister).commit();

    }

    public void saveUserLoginStatus(boolean isLogin){
        mEditor.putBoolean(Constants.PREF_USER_LOGIN_STATUS,isLogin).commit();
    }

    public void saveUserId(String userId){
        mEditor.putString(Constants.User_id,userId).commit();
    }

    public void saveUserName(String userName){
        mEditor.putString(Constants.User_Name,userName).commit();
    }

    public void saveUserEmail(String userEmail){
        mEditor.putString(Constants.User_Email,userEmail).commit();
    }

    public void saveUserMobile(String userMobile){
        mEditor.putString(Constants.User_Mobile,userMobile).commit();
    }

    public void saveroleId(String roleId){
        mEditor.putString(Constants.Role_id,roleId).commit();
    }

    public void saveisActive(String isActive){
        mEditor.putString(Constants.isActive,isActive).commit();
    }

    public void saveUserAddress(String Address){
        mEditor.putString(Constants.Address,Address).commit();
    }

    public void saveUserProfile(String Profile){
        mEditor.putString(Constants.Profile,Profile).commit();
    }
}
