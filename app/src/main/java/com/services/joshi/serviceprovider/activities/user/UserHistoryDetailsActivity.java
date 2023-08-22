package com.services.joshi.serviceprovider.activities.user;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.braintreepayments.api.dropin.DropInActivity;
import com.braintreepayments.api.dropin.DropInRequest;
import com.braintreepayments.api.dropin.DropInResult;
import com.braintreepayments.api.models.PaymentMethodNonce;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;
import com.sdsmdg.tastytoast.TastyToast;
import com.services.joshi.serviceprovider.PaymentService;
import com.services.joshi.serviceprovider.R;
import com.services.joshi.serviceprovider.adapter.RatingCommentAdapter;
import com.services.joshi.serviceprovider.api.Api;
import com.services.joshi.serviceprovider.api.Common;
import com.services.joshi.serviceprovider.api.RetrofitClient;
import com.services.joshi.serviceprovider.models.Bill;
import com.services.joshi.serviceprovider.models.Provider;
import com.services.joshi.serviceprovider.models.ProviderWithRating;
import com.services.joshi.serviceprovider.models.RatingUser;
import com.services.joshi.serviceprovider.repository.DefaultResponse;
import com.services.joshi.serviceprovider.repository.GaneratedBillResponse;
import com.services.joshi.serviceprovider.repository.LastPendingPaymentResponse;
import com.services.joshi.serviceprovider.repository.RatingUserResponse;
import com.services.joshi.serviceprovider.storage.SharedPrefManager;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import cz.msebera.android.httpclient.Header;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserHistoryDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    TextView textViewName, textViewService, textViewCreated, textViewAddress, textViewPhone, textViewEmail, textViewStatus;
    ImageView imageViewUserimage, imageViewBackButton;
    Button buttonPayment, buttonRating;

    private static final String CHANNEL_ID = "PAYMENT";
    public static final String CHANNEL_NAME = "Payment ";
    public static final String CHANEL_DESCRIPTION = "Payment Successfully Completed";


    RecyclerView recyclerView;
    RatingCommentAdapter ratingCommentAdapter;

    List<RatingUser> ratingUsers;

    int Provider_id;


    private static final int REQUEST_CODE = 7777;
    String token, amount;
    HashMap<String, String> paramHash;

    Api mServiceScalars;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_history_details);
        setTitle("History Details");

        mServiceScalars = Common.getScalarsApi();


        loadRecyclerViewData();


        ratingUsers = new ArrayList<>();

        textViewName = (TextView) findViewById(R.id.user_history_detail_name);
        textViewService = (TextView) findViewById(R.id.user_history_detail_category);
        textViewCreated = (TextView) findViewById(R.id.user_history_detail_crated_at);
        textViewAddress = findViewById(R.id.user_history_detail_address);
        textViewPhone = findViewById(R.id.user_history_detail_phone);
        textViewEmail = findViewById(R.id.user_history_detail_email);
        textViewStatus = findViewById(R.id.user_history_detail_status);
        imageViewUserimage = (ImageView) findViewById(R.id.user_history_detail_image);
        imageViewBackButton = findViewById(R.id.user_history_detail_back_button);
        buttonPayment = findViewById(R.id.user_history_detail_makepayment_button);
        buttonRating = findViewById(R.id.user_history_detail_rating_button);
        recyclerView = findViewById(R.id.user_history_detail_rating_and_comment_recycerview);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        buttonPayment.setEnabled(false);
        buttonRating.setEnabled(false);


        if (getIntent().getExtras() == null) {
            final int user_id = SharedPrefManager.getInstance(this).getUser().getId();
            Call<LastPendingPaymentResponse> call = RetrofitClient
                    .getInstance()
                    .getApi()
                    .getLastPanddingPaymnetDetail(user_id);

            call.enqueue(new Callback<LastPendingPaymentResponse>() {
                @Override
                public void onResponse(Call<LastPendingPaymentResponse> call, Response<LastPendingPaymentResponse> response) {
                    if (response.isSuccessful()) {

                        ProviderWithRating bill = response.body().getBill().getService().getProvider();
                        Provider_id = bill.getId();

                        textViewName.setText(bill.getFname() + " " + bill.getLname());
                        textViewEmail.setText(bill.getEmail());
                        textViewPhone.setText(bill.getEmail());
                        textViewAddress.setText(bill.getPhone().toString());
                        textViewService.setText(bill.getCategory().getCategory_name());
                        textViewCreated.setText(response.body().getBill().getService().getUpdatedAt());

                        if (response.body().getBill().getService().getStatus() == 0) {
                            textViewStatus.setTextColor(getResources().getColor(R.color.warning));
                            textViewStatus.setText("Pending");
                        }
                        if (response.body().getBill().getService().getStatus() == 1) {
                            buttonRating.setEnabled(true);
                            loadToken();
                            textViewStatus.setTextColor(getResources().getColor(R.color.success));
                            textViewStatus.setText("Completed");
                        }
                        if (response.body().getBill().getService().getStatus() == 2) {
                            textViewStatus.setTextColor(getResources().getColor(R.color.red));
                            textViewStatus.setText("Rejected");
                        }
                        if (response.body().getBill().getService().getStatus() == 4) {
                            textViewStatus.setTextColor(getResources().getColor(R.color.info));
                            textViewStatus.setText("Accepted");
                        }

                        Picasso.with(UserHistoryDetailsActivity.this).load(bill.getUserImage()).into(imageViewUserimage);
                    }
                }

                @Override
                public void onFailure(Call<LastPendingPaymentResponse> call, Throwable t) {
                    TastyToast.makeText(UserHistoryDetailsActivity.this, t.getMessage(), TastyToast.LENGTH_LONG, TastyToast.ERROR).show();
                }
            });
        } else {

            Provider_id = getIntent().getIntExtra("provider_id", 0);

            textViewName.setText(getIntent().getStringExtra("name"));
            textViewEmail.setText(getIntent().getStringExtra("email"));
            textViewPhone.setText(getIntent().getStringExtra("phone"));
            textViewAddress.setText(getIntent().getStringExtra("address"));
            textViewService.setText(getIntent().getStringExtra("category"));
            textViewCreated.setText(" At : " + getIntent().getStringExtra("created_date"));

            if (getIntent().getIntExtra("status", 0) == 0) {
                textViewStatus.setTextColor(getResources().getColor(R.color.warning));
                textViewStatus.setText("Pending");
            }
            if (getIntent().getIntExtra("status", 0) == 1) {
                buttonRating.setEnabled(true);
                loadToken();
                textViewStatus.setTextColor(getResources().getColor(R.color.success));
                textViewStatus.setText("Completed");
            }
            if (getIntent().getIntExtra("status", 0) == 2) {
                textViewStatus.setTextColor(getResources().getColor(R.color.red));
                textViewStatus.setText("Rejected");
            }
            if (getIntent().getIntExtra("status", 0) == 4) {
                textViewStatus.setTextColor(getResources().getColor(R.color.info));
                textViewStatus.setText("Accepted");
            }

            Picasso.with(this).load(getIntent().getStringExtra("provider_image")).into(imageViewUserimage);
        }


        loadRecyclerViewData();

        imageViewBackButton.setOnClickListener(this);
        buttonRating.setOnClickListener(this);
        buttonPayment.setOnClickListener(this);
    }

    private void loadRecyclerViewData() {

        int id = getIntent().getIntExtra("provider_id", 0);

        Call<RatingUserResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .getComments(id);

        call.enqueue(new Callback<RatingUserResponse>() {
            @Override
            public void onResponse(Call<RatingUserResponse> call, Response<RatingUserResponse> response) {
                if (response.body() == null) {
                    Toast.makeText(UserHistoryDetailsActivity.this, "getting null data", Toast.LENGTH_SHORT).show();
                    // Stopping swipe refresh
                    return;
                }

                ratingUsers = response.body().getRatingUser();

                if (ratingUsers.size() == 0) {
                    return;
                }

                ratingCommentAdapter = new RatingCommentAdapter(UserHistoryDetailsActivity.this, ratingUsers);
                recyclerView.setAdapter(ratingCommentAdapter);

            }

            @Override
            public void onFailure(Call<RatingUserResponse> call, Throwable t) {
                Toast.makeText(UserHistoryDetailsActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    private void makePaymentStatusDone() {
        int user_id = SharedPrefManager.getInstance(this).getUser().getId();
        Call<DefaultResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .makePaymentDone(user_id, Provider_id);
        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                if (response.isSuccessful() && !response.body().isError()) {
//                    TastyToast.makeText(UserHistoryDetailsActivity.this, response.body().getMessage(), TastyToast.LENGTH_LONG, TastyToast.SUCCESS).show();
                    Log.v("MESSAGE", response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {
                TastyToast.makeText(UserHistoryDetailsActivity.this, t.getMessage(), TastyToast.LENGTH_LONG, TastyToast.SUCCESS).show();
            }
        });
    }

    private void submitPayment() {
        DropInRequest dropInRequest = new DropInRequest().clientToken(token);
        startActivityForResult(dropInRequest.getIntent(this), REQUEST_CODE);
    }

    private void loadToken() {

        final ProgressDialog mDialog;

        mDialog = new ProgressDialog(UserHistoryDetailsActivity.this, android.R.style.Theme_DeviceDefault_Dialog);
        mDialog.setCancelable(false);
        mDialog.setMessage("Please wait");
        mDialog.show();

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(Common.API_TOKEN_URL, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                buttonPayment.setEnabled(false);
                mDialog.dismiss();
                Toast.makeText(UserHistoryDetailsActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                buttonPayment.setEnabled(true);
                mDialog.dismiss();
                token = responseString;
            }
        });
    }

    private void rateTheProvider() {

        LayoutInflater layoutInflater = LayoutInflater.from(this);
        final View popupView = layoutInflater.inflate(R.layout.user_rating_popup_dialog, null);

        AlertDialog.Builder alerBuilder = new AlertDialog.Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);

        alerBuilder.setView(popupView);
        final RatingBar ratingBar = popupView.findViewById(R.id.user_rating_popup_rating);
        final EditText editTextComment = popupView.findViewById(R.id.user_rating_popup_eidttext);

        alerBuilder
                .setCancelable(false)
                .setPositiveButton("Rate",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                String comment = editTextComment.getText().toString();
                                String rating = String.valueOf(ratingBar.getRating());
                                int user_id = SharedPrefManager.getInstance(UserHistoryDetailsActivity.this).getUser().getId();
                                int provider_id = getIntent().getIntExtra("provider_id", 0);

                                final ProgressDialog progressDialog = new ProgressDialog(UserHistoryDetailsActivity.this, R.style.AppCompatAlertDialogStyle);
                                progressDialog.setMessage("Please wait...");
                                progressDialog.show();

                                Call<DefaultResponse> call = RetrofitClient
                                        .getInstance()
                                        .getApi()
                                        .rateProvider(user_id, provider_id, rating, comment);

                                call.enqueue(new Callback<DefaultResponse>() {
                                    @Override
                                    public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                                        progressDialog.dismiss();
                                        if (response.isSuccessful()) {
                                            TastyToast.makeText(UserHistoryDetailsActivity.this, response.body().getMessage(), TastyToast.LENGTH_LONG, TastyToast.SUCCESS).show();
                                            buttonRating.setEnabled(false);
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<DefaultResponse> call, Throwable t) {
                                        progressDialog.dismiss();
                                        TastyToast.makeText(UserHistoryDetailsActivity.this, t.getMessage(), TastyToast.LENGTH_LONG, TastyToast.ERROR).show();
                                    }
                                });
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

        AlertDialog alertDialog = alerBuilder.create();

        alertDialog.show();

    }

    private void showBill() {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        final View paymentPopupView = layoutInflater.inflate(R.layout.user_bill_dialog, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);

        builder.setView(paymentPopupView);
        final TextView textViewDescription = paymentPopupView.findViewById(R.id.user_bill_dialog_description);
        final TextView textViewAmount = paymentPopupView.findViewById(R.id.user_bill_dialog_amount);

        int user_id = SharedPrefManager.getInstance(UserHistoryDetailsActivity.this).getUser().getId();
        int provider_id = getIntent().getIntExtra("provider_id", 0);

        Call<GaneratedBillResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .getBill(user_id, provider_id);

        call.enqueue(new Callback<GaneratedBillResponse>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<GaneratedBillResponse> call, Response<GaneratedBillResponse> response) {
                if (response.isSuccessful()) {
                    textViewAmount.setText(response.body().getBill().getAmount().toString());
                    textViewDescription.setText(response.body().getBill().getDescription());
                }
            }

            @Override
            public void onFailure(Call<GaneratedBillResponse> call, Throwable t) {
                TastyToast.makeText(UserHistoryDetailsActivity.this, t.getMessage(), TastyToast.LENGTH_LONG, TastyToast.ERROR).show();
            }
        });

        builder.setTitle("Payment Details")
                .setPositiveButton("Make Payment", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        submitPayment();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                DropInResult result = data.getParcelableExtra(DropInResult.EXTRA_DROP_IN_RESULT);
                PaymentMethodNonce nonce = result.getPaymentMethodNonce();
                final String strNounce = nonce.getNonce();

                int provider_id;
                if (getIntent().getExtras() == null) {
                    provider_id = Provider_id;
                    Toast.makeText(this, provider_id + "", Toast.LENGTH_SHORT).show();
                } else {
                    provider_id = getIntent().getIntExtra("provider_id", 0);
                    Toast.makeText(this, provider_id + "", Toast.LENGTH_SHORT).show();

                }

                paramHash = new HashMap<>();

                int user_id = SharedPrefManager.getInstance(this).getUser().getId();


                Call<GaneratedBillResponse> call = RetrofitClient
                        .getInstance()
                        .getApi()
                        .getBill(user_id, provider_id);

                call.enqueue(new Callback<GaneratedBillResponse>() {
                    @Override
                    public void onResponse(Call<GaneratedBillResponse> call, Response<GaneratedBillResponse> response) {
                        if (response.isSuccessful()) {

                            paramHash.put("amount", response.body().getBill().getAmount().toString());
                            paramHash.put("nonce", strNounce);
                            Toast.makeText(UserHistoryDetailsActivity.this, "amount taken from database", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<GaneratedBillResponse> call, Throwable t) {
                        TastyToast.makeText(UserHistoryDetailsActivity.this, t.getMessage(), TastyToast.LENGTH_LONG, TastyToast.ERROR).show();
                    }
                });

                if (amount == null)
                    paramHash.put("amount", "50");
                paramHash.put("nonce", strNounce);

                sendPayments();

            } else if (resultCode == RESULT_CANCELED)
                Toast.makeText(this, "User Cancel", Toast.LENGTH_SHORT).show();
            else {
                Exception error = (Exception) data.getSerializableExtra(DropInActivity.EXTRA_ERROR);
                Log.e("Result Error", error.toString());
            }
        }
    }

    private void sendPayments() {
        mServiceScalars.payment(paramHash.get("nonce"), paramHash.get("amount"))
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (response.body() != null) {

                            if (response.body().toString().contains("Successful")) {
                                Toast.makeText(UserHistoryDetailsActivity.this, "Transaction Successful!", Toast.LENGTH_SHORT).show();

                                makePaymentStatusDone();

                                Intent serviceIntent = new Intent(UserHistoryDetailsActivity.this, PaymentService.class);
                                stopService(serviceIntent);

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
                                    notificationChannel.setDescription(CHANEL_DESCRIPTION);
                                    NotificationManager notificationManager = getSystemService(NotificationManager.class);
                                    notificationManager.createNotificationChannel(notificationChannel);
                                }
                                displayNotification();
                                onBackPressed();

                            } else {
                                Toast.makeText(UserHistoryDetailsActivity.this, "Transaction Failed!", Toast.LENGTH_SHORT).show();
                            }
                        }
                        //Toast.makeText(MainActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                        Log.e("OnResponse", response.toString());
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Toast.makeText(UserHistoryDetailsActivity.this, "Failed" + t.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("OnResponse", t.getMessage());
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
            case R.id.user_history_detail_makepayment_button:
                showBill();
                break;
            case R.id.user_history_detail_rating_button:
                rateTheProvider();

                break;
            case R.id.user_history_detail_back_button:
                onBackPressed();
                break;
        }
    }

    private void displayNotification() {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this, CHANNEL_ID)
                        .setSmallIcon(R.drawable.user_logo)
                        .setContentTitle("Payment Done")
                        .setContentText("Payment is done to Provider")
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(1, builder.build());
    }
}
