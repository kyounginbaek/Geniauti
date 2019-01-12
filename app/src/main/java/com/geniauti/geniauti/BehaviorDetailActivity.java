package com.geniauti.geniauti;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
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
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class BehaviorDetailActivity extends AppCompatActivity {

    private Menu menu;

    private Behavior selectedBehavior;
    View editLine1, editLine2, editLine3, editLine4, editLine5, editLine6, editLine7, editLine8;
    TextView editText1, editText2, editText3, editText4, editText5, editText6, editText7, editText8, editText9;
    View behaviorLine1, behaviorLine2, behaviorLine3, behaviorLine4, behaviorLine5, behaviorLine6, behaviorLine7, behaviorLine8;
    LinearLayout editLayout1, editLayout2, editLayout3, editLayout4, editLayout5, editLayout6, editLayout7, editLayout8, editLayout9;

    private boolean editMode = false;
    private FirebaseFirestore db;
    private FirebaseUser user;
    private AlertDialog bookmarkDialog;
    private LinearLayout bookmarkSubmit;
    private TextView textBookmark;

    private Map<String, Object> presetData;

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

        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        selectedBehavior = (Behavior) getIntent().getSerializableExtra("temp");

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

        String tmp_reason = "";

        if(selectedBehavior.reason_type.get("interest")!=null) {
            behavior_interest.setVisibility(View.VISIBLE);
            tmp_reason = tmp_reason + "관심, ";
        }
        if(selectedBehavior.reason_type.get("selfstimulation")!=null) {
            behavior_self_stimulation.setVisibility(View.VISIBLE);
            tmp_reason = tmp_reason + "자기자극, ";
        }
        if(selectedBehavior.reason_type.get("taskevation")!=null) {
            behavior_task_evation.setVisibility(View.VISIBLE);
            tmp_reason = tmp_reason + "과제회피, ";
        }
        if(selectedBehavior.reason_type.get("demand")!=null) {
            behavior_demand.setVisibility(View.VISIBLE);
            tmp_reason = tmp_reason + "요구, ";
        }
        if(selectedBehavior.reason_type.get("etc")!=null){
            behavior_etc.setVisibility(View.VISIBLE);
            tmp_reason = tmp_reason + "기타, ";
        }

        TextView behavior_categorization = findViewById(R.id.txt_behavior_categorization);
        TextView behavior_time = findViewById(R.id.txt_behavior_time);
        TextView behavior_place = findViewById(R.id.txt_behavior_place);
        TextView behavior_type = findViewById(R.id.txt_behavior_type);
        SeekBar behavior_intensity = findViewById(R.id.behavior_seekbar);

        TextView intensityOne = (TextView) findViewById(R.id.txt_behavior_detail_intensity_one);
        TextView intensityTwo = (TextView) findViewById(R.id.txt_behavior_detail_intensity_two);
        TextView intensityThree = (TextView) findViewById(R.id.txt_behavior_detail_intensity_three);
        TextView intensityFour = (TextView) findViewById(R.id.txt_behavior_detail_intensity_four);
        TextView intensityFive = (TextView) findViewById(R.id.txt_behavior_detail_intensity_five);

        TextView behavior_before = findViewById(R.id.txt_behavior_before);
        TextView behavior_current = findViewById(R.id.txt_behavior_current);
        TextView behavior_after = findViewById(R.id.txt_behavior_after);

        editLine1 = findViewById(R.id.behavior_edit_line1);
        editLine2 = findViewById(R.id.behavior_edit_line2);
        editLine3 = findViewById(R.id.behavior_edit_line3);
        editLine4 = findViewById(R.id.behavior_edit_line4);
        editLine5 = findViewById(R.id.behavior_edit_line5);
        editLine6 = findViewById(R.id.behavior_edit_line6);
        editLine7 = findViewById(R.id.behavior_edit_line7);
        editLine8 = findViewById(R.id.behavior_edit_line8);

        editText1 = findViewById(R.id.behavior_edit_text1);
        editText2 = findViewById(R.id.behavior_edit_text2);
        editText3 = findViewById(R.id.behavior_edit_text3);
        editText4 = findViewById(R.id.behavior_edit_text4);
        editText5 = findViewById(R.id.behavior_edit_text5);
        editText6 = findViewById(R.id.behavior_edit_text6);
        editText7 = findViewById(R.id.behavior_edit_text7);
        editText8 = findViewById(R.id.behavior_edit_text8);
        editText9 = findViewById(R.id.behavior_edit_text9);

        editLayout1 = findViewById(R.id.behavior_edit_layout1);
        editLayout2 = findViewById(R.id.behavior_edit_layout2);
        editLayout3 = findViewById(R.id.behavior_edit_layout3);
        editLayout4 = findViewById(R.id.behavior_edit_layout4);
        editLayout5 = findViewById(R.id.behavior_edit_layout5);
        editLayout6 = findViewById(R.id.behavior_edit_layout6);
        editLayout7 = findViewById(R.id.behavior_edit_layout7);
        editLayout8 = findViewById(R.id.behavior_edit_layout8);
        editLayout9 = findViewById(R.id.behavior_edit_layout9);

        editLayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editMode == true) {
                    goToBehaviorActivity(3);
                }
            }
        });

        editLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editMode == true) {
                    goToBehaviorActivity(1);
                }
            }
        });

        editLayout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editMode == true) {
                    goToBehaviorActivity(2);
                }
            }
        });

        editLayout4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editMode == true) {
                    goToBehaviorActivity(7);
                }
            }
        });

        editLayout5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editMode == true) {
                    goToBehaviorActivity(9);
                }
            }
        });

        editLayout6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editMode == true) {
                    goToBehaviorActivity(8);
                }
            }
        });

        editLayout7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editMode == true) {
                    goToBehaviorActivity(4);
                }
            }
        });

        editLayout8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editMode == true) {
                    goToBehaviorActivity(5);
                }
            }
        });

        editLayout9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editMode == true) {
                    goToBehaviorActivity(6);
                }
            }
        });

        behaviorLine1 = findViewById(R.id.behavior_line1);
        behaviorLine2 = findViewById(R.id.behavior_line2);
        behaviorLine3 = findViewById(R.id.behavior_line3);
        behaviorLine4 = findViewById(R.id.behavior_line4);
        behaviorLine5 = findViewById(R.id.behavior_line5);
        behaviorLine6 = findViewById(R.id.behavior_line6);
        behaviorLine7 = findViewById(R.id.behavior_line7);
        behaviorLine8 = findViewById(R.id.behavior_line8);

        behavior_categorization.setText(selectedBehavior.categorization);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy년 MM월 dd일 aa hh:mm", Locale.KOREAN);

        behavior_time.setText(formatter.format(selectedBehavior.start_time)+" ~ "+formatter.format(selectedBehavior.end_time).substring(14,22));
        behavior_place.setText(selectedBehavior.place);

        String tmp_type = "";
        Iterator it = selectedBehavior.type.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            switch(pair.getKey().toString()) {
                case "selfharm":
                    tmp_type = tmp_type + "자해, ";
                    break;
                case "harm":
                    tmp_type = tmp_type + "타해, ";
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
        }
        behavior_type.setText(tmp_type.substring(0, tmp_type.length()-2));

        behavior_intensity.setProgress(selectedBehavior.intensity-1);
        switch(selectedBehavior.intensity){
            case 1:
                intensityOne.setTextColor(Color.parseColor("#2dc76d"));
                break;
            case 2:
                intensityTwo.setTextColor(Color.parseColor("#2dc76d"));
                break;
            case 3:
                intensityThree.setTextColor(Color.parseColor("#2dc76d"));
                break;
            case 4:
                intensityFour.setTextColor(Color.parseColor("#2dc76d"));
                break;
            case 5:
                intensityFive.setTextColor(Color.parseColor("#2dc76d"));
                break;
        }

        behavior_before.setText(selectedBehavior.before_behavior);
        behavior_current.setText(selectedBehavior.current_behavior);
        behavior_after.setText(selectedBehavior.after_behavior);

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(BehaviorDetailActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_behavior_bookmark, null);
        mBuilder.setView(mView);
        bookmarkDialog = mBuilder.create();

        LinearLayout bookmarkCancel = (LinearLayout) mView.findViewById(R.id.bookmark_cancel);
        bookmarkCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bookmarkDialog.dismiss();
            }
        });

        textBookmark = (TextView) mView.findViewById(R.id.txt_behavior_bookmark);
        textBookmark.setText(selectedBehavior.place + " / " + selectedBehavior.categorization + " / " + tmp_reason.substring(0, tmp_reason.length()-2));

        bookmarkSubmit = (LinearLayout) mView.findViewById(R.id.bookmark_submit);
        bookmarkSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bookmarkSubmit.setEnabled(false);

                presetData = new HashMap<>();
                presetData.put("title", textBookmark.getText().toString());
                presetData.put("place", selectedBehavior.place);
                presetData.put("categorization", selectedBehavior.categorization);
                presetData.put("type", selectedBehavior.type);
                presetData.put("intensity", selectedBehavior.intensity);
                presetData.put("reason_type", selectedBehavior.reason_type);
                presetData.put("reason", selectedBehavior.reason);

                final DocumentReference sfDocRef = db.collection("users").document(user.getUid());

                db.runTransaction(new Transaction.Function<Void>() {
                    @Override
                    public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                        DocumentSnapshot snapshot = transaction.get(sfDocRef);
                        List<Map<String, Object>> behaviorPresetArray = (List<Map<String, Object>>) snapshot.get("preset.behavior_preset");
                        if(behaviorPresetArray != null) {
                            behaviorPresetArray.add(presetData);
                            transaction.update(sfDocRef, "preset.behavior_preset", behaviorPresetArray);

                        } else {
                            List<Map<String, Object>> newBehaviorPresetArray = new ArrayList();
                            newBehaviorPresetArray.add(presetData);
                            transaction.update(sfDocRef, "preset.behavior_preset", newBehaviorPresetArray);

                        }

                        // Success
                        return null;
                    }
                }).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
