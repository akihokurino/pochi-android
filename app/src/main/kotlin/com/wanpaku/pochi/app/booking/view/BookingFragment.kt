package com.wanpaku.pochi.app.booking.view

import android.content.Context
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.BindView
import com.wanpaku.pochi.R
import com.wanpaku.pochi.app.delegate.ButterKnifeBindDelegate
import com.wanpaku.pochi.app.internal.BaseFragment
import com.wanpaku.pochi.app.internal.ViewLifecycle

class BookingFragment : BaseFragment() {

    companion object {

        val TAG_FOR_USER = BookingFragment::class.java.simpleName + BookingType.ForUser.name
        val TAG_FOR_SITTER = BookingFragment::class.java.simpleName + BookingType.ForSitter.name

        private val KEY_TYPE = "key_type"

        fun newInstanceForUser() = newInstance(BookingType.ForUser)

        fun newInstanceForSitter() = newInstance(BookingType.ForSitter)

        private fun newInstance(type: BookingType) = BookingFragment().apply {
            arguments = Bundle().apply {
                putSerializable(KEY_TYPE, type)
            }
        }

        private fun bookingType(bundle: Bundle) =
                bundle.getSerializable(KEY_TYPE) as BookingType

    }

    @BindView(R.id.tab_layout) lateinit var tabLayout: TabLayout
    @BindView(R.id.view_pager) lateinit var viewPager: ViewPager

    private val adapter by lazy {
        Adapter(childFragmentManager, context, bookingType(arguments))
    }

    override fun viewLifecycle(): List<ViewLifecycle> = listOf(ButterKnifeBindDelegate(this))

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_booking, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
    }

    private fun setupViews() {
        tabLayout.setupWithViewPager(viewPager)
        viewPager.apply {
            offscreenPageLimit = this@BookingFragment.adapter.count
            adapter = this@BookingFragment.adapter
        }
    }

    class Adapter(fm: FragmentManager, private val context: Context, private val type: BookingType) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment? {
            val category = BookingCategory.find(position)
            return BookingListFragment.newInstance(category, type)
        }

        override fun getCount() = BookingCategory.values().size

        override fun getPageTitle(position: Int): CharSequence {
            val category = BookingCategory.find(position)
            return when (category) {
                BookingCategory.Request -> context.getString(R.string.request)
                BookingCategory.FixedRequest -> context.getString(R.string.fixed_request)
                BookingCategory.Stay -> context.getString(if (type == BookingType.ForUser) R.string.stay_now else R.string.stayed_now)
            }
        }
    }

}