package com.geniauti.geniauti;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class BehaviorActivity extends AppCompatActivity implements BehaviorFirstFragment.OnFragmentInteractionListener, BehaviorSecondFragment.OnFragmentInteractionListener, BehaviorThirdFragment.OnFragmentInteractionListener, BehaviorFourthFragment.OnFragmentInteractionListener, BehaviorFifthFragment.OnFragmentInteractionListener, BehaviorSixthFragment.OnFragmentInteractionListener, BehaviorSeventhFragment.OnFragmentInteractionListener, BehaviorEighthFragment.OnFragmentInteractionListener, BehaviorNinthFragment.OnFragmentInteractionListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private PagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private CustomViewPager mViewPager;
    public Behavior tempBehavior;
    public static Parcelable state;

    private FirebaseFirestore db;
    private FirebaseUser user;
    private Map<String, Object> docData;
    private String cid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_behavior);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.behavior_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        tempBehavior = (Behavior) getIntent().getSerializableExtra("tempBehavior");

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new PagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (CustomViewPager) findViewById(R.id.behavior_container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_close_green_24dp));
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.behavior_fab);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
            {

            }

            @Override
            public void onPageSelected(int position)
            {
                if(position==0) {
                    toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_close_green_24dp));
                } else {
                    toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back_green_24dp));
                }

                if(position==8) {
                    fab.setImageResource(R.drawable.ic_check_white_24dp);
                } else {
                    fab.setImageResource(R.drawable.ic_arrow_forward_white_24dp);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state)
            {

            }


        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mViewPager.getCurrentItem()==0) {
                    finish();
                } else {
                    mViewPager.setCurrentItem(getItem(-1), true);
                }
            }
        });

        // mViewPager.setCurrentItem(getItem(+1), true);

        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        db.collection("childs")
                .whereGreaterThanOrEqualTo("users."+user.getUid()+".name", "")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                cid = document.getId();
                            }
                        } else {
//                                Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment firstFragment = getSupportFragmentManager().findFragmentByTag("android:switcher:"+R.id.behavior_container+":"+0);
                Fragment secondFragment = getSupportFragmentManager().findFragmentByTag("android:switcher:"+R.id.behavior_container+":"+1);
                Fragment thirdFragment = getSupportFragmentManager().findFragmentByTag("android:switcher:"+R.id.behavior_container+":"+2);
                Fragment fourthFragment = getSupportFragmentManager().findFragmentByTag("android:switcher:"+R.id.behavior_container+":"+3);
                Fragment fifthFragment = getSupportFragmentManager().findFragmentByTag("android:switcher:"+R.id.behavior_container+":"+4);
                Fragment sixthFragment = getSupportFragmentManager().findFragmentByTag("android:switcher:"+R.id.behavior_container+":"+5);
                Fragment seventhFragment = getSupportFragmentManager().findFragmentByTag("android:switcher:"+R.id.behavior_container+":"+6);
                Fragment eighthFragment = getSupportFragmentManager().findFragmentByTag("android:switcher:"+R.id.behavior_container+":"+7);
                Fragment ninthFragment = getSupportFragmentManager().findFragmentByTag("android:switcher:"+R.id.behavior_container+":"+8);

                BehaviorFirstFragment f1 = (BehaviorFirstFragment) firstFragment;
                BehaviorSecondFragment f2 = (BehaviorSecondFragment) secondFragment;
                BehaviorThirdFragment f3 = (BehaviorThirdFragment) thirdFragment;
                BehaviorFourthFragment f4 = (BehaviorFourthFragment) fourthFragment;
                BehaviorFifthFragment f5 = (BehaviorFifthFragment) fifthFragment;
                BehaviorSixthFragment f6 = (BehaviorSixthFragment) sixthFragment;
                BehaviorSeventhFragment f7 = (BehaviorSeventhFragment) seventhFragment;
                BehaviorEighthFragment f8 = (BehaviorEighthFragment) eighthFragment;
                BehaviorNinthFragment f9 = (BehaviorNinthFragment) ninthFragment;

                if(mViewPager.getCurrentItem()==0) {
                    SimpleDateFormat formatter = new SimpleDateFormat("aa hh:mm", Locale.KOREAN);
                    try {
                        Date hour_start = formatter.parse(f1.hour_start);
                        Date hour_end = formatter.parse(f1.hour_end);

                        if(f1.hour_start.substring(0,2).equals("오후") && f1.hour_end.substring(0,2).equals("오전") && Integer.parseInt(f1.hour_end.substring(3,5)) < 4) {
                            mViewPager.setCurrentItem(getItem(+1), true);
                        } else {
                            if(hour_start.compareTo(hour_end) < 0) {
                                mViewPager.setCurrentItem(getItem(+1), true);
                            } else {
                                Toast.makeText(BehaviorActivity.this, "시작 시간이 종료시간보다 빠르도록 선택해주세요.", Toast.LENGTH_SHORT).show();
                            }
                        }

                    } catch (java.text.ParseException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                } else if(mViewPager.getCurrentItem()==1) {
                    if(f2.getResult()==""){
                        Toast.makeText(BehaviorActivity.this, "장소를 선택해주세요.", Toast.LENGTH_SHORT).show();
                    } else {
                        mViewPager.setCurrentItem(getItem(+1), true);
                    }
                } else if(mViewPager.getCurrentItem()==2){
                    if(f3.getResult()==""){
                        Toast.makeText(BehaviorActivity.this, "제목을 선택해주세요.", Toast.LENGTH_SHORT).show();
                    } else {
                        mViewPager.setCurrentItem(getItem(+1), true);
                    }
                } else if(mViewPager.getCurrentItem()==3){
                    if(f4.textInput.getText().toString().equals("")){
                        Toast.makeText(BehaviorActivity.this, "빈칸을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    } else {
                        mViewPager.setCurrentItem(getItem(+1), true);
                    }
                } else if(mViewPager.getCurrentItem()==4){
                    if(f5.textInput.getText().toString().equals("")){
                        Toast.makeText(BehaviorActivity.this, "빈칸을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    } else {
                        mViewPager.setCurrentItem(getItem(+1), true);
                    }
                } else if(mViewPager.getCurrentItem()==5){
                    if(f6.textInput.getText().toString().equals("")){
                        Toast.makeText(BehaviorActivity.this, "빈칸을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    } else {
                        mViewPager.setCurrentItem(getItem(+1), true);
                    }
                } else if(mViewPager.getCurrentItem()==6) {
                    if(f7.getResult().toString()=="{}"){
                        Toast.makeText(BehaviorActivity.this, "행동을 선택해주세요.", Toast.LENGTH_SHORT).show();
                    } else {
                        mViewPager.setCurrentItem(getItem(+1), true);
                    }
                } else if(mViewPager.getCurrentItem()==7) {

                    mViewPager.setCurrentItem(getItem(+1), true);
                } else if(mViewPager.getCurrentItem()==8) {
                    if(f9.getResult().toString()=="{}"){
                        Toast.makeText(BehaviorActivity.this, "행동 원인을 골라주세요.", Toast.LENGTH_SHORT).show();
                    } else {
                        fab.setEnabled(false);
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy년 MM월 dd일 EEE aa hh:mm", Locale.KOREAN);
                        try{
                            Date date_start = formatter.parse(f1.date_start + " " + f1.hour_start);
                            Date date_end = formatter.parse(f1.date_start + " " + f1.hour_end);
                            Calendar cal = Calendar.getInstance();

                            docData = new HashMap<>();
                            docData.put("start_time", date_start);
                            docData.put("end_time", date_end);
                            docData.put("place", f2.getResult());
                            docData.put("categorization", f3.getResult());
                            docData.put("current_behavior", f4.textInput.getText().toString());
                            docData.put("before_behavior", f5.textInput.getText().toString());
                            docData.put("after_behavior", f6.textInput.getText().toString());
                            docData.put("type", f7.getResult());
                            docData.put("intensity", f8.seekbarValue+1);
                            docData.put("reason", f9.getResult());
                            docData.put("reason_detail", "");
                            docData.put("created_at", cal.getTime());
                            docData.put("updated_at", "");
                            docData.put("uid", user.getUid());
                            docData.put("name", user.getDisplayName());
                            docData.put("cid", cid);

                            db.collection("behaviors").document()
                                    .set(docData)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
//                                                                  mProgressView.setVisibility(View.GONE);
                                            MainActivity.adapter.notifyDataSetChanged();
                                            finish();
                                            Toast toast = Toast.makeText(BehaviorActivity.this, "행동 생성이 완료되었습니다.", Toast.LENGTH_SHORT);
                                            toast.show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            fab.setEnabled(true);
                                        }
                                    });
                        } catch (java.text.ParseException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                            fab.setEnabled(true);
                        }
                        }
                    }
            }
        });

    }

    @Override
    public void onBackPressed() {

        if(mViewPager.getCurrentItem()==0) {
            finish();
        } else {
            mViewPager.setCurrentItem(getItem(-1), true);
        }

    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */

    private int getItem(int i) {
        return mViewPager.getCurrentItem() + i;
    }

    public class PagerAdapter extends FragmentPagerAdapter {
        int mNumOfTabs = 9;

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    BehaviorFirstFragment tab1 = new BehaviorFirstFragment();
                    return tab1;
                case 1:
                    BehaviorSecondFragment tab2 = new BehaviorSecondFragment();
                    return tab2;
                case 2:
                    BehaviorThirdFragment tab3 = new BehaviorThirdFragment();
                    return tab3;
                case 3:
                    BehaviorFourthFragment tab4 = new BehaviorFourthFragment();
                    return tab4;
                case 4:
                    BehaviorFifthFragment tab5 = new BehaviorFifthFragment();
                    return tab5;
                case 5:
                    BehaviorSixthFragment tab6 = new BehaviorSixthFragment();
                    return tab6;
                case 6:
                    BehaviorSeventhFragment tab7 = new BehaviorSeventhFragment();
                    return tab7;
                case 7:
                    BehaviorEighthFragment tab8 = new BehaviorEighthFragment();
                    return tab8;
                case 8:
                    BehaviorNinthFragment tab9 = new BehaviorNinthFragment();
                    return tab9;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return mNumOfTabs;
        }

    }

    @Override
    public void onFragmentInteraction(Uri uri){
        System.out.println(uri);
    }

}

