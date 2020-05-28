package com.example.chattingapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class SignupActivity extends AppCompatActivity {
    private int final_name;
    private int flag = 0;
    private Button check;
    private DatabaseReference databaseReference;
    private FirebaseDatabase database;
    private EditText email;
    private EditText name;
    private EditText password;
    private Button signup;
    private FirebaseAuth mAuth;
    private ImageView imageview;
    private DatabaseReference mRef;
    private ArrayList<ChatData> CheckList;
    private int buttoncheck=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        CheckList= new ArrayList<ChatData>();
        check = (Button) findViewById(R.id.id_check_button);
        imageview = (ImageView) findViewById(R.id.profile_image_view);

        email = (EditText) findViewById(R.id.signupactivity_edittext_email);
        name = (EditText) findViewById(R.id.signupactivity_edittext_name);
        password = (EditText) findViewById(R.id.signupactivity_edittext_password);
        signup = (Button) findViewById(R.id.signupactivity_button_signup);


        mRef = FirebaseDatabase.getInstance().getReference().child("users"); //유저정보 저장

        mRef.addValueEventListener(new ValueEventListener() {//고유 uid 체크
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ChatData chatdata = snapshot.getValue(ChatData.class);
                    CheckList.add(chatdata);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        })
        ;

            Glide.with(getApplicationContext()).load(R.drawable.ic_account_box_black_24dp).into(imageview);


        imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(intent, 4);
            }
        });

        //Glide.with(this).load().into(imageview);
        mAuth = FirebaseAuth.getInstance();

        final_name=0;
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttoncheck=1;
                Pattern ps = Pattern.compile("^[a-zA-Z0-9가-힣ㄱ-ㅎㅏ-ㅣ]*$");
                if (name.getText().toString().equals("")) {
                    Toast.makeText(getBaseContext(), "put letters", Toast.LENGTH_SHORT).show();
                }
                else if(name.getText().toString().length()<4){
                    Toast.makeText(getBaseContext(), "please put the name more", Toast.LENGTH_SHORT).show();
                    buttoncheck=0;
                }

                else if (!ps.matcher(name.getText().toString()).matches()) {
                    Toast.makeText(getApplicationContext(), "한글,영어,숫자가능", Toast.LENGTH_SHORT).show();
                    buttoncheck=0;

                } else {
                    int check = 0;
                    for (int i = 0; i < CheckList.size(); i++) {
                        if (CheckList.get(i).getName().equals(name.getText().toString())) {
                            Toast.makeText(getBaseContext(), "already name", Toast.LENGTH_SHORT).show();
                            final_name = 0;
                            check = 1;
                            break;
                        }

                    }
                    if (check == 0) {
                        final_name = 1;
                        Toast.makeText(getApplicationContext(), "correct name", Toast.LENGTH_SHORT).show();

                    }

                }
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (final_name == 1) { //name체크 성공!
                    flag = 0;
                    mAuth
                            .createUserWithEmailAndPassword(email.getText().toString().trim(), password.getText().toString().trim())
                            .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {//회원가입 성공시
                                        Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                                        //intent.putExtra("name",name.getText().toString());
                                        ChatData chatData = new ChatData(name.getText().toString(), task.getResult().getUser().getUid(), null, email.getText().toString(), 1);

                                        for (int i = 0; i < CheckList.size(); i++) {
                                            if (CheckList.get(i).getEmail().equals(chatData.getEmail())) { //이메일이 같으면,
                                                flag = 1;
                                                break;

                                            }

                                        }

                                        if (flag == 0) {
                                            mRef.push().setValue(chatData);
                                        }
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Toast.makeText(SignupActivity.this, "email already exsit or 비밀번호를 8자이상 입력해주세요.", Toast.LENGTH_LONG).show();
                                        return;
                                    }
                                }
                            })
                    ;
                } else {
                    if (name.getText().toString().equals("") || email.getText().toString().equals("") || password.getText().toString().equals("")) {
                        Toast.makeText(getApplicationContext(), "all section comlete please", Toast.LENGTH_SHORT).show();
                    } else {
                        if(buttoncheck==0){
                            Toast.makeText(getApplicationContext(),"check the name!!!!",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "name dupli", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 4) {
            if (requestCode == RESULT_OK) {
                String imgfinal = getpicture(data.getData());
                //Uri uri = data.getData();
                Glide.with(getApplicationContext()).load(imgfinal).apply(RequestOptions.circleCropTransform()).into(imageview);


            }



        }
    }

    private String getpicture(Uri data) {
        int index = 0;
        String[] proj = {MediaStore.Images.Media.DATA};

        Cursor cursor = getContentResolver().query(data, proj, null, null, null);
        if (cursor == null) {
            Toast.makeText(getApplicationContext(), "picture select", Toast.LENGTH_SHORT);


        } else if (cursor.moveToFirst()) {
            index = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
            String imgPath = cursor.getString(index);
            cursor.close();
            return imgPath;
        }


        return "";
    }



}
