package com.geniauti.geniauti;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;


import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    private RelativeLayout childEdit;
    private RelativeLayout profileEdit;
    private LinearLayout parentAdd;
    private RelativeLayout mSignOut;

    private EditText txtEmail;
    private LinearLayout inviteCancel;
    private LinearLayout parentInvite;
    private LinearLayout bookmarkAdd;
    Bitmap bitmap;
    Uri filePath;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private View v;
    private TextView childName;
    private TextView childParent;
    private OnFragmentInteractionListener mListener;
    private FirebaseUser user;

    private ListView parentListView;
    public static ListView bookmarkListView;
    private ArrayList<Parent> parentData;
    public static ArrayList<Bookmark> bookmarkData;
    private ParentListviewAdapter parentAdapter;
    public static BookmarkListviewAdapter bookmarkAdapter;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        inflater.inflate(R.menu.menu_settings, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.menu_settings) {
            Intent intent = new Intent(this.getContext(), SettingsActivity.class);
            this.startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
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
        v = inflater.inflate(R.layout.fragment_profile, container, false);

        Thread mThread = new Thread() {
            @Override
            public void run(){
                try{
                    URL url = new URL("https://cdn4.iconfinder.com/data/icons/evil-icons-user-interface/64/avatar-128.png");

                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setDoInput(true);
                    conn.connect();

                    InputStream is = conn.getInputStream();
                    bitmap = BitmapFactory.decodeStream(is);

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        mThread.start();

        try {
            mThread.join();
//            mProfileImage.setImageBitmap(bitmap);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        childName = (TextView) v.findViewById(R.id.profile_child_name);
        childParent = (TextView) v.findViewById(R.id.txt_child_parent);

        db.collection("childs")
                .whereGreaterThanOrEqualTo("users."+user.getUid()+".name", "")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
//                            Log.w(TAG, "Listen failed.", e);
                            return;
                        }

                        for (QueryDocumentSnapshot doc : value) {
                            childName.setText(doc.getData().get("name").toString());
                            childParent.setText(doc.getData().get("name").toString()+"의 보호자");

                            HashMap<String, Object> users = (HashMap<String, Object>) doc.getData().get("users");
                            Iterator it = users.entrySet().iterator();
                            while(it.hasNext()){
                                Map.Entry pair = (Map.Entry)it.next();
                                HashMap<String, Object> data = (HashMap<String, Object>) pair.getValue();
                                if(!pair.getKey().toString().equals(user.getUid())){
                                    Parent item = new Parent(data.get("name").toString(), data.get("profile_pic").toString(), data.get("relationship").toString(), pair.getKey().toString());
                                    parentData.add(item);
                                }
                            }
                            parentAdapter = new ParentListviewAdapter(getContext(), R.layout.list_parent, parentData);
                            parentListView.setAdapter(parentAdapter);
                            setListViewHeightBasedOnChildren(parentListView);
                        }
                    }
                });

        childEdit = (RelativeLayout) v.findViewById(R.id.child_edit);
        childEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity().getApplication(), ChildEditActivity.class));
            }
        });

        parentListView = (ListView) v.findViewById(R.id.parent_listview);
        parentData = new ArrayList<>();

        bookmarkListView = (ListView) v.findViewById(R.id.bookmark_listview);
        bookmarkData = new ArrayList<>();

        db.collection("users").document(user.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if(document.exists()) {

                                HashMap<String, Object> preset = (HashMap<String, Object>) document.getData().get("preset");
                                if(preset != null) {
                                    List<HashMap<String, Object>> behavior_preset = (List<HashMap<String,Object>>) preset.get("behavior_preset");
                                    for(int i=0; i<behavior_preset.size(); i++) {
                                        Bookmark item = new Bookmark(i, behavior_preset.get(i).get("title").toString(), behavior_preset.get(i).get("place").toString(), behavior_preset.get(i).get("categorization").toString(), (HashMap<String, Object>) behavior_preset.get(i).get("type"), Integer.parseInt(behavior_preset.get(i).get("intensity").toString()), (HashMap<String, Object>) behavior_preset.get(i).get("reason_type"), (HashMap<String, Object>) behavior_preset.get(i).get("reason"));
                                        bookmarkData.add(item);
                                    }

                                    bookmarkAdapter = new BookmarkListviewAdapter(getContext(), R.layout.list_bookmark_profile, bookmarkData);
                                    bookmarkListView.setAdapter(bookmarkAdapter);
                                    setListViewHeightBasedOnChildren(bookmarkListView);
                                }
                            } else {

                            }
                        } else {
//                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        profileEdit = (RelativeLayout) v.findViewById(R.id.profile_edit);
        profileEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity().getApplication(), ProfileEditActivity.class));
            }
        });

        parentAdd = (LinearLayout) v.findViewById(R.id.parent_add);
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext());
        View mView = getLayoutInflater().inflate(R.layout.dialog_parent_invite, null);
        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();

        parentAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
            }
        });

        txtEmail = (EditText) mView.findViewById(R.id.txt_email);

        inviteCancel = (LinearLayout) mView.findViewById(R.id.invite_cancel);
        inviteCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        parentInvite = (LinearLayout) mView.findViewById(R.id.parent_invite);
        parentInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(txtEmail.getText().toString().equals("")){
                    Toast.makeText(getActivity(), "빈칸을 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else {

                }
            }
        });

        bookmarkAdd = (LinearLayout) v.findViewById(R.id.bookmark_add);
        bookmarkAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity().getApplication(), BehaviorActivity.class));
            }
        });

        mSignOut = (RelativeLayout) v.findViewById(R.id.sign_out);
        mSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getActivity().getApplication(), LoginActivity.class));
                getActivity().finish();
            }
        });


