package com.mrcoder.charcha.adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.mrcoder.charcha.fragments.ChatsFragment;
import com.mrcoder.charcha.fragments.ContactsFragment;
import com.mrcoder.charcha.fragments.GroupsFragment;

public class TabAccessorAdapter extends FragmentPagerAdapter {
    public TabAccessorAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                ChatsFragment chatsFragment = new ChatsFragment();
                return chatsFragment;

            case 1:
                GroupsFragment groupsFragment = new GroupsFragment();
                return groupsFragment;
            case 2:
                ContactsFragment contactsFragment = new ContactsFragment();
                return contactsFragment;

        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        //return super.getPageTitle(position);
        switch (position) {
            case 0:
//                ChatsFragment chatsFragment = new ChatsFragment();
                return "Chats";

            case 1:
//                GroupsFragment groupsFragment = new GroupsFragment();
                return "Groups";
            case 2:
//                ContactsFragment contactsFragment = new ContactsFragment();
                return "Contacts";

        }
        return null;
    }
}
