package kg.zukhridin.nework.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.adapter.FragmentViewHolder

class PagerAdapter(fragment: FragmentManager, lifecycle: Lifecycle, private val fragments: ArrayList<Fragment>) : FragmentStateAdapter(fragment, lifecycle) {
    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}