package com.geniauti.geniauti;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ChildAddActivity extends AppCompatActivity {

    private View mProgressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_add);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        ImageView childImaage = (ImageView) findViewById(R.id.child_image);
        childImaage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        mProgressView = findViewById(R.id.child_add_progress);

        final TextView mChildName = (TextView) findViewById(R.id.txt_child_name);
        final TextView mChildAge= (TextView) findViewById(R.id.txt_child_age);
        final RadioGroup radioGroup = (RadioGroup) findViewById(R.id.child_sex);
        final TextView mChildRelation = (TextView) findViewById(R.id.txt_child_relation);

        Button mChildAddButton = (Button) findViewById(R.id.child_add_button);
        mChildAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProgressView.setVisibility(View.VISIBLE);

                final FirebaseFirestore db = FirebaseFirestore.getInstance();
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                int selectedId = radioGroup.getCheckedRadioButtonId();
                // find the radiobutton by returned id
                RadioButton radioButton = (RadioButton) findViewById(selectedId);

                Map<String, Object> nestedNestedData = new HashMap<>();
                nestedNestedData.put("name", user.getDisplayName());
                nestedNestedData.put("profile_pic", "");
                nestedNestedData.put("relationship", mChildRelation.getText().toString());

                Map<String, Object> nestedData = new HashMap<>();
                nestedData.put(user.getUid().toString(), nestedNestedData);

                Map<String, Object> docData = new HashMap<>();
                docData.put("name", mChildName.getText().toString());
                docData.put("profile_pic", "");
                docData.put("age", mChildAge.getText().toString());
                docData.put("sex", radioButton.getText());
                docData.put("users", nestedData);

                db.collection("childs").document()
                        .set(docData)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                mProgressView.setVisibility(View.GONE);
                                Intent intent=new Intent(ChildAddActivity.this,MainActivity.class);
                                startActivity(intent);
                                finish();
//                                Log.d(TAG, "DocumentSnapshot successfully written!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
//                                Log.w(TAG, "Error writing document", e);
                            }
                        });
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ChildAddActivity.this, ChildRegisterActivity.class));
        finish();
        super.onBackPressed();
    }
}
