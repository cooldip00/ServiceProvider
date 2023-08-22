package com.services.joshi.serviceprovider.activities.user.drawerfragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.sdsmdg.tastytoast.TastyToast;
import com.services.joshi.serviceprovider.R;
import com.services.joshi.serviceprovider.adapter.UserHomeRecyclerAdapter;
import com.services.joshi.serviceprovider.api.RetrofitClient;
import com.services.joshi.serviceprovider.models.SearchedProvider;
import com.services.joshi.serviceprovider.repository.SearchProviderResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.content.Context.LOCATION_SERVICE;

public class HomeFragment extends Fragment {

    GridLayoutManager layoutManager;
    List<SearchedProvider> searchData;

    UserHomeRecyclerAdapter userHomeRecyclerAdapter;
    RecyclerView recyclerView;
    EditText editTextsearch;
    Button buttonsearch;
    LinearLayout linearLayoutNoDataFound;

    private LocationManager locationManager;
    private LocationListener listener;

    String lattitude, longtitude;
    double lattitude1, longtitude1;

    SwipeRefreshLayout mSwipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.user_fragment_home, container, false);

        mSwipeRefreshLayout = view.findViewById(R.id.user_nav_home_swipe_container);
        linearLayoutNoDataFound = view.findViewById(R.id.user_home_no_data_found);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadRecyclerViewData();
            }
        });
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);
        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        searchData = new ArrayList<>();

        locationManager = (LocationManager) Objects.requireNonNull(getActivity()).getSystemService(LOCATION_SERVICE);
        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                lattitude1 = location.getLatitude();
                longtitude1 = location.getLongitude();

                if ((lattitude1 != 0) && (longtitude1 != 0)) {
                    lattitude = String.valueOf(lattitude1);
                    longtitude = String.valueOf(longtitude1);
                } else {
                    lattitude = "0";
                    longtitude = "0";
                }

                Log.v("Locatin", location.getLongitude() + " " + location.getLatitude());
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

        gpsPermission();
        requestPermission();

        recyclerView = view.findViewById(R.id.user_home_recyclerview);
        editTextsearch = view.findViewById(R.id.edt_search_service);
        buttonsearch = view.findViewById(R.id.btn_search);


        layoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);


        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
                // Fetching data from server
                loadRecyclerViewData();
            }
        });

        buttonsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadSearchedRecyclerViewData();
            }
        });


    }

    private void loadSearchedRecyclerViewData() {

        String keyword;
        keyword = editTextsearch.getText().toString().trim();

        if (keyword.isEmpty()) {
            editTextsearch.setError("Enter the Keyword");
            editTextsearch.requestFocus();
            return;
        }

        // Showing refresh animation before making http call
        mSwipeRefreshLayout.setRefreshing(true);

        Call<SearchProviderResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .getSearchedProvider(keyword);

        call.enqueue(new Callback<SearchProviderResponse>() {
            @Override
            public void onResponse(Call<SearchProviderResponse> call, Response<SearchProviderResponse> response) {
                if (response.isSuccessful()) {

                    if (!response.body().getProvider().isEmpty()) {

                        searchData = response.body().getProvider();

                        if (searchData.size() > 0) {

                            linearLayoutNoDataFound.setVisibility(View.INVISIBLE);
                            recyclerView.setVisibility(View.VISIBLE);
                            userHomeRecyclerAdapter = new UserHomeRecyclerAdapter(getContext(), searchData);
                            recyclerView.setAdapter(userHomeRecyclerAdapter);

                            editTextsearch.setText("");
                        } else {
                            linearLayoutNoDataFound.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.INVISIBLE);
                            editTextsearch.setText("");
                        }
                    } else {
                        linearLayoutNoDataFound.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.INVISIBLE);
                    }
                    // Stopping swipe refresh
                    mSwipeRefreshLayout.setRefreshing(false);
                } else {
                    // Stopping swipe refresh
                    mSwipeRefreshLayout.setRefreshing(false);
                    TastyToast.makeText(getActivity(), "Getting Some Error Try Again", TastyToast.LENGTH_LONG, TastyToast.ERROR).show();
                }
            }

            @Override
            public void onFailure(Call<SearchProviderResponse> call, Throwable t) {
                // Stopping swipe refresh
                mSwipeRefreshLayout.setRefreshing(false);
                TastyToast.makeText(getActivity(), t.getMessage(), TastyToast.LENGTH_LONG, TastyToast.ERROR).show();
            }
        });
    }

    private void loadRecyclerViewData() {

        // Showing refresh animation before making http call
        mSwipeRefreshLayout.setRefreshing(true);


        Call<SearchProviderResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .getNearbyProvider(lattitude1, longtitude1);

        call.enqueue(new Callback<SearchProviderResponse>() {
            @Override
            public void onResponse(Call<SearchProviderResponse> call, Response<SearchProviderResponse> response) {

                if (response.isSuccessful()) {

                    if (!response.body().getProvider().isEmpty()) {

                        searchData = response.body().getProvider();

                        if (searchData.size() > 0) {

                            linearLayoutNoDataFound.setVisibility(View.INVISIBLE);
                            recyclerView.setVisibility(View.VISIBLE);

                            userHomeRecyclerAdapter = new UserHomeRecyclerAdapter(getContext(), searchData);
                            recyclerView.setAdapter(userHomeRecyclerAdapter);

                            editTextsearch.setText("");
                        } else {
                            linearLayoutNoDataFound.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.INVISIBLE);
                            editTextsearch.setText("");
                        }
                    } else {
                        linearLayoutNoDataFound.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.INVISIBLE);
                    }
                    // Stopping swipe refresh
                    mSwipeRefreshLayout.setRefreshing(false);
                } else {
                    // Stopping swipe refresh
                    mSwipeRefreshLayout.setRefreshing(false);
                    TastyToast.makeText(getActivity(), "GPS is not Detected", TastyToast.LENGTH_LONG, TastyToast.ERROR).show();
                }
            }

            @Override
            public void onFailure(Call<SearchProviderResponse> call, Throwable t) {
                // Stopping swipe refresh
                mSwipeRefreshLayout.setRefreshing(false);
                TastyToast.makeText(getActivity(), t.getMessage(), TastyToast.LENGTH_LONG, TastyToast.ERROR).show();
            }
        });

    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(Objects.requireNonNull(getActivity()), new String[]{ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET}, 10);
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

    private void gpsPermission() {
        //noinspection MissingPermission
        if (ActivityCompat.checkSelfPermission(Objects.requireNonNull(getActivity()), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
