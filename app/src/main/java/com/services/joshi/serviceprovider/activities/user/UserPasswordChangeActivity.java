package com.services.joshi.serviceprovider.activities.user;

import android.app.ProgressDialog;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sdsmdg.tastytoast.TastyToast;
import com.services.joshi.serviceprovider.R;
import com.services.joshi.serviceprovider.api.RetrofitClient;
import com.services.joshi.serviceprovider.repository.DefaultResponse;
import com.services.joshi.serviceprovider.storage.SharedPrefManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserPasswordChangeActivity extends AppCompatActivity {

    EditText editTextOldpassword, editTextnewpassword, editTextconfirmpassword;
    Button btnchangepassword;

    String oldpassword, newpassword, conformpassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_chnage_password_activity);
        setTitle("Change Password");
        setTitleColor(getResources().getColor(R.color.white));

        editTextOldpassword = (EditText) findViewById(R.id.user_old_password);
        editTextnewpassword = (EditText) findViewById(R.id.user_new_password);
        editTextconfirmpassword = (EditText) findViewById(R.id.user_new_confirm_password);

        btnchangepassword = (Button) findViewById(R.id.user_btn_paasword_change);

        btnchangepassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changePassowrd();
            }
        });
    }

    private void changePassowrd() {

        oldpassword = editTextOldpassword.getText().toString().trim();
        newpassword = editTextnewpassword.getText().toString().trim();
        conformpassword = editTextconfirmpassword.getText().toString().trim();

        if (oldpassword.isEmpty()) {
            TastyToast.makeText(this, "Old Password is Required", Toast.LENGTH_LONG, TastyToast.ERROR).show();

            return;
        }
        if (oldpassword.length() < 6) {
            TastyToast.makeText(this, "Password Length Must Be 6 Character", Toast.LENGTH_LONG, TastyToast.ERROR).show();

            return;
        }

        if (newpassword.isEmpty()) {
            TastyToast.makeText(this, "New Password is Required", Toast.LENGTH_LONG, TastyToast.ERROR).show();
            return;
        }
        if (newpassword.length() < 6) {
            TastyToast.makeText(this, "Password Length Must Be 6 Character", Toast.LENGTH_LONG, TastyToast.ERROR).show();

            return;
        }

        if (conformpassword.isEmpty()) {
            TastyToast.makeText(this, "Conform Password is Required", Toast.LENGTH_LONG, TastyToast.ERROR).show();

            return;
        }
        if (conformpassword.length() < 6) {
            TastyToast.makeText(this, "Password Length Must Be 6 Character", Toast.LENGTH_LONG, TastyToast.ERROR).show();

            return;
        }

        final ProgressDialog progressDialog = new ProgressDialog(this, R.style.AppCompatAlertDialogStyle);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        int id = SharedPrefManager.getInstance(this).getUser().getId();
        Call<DefaultResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .changeUserPassword(id, oldpassword, newpassword, conformpassword);

        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    if (!response.body().isError()) {
                        TastyToast.makeText(UserPasswordChangeActivity.this, response.body().getMessage(), Toast.LENGTH_LONG, TastyToast.SUCCESS).show();
                        onBackPressed();
                    } else {
                        TastyToast.makeText(UserPasswordChangeActivity.this, response.body().getMessage(), Toast.LENGTH_LONG, TastyToast.ERROR).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {
                progressDialog.dismiss();
                TastyToast.makeText(UserPasswordChangeActivity.this, t.getMessage(), Toast.LENGTH_LONG, TastyToast.ERROR).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
