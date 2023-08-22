package com.services.joshi.serviceprovider.activities.serviceprovider.drawerfragment;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.sdsmdg.tastytoast.TastyToast;
import com.services.joshi.serviceprovider.R;
import com.services.joshi.serviceprovider.activities.serviceprovider.SPHomeActivity;
import com.services.joshi.serviceprovider.storage.SharedPrefManagerSP;


/**
 * A simple {@link Fragment} subclass.
 */
public class SPHomeFragment extends Fragment implements View.OnClickListener {

    Button buttonGoTOProfile;
    ViewFlipper viewFlipper;
    TextView textViewName, textViewEmail, textViewPhone, textViewAddress, textViewWellcomeMsg;
    int images[] = {R.drawable.slide_one, R.drawable.slide_two, R.drawable.slide_three, R.drawable.slide_four, R.drawable.saloon, R.drawable.on, R.drawable.blog};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.sp_fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        textViewName = view.findViewById(R.id.txt_home_sp_name);
        textViewEmail = view.findViewById(R.id.txt_home_sp_email);
        textViewPhone = view.findViewById(R.id.txt_home_sp_phone);
        textViewAddress = view.findViewById(R.id.txt_home_sp_address);
        viewFlipper = view.findViewById(R.id.sp_viewpager_slide);
        buttonGoTOProfile = view.findViewById(R.id.btn_sp_go_to_profile);
        textViewWellcomeMsg = view.findViewById(R.id.txt_home_sp_welcome_message);
        textViewWellcomeMsg.setSelected(true);

        buttonGoTOProfile.setOnClickListener(this);

        for (int image : images) {
            flipperImages(image);
        }

        String name = SharedPrefManagerSP.getInstance(getActivity()).getProvider().getFname() + " "
                + SharedPrefManagerSP.getInstance(getActivity()).getProvider().getLname();
        textViewName.setText(name);
        textViewEmail.setText(SharedPrefManagerSP.getInstance(getActivity()).getProvider().getEmail());
        textViewPhone.setText(SharedPrefManagerSP.getInstance(getActivity()).getProvider().getPhone());
        textViewAddress.setText(SharedPrefManagerSP.getInstance(getActivity()).getProvider().getAddress());

        if (SPHomeActivity.LoginFistTime)
            TastyToast.makeText(getActivity(), "Welcome " + name, Toast.LENGTH_LONG, TastyToast.SUCCESS).show();
    }

    public void flipperImages(int image) {
        ImageView imageView = new ImageView(getActivity());
        imageView.setBackgroundResource(image);
        imageView.setMaxHeight(viewFlipper.getHeight());
        imageView.setMaxWidth(viewFlipper.getWidth());

        viewFlipper.addView(imageView);
        viewFlipper.setFlipInterval(2500);
        viewFlipper.setAutoStart(true);

        viewFlipper.setInAnimation(getActivity(), android.R.anim.slide_in_left);
        viewFlipper.setOutAnimation(getActivity(), android.R.anim.slide_out_right);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sp_go_to_profile:
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.sp_nav_fragment_container, new SPProfileFragment())
                        .commit();
                break;
        }
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
