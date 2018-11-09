package com.geniauti.geniauti;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CaseDetailActivity extends AppCompatActivity {

    private View mProgressView;

    private TextView caseTitle;
    private TextView caseBackgroundInfo;
    private TextView caseBehavior;
    private TextView caseCauseTitle1, caseCauseTitle2;
    private TextView caseCauseDescription1, caseCauseDescription2;
    private TextView caseSolutionTitle1, caseSolutionTitle2, caseSolutionTitle3;
    private TextView caseSolutionDescription1, caseSolutionDescription2, caseSolutionDescription3;
    private TextView caseEffect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_case_detail);

        // toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.case_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("사례");

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        mProgressView = findViewById(R.id.case_detail_progress);

        caseTitle = findViewById(R.id.case_title);
        caseBackgroundInfo = findViewById(R.id.case_backgroundInfo);
        caseBehavior = findViewById(R.id.case_behavior);
        caseCauseTitle1 = findViewById(R.id.case_cause_title1);
        caseCauseTitle2 = findViewById(R.id.case_cause_title2);
        caseCauseDescription1 = findViewById(R.id.case_cause_description1);
        caseCauseDescription2 = findViewById(R.id.case_cause_description2);
        caseSolutionTitle1 = findViewById(R.id.case_solution_title1);
        caseSolutionTitle2 = findViewById(R.id.case_solution_title2);
        caseSolutionTitle3 = findViewById(R.id.case_solution_title3);
        caseSolutionDescription1 = findViewById(R.id.case_solution_description1);
        caseSolutionDescription2 = findViewById(R.id.case_solution_description2);
        caseSolutionDescription3 = findViewById(R.id.case_solution_description3);
        caseEffect = findViewById(R.id.case_effect);

        Cases selectedCase = (Cases) getIntent().getSerializableExtra("temp");

        caseTitle.setText(selectedCase.case_title);
        caseBackgroundInfo.setText(selectedCase.case_backgroundInfo);
        caseBehavior.setText(selectedCase.case_behavior);

        List<HashMap<String,String>> case_cause = new ArrayList<HashMap<String,String>>();
        case_cause = (List<HashMap<String,String>>) selectedCase.case_cause;
        caseCauseTitle1.setText(case_cause.get(0).get("title"));
        caseCauseTitle2.setText(case_cause.get(1).get("title"));
        caseCauseDescription1.setText(case_cause.get(0).get("description"));
        caseCauseDescription2.setText(case_cause.get(1).get("description"));

        List<HashMap<String,String>> case_solution = new ArrayList<HashMap<String,String>>();
        case_solution = (List<HashMap<String,String>>) selectedCase.case_solution;
        caseSolutionTitle1.setText(case_solution.get(0).get("title"));
        caseSolutionTitle2.setText(case_solution.get(1).get("title"));
        caseSolutionTitle3.setText(case_solution.get(2).get("title"));
        caseSolutionDescription1.setText(case_solution.get(0).get("description"));
        caseSolutionDescription2.setText(case_solution.get(1).get("description"));
        caseSolutionDescription3.setText(case_solution.get(2).get("description"));
        caseEffect.setText(selectedCase.case_effect);
    }

//    @Override
//    public void onBackPressed() {
//        finish();
//        super.onBackPressed();
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

}
