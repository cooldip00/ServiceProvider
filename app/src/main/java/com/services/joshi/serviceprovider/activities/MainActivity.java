package com.services.joshi.serviceprovider.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.services.joshi.serviceprovider.R;
import com.services.joshi.serviceprovider.activities.serviceprovider.SPHomeActivity;
import com.services.joshi.serviceprovider.activities.serviceprovider.SPLoginActivity;
import com.services.joshi.serviceprovider.activities.user.UserHomeActivity;
import com.services.joshi.serviceprovider.activities.user.UserLoginActivity;
import com.services.joshi.serviceprovider.storage.SharedPrefManager;
import com.services.joshi.serviceprovider.storage.SharedPrefManagerSP;

public class MainActivity extends AppCompatActivity {

    public static final int STORAGE_PERMISSION_CODE = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //changing statusbar color
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimaryDark));
        }

        requestStoragePermission();
    }

    protected void onStart() {
        super.onStart();

        if (SharedPrefManager.getInstance(this).isloggedIn()) {
            Intent intent = new Intent(this, UserHomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        if (SharedPrefManagerSP.getInstance(this).isloggedIn()) {
            //if user is save we have to start fresh activity  directly for that
            Intent intent = new Intent(this, SPHomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    protected void gotoUserLogin(View view) {
        startActivity(new Intent(this, UserLoginActivity.class));
    }

    protected void gotoServiceProvicerLogin(View view) {
        startActivity(new Intent(this, SPLoginActivity.class));
    }

    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Toast.makeText(this, "The permission is required because of get image from storage", Toast.LENGTH_SHORT).show();
        }
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }

    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
                Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
//                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }
}
