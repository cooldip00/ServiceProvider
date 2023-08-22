package com.services.joshi.serviceprovider.adapter;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.services.joshi.serviceprovider.R;
import com.services.joshi.serviceprovider.activities.user.UserServiceRequestDetailsActivity;
import com.services.joshi.serviceprovider.extraclasses.ItemClickListener;
import com.services.joshi.serviceprovider.models.Provider;
import com.services.joshi.serviceprovider.models.ProviderWithRating;
import com.services.joshi.serviceprovider.models.Services;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.RequestViewHolder> {


    Context mContext;
    List<Services> mData;

    public RequestAdapter(Context mContext, List<Services> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(mContext).inflate(R.layout.custom_request_list_view, parent, false);
        final RequestViewHolder requestViewHolder = new RequestViewHolder(v);
        return requestViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final RequestViewHolder holder, final int position) {
        final ProviderWithRating provider = mData.get(position).getProvider();
        holder.tv_service.setText(provider.getCategory().getCategory_name());
        holder.tv_name.setText(provider.getFname() + " " + provider.getMname() + " " + provider.getLname());
        holder.tv_ratting.setText(String.valueOf(4.5));
        holder.textViewDate.setText(provider.getCreatedAt());

        if (provider.getUserImage().isEmpty()) {
            holder.request_image.setImageResource(R.drawable.user_logo);
        } else {
            Picasso.with(mContext).load(provider.getUserImage()).into(holder.request_image);
        }

        if (mData.get(position).getStatus() == 0) {
            holder.textViewStatus.setText("Pending");
            holder.textViewStatus.setTextColor(mContext.getResources().getColor(R.color.warning));
        }

        if (mData.get(position).getStatus() == 4) {
            holder.textViewStatus.setText("Accepted");
            holder.textViewStatus.setTextColor(mContext.getResources().getColor(R.color.success));
        }

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClicked) {
                if (isLongClicked) {
                    Toast.makeText(mContext, provider.getFname() + " " + provider.getLname(), Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(mContext, UserServiceRequestDetailsActivity.class);
                    intent.putExtra("activity_name", "Requested Service Detail");
                    intent.putExtra("status", mData.get(position).getStatus());
                    intent.putExtra("provider_id", provider.getId());
                    intent.putExtra("name", provider.getFname() + " " + provider.getLname());
                    intent.putExtra("email", provider.getEmail());
                    intent.putExtra("phone", provider.getPhone());
                    intent.putExtra("address", provider.getAddress());
                    intent.putExtra("gender", provider.getGender());
                    intent.putExtra("provider_image", provider.getUserImage());
                    intent.putExtra("category", provider.getCategory().getCategory_name());
                    intent.putExtra("created_date", mData.get(position).getCreatedAt());
                    mContext.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class RequestViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        private TextView tv_name, tv_service, tv_ratting, textViewDate, textViewStatus;
        private ImageView request_image;

        private ItemClickListener itemClickListener;


        public RequestViewHolder(View itemView) {
            super(itemView);

            tv_service = (TextView) itemView.findViewById(R.id.request_card_view_textView_serviceName);
            tv_name = (TextView) itemView.findViewById(R.id.request_card_view_textView_providername);
            tv_ratting = (TextView) itemView.findViewById(R.id.request_card_view_textView_rating);
            request_image = (ImageView) itemView.findViewById(R.id.request_card_view_image);
            textViewDate = itemView.findViewById(R.id.request_card_view_request_date);
            textViewStatus = itemView.findViewById(R.id.request_card_view_status);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View view) {
            itemClickListener.onClick(view, getAdapterPosition(), false);
        }

        @Override
        public boolean onLongClick(View view) {
            itemClickListener.onClick(view, getAdapterPosition(), true);
            return true;
        }
    }
}
