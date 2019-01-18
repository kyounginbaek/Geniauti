package com.geniauti.geniauti;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class BookmarkActivity extends AppCompatActivity implements BehaviorFirstFragment.OnFragmentInteractionListener, BehaviorSecondFragment.OnFragmentInteractionListener, BehaviorThirdFragment.OnFragmentInteractionListener, BehaviorFourthFragment.OnFragmentInteractionListener, BehaviorFifthFragment.OnFragmentInteractionListener, BehaviorSixthFragment.OnFragmentInteractionListener, BehaviorSeventhFragment.OnFragmentInteractionListener, BehaviorEighthFragment.OnFragmentInteractionListener, BehaviorNinthFragment.OnFragmentInteractionListener {

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
    public static Bookmark editBookmark;
    public static boolean editBookmarkState = false;

    private Map<String, Object> docData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.bookmark_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new PagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (CustomViewPager) findViewById(R.id.bookmark_container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(4);

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_close_green_24dp));
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.bookmark_fab);

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

                if(position==4) {
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

        // editBehavior
        editBookmark = (Bookmark) getIntent().getSerializableExtra("bookmarkEdit");
        if(editBookmark != null){
            editBookmarkState = true;
            int editPage = getIntent().getIntExtra("bookmarkEditPage", 0);
            mViewPager.setCurrentItem(editPage-1);
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Fragment firstFragment = getSupportFragmentManager().findFragmentByTag("android:switcher:"+R.id.bookmark_container+":"+0);
                Fragment secondFragment = getSupportFragmentManager().findFragmentByTag("android:switcher:"+R.id.bookmark_container+":"+0);
                Fragment thirdFragment = getSupportFragmentManager().findFragmentByTag("android:switcher:"+R.id.bookmark_container+":"+1);
//                Fragment fourthFragment = getSupportFragmentManager().findFragmentByTag("android:switcher:"+R.id.bookmark_container+":"+3);
//                Fragment fifthFragment = getSupportFragmentManager().findFragmentByTag("android:switcher:"+R.id.bookmark_container+":"+4);
//                Fragment sixthFragment = getSupportFragmentManager().findFragmentByTag("android:switcher:"+R.id.bookmark_container+":"+5);
                Fragment seventhFragment = getSupportFragmentManager().findFragmentByTag("android:switcher:"+R.id.bookmark_container+":"+2);
                Fragment eighthFragment = getSupportFragmentManager().findFragmentByTag("android:switcher:"+R.id.bookmark_container+":"+3);
                Fragment ninthFragment = getSupportFragmentManager().findFragmentByTag("android:switcher:"+R.id.bookmark_container+":"+4);

//                BehaviorFirstFragment f1 = (BehaviorFirstFragment) firstFragment;
                BehaviorSecondFragment f2 = (BehaviorSecondFragment) secondFragment;
                BehaviorThirdFragment f3 = (BehaviorThirdFragment) thirdFragment;
//                BehaviorFourthFragment f4 = (BehaviorFourthFragment) fourthFragment;
//                BehaviorFifthFragment f5 = (BehaviorFifthFragment) fifthFragment;
//                BehaviorSixthFragment f6 = (BehaviorSixthFragment) sixthFragment;
                BehaviorSeventhFragment f7 = (BehaviorSeventhFragment) seventhFragment;
                BehaviorEighthFragment f8 = (BehaviorEighthFragment) eighthFragment;
                BehaviorNinthFragment f9 = (BehaviorNinthFragment) ninthFragment;

                if(mViewPager.getCurrentItem()==0) {
                    if(f2.getResult()==""){
                        Toast.makeText(BookmarkActivity.this, "장소를 선택해주세요.", Toast.LENGTH_SHORT).show();
                    } else {
                        mViewPager.setCurrentItem(getItem(+1), true);
                    }
                } else if(mViewPager.getCurrentItem()==1){
                    if(f3.getResult()==""){
                        Toast.makeText(BookmarkActivity.this, "제목을 선택해주세요.", Toast.LENGTH_SHORT).show();
                    } else {
                        mViewPager.setCurrentItem(getItem(+1), true);
                    }
                } else if(mViewPager.getCurrentItem()==2) {
                    if(f7.getResult().toString()=="{}"){
                        Toast.makeText(BookmarkActivity.this, "행동을 선택해주세요.", Toast.LENGTH_SHORT).show();
                    } else {
                        mViewPager.setCurrentItem(getItem(+1), true);
                    }
                } else if(mViewPager.getCurrentItem()==3) {

                    mViewPager.setCurrentItem(getItem(+1), true);
                } else if(mViewPager.getCurrentItem()==4) {
                    if(f9.getResult().toString()=="{}"){
                        Toast.makeText(BookmarkActivity.this, "행동 원인을 골라주세요.", Toast.LENGTH_SHORT).show();
                    } else {
                        fab.setEnabled(false);
//                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy년 MM월 dd일 EEE aa hh:mm", Locale.KOREAN);

//                        Date date_start = formatter.parse(f1.date_start + " " + f1.hour_start);
//                        Date date_end = formatter.parse(f1.date_start + " " + f1.hour_end);
//                        Calendar cal = Calendar.getInstance();

                        docData = new HashMap<>();
//                            docData.put("start_time", date_start);
//                            docData.put("end_time", date_end);
                        docData.put("place", f2.getResult());
                        docData.put("categorization", f3.getResult());
//                            docData.put("current_behavior", f4.textInput.getText().toString());
//                            docData.put("before_behavior", f5.textInput.getText().toString());
//                            docData.put("after_behavior", f6.textInput.getText().toString());
                        docData.put("type", f7.getResult());
                        docData.put("intensity", f8.seekbarValue + 1);
                        docData.put("reason_type", f9.getResult());
                        docData.put("reason", f9.reasonDetail());

                        if(editBookmarkState == true) {
                            docData.put("title", editBookmark.title);

                            final DocumentReference sfDocRef = MainActivity.db.collection("users").document(MainActivity.user.getUid());
                            MainActivity.db.runTransaction(new Transaction.Function<Void>() {
                                @Override
                                public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                                    DocumentSnapshot snapshot = transaction.get(sfDocRef);
                                    List<Map<String, Object>> behaviorPresetArray = (List<Map<String, Object>>) snapshot.get("preset.behavior_preset");
                                    behaviorPresetArray.set(editBookmark.position, docData);
                                    transaction.update(sfDocRef, "preset.behavior_preset", behaviorPresetArray);

                                    // Success
                                    return null;
                                }
                            }).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
//                        mProgressView.setVisibility(View.GONE);
                                    MainActivity.adapter.notifyDataSetChanged();

                                    BookmarkDetailActivity.selectedBookmark.categorization = docData.get("categorization").toString();
                                    BookmarkDetailActivity.selectedBookmark.place = docData.get("place").toString();
                                    BookmarkDetailActivity.selectedBookmark.type = (HashMap<String, Object>) docData.get("type");
                                    BookmarkDetailActivity.selectedBookmark.reason_type = (HashMap<String, Object>) docData.get("reason_type");
                                    BookmarkDetailActivity.selectedBookmark.reason = (HashMap<String, Object>) docData.get("reason");
                                    BookmarkDetailActivity.selectedBookmark.intensity = (int) docData.get("intensity");

                                    BookmarkDetailActivity.bookmark_categorization.setText(docData.get("categorization").toString());
                                    BookmarkDetailActivity.bookmark_place.setText(docData.get("place").toString());
                                    BookmarkDetailActivity.setType((HashMap<String, Object>) docData.get("type"));
                                    BookmarkDetailActivity.setReasonType((HashMap<String, Object>) docData.get("reason_type"));
                                    BookmarkDetailActivity.setIntensity((int) docData.get("intensity"));
                                    BookmarkDetailActivity.editModeOff();

                                    finish();
                                    Toast toast = Toast.makeText(BookmarkActivity.this, "자주 쓰는 기록이 수정되었습니다.", Toast.LENGTH_SHORT);
                                    toast.show();

                                }
                            })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            fab.setEnabled(true);
                                        }
                                    });
                        } else {
                            docData.put("title", f2.getResult() + " / " + f3.getResult() + " / " + reasonTypeSerialize(f9.getResult()));

                            final DocumentReference sfDocRef = MainActivity.db.collection("users").document(MainActivity.user.getUid());
                            MainActivity.db.runTransaction(new Transaction.Function<Void>() {
                                @Override
                                public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                                    DocumentSnapshot snapshot = transaction.get(sfDocRef);
                                    List<Map<String, Object>> behaviorPresetArray = (List<Map<String, Object>>) snapshot.get("preset.behavior_preset");
                                    behaviorPresetArray.add(docData);
                                    transaction.update(sfDocRef, "preset.behavior_preset", behaviorPresetArray);

                                    // Success
                                    return null;
                                }
                            }).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
