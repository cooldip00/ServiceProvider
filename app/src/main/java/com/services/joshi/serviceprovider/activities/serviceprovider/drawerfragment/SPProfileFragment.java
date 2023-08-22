package com.services.joshi.serviceprovider.activities.serviceprovider.drawerfragment;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import android.util.Base64;
import android.util.Log;
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
import com.services.joshi.serviceprovider.repository.SpProfileUpdateResponse;
import com.services.joshi.serviceprovider.storage.SharedPrefManagerSP;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.app.Activity.RESULT_OK;
import static android.content.Context.LOCATION_SERVICE;
import static com.services.joshi.serviceprovider.storage.SharedPrefManagerSP.getInstance;


/**
 * A simple {@link Fragment} subclass.
 */
public class SPProfileFragment extends Fragment {

    EditText editTextfname, editTextmname, editTextlname, editTextaddress;
    TextView textViewemail, textViewphone;
    ImageView providerimage, changeimage;
    Button buttonchangeprofile;

    Uri imageUri;
    Bitmap bitmapImage;

    private LocationManager locationManager;
    private LocationListener listener;

    String lattitude, longtitude;
    double lattitude1, longtitude1;

    String provider_image, fname, mname, lname, address;

    private static final int PICK_IMAGE_REQUEST = 123;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.sp_fragment_profile, container, false);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                lattitude1 = location.getLatitude();
                longtitude1 = location.getLongitude();

                lattitude = String.valueOf(lattitude1);
                longtitude = String.valueOf(longtitude1);

                Toast.makeText(getActivity(), location.getLongitude() + " " + location.getLatitude(), Toast.LENGTH_LONG).show();
                Log.v("Locatin", location.getLongitude() + " " + location.getLatitude());
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {
                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(i);
            }
        };

        gpsPermission();
        requestPermission();

        editTextfname = view.findViewById(R.id.sp_fname);
        editTextmname = view.findViewById(R.id.sp_mname);
        editTextlname = view.findViewById(R.id.sp_lname);
        editTextaddress = view.findViewById(R.id.sp_address);
        textViewemail = view.findViewById(R.id.sp_email);
        textViewphone = view.findViewById(R.id.sp_contact);
        providerimage = view.findViewById(R.id.sp_change_image);
        changeimage = view.findViewById(R.id.sp_profile_change_image);
        buttonchangeprofile = view.findViewById(R.id.btn_sp_change_profile);


        SharedPrefManagerSP sharedPrefManagerSP = getInstance(getActivity());
        editTextfname.setText(sharedPrefManagerSP.getProvider().getFname());
        editTextmname.setText(sharedPrefManagerSP.getProvider().getMname());
        editTextlname.setText(sharedPrefManagerSP.getProvider().getLname());
        editTextaddress.setText(sharedPrefManagerSP.getProvider().getAddress());
        textViewemail.setText(sharedPrefManagerSP.getProvider().getEmail());
        textViewphone.setText(sharedPrefManagerSP.getProvider().getPhone());

        if (sharedPrefManagerSP.getProvider().getUserImage() != null) {
            Picasso.with(getActivity()).load(sharedPrefManagerSP.getProvider().getUserImage()).into(providerimage);
        } else {
            providerimage.setImageResource(R.drawable.no_image);
        }

        changeimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });
        providerimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });
        buttonchangeprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProfile();
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE_REQUEST && data != null) {
            imageUri = data.getData();

            try {
                bitmapImage = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                providerimage.setImageBitmap(bitmapImage);

                provider_image = imageToString();

                updateImage(provider_image);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private String imageToString() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imageByte = byteArrayOutputStream.toByteArray();
        String EncodedImage = Base64.encodeToString(imageByte, Base64.DEFAULT);
        return EncodedImage;
    }

    private void openGallery() {
        Intent gallary = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        gallary.setType("image/*");
        gallary.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(gallary, PICK_IMAGE_REQUEST);
    }

    private void updateImage(String image) {

        int id = getInstance(getActivity()).getProvider().getId();

        Call<SpProfileUpdateResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .updateProviderProfileImage(id, image);

        call.enqueue(new Callback<SpProfileUpdateResponse>() {
            @Override
            public void onResponse(Call<SpProfileUpdateResponse> call, Response<SpProfileUpdateResponse> response) {
                if (response.isSuccessful()) {
                    if (!response.body().isError()) {
                        TastyToast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_LONG, TastyToast.SUCCESS).show();
                        getInstance(getActivity()).updateImage(response.body().getProvider().getUserImage());
                    } else {
                        TastyToast.makeText(getActivity(), "Somthing Went Wrong Please Try Again ", Toast.LENGTH_LONG, TastyToast.ERROR).show();
                    }
                } else {
                    TastyToast.makeText(getActivity(), "Somthing Went Wrong Please Try Again ", Toast.LENGTH_LONG, TastyToast.ERROR).show();
                }
            }

            @Override
            public void onFailure(Call<SpProfileUpdateResponse> call, Throwable t) {

            }
        });
    }

    private void updateProfile() {
        fname = editTextfname.getText().toString().trim();
        mname = editTextmname.getText().toString().trim();
        lname = editTextlname.getText().toString().trim();
        address = editTextaddress.getText().toString().trim();

        if (fname.isEmpty()) {
            editTextfname.setError("First Name Is Required");
            editTextfname.requestFocus();
            editTextfname.getBackground().getCurrent().setColorFilter(getResources().getColor(android.R.color.holo_red_light), PorterDuff.Mode.SCREEN);
            return;
        }
        if (mname.isEmpty()) {
            editTextmname.setError("Middle Name Is Required");
            editTextmname.requestFocus();
            editTextmname.getBackground().getCurrent().setColorFilter(getResources().getColor(android.R.color.holo_red_light), PorterDuff.Mode.SCREEN);
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
        int id = getInstance(getActivity()).getProvider().getId();
        Call<SpProfileUpdateResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .updateProviderData(id, fname, mname, lname, address, lattitude, longtitude);

        call.enqueue(new Callback<SpProfileUpdateResponse>() {
            @Override
            public void onResponse(Call<SpProfileUpdateResponse> call, Response<SpProfileUpdateResponse> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    if (!response.body().isError()) {
                        TastyToast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_LONG, TastyToast.SUCCESS).show();
                        SharedPrefManagerSP sharedPrefManagerSP = getInstance(getActivity());
                        getInstance(getContext()).updateProvider(
                                sharedPrefManagerSP.getProvider().getFname(),
                                sharedPrefManagerSP.getProvider().getMname(),
                                sharedPrefManagerSP.getProvider().getLname(),
                                sharedPrefManagerSP.getProvider().getAddress()
                        );
                    } else {
                        TastyToast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_LONG, TastyToast.ERROR).show();
                    }
                } else {
                    TastyToast.makeText(getActivity(), "Some thing went Wrong Try Again", Toast.LENGTH_LONG, TastyToast.ERROR).show();
                }
            }

            @Override
            public void onFailure(Call<SpProfileUpdateResponse> call, Throwable t) {
                progressDialog.dismiss();
                TastyToast.makeText(getActivity(), t.getMessage(), TastyToast.LENGTH_LONG, TastyToast.ERROR);
            }
        });
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(Objects.requireNonNull(getActivity()), new String[]{ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET}, 10);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 10:
                gpsPermission();
                break;
            default:
                break;
        }
    }

    private void gpsPermission() {
        //noinspection MissingPermission
        if (ActivityCompat.checkSelfPermission(Objects.requireNonNull(getActivity()), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}
                        , 10);
            }
            return;
        }
        /*locationManager.requestLocationUpdates("gps", 5000, 0, listener);*/
        locationManager.requestSingleUpdate("gps", listener, null);
    }


}
