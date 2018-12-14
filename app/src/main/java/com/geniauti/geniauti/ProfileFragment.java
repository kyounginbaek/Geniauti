package com.geniauti.geniauti;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


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

    private ListView parentListView;
    private ListView bookmarkListView;
    private ArrayList<Parent> parentData;
    private ArrayList<Bookmark> bookmarkData;

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

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
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

    public class Bookmark {
        public Bookmark(String main,String sub) {
            this.main = main;
            this.sub = sub;
        }
        private String main;
        private String sub;
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
