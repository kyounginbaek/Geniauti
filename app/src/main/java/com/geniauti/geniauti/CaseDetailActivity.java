package com.geniauti.geniauti;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.geniauti.geniauti.ProfileFragment.setListViewHeightBasedOnChildren;
import static java.security.AccessController.getContext;

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
        caseEffect = findViewById(R.id.case_effect);

        Cases selectedCase = (Cases) getIntent().getSerializableExtra("temp");

        String tmp_reason = "행동 원인 > ";
        String tmp_type = "행동 종류 > ";

        if(selectedCase.tags_reason.get("attention")!=null) {
            tmp_reason = tmp_reason + "관심 / ";
        }
        if(selectedCase.tags_reason.get("self-sitmulatory behaviour")!=null) {
            tmp_reason = tmp_reason + "자기 자극 / ";
        }
        if(selectedCase.tags_reason.get("escape")!=null) {
            tmp_reason = tmp_reason + "과제 회피 / ";
        }
        if(selectedCase.tags_reason.get("tangibles")!=null) {
            tmp_reason = tmp_reason + "요구 / ";
        }

        caseReasons.setText(tmp_reason.substring(0, tmp_reason.length()-2));

        if(selectedCase.tags_type.get("self-injury")!=null) {
            tmp_type = tmp_type + "자해 / ";
        }
        if(selectedCase.tags_type.get("aggression")!=null) {
            tmp_type = tmp_type + "타해 / ";
        }
        if(selectedCase.tags_type.get("disruption")!=null) {
            tmp_type = tmp_type + "파괴 / ";
        }
        if(selectedCase.tags_type.get("elopement")!=null) {
            tmp_type = tmp_type + "이탈 / ";
        }
        if(selectedCase.tags_type.get("sexual behaviors")!=null) {
            tmp_type = tmp_type + "성적 / ";
        }
        if(selectedCase.tags_type.get("other behaviors")!=null) {
            tmp_type = tmp_type + "기타 / ";
        }

        caseTypes.setText(tmp_type.substring(0, tmp_type.length()-2));

        caseTitle.setText(selectedCase.title);
        caseBackgroundInfo.setText(selectedCase.backgroundInfo);
        caseBehavior.setText(selectedCase.behavior);

        CustomListView causeListView = (CustomListView) findViewById(R.id.case_cause_list);
        CauseListViewAdapter causeAdapter = new CauseListViewAdapter(getApplication(), R.layout.list_case_cause, selectedCase.cause);
        causeListView.setAdapter(causeAdapter);

        CustomListView solutionListView = (CustomListView) findViewById(R.id.case_solution_list);
        SolutionListViewAdapter solutionAdapter = new SolutionListViewAdapter(getApplication(), R.layout.list_case_solution, selectedCase.solution);
        solutionListView.setAdapter(solutionAdapter);

        caseEffect.setText(selectedCase.effect);

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

    public class CauseListViewAdapter extends BaseAdapter {

        private LayoutInflater inflater;
        private List<HashMap<String,Object>>  data;
        private int layout;

        public CauseListViewAdapter(Context context, int layout, List<HashMap<String,Object>>  data){
            this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.data = data;
            this.layout = layout;
        }

        @Override
        public int getCount(){return data.size();}

        @Override
        public Object getItem(int position){
            return data.get(position);
        }

        @Override
        public long getItemId(int position){return position;}

        @Override
        public View getView(int position, View v, ViewGroup parent){
            if(v == null){
                v = inflater.inflate(R.layout.list_case_cause, parent, false);
            }

            TextView causeTitle = (TextView) v.findViewById(R.id.case_cause_title);
            TextView causeDescription = (TextView) v.findViewById(R.id.case_cause_description);

            causeTitle.setText(data.get(position).get("title").toString());
            causeDescription.setText(data.get(position).get("description").toString());

            return v;
        }
    }

    public class SolutionListViewAdapter extends BaseAdapter {

        private LayoutInflater inflater;
        private List<HashMap<String,Object>> data;
        private int layout;

        public SolutionListViewAdapter(Context context, int layout, List<HashMap<String,Object>> data){
            this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.data = data;
            this.layout = layout;
        }

        @Override
        public int getCount(){return data.size();}

        @Override
        public Object getItem(int position){
            return data.get(position);
        }

        @Override
        public long getItemId(int position){return position;}

        @Override
        public View getView(int position, View v, ViewGroup parent){
            if(v == null){
                v = inflater.inflate(R.layout.list_case_solution, parent, false);
            }

            TextView solutionTitle = (TextView) v.findViewById(R.id.case_solution_title);
            TextView solutionDescription = (TextView) v.findViewById(R.id.case_solution_description);

            solutionTitle.setText(data.get(position).get("title").toString());
            solutionDescription.setText(data.get(position).get("description").toString());

            return v;
        }
    }


}
