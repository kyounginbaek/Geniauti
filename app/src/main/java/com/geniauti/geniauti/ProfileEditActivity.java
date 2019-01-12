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
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileEditActivity extends AppCompatActivity {

    private EditText profileName;
    private EditText profileEmail;
    private RelativeLayout passwordEdit;
    private Button btnProfileEdit;
    private String newName, newEmail;

    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private View mProgressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        // toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.profile_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("내 정보 수정");

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        profileName = (EditText) findViewById(R.id.txt_profile_name);
        profileEmail = (EditText) findViewById(R.id.txt_profile_email);
        profileEmail.setText(user.getEmail());

        profileName.setText(user.getDisplayName());

        btnProfileEdit = (Button) findViewById(R.id.profile_edit_button);
        btnProfileEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnProfileEdit.setEnabled(false);

                // Reset errors.
                profileName.setError(null);
                profileEmail.setError(null);

                boolean cancel = false;
                boolean editted = false;
                View focusView = null;

                newName = profileName.getText().toString();
                newEmail = profileEmail.getText().toString();

                if (TextUtils.isEmpty(newName)) {
                    profileName.setError("이름을 입력해주세요.");
                    focusView = profileName;
                    cancel = true;
                }

                if (TextUtils.isEmpty(newName)) {
                    profileEmail.setError("이메일 주소를 입력해주세요.");
                    focusView = profileEmail;
                    cancel = true;
                }

                if (!isEmailValid(newEmail)) {
                    profileEmail.setError("잘못된 이메일 형식입니다.");
                    focusView = profileEmail;
                    cancel = true;
                }

                if(cancel) {
                    btnProfileEdit.setEnabled(true);
                } else {

                    if(newName.equals(user.getDisplayName()) && newEmail.equals(user.getEmail())) {
                        btnProfileEdit.setEnabled(true);
                        Toast.makeText(ProfileEditActivity.this, "이름 혹은 이메일 주소를 변경해주세요.", Toast.LENGTH_SHORT).show();
                    } else {
                        if(!newName.equals(user.getDisplayName())) {
                            nameChange();
                        } else {
                            if(!newEmail.equals(user.getEmail())) {
                                // email
                                emailChange();
                            }
                        }


                    }

//                    user.updateEmail(newEmail)
//                            .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                @Override
//                                public void onComplete(@NonNull Task<Void> task) {
//                                    if (task.isSuccessful()) {
//
//                                    }
//                                }
//                            });
                }
            }
        });

        passwordEdit = (RelativeLayout) findViewById(R.id.password_edit);
        passwordEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileEditActivity.this, PasswordEditActivity.class));
            }
        });

    }

    private void nameChange() {
        // users
        db.collection("users").document(user.getUid())
                .update("name", newName)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        // childs


                        // behaviors


                        // admin
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(newName)
                                .build();

                        user.updateProfile(profileUpdates)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            finish();
                                        }
                                    }
                                });

                        // email change check
                        if(!newEmail.equals(user.getEmail())) {
                            // email
                            emailChange();
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        btnProfileEdit.setEnabled(true);
                    }
                });
    }

    private void emailChange() {
        user.updateEmail(newEmail)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                        }
                    }
                });
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }
}
