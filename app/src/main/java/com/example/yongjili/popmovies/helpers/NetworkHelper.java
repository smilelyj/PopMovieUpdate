package com.example.yongjili.popmovies.helpers;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Casey on 7/21/2015.
 */
public class NetworkHelper {
    public static boolean hasInternetConnection(ConnectivityManager connectivityManager) {
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
}
