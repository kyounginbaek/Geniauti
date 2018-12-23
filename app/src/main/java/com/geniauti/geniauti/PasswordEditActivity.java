package com.geniauti.geniauti;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class PasswordEditActivity extends AppCompatActivity {

    private FirebaseUser user;
    private View mProgressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_edit);

        // toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.password_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("비밀번호 변경");

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        user = FirebaseAuth.getInstance().getCurrentUser();
        mProgressView = findViewById(R.id.password_edit_progress);

        final EditText currentPassword = (EditText) findViewById(R.id.txt_current_password);
        final EditText newPassword = (EditText) findViewById(R.id.txt_new_password);
        final EditText newPasswordCheck = (EditText) findViewById(R.id.txt_new_password_check);

        Button btnPasswordEdit = (Button) findViewById(R.id.password_edit_button);
        btnPasswordEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProgressView.setVisibility(View.VISIBLE);

                // Reset errors.
                currentPassword.setError(null);
                newPassword.setError(null);
                newPasswordCheck.setError(null);

                boolean cancel = false;
                View focusView = null;

                String mCurrentPassword = currentPassword.getText().toString();
                final String mNewPassword = newPassword.getText().toString();
                String mNewPasswordCheck = newPasswordCheck.getText().toString();

                if(TextUtils.isEmpty(mCurrentPassword)){
                    currentPassword.setError("기존 비밀번호를 입력해주세요.");
                    focusView = currentPassword;
                    cancel = true;
                }

                if(TextUtils.isEmpty(mNewPassword)){
                    newPassword.setError("새로운 비밀번호를 입력해주세요.");
                    focusView = newPassword;
                    cancel = true;
                }

                if(!isPasswordValid(mNewPassword)) {
                    newPassword.setError("6자리 이상의 비밀번호를 입력해주세요.");
                    focusView = newPassword;
                    cancel = true;
                }

                if(TextUtils.isEmpty(mNewPasswordCheck)){
                    newPasswordCheck.setError("새로운 비밀번호 확인을 입력해주세요.");
                    focusView = newPasswordCheck;
                    cancel = true;
                }

                if(!isPasswordValid(mNewPasswordCheck)){
                    newPasswordCheck.setError("6자리 이상의 비밀번호를 입력해주세요.");
                    focusView = newPasswordCheck;
                    cancel = true;
                }

                if(mCurrentPassword.equals(mNewPassword)){
                    newPassword.setError("기존 비밀번호와 다른 새로운 비밀번호를 입력해주세요.");
                    focusView = newPassword;
                    cancel = true;
                }

                if(!mNewPassword.equals(mNewPasswordCheck)){
                    newPasswordCheck.setError("새로운 비밀번호와 확인이 일치하지 않습니다.");
                    focusView = newPasswordCheck;
                    cancel = true;
                }

                if(cancel){
                    mProgressView.setVisibility(View.GONE);
                } else {
                    AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), mCurrentPassword);

                    user.reauthenticate(credential)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        user.updatePassword(mNewPassword)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            mProgressView.setVisibility(View.GONE);
                                                            finish();
                                                            Toast toast = Toast.makeText(PasswordEditActivity.this, "비밀번호가 변경되었습니다.", Toast.LENGTH_SHORT);
                                                            toast.show();
                                                        }
                                                    }
                                                });
                                    } else {
                                        mProgressView.setVisibility(View.GONE);
                                        Toast toast = Toast.makeText(PasswordEditActivity.this, "기존 비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT);
                                        toast.show();
                                    }
                                }
                            });
                }
            }
        });
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 5;
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
