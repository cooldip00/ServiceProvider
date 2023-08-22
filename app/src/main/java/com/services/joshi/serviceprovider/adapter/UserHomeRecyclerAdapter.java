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

import com.services.joshi.serviceprovider.R;
import com.services.joshi.serviceprovider.activities.user.UserSearchDetailActivity;
import com.services.joshi.serviceprovider.extraclasses.ItemClickListener;
import com.services.joshi.serviceprovider.models.SearchedProvider;
import com.services.joshi.serviceprovider.storage.SharedPrefManager;
import com.squareup.picasso.Picasso;

import java.util.List;

public class UserHomeRecyclerAdapter extends RecyclerView.Adapter<UserHomeRecyclerAdapter.MyHomeViewHolder> {

    Context context;
    List<SearchedProvider> mData;

    public UserHomeRecyclerAdapter(Context context, List<SearchedProvider> mData) {
        this.context = context;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyHomeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v;
        v = LayoutInflater.from(context).inflate(R.layout.custom_home_grid_layout, viewGroup, false);
        final MyHomeViewHolder requestViewHolder = new MyHomeViewHolder(v);
        return new MyHomeViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHomeViewHolder myHomeViewHolder, int position) {

        final SearchedProvider searchProviderResponse = mData.get(position);
        myHomeViewHolder.textViewservice.setText(searchProviderResponse.getCategory().getCategory_name());
        myHomeViewHolder.textViewname.setText(searchProviderResponse.getFname() + " " + searchProviderResponse.getMname() + " " + searchProviderResponse.getLname());
        myHomeViewHolder.textViewratting.setText(String.valueOf(String.valueOf(4.5)));

        Picasso.with(context).load(searchProviderResponse.getUserImage()).into(myHomeViewHolder.imageViewimage);

        myHomeViewHolder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClicked) {
                Intent intent = new Intent(context, UserSearchDetailActivity.class);
                intent.putExtra("user_id", SharedPrefManager.getInstance(context).getUser().getId());
                intent.putExtra("provider_id", searchProviderResponse.getId());
                intent.putExtra("name", searchProviderResponse.getFname() + " " +
                        searchProviderResponse.getMname() + " " +
                        searchProviderResponse.getLname());
                intent.putExtra("email", searchProviderResponse.getEmail());
                intent.putExtra("phone", searchProviderResponse.getPhone()+"");
                intent.putExtra("address", searchProviderResponse.getAddress());
                intent.putExtra("user_image", searchProviderResponse.getUserImage());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyHomeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        ImageView imageViewimage;
        TextView textViewservice, textViewname, textViewratting;
        private ItemClickListener itemClickListener;

        public MyHomeViewHolder(@NonNull View itemView) {
            super(itemView);

            imageViewimage = itemView.findViewById(R.id.home_card_view_image);
            textViewservice = itemView.findViewById(R.id.home_card_view_textView_serviceName);
            textViewname = itemView.findViewById(R.id.home_card_view_textView_providerName);
            textViewratting = itemView.findViewById(R.id.home_card_view_textView_rating);

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


