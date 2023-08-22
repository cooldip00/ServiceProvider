package com.services.joshi.serviceprovider.activities.serviceprovider.spsignupfragments;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.sdsmdg.tastytoast.TastyToast;
import com.services.joshi.serviceprovider.R;
import com.services.joshi.serviceprovider.activities.serviceprovider.SPSignUpActivity;
import com.services.joshi.serviceprovider.api.RetrofitClient;
import com.services.joshi.serviceprovider.models.Category;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SPSignUpFragmentThree extends Fragment {

    String address, category, gender;
    EditText editTextAddress;
    Spinner spinnerGender, spinnerCategory;
    Button btn_next, btn_back;
    private static final String TAG = "MyActivity";
    List<Category> categories;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sp_signup_3, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editTextAddress = view.findViewById(R.id.ed_signup_sp_address);

        btn_next = view.findViewById(R.id.btn_next_sp_sign_fragment_three);
        btn_back = view.findViewById(R.id.btn_back_sp_sign_fragment_three);
        spinnerGender = view.findViewById(R.id.spinner_gender);

        spinnerCategory = view.findViewById(R.id.spinner_signup_sp_category);

        getCategory();

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passDataToNextFragment();
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((SPSignUpActivity) getActivity()).goToBackPage();
            }
        });
    }

    private void getCategory() {
        Call<List<Category>> call = RetrofitClient
                .getInstance()
                .getApi()
                .getCategory();

        call.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                categories = response.body();
                if (!categories.isEmpty()) {
                    List<String> listSpinner = new ArrayList<String>();

                    for (int i = 0; i < categories.size(); i++) {
                        listSpinner.add(categories.get(i).getCategory_name());
                    }

                    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                            (Objects.requireNonNull(getActivity()), android.R.layout.simple_spinner_item,
                                    listSpinner); //selected item will look like a spinner set from XML
                    spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                            .simple_spinner_dropdown_item);
                    spinnerCategory.setAdapter(spinnerArrayAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                TastyToast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG, TastyToast.ERROR).show();
            }
        });
    }

    private void passDataToNextFragment() {

        address = editTextAddress.getText().toString();
        category = spinnerCategory.getSelectedItem().toString();
        gender = spinnerGender.getSelectedItem().toString();

        if (address.isEmpty()) {
            editTextAddress.setError("Address is Required");
            editTextAddress.requestFocus();
            editTextAddress.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.holo_red_light), PorterDuff.Mode.LIGHTEN);
            return;
        }

        if (category.isEmpty()) {
            spinnerCategory.requestFocus();
            spinnerCategory.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.holo_red_light), PorterDuff.Mode.LIGHTEN);
            return;
        }
        if (gender.equals("Select Gender")) {
            spinnerGender.requestFocus();
            TextView errorText = (TextView) spinnerGender.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);//just to highlight that this is an error
            errorText.setText("Select Gender");//changes the selected item text to this
            return;
        }

        Fragment fragment = new SPSignUpFragmentFour();

        SPSignUpActivity.ADDRESS = address;
        SPSignUpActivity.CATEGORY = category;
        SPSignUpActivity.GENDER = gender;

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        final int commit = fragmentManager.beginTransaction().replace(R.id.sp_signup_fragment_four, fragment).addToBackStack(null).commit();

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
