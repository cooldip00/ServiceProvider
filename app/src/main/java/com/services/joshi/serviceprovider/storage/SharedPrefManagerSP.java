package com.services.joshi.serviceprovider.storage;

import android.content.Context;
import android.content.SharedPreferences;

import com.services.joshi.serviceprovider.models.Category;
import com.services.joshi.serviceprovider.models.Provider;
import com.services.joshi.serviceprovider.models.User;

import java.util.List;

public class SharedPrefManagerSP {

    private static final String SHARED_PREF_NAME = "provider_preference";

    private static SharedPrefManagerSP mInstance;
    private Context mCtx;

    private SharedPrefManagerSP(Context mCtx) {
        this.mCtx = mCtx;
    }

    public static synchronized SharedPrefManagerSP getInstance(Context mCtx) {
        if (mInstance == null) {
            mInstance = new SharedPrefManagerSP(mCtx);
        }
        return mInstance;
    }

    public void saveProvider(Provider user) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt("id", user.getId());
        editor.putString("fname", user.getFname());
        editor.putString("mname", user.getMname());
        editor.putString("lname", user.getLname());
        editor.putString("email", user.getEmail());
        editor.putString("address", user.getAddress());
        editor.putString("phone", user.getPhone());
        editor.putString("gender", user.getGender());
        editor.putString("aadhar_image", user.getAadharImage());
        editor.putString("user_image", user.getUserImage());
        editor.putInt("status", user.getStatus());
        editor.putString("latitude", user.getUserImage());
        editor.putString("longtitude", user.getUserImage());
        editor.putString("created_at", user.getUserImage());
        editor.putString("updated_at", user.getUserImage());

        editor.commit();
    }

    public void updateProvider(String fname, String mname, String lname, String address) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("fname", fname);
        editor.putString("mname", lname);
        editor.putString("lname", mname);
        editor.putString("address", address);

        editor.apply();
    }

    public void updateImage(String image) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("user_image", image);

        editor.apply();
    }

    public boolean isloggedIn() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt("id", -1) != -1;
    }

    public Provider getProvider() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new Provider(
                sharedPreferences.getInt("id", -1),
                sharedPreferences.getString("fname", null),
                sharedPreferences.getString("mname", null),
                sharedPreferences.getString("lname", null),
                sharedPreferences.getString("email", null),
                sharedPreferences.getString("phone", null),
                sharedPreferences.getString("address", null),
                sharedPreferences.getInt("category_id", -1),
                sharedPreferences.getString("gender", null),
                sharedPreferences.getString("aadhar_image", null),
                sharedPreferences.getString("user_image", null),
                sharedPreferences.getInt("status", -1),
                sharedPreferences.getString("latitude", null),
                sharedPreferences.getString("longtitude", null),
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
