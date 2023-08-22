package com.services.joshi.serviceprovider.activities.serviceprovider.drawerfragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.services.joshi.serviceprovider.R;
import com.services.joshi.serviceprovider.activities.user.UserHistoryDetailsActivity;
import com.services.joshi.serviceprovider.adapter.RatingCommentAdapter;
import com.services.joshi.serviceprovider.api.RetrofitClient;
import com.services.joshi.serviceprovider.models.RatingUser;
import com.services.joshi.serviceprovider.repository.RatingUserResponse;
import com.services.joshi.serviceprovider.storage.SharedPrefManagerSP;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SPRatingCommentFragmnet extends Fragment {

    ImageView imageViewNoFoundImage;
    TextView textViewNoFoundText;

    LinearLayout linearLayout;

    List<RatingUser> ratingUsers;

    RatingCommentAdapter ratingCommentAdapter;

    private RecyclerView recyclerView;

    SwipeRefreshLayout mSwipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sp_fragment_rating_and_comment, container, false);

        // SwipeRefreshLayout
        mSwipeRefreshLayout = view.findViewById(R.id.sp_nav_ratingcomment_swipe_container);

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

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.sp_ratingcomment_recyclerview);
        textViewNoFoundText = view.findViewById(R.id.sp_ratingcomment_nofound_text);
        imageViewNoFoundImage = view.findViewById(R.id.sp_ratingcomment_nofound_image);

        linearLayout = view.findViewById(R.id.sp_ratingcomment_nodata_found);

        ratingUsers = new ArrayList<>();
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

        mSwipeRefreshLayout.setRefreshing(true);

        int id = SharedPrefManagerSP.getInstance(getActivity()).getProvider().getId();

        Call<RatingUserResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .getComments(id);

        call.enqueue(new Callback<RatingUserResponse>() {
            @Override
            public void onResponse(Call<RatingUserResponse> call, Response<RatingUserResponse> response) {
                mSwipeRefreshLayout.setRefreshing(false);
                if (response.body() == null) {

                    Toast.makeText(getContext(), "getting null data", Toast.LENGTH_SHORT).show();
                    // Stopping swipe refresh
                    return;
                }

                ratingUsers = response.body().getRatingUser();

                if (ratingUsers.size() == 0) {
                    Toast.makeText(getActivity(), "No Comments Given To the Provider", Toast.LENGTH_LONG).show();
                    return;
                }

                ratingCommentAdapter = new RatingCommentAdapter(getActivity(), ratingUsers);
                recyclerView.setAdapter(ratingCommentAdapter);

            }

            @Override
            public void onFailure(Call<RatingUserResponse> call, Throwable t) {
                mSwipeRefreshLayout.setRefreshing(false);
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
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
