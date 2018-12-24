package com.geniauti.geniauti;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
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
import java.util.Locale;
import java.util.concurrent.TimeUnit;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ChartWeekFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ChartWeekFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChartWeekFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FirebaseUser user;
    private FirebaseFirestore db;
    private String cid;

    private View v;
    private ArrayList<Behavior> behaviorData;

    private OnFragmentInteractionListener mListener;
    public static ViewPager viewPager;
    public static ViewPagerAdapter adapter;
    private TextView weekDate;
    private Calendar cal, calSunday, calSaturday, tmpcal;
    private SimpleDateFormat sdf, sdfCompare, sdfDay;
    private ImageView forwardButton;
    private String weekDateandTime;
    private String yesterdayDateandTime;
    private int lastPage;

    public ChartWeekFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChartWeekFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChartWeekFragment newInstance(String param1, String param2) {
        ChartWeekFragment fragment = new ChartWeekFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_chart_week, container, false);

        weekDate = v.findViewById(R.id.txt_chart_week);
        sdf = new SimpleDateFormat("yyyy년 MM월", Locale.KOREAN);
        sdfCompare = new SimpleDateFormat("yyyy년 MM월 dd일", Locale.KOREAN);
        sdfDay = new SimpleDateFormat("dd일", Locale.KOREAN);
        cal = Calendar.getInstance();
        calSunday = Calendar.getInstance();
        calSaturday = Calendar.getInstance();
        weekDateandTime = sdfCompare.format(cal.getTime());

        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        if (Calendar.MONDAY == dayOfWeek) {
            calSunday.add(Calendar.DATE, -1);
            calSaturday.add(Calendar.DATE, +5);
        } else if (Calendar.TUESDAY == dayOfWeek) {
            calSunday.add(Calendar.DATE, -2);
            calSaturday.add(Calendar.DATE, +4);
        } else if (Calendar.WEDNESDAY == dayOfWeek) {
            calSunday.add(Calendar.DATE, -3);
            calSaturday.add(Calendar.DATE, +3);
        } else if (Calendar.THURSDAY == dayOfWeek) {
            calSunday.add(Calendar.DATE, -4);
            calSaturday.add(Calendar.DATE, +2);
        } else if (Calendar.FRIDAY == dayOfWeek) {
            calSunday.add(Calendar.DATE, -5);
            calSaturday.add(Calendar.DATE, +1);
        } else if (Calendar.SATURDAY == dayOfWeek) {
            calSunday.add(Calendar.DATE, -6);
            calSaturday.add(Calendar.DATE, +0);
        } else if (Calendar.SUNDAY == dayOfWeek) {
            calSunday.add(Calendar.DATE, -0);
            calSaturday.add(Calendar.DATE, +6);
        }
        weekDate.setText(weekDateResult(cal, calSunday, calSaturday));

        tmpcal = Calendar.getInstance();
        tmpcal.add(Calendar.DATE, -7);
        yesterdayDateandTime = sdfCompare.format(tmpcal.getTime());

        ImageView backButton = v.findViewById(R.id.chart_week_back);
        forwardButton = v.findViewById(R.id.chart_week_forward);
        forwardButton.setVisibility(View.GONE);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(viewPager.getCurrentItem()-1, true);
            }
        });

        forwardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(viewPager.getCurrentItem()+1, true);
            }
        });

        user = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();

        behaviorData = new ArrayList<>();

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

                            db.collection("behaviors")
                                    .whereEqualTo("cid", cid)
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                    Behavior item = new Behavior((Date) document.getDate("start_time"), (Date) document.getDate("end_time"), document.get("place").toString(), document.get("categorization").toString(), document.get("current_behavior").toString(), document.get("before_behavior").toString(), document.get("after_behavior").toString(), (HashMap<String, Object>) document.get("type"), Integer.parseInt(document.get("intensity").toString()), (HashMap<String, Object>) document.get("reason"), document.get("created_at").toString(),  document.get("updated_at").toString(), document.get("uid").toString(), document.get("name").toString(), document.get("cid").toString(), "");
                                                    behaviorData.add(item);
                                                }

                                                // Setting ViewPager for each Tabs
                                                viewPager = (ViewPager) v.findViewById(R.id.chart_week_viewpager);
                                                setupViewPager(viewPager);
                                                lastPage = adapter.getCount()-1;

                                                viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener()
                                                {
                                                    @Override
                                                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
                                                    {

                                                    }

                                                    @Override
                                                    public void onPageSelected(int position)
                                                    {
                                                        if(position < lastPage) {
                                                            cal.add(Calendar.DATE, -7);
                                                            calSunday.add(Calendar.DATE, -7);
                                                            calSaturday.add(Calendar.DATE, -7);
                                                            weekDate.setText(weekDateResult(cal, calSunday, calSaturday));

                                                            if(sdfCompare.format(cal.getTime()).equals(yesterdayDateandTime)) {
                                                                forwardButton.setVisibility(View.VISIBLE);
                                                            }
                                                        } else if(position > lastPage) {
                                                            cal.add(Calendar.DATE, +7);
                                                            calSunday.add(Calendar.DATE, +7);
                                                            calSaturday.add(Calendar.DATE, +7);
                                                            weekDate.setText(weekDateResult(cal, calSunday, calSaturday));

                                                            if(sdfCompare.format(cal.getTime()).equals(weekDateandTime)) {
                                                                forwardButton.setVisibility(View.GONE);
                                                            }
                                                        }

                                                        lastPage = position;
                                                    }

                                                    @Override
                                                    public void onPageScrollStateChanged(int state) {

                                                    }
                                                });

                                            } else {

                                            }
                                        }
                                    });
                        } else {
//                                Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });


        return v;
    }

    public String weekDateResult(Calendar cal, Calendar calSunday, Calendar calSaturday) {

        return sdf.format(cal.getTime()) + " " + sdfDay.format(calSunday.getTime()) + " - " + sdfDay.format(calSaturday.getTime());
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(adapter.getCount()-1);

    }

    public class ViewPagerAdapter extends FragmentStatePagerAdapter {

        long msDiff = Calendar.getInstance().getTimeInMillis();
        long daysDiff = TimeUnit.MILLISECONDS.toDays(msDiff)*365/12/4;

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return TemplateChartWeekFragment.newInstance(position, behaviorData);
        }

        @Override
        public int getCount() {
            return (int) daysDiff;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
