package com.geniauti.geniauti;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static com.geniauti.geniauti.MainActivity.storage;
import static com.geniauti.geniauti.MainActivity.storageRef;
import static java.lang.Integer.parseInt;

public class ChildEditActivity extends AppCompatActivity {

    private EditText mChildName;
    private EditText mChildAge;
    private EditText mChildRelationship;
    private RadioGroup radioGroupAge, radioGroupSex;
    private RadioButton radioMonth, radioYear;
    private RadioButton radioBoy, radioGirl;
    private RadioButton radioBtnAge;

    private View mProgressView;
    private Button childEditButton;
    private RelativeLayout childImageLayout;
    private ImageView childImage;
    private TextView mChildAgeText;

    private Image image = null;
    private UploadTask uploadTask;

    private long currentTime;
    private String childName;
    private String childYear;
    private String childAge;
    private int childAgeNumber;
    private String childSex;
    private RadioButton radioBtnSex;
    private String relationship;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_edit);

        // toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.child_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("아이 정보 수정");

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        mChildName = (EditText) findViewById(R.id.edit_child_name);
        mChildAge = (EditText) findViewById(R.id.edit_child_age);
        mChildRelationship = (EditText) findViewById(R.id.edit_child_relationship);
        mChildAgeText = (TextView) findViewById(R.id.edit_child_age_txt);

        radioGroupAge = (RadioGroup) findViewById(R.id.edit_radio_child_age);
        radioGroupAge.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                if(checkedId == R.id.edit_radio_btn_month) {
                    mChildAgeText.setText("아이 나이(개월)");
                } else {
                    mChildAgeText.setText("아이 나이(세)");
                }
            }
        });

        radioMonth = (RadioButton) findViewById(R.id.edit_radio_btn_month);
        radioYear = (RadioButton) findViewById(R.id.edit_radio_btn_year);

        radioGroupSex = (RadioGroup) findViewById(R.id.edit_radio_child_sex);

        radioBoy = (RadioButton) findViewById(R.id.edit_radio_btn_boy);
        radioGirl = (RadioButton) findViewById(R.id.edit_radio_btn_girl);

        childImageLayout = (RelativeLayout) findViewById(R.id.child_edit_image_layout);
        childImageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.create(ChildEditActivity.this)
                        .limit(1)
                        .theme(R.style.AppTheme)
                        .start();
            }
        });

        childImage = (ImageView) findViewById(R.id.child_edit_image);

        mProgressView = findViewById(R.id.child_edit_progress);
        mProgressView.bringToFront();
        mProgressView.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        MainActivity.db.collection("childs")
                .whereGreaterThanOrEqualTo("users."+MainActivity.user.getUid()+".name", "")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                childName = document.getData().get("name").toString();
                                mChildName.setText(childName);

                                if(parseInt(document.getData().get("age").toString()) % 12 == 0) {
                                    radioYear.setChecked(true);
                                    int age = Integer.parseInt(document.getData().get("age").toString()) / 12;

                                    childYear = "세";
                                    childAge = String.valueOf(age);

                                    mChildAge.setText(childAge);
                                } else {
                                    radioMonth.setChecked(true);
                                    childYear = "개월";
                                    childAge = document.getData().get("age").toString();

                                    mChildAge.setText(childAge);
                                }

                                childSex = document.getData().get("sex").toString();
                                if(childSex.equals("남자")) {
                                    radioBoy.setChecked(true);
                                } else {
                                    radioGirl.setChecked(true);
                                }

                                HashMap<String, Object> child = (HashMap<String, Object>)  document.getData().get("users");
                                HashMap<String, Object> users = (HashMap<String, Object>)  child.get(MainActivity.user.getUid());

                                relationship = users.get("relationship").toString();
                                mChildRelationship.setText(relationship);

                                StorageReference pathReference = storageRef.child("childs/"+MainActivity.cid);
                                pathReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        // Got the download URL for 'users/me/profile.png'
                                        Glide.with(getApplication()).load(uri).into(childImage);
                                        mProgressView.setVisibility(View.GONE);
                                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception exception) {
                                        // Handle any errors
                                        mProgressView.setVisibility(View.GONE);
                                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                    }
                                });

                            }
                        } else {
//                                Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        childEditButton = (Button) findViewById(R.id.child_edit_button);
        childEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                childEditButton.setEnabled(false);
                mProgressView.setVisibility(View.VISIBLE);
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                // Reset errors.
                mChildName.setError(null);
                mChildAge.setError(null);
                mChildRelationship.setError(null);

                boolean cancel = false;

                if(TextUtils.isEmpty(mChildName.getText().toString())) {
                    mChildName.setError("아이 이름을 입력해주세요.");
                    cancel = true;
                }

                if(TextUtils.isEmpty(mChildAge.getText().toString())) {
                    mChildAge.setError("아이 나이를 입력해주세요.");
                    cancel = true;
                }

                if(TextUtils.isEmpty(mChildRelationship.getText().toString())) {
                    mChildRelationship.setError("아이와의 관계를 입력해주세요.");
                    cancel = true;
                }

                if(cancel){
                    childEditButton.setEnabled(true);
                    mProgressView.setVisibility(View.GONE);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                } else {
                    if(image != null) {
                        childImageEdit();
                    } else {

                        int selectedAge = radioGroupAge.getCheckedRadioButtonId();
                        // find the radiobutton by returned id
                        radioBtnAge = (RadioButton) findViewById(selectedAge);

                        int selectedSex = radioGroupSex.getCheckedRadioButtonId();
                        // find the radiobutton by returned id
                        radioBtnSex = (RadioButton) findViewById(selectedSex);

                        if(!relationship.equals(mChildRelationship.getText().toString()) || !childName.equals(mChildName.getText().toString()) || !childAge.equals(mChildAge.getText().toString())
                                || !childYear.equals(radioBtnAge.getText().toString()) || !childSex.equals(radioBtnSex.getText().toString().substring(0,2))) {
                            childInfoEdit();
                        } else {
                            childEditButton.setEnabled(true);
                            mProgressView.setVisibility(View.GONE);
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            Toast toast = Toast.makeText(ChildEditActivity.this, "아이 정보를 수정해주세요.", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }
                }
            }
        });

    }

    private void childImageEdit() {

        Uri file = Uri.fromFile(new File(image.getPath()));
        StorageReference riversRef = MainActivity.storageRef.child("childs/"+MainActivity.cid);
        uploadTask = riversRef.putFile(file);

        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                childEditButton.setEnabled(true);
                mProgressView.setVisibility(View.GONE);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                Toast toast = Toast.makeText(ChildEditActivity.this, "오류가 발생했습니다. 다시 한번 시도해주세요.", Toast.LENGTH_SHORT);
                toast.show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                childInfoEdit();
            }
        });
    }

    private void childInfoEdit() {

//        Map<String, Object> nestedNestedData = new HashMap<>();
//        nestedNestedData.put("name", MainActivity.user.getDisplayName());
//        nestedNestedData.put("profile_pic", "");
//        nestedNestedData.put("relationship", mChildRelationship.getText().toString());

//        Map<String, Object> nestedData = new HashMap<>();
//        nestedData.put(MainActivity.user.getUid(), nestedNestedData);

//        Map<String, Object> docData = new HashMap<>();
//        docData.put("name", mChildName.getText().toString());
//        docData.put("profile_pic", "childs/"+MainActivity.cid+"_"+String.valueOf(currentTime));
//        docData.put("age", childAge);
//        docData.put("sex", radioBtnSex.getText().toString().substring(0,2));
//        docData.put("users", nestedData);

        final DocumentReference sfDocRef = MainActivity.db.collection("childs").document(MainActivity.cid);
        MainActivity.db.runTransaction(new Transaction.Function<Void>() {
            @Override
            public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                DocumentSnapshot snapshot = transaction.get(sfDocRef);
                Map<String, Object> childsMap = snapshot.getData();

                // relationship edit
                if(!relationship.equals(mChildRelationship.getText())) {
                    Map<String, Object> parentsMap = (Map<String, Object>) childsMap.get("users");
                    Map<String, Object> myProfileMap = (Map<String, Object>) parentsMap.get(MainActivity.user.getUid());
                    myProfileMap.put("relationship", mChildRelationship.getText().toString());
                    parentsMap.put(MainActivity.user.getUid(), myProfileMap);
                    childsMap.put("users", parentsMap);
                }

                if(!childName.equals(mChildName.getText())) {
                    childsMap.put("name", mChildName.getText().toString());
                }

                if(image != null) {
                    currentTime = System.currentTimeMillis();
                    childsMap.put("profile_pic", "childs/"+MainActivity.cid+"_"+String.valueOf(currentTime));
                }

                if(!childAge.equals(mChildAge.getText().toString()) || !childYear.equals(radioBtnAge.getText().toString())) {
                    childAgeNumber = parseInt(mChildAge.getText().toString());

                    if(radioBtnAge.getText().toString().equals("세")) {
                        childAgeNumber = parseInt(mChildAge.getText().toString()) * 12;
                    }

                    childsMap.put("age", childAgeNumber);
                }

                if(!childSex.equals(radioBtnSex.getText().toString().substring(0,2))) {
                    childsMap.put("sex", radioBtnSex.getText().toString().substring(0,2));
                }

                transaction.update(sfDocRef, childsMap);

                // Success
                return null;
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                if(!relationship.equals(mChildRelationship.getText())) {

                    // behavior edit
                    mProgressView.setVisibility(View.GONE);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    finish();
                    Toast toast = Toast.makeText(ChildEditActivity.this, "아이 정보가 수정되었습니다.", Toast.LENGTH_SHORT);
                    toast.show();


                } else {
                    mProgressView.setVisibility(View.GONE);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    finish();
                    Toast toast = Toast.makeText(ChildEditActivity.this, "아이 정보가 수정되었습니다.", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        childEditButton.setEnabled(true);
                        mProgressView.setVisibility(View.GONE);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        Toast toast = Toast.makeText(ChildEditActivity.this, "오류가 발생했습니다. 다시 한번 시도해주세요.", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                });


//        MainActivity.db.collection("childs").document(MainActivity.cid)
//                .update(docData)
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//
//                        Map<String, Object> childNestedData = new HashMap<>();
//                        childNestedData.put("name", mChildName.getText().toString());
//                        childNestedData.put("profile_pic", "childs/"+MainActivity.cid+"_"+String.valueOf(currentTime));
//                        childNestedData.put("age", childAge);
//                        childNestedData.put("sex", radioBtnSex.getText().toString().substring(0,2));
//
//                        Map<String, Object> nestedData = new HashMap<>();
//                        nestedData.put(MainActivity.cid, childNestedData);
//
//                        Map<String, Object> userData = new HashMap<>();
//                        userData.put("childs", nestedData);
//                        userData.put("relationship", mChildRelationship.getText().toString());
//
//                        MainActivity.db.collection("users").document(MainActivity.user.getUid())
//                                .update(userData)
//                                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                    @Override
//                                    public void onSuccess(Void aVoid) {
//
//                                        if(image != null) {
//                                            childImageEdit();
//                                        } else {
//                                            mProgressView.setVisibility(View.GONE);
//                                            finish();
//                                            Toast toast = Toast.makeText(ChildEditActivity.this, "아이 정보가 수정되었습니다.", Toast.LENGTH_SHORT);
//                                            toast.show();
//                                        }
//                                    }
//                                })
//                                .addOnFailureListener(new OnFailureListener() {
//                                    @Override
//                                    public void onFailure(@NonNull Exception e) {
//                                        childEditButton.setEnabled(true);
//                                        mProgressView.setVisibility(View.GONE);
//                                    }
//                                });
////                                Log.d(TAG, "DocumentSnapshot successfully written!");
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
////                                Log.w(TAG, "Error writing document", e);
//                        childEditButton.setEnabled(true);
//                        mProgressView.setVisibility(View.GONE);
//                    }
//                });
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
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
