package com.example.chattingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private String Myname;
    private ArrayList<ChatData> CheckList;
    private int flag;
    private EditText edit_id;
    private EditText edit_password;
    FirebaseAuth  firebaseAuth;
    private FirebaseUser mUser;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private String Name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CheckList = new ArrayList<ChatData>();
        edit_id = (EditText)findViewById(R.id.edit_id);
        edit_password = (EditText)findViewById(R.id.edit_password);
        //Name= getIntent().getStringExtra("name");


        firebaseAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();




        Button button_signup = (Button)findViewById(R.id.button_signup);
        button_signup.setOnClickListener(new View.OnClickListener() { //회원가입 버튼 누를때
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,SignupActivity.class));
            }
        });

        Button login_button = (Button)findViewById(R.id.button_login);
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edit_id.getText().toString().equals("")||edit_password.getText().toString().equals("")){
                    Toast.makeText(getBaseContext(), "put the email and password!!", Toast.LENGTH_SHORT).show();
                }
                else {
                    String id = edit_id.getText().toString();
                    String password = edit_password.getText().toString();
                    firebaseAuth.signInWithEmailAndPassword(id, password)
                            .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) { //로그인 성공 현재 내가짠 로직은 로그인 버튼을 눌러야만 목록에 저장이됨 만약 회원가입을하고 로그인패스하고 다른아이디로 회원가입을 바로하면 error
                                        Intent intent = new Intent(MainActivity.this, TabMenuActivity.class);


                                        intent.putExtra("id", edit_id.getText().toString());
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Toast.makeText(MainActivity.this, "login fail", Toast.LENGTH_SHORT).show();

                                    }

                                }
                            })
                    ;
                }
            }

        });
    }



}
