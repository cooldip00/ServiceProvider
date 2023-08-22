package com.services.joshi.serviceprovider.activities.user;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.sdsmdg.tastytoast.TastyToast;
import com.services.joshi.serviceprovider.R;
import com.services.joshi.serviceprovider.api.RetrofitClient;
import com.services.joshi.serviceprovider.repository.DefaultResponse;
import com.services.joshi.serviceprovider.storage.SharedPrefManager;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserSearchDetailActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView backpresses, imageViewproviderimage;
    Button buttonRequest, buttonCancel;
    TextView textViewemail, textViewphone, textViewaddress, textViewName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_search_detail);

        backpresses = (ImageView) findViewById(R.id.user_search_detail_back_button);
        imageViewproviderimage = (ImageView) findViewById(R.id.user_search_detail_image);

        buttonRequest = (Button) findViewById(R.id.user_search_detail_button_request);
        buttonCancel = (Button) findViewById(R.id.user_search_detail_button_cancel);

        textViewemail = (TextView) findViewById(R.id.user_search_detail_email);
        textViewphone = (TextView) findViewById(R.id.user_search_detail_phone);
        textViewaddress = (TextView) findViewById(R.id.user_search_detail_address);
        textViewName = findViewById(R.id.user_search_detail_name);

        checkServiceStatus();

        textViewName.setText(getIntent().getStringExtra("name"));
        textViewemail.setText(getIntent().getStringExtra("email"));
        textViewphone.setText(getIntent().getStringExtra("phone"));
        textViewaddress.setText(getIntent().getStringExtra("address"));

        if (getIntent().getStringExtra("user_image") != null) {
            Picasso.with(this).load(getIntent().getStringExtra("user_image")).into(imageViewproviderimage);
        } else {
            imageViewproviderimage.setImageResource(R.drawable.no_image);
        }

        buttonRequest.setOnClickListener(this);
        buttonCancel.setOnClickListener(this);
        backpresses.setOnClickListener(this);
    }

    private void requestAdd() {

        int user_id = SharedPrefManager.getInstance(this).getUser().getId();
        int provider_id = getIntent().getIntExtra("provider_id", 0);


        Call<DefaultResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .addServiceRequest(user_id, provider_id);

        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                if (response.isSuccessful() && !response.body().isError()) {
                    buttonCancel.setVisibility(View.VISIBLE);
                    buttonRequest.setVisibility(View.INVISIBLE);
                    TastyToast.makeText(UserSearchDetailActivity.this, response.body().getMessage(), TastyToast.LENGTH_LONG, TastyToast.SUCCESS).show();
                } else {
                    buttonCancel.setVisibility(View.INVISIBLE);
                    buttonRequest.setVisibility(View.VISIBLE);
                    TastyToast.makeText(UserSearchDetailActivity.this, "Some thing is wrong", TastyToast.LENGTH_LONG, TastyToast.ERROR).show();
                }
            }

            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {
                TastyToast.makeText(UserSearchDetailActivity.this, t.getMessage(), TastyToast.LENGTH_LONG, TastyToast.ERROR).show();
            }
        });

    }

    private void requestCancel() {

        int user_id = SharedPrefManager.getInstance(this).getUser().getId();
        int provider_id = getIntent().getIntExtra("provider_id", 0);


        Call<DefaultResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .cancelServiceRequest(user_id, provider_id);

        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                if (response.isSuccessful() && !response.body().isError()) {
                    buttonCancel.setVisibility(View.INVISIBLE);
                    buttonRequest.setVisibility(View.VISIBLE);
                    TastyToast.makeText(UserSearchDetailActivity.this, response.body().getMessage(), TastyToast.LENGTH_LONG, TastyToast.SUCCESS).show();
                } else {
                    buttonCancel.setVisibility(View.VISIBLE);
                    buttonRequest.setVisibility(View.INVISIBLE);
                    TastyToast.makeText(UserSearchDetailActivity.this, "Some thing is wrong", TastyToast.LENGTH_LONG, TastyToast.ERROR).show();
                }
            }

            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {
                TastyToast.makeText(UserSearchDetailActivity.this, t.getMessage(), TastyToast.LENGTH_LONG, TastyToast.ERROR).show();
            }
        });

    }

    private void checkServiceStatus() {

        int user_id = SharedPrefManager.getInstance(this).getUser().getId();
        int provider_id = getIntent().getIntExtra("provider_id", 0);


        Call<DefaultResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .checkServiceRequest(user_id, provider_id);

        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                if (response.isSuccessful()) {
                    if (!response.body().isError()) {
                        buttonCancel.setVisibility(View.VISIBLE);
                        buttonRequest.setVisibility(View.INVISIBLE);
                    } else {
                        buttonCancel.setVisibility(View.INVISIBLE);
                        buttonRequest.setVisibility(View.VISIBLE);
                    }
                } else {
                    TastyToast.makeText(UserSearchDetailActivity.this, "Some thing is wrong", TastyToast.LENGTH_LONG, TastyToast.ERROR).show();
                }
            }

            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {
                TastyToast.makeText(UserSearchDetailActivity.this, t.getMessage(), TastyToast.LENGTH_LONG, TastyToast.ERROR).show();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_search_detail_back_button:
                onBackPressed();
                break;
            case R.id.user_search_detail_button_request:
                requestAdd();
                break;
            case R.id.user_search_detail_button_cancel:
                requestCancel();
                break;
        }
    }
}
