package com.geniauti.geniauti;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.geniauti.geniauti.SearchFragment.adapterBookmark;
import static com.geniauti.geniauti.SearchFragment.bookmark;

public class DicSecondFragment extends Fragment {

    private static ExpandableListView expandableListView;
    private static ExpandableListAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_second, container, false);

        expandableListView = (ExpandableListView) view.findViewById(R.id.simple_expandable_listview2);

        // Setting group indicator null for custom indicator
        expandableListView.setGroupIndicator(null);

        setItems();
        setListener();

        return view;
    }

    // Setting headers and childs to expandable listview
    void setItems() {

        // Array list for header
        final ArrayList<String> header = new ArrayList<String>();

        // Array list for child items
        final List<String> child_harm = new ArrayList<String>();
        final List<String> child_selfHarm = new ArrayList<String>();
        final List<String> child_destruction = new ArrayList<String>();
        final List<String> child_leave = new ArrayList<String>();
        final List<String> child_sexual = new ArrayList<String>();

        // Hash map for both header and child
        final HashMap<String, List<String>> hashMap = new HashMap<String, List<String>>();

        header.add("자해 사례");
        header.add("타해 사례");
        header.add("파괴 사례");
        header.add("이탈 사례");
        header.add("성적 사례");
        header.add("기타 사례");

        // Hash map for both header and child
        final List<List<Cases>> c_Array = new ArrayList<List<Cases>>();

        final List<Cases> c_harm = new ArrayList<Cases>();
        final List<Cases> c_selfHarm = new ArrayList<Cases>();
        final List<Cases> c_destruction = new ArrayList<Cases>();
        final List<Cases> c_leave = new ArrayList<Cases>();
        final List<Cases> c_sexual = new ArrayList<Cases>();
        final List<Cases> c_etc = new ArrayList<Cases>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("cases")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                HashMap<String,String> case_tags = new HashMap<String,String>();
                                case_tags = (HashMap<String,String>) document.get("tags_type");

                                Cases c = new Cases(document.get("title").toString(), document.get("backgroundInfo").toString(), document.get("behavior").toString(),
                                        (List<HashMap<String, Object>>) document.get("cause"), (List<HashMap<String, Object>>) document.get("solution"), document.get("effect").toString(),
                                        (HashMap<String, Object>) document.get("tags_reason"), (HashMap<String, Object>) document.get("tags_type"), document.getId());

                                if(case_tags.get("self-injury")!=null) {
                                    c_selfHarm.add(c);
                                }
                                if(case_tags.get("aggression")!=null) {
                                    c_harm.add(c);
                                }
                                if(case_tags.get("disruption")!=null) {
                                    c_destruction.add(c);
                                }
                                if(case_tags.get("elopement")!=null) {
                                    c_leave.add(c);
                                }
                                if(case_tags.get("sexual behaviors")!=null) {
                                    c_sexual.add(c);
                                }
                                if(case_tags.get("other behaviors")!=null) {
                                    c_etc.add(c);
                                }
//                                (TAG, document.getId() + " => " + document.getData());

                                // Adding header and childs to hash map
                                c_Array.add(c_selfHarm);
                                c_Array.add(c_harm);
                                c_Array.add(c_destruction);
                                c_Array.add(c_leave);
                                c_Array.add(c_sexual);
                                c_Array.add(c_etc);

                                adapter = new ExpandableListAdapter(getActivity(), header, hashMap, c_Array);

                                // Setting adpater over expandablelistview
                                expandableListView.setAdapter(adapter);
                            }
                        } else {
//                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        // Adding header and childs to hash map
        hashMap.put(header.get(0), child_harm);
        hashMap.put(header.get(1), child_selfHarm);
        hashMap.put(header.get(2), child_destruction);
        hashMap.put(header.get(3), child_leave);
        hashMap.put(header.get(4), child_sexual);

    }

    // Setting different listeners to expandablelistview
    void setListener() {

        // This listener will show toast on group click
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView listview, View view,
                                        int group_pos, long id) {

//                Toast.makeText(MainActivity.this,
//                        "You clicked : " + adapter.getGroup(group_pos),
//                        Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        // This listener will expand one group at one time
        // You can remove this listener for expanding all groups
//        expandableListView
//                .setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
//
//                    // Default position
//                    int previousGroup = -1;
//
//                    @Override
//                    public void onGroupExpand(int groupPosition) {
//                        if (groupPosition != previousGroup)
//
//                            // Collapse the expanded group
//                            expandableListView.collapseGroup(previousGroup);
//                        previousGroup = groupPosition;
//                    }
//
//                });

        // This listener will show toast on child click
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView listview, View view,
                                        final int groupPos, final int childPos, long id) {

                Cases tmp = (Cases) adapter.getChild(groupPos, childPos);
                bookmarkCheck(tmp);

                Intent intent = new Intent(getActivity(), CaseDetailActivity.class);
                intent.putExtra("temp", tmp);
                startActivity(intent);

                return false;
            }
        });
    }

    public void bookmarkCheck(final Cases tmp) {

        final Handler mHandler = new Handler();

        final Thread t = new Thread(new Runnable(){
            @Override
            public void run() {
                mHandler.post(new Runnable(){
                    @Override public void run() {
                        MainActivity.db.collection("users").document(MainActivity.user.getUid())
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            DocumentSnapshot document = task.getResult();
                                            if(document.exists()) {
                                                HashMap<String, HashMap<String, Object>> result = (HashMap<String, HashMap<String, Object>>) document.get("cases");
                                                boolean bookmark_check = false;

                                                // 만약 이미 검색한 기록이 있으면, 바로 이동
                                                // 만약 검색한 기록이 없으면, 새로 추가
                                                if(result != null) {
                                                    for(Map.Entry me : result.entrySet()) {
                                                        if(me.getKey().toString().equals(tmp.id)) {
                                                            bookmark_check = true;
                                                        }
                                                    }
                                                }

                                                if(!bookmark_check) {
                                                    Object docData = tmp.firebase_input_data();

                                                    MainActivity.db.collection("users").document(MainActivity.user.getUid())
                                                            .update("cases."+tmp.id, docData)
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                    bookmark.add(0, tmp);
                                                                    adapterBookmark.notifyDataSetChanged();

                                                                }
                                                            })
                                                            .addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {

                                                                }
                                                            });
                                                }

                                            } else {

                                            }
                                        } else {

                                        }
                                    }
                                });
                    }
                });
            }
        });
        t.start();
    }

}
