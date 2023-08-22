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
import com.services.joshi.serviceprovider.activities.user.UserServiceDetailsActivity;
import com.services.joshi.serviceprovider.extraclasses.ItemClickListener;
import com.services.joshi.serviceprovider.models.Provider;
import com.squareup.picasso.Picasso;

import java.util.List;

/*
 * RecyclerView Adapter
 * RecyclerView ViewHolder
 * */
public class ServicesAdapter extends RecyclerView.Adapter<ServicesAdapter.ServicesViewHolder> {

    private Context mCtx;
    private List<Provider> servicesList;

    ServicesViewHolder services;


    public ServicesAdapter(Context mCtx, List<Provider> servicesList) {
        this.mCtx = mCtx;
        this.servicesList = servicesList;
    }

    @NonNull
    @Override
    public ServicesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mCtx).inflate(R.layout.service_card_view, parent, false);
        services = new ServicesViewHolder(view);
        return services;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final ServicesViewHolder holder, final int position) {
        final Provider serviceModel = servicesList.get(position);

        holder.textViewServiceName.setText(serviceModel.getCategory().getCategory_name());
        holder.textViewProviderName.setText(serviceModel.getFname() + " " + serviceModel.getLname());


        if (serviceModel.getUserImage().isEmpty()) {
            holder.imageViewServiceImage.setImageResource(R.drawable.user_logo);
        } else {
            Picasso.with(mCtx).load(serviceModel.getUserImage()).into(holder.imageViewServiceImage);
        }

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClicked) {
                if (isLongClicked) {
                    Toast.makeText(mCtx, serviceModel.getFname() + " " + serviceModel.getLname(), Toast.LENGTH_SHORT).show();
                }
                else{
                    Intent intent = new Intent(mCtx, UserServiceDetailsActivity.class);
                    intent.putExtra("activity_name", "Service Detail");
                    intent.putExtra("provider_id",serviceModel.getId());
                    intent.putExtra("name",serviceModel.getFname()+" "+serviceModel.getLname());
                    intent.putExtra("email",serviceModel.getEmail());
                    intent.putExtra("phone",serviceModel.getPhone());
                    intent.putExtra("address",serviceModel.getAddress());
                    intent.putExtra("gender",serviceModel.getGender());
                    intent.putExtra("provider_image",serviceModel.getUserImage());
                    intent.putExtra("category",serviceModel.getCategory().getCategory_name());
                    mCtx.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return servicesList.size();
    }


    static class ServicesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        ImageView imageViewServiceImage;
        TextView textViewProviderName, textViewServiceName, textViewRating;

        private ItemClickListener itemClickListener;

        @SuppressLint("WrongViewCast")
        public ServicesViewHolder(View itemView) {
            super(itemView);
            imageViewServiceImage = itemView.findViewById(R.id.service_card_view_image);
            textViewServiceName = itemView.findViewById(R.id.service_card_view_textView_serviceName);
            textViewProviderName = itemView.findViewById(R.id.service_card_view_textView_providerName);
            textViewRating = itemView.findViewById(R.id.service_card_view_textView_rating);

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

