package com.geniauti.geniauti;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentStatePagerAdapter;
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

import android.widget.TextView;

public class BehaviorActivity extends AppCompatActivity implements BehaviorFirstFragment.OnFragmentInteractionListener, BehaviorSecondFragment.OnFragmentInteractionListener, BehaviorThirdFragment.OnFragmentInteractionListener, BehaviorFourthFragment.OnFragmentInteractionListener {

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
    private ViewPager mViewPager;
    public Behavior tempBehavior;

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
        mViewPager = (ViewPager) findViewById(R.id.behavior_container);
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

                if(position==3) {
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

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mViewPager.getCurrentItem()==0) {
                    BehaviorFirstFragment bff = (BehaviorFirstFragment) getSupportFragmentManager().findFragmentById(R.id.behavior_first_frag);


                    mViewPager.setCurrentItem(getItem(+1), true);
                } else if(mViewPager.getCurrentItem()==1) {

                    mViewPager.setCurrentItem(getItem(+1), true);
                } else if(mViewPager.getCurrentItem()==2) {

                    mViewPager.setCurrentItem(getItem(+1), true);
                } else if(mViewPager.getCurrentItem()==3) {

                    finish();
                }
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_behavior, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_behavior, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */

    private int getItem(int i) {
        return mViewPager.getCurrentItem() + i;
    }

    public class PagerAdapter extends FragmentStatePagerAdapter {
        int mNumOfTabs = 4;

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
