package com.idn.adniwhatsappclone.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.idn.adniwhatsappclone.MainActivity
import com.idn.adniwhatsappclone.fragment.ChatsFragment
import com.idn.adniwhatsappclone.fragment.StatusListFragment
import com.idn.adniwhatsappclone.fragment.StatusUpdateFragment

class SectionPagerAdapter(fm: FragmentManager): FragmentPagerAdapter(fm) {

    private val chatsFragment = ChatsFragment()
    private val statusUpdateFragment = StatusUpdateFragment()
    private val statusFragment = StatusListFragment()

    override fun getItem(position: Int): Fragment {
        return when (position){
            0 -> statusUpdateFragment // menempatkan StatusUpdateFragment di posisi pertama
            1 -> chatsFragment        // ChatsFragment posisi kedua dalam adapter
            2 -> statusFragment       // StatusListFragment posisi ketiga dalam adapter
            else -> chatsFragment
        }
    }

    override fun getCount(): Int {
        return 3
    }
}
