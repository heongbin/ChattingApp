package com.example.chattingapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList; //4/16일 내채팅,상대채팅말고 중간 채팅 만들기!! 뷰홀더에 적용하기!!

class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private String MyEmail;
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
        else{
            view = inflater.inflate(R.layout.recycler_empty_chat_view,parent,false);
            return new MiddleViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) { //내채팅,상대채팅에따라 홀더를 적용해줌 onbind는 생성된 뷰를 뷰홀더를 통헤 각자 데이터클래스를 뷰와 연결해줌.
        ChatData chatdata = myChatDataList.get(position);
        if (chatdata.getuid()!=null ) {
            if (chatdata.getuid().equals(mProfileUid)) {//사용자가 채팅한거면
                RightViewHolder rightholder = (RightViewHolder) holder;
                rightholder.RightTimeView.setText(chatdata.getTime());
                rightholder.RightTitleView.setText(chatdata.getName());
                if (chatdata.getImg()==null) {//프로필사진이x면
                    rightholder.RightImagerView.setImageResource(R.drawable.ic_account_box_black_24dp);
                } else {
                    Glide.with(mycontext).load(chatdata.getImg()).into(rightholder.RightImagerView);// 4/12 glide라이브러리 implement하기부터
                }
                if(chatdata.getMaincontent()==null){
                    rightholder.RightMainContextView.setText("no find");
                }
                else {
                    rightholder.RightMainContextView.setText(chatdata.getMaincontent());
                }

            } else { //상대방이 채팅한거면
                LeftViewHolder leftholder = (LeftViewHolder) holder;
                leftholder.LeftTitleView.setText(chatdata.getName());
                if (chatdata.getImg().equals("")) {//프로필사진이x면
                    leftholder.LeftImagerView.setImageResource(R.drawable.ic_account_box_black_24dp);
                } else {
                    Glide.with(mycontext).load(chatdata.getImg()).into(leftholder.LeftImagerView);// 4/12 glide라이브러리 implement하기부터
                }
                leftholder.LeftMainContextView.setText(chatdata.getMaincontent());
                leftholder.LeftTimeView.setText(chatdata.getTime());

            }
        }
    }

    @Override//채팅창처럼 여러가지 뷰를 쓸경우 뷰타입을 따로 각각 정의해줘야함.
    public int getItemViewType(int position) {
        Log.d("chase","뷰타입:"+ mProfileUid );
        ChatData chatdata = myChatDataList.get(position);
        Log.d("getitemview","리스트에서의 뷰타입:" + chatdata.getuid());

        if(chatdata.getuid()==null||chatdata.getuid().equals("")){
            return 2;
        }
        else {
            if (chatdata.getuid().equals(mProfileUid)) { //사용자가나면
                return 1;
            } else {//상대방이 채팅하면
                return 0;

            }
        }
    }

    @Override
    public int getItemCount() {
        return myChatDataList.size();
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
