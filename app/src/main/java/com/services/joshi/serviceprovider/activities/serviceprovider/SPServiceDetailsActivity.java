package com.services.joshi.serviceprovider.activities.serviceprovider;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.services.joshi.serviceprovider.R;
import com.squareup.picasso.Picasso;

public class SPServiceDetailsActivity extends AppCompatActivity {

    TextView textViewName, textViewService, textViewRatting, textViewAddress, textViewPhone, textViewEmail;
    ImageView imageViewUserimage, imageViewBackButton;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spservice_inner);
        setTitle("Service Details");

        textViewName = (TextView) findViewById(R.id.sp_service_detail_name);
        textViewService = (TextView) findViewById(R.id.sp_service_detail_category);
        textViewRatting = (TextView) findViewById(R.id.sp_service_detail_rating);
        textViewAddress = findViewById(R.id.sp_service_detail_address);
        textViewPhone = findViewById(R.id.sp_service_detail_phone);
        textViewEmail = findViewById(R.id.sp_service_detail_email);
        imageViewUserimage = (ImageView) findViewById(R.id.sp_service_detail_image);
        imageViewBackButton = findViewById(R.id.sp_service_detail_back_button);

        textViewName.setText(getIntent().getStringExtra("name"));
        textViewEmail.setText(getIntent().getStringExtra("email"));
        textViewPhone.setText(getIntent().getStringExtra("phone"));
        textViewAddress.setText(getIntent().getStringExtra("address"));
        textViewService.setText(getIntent().getStringExtra("category"));
        textViewRatting.setText(getIntent().getStringExtra("rating"));

        Picasso.with(this).load(getIntent().getStringExtra("provider_image")).into(imageViewUserimage);

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
