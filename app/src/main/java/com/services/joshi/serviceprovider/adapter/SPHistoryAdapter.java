package com.services.joshi.serviceprovider.adapter;

import android.annotation.SuppressLint;
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
import com.services.joshi.serviceprovider.activities.serviceprovider.SPHistoryDetailsActivity;
import com.services.joshi.serviceprovider.extraclasses.ItemClickListener;
import com.services.joshi.serviceprovider.models.Services;
import com.services.joshi.serviceprovider.models.User;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SPHistoryAdapter extends RecyclerView.Adapter<SPHistoryAdapter.SPHistoryViewHolder> {

    Context context;
    List<Services> mData;

    SPHistoryViewHolder spHistoryViewHolder;

    public SPHistoryAdapter(Context context, List<Services> mData) {
        this.context = context;
        this.mData = mData;
    }

    @NonNull
    @Override
    public SPHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.sp_history_card_view, parent, false);
        spHistoryViewHolder = new SPHistoryViewHolder(view);
        return spHistoryViewHolder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final SPHistoryViewHolder holder, final int position) {
        final Services history = mData.get(position);

        if (history.getUser() != null) {
            holder.textViewProviderName.setText(history.getUser().getFname() + " " + history.getUser().getLname());
            holder.textViewServiceName.setText(history.getProvider().getCategory().getCategory_name());
            holder.textViewRating.setText(String.valueOf(4.5));

            if (history.getStatus() == 1) {
                holder.textViewServiceStatus.setText("Completed");
                holder.textViewServiceStatus.setTextColor(context.getResources().getColor(R.color.success_msg));

            }
            if (history.getStatus() == 0) {
                holder.textViewServiceStatus.setTextColor(context.getResources().getColor(R.color.warning));
                holder.textViewServiceStatus.setText("Pending");
            }
            if (history.getStatus() == 2) {
                holder.textViewServiceStatus.setTextColor(context.getResources().getColor(R.color.red));
                holder.textViewServiceStatus.setText("Rejected");
            }
            if (history.getStatus() == 4) {
                holder.textViewServiceStatus.setTextColor(context.getResources().getColor(R.color.info));
                holder.textViewServiceStatus.setText("Accepted");
            }

            if (history.getUser().getUserImage() == null) {
                holder.imageViewhistoryImage.setImageResource(R.drawable.no_image);
            } else {
                Picasso.with(context).load(history.getUser().getUserImage()).into(holder.imageViewhistoryImage);
            }
        } else {
            Toast.makeText(context, "User Not found", Toast.LENGTH_LONG).show();
        }

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClicked) {
                if (isLongClicked) {
                    Toast.makeText(context, history.getUser().getFname() + " " + history.getUser().getLname(), Toast.LENGTH_SHORT).show();
                } else {
                    final User user = history.getUser();
                    Intent intent = new Intent(context, SPHistoryDetailsActivity.class);
                    intent.putExtra("activity_name", "Service Detail");
                    intent.putExtra("provider_id", user.getId());
                    intent.putExtra("name", user.getFname() + " " + user.getLname());
                    intent.putExtra("email", user.getEmail());
                    intent.putExtra("phone", user.getPhone());
                    intent.putExtra("address", user.getAddress());
                    intent.putExtra("gender", user.getGender());
                    intent.putExtra("status", mData.get(position).getStatus());
                    intent.putExtra("user_image", user.getUserImage());
                    intent.putExtra("created_date", mData.get(position).getUpdatedAt());
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    static class SPHistoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        ImageView imageViewhistoryImage;
        TextView textViewProviderName, textViewServiceName, textViewRating, textViewServiceStatus;

        private ItemClickListener itemClickListener;

        @SuppressLint("WrongViewCast")
        public SPHistoryViewHolder(View itemView) {
            super(itemView);

            imageViewhistoryImage = (ImageView) itemView.findViewById(R.id.sphistory_card_view_textView_image);
            textViewProviderName = (TextView) itemView.findViewById(R.id.sphistory_card_view_textView_providername);
            textViewServiceName = (TextView) itemView.findViewById(R.id.sphistory_card_view_textView_serviceName);
            textViewRating = (TextView) itemView.findViewById(R.id.sphistory_card_view_textView_rating);
            textViewServiceStatus = itemView.findViewById(R.id.sphistory_card_view_textView_status);

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
