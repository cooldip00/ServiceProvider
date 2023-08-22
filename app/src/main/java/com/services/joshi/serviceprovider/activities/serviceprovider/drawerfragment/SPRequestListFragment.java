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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sdsmdg.tastytoast.TastyToast;
import com.services.joshi.serviceprovider.R;
import com.services.joshi.serviceprovider.adapter.SPRequestAdapter;
import com.services.joshi.serviceprovider.api.RetrofitClient;
import com.services.joshi.serviceprovider.models.Services;
import com.services.joshi.serviceprovider.repository.SPRequestServiceResponse;
import com.services.joshi.serviceprovider.storage.SharedPrefManagerSP;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class SPRequestListFragment extends Fragment {

    ImageView imageViewNoFoundImage;
    TextView textViewNoFoundText;
    List<Services> servicesList;

    LinearLayout linearLayout;

    SPRequestAdapter spRequestAdapter;

    private RecyclerView recyclerView;

    SwipeRefreshLayout mSwipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sp_fragment_requestlist, container, false);

        // SwipeRefreshLayout
        mSwipeRefreshLayout = view.findViewById(R.id.sp_nav_requestlis_swipe_container);

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

        recyclerView = view.findViewById(R.id.sprequest_recyclerview);
        textViewNoFoundText = view.findViewById(R.id.sp_requestlist_nofound_text);
        imageViewNoFoundImage = view.findViewById(R.id.sp_requestlist_nofound_image);

        linearLayout = view.findViewById(R.id.sp_requestlist_nodata_found);


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

        Call<SPRequestServiceResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .getProviderServiceRequest(SharedPrefManagerSP.getInstance(getActivity()).getProvider().getId());

        call.enqueue(new Callback<SPRequestServiceResponse>() {
            @Override
            public void onResponse(Call<SPRequestServiceResponse> call, Response<SPRequestServiceResponse> response) {
                SPRequestServiceResponse cr = response.body();

                if (!cr.isError()) {
                    servicesList = cr.getServices();

                    if (servicesList.size() != 0) {
                        linearLayout.setVisibility(View.INVISIBLE);
                        recyclerView.setVisibility(View.VISIBLE);

                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

                        spRequestAdapter = new SPRequestAdapter(getActivity(), servicesList);
                        recyclerView.setAdapter(spRequestAdapter);
                    } else {
                        recyclerView.setVisibility(View.INVISIBLE);
                        linearLayout.setVisibility(View.VISIBLE);
//                        Toast.makeText(getActivity(), "No Data Found", Toast.LENGTH_SHORT).show();
                        mSwipeRefreshLayout.setRefreshing(false);
                        return;
                    }
                    // Stopping swipe refresh
                    mSwipeRefreshLayout.setRefreshing(false);
                } else {
                    TastyToast.makeText(getActivity(), "Unable Fetch Providers Try Again", Toast.LENGTH_LONG, TastyToast.ERROR).show();
                }

            }

            @Override
            public void onFailure(Call<SPRequestServiceResponse> call, Throwable t) {
                mSwipeRefreshLayout.setRefreshing(false);
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        loadRecyclerViewData();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadRecyclerViewData();
    }

    @Override
    public void onPause() {
        super.onPause();
        loadRecyclerViewData();
    }
}
