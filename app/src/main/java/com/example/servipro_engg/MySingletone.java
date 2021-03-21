package com.example.servipro_engg;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class MySingletone
{
    public static MySingletone myInstance;
    private RequestQueue requestQueue;
    private static Context mCtx;

    public MySingletone(Context context) {
        mCtx = context;
        requestQueue = getRequestQueue();
    }

    private RequestQueue getRequestQueue()
    {
        if (requestQueue == null)
            requestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());

        return requestQueue;
    }

    public static synchronized MySingletone getInstance(Context context)
    {
        if (myInstance == null){
            myInstance = new MySingletone(context);
        }

        return myInstance;
    }

    public<T> void addToRequestQueue(Request<T> request)
    {
        getRequestQueue().add(request);
    }
}
