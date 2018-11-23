package com.geniauti.geniauti;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MainFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment {

    CompactCalendarView compactCalendar;
    private SimpleDateFormat dateFormatMonth = new SimpleDateFormat("MMMM- yyyy", Locale.getDefault());

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private View v;
    private OnFragmentInteractionListener mListener;

    private FirebaseUser user;
    private FirebaseFirestore db;
    private String cid;
    private ArrayList<Behavior> cardData;
    private ListView cardListView;

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

        // final ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        // actionBar.setDisplayHomeAsUpEnabled(false);
        // actionBar.setTitle(null);

        compactCalendar = (CompactCalendarView) v.findViewById(R.id.compactcalendar_view);
        compactCalendar.setUseThreeLetterAbbreviation(true);

        //Set an event for Teachers' Professional Day 2016 which is 21st of October

        Event ev1 = new Event(Color.GREEN, 1540490079000L, "Teachers' Professional Day");
        compactCalendar.addEvent(ev1);

        Event ev2 = new Event(Color.RED, 1540490079000L, "Teachers' Professional Day");
        compactCalendar.addEvent(ev2);

        compactCalendar.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                Context context = getContext();

                if (dateClicked.toString().compareTo("Fri Oct 21 00:00:00 AST 2016") == 0) {
                    // Toast.makeText(context, "Teachers' Professional Day", Toast.LENGTH_SHORT).show();
                }else {
                    // Toast.makeText(context, "No Events Planned for that day", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                // actionBar.setTitle(dateFormatMonth.format(firstDayOfNewMonth));
            }
        });

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext());
        View mView = getLayoutInflater().inflate(R.layout.dialog_behavior_add, null);
        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();

        ListView listView = (ListView) mView.findViewById(R.id.behavior_listview);
        ArrayList<Listviewitem> data = new ArrayList<>();
        Listviewitem item1 = new Listviewitem("집 / 때리기 / 관심","최근 기록");
        Listviewitem item2 = new Listviewitem("공원 / 소리 지르기 / 요구","저장된 기록 1");
        Listviewitem item3 = new Listviewitem("집 / 울기 / 관심","저장된 기록 2");

        data.add(item1);
        data.add(item2);
        data.add(item3);

        ListviewAdapter adapter = new ListviewAdapter(getContext(), R.layout.list_behavior, data);
        listView.setAdapter(adapter);

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


        // cardListView
        cardListView = (ListView) v.findViewById(R.id.behavior_card_listview);
        cardData = new ArrayList<>();

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
                            }

                            db.collection("behaviors")
                                    .whereEqualTo("cid", cid)
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                    Behavior item = new Behavior((Date) document.getDate("start_time"), (Date) document.getDate("end_time"), document.get("place").toString(), document.get("categorization").toString(), document.get("current_behavior").toString(), document.get("before_behavior").toString(), document.get("after_behavior").toString(), (HashMap<String, String>) document.get("type"), Integer.parseInt(document.get("intensity").toString()), (HashMap<String, String>) document.get("reason"), document.get("created_at").toString(),  document.get("updated_at").toString(), document.get("uid").toString(), document.get("name").toString(), document.get("cid").toString());
                                                    cardData.add(item);
                                                }

                                                final CardListviewAdapter cardAdapter = new CardListviewAdapter(getContext(), R.layout.list_behavior_card, cardData);
                                                cardListView.setAdapter(cardAdapter);

                                                cardListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                    @Override
                                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                        Intent intent = new Intent(getActivity(), BehaviorDetailActivity.class);
                                                        intent.putExtra("temp", (Behavior) cardAdapter.getItem(position));
                                                        startActivity(intent);
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

            LinearLayout card_interest = convertView.findViewById(R.id.card_interest);
            LinearLayout card_self_stimulation = convertView.findViewById(R.id.card_self_stimulation);
            LinearLayout card_task_evation = convertView.findViewById(R.id.card_task_evation);
            LinearLayout card_demand = convertView.findViewById(R.id.card_demand);
            LinearLayout card_etc = convertView.findViewById(R.id.card_etc);

            TextView txtCategorization = convertView.findViewById(R.id.txt_card_categorization);
            TextView txtTime = convertView.findViewById(R.id.txt_card_time);
            TextView txtNameRelationship = convertView.findViewById(R.id.txt_card_name_relationship);

            txtCategorization.setText(listviewitem.categorization);
            txtTime.setText(listviewitem.start_time.toString() + listviewitem.end_time.toString());
            txtNameRelationship.setText(listviewitem.name+"()");

            card_interest.setVisibility(View.GONE);
            card_self_stimulation.setVisibility(View.GONE);
            card_task_evation.setVisibility(View.GONE);
            card_demand.setVisibility(View.GONE);
            card_etc.setVisibility(View.GONE);

            if(listviewitem.reason.get("관심")!=null) {
                card_interest.setVisibility(View.VISIBLE);
            }
            if(listviewitem.reason.get("감각")!=null) {
                card_self_stimulation.setVisibility(View.VISIBLE);
            }
            if(listviewitem.reason.get("주목")!=null) {
                card_task_evation.setVisibility(View.VISIBLE);
            }
            if(listviewitem.reason.get("불만")!=null) {
                card_demand.setVisibility(View.VISIBLE);
            }
            if(listviewitem.reason.get("거부")!=null){
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

    public class Listviewitem {
        private String main;
        private String sub;
        public String getMain() {return main;}
        public String getSub() {return sub;}
        public Listviewitem(String main,String sub) {
            this.main = main;
            this.sub = sub;
        }
    }

    public class ListviewAdapter extends BaseAdapter {
        private LayoutInflater inflater;
        private ArrayList<Listviewitem> data;
        private int layout;
        public ListviewAdapter(Context context, int layout, ArrayList<Listviewitem> data){
            this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.data = data;
            this.layout = layout;
        }

        @Override
        public int getCount(){return data.size();}

        @Override
        public String getItem(int position){return data.get(position).getMain();}

        @Override
        public long getItemId(int position){return position;}

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            if(convertView == null){
                convertView = inflater.inflate(layout, parent, false);
            }
            Listviewitem listviewitem = data.get(position);
            TextView main = (TextView) convertView.findViewById(R.id.first_txtview);
            main.setText(listviewitem.getMain());
            TextView sub = (TextView)convertView.findViewById(R.id.second_txtview);
            sub.setText(listviewitem.getSub());
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
