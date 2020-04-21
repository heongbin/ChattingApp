package com.example.chattingapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class SuccessActivity extends AppCompatActivity {
    private ArrayList<ChatData> chatlist;
    private String id;
    private String MyUid;
    private RecyclerView mRecyclerView;
    private RecyclerAdapter mRecyclerAdapter;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference MyRef;
    private EditText main_send_text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);
        chatlist = new ArrayList<ChatData>();


        mRecyclerView=findViewById(R.id.recyclerview_id);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        id=getIntent().getStringExtra("id"); //이메일 주소


        firebaseDatabase = FirebaseDatabase.getInstance();
        MyRef = firebaseDatabase.getReference().child("message");

        main_send_text = findViewById(R.id.send_message_edittextview);
        Button SendMessageButton = findViewById(R.id.send_message_id);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user != null){
            MyUid = user.getUid();//로그인 UID
            Log.d("ERROR",MyUid);
        }
        Log.d("error",MyUid);

        mRecyclerAdapter = new RecyclerAdapter(getApplicationContext(),chatlist);
        mRecyclerView.setAdapter(mRecyclerAdapter);




        SendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyRef.push().setValue(new ChatData(MyUid,null,id,main_send_text.getText().toString(),null));
                main_send_text.setText("");
            }
        });

        MyRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, @Nullable String s) {//데이터베이스에 데이터가 추가될때마다 변화되는것 정의
                ChatData value = dataSnapshot.getValue(ChatData.class);
                if (value!= null) {
                    Log.d("childlistenervalue", "chatuid:" + value.getuid());
                    chatlist.add(value);
                    mRecyclerAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        })
;





    }
}
