package com.services.joshi.serviceprovider.activities.user;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.services.joshi.serviceprovider.R;
import com.squareup.picasso.Picasso;

public class UserServiceRequestDetailsActivity extends AppCompatActivity {

    TextView textViewservice, textViewName, textViewRatting, textViewEmail, textViewPhone, textViewAddress, textViewStatus;
    ImageView imageViewProvider, imageViewBackButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_activity_request_service_details);
        setTitle(getIntent().getStringExtra("activity_name"));

        textViewName = findViewById(R.id.user_request_service_detail_name);
        textViewEmail = findViewById(R.id.user_request_service_detail_email);
        textViewPhone = findViewById(R.id.user_request_service_detail_phone);
        textViewRatting = findViewById(R.id.user_request_service_detail_rating);
        textViewservice = findViewById(R.id.user_request_service_detail_category);
        imageViewProvider = findViewById(R.id.user_request_service_detail_image);
        textViewAddress = findViewById(R.id.user_request_service_detail_address);
        textViewStatus = findViewById(R.id.user_request_service_detail_status);
        imageViewBackButton = findViewById(R.id.user_request_service_detail_back_button);


        Picasso.with(this).load(getIntent().getStringExtra("provider_image")).into(imageViewProvider);

        textViewName.setText(getIntent().getStringExtra("name"));
        textViewEmail.setText(getIntent().getStringExtra("email"));
        textViewPhone.setText(getIntent().getStringExtra("phone"));
        textViewRatting.setText(4.5 + "");
        textViewservice.setText(getIntent().getStringExtra("category"));
        textViewAddress.setText(getIntent().getStringExtra("address"));

        if (getIntent().getIntExtra("status", 0) == 0) {
            textViewStatus.setTextColor(getResources().getColor(R.color.warning));
            textViewStatus.setText("Pending");
        }
        if (getIntent().getIntExtra("status", 0) == 4) {
            textViewStatus.setTextColor(getResources().getColor(R.color.success));
            textViewStatus.setText("Accepted");
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
