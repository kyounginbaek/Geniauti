package com.geniauti.geniauti;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class BookmarkDetailActivity extends AppCompatActivity {

    private Menu menu;

    public static Bookmark selectedBookmark;
    public static View editLine1, editLine2, editLine3, editLine4, editLine5, editLine6, editLine7, editLine8;
    public static TextView editText1, editText2, editText3, editText4, editText5, editText6, editText7, editText8, editText9;
    public static View bookmarkLine1, bookmarkLine2, bookmarkLine3, bookmarkLine4, bookmarkLine5, bookmarkLine6, bookmarkLine7, bookmarkLine8;
    public static LinearLayout editLayout1, editLayout2, editLayout3, editLayout4, editLayout5, editLayout6, editLayout7, editLayout8, editLayout9;

    public static boolean editMode;
    private AlertDialog bookmarkDialog;
    private TextView textBookmark;
    private LinearLayout bookmarkSubmit;

    public static TextView bookmark_categorization;
    public static TextView bookmark_place;
    public static TextView bookmark_type;
    public static SeekBar bookmark_intensity;

    public static LinearLayout bookmark_interest;
    public static LinearLayout bookmark_self_stimulation;
    public static LinearLayout bookmark_task_evation;
    public static LinearLayout bookmark_demand;
    public static LinearLayout bookmark_etc;

    public static TextView intensityOne;
    public static TextView intensityTwo;
    public static TextView intensityThree;
    public static TextView intensityFour;
    public static TextView intensityFive;

    private Map<String, Object> presetData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark_detail);

        // toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.bookmark_detail_toolbar);
        setSupportActionBar(toolbar);

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        editMode = false;

        selectedBookmark = (Bookmark) getIntent().getSerializableExtra("temp");
        getSupportActionBar().setTitle(selectedBookmark.title);

        bookmark_interest = findViewById(R.id.bookmark_interest);
        bookmark_self_stimulation = findViewById(R.id.bookmark_self_stimulation);
        bookmark_task_evation = findViewById(R.id.bookmark_task_evation);
        bookmark_demand = findViewById(R.id.bookmark_demand);
        bookmark_etc = findViewById(R.id.bookmark_etc);

        bookmark_categorization = findViewById(R.id.txt_bookmark_categorization);
//        TextView bookmark_time = findViewById(R.id.txt_bookmark_time);
        bookmark_place = findViewById(R.id.txt_bookmark_place);
        bookmark_type = findViewById(R.id.txt_bookmark_type);
        bookmark_intensity = findViewById(R.id.bookmark_seekbar);
        bookmark_intensity.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        intensityOne = (TextView) findViewById(R.id.txt_bookmark_detail_intensity_one);
        intensityTwo = (TextView) findViewById(R.id.txt_bookmark_detail_intensity_two);
        intensityThree = (TextView) findViewById(R.id.txt_bookmark_detail_intensity_three);
        intensityFour = (TextView) findViewById(R.id.txt_bookmark_detail_intensity_four);
        intensityFive = (TextView) findViewById(R.id.txt_bookmark_detail_intensity_five);

//        TextView bookmark_before = findViewById(R.id.txt_bookmark_before);
//        TextView bookmark_current = findViewById(R.id.txt_bookmark_current);
//        TextView bookmark_after = findViewById(R.id.txt_bookmark_after);

        editLine1 = findViewById(R.id.bookmark_edit_line1);
        editLine2 = findViewById(R.id.bookmark_edit_line2);
        editLine3 = findViewById(R.id.bookmark_edit_line3);
        editLine4 = findViewById(R.id.bookmark_edit_line4);
        editLine5 = findViewById(R.id.bookmark_edit_line5);
        editLine6 = findViewById(R.id.bookmark_edit_line6);
        editLine7 = findViewById(R.id.bookmark_edit_line7);
        editLine8 = findViewById(R.id.bookmark_edit_line8);

        editText1 = findViewById(R.id.bookmark_edit_text1);
//        editText2 = findViewById(R.id.bookmark_edit_text2);
        editText3 = findViewById(R.id.bookmark_edit_text3);
        editText4 = findViewById(R.id.bookmark_edit_text4);
        editText5 = findViewById(R.id.bookmark_edit_text5);
        editText6 = findViewById(R.id.bookmark_edit_text6);
