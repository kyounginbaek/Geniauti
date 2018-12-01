package com.geniauti.geniauti;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.Map;

public class BehaviorDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_behavior_detail);

        // toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.behavior_detail_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("행동 카드");

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        Behavior selectedBehavior = (Behavior) getIntent().getSerializableExtra("temp");

        LinearLayout behavior_interest = findViewById(R.id.behavior_interest);
        LinearLayout behavior_self_stimulation = findViewById(R.id.behavior_self_stimulation);
        LinearLayout behavior_task_evation = findViewById(R.id.behavior_task_evation);
        LinearLayout behavior_demand = findViewById(R.id.behavior_demand);
        LinearLayout behavior_etc = findViewById(R.id.behavior_etc);

        behavior_interest.setVisibility(View.GONE);
        behavior_self_stimulation.setVisibility(View.GONE);
        behavior_task_evation.setVisibility(View.GONE);
        behavior_demand.setVisibility(View.GONE);
        behavior_etc.setVisibility(View.GONE);

        if(selectedBehavior.reason.get("관심")!=null) {
            behavior_interest.setVisibility(View.VISIBLE);
        }
        if(selectedBehavior.reason.get("자기자극")!=null) {
            behavior_self_stimulation.setVisibility(View.VISIBLE);
        }
        if(selectedBehavior.reason.get("과제회피")!=null) {
            behavior_task_evation.setVisibility(View.VISIBLE);
        }
        if(selectedBehavior.reason.get("요구")!=null) {
            behavior_demand.setVisibility(View.VISIBLE);
        }
        if(selectedBehavior.reason.get("기타")!=null){
            behavior_etc.setVisibility(View.VISIBLE);
        }

        TextView behavior_categorization = findViewById(R.id.txt_behavior_categorization);
        TextView behavior_time = findViewById(R.id.txt_behavior_time);
        TextView behavior_place = findViewById(R.id.txt_behavior_place);
        TextView behavior_type = findViewById(R.id.txt_behavior_type);
        SeekBar behavior_intensity = findViewById(R.id.behavior_seekbar);
        TextView behavior_befor = findViewById(R.id.txt_behavior_before);
        TextView behavior_current = findViewById(R.id.txt_behavior_current);
        TextView behavior_after = findViewById(R.id.txt_behavior_after);

        behavior_categorization.setText(selectedBehavior.categorization);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd. aa hh:mm");

        behavior_time.setText(formatter.format(selectedBehavior.start_time)+" ~ "+formatter.format(selectedBehavior.end_time).substring(12,20));
        behavior_place.setText(selectedBehavior.place);

        String tmp_type = "";
        Iterator it = selectedBehavior.type.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            switch(pair.getKey().toString()) {
                case "selfHarm":
                    tmp_type = tmp_type + "자해, ";
                    break;
                case "harm":
                    tmp_type = tmp_type + "타패, ";
                    break;
                case "destruction":
                    tmp_type = tmp_type + "파괴, ";
                    break;
                case "breakaway":
                    tmp_type = tmp_type + "이탈, ";
                    break;
                case "sexual":
                    tmp_type = tmp_type + "성적, ";
                    break;
                case "etc":
                    tmp_type = tmp_type + "기타, ";
                    break;
            }
            it.remove(); // avoids a ConcurrentModificationException
        }
        behavior_type.setText(tmp_type.substring(0, tmp_type.length()-2));

        behavior_intensity.setProgress(selectedBehavior.intensity);
        behavior_befor.setText(selectedBehavior.before_behavior);
        behavior_current.setText(selectedBehavior.current_behavior);
        behavior_after.setText(selectedBehavior.after_behavior);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_behavior, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.menu_behavior_edit) {
//            Intent intent = new Intent(this.getContext(), SettingsActivity.class);
//            this.startActivity(intent);
            return true;
        } else if(id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