//        FirebaseStorage storage = FirebaseStorage.getInstance();
//        StorageReference storageRef = storage.getReferenceFromUrl("gs://geniauti.appspot.com/");
//        final StorageReference childsRef = storageRef.child("users/"+user.getUid());
//
//        mUploadButton = (Button) v.findViewById(R.id.upload);
//        mUploadButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                UploadTask uploadTask = childsRef.putFile(filePath);
//
//                // Register observers to listen for when the download is done or if it fails
//                uploadTask.addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception exception) {
//                        // Handle unsuccessful uploads
//                    }
//                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
//                        docRef.update("image", "")
//                                .addOnSuccessListener(new OnSuccessListener< Void >() {
//                                    @Override
//                                    public void onSuccess(Void aVoid) {
//                                        // Toast.makeText(MainActivity.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
//                                    }
//                                });
//                    }
//                });
//            }
//        });

        return v;
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    public class Parent {
        public Parent(String name, String profile_pic, String relationship, String uid) {
            this.name = name;
            this.profile_pic = profile_pic;
            this.relationship = relationship;
            this.uid = uid;
        }
        private String name;
        private String profile_pic;
        private String relationship;
        private String uid;
    }

    public class ParentListviewAdapter extends BaseAdapter {

        private LayoutInflater inflater;
        private ArrayList<Parent> data;
        private int layout;

        public ParentListviewAdapter(Context context, int layout, ArrayList<Parent> data){
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
        public View getView(final int position, View v, ViewGroup parent){
            if(v == null){
                v = inflater.inflate(R.layout.list_parent, parent, false);
            }

            Parent parentData = data.get(position);

            ImageView parentImage = (ImageView) v.findViewById(R.id.list_parent_image);
            TextView parentName = (TextView) v.findViewById(R.id.list_parent_name);
            TextView parentRelationship = (TextView) v.findViewById(R.id.list_parent_relationship);

            parentName.setText(parentData.name);
            parentRelationship.setText(parentData.relationship);

            return v;
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
                convertView = inflater.inflate(R.layout.list_bookmark_profile, parent, false);
            }

            final Bookmark bookmarkDataTmp = data.get(position);

            TextView bookmarkTitle = (TextView) convertView.findViewById(R.id.list_bookmark_profile_title);
            TextView bookmarkSub = (TextView) convertView.findViewById(R.id.list_bookmark_profile_sub);
            bookmarkTitle.setText(bookmarkDataTmp.title);
            bookmarkSub.setText("저장된 기록" + String.valueOf(position + 1));

            LinearLayout bookmarkLayout = (LinearLayout) convertView.findViewById(R.id.list_bookmark_profile_layout);
            bookmarkLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), BookmarkDetailActivity.class);
                    intent.putExtra("temp", (Bookmark) bookmarkDataTmp);
                    startActivity(intent);
                }
            });

            AlertDialog.Builder alt_bld = new AlertDialog.Builder(getActivity());
            final DocumentReference sfDocRef = db.collection("users").document(user.getUid());
            alt_bld.setMessage("자주 쓰는 기록을 삭제하시겠습니까?").setCancelable(
                    false).setPositiveButton("확인",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // Action for 'Yes' Button
                            db.runTransaction(new Transaction.Function<Void>() {
                                @Override
                                public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                                    DocumentSnapshot snapshot = transaction.get(sfDocRef);
                                    List<Map<String, Object>> behaviorPresetArray = (List<Map<String, Object>>) snapshot.get("preset.behavior_preset");
                                    behaviorPresetArray.remove(position);
                                    transaction.update(sfDocRef, "preset.behavior_preset", behaviorPresetArray);

                                    // Success
                                    return null;
                                }
                            }).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
//                        mProgressView.setVisibility(View.GONE);
                                    bookmarkData.remove(position);
                                    setListViewHeightBasedOnChildren(bookmarkListView);
                                    MainFragment.bookmarkData.remove(position);

                                    bookmarkAdapter.notifyDataSetChanged();
                                    MainFragment.bookmarkAdapter.notifyDataSetChanged();
                                    Toast toast = Toast.makeText(getActivity(), "자주 쓰는 기록이 삭제되었습니다.", Toast.LENGTH_SHORT);
                                    toast.show();

                                }
                            })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

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

            LinearLayout bookmarkDelete = (LinearLayout) convertView.findViewById(R.id.list_bookmark_profile_delete);
            bookmarkDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alert.show();
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
