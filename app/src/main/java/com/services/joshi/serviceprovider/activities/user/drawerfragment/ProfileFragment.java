package com.services.joshi.serviceprovider.activities.user.drawerfragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sdsmdg.tastytoast.TastyToast;
import com.services.joshi.serviceprovider.R;
import com.services.joshi.serviceprovider.api.RetrofitClient;
import com.services.joshi.serviceprovider.models.User;
import com.services.joshi.serviceprovider.repository.UserProfileUpdateResponse;
import com.services.joshi.serviceprovider.storage.SharedPrefManager;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {

    EditText editTextfname, editTextlname, editTextaddress;
    TextView textViewemail, textViewphone;
    ImageView userimage, changeimage;
    Button buttonchangeprofile;

    Bitmap bitmapImage;
    Uri imageUri;

    String user_image;
    String fname, lname, address;

    private static final int PICK_IMAGE_REQUEST = 123;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.user_fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editTextfname = view.findViewById(R.id.user_fname);
        editTextlname = view.findViewById(R.id.user_lname);
        editTextaddress = view.findViewById(R.id.user_address);
        textViewemail = view.findViewById(R.id.user_email);
        textViewphone = view.findViewById(R.id.user_contact);
        userimage = view.findViewById(R.id.user_change_image);
        changeimage = view.findViewById(R.id.user_profile_change_image);
        buttonchangeprofile = view.findViewById(R.id.btn_user_change_profile);

        SharedPrefManager sharedPrefManager = SharedPrefManager.getInstance(getActivity());


        if (SharedPrefManager.getInstance(getActivity()).getUserImage() != null) {

            Picasso.with(getActivity()).load(
                    SharedPrefManager.getInstance(getActivity()).getUserImage())
                    .into(userimage);
        } else {
            userimage.setImageResource(R.drawable.no_image);
        }

        editTextfname.setText(sharedPrefManager.getUser().getFname());
        editTextlname.setText(sharedPrefManager.getUser().getLname());
        editTextaddress.setText(sharedPrefManager.getUser().getAddress());
        textViewemail.setText(sharedPrefManager.getUser().getEmail());
        textViewphone.setText(sharedPrefManager.getUser().getPhone());


        buttonchangeprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProfile();
            }
        });

        userimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

        changeimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE_REQUEST) {
            imageUri = data.getData();
            try {
                bitmapImage = MediaStore.Images.Media.getBitmap(Objects.requireNonNull(getActivity()).getContentResolver(), imageUri);
                userimage.setImageBitmap(bitmapImage);
                user_image = imageToString();

                updateImage(user_image);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String imageToString() throws UnsupportedEncodingException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imgByte = byteArrayOutputStream.toByteArray();
        String EncodedImage = Base64.encodeToString(imgByte, Base64.DEFAULT);
        return EncodedImage;
    }

    int id = SharedPrefManager.getInstance(getActivity()).getUser().getId();

    private void updateImage(String image) {
        Call<UserProfileUpdateResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .updateProfileImage(id, image);

        call.enqueue(new Callback<UserProfileUpdateResponse>() {
            @Override
            public void onResponse(Call<UserProfileUpdateResponse> call, Response<UserProfileUpdateResponse> response) {

                if (response.isSuccessful()) {
                    List<User> user = new ArrayList<>();
                    user.add(response.body().getUser());
                    SharedPrefManager.getInstance(getActivity()).saveUser(user);

                    Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserProfileUpdateResponse> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openGallery() {

        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        gallery.setType("image/*");
        gallery.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(gallery, PICK_IMAGE_REQUEST);
    }

    private void updateProfile() {

        fname = editTextfname.getText().toString().trim();
        lname = editTextlname.getText().toString().trim();
        address = editTextaddress.getText().toString().trim();

        if (fname.isEmpty()) {
            editTextfname.setError("First Name Is Required");
            editTextfname.requestFocus();
            editTextfname.getBackground().getCurrent().setColorFilter(getResources().getColor(android.R.color.holo_red_light), PorterDuff.Mode.SCREEN);
            return;
        }

        if (lname.isEmpty()) {
            editTextlname.setError("Last Name Is Required");
            editTextlname.requestFocus();
            editTextlname.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.holo_red_light), PorterDuff.Mode.LIGHTEN);
            return;
        }

        if (address.isEmpty()) {
            editTextaddress.setError("Address is Required");
            editTextaddress.requestFocus();
            editTextaddress.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.holo_red_light), PorterDuff.Mode.LIGHTEN);
            return;
        }


        //ProgressDialog creation
        final ProgressDialog progressDialog = new ProgressDialog(getActivity(), R.style.AppCompatAlertDialogStyle);
        progressDialog.setMessage("Updating Please wait...");
        progressDialog.show();
        int id = SharedPrefManager.getInstance(getActivity()).getUser().getId();
        Call<UserProfileUpdateResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .updateUserData(id, fname, lname, address);

        call.enqueue(new Callback<UserProfileUpdateResponse>() {
            @Override
            public void onResponse(Call<UserProfileUpdateResponse> call, Response<UserProfileUpdateResponse> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    if (!response.body().isError()) {
                        TastyToast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_LONG, TastyToast.SUCCESS).show();
                        SharedPrefManager.getInstance(getActivity()).updateUesrDetails(fname, lname, address);
                    }
                }
            }

            @Override
            public void onFailure(Call<UserProfileUpdateResponse> call, Throwable t) {
                progressDialog.dismiss();
                TastyToast.makeText(getActivity(), t.getMessage(), TastyToast.LENGTH_LONG, TastyToast.ERROR);
            }
        });

    }
}