//                        mProgressView.setVisibility(View.GONE);
                                    MainActivity.adapter.notifyDataSetChanged();
                                    finish();
                                    Toast toast = Toast.makeText(BookmarkActivity.this, "자주 쓰는 기록이 추가되었습니다.", Toast.LENGTH_SHORT);
                                    toast.show();

                                }
                            })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            fab.setEnabled(true);
                                        }
                                    });
                        }
                    }
                }
            }
        });

    }

    public String reasonTypeSerialize(HashMap<String, Boolean> hashMap) {

        String tmp_reason = "";

        if(hashMap.get("interest")!=null) {
            tmp_reason = tmp_reason + "관심, ";
        }
        if(hashMap.get("selfstimulation")!=null) {
            tmp_reason = tmp_reason + "자기자극, ";
        }
        if(hashMap.get("taskevation")!=null) {
            tmp_reason = tmp_reason + "과제회피, ";
        }
        if(hashMap.get("demand")!=null) {
            tmp_reason = tmp_reason + "요구, ";
        }
        if(hashMap.get("etc")!=null){
            tmp_reason = tmp_reason + "기타, ";
        }

        return tmp_reason.substring(0, tmp_reason.length()-2);
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
        int mNumOfTabs = 5;

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
//                case 0:
//                    BehaviorFirstFragment tab1 = new BehaviorFirstFragment();
//                    return tab1;
                case 0:
                    BehaviorSecondFragment tab2 = new BehaviorSecondFragment();
                    return tab2;
                case 1:
                    BehaviorThirdFragment tab3 = new BehaviorThirdFragment();
                    return tab3;
//                case 3:
//                    BehaviorFourthFragment tab4 = new BehaviorFourthFragment();
//                    return tab4;
//                case 4:
//                    BehaviorFifthFragment tab5 = new BehaviorFifthFragment();
//                    return tab5;
//                case 5:
//                    BehaviorSixthFragment tab6 = new BehaviorSixthFragment();
//                    return tab6;
                case 2:
                    BehaviorSeventhFragment tab7 = new BehaviorSeventhFragment();
                    return tab7;
                case 3:
                    BehaviorEighthFragment tab8 = new BehaviorEighthFragment();
                    return tab8;
                case 4:
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

