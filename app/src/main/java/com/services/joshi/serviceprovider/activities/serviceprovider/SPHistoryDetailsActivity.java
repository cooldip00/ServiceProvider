package com.services.joshi.serviceprovider.activities.serviceprovider;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.services.joshi.serviceprovider.R;
import com.squareup.picasso.Picasso;

public class SPHistoryDetailsActivity extends AppCompatActivity {

    TextView textViewName, textViewService, textViewCreated, textViewAddress, textViewPhone, textViewEmail, textViewStatus;
    ImageView imageViewUserimage, imageViewBackButton;
    Button buttonPopup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sp_history_details);
        setTitle("History Details");

        textViewName = (TextView) findViewById(R.id.sp_history_detail_name);
        textViewCreated = (TextView) findViewById(R.id.sp_history_detail_crated_at);
        textViewAddress = findViewById(R.id.sp_history_detail_address);
        textViewPhone = findViewById(R.id.sp_history_detail_phone);
        textViewEmail = findViewById(R.id.sp_history_detail_email);
        textViewStatus = findViewById(R.id.sp_history_detail_status);
        imageViewUserimage = (ImageView) findViewById(R.id.sp_history_detail_image);
        imageViewBackButton = findViewById(R.id.sp_history_detail_back_button);

        textViewName.setText(getIntent().getStringExtra("name"));
        textViewEmail.setText(getIntent().getStringExtra("email"));
        textViewPhone.setText(getIntent().getStringExtra("phone"));
        textViewAddress.setText(getIntent().getStringExtra("address"));
        textViewCreated.setText(" At : " + getIntent().getStringExtra("created_date"));


        if (getIntent().getIntExtra("status", 0) == 0) {
            textViewStatus.setTextColor(getResources().getColor(R.color.warning));
            textViewStatus.setText("Pending");
        }
        if (getIntent().getIntExtra("status", 0) == 1) {
            textViewStatus.setTextColor(getResources().getColor(R.color.success));
            textViewStatus.setText("Done");
        }
        if (getIntent().getIntExtra("status", 0) == 2) {
            textViewStatus.setTextColor(getResources().getColor(R.color.red));
            textViewStatus.setText("Rejected");
        }
        if (getIntent().getIntExtra("status", 0) == 4) {
            textViewStatus.setTextColor(getResources().getColor(R.color.info));
            textViewStatus.setText("Accepted");
        }


        if (getIntent().hasExtra("user_image")) {
            Picasso.with(this).load(getIntent().getStringExtra("user_image")).into(imageViewUserimage);
        } else {
            imageViewUserimage.setImageResource(R.drawable.no_image);
        }

        imageViewBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
