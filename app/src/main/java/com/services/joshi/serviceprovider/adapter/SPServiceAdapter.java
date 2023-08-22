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
import com.services.joshi.serviceprovider.activities.serviceprovider.SPServiceDetailsActivity;
import com.services.joshi.serviceprovider.extraclasses.ItemClickListener;
import com.services.joshi.serviceprovider.models.Provider;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SPServiceAdapter extends RecyclerView.Adapter<SPServiceAdapter.SPServiceViewHolder> {

    Context context;
    List<Provider> mData;
    SPServiceViewHolder spServiceViewHolder;

    public SPServiceAdapter(Context context, List<Provider> mData) {
        this.context = context;
        this.mData = mData;
    }

    @NonNull
    @Override
    public SPServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(context).inflate(R.layout.sp_service_card_view, parent, false);
        spServiceViewHolder = new SPServiceViewHolder(v);

        return spServiceViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final SPServiceViewHolder holder, final int position) {

        final Provider provider = mData.get(position);

        holder.textViewServiceName.setText(provider.getCategory().getCategory_name());
        holder.textViewProviderName.setText(provider.getFname() + " " + provider.getLname());
        holder.textViewRating.setText(String.valueOf(4.5));

        Picasso.with(context).load(provider.getUserImage()).into(holder.imageViewServiceImage);

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClicked) {
                if (isLongClicked) {
                    Toast.makeText(context, provider.getFname() + " " + provider.getLname(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Success" + String.valueOf(holder.getAdapterPosition()), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, SPServiceDetailsActivity.class);
                    intent.putExtra("activity_name", "Requested Service Detail");
                    intent.putExtra("provider_id", provider.getId());
                    intent.putExtra("name", provider.getFname() + " " + provider.getLname());
                    intent.putExtra("email", provider.getEmail());
                    intent.putExtra("phone", provider.getPhone());
                    intent.putExtra("address", provider.getAddress());
                    intent.putExtra("gender", provider.getGender());
                    intent.putExtra("provider_image", provider.getUserImage());
                    intent.putExtra("category", provider.getCategory().getCategory_name());
                    intent.putExtra("rating", String.valueOf(4.5));
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class SPServiceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{


        ImageView imageViewServiceImage;
        TextView textViewProviderName, textViewServiceName, textViewRating;

        private ItemClickListener itemClickListener;

        public SPServiceViewHolder(View itemView) {
            super(itemView);
            imageViewServiceImage = itemView.findViewById(R.id.spservice_card_view_image);
            textViewServiceName = itemView.findViewById(R.id.spservice_card_view_textView_serviceName);
            textViewProviderName = itemView.findViewById(R.id.spservice_card_view_textView_providerName);
            textViewRating = itemView.findViewById(R.id.spservice_card_view_textView_rating);

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
