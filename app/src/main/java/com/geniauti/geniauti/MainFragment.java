package com.geniauti.geniauti;

import android.app.AlertDialog;
import android.support.annotation.NonNull;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.geniauti.geniauti.compactcalendarview.CompactCalendarView;
import com.geniauti.geniauti.compactcalendarview.domain.Event;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MainFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment {

    private CompactCalendarView compactCalendar;

    private SimpleDateFormat dateFormatMonth = new SimpleDateFormat("yyyy년 MM월", Locale.KOREAN);

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Calendar cal = Calendar.getInstance();

    private boolean calendarShow = true;
    private LinearLayout calendarLayout;
    private View v;
    private OnFragmentInteractionListener mListener;
    private TextView txtMonth;
    private LinearLayout calendarBack;
    private LinearLayout calendarForward;

    private ArrayList<Behavior> cardData;
    private ArrayList<Behavior> tmpData;
    private ListView cardListView;
    private CardListviewAdapter cardAdapter;
    private CardView cardBlankBehavior;

    private int color_interest;
    private int color_self_stimulation;
    private int color_task_evation;
    private int color_demand;
    private int color_etc;
    private int colorCode;
    private long timeInMilliseconds;
    private HashMap<String, HashMap<String, Boolean>> calendarHash = new HashMap<>();
    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy년 MM월 dd일", Locale.KOREAN);

    private ListView bookmarkListView;
    public static ArrayList<Bookmark> bookmarkData;
    public static BookmarkListviewAdapter bookmarkAdapter;
    private AlertDialog dialog;
    private View behavior_add_line;

    public MainFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainFragment newInstance(String param1, String param2) {
        MainFragment fragment = new MainFragment();
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

        setHasOptionsMenu(true);
        setRetainInstance(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.menu_main_calendar) {
            if(!calendarShow) {
                expand(calendarLayout);
                calendarShow = true;
            } else {
                collapse(calendarLayout);
                calendarShow = false;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    public static void expand(final View v) {
        v.measure(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        final int targetHeight = v.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? LinearLayout.LayoutParams.WRAP_CONTENT
                        : (int)(targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int)(targetHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    public static void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if(interpolatedTime == 1){
                    v.setVisibility(View.GONE);
                }else{
                    v.getLayoutParams().height = initialHeight - (int)(initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int)(initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (v != null) {
            if ((ViewGroup) v.getParent() != null)
                ((ViewGroup) v.getParent()).removeView(v);
            return v;
        }
        v = inflater.inflate(R.layout.fragment_main, container, false);

        calendarLayout = (LinearLayout) v.findViewById(R.id.calendar_layout);

        // final ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        // actionBar.setDisplayHomeAsUpEnabled(false);
        // actionBar.setTitle(null);

        compactCalendar = (CompactCalendarView) v.findViewById(R.id.compactcalendar_view);
        compactCalendar.setCurrentSelectedDayTextColor(Color.parseColor("#ffffff"));
        compactCalendar.shouldSelectFirstDayOfMonthOnScroll(false);
        compactCalendar.shouldDrawIndicatorsBelowSelectedDays(true);

        calendarBack = (LinearLayout) v.findViewById(R.id.calendar_back);
        calendarForward = (LinearLayout) v.findViewById(R.id.calendar_forward);

        calendarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                compactCalendar.scrollLeft();
            }
        });

        calendarForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                compactCalendar.scrollRight();
            }
        });

        color_interest = Color.parseColor("#db9a53");
        color_self_stimulation = Color.parseColor("#3fd3b8");
        color_task_evation = Color.parseColor("#3f73d3");
        color_demand = Color.parseColor("#7b3fd3");
        color_etc = Color.parseColor("#93c7ff");

        compactCalendar.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                Context context = getContext();

                int position = foundIndex(cardData, dateClicked);
                if(position == -1) {

                } else {
                    // Scroll to Position
                    int duration = 250;  //miliseconds
                    int offset = 0;      //fromListTop

                    cardListView.smoothScrollToPositionFromTop(position,offset,duration);
                }

            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                txtMonth.setText(dateFormatMonth.format(firstDayOfNewMonth));
            }
        });

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext());
        View mView = getLayoutInflater().inflate(R.layout.dialog_behavior_add, null);
        mBuilder.setView(mView);
        dialog = mBuilder.create();
        behavior_add_line = (View) mView.findViewById(R.id.behavior_add_line);

        LinearLayout behavior_add = (LinearLayout) mView.findViewById(R.id.behavior_add);
        behavior_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),BehaviorActivity.class);
                startActivity(intent);
                dialog.dismiss();
            }
        });

        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
            }
        });

        bookmarkListView = (ListView) mView.findViewById(R.id.behavior_listview);
        bookmarkData = new ArrayList<>();

        MainActivity.db.collection("users").document(MainActivity.user.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if(document.exists()) {
                                HashMap<String, Object> preset = (HashMap<String, Object>) document.getData().get("preset");
                                if(preset != null){
                                    List<HashMap<String, Object>> behavior_preset = (List<HashMap<String,Object>>) preset.get("behavior_preset");
                                    for(int i=0; i<behavior_preset.size(); i++) {
                                        Bookmark item = new Bookmark(i, behavior_preset.get(i).get("title").toString(), behavior_preset.get(i).get("place").toString(),
                                                behavior_preset.get(i).get("categorization").toString(), (HashMap<String, Object>) behavior_preset.get(i).get("type"),
                                                Integer.parseInt(behavior_preset.get(i).get("intensity").toString()), (HashMap<String, Object>) behavior_preset.get(i).get("reason_type"),
                                                (HashMap<String, Object>) behavior_preset.get(i).get("reason"));
                                        bookmarkData.add(item);
                                    }

                                    bookmarkAdapter = new BookmarkListviewAdapter(getContext(), R.layout.list_bookmark_profile, bookmarkData);
                                    bookmarkListView.setAdapter(bookmarkAdapter);
                                }
                            } else {
                                behavior_add_line.setVisibility(View.GONE);
                            }
                        } else {
//                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });


        // cardListView
        cardListView = (ListView) v.findViewById(R.id.behavior_card_listview);
        cardData = new ArrayList<>();
        tmpData = new ArrayList<>();

        cardBlankBehavior = (CardView) v.findViewById(R.id.blank_behavior_layout);

        MainActivity.db.collection("behaviors")
                .whereEqualTo("cid", MainActivity.cid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                HashMap<String, Boolean> reasonToday = (HashMap<String, Boolean>) document.get("reason_type");
                                String todayDate = formatter.format(document.getDate("start_time"));

                                if(calendarHash.get(todayDate)==null){
                                    calendarHash.put(todayDate, reasonToday);
                                } else {
                                    HashMap<String, Boolean> reasonHash = (HashMap<String, Boolean>) calendarHash.get(todayDate);
                                    HashMap<String, Boolean> tmpReason = calendarHash.get(todayDate);
                                    if(reasonHash.get("interest")==null && reasonToday.get("interest")!=null) {
                                        tmpReason.put("interest", true);
                                    }
                                    if(reasonHash.get("selfstimulation")==null && reasonToday.get("selfstimulation")!=null) {
                                        tmpReason.put("selfstimulation", true);
                                    }
                                    if(reasonHash.get("taskevation")==null && reasonToday.get("taskevation")!=null) {
                                        tmpReason.put("taskevation", true);
                                    }
                                    if(reasonHash.get("demand")==null && reasonToday.get("demand")!=null) {
                                        tmpReason.put("demand", true);
                                    }
                                    if(reasonHash.get("etc")==null && reasonToday.get("etc")!=null){
                                        tmpReason.put("etc", true);
                                    }
                                    calendarHash.put(todayDate, tmpReason);
                                }

                                Behavior item = new Behavior(document.getId(), (Date) document.getDate("start_time"), (Date) document.getDate("end_time"), document.get("place").toString(),
                                        document.get("categorization").toString(), document.get("current_behavior").toString(), document.get("before_behavior").toString(), document.get("after_behavior").toString(),
                                        (HashMap<String, Object>) document.get("type"), Integer.parseInt(document.get("intensity").toString()), (HashMap<String, Object>) document.get("reason_type"),
                                        (HashMap<String, Object>) document.get("reason"), document.get("created_at").toString(),  document.get("updated_at").toString(), document.get("uid").toString(),
                                        document.get("name").toString(), document.get("cid").toString(), document.get("relationship").toString());
                                cardData.add(item);
                            }

                            if(cardData.size() == 0){
                                cardBlankBehavior.setVisibility(View.VISIBLE);
                            } else {
                                makeDotLoop(calendarHash);

                                Collections.sort(cardData, new BehaviorComparator());
                                tmpData.addAll(cardData);

                                cardAdapter = new CardListviewAdapter(getContext(), R.layout.list_behavior_card, cardData);
                                cardListView.setAdapter(cardAdapter);
                            }

                        } else {

                        }
                    }
                });

        // Today List View
        int date = cal.get(Calendar.DATE);
        int month = cal.get(Calendar.MONTH);
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        String korDayOfWeek = "";

        txtMonth = (TextView) v.findViewById(R.id.txt_month);
        if(month < 9) {
            txtMonth.setText(cal.get(Calendar.YEAR) +"년 " + "0" + String.valueOf(month+1)+"월");
        } else {
            txtMonth.setText(cal.get(Calendar.YEAR) +"년 " + String.valueOf(month+1)+"월");
        }

        switch(dayOfWeek) {
            case 1:
                korDayOfWeek = "일요일";
                break;
            case 2:
                korDayOfWeek = "월요일";
                break;
            case 3:
                korDayOfWeek = "화요일";
                break;
            case 4:
                korDayOfWeek = "수요일";
                break;
            case 5:
                korDayOfWeek = "목요일";
                break;
            case 6:
                korDayOfWeek = "금요일";
                break;
            case 7:
                korDayOfWeek = "토요일";
                break;
        }

        return v;
    }

    public void  makeDotLoop(HashMap<String, HashMap<String, Boolean>> calendarHash) {
        Iterator it = calendarHash.entrySet().iterator();

        while(it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            try {
                Date today = formatter.parse(pair.getKey().toString());
                HashMap<String, Boolean> reason = (HashMap<String, Boolean>) pair.getValue();

                if(reason.get("interest")!=null){
                    makeDot(today, "interest");
                }
                if(reason.get("selfstimulation")!=null){
                    makeDot(today, "selfstimulation");
                }
                if(reason.get("taskevation")!=null){
                    makeDot(today, "taskevation");
                }
                if(reason.get("demand")!=null){
                    makeDot(today, "demand");
                }
                if(reason.get("etc")!=null){
                    makeDot(today, "etc");
                }
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public void makeDot(Date date, String reason) {
        // Color Code
        switch(reason) {
            case "interest":
                colorCode = color_interest;
                break;
            case "selfstimulation":
                colorCode = color_self_stimulation;
                break;
            case "taskevation":
                colorCode = color_task_evation;
                break;
            case "demand":
                colorCode = color_demand;
                break;
            case "etc":
                colorCode = color_etc;
                break;
        }

        // Date
        timeInMilliseconds = date.getTime();
        Event ev = new Event(colorCode, timeInMilliseconds, "");
        compactCalendar.addEvent(ev);
    }

    public int foundIndex(ArrayList<Behavior> behavior, Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy년 MM월 dd일 E요일", Locale.KOREAN);
        for (int i = 0; i < behavior.size(); i++) {
            if(formatter.format(behavior.get(i).start_time).equals(formatter.format(date))){
                return i;
            }
        }
        return -1;
    }

    public class BehaviorComparator implements Comparator<Behavior> {
        public int compare(Behavior left, Behavior right) {
            if(left.start_time.before(right.start_time)) {
                return 1;
            } else {
                return -1;
            }
        }
    }

    public class CardListviewAdapter extends BaseAdapter {

        private LayoutInflater inflater;
        private ArrayList<Behavior> data;
        private int layout;

        public CardListviewAdapter(Context context, int layout, ArrayList<Behavior> data){
            this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.data = data;
            this.layout = layout;
        }

        @Override
        public int getCount(){return data.size();}

        @Override
        public Object getItem(int position){
            return data.get(position);
        }

        @Override
        public long getItemId(int position){return position;}

        @Override
        public View getView(final int position, View convertView, ViewGroup parent){
            if(convertView == null){
                convertView = inflater.inflate(layout, parent, false);
            }

            Behavior listviewitem = data.get(position);

            String date_compare = "";
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy년 MM월 dd일 E요일", Locale.KOREAN);
            Date dateToday = new Date();

            if(position!=0) {
                Behavior tmp_listviewitem = data.get(position-1);
                date_compare = formatter.format(tmp_listviewitem.start_time);
            }

            LinearLayout card_layout = convertView.findViewById(R.id.behavior_card_layout);
            card_layout.setEnabled(false);
            card_layout.setClickable(false);
            card_layout.setOnClickListener(null);

            LinearLayout layout_date = convertView.findViewById(R.id.layout_date);
            TextView txt_date = convertView.findViewById(R.id.txt_date);
            if(formatter.format(listviewitem.start_time).equals(date_compare)) {
                layout_date.setVisibility(View.GONE);
            } else {
                layout_date.setVisibility(View.VISIBLE);
                if(formatter.format(listviewitem.start_time).equals(formatter.format(dateToday))){
                    txt_date.setTextColor(Color.parseColor("#2dc76d"));
                } else {
                    txt_date.setTextColor(Color.parseColor("#3b3b3b"));
                }

                String date_format = formatter.format(listviewitem.start_time);
                txt_date.setText(date_format);
            }

            LinearLayout layoutCard = convertView.findViewById(R.id.layout_card);
            layoutCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), BehaviorDetailActivity.class);
                    intent.putExtra("temp", (Behavior) cardAdapter.getItem(position));
                    startActivity(intent);
                }
            });

            LinearLayout card_interest = convertView.findViewById(R.id.card_interest);
            LinearLayout card_self_stimulation = convertView.findViewById(R.id.card_self_stimulation);
            LinearLayout card_task_evation = convertView.findViewById(R.id.card_task_evation);
            LinearLayout card_demand = convertView.findViewById(R.id.card_demand);
            LinearLayout card_etc = convertView.findViewById(R.id.card_etc);

            TextView txtCategorization = convertView.findViewById(R.id.txt_card_categorization);
            TextView txtTime = convertView.findViewById(R.id.txt_card_time);
            TextView txtNameRelationship = convertView.findViewById(R.id.txt_card_name_relationship);

            txtCategorization.setText(listviewitem.categorization);

            SimpleDateFormat formatterHour = new SimpleDateFormat("aa hh:mm", Locale.KOREAN);
            txtTime.setText(formatterHour.format(listviewitem.start_time) +" ~ "+ formatterHour.format(listviewitem.end_time));
            txtNameRelationship.setText(listviewitem.name+"("+listviewitem.relationship+")");

            card_interest.setVisibility(View.GONE);
            card_self_stimulation.setVisibility(View.GONE);
            card_task_evation.setVisibility(View.GONE);
            card_demand.setVisibility(View.GONE);
            card_etc.setVisibility(View.GONE);

            if(listviewitem.reason_type.get("interest")!=null) {
                card_interest.setVisibility(View.VISIBLE);
            }
            if(listviewitem.reason_type.get("selfstimulation")!=null) {
                card_self_stimulation.setVisibility(View.VISIBLE);
            }
            if(listviewitem.reason_type.get("taskevation")!=null) {
                card_task_evation.setVisibility(View.VISIBLE);
            }
            if(listviewitem.reason_type.get("demand")!=null) {
                card_demand.setVisibility(View.VISIBLE);
            }
            if(listviewitem.reason_type.get("etc")!=null){
                card_etc.setVisibility(View.VISIBLE);
            }

