package com.services.joshi.serviceprovider.activities.serviceprovider.drawerfragment;


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

import com.services.joshi.serviceprovider.R;
import com.services.joshi.serviceprovider.adapter.SPHistoryAdapter;
import com.services.joshi.serviceprovider.api.RetrofitClient;
import com.services.joshi.serviceprovider.models.Services;
import com.services.joshi.serviceprovider.repository.SPHistoryResponse;
import com.services.joshi.serviceprovider.storage.SharedPrefManagerSP;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class SPHistoryFragment extends Fragment {

    RecyclerView recyclerView;

    SPHistoryAdapter historyAdapter;

    List<Services> historyList;

    SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.sp_fragment_history, container, false);

        // SwipeRefreshLayout
        mSwipeRefreshLayout = view.findViewById(R.id.sp_nav_fragment_history_swipe_container);

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

        recyclerView = view.findViewById(R.id.sphistory_recyclerview);

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

    private void loadRecyclerViewData(){
        // Showing refresh animation before making http call
        mSwipeRefreshLayout.setRefreshing(true);

        int id = SharedPrefManagerSP.getInstance(getActivity()).getProvider().getId();
        Call<SPHistoryResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .getProviderHistory(id);

        call.enqueue(new Callback<SPHistoryResponse>() {
            @Override
            public void onResponse(Call<SPHistoryResponse> call, Response<SPHistoryResponse> response) {
                if (response.body() == null) {
                    Toast.makeText(getActivity(), "getting null data", Toast.LENGTH_SHORT).show();
                    return;
                }

                historyList = response.body().getServices();

                if (historyList.size() == 0) {
                    Toast.makeText(getActivity(), "No History found", Toast.LENGTH_SHORT).show();
                    return;
                }

                historyAdapter = new SPHistoryAdapter(getActivity(), historyList);
                recyclerView.setAdapter(historyAdapter);

                // Stopping swipe refresh
                mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<SPHistoryResponse> call, Throwable t) {
                // Stopping swipe refresh
                mSwipeRefreshLayout.setRefreshing(false);
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStart() {
        super.onStart();
    }
}
