package com.geniauti.geniauti;

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
import com.google.firebase.firestore.CollectionReference;
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
 * {@link ChartYearFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ChartYearFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChartYearFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private View v;
    public static HashMap<String, Statistics> statisticsHashMap;

    private OnFragmentInteractionListener mListener;
    public static ViewPager viewPager;
    public static ViewPagerAdapter adapter;
    public static TextView yearDate;
    private Calendar cal, tmpcal;
    private SimpleDateFormat sdf, sdfNew;
    public static ImageView forwardButton;
    public static ImageView backButton;
    private String yearDateandTime;
    private String previousDateandTime;
    private int lastPage;

    public static View mProgressView;

    public ChartYearFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChartYearFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChartYearFragment newInstance(String param1, String param2) {
        ChartYearFragment fragment = new ChartYearFragment();
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
        v = inflater.inflate(R.layout.fragment_chart_year, container, false);

        yearDate = v.findViewById(R.id.txt_chart_year);
        sdf = new SimpleDateFormat("yyyy년", Locale.KOREAN);
        sdfNew = new SimpleDateFormat("yyyy", Locale.KOREAN);

        mProgressView = (View) v.findViewById(R.id.chart_year_progress);
        mProgressView.bringToFront();

        backButton = v.findViewById(R.id.chart_year_back);
        forwardButton = v.findViewById(R.id.chart_year_forward);

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

        viewPager = (ViewPager) v.findViewById(R.id.chart_year_viewpager);

        MainActivity.db.collection("statistics").document(MainActivity.cid).collection("year")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
//                            Log.w(TAG, "Listen failed.", e);
                            return;
                        }

                        statisticsHashMap = new HashMap<>();

                        for (QueryDocumentSnapshot doc : value) {
                            Statistics item = new Statistics(doc.getId(), (HashMap<String, Object>) doc.get("behavior_freq"), (HashMap<String, Object>) doc.get("summary"),
                                    (HashMap<String, Object>) doc.get("type"), (HashMap<String, Object>) doc.get("reason_type"), (HashMap<String, Object>) doc.get("place"));
                            statisticsHashMap.put(doc.getId(), item);
                        }

                        if(getActivity()!=null) {
                            forwardButton.setVisibility(View.GONE);

                            cal = Calendar.getInstance();
                            yearDateandTime = sdf.format(cal.getTime());
                            yearDate.setText(yearDateandTime);

                            tmpcal = Calendar.getInstance();
                            tmpcal.add(Calendar.YEAR, -1);
                            previousDateandTime = sdf.format(tmpcal.getTime());

                            adapter = new ViewPagerAdapter(getFragmentManager());
                            viewPager.setAdapter(adapter);
                            lastPage = adapter.getCount()-1;
                            viewPager.clearOnPageChangeListeners();
                            viewPager.setCurrentItem(adapter.getCount()-1);
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
                                        cal.add(Calendar.YEAR, -1);
                                        yearDate.setText(sdf.format(cal.getTime()));

                                        if(sdf.format(cal.getTime()).equals(previousDateandTime)) {
                                            forwardButton.setVisibility(View.VISIBLE);
                                        }
                                    } else if(position > lastPage) {
                                        cal.add(Calendar.YEAR, 1);
                                        yearDate.setText(sdf.format(cal.getTime()));

                                        if(sdf.format(cal.getTime()).equals(yearDateandTime)) {
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

//        db.collection("childs")
//                .whereGreaterThanOrEqualTo("users."+user.getUid()+".name", "")
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                cid = document.getId();
//                            }
//
//                            db.collection("behaviors")
//                                    .whereEqualTo("cid", cid)
//                                    .get()
//                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                                        @Override
//                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                                            if (task.isSuccessful()) {
//                                                for (QueryDocumentSnapshot document : task.getResult()) {
//                                                    Behavior item = new Behavior(document.getId(), (Date) document.getDate("start_time"), (Date) document.getDate("end_time"), document.get("place").toString(), document.get("categorization").toString(), document.get("current_behavior").toString(), document.get("before_behavior").toString(), document.get("after_behavior").toString(), (HashMap<String, Object>) document.get("type"), Integer.parseInt(document.get("intensity").toString()), (HashMap<String, Object>) document.get("reason_type"), (HashMap<String, Object>) document.get("reason"), document.get("created_at").toString(),  document.get("updated_at").toString(), document.get("uid").toString(), document.get("name").toString(), document.get("cid").toString(), "");
//                                                    behaviorData.add(item);
//                                                }
//
//                                                // Setting ViewPager for each Tabs
//                                                viewPager = (ViewPager) v.findViewById(R.id.chart_year_viewpager);
//                                                setupViewPager(viewPager);
//                                                lastPage = adapter.getCount()-1;
//
//                                                viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener()
//                                                {
//                                                    @Override
//                                                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
//                                                    {
//
//                                                    }
//
//                                                    @Override
//                                                    public void onPageSelected(int position)
//                                                    {
//                                                        if(position < lastPage) {
//                                                            cal.add(Calendar.YEAR, -1);
//                                                            yearDate.setText(sdf.format(cal.getTime()));
//
//                                                            if(sdf.format(cal.getTime()).equals(preveiousDateandTime)) {
//                                                                forwardButton.setVisibility(View.VISIBLE);
//                                                            }
//                                                        } else if(position > lastPage) {
//                                                            cal.add(Calendar.YEAR, 1);
//                                                            yearDate.setText(sdf.format(cal.getTime()));
//
//                                                            if(sdf.format(cal.getTime()).equals(yearDateandTime)) {
//                                                                forwardButton.setVisibility(View.GONE);
//                                                            }
//                                                        }
//
//                                                        lastPage = position;
//                                                    }
//
//                                                    @Override
//                                                    public void onPageScrollStateChanged(int state) {
//
//                                                    }
//                                                });
//
//                                            } else {
//
//                                            }
//                                        }
//                                    });
//                        } else {
////                                Log.d(TAG, "Error getting documents: ", task.getException());
//                        }
//                    }
//                });

        return v;
    }

    public class ViewPagerAdapter extends FragmentStatePagerAdapter {

        long msDiff = Calendar.getInstance().getTimeInMillis();
        long daysDiff = TimeUnit.MILLISECONDS.toDays(msDiff)/365;

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return TemplateChartYearFragment.newInstance(position);
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
