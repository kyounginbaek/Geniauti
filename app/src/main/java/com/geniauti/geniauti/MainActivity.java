package com.geniauti.geniauti;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class MainActivity extends AppCompatActivity implements MainFragment.OnFragmentInteractionListener, DicFragment.OnFragmentInteractionListener, ChartFragment.OnFragmentInteractionListener, ProfileFragment.OnFragmentInteractionListener {

    private BackPressCloseHandler backPressCloseHandler;

    public static CustomViewPager viewPager;
    public static ViewPagerAdapter adapter;

    public static FirebaseUser user;
    public static FirebaseFirestore db;

    public static FirebaseStorage storage;
    public static StorageReference storageRef;

    public static String cid;
    public static String relationship;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        backPressCloseHandler = new BackPressCloseHandler(this);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("행동 조회");

        user = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        MainActivity.db.collection("childs")
                .whereGreaterThanOrEqualTo("users."+MainActivity.user.getUid()+".name", "")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                cid = document.getId();
                            }

                            setupViewPager(viewPager);
                        }
                    }
                });

        db.collection("childs")
                .whereGreaterThanOrEqualTo("users."+MainActivity.user.getUid()+".name", "")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
//                            Log.w(TAG, "Listen failed.", e);
                            return;
                        }

                        for (QueryDocumentSnapshot doc : value) {
                            HashMap<String, Object> result = (HashMap<String, Object>) doc.get("users."+user.getUid());
                            relationship = result.get("relationship").toString();
                        }
                    }
                });

        viewPager = (CustomViewPager) findViewById(R.id.fragment_container);

        final BottomNavigationViewEx navigation = (BottomNavigationViewEx) findViewById(R.id.navigation);
        navigation.enableAnimation(false);
        navigation.enableShiftingMode(false);
        navigation.enableItemShiftingMode(false);

        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_main:
                        DicFragment.searchFragmentHide();
                        viewPager.setCurrentItem(0);
                        getSupportActionBar().setTitle("행동 조회");
                        break;

                    case R.id.navigation_dic:
                        viewPager.setCurrentItem(1);
                        getSupportActionBar().setTitle("사전");
                        break;

                    case R.id.navigation_chart:
                        DicFragment.searchFragmentHide();
                        viewPager.setCurrentItem(2);
                        getSupportActionBar().setTitle("차트");
                        break;

                    case R.id.navigation_profile:
                        DicFragment.searchFragmentHide();
                        viewPager.setCurrentItem(3);
                        getSupportActionBar().setTitle("프로필");
                        break;
                }
                return true;
            }
        });
    }

    public class BackPressCloseHandler {

        private long backKeyPressedTime = 0;
        private Toast toast;
        private Activity activity;

        public BackPressCloseHandler(Activity context) {
            this.activity = context;
        }

        public void onBackPressed() {
            if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
                backKeyPressedTime = System.currentTimeMillis(); showGuide();
                return;
            }

            if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
                activity.finish(); toast.cancel();
            }
        }

        public void showGuide() {
            toast = Toast.makeText(activity, "\'뒤로\'버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    @Override public void onBackPressed() {
//        super.onBackPressed();
        backPressCloseHandler.onBackPressed();
    }


    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(3);
    }

    public class ViewPagerAdapter extends FragmentStatePagerAdapter {

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    MainFragment tab1 = new MainFragment();
                    return tab1;
                case 1:
                    DicFragment tab2 = new DicFragment();
                    return tab2;
                case 2:
                    ChartFragment tab3 = new ChartFragment();
                    return tab3;
                case 3:
                    ProfileFragment tab4 = new ProfileFragment();
                    return tab4;
                default:
                    return null;
            }
        }

        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public int getCount() {
            return 4;
        }

    }

    @Override
    public void onFragmentInteraction(Uri uri){
        System.out.println(uri);
    }

}
