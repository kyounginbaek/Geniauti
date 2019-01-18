package com.geniauti.geniauti;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SearchFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private LinearLayout recentSearch;
    private ListView listView_bookmark;
    private ListView listView_all;
    public static SearchFragment.ListviewAdapter adapterBookmark;
    private static SearchFragment.ListviewAdapter adapterAll;
    private View searchLine;

    public static ArrayList<Cases> bookmark;
    private static ArrayList<Cases> allCases;
    private static ArrayList<Cases> tmpCases;

    private OnFragmentInteractionListener mListener;

    public SearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
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
        View v = inflater.inflate(R.layout.fragment_search, container, false);

        setupUI(v);

        recentSearch = (LinearLayout) v.findViewById(R.id.recent_search);
        listView_bookmark = (ListView) v.findViewById(R.id.search_listview_bookmark);
        listView_all = (ListView) v.findViewById(R.id.search_listview_all);

        searchLine = (View) v.findViewById(R.id.search_line);

        bookmark = new ArrayList<>();
        allCases = new ArrayList<>();
        tmpCases = new ArrayList<>();

        // Get User's Bookmark Data
        MainActivity.db.collection("users").document(MainActivity.user.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if(document.exists()) {
                                HashMap<String, HashMap<Object, Object>> result = (HashMap<String, HashMap<Object, Object>>) document.get("cases");

                                if(result != null) {
                                    for(Map.Entry me : result.entrySet()) {
                                        Object i = me.getKey();
                                        Cases item = new Cases(result.get(i).get("case_title").toString(), result.get(i).get("case_backgroundInfo").toString(), result.get(i).get("case_behavior").toString(),
                                                (List<HashMap<String, String>>) result.get(i).get("case_cause"), (List<HashMap<String, String>>) result.get(i).get("case_solution"),
                                                result.get(i).get("case_effect").toString(), (HashMap<String, String>) result.get(i).get("case_tags"), (String) me.getKey());
                                        bookmark.add(item);
                                    }
                                } else {
                                    searchLine.setVisibility(View.GONE);
                                }

                            } else {
                                searchLine.setVisibility(View.GONE);
                            }
                        } else {
//                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        adapterBookmark = new SearchFragment.ListviewAdapter(getContext(), R.layout.list_search, bookmark, "bookmark");
        listView_bookmark.setAdapter(adapterBookmark);
        listView_bookmark.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), CaseDetailActivity.class);
                intent.putExtra("temp", (Cases) adapterBookmark.getItem(position));
                startActivity(intent);
            }
        });

        // Get All Cases Data
        MainActivity.db.collection("cases")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Cases item = new Cases(document.get("case_title").toString(), document.get("case_backgroundInfo").toString(), document.get("case_behavior").toString(),
                                        (List<HashMap<String, String>>) document.get("case_cause"), (List<HashMap<String, String>>) document.get("case_solution"), document.get("case_effect").toString(),
                                        (HashMap<String, String>) document.get("case_tags"), document.getId());
                                allCases.add(item);
                            }
                            tmpCases.addAll(allCases);
                        } else {
//                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        adapterAll = new SearchFragment.ListviewAdapter(getContext(), R.layout.list_search, allCases, "all");
        listView_all.setAdapter(adapterAll);
        listView_all.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                MainActivity.db.collection("users").document(MainActivity.user.getUid().toString())
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if(document.exists()) {
                                        HashMap<String, HashMap<String, Object>> result = (HashMap<String, HashMap<String, Object>>) document.get("cases");
                                        boolean bookmark_check = false;

                                        final Cases tmp = (Cases) adapterAll.getItem(position);
                                        // 만약 이미 검색한 기록이 있으면, 바로 이동
                                        // 만약 검색한 기록이 없으면, 새로 추가
                                        if(result != null) {
                                            for(Map.Entry me : result.entrySet()) {
                                                if(me.getKey().toString().equals(tmp.case_id)) {
                                                    bookmark_check = true;
                                                }
                                            }
                                        }

                                        if(!bookmark_check) {
                                            Object docData = tmp.firebase_input_data();

                                            MainActivity.db.collection("users").document(MainActivity.user.getUid())
                                                    .update("cases."+tmp.case_id, docData)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
//                                                           mProgressView.setVisibility(View.GONE);

                                                            bookmark.add(0, tmp);
                                                            adapterBookmark.notifyDataSetChanged();

                                                            Intent intent = new Intent(getActivity(), CaseDetailActivity.class);
                                                            intent.putExtra("temp", tmp);
                                                            startActivity(intent);
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
//                                                          Log.w(TAG, "Error writing document", e);
                                                        }
                                                    });
                                        } else {
                                            Intent intent = new Intent(getActivity(), CaseDetailActivity.class);
                                            intent.putExtra("temp", (Cases) adapterAll.getItem(position));
                                            startActivity(intent);
                                        }
                                    } else {

                                    }
                                } else {
//                            Log.d(TAG, "Error getting documents: ", task.getException());
                                }
                            }
                        });
            }
        });

        return v;
    }

    public void listviewAllShow() {
        if(listView_all.isShown()) {

        } else {
            listView_all.setVisibility(View.VISIBLE);
            recentSearch.setVisibility(View.GONE);
            listView_bookmark.setVisibility(View.GONE);
        }
    }

    public void listviewBookmarkShow() {
        listView_all.setVisibility(View.GONE);
        recentSearch.setVisibility(View.VISIBLE);
        listView_bookmark.setVisibility(View.VISIBLE);
    }

    // 검색을 수행하는 메소드
    public static void getFilter(String charText) {

        // 문자 입력시마다 리스트를 지우고 새로 뿌려준다.
        allCases.clear();

        // 문자 입력이 없을때는 모든 데이터를 보여준다.
        if (charText.length() == 0) {
            allCases.addAll(tmpCases);
        }
        // 문자 입력을 할때..
        else
        {
            // 리스트의 모든 데이터를 검색한다.
            for(int i = 0;i < tmpCases.size(); i++)
            {
                // arraylist의 모든 데이터에 입력받은 단어(charText)가 포함되어 있으면 true를 반환한다.
                if (tmpCases.get(i).case_title.toLowerCase().contains(charText))
                {
                    // 검색된 데이터를 리스트에 추가한다.
                    allCases.add(tmpCases.get(i));
                }
            }
        }
        // 리스트 데이터가 변경되었으므로 아답터를 갱신하여 검색된 데이터를 화면에 보여준다.
        adapterAll.notifyDataSetChanged();
    }

    public class ListviewAdapter extends BaseAdapter {
        private LayoutInflater inflater;
        private ArrayList<Cases> data;
        private int layout;
        private String kind;

        public ListviewAdapter(Context context, int layout, ArrayList<Cases> data, String kind){
            this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.data = data;
            this.layout = layout;
            this.kind = kind;
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

            Cases listviewitem = data.get(position);
            TextView case_title = (TextView) convertView.findViewById(R.id.search_title);
            case_title.setText(listviewitem.case_title);

            TextView case_tag_reason = (TextView) convertView.findViewById(R.id.search_reason);
            TextView case_tag_type = (TextView) convertView.findViewById(R.id.search_type);

            String tmp_reason = "행동 원인 > ";
            String tmp_type = "행동 종류 > ";

            if(listviewitem.case_tags.get("taskEvation")!=null) {
                tmp_reason = tmp_reason + "과제 회피 / ";
            }
            if(listviewitem.case_tags.get("selfStimulation")!=null) {
                tmp_reason = tmp_reason + "자기 자극 / ";
            }
            if(listviewitem.case_tags.get("interest")!=null) {
                tmp_reason = tmp_reason + "관심 / ";
            }
            if(listviewitem.case_tags.get("demand")!=null) {
                tmp_reason = tmp_reason + "요구 / ";
            }

            case_tag_reason.setText(tmp_reason.substring(0, tmp_reason.length()-2));

            if(listviewitem.case_tags.get("harm")!=null) {
                tmp_type = tmp_type + "타해 / ";
            }
            if(listviewitem.case_tags.get("selfHArm")!=null) {
                tmp_type = tmp_type + "자해 / ";
            }
            if(listviewitem.case_tags.get("destruction")!=null) {
                tmp_type = tmp_type + "파괴 / ";
            }
            if(listviewitem.case_tags.get("leave")!=null) {
                tmp_type = tmp_type + "이탈 / ";
            }
            if(listviewitem.case_tags.get("sexual")!=null) {
                tmp_type = tmp_type + "성적 / ";
            }

            case_tag_type.setText(tmp_type.substring(0,tmp_type.length()-2));

            LinearLayout bookmark_delete = (LinearLayout) convertView.findViewById(R.id.bookmark_delete);

            if(kind == "bookmark") {
                bookmark_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder alt_bld = new AlertDialog.Builder(getActivity());
                        alt_bld.setMessage("즐겨찾기를 삭제하시겠습니까?").setCancelable(
                                false).setPositiveButton("확인",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // Action for 'Yes' Button
                                        // 즐겨찾기 삭제
                                        Object delete_data = FieldValue.delete();
                                        Cases tmp = (Cases) adapterBookmark.getItem(position);

                                        MainActivity.db.collection("users").document(MainActivity.user.getUid())
                                                .update("cases."+tmp.case_id, delete_data)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
//                                                           mProgressView.setVisibility(View.GONE);
                                                        bookmark.remove(position);
                                                        adapterBookmark.notifyDataSetChanged();
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
//                                                          Log.w(TAG, "Error writing document", e);
                                                    }
                                                });
                                    }
                                }).setNegativeButton("취소",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // Action for 'NO' Button
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alert = alt_bld.create();
                        // Title for AlertDialog
//                        alert.setTitle("Title");
                        // Icon for AlertDialog
                        alert.show();
                    }
                });
            } else {
                bookmark_delete.setVisibility(convertView.GONE);
            }

            return convertView;
        }
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

    public void setupUI(View view) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(getActivity());
                    return false;
                }
            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
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
