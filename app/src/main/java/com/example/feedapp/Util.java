package com.example.feedapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * Created by rai on 18/3/18.
 */

public class Util {

    public static boolean isInternetConnection(Context context) {
    // TODO Auto-generated method stub

    ConnectivityManager cn = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
    NetworkInfo nf=cn.getActiveNetworkInfo();
    boolean statusInternet;

    if(nf != null && nf.isConnected()==true )
    {
        Log.i("Info:", "Network Available.");
        statusInternet=true;
    }
    else
    {
        Log.i("Info:", "Network Not Available.");
        statusInternet=false;

    }
    return statusInternet;
}
}
