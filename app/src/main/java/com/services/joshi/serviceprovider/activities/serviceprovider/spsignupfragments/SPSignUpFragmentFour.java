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
import androidx.fragment.app.FragmentManager;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.services.joshi.serviceprovider.R;
import com.services.joshi.serviceprovider.activities.serviceprovider.SPSignUpActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;


public class SPSignUpFragmentFour extends Fragment {

    Button btn_next, btn_back, btn_uploadImage;
    ImageView user_img;
    String user_image;

    Uri imageUri;
    Bitmap bitImage;

    private static final int PICK_IMAGE_REQUEST = 123;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sp_signup_4, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btn_uploadImage = view.findViewById(R.id.btn_userImage_sp_sign_fragment_four);
        user_img = view.findViewById(R.id.img_userImage_sp_sign_fragment_four);
        btn_next = view.findViewById(R.id.btn_next_sp_sign_fragment_four);
        btn_back = view.findViewById(R.id.btn_back_sp_sign_fragment_four);

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passDataToNextFragment();
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((SPSignUpActivity) Objects.requireNonNull(getActivity())).goToBackPage();
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

        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE_REQUEST) {
            imageUri = data.getData();
            try {
                bitImage = MediaStore.Images.Media.getBitmap(Objects.requireNonNull(getActivity()).getContentResolver(), imageUri);
                user_img.setImageBitmap(bitImage);
                user_image = imageToString();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void passDataToNextFragment() {
        Fragment fragment = new SPSignUpFragmentFive();

        SPSignUpActivity.USER_IMAGE = user_image;

        FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.sp_signup_fragment_five, fragment).addToBackStack(null).commit();

        ((SPSignUpActivity) getActivity()).goToNextPage();
    }

    private String imageToString() throws UnsupportedEncodingException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitImage.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imgByte = byteArrayOutputStream.toByteArray();
        String EncodedImage = Base64.encodeToString(imgByte, Base64.DEFAULT);
        return EncodedImage;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

}
