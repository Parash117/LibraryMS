package com.example.libms;

import android.content.Context;
import android.net.ConnectivityManager;

import java.util.Objects;

import androidx.annotation.NonNull;

/**
 * Created by Akshay Raj on 06/02/18.
 * akshay@snowcorp.org
 * www.snowcorp.org
 */

public class InternetConnection {
    /**
     * CHECK WHETHER INTERNET CONNECTION IS AVAILABLE OR NOT
     */
    public static boolean checkConnection(@NonNull Context context) {
        return ((ConnectivityManager) Objects.requireNonNull(context.getSystemService
                (Context.CONNECTIVITY_SERVICE))).getActiveNetworkInfo() != null;
    }
}
