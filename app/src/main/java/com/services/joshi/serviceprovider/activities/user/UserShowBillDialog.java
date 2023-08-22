package com.services.joshi.serviceprovider.activities.user;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.services.joshi.serviceprovider.R;

import androidx.appcompat.app.AppCompatDialogFragment;

@SuppressLint("ValidFragment")
public class UserShowBillDialog extends AppCompatDialogFragment {

    TextView textViewDescription,textViewAmount;

    Context mCx;
    int service_id;

    String amount,description;

    public UserShowBillDialog(Context mCx, int service_id, String amount, String description) {
        this.mCx = mCx;
        this.service_id = service_id;
        this.amount = amount;
        this.description = description;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.user_bill_dialog, null);
        builder.setView(view)
                .setTitle("Bill Details")
                .setPositiveButton("Make Payment", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        return builder.create();
    }
}
