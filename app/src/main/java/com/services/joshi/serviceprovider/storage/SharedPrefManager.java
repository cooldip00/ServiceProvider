package com.services.joshi.serviceprovider.storage;

import android.content.Context;
import android.content.SharedPreferences;

import com.services.joshi.serviceprovider.models.User;

import java.util.List;

public class SharedPrefManager {

    private static final String SHARED_PREF_NAME = "user_preference";

    private static SharedPrefManager mInstance;
    private Context mCtx;

    private SharedPrefManager(Context mCtx) {
        this.mCtx = mCtx;
    }

    public static synchronized SharedPrefManager getInstance(Context mCtx) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(mCtx);
        }
        return mInstance;
    }

    public void saveUser(List<User> user) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        User userData = user.get(0);
        editor.putInt("id", userData.getId());
        editor.putString("fname", userData.getFname());
        editor.putString("lname", userData.getLname());
        editor.putString("email", userData.getEmail());
        editor.putString("phone", userData.getPhone());
        editor.putString("gender", userData.getGender());
        editor.putString("address", userData.getAddress());
        editor.putString("user_image", userData.getUserImage());

        editor.apply();
    }

    public void updateUesrDetails(String fname, String lname, String address) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("fname", fname);
        editor.putString("lname", lname);
        editor.putString("address", address);

        editor.apply();
    }

    public boolean isloggedIn() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt("id", -1) != -1;
    }

    public String getUserImage() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString("user_image", null);

    }

    public User getUser() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new User(
                sharedPreferences.getInt("id", -1),
                sharedPreferences.getString("fname", null),
                sharedPreferences.getString("lname", null),
                sharedPreferences.getString("email", null),
                sharedPreferences.getString("phone", null),
                sharedPreferences.getString("gender", null),
                sharedPreferences.getString("address", null),
                sharedPreferences.getInt("status", -1),
                sharedPreferences.getString("created_at", null),
                sharedPreferences.getString("updated_at", null)
        );
    }

    public void clear() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
