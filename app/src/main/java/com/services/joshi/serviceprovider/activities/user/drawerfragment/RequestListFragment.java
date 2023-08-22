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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sdsmdg.tastytoast.TastyToast;
import com.services.joshi.serviceprovider.R;
import com.services.joshi.serviceprovider.adapter.RequestAdapter;
import com.services.joshi.serviceprovider.api.RetrofitClient;
import com.services.joshi.serviceprovider.models.Services;
import com.services.joshi.serviceprovider.repository.RequestServiceResponse;
import com.services.joshi.serviceprovider.storage.SharedPrefManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class RequestListFragment extends Fragment {

    ImageView imageViewNoFoundImage;
    TextView textViewNoFoundText;
    List<Services> servicesList;

    RequestAdapter requestAdapter;

    private RecyclerView recyclerView;

    SwipeRefreshLayout mSwipeRefreshLayout;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_fragment_requestlist, container, false);

        // SwipeRefreshLayout
        mSwipeRefreshLayout = view.findViewById(R.id.user_nav_requestlis_swipe_container);

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

        recyclerView = view.findViewById(R.id.request_recyclerview);
        textViewNoFoundText = view.findViewById(R.id.user_requestlist_nofound_text);
        imageViewNoFoundImage = view.findViewById(R.id.user_requestlist_nofound_image);

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

        int id = SharedPrefManager.getInstance(getActivity()).getUser().getId();
        // Showing refresh animation before making http call
        mSwipeRefreshLayout.setRefreshing(true);

        Call<RequestServiceResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .getRequestListProvider(id);

        call.enqueue(new Callback<RequestServiceResponse>() {
            @Override
            public void onResponse(Call<RequestServiceResponse> call, Response<RequestServiceResponse> response) {
                RequestServiceResponse cr = response.body();

                if (!cr.isError()) {

                    servicesList = cr.getServices();

                    if (servicesList.size() == 0) {
                        textViewNoFoundText.setVisibility(View.VISIBLE);
                        imageViewNoFoundImage.setVisibility(View.VISIBLE);
                        Toast.makeText(getActivity(), "No Data Found", Toast.LENGTH_SHORT).show();
                        mSwipeRefreshLayout.setRefreshing(false);
                        return;
                    } else {
                        requestAdapter = new RequestAdapter(getActivity(), servicesList);
                        recyclerView.setAdapter(requestAdapter);
                    }
                    // Stopping swipe refresh
                    mSwipeRefreshLayout.setRefreshing(false);
                } else {
                    TastyToast.makeText(getActivity(), "Unable Fetch Providers Try Again", Toast.LENGTH_LONG, TastyToast.ERROR).show();
                }

            }

            @Override
            public void onFailure(Call<RequestServiceResponse> call, Throwable t) {
                mSwipeRefreshLayout.setRefreshing(false);
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
