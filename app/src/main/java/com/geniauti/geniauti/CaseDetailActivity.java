package com.geniauti.geniauti;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CaseDetailActivity extends AppCompatActivity {

    private View mProgressView;

    private TextView caseReasons, caseTypes;
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
        mProgressView.setVisibility(View.VISIBLE);

        caseReasons = findViewById(R.id.case_reasons);
        caseTypes = findViewById(R.id.case_types);
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

        String tmp_reason = "행동 원인 > ";
        String tmp_type = "행동 종류 > ";

        if(selectedCase.case_tags.get("attention")!=null) {
            tmp_reason = tmp_reason + "관심 / ";
        }
        if(selectedCase.case_tags.get("self-sitmulatory behaviour")!=null) {
            tmp_reason = tmp_reason + "자기 자극 / ";
        }
        if(selectedCase.case_tags.get("escape")!=null) {
            tmp_reason = tmp_reason + "과제 회피 / ";
        }
        if(selectedCase.case_tags.get("tangibles")!=null) {
            tmp_reason = tmp_reason + "요구 / ";
        }

        caseReasons.setText(tmp_reason.substring(0, tmp_reason.length()-2));

        if(selectedCase.case_tags.get("self-injury")!=null) {
            tmp_type = tmp_type + "자해 / ";
        }
        if(selectedCase.case_tags.get("aggression")!=null) {
            tmp_type = tmp_type + "타해 / ";
        }
        if(selectedCase.case_tags.get("disruption")!=null) {
            tmp_type = tmp_type + "파괴 / ";
        }
        if(selectedCase.case_tags.get("elopement")!=null) {
            tmp_type = tmp_type + "이탈 / ";
        }
        if(selectedCase.case_tags.get("sexual behaviors")!=null) {
            tmp_type = tmp_type + "성적 / ";
        }
        if(selectedCase.case_tags.get("other behaviors")!=null) {
            tmp_type = tmp_type + "기타 / ";
        }

        caseTypes.setText(tmp_type.substring(0, tmp_type.length()-2));

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

        mProgressView.setVisibility(View.GONE);
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
