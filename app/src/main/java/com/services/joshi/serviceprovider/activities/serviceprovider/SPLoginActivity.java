package com.services.joshi.serviceprovider.activities.serviceprovider;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.sdsmdg.tastytoast.TastyToast;
import com.services.joshi.serviceprovider.R;
import com.services.joshi.serviceprovider.api.RetrofitClient;
import com.services.joshi.serviceprovider.repository.SPLoginResponse;
import com.services.joshi.serviceprovider.storage.SharedPrefManagerSP;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SPLoginActivity extends AppCompatActivity implements View.OnClickListener {

    EditText editTextEmail, editTextPassword;

    String email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sp_login);

        //changing statusbar color
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimaryDark));
        }

        editTextEmail = findViewById(R.id.ed_sp_login_email);
        editTextPassword = findViewById(R.id.ed_sp_login_password);

        findViewById(R.id.btn_sp_login).setOnClickListener(this);
        findViewById(R.id.btn_sp_login_goto_signup).setOnClickListener(this);
    }

    private void providerLogin() {

        //validation
        email = editTextEmail.getText().toString().trim();
        password = editTextPassword.getText().toString().trim();

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Invalid Email Pattern");
            editTextEmail.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.holo_red_light), PorterDuff.Mode.LIGHTEN);
            return;
        }

        if (email.isEmpty()) {
            editTextEmail.setError("Email Is Required");
            editTextEmail.requestFocus();
            editTextEmail.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.holo_red_light), PorterDuff.Mode.LIGHTEN);
            return;
        }


        if (password.isEmpty()) {
            editTextPassword.setError("Password is Required");
            editTextPassword.requestFocus();
            editTextPassword.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.holo_red_light), PorterDuff.Mode.LIGHTEN);
            return;
        }
        if (password.length() < 6) {
            editTextPassword.setError("Password Length Must Be 6 Character");
            editTextPassword.requestFocus();
            editTextPassword.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.holo_red_light), PorterDuff.Mode.LIGHTEN);
            return;
        }

        final ProgressDialog progressDialog = new ProgressDialog(this, R.style.AppCompatAlertDialogStyle);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        //retrofit call
        Call<SPLoginResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .providerLogin(email, password);

        call.enqueue(new Callback<SPLoginResponse>() {
            @Override
            public void onResponse(Call<SPLoginResponse> call, Response<SPLoginResponse> response) {

                SPLoginResponse spLoginResponse = response.body();
                progressDialog.dismiss();

                if (spLoginResponse.getStatusCode() == 200) {
                    SharedPrefManagerSP.getInstance(SPLoginActivity.this)
                            .saveProvider(spLoginResponse.getProvider());

                    //if user is save we have to start fresh activity  directly for that
                    Intent intent = new Intent(SPLoginActivity.this, SPHomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    return;
                }
                if (spLoginResponse.getStatusCode() == 302 || spLoginResponse.getStatusCode() == 303 || spLoginResponse.getStatusCode() == 301 || spLoginResponse.getStatusCode() == 300 || spLoginResponse.getStatusCode() == 304) {
                    TastyToast.makeText(SPLoginActivity.this, spLoginResponse.getMessage(), Toast.LENGTH_LONG, TastyToast.ERROR).show();
                    return;
                }

            }

            @Override
            public void onFailure(Call<SPLoginResponse> call, Throwable t) {
                progressDialog.dismiss();
                TastyToast.makeText(SPLoginActivity.this, t.getMessage(), Toast.LENGTH_LONG, TastyToast.ERROR).show();
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sp_login:
                providerLogin();
                break;
            case R.id.btn_sp_login_goto_signup:
                startActivity(new Intent(this, SPSignUpActivity.class));
                finish();
                break;
        }

    }
}
