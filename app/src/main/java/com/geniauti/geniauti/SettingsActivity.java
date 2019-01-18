package com.geniauti.geniauti;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class SettingsActivity extends AppCompatActivity {

    private TextView csvEmail;
    private String csv;
    private CSVWriter writer;
    private List<String[]> data;
    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy년 MM월 dd일 aa hh:mm", Locale.KOREAN);

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
//        AlertDialog.Builder mBuilder = new AlertDialog.Builder(SettingsActivity.this);
//        View mView = getLayoutInflater().inflate(R.layout.dialog_csv_export, null);
//        csvEmail = (TextView) mView.findViewById(R.id.txt_csv_email);
//        mBuilder.setView(mView);
//        final AlertDialog csvDialog = mBuilder.create();
//
//        csvExportLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                csvDialog.show();
//            }
//        });
//
//        LinearLayout csvCancel = (LinearLayout) mView.findViewById(R.id.csv_cancel);
//        csvCancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                csvDialog.dismiss();
//            }
//        });
//
//        LinearLayout csvExport = (LinearLayout) mView.findViewById(R.id.csv_export);
//        csvExport.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(csvEmail.getText().toString().equals("")) {
//                    Toast toast = Toast.makeText(SettingsActivity.this, "이메일 주소를 입력해주세요.", Toast.LENGTH_SHORT);
//                    toast.show();
//                } else if(!csvEmail.getText().toString().contains("@")) {
//                    Toast toast = Toast.makeText(SettingsActivity.this, "올바른 이메일 형식이 아닙니다.", Toast.LENGTH_SHORT);
//                    toast.show();
//                } else {
//
//
//                }
//            }
//        });



        csvExportLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                csv = (Environment.getExternalStorageDirectory().getAbsolutePath() + "/BehaviorData.csv"); // Here csv file name is MyCsvFile.csv
                writer = null;


                try {
                    FileOutputStream fos = new FileOutputStream(csv);
                    writer = new CSVWriter(new OutputStreamWriter(fos, "EUC-KR"));

                    data = new ArrayList<String[]>();
                    data.add(new String[]{"start_time", "end_time", "place", "categorization", "current_behavior", "before_behavior", "after_behavior", "type", "intensity", "reason_type", "reason", "created_at", "name", "relationship"});

                    MainActivity.db.collection("behaviors")
                            .whereEqualTo("cid", MainActivity.cid)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {

                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            HashMap<String, Boolean> type = (HashMap<String, Boolean>) document.get("type");
                                            String tmpType = "";
                                            if(type.get("selfharm")!=null) {
                                                tmpType = tmpType + "자해 / ";
                                            }
                                            if(type.get("harm")!=null) {
                                                tmpType = tmpType + "타해 / ";
                                            }
                                            if(type.get("destruction")!=null) {
                                                tmpType = tmpType + "파괴 / ";
                                            }
                                            if(type.get("breakaway")!=null) {
                                                tmpType = tmpType + "이탈 / ";
                                            }
                                            if(type.get("sexual")!=null){
                                                tmpType = tmpType + "성적 / ";
                                            }
                                            if(type.get("etc")!=null){
                                                tmpType = tmpType + "기타 / ";
                                            }

                                            HashMap<String, Boolean> reason_type = (HashMap<String, Boolean>) document.get("reason_type");
                                            String tmpReasonType = "";
                                            if(reason_type.get("interest")!=null) {
                                                tmpReasonType = tmpReasonType + "관심 / ";
                                            }
                                            if(reason_type.get("selfstimulation")!=null) {
                                                tmpReasonType = tmpReasonType + "자기자극 / ";
                                            }
                                            if(reason_type.get("taskevation")!=null) {
                                                tmpReasonType = tmpReasonType + "과제회피 / ";
                                            }
                                            if(reason_type.get("demand")!=null) {
                                                tmpReasonType = tmpReasonType + "요구 / ";
                                            }
                                            if(reason_type.get("etc")!=null){
                                                tmpReasonType = tmpReasonType + "기타 / ";
                                            }

                                            HashMap<String, Boolean> reason = (HashMap<String, Boolean>) document.get("reason");
                                            String tmpReason = "";
                                            if(reason.get("interest1")!=null) {
                                                tmpReason = tmpReason + "자신에게 관심을 갖는 것이 좋아서 / ";
                                            }
                                            if(reason.get("interest2")!=null) {
                                                tmpReason = tmpReason + "타인에게 주목 받는 것을 즐겨서 / ";
                                            }
                                            if(reason.get("interest3")!=null) {
                                                tmpReason = tmpReason + "관심의 대상이 되고 싶어서 / ";
                                            }
                                            if(reason.get("interest4")!=null) {
                                                tmpReason = tmpReason + "다른 사람의 관심을 끌려고 / ";
                                            }
                                            if(reason.get("stimulation1")!=null){
                                                tmpReason = tmpReason + "행동을 통해 얻는 감각이 좋아서 / ";
                                            }
                                            if(reason.get("stimulation2")!=null){
                                                tmpReason = tmpReason + "행동을 하는 것 자체가 좋아서 / ";
                                            }
                                            if(reason.get("stimulation3")!=null){
                                                tmpReason = tmpReason + "행동이 주는 자극을 얻으려고 / ";
                                            }
                                            if(reason.get("demand1")!=null){
                                                tmpReason = tmpReason + "원하는 것을 즉각 얻지 못해서 / ";
                                            }
                                            if(reason.get("demand2")!=null){
                                                tmpReason = tmpReason + "원하는 물건을 가질 수 없어서 / ";
                                            }
                                            if(reason.get("demand3")!=null){
                                                tmpReason = tmpReason + "어떤 물건(장난감)을 갖기 위해서 / ";
                                            }
                                            if(reason.get("demand4")!=null){
                                                tmpReason = tmpReason + "본인이 갖고 싶은 물건을 얻으려고 / ";
                                            }
                                            if(reason.get("taskevation1")!=null){
                                                tmpReason = tmpReason + "시키는 일을 거부하려고 / ";
                                            }
                                            if(reason.get("taskevation2")!=null){
                                                tmpReason = tmpReason + "하려고 한 일이 힘들어서 / ";
                                            }
                                            if(reason.get("taskevation3")!=null){
                                                tmpReason = tmpReason + "주어진 일을 하기 싫어서 / ";
                                            }
                                            if(reason.get("taskevation4")!=null){
                                                tmpReason = tmpReason + "시키는 일이 어려워 피하려고 / ";
                                            }

                                            data.add(new String[]{formatter.format(document.get("start_time")), formatter.format(document.get("end_time")), document.get("place").toString(), document.get("categorization").toString(),
                                                    document.get("current_behavior").toString(), document.get("before_behavior").toString(), document.get("after_behavior").toString(), tmpType.substring(0, tmpType.length()-2),
                                                    document.get("intensity").toString(), tmpReasonType.substring(0, tmpReasonType.length()-2), tmpReason.substring(0, tmpReason.length()-2),
                                                    formatter.format(document.get("created_at")), document.get("name").toString(), document.get("relationship").toString()});
                                        }

                                        writer.writeAll(data); // data is adding to csv

                                        try {
                                            writer.close();

                                            Intent intent = new Intent(Intent.ACTION_SEND);
                                            intent.setType("plain/text");
                                            String[] address = {MainActivity.user.getEmail()};
                                            intent.putExtra(Intent.EXTRA_EMAIL, address);
                                            intent.putExtra(Intent.EXTRA_SUBJECT, ProfileFragment.childName + "의 행동조회 데이터");
                                            intent.putExtra(Intent.EXTRA_TEXT, ProfileFragment.childName + "의 행동조회 데이터를 보내드립니다.");

                                            File file = new File(csv);
                                            Uri uri = Uri.fromFile(file);
                                            intent.putExtra(Intent.EXTRA_STREAM, uri);
                                            startActivity(Intent.createChooser(intent, "어플리케이션을 선택해주세요."));

                                        } catch (IOException e) {

                                        }

                                    } else {

                                    }
                                }
                            });
                } catch (IOException e) {
                    e.printStackTrace();
                }
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
