package com.example.chattingapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Path;
import android.icu.text.RelativeDateTimeFormatter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList; //4/16일 내채팅,상대채팅말고 중간 채팅 만들기!! 뷰홀더에 적용하기!!

import static androidx.core.content.ContextCompat.startActivity;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private String tmp;
    private String MyEmail;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference Myref;
    private DatabaseReference Myfref;
    private DatabaseReference MyChattingids;
    private String MyName;
    private ArrayList<Integer> chatidlist;
    private String Chatting_key;
    private ArrayList<ChatData> User;
    private ArrayList<ChatData> chatids=new ArrayList<ChatData>();
    private ArrayList<ChatData> chatkeyandnames = new ArrayList<ChatData>();
    private ArrayList<ChattingListData> ChattingRoomList;
    private String tmpkey;
    DatabaseReference MyCref;
    DatabaseReference MyCfref;

    Context mycontext;//4/12 context와 view에대해 공부하기. context는 abstract클래스로 어플리케이션의 자원이나 클래스에 접근할수 있게해줌. 지금객체가 어떤 activity에 있나 자신의 위치를 알려주는 역할.
    private String mProfileUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private ArrayList<ChatData> myChatDataList;



    RecyclerAdapter(Context context,ArrayList<ChatData> chatdatalist){
        this.mycontext = context;
        this.myChatDataList = chatdatalist;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        mycontext = parent.getContext();
        LayoutInflater inflater = (LayoutInflater)mycontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);//레이아웃 인플레이터는 xml에 정해둔틀을 실제 메모리에 올려놓는함수
        //layoutinflater class는 전체화면이아닌 일부분만차지하는 화면구성요소들을 xml레이아웃에서 보여줌.
        //즉 인플레이터는 xml에 정의된 resource를 view객체로 반환함.
        if (viewType==0){
            view = inflater.inflate(R.layout.recycler_detail,parent,false); //parent 레이아웃에 카드뷰채팅레이아웃을 적용시킴.
            return new LeftViewHolder(view);

        }


        else if (viewType==1) {
            view = inflater.inflate(R.layout.my_chatting_detail, parent, false);
            return new RightViewHolder(view); //뷰홀더는 각뷰를 보관하는 홀더객체, 뷰를 저장했다가 재활용할수있음. 이게 아니면 매번 리스트뷰 새로나올떄마다 findviewbyid로 생성해야함. 메모리문제.

        }
        else if(viewType==3){//친구목록 뷰타입
            view = inflater.inflate(R.layout.friend_list_detail,parent,false);
            return new FriendListViewHolder(view);
        }
        else if(viewType==4){
            view = inflater.inflate(R.layout.chatting_list_detail,parent,false);
            return new ChattingListViewHolder(view);
        }
        else{
            view = inflater.inflate(R.layout.recycler_empty_chat_view,parent,false);
            return new MiddleViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) { //내채팅,상대채팅에따라 홀더를 적용해줌 onbind는 생성된 뷰를 뷰홀더를 통헤 각자 데이터클래스를 뷰와 연결해줌.
        ChatData chatdata = myChatDataList.get(position);
        if (holder instanceof FriendListViewHolder){//친구목록이때

            FriendListViewHolder friendListViewHolder = (FriendListViewHolder)holder;
            if(chatdata.getImg()==null){
                friendListViewHolder.FriendListImg.setImageResource(R.drawable.ic_account_box_black_24dp);
            }
            else{
                Glide.with(mycontext).load(chatdata.getImg()).into(friendListViewHolder.FriendListImg);
            }
            friendListViewHolder.FriendListName.setText(chatdata.getName());

        }
        else if(holder instanceof  ChattingListViewHolder){
            ChattingListViewHolder chattingListViewHolder = (ChattingListViewHolder)holder;
            chattingListViewHolder.ChattingListTime.setText(chatdata.getTime());
            if(chatdata.getImg()==null){
                chattingListViewHolder.ChattingListImg.setImageResource(R.drawable.ic_account_box_black_24dp);
            }
            else{
                Glide.with(mycontext).load(chatdata.getImg()).into(chattingListViewHolder.ChattingListImg);
            }
            chattingListViewHolder.ChattingListName.setText(chatdata.getName());//상대방 이름표시.
            chattingListViewHolder.ChattingListLastComment.setText(chatdata.getMaincontent());



        }

        else {
            if (chatdata.getuid() != null) {
                if (chatdata.getuid().equals(mProfileUid)) {//사용자가 채팅한거면
                    RightViewHolder rightholder = (RightViewHolder) holder;
                    rightholder.RightTimeView.setText(chatdata.getTime());
                    rightholder.RightTitleView.setText(chatdata.getName());
                    if (chatdata.getImg() == null) {//프로필사진이x면
                        rightholder.RightImagerView.setImageResource(R.drawable.ic_account_box_black_24dp);
                    } else {
                        Glide.with(mycontext).load(chatdata.getImg()).into(rightholder.RightImagerView);// 4/12 glide라이브러리 implement하기부터
                    }
                    if (chatdata.getMaincontent() == null) {
                        rightholder.RightMainContextView.setText("no find");
                    } else {
                        rightholder.RightMainContextView.setText(chatdata.getMaincontent());
                    }

                } else { //상대방이 채팅한거면
                    LeftViewHolder leftholder = (LeftViewHolder) holder;
                    leftholder.LeftTitleView.setText(chatdata.getName());
                    if (chatdata.getImg()==null) {//프로필사진이x면
                        leftholder.LeftImagerView.setImageResource(R.drawable.ic_account_box_black_24dp);
                    } else {
                        Glide.with(mycontext).load(chatdata.getImg()).into(leftholder.LeftImagerView);// 4/12 glide라이브러리 implement하기부터
                    }
                    leftholder.LeftMainContextView.setText(chatdata.getMaincontent());
                    leftholder.LeftTimeView.setText(chatdata.getTime());

                }

            }
        }
    }

    @Override//채팅창처럼 여러가지 뷰를 쓸경우 뷰타입을 따로 각각 정의해줘야함.
    public int getItemViewType(int position) {
        Log.d("chase", "뷰타입:" + mProfileUid);
        ChatData chatdata = myChatDataList.get(position);
        //Log.d("getitemview", "리스트에서의 뷰타입:" + chatdata.getuid());
        if (chatdata.getViewtype() == 0) {
            if (chatdata.getuid() == null || chatdata.getuid().equals("")) {
                return 2;
            } else {
                if (chatdata.getuid().equals(mProfileUid)) { //사용자가나면
                    return 1;
                } else {//상대방이 채팅하면
                    return 0;

                }
            }
        }
        else if(chatdata.getViewtype()==1){ //친구목록 리스트 뷰홀더타입
            return 3;
        }
    else{//채팅목록 받는 뷰타입을 2
        return 4;
        }

    }
    @Override
    public int getItemCount() {
        return myChatDataList.size();
    }

    private class ChattingListViewHolder extends RecyclerView.ViewHolder{
        ImageView ChattingListImg;
        TextView ChattingListName;
        TextView ChattingListLastComment;
        TextView ChattingListTime;
        public ChattingListViewHolder(View view){
            super(view);
            User = new ArrayList<ChatData>();
            //chatids = new ArrayList<ChatData>();
            chatkeyandnames = new ArrayList<ChatData>();


            Log.d("mmprofile4:",mProfileUid);
            MyCref=FirebaseDatabase.getInstance().getReference().child("users"); //내아이디를 확인하는부분.
            MyCref.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    ChatData chatData = dataSnapshot.getValue(ChatData.class);
                    User.add(chatData);
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

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
            MyCfref= FirebaseDatabase.getInstance().getReference().child("chattingroomid");//여기부터
            MyCfref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        ChatData chatData = snapshot.getValue(ChatData.class);
                        //chatkeyandnames.add(chatData);
                        chatkeyandnames.add(new ChatData(snapshot.getKey(), chatData.getnamea(), chatData.getnameb()));


                    }

                    //Log.d("usersview4", tmp);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            })
            ;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int i=0;i<User.size();i++){
                        if (User.get(i).getuid().equals(mProfileUid)){
                            MyName = User.get(i).getName();
                            break;
                        }
                    }

                    for (int i = 0; i < chatkeyandnames.size(); i++) {
                        if ((chatkeyandnames.get(i).getnameb().equals(MyName) && chatkeyandnames.get(i).getnamea().equals(ChattingListName.getText().toString())) || (chatkeyandnames.get(i).getnameb().equals(ChattingListName.getText().toString()) && chatkeyandnames.get(i).getnamea().equals(MyName))) {
                            tmpkey = chatkeyandnames.get(i).getKey();
                            break;

                        }
                    }
                    Chatting_key=tmpkey;
                    Log.d("chattingkey_chase:",Chatting_key);
                    mycontext = v.getContext();

                    Intent intent = new Intent(mycontext, SuccessActivity.class);
                    intent.putExtra("chat_key", Chatting_key); //채팅할 상대방 아이디를 채팅페이지로 전송.
                    mycontext.startActivity(intent);



                }
            });
            ChattingListImg = (ImageView)view.findViewById(R.id.chattingroom_imageview);
            ChattingListName = (TextView)view.findViewById(R.id.chattingroom_your_name);
            ChattingListLastComment = (TextView)view.findViewById(R.id.chattingroom_last_comment);
            ChattingListTime = (TextView)view.findViewById(R.id.last_comment_time);
        }

    }

    private class FriendListViewHolder extends RecyclerView.ViewHolder{

        ImageView FriendListImg;
        TextView FriendListName;
        public FriendListViewHolder(View view){
            super(view);
            User = new ArrayList<ChatData>();
            //chatids = new ArrayList<ChatData>();
            //chatkeyandnames = new ArrayList<ChatData>();

            firebaseDatabase = FirebaseDatabase.getInstance();
            Log.d("mmprofile4:",mProfileUid);
            Myfref=firebaseDatabase.getReference().child("users"); //내아이디를 확인하는부분.
            Myfref.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    ChatData chatData = dataSnapshot.getValue(ChatData.class);
                    User.add(chatData);
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

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
            Myref= firebaseDatabase.getReference().child("chattingroomid");//여기부터
            Myref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        ChatData chatData = snapshot.getValue(ChatData.class);
                        //chatkeyandnames.add(chatData);
                        chatkeyandnames.add(new ChatData(snapshot.getKey(), chatData.getnamea(), chatData.getnameb()));


                    }

                    //Log.d("usersview4", tmp);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            })
            ;


           // MyChattingids=firebaseDatabase.getReference().child("chattingroomid"); //chattingroomid에 고유 데이터 확인하는 리스트만드는 구간.

            view.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    for (int i=0;i<User.size();i++){
                        if (User.get(i).getuid().equals(mProfileUid)){
                            MyName = User.get(i).getName();
                            break;
                        }
                    }



                        int flag=0;
                        Log.d("Myname!!",String.valueOf(flag));
                        if(chatkeyandnames.isEmpty()){
                            //Myref.push().setValue(new ChatData(MyName, FriendListName.getText().toString()));
                            tmpkey=Myref.push().getKey();
                            Myref.child(tmpkey).setValue(new ChatData(MyName, FriendListName.getText().toString()));


                        }

                        else {//이부분 chattingroomid부분에서 중복 발생. 내일 해결.
                            for (int i = 0; i < chatkeyandnames.size(); i++) {
                                if ((chatkeyandnames.get(i).getnameb().equals(MyName) && chatkeyandnames.get(i).getnamea().equals(FriendListName.getText().toString())) || (chatkeyandnames.get(i).getnameb().equals(FriendListName.getText().toString()) && chatkeyandnames.get(i).getnamea().equals(MyName))) {
                                    tmpkey = chatkeyandnames.get(i).getKey();
                                    flag=1;
                                    break;


                                }
                            }


                                if(flag==0) {
                                    tmpkey=Myref.push().getKey();
                                    Myref.child(tmpkey).setValue(new ChatData(MyName, FriendListName.getText().toString()));






                                }


                        }

                    //4.28에 해당 친구를 클릭햇을때 데이터베이스에 child(chatting_data) 를 만들어서 해당고유 채팅방이 내아이디와 채팅하는 상대방아이디를 갖는 데이터베이스를 만듬. 그리고 채팅페이지에서는 그 데이터를 이용하여 나와 상대채유저만갖느는 고유한 채탕벙 생성.
                        mycontext = v.getContext();
                        Chatting_key=tmpkey;


                        //Log.d("sibal",tmpkey);
                        //if (!chatkeyandnames.isEmpty()) {
                          //  for (int i = 0; i < chatkeyandnames.size(); i++) {
                            //    if (((chatkeyandnames.get(i).getnamea().equals(FriendListName.getText().toString())) && (chatkeyandnames.get(i).getnameb().equals(MyName))) || ((chatkeyandnames.get(i).getnamea().equals(MyName) && chatkeyandnames.get(i).getnameb().equals(FriendListName.getText().toString())))) {
                              //      Chatting_key = chatkeyandnames.get(i).getKey();   //클릭한목록의 상대 아이디와 내아이디를 포함하는 채팅방의 고유키 채팅 액티비티에 보내기
                                //    break;
                                //}
                            //}
                        //}




                        Intent intent = new Intent(mycontext, SuccessActivity.class);
                        intent.putExtra("chat_key", Chatting_key); //채팅할 상대방 아이디를 채팅페이지로 전송.
                        mycontext.startActivity(intent);


                }
            })
            ;


            FriendListImg = view.findViewById(R.id.friend_list_img_view);
            FriendListName = view.findViewById(R.id.friend_list_name_view);

        }

    }


    private class LeftViewHolder extends RecyclerView.ViewHolder {
        ImageView LeftImagerView;
        TextView LeftTitleView;
        TextView LeftMainContextView;
        TextView LeftTimeView;

        public LeftViewHolder(View view) {
            super(view);
            LeftImagerView = view.findViewById(R.id.image_view_id);
            LeftTitleView = view.findViewById(R.id.title_view_id);
            LeftMainContextView = view.findViewById(R.id.main_text_view_id);
            LeftTimeView = view.findViewById(R.id.time_view_id);

        }
    }

    private class RightViewHolder extends RecyclerView.ViewHolder {
        ImageView RightImagerView;
        TextView RightTitleView;
        TextView RightMainContextView;
        TextView RightTimeView;

        public RightViewHolder(View view) {
            super(view); //parent 클래스의 생성자를 먼저 선언
            RightImagerView = view.findViewById(R.id.my_chat_imgview);
            RightTitleView = view.findViewById(R.id.my_chat_name);
            RightMainContextView = view.findViewById(R.id.my_chat_main);
            RightTimeView = view.findViewById(R.id.my_chat_time);
        }
    }

    private class MiddleViewHolder extends RecyclerView.ViewHolder {
        TextView Chat_Entrance;
        public MiddleViewHolder(View view) {
            super(view);
            Chat_Entrance = view.findViewById(R.id.middle_chat_title);
        }
    }


}

