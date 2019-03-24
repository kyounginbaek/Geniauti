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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.geniauti.geniauti.SearchFragment.adapterBookmark;
import static com.geniauti.geniauti.SearchFragment.bookmark;

public class DicFirstFragment extends Fragment {

    private static ExpandableListView expandableListView;
    private static ExpandableListAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_first, container, false);

        expandableListView = (ExpandableListView) view.findViewById(R.id.simple_expandable_listview1);

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
        final List<String> child_taskEvation = new ArrayList<String>();
        final List<String> child_selfStimulation = new ArrayList<String>();
        final List<String> child_interest = new ArrayList<String>();
        final List<String> child_demand = new ArrayList<String>();

        // Hash map for both header and child
        final HashMap<String, List<String>> hashMap = new HashMap<String, List<String>>();

        header.add("관심 사례");
        header.add("자기 자극 사례");
        header.add("과제 회피 사례");
        header.add("요구 사례");

        // Hash map for both header and child
        final List<List<Cases>> c_Array = new ArrayList<List<Cases>>();

        final List<Cases> c_taskEvation = new ArrayList<Cases>();
        final List<Cases> c_selfStimulation = new ArrayList<Cases>();
        final List<Cases> c_interest = new ArrayList<Cases>();
        final List<Cases> c_demand = new ArrayList<Cases>();

        MainActivity.db.collection("cases")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                HashMap<String,String> case_tags = new HashMap<String,String>();
                                case_tags = (HashMap<String,String>) document.get("tags_reason");

                                Cases c = new Cases(document.get("title").toString(), document.get("backgroundInfo").toString(), document.get("behavior").toString(),
                                        (List<HashMap<String, Object>>) document.get("cause"), (List<HashMap<String, Object>>) document.get("solution"), document.get("effect").toString(),
                                        (HashMap<String, Object>) document.get("tags_reason"), (HashMap<String, Object>) document.get("tags_type"), document.getId());

                                if(case_tags.get("attention")!=null) {
                                    c_interest.add(c);
                                }
                                if(case_tags.get("self-stimulatory behaviour")!=null) {
                                    c_selfStimulation.add(c);
                                }
                                if(case_tags.get("escape")!=null) {
                                    c_taskEvation.add(c);
                                }
                                if(case_tags.get("tangibles")!=null) {
                                    c_demand.add(c);
                                }
//                                (TAG, document.getId() + " => " + document.getData());
                            }

                            // Adding header and childs to hash map
                            c_Array.add(c_interest);
                            c_Array.add(c_selfStimulation);
                            c_Array.add(c_taskEvation);
                            c_Array.add(c_demand);

                            adapter = new ExpandableListAdapter(getActivity(), header, hashMap, c_Array);

                            // Setting adpater over expandablelistview
                            expandableListView.setAdapter(adapter);
                        } else {
//                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        // Adding header and childs to hash map
        hashMap.put(header.get(0), child_taskEvation);
        hashMap.put(header.get(1), child_selfStimulation);
        hashMap.put(header.get(2), child_interest);
        hashMap.put(header.get(3), child_demand);
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
