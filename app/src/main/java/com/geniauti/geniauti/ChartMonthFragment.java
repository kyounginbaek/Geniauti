package com.geniauti.geniauti;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
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
 * {@link ChartMonthFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ChartMonthFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChartMonthFragment extends Fragment {
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
    private ArrayList<BehaviorChart> behaviorData;

    private ChartMonthFragment.OnFragmentInteractionListener mListener;
    public static ViewPager viewPager;
    public static ChartMonthFragment.ViewPagerAdapter adapter;
    private TextView monthDate;
    private Calendar cal, tmpcal;
    private SimpleDateFormat sdf;
    private ImageView forwardButton;
    private String monthDateandTime;
    private String yesterdayDateandTime;
    private int lastPage;

    public ChartMonthFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChartMonthFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChartMonthFragment newInstance(String param1, String param2) {
        ChartMonthFragment fragment = new ChartMonthFragment();
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
        v = inflater.inflate(R.layout.fragment_chart_month, container, false);

        monthDate = v.findViewById(R.id.txt_chart_month);
        sdf = new SimpleDateFormat("yyyy년 MM월", Locale.KOREAN);
        cal = Calendar.getInstance();
        monthDateandTime = sdf.format(cal.getTime());
        monthDate.setText(monthDateandTime);

        tmpcal = Calendar.getInstance();
        tmpcal.add(Calendar.MONTH, -1);
        yesterdayDateandTime = sdf.format(tmpcal.getTime());

        ImageView backButton = v.findViewById(R.id.chart_month_back);
        forwardButton = v.findViewById(R.id.chart_month_forward);
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

        viewPager = (ViewPager) v.findViewById(R.id.chart_month_viewpager);

        MainActivity.db.collection("behaviors")
                .whereEqualTo("cid", MainActivity.cid)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
//                            Log.w(TAG, "Listen failed.", e);
                            return;
                        }

                        behaviorData = new ArrayList<>();

                        for (QueryDocumentSnapshot doc : value) {
                            BehaviorChart item = new BehaviorChart((Date) doc.getDate("start_time"), (Date) doc.getDate("end_time"), doc.get("place").toString(), doc.get("categorization").toString(),
                                    doc.get("current_behavior").toString(), doc.get("before_behavior").toString(), doc.get("after_behavior").toString(), (HashMap<String, Object>) doc.get("type"),
                                    Integer.parseInt(doc.get("intensity").toString()), (HashMap<String, Object>) doc.get("reason"), doc.get("created_at").toString(),  doc.get("updated_at").toString(),
                                    doc.get("uid").toString(), doc.get("name").toString(), doc.get("cid").toString(), "");
                            behaviorData.add(item);
                        }

                        if(getActivity()!=null) {
                            // Setting ViewPager for each Tabs
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
                                        cal.add(Calendar.MONTH, -1);
                                        monthDate.setText(sdf.format(cal.getTime()));

                                        if(sdf.format(cal.getTime()).equals(yesterdayDateandTime)) {
                                            forwardButton.setVisibility(View.VISIBLE);
                                        }
                                    } else if(position > lastPage) {
                                        cal.add(Calendar.MONTH, 1);
                                        monthDate.setText(sdf.format(cal.getTime()));

                                        if(sdf.format(cal.getTime()).equals(monthDateandTime)) {
                                            forwardButton.setVisibility(View.GONE);
                                        }
                                    }

                                    lastPage = position;
                                }

                                @Override
                                public void onPageScrollStateChanged(int state) {

                                }
                            });
                        }

                    }
                });

        return v;
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new ChartMonthFragment.ViewPagerAdapter(getFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(adapter.getCount()-1);
    }

    public class ViewPagerAdapter extends FragmentStatePagerAdapter {

        long msDiff = Calendar.getInstance().getTimeInMillis();
        long daysDiff = TimeUnit.MILLISECONDS.toDays(msDiff)/365*12;

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return TemplateChartMonthFragment.newInstance(position, behaviorData);
        }

        @Override
        public int getCount() {
            return (int) daysDiff;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public Parcelable saveState()
        {
            Bundle bundle = (Bundle) super.saveState();
            if (bundle != null)
            {
                // Never maintain any states from the base class, just null it out
                bundle.putParcelableArray("states", null);
            } else
            {
                // do nothing
            }
            return bundle;
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