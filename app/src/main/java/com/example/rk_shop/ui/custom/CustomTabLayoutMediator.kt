package com.example.rk_shop.ui.custom

import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import java.lang.ref.WeakReference

class CustomTabLayoutMediator(
    private val tabLayout: TabLayout,
    private val viewPager: ViewPager2,
    private val autoRefresh: Boolean = true,
    private val smoothScroll: Boolean = true,
    private val tabConfigurationStrategy: TabConfigurationStrategy
) {

    private var adapter: RecyclerView.Adapter<*>? = null
    private var attached = false
    private var onPageChangeCallback: TabLayoutOnPageChangeCallback? = null
    private var onTabSelectedListener: TabLayout.OnTabSelectedListener? = null
    private var pagerAdapterObserver: RecyclerView.AdapterDataObserver? = null

    interface TabConfigurationStrategy {
        fun onConfigureTab(tab: TabLayout.Tab, position: Int)
    }

    fun attach() {
        check(!attached) { "TabLayoutMediator is already attached" }
        adapter = viewPager.adapter
        checkNotNull(adapter) { "ViewPager2 has no adapter attached" }
        attached = true

        onPageChangeCallback = TabLayoutOnPageChangeCallback(tabLayout)
        viewPager.registerOnPageChangeCallback(onPageChangeCallback!!)

        onTabSelectedListener = ViewPagerOnTabSelectedListener(viewPager, smoothScroll)
        tabLayout.addOnTabSelectedListener(onTabSelectedListener!!)

        if (autoRefresh) {
            pagerAdapterObserver = PagerAdapterObserver()
            adapter?.registerAdapterDataObserver(pagerAdapterObserver!!)
        }

        populateTabsFromPagerAdapter()

        val currentItem = viewPager.currentItem
        if (currentItem != tabLayout.selectedTabPosition && currentItem < tabLayout.tabCount) {
            tabLayout.getTabAt(currentItem)?.select()
        }
    }

    fun detach() {
        adapter?.unregisterAdapterDataObserver(pagerAdapterObserver!!)
        tabLayout.removeOnTabSelectedListener(onTabSelectedListener!!)
        viewPager.unregisterOnPageChangeCallback(onPageChangeCallback!!)
        pagerAdapterObserver = null
        onTabSelectedListener = null
        onPageChangeCallback = null
        adapter = null
        attached = false
    }

    private fun populateTabsFromPagerAdapter() {
        tabLayout.removeAllTabs()
        adapter?.let {
            val adapterCount = it.itemCount
            for (i in 0 until adapterCount) {
                val tab = tabLayout.newTab()
                tabConfigurationStrategy.onConfigureTab(tab, i)
                tabLayout.addTab(tab, false)
            }
            if (adapterCount > 0) {
                val lastItem = tabLayout.tabCount - 1
                val currItem = viewPager.currentItem.coerceAtMost(lastItem)
                if (currItem != tabLayout.selectedTabPosition) {
                    tabLayout.getTabAt(currItem)?.select()
                }
            }
        }
    }

    private inner class PagerAdapterObserver : RecyclerView.AdapterDataObserver() {
        override fun onChanged() {
            populateTabsFromPagerAdapter()
        }

        override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
            populateTabsFromPagerAdapter()
        }

        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            populateTabsFromPagerAdapter()
        }

        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
            populateTabsFromPagerAdapter()
        }

        override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
            populateTabsFromPagerAdapter()
        }
    }

    private class TabLayoutOnPageChangeCallback(tabLayout: TabLayout) :
        ViewPager2.OnPageChangeCallback() {
        private val tabLayoutRef = WeakReference(tabLayout)
        private var previousScrollState = 0
        private var scrollState = 0

        override fun onPageScrollStateChanged(state: Int) {
            previousScrollState = scrollState
            scrollState = state
        }

        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
            val tabLayout = tabLayoutRef.get()
            if (tabLayout != null && positionOffset != 0f && scrollState != ViewPager2.SCROLL_STATE_IDLE) {
                val currentTab = tabLayout.getTabAt(position)
                val nextTab = tabLayout.getTabAt(position + 1)
                currentTab?.view?.alpha = 1 - positionOffset
                nextTab?.view?.alpha = positionOffset
            }
        }

        override fun onPageSelected(position: Int) {
            val tabLayout = tabLayoutRef.get()
            if (tabLayout != null && tabLayout.selectedTabPosition != position
                && position < tabLayout.tabCount
            ) {
                if (scrollState == ViewPager2.SCROLL_STATE_IDLE
                    || (scrollState == ViewPager2.SCROLL_STATE_SETTLING
                            && previousScrollState == ViewPager2.SCROLL_STATE_IDLE)
                ) {
                    tabLayout.getTabAt(position)?.select()
                }
            }
        }
    }

    private class ViewPagerOnTabSelectedListener(
        private val viewPager: ViewPager2,
        private val smoothScroll: Boolean
    ) : TabLayout.OnTabSelectedListener {
        override fun onTabSelected(tab: TabLayout.Tab) {
            viewPager.setCurrentItem(tab.position, smoothScroll)
        }

        override fun onTabUnselected(tab: TabLayout.Tab) {
            // No-op
        }

        override fun onTabReselected(tab: TabLayout.Tab) {
            // No-op
        }
    }
}
