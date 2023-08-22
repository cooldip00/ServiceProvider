package com.services.joshi.serviceprovider.activities.user;

import android.content.Intent;

import androidx.annotation.NonNull;

import com.google.android.material.navigation.NavigationView;

import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sdsmdg.tastytoast.TastyToast;
import com.services.joshi.serviceprovider.PaymentService;
import com.services.joshi.serviceprovider.R;
import com.services.joshi.serviceprovider.activities.user.drawerfragment.UserSettingFragment;
import com.services.joshi.serviceprovider.activities.user.drawerfragment.RequestListFragment;
import com.services.joshi.serviceprovider.activities.MainActivity;
import com.services.joshi.serviceprovider.activities.user.drawerfragment.AboutUsFragment;
import com.services.joshi.serviceprovider.activities.user.drawerfragment.ContactUsFragment;
import com.services.joshi.serviceprovider.activities.user.drawerfragment.HistoryFragment;
import com.services.joshi.serviceprovider.activities.user.drawerfragment.HomeFragment;
import com.services.joshi.serviceprovider.activities.user.drawerfragment.ProfileFragment;
import com.services.joshi.serviceprovider.activities.user.drawerfragment.ServiceFragment;
import com.services.joshi.serviceprovider.api.RetrofitClient;
import com.services.joshi.serviceprovider.repository.StatusResponse;
import com.services.joshi.serviceprovider.storage.SharedPrefManager;
import com.squareup.picasso.Picasso;

public class UserHomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawer;
    public static boolean LoginFistTime = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);
        setTitle("Home");

        Toolbar toolbar = findViewById(R.id.nav_user_toolbar);
        setSupportActionBar(toolbar);

        //changing statusbar color
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimaryDark));
        }

        drawer = findViewById(R.id.user_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.user_nav_drawer_open, R.string.user_nav_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_user_view);

        View headerView = navigationView.getHeaderView(0);
        ImageView imageViewUserImage = headerView.findViewById(R.id.user_nav_header_profile_image);
        TextView textViewName = headerView.findViewById(R.id.user_nav_header_name);
        TextView textViewEmail = headerView.findViewById(R.id.user_nav_header_email);

        if (SharedPrefManager.getInstance(this).getUser().getUserImage() != null) {
            Picasso.with(this).load(SharedPrefManager.getInstance(this).getUser().getUserImage()).into(imageViewUserImage);
        } else {
            imageViewUserImage.setImageResource(R.drawable.no_image);
        }

        String name = SharedPrefManager.getInstance(this).getUser().getFname() + " " +
                SharedPrefManager.getInstance(this).getUser().getLname();

        textViewName.setText(name);
        textViewEmail.setText(SharedPrefManager.getInstance(this).getUser().getEmail());

        navigationView.setNavigationItemSelectedListener(this);


        LoginFistTime = false;

        if (getIntent().getIntExtra("profile", -1) == 10) {
            getSupportFragmentManager().beginTransaction().replace(R.id.user_nav_fragment_container,
                    new ProfileFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_user_home);
        }

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.user_nav_fragment_container,
                    new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_user_home);
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.user_option_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.user_logout:
                logout();
                break;
            case R.id.user_about:
                getSupportFragmentManager().beginTransaction().replace(R.id.user_nav_fragment_container,
                        new AboutUsFragment()).commit();
                setTitle("About Us");
                break;
            case R.id.user_change_password:
                startActivity(new Intent(UserHomeActivity.this, UserPasswordChangeActivity.class));
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START))
            drawer.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }

    void logout() {
        SharedPrefManager.getInstance(UserHomeActivity.this).clear();

        Intent intent = new Intent(UserHomeActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();

        checkForPandingPayment();

        if (!SharedPrefManager.getInstance(this).isloggedIn()) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.nav_user_switch_to_ar:
                startActivity(new Intent(UserHomeActivity.this, User_AR_Activity.class));
                break;
            case R.id.nav_user_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.user_nav_fragment_container,
                        new HomeFragment()).commit();
                setTitle("Home");

                break;
            case R.id.nav_user_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.user_nav_fragment_container,
                        new ProfileFragment()).commit();
                setTitle("My Profile");
                break;
            case R.id.nav_user_services:
                getSupportFragmentManager().beginTransaction().replace(R.id.user_nav_fragment_container,
                        new ServiceFragment()).commit();
                setTitle("Services");
                break;
            case R.id.nav_user_request_list:
                getSupportFragmentManager().beginTransaction().replace(R.id.user_nav_fragment_container,
                        new RequestListFragment()).commit();
                setTitle("Request List");
                break;
            case R.id.nav_user_history:
                getSupportFragmentManager().beginTransaction().replace(R.id.user_nav_fragment_container,
                        new HistoryFragment()).commit();
                setTitle("History");
                break;
            case R.id.nav_user_setting:
                getSupportFragmentManager().beginTransaction().replace(R.id.user_nav_fragment_container,
                        new UserSettingFragment()).commit();
                setTitle("Setting");
                break;
            case R.id.nav_user_share:
                Toast.makeText(this, "Share is called", Toast.LENGTH_SHORT).show();
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,
                        "Hey check out my app at: https://play.google.com/store/apps/details?id=com.google.android.apps.plus");
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                break;
            case R.id.nav_user_about:
                getSupportFragmentManager().beginTransaction().replace(R.id.user_nav_fragment_container,
                        new AboutUsFragment()).commit();
                setTitle("About Us");
                break;
            case R.id.nav_user_contact:
                getSupportFragmentManager().beginTransaction().replace(R.id.user_nav_fragment_container,
                        new ContactUsFragment()).commit();
                setTitle("Contact Us");
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return false;
    }

    private void checkForPandingPayment() {
        int user_id = SharedPrefManager.getInstance(this).getUser().getId();

        Call<StatusResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .checkPendingPaymnet(user_id);
        call.enqueue(new Callback<StatusResponse>() {
            @Override
            public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().isStatus()) {
                        Intent serviceIntent = new Intent(UserHomeActivity.this, PaymentService.class);

                        ContextCompat.startForegroundService(UserHomeActivity.this, serviceIntent);
                    }

                }
            }

            @Override
            public void onFailure(Call<StatusResponse> call, Throwable t) {
                TastyToast.makeText(UserHomeActivity.this, t.getMessage(), Toast.LENGTH_LONG, TastyToast.ERROR).show();
            }
        });
    }
}
