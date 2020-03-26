package com.hwichance.android.WhereIsMyMask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;

public class SplashActivity extends AppCompatActivity {
    private static final int LOCATION_PERMISSIONS_REQUEST_CODE = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSIONS_REQUEST_CODE);
        } else {
            startMain();
        }
    }

    private void startMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case LOCATION_PERMISSIONS_REQUEST_CODE: {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startMain();
                } else {
                    if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                        AlertDialog.Builder PermissionDeniedAlertDialogBuilder = new AlertDialog.Builder(this);
                        PermissionDeniedAlertDialogBuilder.setMessage(R.string.permission_denied);
                        PermissionDeniedAlertDialogBuilder.setPositiveButton(R.string.ok, (dialog, which) -> {
                            finish();
                        });
                        AlertDialog PermissionDeniedAlertDialog = PermissionDeniedAlertDialogBuilder.create();
                        PermissionDeniedAlertDialog.show();
                    } else {
                        AlertDialog.Builder requestPermissionAlertDialogBuilder = new AlertDialog.Builder(this);
                        requestPermissionAlertDialogBuilder.setMessage(R.string.permission_need);
                        requestPermissionAlertDialogBuilder.setPositiveButton(R.string.ok, (dialog, which) -> {
                            Intent settingIntent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                    .setData(Uri.parse("package:" + getPackageName()));
                            settingIntent.addCategory(Intent.CATEGORY_DEFAULT);
                            settingIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(settingIntent);
                            finish();
                        });
                        AlertDialog requestPermissionAlertDialog = requestPermissionAlertDialogBuilder.create();
                        requestPermissionAlertDialog.show();
                    }
                }
                return;
            }
        }
    }
}
