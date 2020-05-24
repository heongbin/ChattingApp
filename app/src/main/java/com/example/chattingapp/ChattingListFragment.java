package com.example.chattingapp;

import android.content.Context;
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
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChattingListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChattingListFragment extends Fragment {
    private String lastname;
    private ArrayList<String> lastcomments;
    private HashMap<String,String> map;
    private String lastcomment;
    private ArrayList<ChatData> tmplist;
    private ArrayList<ChatData> MyChattingList;
    private ArrayList<ChatData> UserDetaildata;
    private ArrayList<ChattingListData> MyChattinglistdata;
    private DatabaseReference ChattingRoomKey;
    private ArrayList<UserDataWithKey> DataWithKey;//현재 사용자와 채팅한 기록이있는 유저들의 이름과 그 고유채팅방 키를 담는 리스트.
    private DatabaseReference Chatowner =FirebaseDatabase.getInstance().getReference().child("Chatownerlist").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
    private String Opponetuser;
    private String MyName;
    private ArrayList<String> Chatkeylist;
    private ArrayList<Users> Userslist;
    private ArrayList<ChatData> chattingroomlist;
    private DatabaseReference MyFref;
    private DatabaseReference MyChat;
    private String Chatkey;
    private String Myuid = FirebaseAuth.getInstance().getCurrentUser().getUid(); //현재 사용자의 아이디.
    private DatabaseReference Myref;
    private FirebaseDatabase firebaseDatabase;
    private RecyclerAdapter recyclerAdapter;
    private RecyclerView mRecyclerview;
    private View view;
    private ArrayList<ChatData> FriendList;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ChattingListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChattingListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChattingListFragment newInstance(String param1, String param2) {
        ChattingListFragment fragment = new ChattingListFragment();
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
        // Inflate the layout for this fragment
        firebaseDatabase = FirebaseDatabase.getInstance();

        view = inflater.inflate(R.layout.fragment_chatting_list,container,false);
        Context context =view.getContext();
        mRecyclerview = view.findViewById(R.id.chatting_list_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        mRecyclerview.setLayoutManager(linearLayoutManager);
        MyChattingList = new ArrayList<ChatData>();
        recyclerAdapter = new RecyclerAdapter(context,MyChattingList);
        mRecyclerview.setAdapter(recyclerAdapter);

        lastcomments = new ArrayList<String>();
        map = new HashMap<String,String>();
        UserDetaildata = new ArrayList<ChatData>();
        Userslist = new ArrayList<Users>();
        chattingroomlist = new ArrayList<ChatData>();
        Chatkeylist = new ArrayList<String>();
        DataWithKey = new ArrayList<UserDataWithKey>();



        Myref = firebaseDatabase.getReference().child("chattingroomid"); //채팅룸목록 확인하기
        MyFref = firebaseDatabase.getReference().child("users"); //내이름확인.


        MyFref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    ChatData chatData = snapshot.getValue(ChatData.class);
                    Userslist.add(new Users(chatData.getuid(),chatData.getName())); //이중에서 myuid와 같은 네임을 찾고 그게 현재 사용자 이름.
                    UserDetaildata.add(chatData);
                }

                for (int i=0;i<Userslist.size();i++){
                    if (Userslist.get(i).getUid().equals(Myuid)){
                        MyName=Userslist.get(i).getName();
                        break;
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        })
        ;



        Myref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    String chatkey = snapshot.getKey();
                    ChatData chatData = snapshot.getValue(ChatData.class);
                    //chattingroomlist.add(new ChatData(chatData.getKey(),chatData.getnamea(),chatData.getnameb())); //사용자이름을userlist로 찾고 현재 사용자의 상대방목록과 그해당 채팅방키를 얻어옴.
                    if(chatData.getnamea().equals(MyName)){
                        DataWithKey.add(new UserDataWithKey(chatkey,chatData.getnameb()));
                    }
                    else if (chatData.getnameb().equals(MyName)){
                        DataWithKey.add(new UserDataWithKey(chatkey,chatData.getnamea()));
                    }

                }

                //for(int i=0;i<chattingroomlist.size();i++){
                  //  if (chattingroomlist.get(i).getnamea().equals(MyName)){//채팅룸 정보가 현재사용자의 uid를 포함하고있다면면
                    //    DataWithKey.add(new UserDataWithKey(chattingroomlist.get(i).getKey(),chattingroomlist.get(i).getnameb()));
                    //}
                    //else if (chattingroomlist.get(i).getnameb().equals(MyName)){
                      //  DataWithKey.add(new UserDataWithKey(chattingroomlist.get(i).getKey(),chattingroomlist.get(i).getnamea()));
                    //}
                //}


            for (int i=0;i<DataWithKey.size();i++ ) {//현재 사용자와 채팅하는 사람들의 유저이름과 그 고유 채팅방키를 가져옴
                lastcomment = null;
                String opponentimg = null;
                final String opponentname;


                Log.d("dat!!", DataWithKey.get(i).getOpponentName());

                tmplist = new ArrayList<ChatData>();
                Log.d("datawithkey:", DataWithKey.get(i).getChattingRoomKey());
                for (int j = 0; j < UserDetaildata.size(); j++) { //상대방의 이미지 얻어오기
                    if (UserDetaildata.get(j).getName().equals(DataWithKey.get(i).getOpponentName())) {
                        opponentimg = UserDetaildata.get(j).getImg();
                        break;
                    }
                }

                opponentname = DataWithKey.get(i).getOpponentName();


                Log.d("Chatowner_check", MyName);


                //Log.d("roomchat!!last",lastcomment);
                //MyChattingList.add(new ChattingListData(lastcomment,opponentname,opponentimg));
                //!Chatowner.setValue(new ChatData(null,opponentname,lastcomment,2));
                //Chatowner = firebaseDatabase.getReference().child("Chatownerlist").child(Myuid).child(DataWithKey.get(i).getOpponentName()); //각자 사용자가 가지는 채팅목록들 저장. 채팅상대의 이름과 프로필사진 그리고 그대화의 마지막대화.
                //현재 사용자와 대화기록이 있는 사람의 이름을 표시.


                FirebaseDatabase.getInstance().getReference().child("Chatownerlist").child(Myuid).child(DataWithKey.get(i).getOpponentName()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Log.d("whatis!!!", snapshot.getKey().toString());
                            //ChatData chatData =snapshot.getValue(ChatData.class);
                            map.put(snapshot.getKey(),snapshot.getValue().toString());


                        }
                        int flag=0;
                        try {
                            for (int i = 0; i < MyChattingList.size(); i++) {
                                if (MyChattingList.get(i).getName().equals(map.get("name"))) {
                                    MyChattingList.get(i).setMaincontent(map.get("maincontent"));
                                    flag = 1;

                                }

                            }
                        }catch (Exception e){

                        }

                        if (flag==0) {
                            if (map.get("name")!=null) {
                                MyChattingList.add(new ChatData(null, map.get("name"), map.get("maincontent"), 2));
                            }
                        }
                        //Log.d("lastcomment!!",map.get("lastcomment"));


                        //Log.d("chek_mychattinglist",MyChattingList.get(MyChattingList.size()-1).getMaincontent());
                        recyclerAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                })
                ;

                //Chatowner.child("name").setValue(opponentname);
                //Chatowner.child("viewtype").setValue(2);
                //Chatowner.child("lastcomment").setValue(lastcomments.get(-1).toString());

                //ChattingRoomKey = FirebaseDatabase.getInstance().getReference().child("chattingroom").child(DataWithKey.get(i).getChattingRoomKey()); //현재사용자와 대화한 대화기록있는 채팅방의 키를 얻어와 채팅방 대화내용을 얻어옴.

                final int finalI = i;
                FirebaseDatabase.getInstance().getReference().child("chattingroom").child(DataWithKey.get(i).getChattingRoomKey()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            ChatData chatData = snapshot.getValue(ChatData.class);

                            lastcomment = chatData.getMaincontent();

                        }
                        //Chatowner.child("lastcomment").setValue(lastcomment);
                        //Chatowner.setValue(new ChatData(null, opponentname, lastcomment, 2));
                        FirebaseDatabase.getInstance().getReference().child("Chatownerlist").child(Myuid).child(DataWithKey.get(finalI).getOpponentName()).setValue(new ChatData(null, opponentname, lastcomment, 2));
                        lastcomment="";

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                })
                ;



            }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        })
        ;


return view;
    }

}
