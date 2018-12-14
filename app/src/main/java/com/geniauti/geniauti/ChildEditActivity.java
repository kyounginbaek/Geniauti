package com.geniauti.geniauti;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

import static java.lang.Integer.parseInt;

public class ChildEditActivity extends AppCompatActivity {

    FirebaseUser user;
    FirebaseFirestore db;

    String cid;
    EditText mChildName;
    EditText mChildAge;
    EditText mChildRelationship;
    RadioGroup radioGroupAge, radioGroupSex;
    RadioButton radioMonth, radioYear;
    RadioButton radioBoy, radioGirl;
    private View mProgressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_edit);

        // toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.child_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("아이 정보 수정");

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        mChildName = (EditText) findViewById(R.id.edit_child_name);
        mChildAge = (EditText) findViewById(R.id.edit_child_age);
        mChildRelationship = (EditText) findViewById(R.id.edit_child_relationship);

        radioGroupAge = (RadioGroup) findViewById(R.id.edit_radio_child_age);
        radioGroupAge.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                if(checkedId == R.id.edit_radio_btn_month) {
                    mChildAge.setHint("아이 나이(개월)");
                } else {
                    mChildAge.setHint("아이 나이(세)");
                }
            }
        });

        radioMonth = (RadioButton) findViewById(R.id.edit_radio_btn_month);
        radioYear = (RadioButton) findViewById(R.id.edit_radio_btn_year);

        radioGroupSex = (RadioGroup) findViewById(R.id.edit_radio_child_sex);

        radioBoy = (RadioButton) findViewById(R.id.edit_radio_btn_boy);
        radioGirl = (RadioButton) findViewById(R.id.edit_radio_btn_girl);

        user = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();

        mProgressView = findViewById(R.id.child_edit_progress);
        mProgressView.setVisibility(View.VISIBLE);
        db.collection("childs")
                .whereGreaterThanOrEqualTo("users."+user.getUid()+".name", "")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                cid = document.getId();

                                mChildName.setText(document.getData().get("name").toString());
                                if(parseInt(document.getData().get("age").toString()) % 12 == 0) {
                                    radioYear.setChecked(true);
                                    int age = Integer.parseInt(document.getData().get("age").toString()) / 12;
                                    mChildAge.setText(String.valueOf(age));
                                } else {
                                    radioMonth.setChecked(true);
                                    mChildAge.setText(document.getData().get("age").toString());
                                }

                                if(document.getData().get("sex").equals("남자")) {
                                    radioBoy.setChecked(true);
                                } else {
                                    radioGirl.setChecked(true);
                                }

                                HashMap<String, Object> child = (HashMap<String, Object>)  document.getData().get("users");
                                HashMap<String, Object> users = (HashMap<String, Object>)  child.get(user.getUid());

                                mChildRelationship.setText(users.get("relationship").toString());
                                mProgressView.setVisibility(View.GONE);
                            }
                        } else {
//                                Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        Button childEditButton = (Button) findViewById(R.id.child_edit_button);
        childEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProgressView.setVisibility(View.VISIBLE);

                // Reset errors.
                mChildName.setError(null);
                mChildAge.setError(null);
                mChildRelationship.setError(null);

                boolean cancel = false;
                View focusView = null;

                if(TextUtils.isEmpty(mChildName.getText().toString())) {
                    mChildName.setError("아이 이름을 입력해주세요.");
                    focusView = mChildName;
                    cancel = true;
                }

                if(TextUtils.isEmpty(mChildAge.getText().toString())) {
                    mChildAge.setError("아이 나이를 입력해주세요.");
                    focusView = mChildAge;
                    cancel = true;
                }

                if(TextUtils.isEmpty(mChildRelationship.getText().toString())) {
                    mChildRelationship.setError("아이와의 관계를 입력해주세요.");
                    focusView = mChildRelationship;
                    cancel = true;
                }

                if(cancel){

                } else {
                    int selectedAge = radioGroupAge.getCheckedRadioButtonId();
                    // find the radiobutton by returned id
                    RadioButton radioBtnAge = (RadioButton) findViewById(selectedAge);

                    int childAge = parseInt(mChildAge.getText().toString());

                    if(radioBtnAge.getText().equals("세")) {
                        childAge = parseInt(mChildAge.getText().toString()) * 12;
                    }

                    int selectedSex = radioGroupSex.getCheckedRadioButtonId();
                    // find the radiobutton by returned id
                    RadioButton radioBtnSex = (RadioButton) findViewById(selectedSex);

                    Map<String, Object> nestedNestedData = new HashMap<>();
                    nestedNestedData.put("name", user.getDisplayName());
                    nestedNestedData.put("profile_pic", "");
                    nestedNestedData.put("relationship", mChildRelationship.getText().toString());

                    Map<String, Object> nestedData = new HashMap<>();
                    nestedData.put(user.getUid(), nestedNestedData);

                    Map<String, Object> docData = new HashMap<>();
                    docData.put("name", mChildName.getText().toString());
                    docData.put("profile_pic", "");
                    docData.put("age", childAge);
                    docData.put("sex", radioBtnSex.getText().toString().substring(0,2));
                    docData.put("users", nestedData);

                    db.collection("childs").document(cid)
                            .update(docData)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    Map<String, Object> userData = new HashMap<>();
                                    userData.put("name", user.getDisplayName());

                                    db.collection("users").document(user.getUid())
                                            .set(userData)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    mProgressView.setVisibility(View.GONE);
                                                    finish();
                                                    Toast toast = Toast.makeText(ChildEditActivity.this, "아이 정보가 수정되었습니다.", Toast.LENGTH_SHORT);
                                                    toast.show();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                }
                                            });
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
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
