package com.services.joshi.serviceprovider.activities.serviceprovider;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDialogFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.sdsmdg.tastytoast.TastyToast;
import com.services.joshi.serviceprovider.R;
import com.services.joshi.serviceprovider.api.RetrofitClient;
import com.services.joshi.serviceprovider.repository.DefaultResponse;
import com.services.joshi.serviceprovider.repository.ServiceAcceptResponse;

@SuppressLint("ValidFragment")
public class RequestAcceptDialog extends AppCompatDialogFragment {
    EditText editTextmessage, editTextAmount;
    Context mCx;
    int service_id;

    @SuppressLint("ValidFragment")
    public RequestAcceptDialog(Context mCx, int service_id) {
        this.service_id = service_id;
        this.mCx = mCx;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.sp_request_dialog_layout, null);
        builder.setView(view)
                .setTitle("Bill Generation")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        completeService();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        editTextmessage = (EditText) view.findViewById(R.id.sp_request_dialog_description);
        editTextAmount = (EditText) view.findViewById(R.id.sp_request_dialog_amount);
        return builder.create();
    }

    private void completeService() {

        int id = this.service_id;
        String description = editTextmessage.getText().toString();
        String stringAmount = editTextAmount.getText().toString();
        int amount = Integer.parseInt(stringAmount);

        Call<DefaultResponse> genrateBill = RetrofitClient
                .getInstance()
                .getApi()
                .generateBill(id, description, amount);


        genrateBill.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().isError()) {
                        TastyToast.makeText(mCx, "Some thing Went Wrong Try Again", Toast.LENGTH_LONG, TastyToast.ERROR).show();
                        return;
                    }
                    TastyToast.makeText(mCx, response.body().getMessage(), Toast.LENGTH_LONG, TastyToast.SUCCESS).show();
                }
            }

            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {
                TastyToast.makeText(mCx, t.getMessage(), Toast.LENGTH_LONG, TastyToast.ERROR).show();
            }
        });

        Call<ServiceAcceptResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .completeService(id);

        call.enqueue(new Callback<ServiceAcceptResponse>() {
            @Override
            public void onResponse(Call<ServiceAcceptResponse> call, Response<ServiceAcceptResponse> response) {
                if (response.body().isError()) {
                    TastyToast.makeText(mCx, "Some thing Went Wrong Try Again", Toast.LENGTH_LONG, TastyToast.ERROR).show();
                } else {
                    TastyToast.makeText(mCx, "Service Request Completed", Toast.LENGTH_LONG, TastyToast.SUCCESS).show();
                }
            }

            @Override
            public void onFailure(Call<ServiceAcceptResponse> call, Throwable t) {
                TastyToast.makeText(mCx, t.getMessage(), Toast.LENGTH_LONG, TastyToast.ERROR).show();
            }
        });
    }
}
