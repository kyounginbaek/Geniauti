package com.geniauti.geniauti;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ChildRegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_register);

        Button mChildRegisterButton = (Button) findViewById(R.id.child_register_button);
        mChildRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ChildRegisterActivity.this,ChildAddActivity.class);
                startActivity(intent);
                finish();
            }
        });

        Button mChildCodeButton = (Button) findViewById(R.id.child_code_button);
        mChildCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ChildRegisterActivity.this,ChildCodeAddActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}
