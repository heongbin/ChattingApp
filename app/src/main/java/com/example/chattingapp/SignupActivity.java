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

public class SignupActivity extends AppCompatActivity {
    private EditText email;
    private EditText name;
    private EditText password;
    private Button signup;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);



        email=(EditText)findViewById(R.id.signupactivity_edittext_email);
        name=(EditText)findViewById(R.id.signupactivity_edittext_name);
        password=(EditText)findViewById(R.id.signupactivity_edittext_password);
        signup=(Button)findViewById(R.id.signupactivity_button_signup);

        mAuth=FirebaseAuth.getInstance();
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth
                        .createUserWithEmailAndPassword(email.getText().toString().trim(),password.getText().toString().trim())
                        .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){//회원가입 성공시
                                    Intent intent = new Intent(SignupActivity.this,MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                                else{
                                    Toast.makeText(SignupActivity.this,"fail",Toast.LENGTH_LONG).show();
                                    return;
                                }
                            }
                        })
                ;
            }
        });



    }
}
