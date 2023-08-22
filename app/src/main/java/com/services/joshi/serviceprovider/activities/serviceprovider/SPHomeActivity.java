package com.services.joshi.serviceprovider.activities.serviceprovider;

import android.content.Intent;

import androidx.annotation.NonNull;

import com.google.android.material.navigation.NavigationView;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;

import android.view.LayoutInflater;
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
import com.services.joshi.serviceprovider.R;
import com.services.joshi.serviceprovider.activities.MainActivity;
import com.services.joshi.serviceprovider.activities.serviceprovider.drawerfragment.SPAboutUsFragment;
import com.services.joshi.serviceprovider.activities.serviceprovider.drawerfragment.SPContactUsFragment;
import com.services.joshi.serviceprovider.activities.serviceprovider.drawerfragment.SPHistoryFragment;
import com.services.joshi.serviceprovider.activities.serviceprovider.drawerfragment.SPHomeFragment;
import com.services.joshi.serviceprovider.activities.serviceprovider.drawerfragment.SPProfileFragment;
import com.services.joshi.serviceprovider.activities.serviceprovider.drawerfragment.SPRatingCommentFragmnet;
import com.services.joshi.serviceprovider.activities.serviceprovider.drawerfragment.SPRequestListFragment;
import com.services.joshi.serviceprovider.activities.serviceprovider.drawerfragment.SPServiceFragment;
import com.services.joshi.serviceprovider.activities.serviceprovider.drawerfragment.SPSettingFragment;
import com.services.joshi.serviceprovider.storage.SharedPrefManagerSP;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class SPHomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static boolean LoginFistTime = true;
    DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sphome);
        setTitle("SP: Home");

        Toolbar toolbar = findViewById(R.id.nav_sp_toolbar);
        setSupportActionBar(toolbar);

        //changing statusbar color
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimaryDark));
        }


        drawer = findViewById(R.id.sp_drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.provider_nav_drawer_open, R.string.provider_nav_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_sp_view);

        View headerView = navigationView.getHeaderView(0);

        ImageView imageViewUserImage = headerView.findViewById(R.id.sp_nav_header_profile_image);
        TextView textViewName = headerView.findViewById(R.id.sp_nav_header_name);
        TextView textViewEmail = headerView.findViewById(R.id.sp_nav_header_email);

        String name = SharedPrefManagerSP.getInstance(this).getProvider().getFname() + " " +
                SharedPrefManagerSP.getInstance(this).getProvider().getMname() + " " +
                SharedPrefManagerSP.getInstance(this).getProvider().getLname();

        if (SharedPrefManagerSP.getInstance(this).getProvider().getUserImage() != null) {
            Picasso.with(this).load(SharedPrefManagerSP.getInstance(this).getProvider().getUserImage()).into(imageViewUserImage);
        } else {
            imageViewUserImage.setImageResource(R.drawable.no_image);
        }

        textViewName.setText(name);
        textViewEmail.setText(SharedPrefManagerSP.getInstance(this).getProvider().getEmail());

        navigationView.setNavigationItemSelectedListener(this);
        LoginFistTime = false;

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.sp_nav_fragment_container,
                    new SPHomeFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_sp_home);
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        String name = SharedPrefManagerSP.getInstance(this).getProvider().getFname() + " " + SharedPrefManagerSP.getInstance(this).getProvider().getLname();

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sp_option_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sp_logout:
                SharedPrefManagerSP.getInstance(this).clear();

                Intent intent = new Intent(SPHomeActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
                break;
            case R.id.sp_about:
                TastyToast.makeText(this, "This is about Us", Toast.LENGTH_SHORT, TastyToast.SUCCESS).show();
                getSupportFragmentManager().beginTransaction().replace(R.id.sp_nav_fragment_container,
                        new SPAboutUsFragment()).commit();
                setTitle("About Us");
                break;
            case R.id.sp_change_password:
                startActivity(new Intent(SPHomeActivity.this, SPPasswordChangeActivity.class));
                break;
        }
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!SharedPrefManagerSP.getInstance(this).isloggedIn()) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START))
            drawer.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_sp_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.sp_nav_fragment_container,
                        new SPHomeFragment()).commit();
                setTitle("Home");

                break;
            case R.id.nav_sp_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.sp_nav_fragment_container,
                        new SPProfileFragment()).commit();
                setTitle("My Profile");
                break;
            case R.id.nav_sp_services:
                getSupportFragmentManager().beginTransaction().replace(R.id.sp_nav_fragment_container,
                        new SPServiceFragment()).commit();
                setTitle("Services");
                break;

            case R.id.nav_sp_request:
                getSupportFragmentManager().beginTransaction().replace(R.id.sp_nav_fragment_container,
                        new SPRequestListFragment()).commit();
                setTitle("Request List");

                break;
            case R.id.nav_sp_history:
                getSupportFragmentManager().beginTransaction().replace(R.id.sp_nav_fragment_container,
                        new SPHistoryFragment()).commit();
                setTitle("History");
                break;
            case R.id.nav_sp_setting:
                getSupportFragmentManager().beginTransaction().replace(R.id.sp_nav_fragment_container,
                        new SPSettingFragment()).commit();
                setTitle("Setting");
                break;
            case R.id.nav_sp_rating_comment:
                getSupportFragmentManager().beginTransaction().replace(R.id.sp_nav_fragment_container,
                        new SPRatingCommentFragmnet()).commit();
                setTitle("Rating Comments");
                break;
            case R.id.nav_sp_share:
                Toast.makeText(this, "Share is called", Toast.LENGTH_SHORT).show();
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,
                        "Hey check out my app at: https://play.google.com/store/apps/details?id=com.google.android.apps.plus");
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                break;
            case R.id.nav_sp_about:
                getSupportFragmentManager().beginTransaction().replace(R.id.sp_nav_fragment_container,
                        new SPAboutUsFragment()).commit();
                setTitle("About Us");
                break;
            case R.id.nav_sp_contact:
                getSupportFragmentManager().beginTransaction().replace(R.id.sp_nav_fragment_container,
                        new SPContactUsFragment()).commit();
                setTitle("Contact Us");
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return false;
    }
}
