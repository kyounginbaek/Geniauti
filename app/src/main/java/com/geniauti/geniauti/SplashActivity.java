package com.geniauti.geniauti;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import static android.support.constraint.Constraints.TAG;

public class SplashActivity extends Activity {

    public int SPLASH_DISPLAY_LENGTH = 1500;

    /** 처음 액티비티가 생성될때 불려진다. */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

//        NetworkInfo mNetworkState = getNetworkInfo();
//        if(mNetworkState != null && mNetworkState.isConnected()) {
//
//        } else {
//            Toast.makeText(SplashActivity.this, "네트워크에 연결되어 있지 않습니다.", Toast.LENGTH_SHORT).show();
//            SplashActivity.this.finish();
//        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            String channelId  = getString(R.string.default_notification_channel_id);
            String channelName = getString(R.string.default_notification_channel_name);
            NotificationManager notificationManager =
                    getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(new NotificationChannel(channelId,
                    channelName, NotificationManager.IMPORTANCE_LOW));
        }

        // If a notification message is tapped, any data accompanying the notification
        // message is available in the intent extras. In this sample the launcher
        // intent is fired when the notification is tapped, so any accompanying data would
        // be handled here. If you want a different intent fired, set the click_action
        // field of the notification message to the desired intent. The launcher intent
        // is used when no click_action is specified.
        //
        // Handle possible data accompanying notification message.
        // [START handle_data_extras]
        if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                Object value = getIntent().getExtras().get(key);
                Log.d(TAG, "Key: " + key + " Value: " + value);
            }
        }
        // [END handle_data_extras]

        if (user != null) {
            db.collection("childs")
                    .whereGreaterThanOrEqualTo("users."+user.getUid()+".name", "")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                if(task.getResult().isEmpty()) {
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            // No user is signed in
                                            Intent mainIntent = new Intent(SplashActivity.this, ChildRegisterActivity.class);
                                            SplashActivity.this.startActivity(mainIntent);
                                            SplashActivity.this.finish();
                                        }
                                    }, SPLASH_DISPLAY_LENGTH);
                                } else {
                                    // User is signed in
                                    Intent mainIntent = new Intent(SplashActivity.this,MainActivity.class);
                                    SplashActivity.this.startActivity(mainIntent);
                                    SplashActivity.this.finish();
                                }
//                                for (QueryDocumentSnapshot document : task.getResult()) {
//                                    Log.d(TAG, document.getId() + " => " + document.getData());

//                                }
                            } else {
//                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    });
        } else {
                /* SPLASH_DISPLAY_LENGTH 뒤에 메뉴 액티비티를 실행시키고 종료한다.*/
                new Handler().postDelayed(new Runnable(){
                    @Override
                    public void run() {
                        // No user is signed in
                        Intent mainIntent = new Intent(SplashActivity.this,LoginActivity.class);
                        SplashActivity.this.startActivity(mainIntent);
                        SplashActivity.this.finish();
                    }
                }, SPLASH_DISPLAY_LENGTH);
        }
    }

    private NetworkInfo getNetworkInfo() {

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo;
    }
}
