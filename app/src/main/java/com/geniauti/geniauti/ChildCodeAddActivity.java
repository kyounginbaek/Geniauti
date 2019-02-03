package com.geniauti.geniauti;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.net.InetAddress;

public class ChildCodeAddActivity extends AppCompatActivity {

    private String childCode;
    private FirebaseFirestore db;
    private View mProgressView;
    private Button mChildCodeAddButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_code_add);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        final TextView childCodeText = (TextView) findViewById(R.id.child_code_txt);
        mChildCodeAddButton = (Button) findViewById(R.id.child_code_add_button);

        db = FirebaseFirestore.getInstance();
        mProgressView = findViewById(R.id.child_code_add_progress);
        mProgressView.bringToFront();

        mChildCodeAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                mChildCodeAddButton.setEnabled(false);
                mProgressView.setVisibility(View.VISIBLE);
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                childCodeText.setError(null);
                boolean cancel = false;
                childCode = childCodeText.getText().toString();

                if(TextUtils.isEmpty(childCode)){
                    childCodeText.setError("초대 코드를 입력해주세요.");
                    cancel = true;
                }

                if(childCode.length() != 5) {
                    childCodeText.setError("5자리의 초대 코드를 입력해주세요.");
                    cancel = true;
                }

                if(cancel) {
                    mChildCodeAddButton.setEnabled(true);
                    mProgressView.setVisibility(View.GONE);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                } else {
                    if(!isNetworkConnected()) {
                        mChildCodeAddButton.setEnabled(true);
                        mProgressView.setVisibility(View.GONE);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        Toast toast = Toast.makeText(ChildCodeAddActivity.this, "인터넷에 연결되어 있지 않습니다. 인터넷 연결 상태를 확인해주세요.", Toast.LENGTH_SHORT);
                        toast.show();
                    } else {
                        db.collection("childs")
                                .whereEqualTo("code", Integer.parseInt(childCode))
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            boolean exist = false;

                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                mProgressView.setVisibility(View.GONE);
                                                Intent intent=new Intent(ChildCodeAddActivity.this, RelationshipAddActivity.class);
                                                intent.putExtra("code", childCode);
                                                startActivity(intent);
                                                finish();
                                                exist = true;
                                            }

                                            if(!exist) {
                                                mChildCodeAddButton.setEnabled(true);
                                                mProgressView.setVisibility(View.GONE);
                                                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                                Toast toast = Toast.makeText(ChildCodeAddActivity.this, "등록된 아이 정보가 없습니다.", Toast.LENGTH_SHORT);
                                                toast.show();
                                            }
                                        } else {
                                            mChildCodeAddButton.setEnabled(true);
                                            mProgressView.setVisibility(View.GONE);
                                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                            Toast toast = Toast.makeText(ChildCodeAddActivity.this, "에러가 발생했습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT);
                                            toast.show();
                                        }
                                    }
                                });
                    }
                }
            }
        });
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ChildCodeAddActivity.this, ChildRegisterActivity.class));
        finish();
        super.onBackPressed();
    }

}
