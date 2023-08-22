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

import com.services.joshi.serviceprovider.activities.user.UserHistoryDetailsActivity;
import com.services.joshi.serviceprovider.R;
import com.services.joshi.serviceprovider.extraclasses.ItemClickListener;
import com.services.joshi.serviceprovider.models.Services;
import com.squareup.picasso.Picasso;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    private Context mCtx;
    private List<Services> historyList;

    public HistoryAdapter(Context mCtx, List<Services> historyList) {
        this.mCtx = mCtx;
        this.historyList = historyList;
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.history_card_view, null);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final HistoryViewHolder holder, final int position) {
        final Services services = historyList.get(position);

        holder.imageViewhistoryImage.setImageResource(R.drawable.user_logo);
        holder.textViewProviderName.setText(services.getProvider().getFname() + " " + services.getProvider().getLname());
        holder.textViewServiceName.setText(services.getProvider().getCategory().getCategory_name());
        holder.textViewRating.setText("3.5");

        if (services.getStatus() == 1) {
            holder.textViewStatus.setText("Completed");
            holder.textViewStatus.setTextColor(mCtx.getResources().getColor(R.color.success_msg));

        }
        if (services.getStatus() == 0) {
            holder.textViewStatus.setText("Pending");
            holder.textViewStatus.setTextColor(mCtx.getResources().getColor(R.color.warning));
        }
        if (services.getStatus() == 2) {
            holder.textViewStatus.setText("Rejected");
            holder.textViewStatus.setTextColor(mCtx.getResources().getColor(R.color.red));
        }

        if (services.getProvider().getUserImage().isEmpty()) {
            holder.imageViewhistoryImage.setImageResource(R.drawable.user_logo);
        } else {
            Picasso.with(mCtx).load(services.getProvider().getUserImage()).into(holder.imageViewhistoryImage);
        }

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClicked) {
                if (isLongClicked) {
                    Toast.makeText(mCtx, services.getProvider().getFname() + " " + services.getProvider().getLname(), Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(mCtx, UserHistoryDetailsActivity.class);
                    intent.putExtra("activity_name", "Requested Service Detail");
                    intent.putExtra("provider_id", services.getProvider().getId());
                    intent.putExtra("status", historyList.get(position).getStatus());
                    intent.putExtra("name", services.getProvider().getFname() + " " + services.getProvider().getLname());
                    intent.putExtra("email", services.getProvider().getEmail());
                    intent.putExtra("phone", services.getProvider().getPhone());
                    intent.putExtra("address", services.getProvider().getAddress());
                    intent.putExtra("gender", services.getProvider().getGender());
                    intent.putExtra("provider_image", services.getProvider().getUserImage());
                    intent.putExtra("category", services.getProvider().getCategory().getCategory_name());
                    intent.putExtra("created_date", services.getUpdatedAt());
                    intent.putExtra("rating", String.valueOf(4.5));
                    mCtx.startActivity(intent);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    public class HistoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        ImageView imageViewhistoryImage;
        TextView textViewProviderName, textViewServiceName, textViewRating, textViewStatus;

        private ItemClickListener itemClickListener;

        public HistoryViewHolder(View itemView) {
            super(itemView);
            imageViewhistoryImage = (ImageView) itemView.findViewById(R.id.history_card_view_image);
            textViewProviderName = (TextView) itemView.findViewById(R.id.history_card_view_textView_providername);
            textViewServiceName = (TextView) itemView.findViewById(R.id.history_card_view_textView_serviceName);
            textViewRating = (TextView) itemView.findViewById(R.id.history_card_view_textView_rating);
            textViewStatus = itemView.findViewById(R.id.history_card_view_textView_status);

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
