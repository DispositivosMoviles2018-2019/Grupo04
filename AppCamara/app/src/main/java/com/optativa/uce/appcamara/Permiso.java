package com.optativa.uce.appcamara;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

public class Permiso {

    private static final int REQUEST_STORAGE = 1;

    private Activity activity;

    public Permiso(Activity activity) {
        this.activity = activity;
    }

    public boolean storage() {
        return ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    public void checkAll() {
        if (!storage()) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_STORAGE);
        }
    }
}
