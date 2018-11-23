package com.geniauti.geniauti;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

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

        TextView behavior_categorization = findViewById(R.id.txt_behavior_categorization);
        TextView behavior_time = findViewById(R.id.txt_behavior_time);
        TextView behavior_place = findViewById(R.id.txt_behavior_place);
        TextView behavior_type = findViewById(R.id.txt_behavior_type);
        SeekBar behavior_intensity = findViewById(R.id.behavior_seekbar);
        TextView behavior_befor = findViewById(R.id.txt_behavior_before);
        TextView behavior_current = findViewById(R.id.txt_behavior_current);
        TextView behavior_after = findViewById(R.id.txt_behavior_after);

        behavior_categorization.setText(selectedBehavior.categorization);
        behavior_time.setText(selectedBehavior.start_time.toString()+selectedBehavior.end_time.toString());
        behavior_place.setText(selectedBehavior.place);
//        behavior_type.setText();
//        SeekBar
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
