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
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.Map;

public class ProfileEditActivity extends AppCompatActivity {

    private UploadTask uploadTask;

    private ImageView profileImage;
    private EditText profileName;
    private EditText profileEmail;
    private RelativeLayout passwordEdit;
    private Button btnProfileEdit;
    private String newName, newEmail;

    private RelativeLayout profileEditLayout;
    private Image image = null;
    private Bitmap myBitmap;

    private long currentTime;

    private View mProgressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        // toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.profile_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("내 정보 수정");

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        mProgressView = findViewById(R.id.profile_edit_progress);
        mProgressView.bringToFront();
        mProgressView.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        profileImage = (ImageView) findViewById(R.id.profile_edit_image);

        StorageReference pathReference = MainActivity.storageRef.child("users/"+MainActivity.user.getUid());
        pathReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'
                Glide.with(getApplication()).load(uri).into(profileImage);
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

        profileEditLayout = (RelativeLayout) findViewById(R.id.profile_edit_image_layout);
        profileEditLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.create(ProfileEditActivity.this)
                        .limit(1)
                        .theme(R.style.AppTheme)
                        .start();
            }
        });

        profileName = (EditText) findViewById(R.id.txt_profile_name);
        profileEmail = (EditText) findViewById(R.id.txt_profile_email);
        profileEmail.setText(MainActivity.user.getEmail());
        profileName.setText(MainActivity.user.getDisplayName());

        btnProfileEdit = (Button) findViewById(R.id.profile_edit_button);
        btnProfileEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                btnProfileEdit.setEnabled(false);
                mProgressView.setVisibility(View.VISIBLE);
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                // Reset errors.
                profileName.setError(null);
                profileEmail.setError(null);

                boolean cancel = false;

                newName = profileName.getText().toString();
                newEmail = profileEmail.getText().toString();

                if (TextUtils.isEmpty(newName)) {
                    profileName.setError("이름을 입력해주세요.");
                    cancel = true;
                }

                if (TextUtils.isEmpty(newName)) {
                    profileEmail.setError("이메일 주소를 입력해주세요.");
                    cancel = true;
                }

                if (!isEmailValid(newEmail)) {
                    profileEmail.setError("잘못된 이메일 형식입니다.");
                    cancel = true;
                }

                if(cancel) {
                    btnProfileEdit.setEnabled(true);
                    mProgressView.setVisibility(View.GONE);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                } else {
                    if(!newEmail.equals(MainActivity.user.getEmail())) {
                        emailChange();
                    } else {
                        if(image != null) {
                            imageChange();
                        } else {
                            if(!newName.equals(MainActivity.user.getDisplayName())) {
                                nameChange();
                            } else {
                                btnProfileEdit.setEnabled(true);
                                mProgressView.setVisibility(View.GONE);
                                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                Toast toast = Toast.makeText(ProfileEditActivity.this, "내 정보를 수정해주세요.", Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        }
                    }
                }
            }
        });

        passwordEdit = (RelativeLayout) findViewById(R.id.password_edit);
        passwordEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileEditActivity.this, PasswordEditActivity.class));
            }
        });

    }

    private void imageChange() {
        Uri file = Uri.fromFile(new File(image.getPath()));
        StorageReference riversRef = MainActivity.storageRef.child("users/"+MainActivity.user.getUid());
        uploadTask = riversRef.putFile(file);

        currentTime = System.currentTimeMillis();

        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                btnProfileEdit.setEnabled(false);
                mProgressView.setVisibility(View.GONE);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                Toast toast = Toast.makeText(ProfileEditActivity.this, "에러가 발생했습니다. 다시 한번 시도해주세요.", Toast.LENGTH_SHORT);
                toast.show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.

                MainActivity.db.collection("childs").document(MainActivity.cid)
                        .update("users."+MainActivity.user.getUid()+".profile_pic", "childs/"+MainActivity.cid+"_"+String.valueOf(currentTime))
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                if(!newName.equals(MainActivity.user.getDisplayName())){
                                    nameChange();
                                } else {
                                    mProgressView.setVisibility(View.GONE);
                                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                    finish();
                                    Toast toast = Toast.makeText(ProfileEditActivity.this, "프로필 정보가 수정되었습니다.", Toast.LENGTH_SHORT);
                                    toast.show();
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                btnProfileEdit.setEnabled(true);
                                mProgressView.setVisibility(View.GONE);
                                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                Toast toast = Toast.makeText(ProfileEditActivity.this, "에러가 발생했습니다. 다시 한번 시도해주세요.", Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        });
            }
        });
    }

    private void nameChange() {
        // users(ok)
        MainActivity.db.collection("users").document(MainActivity.user.getUid())
                .update("name", newName)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        // childs(ok)
                        MainActivity.db.collection("childs").document(MainActivity.cid)
                                .update("users."+MainActivity.user.getUid()+".name", newName)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        // admin(ok)
                                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                .setDisplayName(newName)
                                                .build();


                                        MainActivity.user.updateProfile(profileUpdates)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {

                                                            // behaviors name edit


                                                        } else {
                                                            btnProfileEdit.setEnabled(true);
                                                            mProgressView.setVisibility(View.GONE);
                                                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                                            Toast toast = Toast.makeText(ProfileEditActivity.this, "에러가 발생했습니다. 다시 한번 시도해주세요.", Toast.LENGTH_SHORT);
                                                            toast.show();
                                                        }
                                                    }
                                                });

                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        btnProfileEdit.setEnabled(true);
                                        mProgressView.setVisibility(View.GONE);
                                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                        Toast toast = Toast.makeText(ProfileEditActivity.this, "에러가 발생했습니다. 다시 한번 시도해주세요.", Toast.LENGTH_SHORT);
                                        toast.show();
                                    }
                                });

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        btnProfileEdit.setEnabled(true);
                        mProgressView.setVisibility(View.GONE);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        Toast toast = Toast.makeText(ProfileEditActivity.this, "에러가 발생했습니다. 다시 한번 시도해주세요.", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                });
    }

    private void emailChange() {
        MainActivity.user.updateEmail(newEmail)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            if(image != null) {
                                imageChange();
                            } else {
                                if(!newName.equals(MainActivity.user.getDisplayName())) {
                                    nameChange();
                                } else {
                                    mProgressView.setVisibility(View.GONE);
                                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                    finish();
                                    Toast toast = Toast.makeText(ProfileEditActivity.this, "프로필 정보가 수정되었습니다.", Toast.LENGTH_SHORT);
                                    toast.show();
                                }
                            }
                        } else {
                            btnProfileEdit.setEnabled(true);
                            mProgressView.setVisibility(View.GONE);
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            Toast toast = Toast.makeText(ProfileEditActivity.this, "이미 등록되어 있는 이메일입니다. 새로운 이메일을 입력해주세요.", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }
                });
    }



    @Override
    protected void onActivityResult(int requestCode, final int resultCode, Intent data) {
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            // or get a single image only
            image = ImagePicker.getFirstImageOrNull(data);
            myBitmap = BitmapFactory.decodeFile(image.getPath());

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
                profileImage.setImageBitmap(myBitmap);
            }
            catch (Exception e) {

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
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
