package com.example.pressnewspaper.Utils;

import android.app.Activity;
import android.content.Context;
import android.drm.DrmStore;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Global {
    public boolean isOnline(Activity activity) {
        ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert cm != null;
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
