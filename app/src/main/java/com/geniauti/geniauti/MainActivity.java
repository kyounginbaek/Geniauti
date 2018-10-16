package com.geniauti.geniauti;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    private TextView mTextUserName;
    private TextView mTextChildName;
    private Button mSignOutButton;
    private Button mChildRegisterButton;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:

                    return true;
                case R.id.navigation_dashboard:

                    return true;
                case R.id.navigation_notifications:

                    return true;
                case R.id.navigation_settings:

                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextUserName = (TextView) findViewById(R.id.username);
        mTextChildName = (TextView) findViewById(R.id.child_name);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
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
                    mTextUserName.setText("사용자명: "+ snapshot.get("name"));
                    if(snapshot.get("childname")=="") {
                        mTextChildName.setText("자녀를 등록해주세요.");
                    } else {
                        mTextChildName.setText("자녀명: "+ snapshot.get("childname"));
                    }
                    // Log.d(TAG, "Current data: " + snapshot.getData());
                } else {
                    // Log.d(TAG, "Current data: null");
                }
            }
        });

        mSignOutButton = (Button) findViewById(R.id.sign_out);
        mSignOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            }
        });

        mChildRegisterButton = (Button) findViewById(R.id.child_register);
        mChildRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
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
                            docRef.update("childname", cName.getText().toString())
                                    .addOnSuccessListener(new OnSuccessListener < Void > () {
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

                            Toast.makeText(MainActivity.this,
                                    "자녀가 등록되었습니다.",
                                    Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }else{
                            Toast.makeText(MainActivity.this,
                                    "빈칸을 입력해주세요.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

}
