package com.geniauti.geniauti;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MainFragment.OnFragmentInteractionListener, DicFragment.OnFragmentInteractionListener, ChartFragment.OnFragmentInteractionListener, ProfileFragment.OnFragmentInteractionListener {

    private CustomViewPager viewPager;

    MainFragment mainFragment;
    DicFragment dicFragment;
    ChartFragment chartFragment;
    ProfileFragment profileFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("달력");

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
                        getSupportActionBar().setTitle("달력");
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

        setupViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        mainFragment = new MainFragment();
        dicFragment = new DicFragment();
        chartFragment = new ChartFragment();
        profileFragment = new ProfileFragment();

        adapter.addFragment(mainFragment);
        adapter.addFragment(dicFragment);
        adapter.addFragment(chartFragment);
        adapter.addFragment(profileFragment);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(3);
    }

    public class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }
        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment) {
            mFragmentList.add(fragment);
        }

    }

    @Override
    public void onFragmentInteraction(Uri uri){
        System.out.println(uri);
    }

}
