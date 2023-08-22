package com.services.joshi.serviceprovider.activities.serviceprovider;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sdsmdg.tastytoast.TastyToast;
import com.services.joshi.serviceprovider.R;
import com.services.joshi.serviceprovider.api.RetrofitClient;
import com.services.joshi.serviceprovider.repository.ServiceAcceptResponse;
import com.services.joshi.serviceprovider.repository.ServiceRejectResponse;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SPServiceRequestDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    TextView textViewName, textViewAddress, textViewPhone, textViewEmail;
    ImageView imageViewUserimage, imageViewBackButton, imageViewDeleteButton;
    Button buttonGenerateBill, buttonAccept, buttonDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sp_activity_requestlist_details);
        setTitle("Request List Details");

        Toast.makeText(getBaseContext(), "Activity Called", Toast.LENGTH_SHORT).show();

        textViewName = (TextView) findViewById(R.id.sp_requestlist_detail_name);
        textViewAddress = findViewById(R.id.sp_requestlist_detail_address);
        textViewPhone = findViewById(R.id.sp_requestlist_detail_phone);
        textViewEmail = findViewById(R.id.sp_requestlist_detail_email);
        imageViewUserimage = (ImageView) findViewById(R.id.sp_requestlist_detail_image);

        imageViewBackButton = findViewById(R.id.sp_requestlist_detail_back_button);
        imageViewDeleteButton = findViewById(R.id.sp_requestlist_detail_delete);
        buttonGenerateBill = findViewById(R.id.sp_requestlist_detail_genratebill);
        buttonAccept = (Button) findViewById(R.id.sp_requestlist_detail_accept);
        buttonDelete = findViewById(R.id.sp_requestlist_detail_button_delete);
        buttonGenerateBill.setEnabled(false);

        if (getIntent().getIntExtra("status", 0) == 4) {
            buttonAccept.setVisibility(View.INVISIBLE);
            buttonDelete.setVisibility(View.VISIBLE);
            buttonGenerateBill.setEnabled(true);
        }

        textViewName.setText(getIntent().getStringExtra("name"));
        textViewEmail.setText(getIntent().getStringExtra("email"));
        textViewPhone.setText(getIntent().getStringExtra("phone"));
        textViewAddress.setText(getIntent().getStringExtra("address"));

        if (getIntent().hasExtra("user_image")) {
            Picasso.with(this).load(getIntent().getStringExtra("user_image")).into(imageViewUserimage);
        } else {
            imageViewUserimage.setImageResource(R.drawable.no_image);
        }


        imageViewBackButton.setOnClickListener(this);
        imageViewDeleteButton.setOnClickListener(this);
        buttonGenerateBill.setOnClickListener(this);
        buttonAccept.setOnClickListener(this);
        buttonDelete.setOnClickListener(this);

    }

    private void rejectRequest() {

        int id = getIntent().getIntExtra("service_id", -1);

        Call<ServiceRejectResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .rejectService(id);

        call.enqueue(new Callback<ServiceRejectResponse>() {
            @Override
            public void onResponse(Call<ServiceRejectResponse> call, Response<ServiceRejectResponse> response) {
                if (!response.body().isError()) {
                    TastyToast.makeText(SPServiceRequestDetailsActivity.this, "Some thing Went Wrong Try Again", Toast.LENGTH_LONG, TastyToast.ERROR).show();
                    onBackPressed();
                } else {
                    TastyToast.makeText(getApplicationContext(), "Service Request Rejected", Toast.LENGTH_LONG, TastyToast.SUCCESS).show();

                }
            }

            @Override
            public void onFailure(Call<ServiceRejectResponse> call, Throwable t) {
                TastyToast.makeText(SPServiceRequestDetailsActivity.this, t.getMessage(), Toast.LENGTH_LONG, TastyToast.ERROR).show();
            }
        });
    }

    private void acceptService() {

        int id = getIntent().getIntExtra("service_id", -1);

        Call<ServiceAcceptResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .acceptService(id);

        call.enqueue(new Callback<ServiceAcceptResponse>() {
            @Override
            public void onResponse(Call<ServiceAcceptResponse> call, Response<ServiceAcceptResponse> response) {
                if (response.body().isError()) {
                    TastyToast.makeText(SPServiceRequestDetailsActivity.this, "Some thing Went Wrong Try Again", Toast.LENGTH_LONG, TastyToast.ERROR).show();
                } else {
                    TastyToast.makeText(getApplicationContext(), "Service Request Accepted", Toast.LENGTH_LONG, TastyToast.SUCCESS).show();
                    buttonAccept.setVisibility(View.INVISIBLE);
                    buttonDelete.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<ServiceAcceptResponse> call, Throwable t) {
                TastyToast.makeText(getBaseContext(), t.getMessage(), Toast.LENGTH_LONG, TastyToast.ERROR).show();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sp_requestlist_detail_back_button:
                onBackPressed();
                break;

            case R.id.sp_requestlist_detail_delete:
                rejectRequest();
                break;

            case R.id.sp_requestlist_detail_accept:
                acceptService();
                break;

            case R.id.sp_requestlist_detail_button_delete:
                rejectRequest();
                break;

            case R.id.sp_requestlist_detail_genratebill:
                int id = getIntent().getIntExtra("service_id", -1);
                buttonGenerateBill.setEnabled(true);
                buttonDelete.setEnabled(false);
                RequestAcceptDialog requestAcceptDialog = new RequestAcceptDialog(this, id);
                requestAcceptDialog.show(getSupportFragmentManager(), "Request Accept");
                break;
        }
    }

}
