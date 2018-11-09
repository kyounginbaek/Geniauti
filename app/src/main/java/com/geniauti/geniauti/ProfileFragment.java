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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;


import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
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

    private ImageView mProfileImage;
    private TextView mTextUserName;
    private TextView mTextChildName;
    private TextView mTextRelation;
    private Button mSignOutButton;
    private Button mChildRegisterButton;
    private Button mSettingsButton;
    private Button mUploadButton;
    Bitmap bitmap;
    Uri filePath;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        getActivity().setTitle("프로필");

        mProfileImage = (ImageView) v.findViewById(R.id.profileImage);

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
            mProfileImage.setImageBitmap(bitmap);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        mTextUserName = (TextView) v.findViewById(R.id.username);
        mTextChildName = (TextView) v.findViewById(R.id.child_name);
        mTextRelation = (TextView) v.findViewById(R.id.parent_relation);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            for (UserInfo profile : user.getProviderData()) {
                // Id of the provider (ex: google.com)
                String providerId = profile.getProviderId();

                // UID specific to the provider
                String uid = profile.getUid();

                // Name, email address, and profile photo Url
                String name = profile.getDisplayName();
                mTextUserName.setText("사용자명: "+ name);
                String email = profile.getEmail();
                Uri photoUrl = profile.getPhotoUrl();
            }
            ;
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        final DocumentReference docRef = db.collection("users").document(user.getUid());
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    // Log.w(TAG, "Listen failed.", e);
                    // return;
                }

                if (snapshot != null && snapshot.exists()) {
//                    mTextUserName.setText("사용자명: "+ snapshot.get("name"));
                    if(snapshot.get("childname")=="") {
                        mTextChildName.setText("자녀명을 등록해주세요.");
                        mTextRelation.setText("자녀와의 관계를 등록해주세요.");
                    } else {
                        mTextChildName.setText("자녀명: "+ snapshot.get("childname"));
                        mTextRelation.setText("자녀와의 관계: "+ snapshot.get("parentrelation"));
                    }
                    // Log.d(TAG, "Current data: " + snapshot.getData());
                } else {
                    // Log.d(TAG, "Current data: null");
                }
            }
        });

        mSignOutButton = (Button) v.findViewById(R.id.sign_out);
        mSignOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getActivity().getApplication(), LoginActivity.class));
                getActivity().finish();
            }
        });

        mChildRegisterButton = (Button) v.findViewById(R.id.child_register);
        mChildRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext());
                View mView = getLayoutInflater().inflate(R.layout.dialog_child_register, null);
                final EditText cName = (EditText) mView.findViewById(R.id.child_name);
                final EditText cRelation = (EditText) mView.findViewById(R.id.child_relation);
                Button cRegister = (Button) mView.findViewById(R.id.child_register);

                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();
                cRegister.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(!cName.getText().toString().isEmpty() && !cRelation.getText().toString().isEmpty()){

                            final FirebaseFirestore db = FirebaseFirestore.getInstance();
                            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            final DocumentReference docRef = db.collection("users").document(user.getUid());
                            final String cid = db.collection("users").document().getId();
                            docRef.update("child", cid);
                            docRef.update("childname", cName.getText().toString());
                            docRef.update("parentrelation", cRelation.getText().toString())
                                    .addOnSuccessListener(new OnSuccessListener< Void >() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            // Toast.makeText(MainActivity.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        if (document.exists()) {
                                            // Create a new user with a first and last name
                                            Map<String, Object> child_data = new HashMap<>();
                                            child_data.put("child", cid);
                                            child_data.put("childname", cName.getText().toString());
                                            child_data.put("parent", user.getUid());
                                            child_data.put("parentname", document.get("name"));
                                            child_data.put("parentrelation", cRelation.getText().toString());

                                            // Add a new document with a generated ID
                                            db.collection("child").document(cid)
                                                    .set(child_data)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            // Log.d("DocumentSnapshot added with ID: " + documentReference.getId());
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            // Log.w("Error adding document", e);
                                                        }
                                                    });
                                            // Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                        } else {
                                            // Log.d(TAG, "No such document");
                                        }
                                    } else {
                                        // Log.d(TAG, "get failed with ", task.getException());
                                    }
                                }
                            });

                            Toast.makeText(getActivity().getApplication(),
                                    "자녀가 등록되었습니다.",
                                    Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }else{
                            Toast.makeText(getActivity().getApplication(),
                                    "빈칸을 입력해주세요.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
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

        mSettingsButton = (Button) v.findViewById(R.id.settings);
        mSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainIntent = new Intent(getActivity().getApplication(),SettingsActivity.class);
                startActivity(mainIntent);
            }
        });

        return v;
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
