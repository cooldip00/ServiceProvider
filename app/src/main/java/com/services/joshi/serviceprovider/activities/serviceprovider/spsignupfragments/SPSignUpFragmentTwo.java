package com.services.joshi.serviceprovider.activities.serviceprovider.spsignupfragments;

import android.graphics.PorterDuff;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.services.joshi.serviceprovider.R;
import com.services.joshi.serviceprovider.activities.serviceprovider.SPSignUpActivity;


public class            SPSignUpFragmentTwo extends Fragment {

    String email,password,phoone_no;
    EditText editTextEmail,editTextPassword,editTextPhone_no;
    Button btn_next,btn_back;

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sp_signup_2, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editTextEmail = view.findViewById(R.id.ed_signup_sp_email);
        editTextPassword = view.findViewById(R.id.ed_signup_sp_password);
        editTextPhone_no = view.findViewById(R.id.ed_signup_sp_phone_no);

        btn_back = view.findViewById(R.id.btn_back_sp_sign_fragment_two);
        btn_next = view.findViewById(R.id.btn_next_sp_sign_fragment_two);

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passDataToThirdFregment();
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((SPSignUpActivity)getActivity()).goToBackPage();
            }
        });
    }

    private void passDataToThirdFregment(){

        email =  editTextEmail.getText().toString().trim();
        password = editTextPassword.getText().toString().trim();
        phoone_no = editTextPhone_no.getText().toString().trim();

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Invalid Email Pattern");
            editTextEmail.requestFocus();
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

        if (phoone_no.isEmpty()) {
            editTextPhone_no.setError("Phone Number Is Required");
            editTextPhone_no.requestFocus();
            editTextPhone_no.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.holo_red_light), PorterDuff.Mode.LIGHTEN);
            return;
        }

        Fragment fragment = new SPSignUpFragmentThree();

        ((SPSignUpActivity)getActivity()).EMAIL = email;
        ((SPSignUpActivity)getActivity()).PHONE_NO = phoone_no;
        ((SPSignUpActivity)getActivity()).PASSWORD = password;

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.sp_signup_fragment_three, fragment).addToBackStack(null).detach(fragment).commit();

        ((SPSignUpActivity)getActivity()).goToNextPage();

    }
}
