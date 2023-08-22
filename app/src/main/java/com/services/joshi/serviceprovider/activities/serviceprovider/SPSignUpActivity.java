package com.services.joshi.serviceprovider.activities.serviceprovider;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.sdsmdg.tastytoast.TastyToast;
import com.services.joshi.serviceprovider.R;
import com.services.joshi.serviceprovider.adapter.SPSignUpFragmentAdapter;
import com.services.joshi.serviceprovider.activities.serviceprovider.spsignupfragments.NonSwipeableViewPager;
import com.services.joshi.serviceprovider.api.RetrofitClient;
import com.services.joshi.serviceprovider.repository.SPSignUpResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class SPSignUpActivity extends AppCompatActivity {

    NonSwipeableViewPager nonSwipeableViewPager;

    public static String FNAME, LNAME, MNAME, EMAIL, PASSWORD, PHONE_NO, ADDRESS, CATEGORY, GENDER, USER_IMAGE, AADHAR_IMAGE;

    private LocationManager locationManager;
    private LocationListener listener;

    TextView buttonGoToLogin;

    String lattitude, longtitude;
    double lattitude1, longtitude1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spsign_up);

        //changing statusbar color
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimaryDark));
        }

        buttonGoToLogin = findViewById(R.id.btn_singup_goto_login);

        buttonGoToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SPSignUpActivity.this, SPLoginActivity.class));
                finish();
            }
        });

        nonSwipeableViewPager = findViewById(R.id.sp_signup_viewpager);
        nonSwipeableViewPager.setAdapter(new SPSignUpFragmentAdapter(getSupportFragmentManager()));

        requestPermission();

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                lattitude1 = location.getLatitude();
                longtitude1 = location.getLongitude();

                lattitude = String.valueOf(lattitude1);
                longtitude = String.valueOf(longtitude1);

                Log.d("OK", "gps is" + lattitude1 + " " + longtitude1);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {
                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(i);
            }
        };
    }

    private void requestPermission() {
        Toast.makeText(this, "Inside the Request", Toast.LENGTH_SHORT).show();
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET}, 10);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 10:
                gpsPermission();
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (nonSwipeableViewPager.getCurrentItem() == 0) {
            super.onBackPressed();
        } else {
            nonSwipeableViewPager.setCurrentItem(nonSwipeableViewPager.getCurrentItem() - 1);
        }
    }

    public void goToNextPage() {
        nonSwipeableViewPager.setCurrentItem(nonSwipeableViewPager.getCurrentItem() + 1, true);
    }

    public void goToBackPage() {
        nonSwipeableViewPager.setCurrentItem(nonSwipeableViewPager.getCurrentItem() - 1, true);
    }

    public void registerProvider() {
        //ProgressDialog creation
        final ProgressDialog progressDialog = new ProgressDialog(this, R.style.AppCompatAlertDialogStyle);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        Call<SPSignUpResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .createProvider(
                        FNAME, MNAME, LNAME,
                        EMAIL, PASSWORD, GENDER,
                        PHONE_NO, ADDRESS, CATEGORY,
                        USER_IMAGE, AADHAR_IMAGE,
                        lattitude, longtitude
                );

        call.enqueue(new Callback<SPSignUpResponse>() {
            @Override
            public void onResponse(@NonNull Call<SPSignUpResponse> call, @NonNull Response<SPSignUpResponse> response) {
                SPSignUpResponse sr = response.body();
                if (sr.isError()) {
                    progressDialog.dismiss();
                    TastyToast.makeText(SPSignUpActivity.this, sr.getMessage(), TastyToast.LENGTH_LONG, TastyToast.ERROR).show();

                } else {
                    progressDialog.dismiss();
                    TastyToast.makeText(SPSignUpActivity.this, sr.getMessage(), TastyToast.LENGTH_LONG, TastyToast.SUCCESS).show();
                    startActivity(new Intent(SPSignUpActivity.this, SPLoginActivity.class));
                    finish();
                }
            }

            @Override
            public void onFailure(Call<SPSignUpResponse> call, Throwable t) {
                progressDialog.dismiss();
                TastyToast.makeText(SPSignUpActivity.this, t.getMessage(), TastyToast.LENGTH_LONG, TastyToast.ERROR).show();
            }
        });
    }

    private void gpsPermission() {
        //noinspection MissingPermission
        if (ActivityCompat.checkSelfPermission(SPSignUpActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(SPSignUpActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}
                        , 10);
            }
            return;
        }
        /*locationManager.requestLocationUpdates("gps", 5000, 0, listener);*/
        locationManager.requestSingleUpdate("gps", listener, null);
    }

}