//        editText7 = findViewById(R.id.bookmark_edit_text7);
//        editText8 = findViewById(R.id.bookmark_edit_text8);
//        editText9 = findViewById(R.id.bookmark_edit_text9);

        editLayout1 = findViewById(R.id.behavior_bookmark_edit_layout1);
//        editLayout2 = findViewById(R.id.behavior_edit_layout2);
        editLayout3 = findViewById(R.id.behavior_bookmark_edit_layout3);
        editLayout4 = findViewById(R.id.behavior_bookmark_edit_layout4);
        editLayout5 = findViewById(R.id.behavior_bookmark_edit_layout5);
        editLayout6 = findViewById(R.id.behavior_bookmark_edit_layout6);
//        editLayout7 = findViewById(R.id.behavior_edit_layout7);
//        editLayout8 = findViewById(R.id.behavior_edit_layout8);
//        editLayout9 = findViewById(R.id.behavior_edit_layout9);

        editLayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editMode == true) {
                    goToBookmarkActivity(2);
                }
            }
        });

//        editLayout2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(editMode == true) {
//                    goToBehaviorActivity(1);
//                }
//            }
//        });

        editLayout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editMode == true) {
                    goToBookmarkActivity(1);
                }
            }
        });

        editLayout4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editMode == true) {
                    goToBookmarkActivity(3);
                }
            }
        });

        editLayout5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editMode == true) {
                    goToBookmarkActivity(5);
                }
            }
        });

        editLayout6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editMode == true) {
                    goToBookmarkActivity(4);
                }
            }
        });

//        editLayout7.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(editMode == true) {
//                    goToBehaviorActivity(4);
//                }
//            }
//        });
//
//        editLayout8.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(editMode == true) {
//                    goToBehaviorActivity(5);
//                }
//            }
//        });
//
//        editLayout9.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(editMode == true) {
//                    goToBehaviorActivity(6);
//                }
//            }
//        });

        bookmarkLine1 = findViewById(R.id.bookmark_line1);
        bookmarkLine2 = findViewById(R.id.bookmark_line2);
        bookmarkLine3 = findViewById(R.id.bookmark_line3);
        bookmarkLine4 = findViewById(R.id.bookmark_line4);
        bookmarkLine5 = findViewById(R.id.bookmark_line5);
        bookmarkLine6 = findViewById(R.id.bookmark_line6);
        bookmarkLine7 = findViewById(R.id.bookmark_line7);
        bookmarkLine8 = findViewById(R.id.bookmark_line8);

        bookmark_categorization.setText(selectedBookmark.categorization);

//        SimpleDateFormat formatter = new SimpleDateFormat("yyyy년 MM월 dd일 aa hh:mm", Locale.KOREAN);

//        bookmark_time.setText(formatter.format(selectedBookmark.start_time)+" ~ "+formatter.format(selectedBookmark.end_time).substring(14,22));
        bookmark_place.setText(selectedBookmark.place);

        setType(selectedBookmark.type);
        setReasonType(selectedBookmark.reason);
        setIntensity(selectedBookmark.intensity);

