package com.wanpaku.pochi.app.maintab

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.annotation.IdRes
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.widget.Toolbar
import com.wanpaku.pochi.R
import com.wanpaku.pochi.app.booking.view.BookingFragment
import com.wanpaku.pochi.app.delegate.BookingToolbarDelegate
import com.wanpaku.pochi.app.delegate.MainTabToolbarDelegate
import com.wanpaku.pochi.app.delegate.SearchToolbarDelegate
import com.wanpaku.pochi.app.internal.BaseActivity
import com.wanpaku.pochi.app.others.OthersFragment
import com.wanpaku.pochi.app.search.view.SearchFragment
import com.wanpaku.pochi.app.search.view.SearchResultActivity
import com.wanpaku.pochi.app.task.TaskFragment
import com.wanpaku.pochi.domain.ZipCode
import com.wanpaku.pochi.infra.ui.widget.disableShiftingMode


class MainTabActivity : BaseActivity() {

    companion object {

        fun startActivity(context: Context) {
            val intent = Intent(context, MainTabActivity::class.java).let {
                Intent.makeRestartActivityTask(it.component)
            }
            context.startActivity(intent)
        }

    }

    private val searchFragment: Fragment by lazy {
        supportFragmentManager.findFragmentByTag(SearchFragment.TAG) as? SearchFragment
                ?: SearchFragment.newInstance()
    }

    private val userBookingFragment: Fragment by lazy {
        supportFragmentManager.findFragmentByTag(BookingFragment.TAG_FOR_USER) as? BookingFragment
                ?: BookingFragment.newInstanceForUser()
    }

    private val sitterBookingFragment: Fragment by lazy {
        supportFragmentManager.findFragmentByTag(BookingFragment.TAG_FOR_SITTER) as? BookingFragment
                ?: BookingFragment.newInstanceForSitter()
    }

    private val taskFragment: Fragment by lazy {
        supportFragmentManager.findFragmentByTag(TaskFragment.TAG) as? TaskFragment
                ?: TaskFragment.newInstance()
    }

    private val othersFragment: Fragment by lazy {
        supportFragmentManager.findFragmentByTag(OthersFragment.TAG) as? OthersFragment
                ?: OthersFragment.newInstance()
    }

    private val toolbarDelegates by lazy {
        mapOf(Pair(TabItem.Search, SearchToolbarDelegate(R.string.search) { launchSearchResult(it) }),
                Pair(TabItem.BookingForUser, BookingToolbarDelegate(R.string.user_booking)),
                Pair(TabItem.BookingForSitter, BookingToolbarDelegate(R.string.sitter_booking)),
                Pair(TabItem.Task, MainTabToolbarDelegate(R.string.task)),
                Pair(TabItem.Others, MainTabToolbarDelegate(R.string.others)))
    }

    private val switchToolbar: (TabItem) -> Unit by lazy {
        var currentTabItem: TabItem? = null
        { tabItem: TabItem ->
            if (tabItem != currentTabItem) {
                toolbarDelegates[currentTabItem]?.unselect(this@MainTabActivity)
                toolbarDelegates[tabItem]?.select(this@MainTabActivity)
                currentTabItem = tabItem
            }
        }
    }

    override fun lifecycleDelegates() = toolbarDelegates.map { it.value }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_tab)
        setupView()
        if (savedInstanceState == null) {
            switchFragment(searchFragment, SearchFragment.TAG)
            switchToolbar(TabItem.Search)
        }
    }

    private fun setupView() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        findViewById<BottomNavigationView>(R.id.bottom_navigation_view).apply {
            disableShiftingMode()
            setOnNavigationItemSelectedListener {
                it.isChecked = true
                TabItem.find(it.itemId)?.let {
                    val (f, tag) = selectFragment(it)
                    switchFragment(f, tag)
                    switchToolbar(it)
                }
                false
            }
        }
    }

    private fun switchFragment(fragment: Fragment, tag: String) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        val currentFragment: Fragment? = supportFragmentManager.findFragmentById(R.id.container)
        currentFragment?.let {
            fragmentTransaction.detach(it)
        }
        if (fragment.isDetached) {
            fragmentTransaction.attach(fragment)
        } else {
            fragmentTransaction.add(R.id.container, fragment, tag)
        }
        fragmentTransaction.commit()
    }

    private fun selectFragment(tabItem: TabItem): Pair<Fragment, String> =
            when (tabItem) {
                TabItem.Search -> Pair(searchFragment, SearchFragment.TAG)
                TabItem.BookingForUser -> Pair(userBookingFragment, BookingFragment.TAG_FOR_USER)
                TabItem.BookingForSitter -> Pair(sitterBookingFragment, BookingFragment.TAG_FOR_SITTER)
                TabItem.Task -> Pair(taskFragment, TaskFragment.TAG)
                TabItem.Others -> Pair(othersFragment, OthersFragment.TAG)
            }

    private fun launchSearchResult(zipCode: ZipCode) {
        SearchResultActivity.startActivity(this, zipCode)
    }

    private enum class TabItem(@IdRes val itemId: Int) {
        Search(R.id.action_search),
        BookingForUser(R.id.action_user_booking),
        BookingForSitter(R.id.action_sitter_booking),
        Task(R.id.action_task),
        Others(R.id.action_others);

        companion object {

            fun find(itemId: Int): TabItem? =
                    TabItem.values().find { it.itemId == itemId }

        }
    }

}

