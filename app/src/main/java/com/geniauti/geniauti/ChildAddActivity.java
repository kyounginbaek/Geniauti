package com.geniauti.geniauti;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.net.InetAddress;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


public class ChildAddActivity extends AppCompatActivity {

    private View mProgressView;

    private ImageView childImage;
    private TextView mChildName;
    private TextView mChildAge;
    private RadioGroup radioGroupAge;
    private RadioButton radioYear;
    private RadioGroup radioGroupSex;
    private RadioButton radioGirl;
    private TextView mChildRelation;
    private Button mChildAddButton;

    private int childAge;
    private RadioButton radioBtnSex;
    private String cid;

    private FirebaseFirestore db;
    private FirebaseUser user;

    private Image image = null;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private UploadTask uploadTask;
    private long currentTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_add);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        mProgressView = findViewById(R.id.child_add_progress);
        mProgressView.bringToFront();

        childImage = (ImageView) findViewById(R.id.child_add_image);

        mChildName = (TextView) findViewById(R.id.txt_child_name);
        mChildAge= (TextView) findViewById(R.id.txt_child_age);
        radioGroupAge = (RadioGroup) findViewById(R.id.radio_child_age);
        radioGroupAge.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
//                if(checkedId == R.id.radio_btn_month) {
//                    mChildAge.setHint("아이 나이(개월)");
//                } else {
//                    mChildAge.setHint("아이 나이(세)");
//                }
                radioYear.setError(null);
            }
        });

        radioYear = (RadioButton) findViewById(R.id.radio_btn_year);
        radioGroupSex = (RadioGroup) findViewById(R.id.radio_child_sex);
        radioGroupSex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                radioGirl.setError(null);
            }
        });

        radioGirl = (RadioButton) findViewById(R.id.radio_btn_girl);
        mChildRelation = (TextView) findViewById(R.id.txt_child_relation);

        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        RelativeLayout childImageLayout = (RelativeLayout) findViewById(R.id.child_add_image_layout);
        childImageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.create(ChildAddActivity.this)
                        .limit(1)
                        .theme(R.style.AppTheme)
                        .start();
            }
        });

        mChildAddButton = (Button) findViewById(R.id.child_add_button);
        mChildAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                mChildAddButton.setEnabled(false);
                mProgressView.setVisibility(View.VISIBLE);
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                // Reset errors.
                mChildName.setError(null);
                mChildAge.setError(null);
                radioYear.setError(null);
                radioGirl.setError(null);
                mChildRelation.setError(null);

                boolean cancel = false;

                if(TextUtils.isEmpty(mChildName.getText().toString())) {
                    mChildName.setError("아이 이름을 입력해주세요.");
                    cancel = true;
                }

                if(TextUtils.isEmpty(mChildAge.getText().toString())) {
                    mChildAge.setError("아이 나이를 입력해주세요.");
                    cancel = true;
                }

//                if((int) radioGroupAge.getCheckedRadioButtonId() <= 0) {
//                    radioYear.setError("연령 표기 방법을 선택해주세요.");
//                    focusView = radioYear;
//                    cancel = true;
//                }

