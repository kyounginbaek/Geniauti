package com.geniauti.geniauti;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
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

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

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

    private TextView txtChildCode;
    private LinearLayout inviteCancel;
    private LinearLayout parentInvite;
    private LinearLayout bookmarkAdd;

    private TextView dialogChildName;
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
    private ImageView childImage;
    private TextView childNameView;
    public static String childName;
    private TextView childParentView;
    private OnFragmentInteractionListener mListener;

    private ListView parentListView;
    public static ListView bookmarkListView;
    private ArrayList<Parent> parentData;
    public static ArrayList<Bookmark> bookmarkData;
    private ParentListviewAdapter parentAdapter;
    public static BookmarkListviewAdapter bookmarkAdapter;
    private StorageReference pathReference;

    private String childCode;
    private Uri childImageURI;

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

        childNameView = (TextView) v.findViewById(R.id.profile_child_name);
        childParentView = (TextView) v.findViewById(R.id.txt_child_parent);
        childImage = (ImageView) v.findViewById(R.id.profile_child_image);

        pathReference = MainActivity.storageRef.child("childs/"+MainActivity.cid);

        MainActivity.db.collection("childs")
                .whereGreaterThanOrEqualTo("users."+MainActivity.user.getUid()+".name", "")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
//                            Log.w(TAG, "Listen failed.", e);
                            return;
                        }

                        for (QueryDocumentSnapshot doc : value) {

                            HashMap<String, Object> users = (HashMap<String, Object>) doc.getData().get("users");
                            Iterator it = users.entrySet().iterator();
                            parentData = new ArrayList<>();

                            while(it.hasNext()){
                                Map.Entry pair = (Map.Entry)it.next();
                                HashMap<String, Object> data = (HashMap<String, Object>) pair.getValue();
//                                if(!pair.getKey().toString().equals(MainActivity.user.getUid())){
//                                    Parent item = new Parent(data.get("name").toString(), data.get("profile_pic").toString(), data.get("relationship").toString(), pair.getKey().toString());
//                                    parentData.add(item);
//                                }

                                Parent item = new Parent(data.get("name").toString(), data.get("profile_pic").toString(), data.get("relationship").toString(), pair.getKey().toString());

                                if(pair.getKey().toString().equals(MainActivity.user.getUid())) {
                                    parentData.add(0, item);
                                } else {
                                    parentData.add(item);
                                }
                            }

                            if(getActivity()!=null) {
                                parentAdapter = new ParentListviewAdapter(getContext(), R.layout.list_parent, parentData);
                                parentListView.setAdapter(parentAdapter);
                                setListViewHeightBasedOnChildren(parentListView);

                                pathReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        // Got the download URL for 'users/me/profile.png'
                                        childImageURI = uri;
                                        Glide.with(ProfileFragment.this).load(childImageURI).into(childImage);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception exception) {
                                        // Handle any errors
                                    }
                                });
                            }

                            childName = doc.getData().get("name").toString();
                            childNameView.setText(childName);
                            childParentView.setText(childName+"의 보호자");
                        }
                    }
                });

        MainActivity.db.collection("childs")
                .whereGreaterThanOrEqualTo("users."+MainActivity.user.getUid()+".name", "")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                final String childName = childCode = document.getData().get("name").toString();
                                childCode = document.getData().get("code").toString();
                                txtChildCode.setText(childCodeNumber(childCode));

                                parentInvite.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        String shareBody = childName + " 아이의 초대 코드는 " + childCodeNumber(childCode) + " 입니다.";
                                        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                                        sharingIntent.setType("text/plain");
                                        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "[지니어티 알림]");
                                        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                                        startActivity(Intent.createChooser(sharingIntent, "보호자 초대 코드 공유하기"));
                                    }
                                });
                            }
                        } else {

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
        bookmarkListView = (ListView) v.findViewById(R.id.bookmark_listview);

        MainActivity.db.collection("users").document(MainActivity.user.getUid())
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot snapshot,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
//                            Log.w(TAG, "Listen failed.", e);
                            return;
                        }

                        if (snapshot != null && snapshot.exists()) {
//                            System.out.println("Current data: " + snapshot.getData());
                            bookmarkData = new ArrayList<>();
                            HashMap<String, Object> preset = (HashMap<String, Object>) snapshot.getData().get("preset");

                            if(preset != null) {
                                List<HashMap<String, Object>> behavior_preset = (List<HashMap<String,Object>>) preset.get("behavior_preset");
                                for(int i=0; i<behavior_preset.size(); i++) {
                                    Bookmark item = new Bookmark(i, behavior_preset.get(i).get("title").toString(), behavior_preset.get(i).get("place").toString(),
                                            behavior_preset.get(i).get("categorization").toString(), (HashMap<String, Object>) behavior_preset.get(i).get("type"),
                                            Integer.parseInt(behavior_preset.get(i).get("intensity").toString()), (HashMap<String, Object>) behavior_preset.get(i).get("reason_type"),
                                            (HashMap<String, Object>) behavior_preset.get(i).get("reason"));
                                    bookmarkData.add(item);
                                }

                                if(getActivity()!=null) {
                                    bookmarkAdapter = new BookmarkListviewAdapter(getContext(), R.layout.list_bookmark_profile, bookmarkData);
                                    bookmarkListView.setAdapter(bookmarkAdapter);
                                    setListViewHeightBasedOnChildren(bookmarkListView);
                                }
                            }
                        } else {
//                            System.out.print("Current data: null");
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
        dialogChildName = (TextView) mView.findViewById(R.id.parent_invite_childname);
        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();

        parentAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
            }
        });

        txtChildCode = (TextView) mView.findViewById(R.id.txt_child_code);

        inviteCancel = (LinearLayout) mView.findViewById(R.id.invite_cancel);
        inviteCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        parentInvite = (LinearLayout) mView.findViewById(R.id.parent_invite);

        bookmarkAdd = (LinearLayout) v.findViewById(R.id.bookmark_add);
        bookmarkAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity().getApplication(), BookmarkActivity.class));
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

    private String childCodeNumber(String number) {

        switch(number.length()) {
            case 1:
                return "0000"+number;
            case 2:
                return "000"+number;
            case 3:
                return "00"+number;
            case 4:
                return "0"+number;
            case 5:
                return number;
            default:
                return number;
        }
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

            final ImageView parentImage = (ImageView) v.findViewById(R.id.list_parent_image);
            StorageReference pathReference = MainActivity.storageRef.child("users/"+parentData.uid);
            pathReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    // Got the download URL for 'users/me/profile.png'
                    Glide.with(getContext()).load(uri).into(parentImage);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                }
            });

            TextView parentName = (TextView) v.findViewById(R.id.list_parent_name);
            TextView parentRelationship = (TextView) v.findViewById(R.id.list_parent_relationship);
            TextView parentSelf = (TextView) v.findViewById(R.id.list_parent_self);

            if(position == 0) {
                parentSelf.setVisibility(View.VISIBLE);
            }

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
            final DocumentReference sfDocRef = MainActivity.db.collection("users").document(MainActivity.user.getUid());
            alt_bld.setMessage("자주 쓰는 기록을 삭제하시겠습니까?").setCancelable(
                    false).setPositiveButton("확인",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // Action for 'Yes' Button
                            MainActivity.db.runTransaction(new Transaction.Function<Void>() {
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
//                                    bookmarkData.remove(position);
//                                    setListViewHeightBasedOnChildren(bookmarkListView);
//                                    MainFragment.bookmarkData.remove(position);

//                                    bookmarkAdapter.notifyDataSetChanged();
//                                    MainFragment.bookmarkAdapter.notifyDataSetChanged();
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
