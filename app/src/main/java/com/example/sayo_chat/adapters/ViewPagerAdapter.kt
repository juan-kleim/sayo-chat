package com.example.sayo_chat.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.sayo_chat.fragments.ContatosFragment
import com.example.sayo_chat.fragments.ConversasFragment

class ViewPagerAdapter(
    private val abas: List<String>,
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
)    : FragmentStateAdapter (fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return abas.size
    }

    override fun createFragment(position: Int): Fragment {
        when( position ) {
            1 -> ContatosFragment()
            2 -> ConversasFragment()
        }

        return ConversasFragment()
    }


}