//                if((int) radioGroupSex.getCheckedRadioButtonId() <= 0) {
//                    radioGirl.setError("아이 성별을 선택해주세요.");
//                    focusView = radioGirl;
//                    cancel = true;
//                }

                if(TextUtils.isEmpty(mChildRelation.getText().toString())) {
                    mChildRelation.setError("아이와의 관계를 입력해주세요.");
                    cancel = true;
                }

                if(cancel) {
                    // There was an error; don't attempt login and focus the first
                    // form field with an error.
                    mChildAddButton.setEnabled(true);
                    mProgressView.setVisibility(View.GONE);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                } else {
                    if(!isNetworkConnected()) {
                        mChildAddButton.setEnabled(true);
                        mProgressView.setVisibility(View.GONE);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        Toast toast = Toast.makeText(ChildAddActivity.this, "인터넷에 연결되어 있지 않습니다. 인터넷 연결 상태를 확인해주세요.", Toast.LENGTH_SHORT);
                        toast.show();
                    } else {
                        childInfoAdd();
                    }
                }
            }
        });
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    private void childImageAdd() {

        Uri file = Uri.fromFile(new File(image.getPath()));
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        StorageReference riversRef = storageRef.child("childs/"+cid);
        uploadTask = riversRef.putFile(file);

        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                mChildAddButton.setEnabled(false);
                mProgressView.setVisibility(View.GONE);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                Toast toast = Toast.makeText(ChildAddActivity.this, "이미지 업로드에 실패했습니다. 다시 한번 시도해주세요.", Toast.LENGTH_SHORT);
                toast.show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                mProgressView.setVisibility(View.GONE);
                Intent intent=new Intent(ChildAddActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void childInfoAdd() {
        currentTime = System.currentTimeMillis();

        int selectedAge = radioGroupAge.getCheckedRadioButtonId();
        // find the radiobutton by returned id
        RadioButton radioBtnAge = (RadioButton) findViewById(selectedAge);

        childAge = Integer.parseInt(mChildAge.getText().toString());

        if(radioBtnAge.getText().equals("세")) {
            childAge = Integer.parseInt(mChildAge.getText().toString()) * 12;
        }

        int selectedSex = radioGroupSex.getCheckedRadioButtonId();
        // find the radiobutton by returned id
        radioBtnSex = (RadioButton) findViewById(selectedSex);

        Map<String, Object> nestedNestedData = new HashMap<>();
        nestedNestedData.put("name", user.getDisplayName());
        nestedNestedData.put("profile_pic", "");
        nestedNestedData.put("relationship", mChildRelation.getText().toString());

        Map<String, Object> nestedData = new HashMap<>();
        nestedData.put(user.getUid(), nestedNestedData);

        Map<String, Object> docData = new HashMap<>();
        docData.put("name", mChildName.getText().toString());
        docData.put("profile_pic", "childs/"+cid+"_"+String.valueOf(currentTime));
        docData.put("age", childAge);
        docData.put("sex", radioBtnSex.getText().toString().substring(0,2));
        docData.put("code", randInt(0,9999));
        docData.put("users", nestedData);

        db.collection("childs").document()
                .set(docData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        db.collection("childs")
                                .whereGreaterThanOrEqualTo("users."+user.getUid()+".name", "")
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                cid = document.getId();

                                                Map<String, Object> childNestedData = new HashMap<>();
                                                childNestedData.put("name", mChildName.getText().toString());
                                                childNestedData.put("profile_pic", "childs/"+cid+"_"+String.valueOf(currentTime));
                                                childNestedData.put("age", childAge);
                                                childNestedData.put("sex", radioBtnSex.getText().toString().substring(0,2));
                                                childNestedData.put("code", randInt(0,9999));

                                                Map<String, Object> nestedData = new HashMap<>();
                                                nestedData.put(cid, childNestedData);

                                                Map<String, Object> userData = new HashMap<>();
                                                userData.put("name", user.getDisplayName());
                                                userData.put("childs", nestedData);

                                                db.collection("users").document(user.getUid())
                                                        .set(userData)
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                if(image != null) {
                                                                    childImageAdd();
                                                                } else {
                                                                    mProgressView.setVisibility(View.GONE);
                                                                    Intent intent=new Intent(ChildAddActivity.this,MainActivity.class);
                                                                    startActivity(intent);
                                                                    finish();
                                                                }
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                mChildAddButton.setEnabled(true);
                                                                mProgressView.setVisibility(View.GONE);
                                                                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                                                Toast toast = Toast.makeText(ChildAddActivity.this, "에러가 발생했습니다. 다시 한번 시도해주세요", Toast.LENGTH_SHORT);
                                                                toast.show();
                                                            }
                                                        });
                                            }
                                        } else {

                                        }
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
//                                Log.w(TAG, "Error writing document", e);
                        mChildAddButton.setEnabled(true);
                        mProgressView.setVisibility(View.GONE);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        Toast toast = Toast.makeText(ChildAddActivity.this, "에러가 발생했습니다. 다시 한번 시도해주세요", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                });
    }

    public static int randInt(int min, int max) {
        Random rand = new Random();
        return rand.nextInt((max - min) + 1) + min;
    }

    @Override
    protected void onActivityResult(int requestCode, final int resultCode, Intent data) {
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            // or get a single image only
            image = ImagePicker.getFirstImageOrNull(data);
            Bitmap myBitmap = BitmapFactory.decodeFile(image.getPath());

            try {
                ExifInterface exif = new ExifInterface(image.getPath());
                int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
                Matrix matrix = new Matrix();
                if (orientation == 6) {
                    matrix.postRotate(90);
                }
                else if (orientation == 3) {
                    matrix.postRotate(180);
                }
                else if (orientation == 8) {
                    matrix.postRotate(270);
                }
                myBitmap = Bitmap.createBitmap(myBitmap, 0, 0, myBitmap.getWidth(), myBitmap.getHeight(), matrix, true); // rotating bitmap
                childImage.setImageBitmap(myBitmap);
            }
            catch (Exception e) {

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ChildAddActivity.this, ChildRegisterActivity.class));
        finish();
        super.onBackPressed();
    }
}