//        bookmark_before.setText(selectedBookmark.before_bookmark);
////        bookmark_current.setText(selectedBookmark.current_bookmark);
////        bookmark_after.setText(selectedBookmark.after_bookmark);

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(BookmarkDetailActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_bookmark_edit, null);
        mBuilder.setView(mView);
        bookmarkDialog = mBuilder.create();

        LinearLayout bookmarkCancel = (LinearLayout) mView.findViewById(R.id.bookmark_edit_cancel);
        bookmarkCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bookmarkDialog.dismiss();
            }
        });

        textBookmark = (TextView) mView.findViewById(R.id.txt_bookmark_edit);
        textBookmark.setText(selectedBookmark.title);

        bookmarkSubmit = (LinearLayout) mView.findViewById(R.id.bookmark_edit_submit);
        bookmarkSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bookmarkSubmit.setEnabled(false);

                if(textBookmark.getText().toString().equals("")) {
                    bookmarkSubmit.setEnabled(true);
                    Toast toast = Toast.makeText(BookmarkDetailActivity.this, "수정하실 자주 쓰는 기록의 이름을 입력해주세요.", Toast.LENGTH_SHORT);
                    toast.show();
                } else if(textBookmark.getText().toString().equals(selectedBookmark.title)) {
                    bookmarkSubmit.setEnabled(true);
                    Toast toast = Toast.makeText(BookmarkDetailActivity.this, "자주 쓰는 기록의 이름을 변경해주세요.", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    final DocumentReference sfDocRef = MainActivity.db.collection("users").document(MainActivity.user.getUid());

                    MainActivity.db.runTransaction(new Transaction.Function<Void>() {
                        @Override
                        public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                            DocumentSnapshot snapshot = transaction.get(sfDocRef);
                            List<Map<String, Object>> behaviorPresetArray = (List<Map<String, Object>>) snapshot.get("preset.behavior_preset");
                            HashMap<String, Object> docData = (HashMap<String, Object>) behaviorPresetArray.get(selectedBookmark.position);
                            docData.put("title", textBookmark.getText().toString());
                            behaviorPresetArray.set(selectedBookmark.position, docData);
                            transaction.update(sfDocRef, "preset.behavior_preset", behaviorPresetArray);

                            // Success
                            return null;
                        }
                    }).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
//                        mProgressView.setVisibility(View.GONE);
                            MainActivity.adapter.notifyDataSetChanged();
                            getSupportActionBar().setTitle(textBookmark.getText().toString());

                            bookmarkDialog.dismiss();
                            Toast toast = Toast.makeText(BookmarkDetailActivity.this, "자주 쓰는 기록 이름이 수정되었습니다.", Toast.LENGTH_SHORT);
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
            }
        });

    }

    public static void editModeOn() {
        editLine1.setVisibility(View.VISIBLE);
        editLine2.setVisibility(View.VISIBLE);
        editLine3.setVisibility(View.VISIBLE);
        editLine4.setVisibility(View.VISIBLE);
        editLine5.setVisibility(View.VISIBLE);
        editLine6.setVisibility(View.VISIBLE);
        editLine7.setVisibility(View.VISIBLE);
        editLine8.setVisibility(View.VISIBLE);

        editText1.setVisibility(View.VISIBLE);
//                editText2.setVisibility(View.VISIBLE);
        editText3.setVisibility(View.VISIBLE);
        editText4.setVisibility(View.VISIBLE);
        editText5.setVisibility(View.VISIBLE);
        editText6.setVisibility(View.VISIBLE);
//                editText7.setVisibility(View.VISIBLE);
//                editText8.setVisibility(View.VISIBLE);
//                editText9.setVisibility(View.VISIBLE);

        bookmarkLine1.setVisibility(View.GONE);
        bookmarkLine2.setVisibility(View.GONE);
        bookmarkLine3.setVisibility(View.GONE);
        bookmarkLine4.setVisibility(View.GONE);
        bookmarkLine5.setVisibility(View.GONE);
        bookmarkLine6.setVisibility(View.GONE);
        bookmarkLine7.setVisibility(View.GONE);
        bookmarkLine8.setVisibility(View.GONE);

        editMode = true;
    }

    public static void editModeOff() {
        editLine1.setVisibility(View.GONE);
        editLine2.setVisibility(View.GONE);
        editLine3.setVisibility(View.GONE);
        editLine4.setVisibility(View.GONE);
        editLine5.setVisibility(View.GONE);
        editLine6.setVisibility(View.GONE);
        editLine7.setVisibility(View.GONE);
        editLine8.setVisibility(View.GONE);

        editText1.setVisibility(View.GONE);
//                editText2.setVisibility(View.GONE);
        editText3.setVisibility(View.GONE);
        editText4.setVisibility(View.GONE);
        editText5.setVisibility(View.GONE);
        editText6.setVisibility(View.GONE);
//                editText7.setVisibility(View.GONE);
//                editText8.setVisibility(View.GONE);
//                editText9.setVisibility(View.GONE);

        bookmarkLine1.setVisibility(View.VISIBLE);
        bookmarkLine2.setVisibility(View.VISIBLE);
        bookmarkLine3.setVisibility(View.VISIBLE);
        bookmarkLine4.setVisibility(View.VISIBLE);
        bookmarkLine5.setVisibility(View.VISIBLE);
        bookmarkLine6.setVisibility(View.VISIBLE);
        bookmarkLine7.setVisibility(View.VISIBLE);
        bookmarkLine8.setVisibility(View.VISIBLE);

        editMode = false;
    }

    public static void setType(HashMap<String, Object> type) {
        String tmp_type = "";
        Iterator it = type.entrySet().iterator();
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
        bookmark_type.setText(tmp_type.substring(0, tmp_type.length()-2));
    }

    public static void setReasonType(HashMap<String, Object> reason_type) {

        bookmark_interest.setVisibility(View.GONE);
        bookmark_self_stimulation.setVisibility(View.GONE);
        bookmark_task_evation.setVisibility(View.GONE);
        bookmark_demand.setVisibility(View.GONE);
        bookmark_etc.setVisibility(View.GONE);

        if(reason_type.get("interest")!=null) {
            bookmark_interest.setVisibility(View.VISIBLE);
        }
        if(reason_type.get("selfstimulation")!=null) {
            bookmark_self_stimulation.setVisibility(View.VISIBLE);
        }
        if(reason_type.get("taskevation")!=null) {
            bookmark_task_evation.setVisibility(View.VISIBLE);
        }
        if(reason_type.get("demand")!=null) {
            bookmark_demand.setVisibility(View.VISIBLE);
        }
        if(reason_type.get("etc")!=null){
            bookmark_etc.setVisibility(View.VISIBLE);
        }
    }

    public static void setIntensity(int intensity) {

        intensityOne.setTextColor(Color.parseColor("#ababab"));
        intensityTwo.setTextColor(Color.parseColor("#ababab"));
        intensityThree.setTextColor(Color.parseColor("#ababab"));
        intensityFour.setTextColor(Color.parseColor("#ababab"));
        intensityFive.setTextColor(Color.parseColor("#ababab"));

        bookmark_intensity.setProgress(intensity-1);
        switch(intensity){
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
    }

    public void goToBookmarkActivity(int editPage) {
        Intent intent = new Intent(BookmarkDetailActivity.this, BookmarkActivity.class);
        intent.putExtra("bookmarkEdit", (Bookmark) selectedBookmark);
        intent.putExtra("bookmarkEditPage", editPage);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_bookmark, menu);

        this.menu = menu;

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        MenuItem actionEdit = menu.findItem(R.id.bookmark_edit);

        if(editMode){
            actionEdit.setTitle("수정 취소");
        } else {
            actionEdit.setTitle("수정");
        }

        if (id == android.R.id.home) {
            finish();
        } else if(id == R.id.bookmark_title) {
            bookmarkDialog.show();
        } else if(id == R.id.bookmark_edit) {

            if(editMode == false) {
                editModeOn();
            } else {
                editModeOff();
            }

        } else if(id == R.id.bookmark_delete) {

            final DocumentReference sfDocRef = MainActivity.db.collection("users").document(MainActivity.user.getUid());
            AlertDialog.Builder alt_bld = new AlertDialog.Builder(BookmarkDetailActivity.this);
            alt_bld.setMessage("자주 쓰는 기록을 삭제하시겠습니까?").setCancelable(
                    false).setPositiveButton("확인",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // Action for 'Yes' Button
                            MainActivity.db.runTransaction(new Transaction.Function<Void>() {
                                @Override
                                public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                                    DocumentSnapshot snapshot = transaction.get(sfDocRef);
                                    List<Map<String, Object>> behaviorPresetArray = (List<Map<String, Object>>) snapshot.get("preset.behavior_preset");
                                    behaviorPresetArray.remove(selectedBookmark.position);
                                    transaction.update(sfDocRef, "preset.behavior_preset", behaviorPresetArray);

                                    // Success
                                    return null;
                                }
                            }).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
//                        mProgressView.setVisibility(View.GONE);
                                    ProfileFragment.bookmarkData.remove(selectedBookmark.position);
                                    ProfileFragment.setListViewHeightBasedOnChildren(ProfileFragment.bookmarkListView);
                                    MainFragment.bookmarkData.remove(selectedBookmark.position);

                                    ProfileFragment.bookmarkAdapter.notifyDataSetChanged();
                                    MainFragment.bookmarkAdapter.notifyDataSetChanged();

                                    finish();
                                    Toast toast = Toast.makeText(BookmarkDetailActivity.this, "자주 쓰는 기록이 삭제되었습니다.", Toast.LENGTH_SHORT);
                                    toast.show();

                                }
                            })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

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
