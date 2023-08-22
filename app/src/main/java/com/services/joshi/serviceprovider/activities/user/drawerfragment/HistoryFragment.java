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

import com.services.joshi.serviceprovider.R;
import com.services.joshi.serviceprovider.adapter.HistoryAdapter;
import com.services.joshi.serviceprovider.api.RetrofitClient;
import com.services.joshi.serviceprovider.models.Services;
import com.services.joshi.serviceprovider.storage.SharedPrefManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryFragment extends Fragment {
    RecyclerView recyclerView, recyclerViewComment;
    List<Services> historyList;
//    List<CommentData> commentList;

    HistoryAdapter historyAdapter;
    TextView textViewNoFoundText;
    ImageView imageViewNoFoundImage;

    SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_fragment_history, container, false);

        // SwipeRefreshLayout
        mSwipeRefreshLayout = view.findViewById(R.id.user_nav_history_swipe_container);

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
        super.onCreate(savedInstanceState);

        historyList = new ArrayList<>();
        recyclerView = view.findViewById(R.id.history_recyclerview);
        recyclerViewComment = view.findViewById(R.id.history_comment_recyclerview);
        textViewNoFoundText = view.findViewById(R.id.user_history_nofound_text);
        imageViewNoFoundImage = view.findViewById(R.id.user_history_nofound_image);

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

        int id = SharedPrefManager.getInstance(getActivity()).getUser().getId();
        Call<List<Services>> call = RetrofitClient
                .getInstance()
                .getApi()
                .getHistory(id);

        call.enqueue(new Callback<List<Services>>() {
            @Override
            public void onResponse(Call<List<Services>> call, Response<List<Services>> response) {
                if (response.body() == null) {
                    Toast.makeText(getActivity(), "getting null data", Toast.LENGTH_SHORT).show();
                    // Stopping swipe refresh
                    mSwipeRefreshLayout.setRefreshing(false);
                    return;
                }
                historyList = response.body();

                if (historyList.size() == 0) {
                    Toast.makeText(getActivity(), "No History found", Toast.LENGTH_SHORT).show();
                    textViewNoFoundText.setVisibility(View.VISIBLE);
                    imageViewNoFoundImage.setVisibility(View.VISIBLE);
                    return;
                }
                historyAdapter = new HistoryAdapter(getActivity(), historyList);
                recyclerView.setAdapter(historyAdapter);

                // Stopping swipe refresh
                mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<List<Services>> call, Throwable t) {
                // Stopping swipe refresh
                mSwipeRefreshLayout.setRefreshing(false);
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
