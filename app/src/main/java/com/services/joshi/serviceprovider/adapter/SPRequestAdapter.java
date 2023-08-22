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
import com.services.joshi.serviceprovider.activities.serviceprovider.SPServiceRequestDetailsActivity;
import com.services.joshi.serviceprovider.extraclasses.ItemClickListener;
import com.services.joshi.serviceprovider.models.Services;
import com.services.joshi.serviceprovider.models.User;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SPRequestAdapter extends RecyclerView.Adapter<SPRequestAdapter.SPRequestViewHolder> {

    Context mContext;
    List<Services> mData;

    public SPRequestAdapter(Context mContext, List<Services> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }


    @NonNull
    @Override
    public SPRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(mContext).inflate(R.layout.custom_sp_request_list_view, parent, false);

        final SPRequestViewHolder requestViewHolder = new SPRequestViewHolder(v);
        return requestViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final SPRequestViewHolder holder, final int position) {
        final User user = mData.get(position).getUser();
        holder.tv_service.setText(mData.get(position).getProvider().getCategory().getCategory_name());
        holder.tv_name.setText(mData.get(position).getUser().getFname() + " " + user.getLname());

        if (user.getUserImage() == null) {
            holder.request_image.setImageResource(R.drawable.no_image);
        } else {
            Picasso.with(mContext).load(user.getUserImage()).into(holder.request_image);
        }

        if (mData.get(position).getStatus() == 0) {
            holder.tv_status.setTextColor(mContext.getResources().getColor(R.color.warning));
            holder.tv_status.setText("Pending");
        }
        else if (mData.get(position).getStatus() == 4) {
            holder.tv_status.setTextColor(mContext.getResources().getColor(R.color.success));
            holder.tv_status.setText("Accepted");
        }

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClicked) {
                if (isLongClicked) {
                    Toast.makeText(mContext, user.getFname() + " " + user.getLname(), Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(mContext, SPServiceRequestDetailsActivity.class);
                    intent.putExtra("activity_name", "Requested Service Detail");
                    intent.putExtra("service_id", mData.get(position).getId());
                    intent.putExtra("user_id", user.getId());
                    intent.putExtra("status", mData.get(position).getStatus());
                    intent.putExtra("name", user.getFname() + " " + user.getLname());
                    intent.putExtra("email", user.getEmail());
                    intent.putExtra("phone", user.getPhone());
                    intent.putExtra("address", user.getAddress());
                    intent.putExtra("gender", user.getGender());
                    if (user.getUserImage() != null)
                        intent.putExtra("user_image", user.getUserImage());
                    intent.putExtra("category", mData.get(position).getProvider().getCategory().getCategory_name());
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

    public static class SPRequestViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        private ItemClickListener itemClickListener;

        private TextView tv_name, tv_service, tv_ratting, tv_status;
        private ImageView request_image;

        public SPRequestViewHolder(View itemView) {
            super(itemView);

            tv_service = (TextView) itemView.findViewById(R.id.sprequest_card_view_textView_serviceName);
            tv_name = (TextView) itemView.findViewById(R.id.sprequest_card_view_textView_providername);
            tv_ratting = (TextView) itemView.findViewById(R.id.sprequest_card_view_textView_rating);
            request_image = (ImageView) itemView.findViewById(R.id.sprequest_card_view_image);
            tv_status = itemView.findViewById(R.id.sprequest_card_view_textView_status);

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
