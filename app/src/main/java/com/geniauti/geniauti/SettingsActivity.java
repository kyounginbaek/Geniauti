package com.geniauti.geniauti;

import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.settings_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("설정");

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        RelativeLayout csvExportLayout = (RelativeLayout) findViewById(R.id.csv_export_layout);
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(SettingsActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_csv_export, null);
        mBuilder.setView(mView);
        final AlertDialog csvDialog = mBuilder.create();

        csvExportLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                csvDialog.show();
            }
        });

        LinearLayout csvCancel = (LinearLayout) mView.findViewById(R.id.csv_cancel);
        csvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                csvDialog.dismiss();
            }
        });

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