//            if(listviewitem.case_tags.get("harm")!=null) {
//                tmp_type = tmp_type + "타해 / ";
//            }
//            if(listviewitem.case_tags.get("selfHArm")!=null) {
//                tmp_type = tmp_type + "자해 / ";
//            }
//            if(listviewitem.case_tags.get("destruction")!=null) {
//                tmp_type = tmp_type + "파괴 / ";
//            }
//            if(listviewitem.case_tags.get("leave")!=null) {
//                tmp_type = tmp_type + "이탈 / ";
//            }
//            if(listviewitem.case_tags.get("sexual")!=null) {
//                tmp_type = tmp_type + "성적 / ";
//            }

            return convertView;
        }
    }

    public class BookmarkListviewAdapter extends BaseAdapter {

        private LayoutInflater inflater;
        private ArrayList<Bookmark> data;
        private int layout;

        public BookmarkListviewAdapter(Context context, int layout, ArrayList<Bookmark> data){
            this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.data = data;
            this.layout = layout;
        }

        @Override
        public int getCount(){return data.size();}

        @Override
        public Object getItem(int position){
            return data.get(position);
        }

        @Override
        public long getItemId(int position){return position;}

        @Override
        public View getView(final int position, View convertView, ViewGroup parent){
            if(convertView == null){
                convertView = inflater.inflate(R.layout.list_bookmark_behavior, parent, false);
            }

            final Bookmark bookmarkData = data.get(position);

            TextView bookmarkTitle = (TextView) convertView.findViewById(R.id.list_bookmark_behavior_title);
            TextView bookmarkSub = (TextView) convertView.findViewById(R.id.list_bookmark_behavior_sub);

            bookmarkTitle.setText(bookmarkData.title);
            bookmarkSub.setText("저장된 기록" + String.valueOf(position + 1));

            LinearLayout bookmarkLayout = (LinearLayout) convertView.findViewById(R.id.list_bookmark_behavior_layout);
            bookmarkLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), BehaviorActivity.class);
                    intent.putExtra("bookmark", (Bookmark) bookmarkData);
                    startActivity(intent);
                    dialog.dismiss();
                }
            });

            return convertView;
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

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
