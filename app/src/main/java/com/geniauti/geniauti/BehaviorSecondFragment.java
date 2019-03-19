package com.geniauti.geniauti;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BehaviorSecondFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BehaviorSecondFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BehaviorSecondFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private View v;
    public static GridListAdapter adapter;
    private ArrayList<String> arrayList;
    public ListView listView;

    private EditText txtLocation;
    private LinearLayout locationCancel;
    private LinearLayout locationAdd;
    public String purpose = "";

    private OnFragmentInteractionListener mListener;

    public BehaviorSecondFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BehaviorSecondFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BehaviorSecondFragment newInstance(String param1, String param2) {
        BehaviorSecondFragment fragment = new BehaviorSecondFragment();
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

        if (v != null) {
            if ((ViewGroup) v.getParent() != null)
                ((ViewGroup) v.getParent()).removeView(v);
            return v;
        }

        v = inflater.inflate(R.layout.fragment_behavior_second, container, false);

        listView = (ListView) v.findViewById(R.id.radio_listview_second);
        arrayList = new ArrayList<>();

        arrayList.add("집");
        arrayList.add("마트");
        arrayList.add("식당");
        arrayList.add("학교");

        MainActivity.db.collection("childs").document(MainActivity.cid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();

                            if(document.exists()) {
                                HashMap<String, Object> preset = (HashMap<String, Object>)  document.get("preset");
                                if(preset != null) {
                                    HashMap<String, Boolean> place = (HashMap<String, Boolean>)  preset.get("place_preset");
                                    if(place != null){
                                        Iterator it = place.entrySet().iterator();
                                        while (it.hasNext()) {
                                            Map.Entry pair = (Map.Entry)it.next();
                                            arrayList.add(pair.getKey().toString());
                                        }
                                    }
                                }
                            }

                            if(purpose.equals("tmpBookmark")) {
                                if(!arrayList.contains(BehaviorActivity.tmpBookmark.place)) {
                                    arrayList.add(BehaviorActivity.tmpBookmark.place);
                                }
                            }

                            if(purpose.equals("editBehavior")) {
                                if(!arrayList.contains(BehaviorActivity.editBehavior.place)) {
                                    arrayList.add(BehaviorActivity.editBehavior.place);
                                }
                            }

                            if(purpose.equals("editBookmark")) {
                                if(!arrayList.contains(BookmarkActivity.editBookmark.place)) {
                                    arrayList.add(BookmarkActivity.editBookmark.place);
                                }
                            }

                            arrayList.add("장소 추가하기");
                            adapter = new GridListAdapter(getContext(), arrayList);
                            listView.setAdapter(adapter);
                        } else {
//                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        // Inflate the layout for this fragment
        return v;
    }

    public static String getResult() {
        int i = adapter.selectedPosition;

        if(i == -1){
            return "";
        } else {
            return adapter.getItem(i).toString();
        }
    }

    public class GridListAdapter extends BaseAdapter {
        private Context context;
        private ArrayList<String> arrayList;
        private LayoutInflater inflater;
        private int selectedPosition = -1;

        public GridListAdapter(Context context, ArrayList<String> arrayList) {
            this.context = context;
            this.arrayList = arrayList;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public Object getItem(int i) {
            return arrayList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {

            if(i == arrayList.size()-1) {
                view = inflater.inflate(R.layout.list_addlist, viewGroup, false);
            } else {
                view = inflater.inflate(R.layout.list_radio, viewGroup, false);
            }

            // 자주 쓰는 기록을 사용하는 경우
            if(purpose.equals("tmpBookmark") && selectedPosition == -1) {
                if(arrayList.get(i).equals(BehaviorActivity.tmpBookmark.place)) {
                    selectedPosition = i;
                }
            }

            // 행동 기록을 수정하는 경우
            if(purpose.equals("editBehavior") && selectedPosition == -1) {
                if(arrayList.get(i).equals(BehaviorActivity.editBehavior.place)) {
                    selectedPosition = i;
                }
            }

            // 자주 쓰는 기록을 수정하는 경우
            if(purpose.equals("editBookmark") && selectedPosition == -1) {
                if(arrayList.get(i).equals(BookmarkActivity.editBookmark.place)) {
                    selectedPosition = i;
                }
            }

            if (i == arrayList.size()-1) {
                TextView addListText = (TextView) view.findViewById(R.id.add_list_txt);
                LinearLayout addListLayout = (LinearLayout) view.findViewById(R.id.add_list_layout);

                if(addListText != null && addListLayout != null) {
                    addListText.setText("장소 추가하기");
                    addListLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                        itemCheckChanged(v);

                            AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext());
                            View mView = getLayoutInflater().inflate(R.layout.dialog_location_add, null);
                            mBuilder.setView(mView);
                            final AlertDialog dialog = mBuilder.create();
                            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

                            dialog.show();

                            txtLocation = (EditText) mView.findViewById(R.id.txt_location);

                            locationCancel = (LinearLayout) mView.findViewById(R.id.location_cancel);
                            locationCancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog.dismiss();
                                }
                            });

                            locationAdd = (LinearLayout) mView.findViewById(R.id.location_add);
                            locationAdd.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    boolean cancel = false;
                                    String location = txtLocation.getText().toString().trim();

                                    if(arrayList.contains(location)) {
                                        Toast.makeText(getActivity(), "이미 등록되어 있는 장소입니다.", Toast.LENGTH_SHORT).show();
                                        cancel = true;
                                    }

                                    if(location.equals("")) {
                                        Toast.makeText(getActivity(), "빈칸을 입력해주세요.", Toast.LENGTH_SHORT).show();
                                        cancel = true;
                                    }

                                    if(cancel){

                                    } else {

                                        MainActivity.db.collection("childs").document(MainActivity.cid)
                                                .update("preset.place_preset."+txtLocation.getText().toString(), true)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
//                                                           mProgressView.setVisibility(View.GONE);
                                                        adapter.selectedPosition = arrayList.size() - 1;
                                                        arrayList.add(arrayList.size() - 1, txtLocation.getText().toString());
                                                        adapter.notifyDataSetChanged();
                                                        listView.invalidateViews();
                                                        dialog.dismiss();
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
//                                                          Log.w(TAG, "Error writing document", e);
                                                    }
                                                });
                                    }
                                }
                            });
                        }
                    });
                }
            } else {
                RadioButton radioButton = (RadioButton) view.findViewById(R.id.rowRadioButton);
                if(radioButton != null) {
                    //Set the position tag to both radio button and label
                    radioButton.setChecked(i == selectedPosition);
                    radioButton.setText(arrayList.get(i));

                    radioButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            selectedPosition = i;
                            adapter.notifyDataSetChanged();
                            listView.invalidateViews();
                        }
                    });
                }

                if(i > 3) {
                    ImageView radioDelete = (ImageView) view.findViewById(R.id.rowRadioDelete);
                    if(radioDelete != null) {
                        radioDelete.setVisibility(View.VISIBLE);

                        AlertDialog.Builder alt_bld = new AlertDialog.Builder(getActivity());
                        alt_bld.setMessage("장소를 삭제하시겠습니까?").setCancelable(
                                false).setPositiveButton("확인",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // Action for 'Yes' Button
                                        Object delete_data = FieldValue.delete();
                                        MainActivity.db.collection("childs").document(MainActivity.cid)
                                                .update("preset.place_preset."+arrayList.get(i), delete_data)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
//                                                           mProgressView.setVisibility(View.GONE);
                                                        if(adapter.selectedPosition == i){
                                                            adapter.selectedPosition = -1;
                                                        } else if(adapter.selectedPosition > i) {
                                                            adapter.selectedPosition += -1;
                                                        }
                                                        arrayList.remove(i);
                                                        adapter.notifyDataSetChanged();
                                                        listView.invalidateViews();
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
                        final AlertDialog alert = alt_bld.create();

                        radioDelete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                alert.show();
                            }
                        });
                    }
                }
            }

            return view;
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
