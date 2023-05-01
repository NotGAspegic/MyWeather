package com.example.myweather;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class LocationActivity extends AppCompatActivity {
    private Button btn;
    private boolean isgranted = false;
    String[] permissions = {"android.permission.ACCESS_FINE_LOCATION", "android.permission.ACCESS_COARSE_LOCATION"};
    private static final int REQUEST_CODE = 101;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        btn = findViewById(R.id.im_in_loc);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.bg_gradient));
        requestPermissions(permissions, REQUEST_CODE);
        isgranted= ActivityCompat.checkSelfPermission(
                this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED;
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isgranted){
                    SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("isPermissionLocGranted", true);
                    editor.apply();
                    Intent intent = new Intent(LocationActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    if(shouldShowRequestPermissionRationale(permissions[0]) && (shouldShowRequestPermissionRationale(permissions[1]) )){
                        Toast.makeText(LocationActivity.this, "Please Provide the Permissions...", Toast.LENGTH_SHORT).show();

                    }
                    else {
                        Toast.makeText(LocationActivity.this, "Please provide the Permissions from the App Settings", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);
                    }
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            isgranted = grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;
            if(isgranted){
                Toast.makeText(this, "Permission granted...", Toast.LENGTH_SHORT).show();
            }
        }
    }
}