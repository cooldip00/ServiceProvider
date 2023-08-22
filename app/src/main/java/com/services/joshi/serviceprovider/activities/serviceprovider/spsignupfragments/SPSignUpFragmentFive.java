package com.services.joshi.serviceprovider.activities.serviceprovider.spsignupfragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.services.joshi.serviceprovider.R;
import com.services.joshi.serviceprovider.activities.serviceprovider.SPSignUpActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;

public class SPSignUpFragmentFive extends Fragment {

    Button btn_register, btn_back, btn_uploadImage;
    ImageView adhar_img;
    String aadhar_image, user_image, fname, lname, mname, email, password, phone_no, address, category, gender;

    private static final int PICK_IMAGE_REQUEST = 123;
    private static final String MSG = "asd";
    Uri aadharUri;
    Bitmap bitImage;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sp_signup_5, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btn_uploadImage = view.findViewById(R.id.btn_aadharImage_sp_sign_fragment_five);
        adhar_img = view.findViewById(R.id.img_adharImage_sp_sign_fragment_five);
        btn_register = view.findViewById(R.id.btn_register_sp_sign_fragment_five);
        btn_back = view.findViewById(R.id.btn_back_sp_sign_fragment_five);

        fname = SPSignUpActivity.FNAME;
        mname = SPSignUpActivity.MNAME;
        lname = SPSignUpActivity.LNAME;
        email = SPSignUpActivity.EMAIL;
        password = SPSignUpActivity.PASSWORD;
        phone_no = SPSignUpActivity.PHONE_NO;
        address = SPSignUpActivity.ADDRESS;
        category = SPSignUpActivity.CATEGORY;
        gender = SPSignUpActivity.GENDER;
        user_image = SPSignUpActivity.USER_IMAGE;

//        Toast.makeText(getActivity(), ""+user_image, Toast.LENGTH_LONG).show();

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                aadhar_image = imageToString();

                SPSignUpActivity.AADHAR_IMAGE = aadhar_image;

                ((SPSignUpActivity)getActivity()).registerProvider();

            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((SPSignUpActivity) getActivity()).goToBackPage();
            }
        });

        btn_uploadImage.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("IntentReset")
            @Override
            public void onClick(View v) {
                Intent gallary = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                gallary.setType("image/*");
                gallary.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(gallary, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE_REQUEST && data != null && data.getData() != null) {
            aadharUri = data.getData();

            try {
                bitImage = MediaStore.Images.Media.getBitmap(Objects.requireNonNull(getActivity()).getContentResolver(), aadharUri);
                adhar_img.setImageBitmap(bitImage);
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            Toast.makeText(getActivity(), "Image is not selected", Toast.LENGTH_SHORT).show();
        }
    }

    private String imageToString() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitImage.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imgByte = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgByte, Base64.DEFAULT);
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
