package com.services.joshi.serviceprovider.activities.user.drawerfragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.sdsmdg.tastytoast.TastyToast;
import com.services.joshi.serviceprovider.R;
import com.services.joshi.serviceprovider.activities.MainActivity;
import com.services.joshi.serviceprovider.api.RetrofitClient;
import com.services.joshi.serviceprovider.repository.DefaultResponse;
import com.services.joshi.serviceprovider.storage.SharedPrefManager;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class UserSettingFragment extends Fragment {


    Context context;
    ListView listView_items;
    ImageView imageView;
    TextView textView;
    ArrayList<HashMap<String, String>> arrayList = new ArrayList<HashMap<String, String>>();
    String name[] = {"Edit Profile", "Help", "About Us", "Contact Us", "Logout", "Delete Account"};
    int image[] = {R.drawable.ic_edit, R.drawable.ic_help_black_24dp, R.drawable.user_logo, R.drawable.ic_contacts_black_24dp, R.drawable.ic_mail, R.drawable.ic_delete_black_24dp};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_setting, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listView_items = (ListView) view.findViewById(R.id.user_list_views);
        for (int i = 0; i < 6; i++) {
            HashMap<String, String> hashMap = new HashMap<String, String>();
            hashMap.put("image", Integer.toString(image[i]));
            hashMap.put("name", name[i]);
            arrayList.add(hashMap);
            String from[] = {"image", "name"};
            int to[] = {R.id.user_setting_image, R.id.user_setting_name};
            SimpleAdapter simpleAdapter = new SimpleAdapter(getActivity(), arrayList, R.layout.custom_setting_user_list_view, from, to);
            listView_items.setAdapter(simpleAdapter);
        }
        listView_items.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0) {
                    Fragment fragment = new ProfileFragment();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.user_nav_fragment_container, fragment);
                    fragmentTransaction.commit();
                    getActivity().setTitle("Edit Profile");
                }

                if (position == 1) {
                    Fragment fragment = new AboutUsFragment();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.user_nav_fragment_container, fragment);
                    fragmentTransaction.commit();
                    getActivity().setTitle("Help");

                }

                if (position == 2) {
                    Fragment fragment = new AboutUsFragment();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.user_nav_fragment_container, fragment);
                    fragmentTransaction.commit();
                    getActivity().setTitle("About Us");


                }

                if (position == 3) {
                    Fragment fragment = new ContactUsFragment();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.user_nav_fragment_container, fragment);
                    fragmentTransaction.commit();
                    getActivity().setTitle("Contact Us");
                }

                if (position == 4) {
                    SharedPrefManager.getInstance(getActivity()).clear();
                    Intent i4 = new Intent(getActivity(), MainActivity.class);
                    startActivity(i4);
                    getActivity().finish();
                }
                if (position == 5) {
                    deactiveAccount();
                }

            }
        });
    }

    private void deactiveAccount() {

        final ProgressDialog progressDialog = new ProgressDialog(getActivity(), R.style.AppCompatAlertDialogStyle);
        progressDialog.setMessage(" Please wait...");
        progressDialog.show();

        int id = SharedPrefManager.getInstance(getActivity()).getUser().getId();

        Call<DefaultResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .deleteUser(id);

        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                if (response.isSuccessful()) {
                    if (!response.body().isError()) {
                        progressDialog.dismiss();
                        TastyToast.makeText(getActivity(), response.body().getMessage(), TastyToast.LENGTH_LONG, TastyToast.SUCCESS).show();
                        SharedPrefManager.getInstance(getActivity()).clear();
                        startActivity(new Intent(getActivity(), MainActivity.class));
                        getActivity().finish();
                    }
                } else {
                    progressDialog.dismiss();
                    TastyToast.makeText(getActivity(), "Some Thing is Wrong try Again", Toast.LENGTH_LONG, TastyToast.ERROR).show();
                }
            }

            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {
                progressDialog.dismiss();
                TastyToast.makeText(getActivity(), t.getMessage(), TastyToast.LENGTH_LONG, TastyToast.ERROR).show();
            }
        });
    }
}
