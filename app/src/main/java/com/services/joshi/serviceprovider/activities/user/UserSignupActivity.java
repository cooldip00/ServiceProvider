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
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.sdsmdg.tastytoast.TastyToast;
import com.services.joshi.serviceprovider.R;
import com.services.joshi.serviceprovider.api.RetrofitClient;
import com.services.joshi.serviceprovider.repository.RegisterResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserSignupActivity extends AppCompatActivity implements View.OnClickListener {

    EditText ed_user_fname, ed_user_lname, ed_user_email, ed_user_phone, ed_user_address, ed_user_password;
    RadioGroup radioGroup_gender;
    String fname, lname, email, phone, address, password, gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_signup);

        //changing statusbar color
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimaryDark));
        }

        //all EditText Initialization
        ed_user_fname = findViewById(R.id.ed_signup_user_fname);
        ed_user_lname = findViewById(R.id.ed_signup_user_lname);
        ed_user_email = findViewById(R.id.ed_signup_user_email);
        ed_user_phone = findViewById(R.id.ed_signup_user_phone);
        ed_user_password = findViewById(R.id.ed_signup_user_password);
        ed_user_address = findViewById(R.id.ed_signup_user_address);
        radioGroup_gender = findViewById(R.id.user_radioGroup_gender);

        findViewById(R.id.btn_user_signup).setOnClickListener(this);
        findViewById(R.id.btn_singup_goto_login).setOnClickListener(this);
    }

    private void userSignUp() {

        //get text from EditText
        fname = ed_user_fname.getText().toString().trim();
        lname = ed_user_lname.getText().toString().trim();
        email = ed_user_email.getText().toString().trim();
        password = ed_user_password.getText().toString().trim();
        address = ed_user_address.getText().toString().trim();
        phone = ed_user_phone.getText().toString().trim();

        //on gender selection
        switch (radioGroup_gender.getCheckedRadioButtonId()) {
            case R.id.user_radioGroup_gender_male:
                gender = "male";
                break;
            case R.id.user_radioGroup_gender_female:
                gender = "female";
                break;
            default:
                gender = null;
                break;
        }

        //on chnage selected gender
        radioGroup_gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                gender = null;
                switch (checkedId) {
                    case R.id.user_radioGroup_gender_male:
                        gender = "male";
                        break;
                    case R.id.user_radioGroup_gender_female:
                        Toast.makeText(UserSignupActivity.this, gender + " is selected", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });


        //Validation of EditText

        if (fname.isEmpty()) {
            ed_user_fname.setError("First Name Is Required");
            ed_user_fname.requestFocus();
            ed_user_fname.getBackground().getCurrent().setColorFilter(getResources().getColor(android.R.color.holo_red_light), PorterDuff.Mode.SCREEN);
            return;
        }

        if (lname.isEmpty()) {
            ed_user_lname.setError("Last Name Is Required");
            ed_user_lname.requestFocus();
            ed_user_lname.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.holo_red_light), PorterDuff.Mode.LIGHTEN);
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            ed_user_email.setError("Invalid Email Pattern");
            ed_user_email.requestFocus();
            ed_user_email.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.holo_red_light), PorterDuff.Mode.LIGHTEN);
            return;
        }

        if (email.isEmpty()) {
            ed_user_email.setError("Email Is Required");
            ed_user_email.requestFocus();
            ed_user_email.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.holo_red_light), PorterDuff.Mode.LIGHTEN);
            return;
        }


        if (password.isEmpty()) {
            ed_user_password.setError("Password is Required");
            ed_user_password.requestFocus();
            ed_user_password.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.holo_red_light), PorterDuff.Mode.LIGHTEN);
            return;
        }

        if (password.length() < 6) {
            ed_user_password.setError("Password Length Must Be 6 Character");
            ed_user_password.requestFocus();
            ed_user_password.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.holo_red_light), PorterDuff.Mode.LIGHTEN);
            return;
        }

        if (phone.isEmpty()) {
            ed_user_phone.setError("Phone Number Is Required");
            ed_user_phone.requestFocus();
            ed_user_phone.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.holo_red_light), PorterDuff.Mode.LIGHTEN);
            return;
        }

        if (address.isEmpty()) {
            ed_user_address.setError("Address is Required");
            ed_user_address.requestFocus();
            ed_user_address.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.holo_red_light), PorterDuff.Mode.LIGHTEN);
            return;
        }

        if (gender == null) {
            TastyToast.makeText(UserSignupActivity.this, "Gender is not selected", TastyToast.LENGTH_LONG, TastyToast.ERROR);
            return;
        }

        //ProgressDialog creation
        final ProgressDialog progressDialog = new ProgressDialog(this, R.style.AppCompatAlertDialogStyle);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        Call<RegisterResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .createUser(fname, lname, email, password, phone, address, gender);

        call.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                progressDialog.dismiss();
                RegisterResponse dr = response.body();
                if (response.isSuccessful() && !dr.isError()) {
                    TastyToast.makeText(UserSignupActivity.this, dr.getMessage(), TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
                    startActivity(new Intent(UserSignupActivity.this, UserLoginActivity.class));
                    finish();
                } else if (response.isSuccessful() && dr.isError()) {
                    TastyToast.makeText(UserSignupActivity.this, dr.getMessage(), TastyToast.LENGTH_LONG, TastyToast.ERROR);
                } else {
                    TastyToast.makeText(UserSignupActivity.this, "Some error Try Again", TastyToast.LENGTH_LONG, TastyToast.ERROR);
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                progressDialog.hide();
                TastyToast.makeText(UserSignupActivity.this, t.getMessage(), TastyToast.LENGTH_LONG, TastyToast.ERROR);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_singup_goto_login:
                startActivity(new Intent(this, UserLoginActivity.class));
                finish();
                break;
            case R.id.btn_user_signup:
                userSignUp();
                break;
        }
    }
}