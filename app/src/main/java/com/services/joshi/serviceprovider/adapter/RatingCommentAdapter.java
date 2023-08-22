package com.services.joshi.serviceprovider.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.services.joshi.serviceprovider.R;
import com.services.joshi.serviceprovider.extraclasses.ItemClickListener;
import com.services.joshi.serviceprovider.models.RatingUser;
import com.services.joshi.serviceprovider.repository.RatingUserResponse;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Callback;

public class RatingCommentAdapter extends RecyclerView.Adapter<RatingCommentAdapter.RatingComentViewHolder> {

    private Context mCtx;
    private List<RatingUser> ratingUsers;

    public RatingCommentAdapter(Context mCtx, List<RatingUser> ratingUsers) {
        this.mCtx = mCtx;
        this.ratingUsers = ratingUsers;
    }


    @NonNull
    @Override
    public RatingComentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mCtx);
        View view = layoutInflater.inflate(R.layout.custome_rating_list, null);
        return new RatingComentViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RatingComentViewHolder holder, int position) {
        final RatingUser user = ratingUsers.get(position);

        holder.textViewName.setText(user.getUser().getFname() +
                " " + user.getUser().getLname());
        holder.ratingBarProvder.setRating(Float.parseFloat(user.getRating()));
        holder.textViewComment.setText(user.getComment());
        if (user.getUser().getUserImage() != null) {
            Picasso.with(mCtx).load(user.getUser().getUserImage()).into(holder.imageView);
        } else {
            holder.imageView.setImageResource(R.drawable.no_image);
        }

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClicked) {
                if (isLongClicked) {
                    Toast.makeText(mCtx, user.getUser().getFname() + " " + user.getUser().getLname(), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(mCtx, user.getComment(), Toast.LENGTH_LONG).show();

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return ratingUsers.size();
    }

    public class RatingComentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        ImageView imageView;
        TextView textViewName, textViewComment;
        RatingBar ratingBarProvder;

        private ItemClickListener itemClickListener;

        public RatingComentViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.list_user_image);
            textViewName = itemView.findViewById(R.id.list_user_name);
            textViewComment = itemView.findViewById(R.id.list_user_comment);
            ratingBarProvder = itemView.findViewById(R.id.list_user_rating);

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
