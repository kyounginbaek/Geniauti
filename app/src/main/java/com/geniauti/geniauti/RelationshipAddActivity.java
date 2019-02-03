package com.geniauti.geniauti;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.geniauti.geniauti.MainActivity.storage;
import static com.geniauti.geniauti.MainActivity.storageRef;

public class RelationshipAddActivity extends AppCompatActivity {

    private String childCode;

    private TextView titleText;
    private TextView childName;
    private ImageView childImage;
    private String cid;

    private FirebaseFirestore db;
    private FirebaseUser user;
    public static FirebaseStorage storage;
    public static StorageReference storageRef;

    private View mProgressView;
    private EditText parentRelationship;
    private Button relationshipAdd;
    private Map<String, Object> userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relationship_add);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        childCode = (String) getIntent().getSerializableExtra("code");

        titleText = findViewById(R.id.relationship_title_txt);
        childName = findViewById(R.id.relationship_child_name);
        childImage = findViewById(R.id.relationship_child_image);

        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        mProgressView = findViewById(R.id.relationship_add_progress);
        mProgressView.setVisibility(View.VISIBLE);
        mProgressView.bringToFront();

        db.collection("childs")
                .whereEqualTo("code", Integer.parseInt(childCode))
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {

                                titleText.setText(document.getData().get("name").toString()+"과의 관계를\n입력해주세요");
                                childName.setText(document.getData().get("name").toString());
                                cid = document.getId();

                                StorageReference pathReference = storageRef.child("childs/"+cid);
                                pathReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        // Got the download URL for 'users/me/profile.png'
                                        Glide.with(getApplication()).load(uri).into(childImage);
                                        mProgressView.setVisibility(View.GONE);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception exception) {
                                        // Handle any errors
                                        mProgressView.setVisibility(View.GONE);
                                    }
                                });

                            }
                        } else {

                        }
                    }
                });

        parentRelationship = findViewById(R.id.relationship_parent_relation);
        relationshipAdd = findViewById(R.id.relationship_add_button);

        relationshipAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                relationshipAdd.setEnabled(false);
                mProgressView.setVisibility(View.VISIBLE);
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                parentRelationship.setError(null);
                boolean cancel = false;
                String relationship = parentRelationship.getText().toString();

                if(TextUtils.isEmpty(relationship)){
                    parentRelationship.setError("아이와의 관계를 입력해주세요.");
                    cancel = true;
                }

                if(cancel) {
                    relationshipAdd.setEnabled(true);
                    mProgressView.setVisibility(View.GONE);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                } else {
                    userData = new HashMap<>();
                    userData.put("name", user.getDisplayName());
                    userData.put("profile_pic", "");
//                    userData.put("childs", nestedData);
                    userData.put("relationship", parentRelationship.getText().toString());

                    final DocumentReference sfDocRef = db.collection("childs").document(cid);
                    db.runTransaction(new Transaction.Function<Void>() {
                        @Override
                        public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                            DocumentSnapshot snapshot = transaction.get(sfDocRef);
                            Map<String, Object> usersMap = (Map<String, Object>) snapshot.get("users");
                            usersMap.put(user.getUid(), userData);
                            transaction.update(sfDocRef, "users", usersMap);

                            // Success
                            return null;
                        }
                    }).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            db.collection("users").document(user.getUid())
                                    .set(userData)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            mProgressView.setVisibility(View.GONE);
                                            Intent intent=new Intent(RelationshipAddActivity.this, MainActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            relationshipAdd.setEnabled(true);
                                            mProgressView.setVisibility(View.GONE);
                                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                            Toast toast = Toast.makeText(RelationshipAddActivity.this, "에러가 발생했습니다. 다시 한번 시도해주세요.", Toast.LENGTH_SHORT);
                                            toast.show();
                                        }
                                    });
                        }
                    })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    relationshipAdd.setEnabled(true);
                                    mProgressView.setVisibility(View.GONE);
                                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                    Toast toast = Toast.makeText(RelationshipAddActivity.this, "에러가 발생했습니다. 다시 한번 시도해주세요.", Toast.LENGTH_SHORT);
                                    toast.show();
                                }
                            });
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(RelationshipAddActivity.this, ChildCodeAddActivity.class));
        finish();
        super.onBackPressed();
    }
}
