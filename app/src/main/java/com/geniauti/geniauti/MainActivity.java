package com.geniauti.geniauti;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.HashMap;


public class MainActivity extends AppCompatActivity implements MainFragment.OnFragmentInteractionListener, DicFragment.OnFragmentInteractionListener, ChartFragment.OnFragmentInteractionListener, ProfileFragment.OnFragmentInteractionListener {

    public static CustomViewPager viewPager;
    public static ViewPagerAdapter adapter;

    public static FirebaseUser user;
    public static FirebaseFirestore db;
    public static String cid;
    public static String relationship;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("행동 조회");

        user = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();

        db.collection("childs")
                .whereGreaterThanOrEqualTo("users."+user.getUid()+".name", "")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                cid = document.getId();
                                HashMap<String, Object> result = (HashMap<String, Object>) document.get("users."+user.getUid());
                                relationship = result.get("relationship").toString();
                            }
                            setupViewPager(viewPager);
                        } else {
//                                Log.d(TAG, "Error getting documents: ", task.getException());
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
