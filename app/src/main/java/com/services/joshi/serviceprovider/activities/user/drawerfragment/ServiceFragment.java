package com.services.joshi.serviceprovider.activities.user.drawerfragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.sdsmdg.tastytoast.TastyToast;
import com.services.joshi.serviceprovider.R;
import com.services.joshi.serviceprovider.adapter.ServicesAdapter;
import com.services.joshi.serviceprovider.api.RetrofitClient;
import com.services.joshi.serviceprovider.models.Provider;
import com.services.joshi.serviceprovider.repository.CategoryResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServiceFragment extends Fragment {

    RecyclerView recyclerView;

    ServicesAdapter servicesAdapter;

    List<Provider> servicesList;

    SwipeRefreshLayout mSwipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_fragment_services, container, false);

        // SwipeRefreshLayout
        mSwipeRefreshLayout = view.findViewById(R.id.swipe_container);

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

        servicesList = new ArrayList<>();

        recyclerView = view.findViewById(R.id.service_recyclerview);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        /**
         * Showing Swipe Refresh animation on activity create
         * As animation won't start on onCreate, post runnable is used
         */

        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
                // Fetching data from server
                loadRecyclerViewData();
            }
        });


    }


    private void loadRecyclerViewData() {
        // Showing refresh animation before making http call
        mSwipeRefreshLayout.setRefreshing(true);

        Call<CategoryResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .getAllProviders();

        call.enqueue(new Callback<CategoryResponse>() {
            @Override
            public void onResponse(Call<CategoryResponse> call, Response<CategoryResponse> response) {
                CategoryResponse cr = response.body();

                if (!cr.getProvider().isEmpty()) {
                    servicesList = cr.getProvider();

                    servicesAdapter = new ServicesAdapter(getActivity(), servicesList);
                    recyclerView.setAdapter(servicesAdapter);

                    // Stopping swipe refresh
                    mSwipeRefreshLayout.setRefreshing(false);
                } else {
                    TastyToast.makeText(getActivity(), "Unale Fetch Providers Try Again", Toast.LENGTH_LONG, TastyToast.ERROR).show();
                }

            }

            @Override
            public void onFailure(Call<CategoryResponse> call, Throwable t) {
                mSwipeRefreshLayout.setRefreshing(false);
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
