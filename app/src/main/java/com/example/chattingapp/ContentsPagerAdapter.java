package com.example.chattingapp;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

class ContentsPagerAdapter extends FragmentStatePagerAdapter {
    private int mPageCount;

    public ContentsPagerAdapter(@NonNull FragmentManager fm, int mPageCount) {
        super(fm);
        this.mPageCount = mPageCount;
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position==0){
            FriendsListFragment friendsListFragment = new FriendsListFragment();

            return friendsListFragment;

        }
        else{
            ChattingListFragment chattingListFragment = new ChattingListFragment();
            return chattingListFragment;

            }
    }

    @Override
    public int getCount() {
        return mPageCount;
    }
}
