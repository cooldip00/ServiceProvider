package com.services.joshi.serviceprovider.activities.serviceprovider.spsignupfragments;

import android.graphics.PorterDuff;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.services.joshi.serviceprovider.R;
import com.services.joshi.serviceprovider.activities.serviceprovider.SPSignUpActivity;


public class SPSignUpFragmentOne extends Fragment {

    public NonSwipeableViewPager nonSwipeableViewPager;
    Button btn;
    EditText editTextfname, editTextmname, editTextlname;
    String fname, mname, lname;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sp_signup_1, container, false);
        nonSwipeableViewPager = new NonSwipeableViewPager(getActivity());
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editTextfname = view.findViewById(R.id.ed_signup_sp_fname);
        editTextmname = view.findViewById(R.id.ed_signup_sp_mname);
        editTextlname = view.findViewById(R.id.ed_signup_sp_lname);

        btn = view.findViewById(R.id.btn_next_sp_sign_fragment_one);

        //Button click event
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passDataToSecondFregment();
            }
        });
    }

    private void passDataToSecondFregment() {

        fname = editTextfname.getText().toString().trim();
        mname = editTextmname.getText().toString().trim();
        lname = editTextlname.getText().toString().trim();

        if (fname.isEmpty()) {
            editTextfname.setError("First Name Is Required");
            editTextfname.requestFocus();
            editTextfname.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.holo_red_light), PorterDuff.Mode.LIGHTEN);
            return;
        }

        if (mname.isEmpty()) {
            editTextmname.setError("Middle Name Is Required");
            editTextmname.requestFocus();
            editTextmname.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.holo_red_light), PorterDuff.Mode.LIGHTEN);
            return;
        }

        if (lname.isEmpty()) {
            editTextlname.setError("Last Name Is Required");
            editTextlname.requestFocus();
            editTextlname.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.holo_red_light), PorterDuff.Mode.LIGHTEN);
            return;
        }

        Fragment fragment = new SPSignUpFragmentTwo();

        ((SPSignUpActivity)getActivity()).FNAME = fname;
        ((SPSignUpActivity)getActivity()).LNAME = lname;
        ((SPSignUpActivity)getActivity()).MNAME = mname;

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.sp_signup_fragment_two,fragment).addToBackStack(null).commit();

        ((SPSignUpActivity) getActivity()).goToNextPage();
    }

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

}