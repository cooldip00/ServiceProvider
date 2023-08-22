package com.services.joshi.serviceprovider;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;


import com.sdsmdg.tastytoast.TastyToast;
import com.services.joshi.serviceprovider.activities.MainActivity;
import com.services.joshi.serviceprovider.activities.user.UserHistoryDetailsActivity;
import com.services.joshi.serviceprovider.api.RetrofitClient;
import com.services.joshi.serviceprovider.models.Bill;
import com.services.joshi.serviceprovider.models.Services;
import com.services.joshi.serviceprovider.repository.LastPendingPaymentResponse;
import com.services.joshi.serviceprovider.storage.SharedPrefManager;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.services.joshi.serviceprovider.App.CHANNEL_ID;

public class PaymentService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //String input = intent.getStringExtra("inputExtra");
        Intent notificationIntent = new Intent(this, UserHistoryDetailsActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Payment Service")
                .setContentText("Service is Complete, Please Pay")
                .setSmallIcon(R.drawable.ic_payment)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(1, notification);

        //do heavy work on a background thread
        //stopSelf();

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