//                        mProgressView.setVisibility(View.GONE);
                        MainActivity.adapter.notifyDataSetChanged();
                        bookmarkDialog.dismiss();
                        Toast toast = Toast.makeText(BehaviorDetailActivity.this, "자주 쓰는 기록으로 등록되었습니다.", Toast.LENGTH_SHORT);
                        toast.show();

                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                bookmarkSubmit.setEnabled(true);
                            }
                        });
            }
        });

    }

    public void goToBehaviorActivity(int editPage) {
        Intent intent = new Intent(BehaviorDetailActivity.this, BehaviorActivity.class);
        intent.putExtra("behaviorEdit", (Behavior) selectedBehavior);
        intent.putExtra("behaviorEditPage", editPage);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_behavior, menu);

        this.menu = menu;

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        MenuItem behaviorEdit = menu.findItem(R.id.behavior_edit);
        if(editMode){
            behaviorEdit.setTitle("수정 취소");
        } else {
            behaviorEdit.setTitle("수정");
        }

        if (id == android.R.id.home) {
            finish();
        } else if(id == R.id.behavior_bookmark){

            bookmarkDialog.show();

        } else if(id == R.id.behavior_edit) {

            if(editMode == false) {
                editLine1.setVisibility(View.VISIBLE);
                editLine2.setVisibility(View.VISIBLE);
                editLine3.setVisibility(View.VISIBLE);
                editLine4.setVisibility(View.VISIBLE);
                editLine5.setVisibility(View.VISIBLE);
                editLine6.setVisibility(View.VISIBLE);
                editLine7.setVisibility(View.VISIBLE);
                editLine8.setVisibility(View.VISIBLE);

                editText1.setVisibility(View.VISIBLE);
                editText2.setVisibility(View.VISIBLE);
                editText3.setVisibility(View.VISIBLE);
                editText4.setVisibility(View.VISIBLE);
                editText5.setVisibility(View.VISIBLE);
                editText6.setVisibility(View.VISIBLE);
                editText7.setVisibility(View.VISIBLE);
                editText8.setVisibility(View.VISIBLE);
                editText9.setVisibility(View.VISIBLE);

                behaviorLine1.setVisibility(View.GONE);
                behaviorLine2.setVisibility(View.GONE);
                behaviorLine3.setVisibility(View.GONE);
                behaviorLine4.setVisibility(View.GONE);
                behaviorLine5.setVisibility(View.GONE);
                behaviorLine6.setVisibility(View.GONE);
                behaviorLine7.setVisibility(View.GONE);
                behaviorLine8.setVisibility(View.GONE);

                editMode = true;
            } else {
                editLine1.setVisibility(View.GONE);
                editLine2.setVisibility(View.GONE);
                editLine3.setVisibility(View.GONE);
                editLine4.setVisibility(View.GONE);
                editLine5.setVisibility(View.GONE);
                editLine6.setVisibility(View.GONE);
                editLine7.setVisibility(View.GONE);
                editLine8.setVisibility(View.GONE);

                editText1.setVisibility(View.GONE);
                editText2.setVisibility(View.GONE);
                editText3.setVisibility(View.GONE);
                editText4.setVisibility(View.GONE);
                editText5.setVisibility(View.GONE);
                editText6.setVisibility(View.GONE);
                editText7.setVisibility(View.GONE);
                editText8.setVisibility(View.GONE);
                editText9.setVisibility(View.GONE);

                behaviorLine1.setVisibility(View.VISIBLE);
                behaviorLine2.setVisibility(View.VISIBLE);
                behaviorLine3.setVisibility(View.VISIBLE);
                behaviorLine4.setVisibility(View.VISIBLE);
                behaviorLine5.setVisibility(View.VISIBLE);
                behaviorLine6.setVisibility(View.VISIBLE);
                behaviorLine7.setVisibility(View.VISIBLE);
                behaviorLine8.setVisibility(View.VISIBLE);

                editMode = false;
            }

        } else if(id == R.id.behavior_delete) {

            AlertDialog.Builder alt_bld = new AlertDialog.Builder(BehaviorDetailActivity.this);
            alt_bld.setMessage("행동 카드를 삭제하시겠습니까?").setCancelable(
                    false).setPositiveButton("확인",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // Action for 'Yes' Button
                            db.collection("behaviors").document(selectedBehavior.bid)
                                    .delete()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
//                                                           mProgressView.setVisibility(View.GONE);
                                            MainActivity.adapter.notifyDataSetChanged();
                                            finish();
                                            Toast toast = Toast.makeText(BehaviorDetailActivity.this, "행동이 삭제되었습니다.", Toast.LENGTH_SHORT);
                                            toast.show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
//                                                          Log.w(TAG, "Error writing document", e);
                                        }
                                    });
                        }
                    }).setNegativeButton("취소",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // Action for 'NO' Button
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = alt_bld.create();
            // Title for AlertDialog
//                        alert.setTitle("Title");
            // Icon for AlertDialog
            alert.show();
        }

        return super.onOptionsItemSelected(item);
    }
}
