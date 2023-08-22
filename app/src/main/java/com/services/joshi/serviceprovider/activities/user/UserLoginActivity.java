package com.services.joshi.serviceprovider.activities.user;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.sdsmdg.tastytoast.TastyToast;
import com.services.joshi.serviceprovider.PaymentService;
import com.services.joshi.serviceprovider.R;
import com.services.joshi.serviceprovider.api.RetrofitClient;
import com.services.joshi.serviceprovider.repository.LoginResponse;
import com.services.joshi.serviceprovider.storage.SharedPrefManager;

import androidx.core.content.ContextCompat;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserLoginActivity extends AppCompatActivity implements View.OnClickListener {

    TextView textViewEmail, textViewPassword;
    String email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        //changing statusbar color
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimaryDark));
        }

        textViewEmail = findViewById(R.id.ed_login_email);
        textViewPassword = findViewById(R.id.ed_login_password);

        findViewById(R.id.btn_user_login).setOnClickListener(this);
        findViewById(R.id.btn_user_login_goto_signup).setOnClickListener(this);
    }

    private void userLogin() {

        //validation
        email = textViewEmail.getText().toString().trim();
        password = textViewPassword.getText().toString().trim();

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            textViewEmail.setError("Invalid Email Pattern");
            textViewEmail.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.holo_red_light), PorterDuff.Mode.LIGHTEN);
            return;
        }

        if (email.isEmpty()) {
            textViewEmail.setError("Email Is Required");
            textViewEmail.requestFocus();
            textViewEmail.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.holo_red_light), PorterDuff.Mode.LIGHTEN);
            return;
        }


        if (password.isEmpty()) {
            textViewPassword.setError("Password is Required");
            textViewPassword.requestFocus();
            textViewPassword.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.holo_red_light), PorterDuff.Mode.LIGHTEN);
            return;
        }
        if (password.length() < 6) {
            textViewPassword.setError("Password Length Must Be 6 Character");
            textViewPassword.requestFocus();
            textViewPassword.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.holo_red_light), PorterDuff.Mode.LIGHTEN);
            return;
        }

        final ProgressDialog progressDialog = new ProgressDialog(this, R.style.AppCompatAlertDialogStyle);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        //retrofit call
        Call<LoginResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .userLogin(email, password);

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                progressDialog.dismiss();

                LoginResponse loginResponse = response.body();

                if (!loginResponse.isError()) {
                    String name = loginResponse.getUser().get(0).getFname() + " " + loginResponse.getUser().get(0).getLname();
                    TastyToast.makeText(UserLoginActivity.this, "hello " + name, TastyToast.LENGTH_LONG, TastyToast.SUCCESS);


                    SharedPrefManager.getInstance(UserLoginActivity.this)
                            .saveUser(loginResponse.getUser());



                    //if user is save we have to start fresh activity  directly for that
                    Intent intent = new Intent(UserLoginActivity.this, UserHomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else {
                    TastyToast.makeText(UserLoginActivity.this, loginResponse.getMessage(), TastyToast.LENGTH_LONG, TastyToast.ERROR);
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                progressDialog.dismiss();
                TastyToast.makeText(UserLoginActivity.this, t.getMessage(), TastyToast.LENGTH_LONG, TastyToast.ERROR);
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_user_login:
                userLogin();
                break;
            case R.id.btn_user_login_goto_signup:
                startActivity(new Intent(this, UserSignupActivity.class));
                break;
        }
    }
}
