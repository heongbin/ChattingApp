package com.example.chattingapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FriendsListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FriendsListFragment extends Fragment {
    private RecyclerAdapter recyclerAdapter;
    private RecyclerView mRecyclerView;
    private ArrayList<ChatData> FriendList;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private String myuid;
    private ImageView Myproileimage;
    private TextView myemail;
    private View view;
    private String mytmpimg;
    private String mytmpname;
    private String MyName;
    private ArrayList<ChatData> User;
    private FirebaseDatabase firebaseDatabase;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FriendsListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FriendsListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FriendsListFragment newInstance(String param1, String param2) {
        FriendsListFragment fragment = new FriendsListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }





    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FriendList = new ArrayList<ChatData>();
        view = inflater.inflate(R.layout.fragment_friends_list,container,false);
        Context context = view.getContext();
        myemail = (TextView)view.findViewById(R.id.my_email_view);
        Myproileimage = (ImageView)view.findViewById(R.id.my_view_id);
        myuid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        mRecyclerView = view.findViewById(R.id.Recyclerview_friendlist_id);
        LinearLayoutManager linearLayout = new LinearLayoutManager(context);

        mRecyclerView.setLayoutManager(linearLayout);// 프래그먼트에서 리사이클러뷰 해결.
        recyclerAdapter = new RecyclerAdapter(context,FriendList);
        mRecyclerView.setAdapter(recyclerAdapter);
        //mRecyclerView.setLayoutManager(linearLayout);


        database = FirebaseDatabase.getInstance();
        myRef = database.getReference().child("users");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    ChatData chatData = snapshot.getValue(ChatData.class);
                    if(chatData.getuid().equals(myuid)){
                        mytmpimg=chatData.getImg();
                        mytmpname=chatData.getName();
                        continue;
                    }
                    Log.d("fragment_check",chatData.getuid());
                    FriendList.add(chatData);


                }

                for (int i=0;i<FriendList.size();i++){
                    Log.d("another_check",FriendList.get(i).getuid());

                }

                        myemail.setText(mytmpname);
                        if (mytmpimg==null){
                            Myproileimage.setImageResource(R.drawable.ic_account_box_black_24dp);

                        }
                        else{
                            Glide.with(view).load(mytmpimg).into(Myproileimage);
                        }






                recyclerAdapter.notifyDataSetChanged();
            }



            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        })

        // Inflate the layout for this fragment
        ;
        return view;
    }
}
