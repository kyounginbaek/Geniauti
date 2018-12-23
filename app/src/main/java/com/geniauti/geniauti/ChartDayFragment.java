package com.geniauti.geniauti;

import android.content.Context;
import android.graphics.Color;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
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
import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ChartDayFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ChartDayFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChartDayFragment extends Fragment implements TemplateChartDayFragment.OnFragmentInteractionListener {
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
    private TextView todayDate;
    private Calendar cal, tmpcal;
    private SimpleDateFormat sdf;
    private ImageView forwardButton;
    private String todayDateandTime;
    private String yesterdayDateandTime;
    private int lastPage;

    public ChartDayFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChartDayFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChartDayFragment newInstance(String param1, String param2) {
        ChartDayFragment fragment = new ChartDayFragment();
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
        v = inflater.inflate(R.layout.fragment_chart_day, container, false);

        todayDate = v.findViewById(R.id.txt_chart_day);
        sdf = new SimpleDateFormat("yyyy년 MM월 dd일 EEE요일");
        cal = Calendar.getInstance();
        todayDateandTime = sdf.format(cal.getTime());
        todayDate.setText(todayDateandTime);

        tmpcal = Calendar.getInstance();
        tmpcal.add(Calendar.DATE, -1);
        yesterdayDateandTime = sdf.format(tmpcal.getTime());

        ImageView backButton = v.findViewById(R.id.chart_day_back);
        forwardButton = v.findViewById(R.id.chart_day_forward);
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
                                                viewPager = (ViewPager) v.findViewById(R.id.chart_day_viewpager);
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
                                                            cal.add(Calendar.DATE, -1);
                                                            todayDate.setText(sdf.format(cal.getTime()));

                                                            if(sdf.format(cal.getTime()).equals(yesterdayDateandTime)) {
                                                                forwardButton.setVisibility(View.VISIBLE);
                                                            }
                                                        } else if(position > lastPage) {
                                                            cal.add(Calendar.DATE, 1);
                                                            todayDate.setText(sdf.format(cal.getTime()));

                                                            if(sdf.format(cal.getTime()).equals(todayDateandTime)) {
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

    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(adapter.getCount()-1);

    }

    public class ViewPagerAdapter extends FragmentStatePagerAdapter {

        long msDiff = Calendar.getInstance().getTimeInMillis();
        long daysDiff = TimeUnit.MILLISECONDS.toDays(msDiff);

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return TemplateChartDayFragment.newInstance(position, behaviorData);
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

    @Override
    public void onFragmentInteraction(Uri uri){
        System.out.println(uri);
    }

}
