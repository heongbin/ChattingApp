package com.example.chattingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private EditText edit_id;
    private EditText edit_password;
    FirebaseAuth  firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edit_id = (EditText)findViewById(R.id.edit_id);
        edit_password = (EditText)findViewById(R.id.edit_password);



        firebaseAuth = FirebaseAuth.getInstance();



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
                String id = edit_id.getText().toString();
                String password = edit_password.getText().toString();

                firebaseAuth.signInWithEmailAndPassword(id,password)
                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()) {
                                    Intent intent = new Intent(MainActivity.this, SuccessActivity.class);
                                    intent.putExtra("id",edit_id.getText().toString());
                                    startActivity(intent);
                                    finish();
                                }else{
                                    Toast.makeText(MainActivity.this,"login fail",Toast.LENGTH_SHORT).show();

                                }

                            }
                        })
;
            }
        });
    }
}
