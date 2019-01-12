package com.geniauti.geniauti;

import android.app.Activity;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.content.Intent;
import android.os.Handler;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

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
