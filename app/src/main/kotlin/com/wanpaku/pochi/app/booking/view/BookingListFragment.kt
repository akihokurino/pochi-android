package com.wanpaku.pochi.app.booking.view

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.wanpaku.pochi.R
import com.wanpaku.pochi.app.booking.contract.BookingListContract
import com.wanpaku.pochi.app.booking.presenter.BookingListPresenter
import com.wanpaku.pochi.app.delegate.ButterKnifeBindDelegate
import com.wanpaku.pochi.app.internal.BaseFragment
import com.wanpaku.pochi.app.internal.ViewLifecycle
import com.wanpaku.pochi.app.message.view.MessageActivity
import com.wanpaku.pochi.domain.booking.Booking
import com.wanpaku.pochi.infra.ui.adapter.BaseRecyclerViewAdapter
import com.wanpaku.pochi.infra.ui.widget.load
import com.wanpaku.pochi.infra.util.toYYYYMMDD
import javax.inject.Inject

class BookingListFragment : BaseFragment(), BookingListContract.View {

    companion object {

        private val KEY_BOOKING_CATEGORY = "key_booking_category"
        private val KEY_BOOKING_TYPE = "key_booking_type"

        fun newInstance(category: BookingCategory, type: BookingType) =
                BookingListFragment().apply {
                    arguments = Bundle().apply {
                        putSerializable(KEY_BOOKING_CATEGORY, category)
                        putSerializable(KEY_BOOKING_TYPE, type)
                    }
                }

        private fun bookingCategory(bundle: Bundle) =
                bundle.getSerializable(KEY_BOOKING_CATEGORY) as BookingCategory

        private fun bookingType(bundle: Bundle) =
                bundle.getSerializable(KEY_BOOKING_TYPE) as BookingType

    }

    @Inject lateinit var presenter: BookingListPresenter

    @BindView(R.id.progress) lateinit var progress: ProgressBar
    @BindView(R.id.recycler_view) lateinit var recyclerView: RecyclerView
    @BindView(R.id.empty_text) lateinit var emptyText: TextView

    private val adapter = Adapter {
        when (bookingType(arguments)) {
            BookingType.ForUser -> MessageActivity.startActivityForUser(baseActivity, it)
            BookingType.ForSitter -> MessageActivity.startActivityForSitter(baseActivity, it)
        }
    }

    override fun viewLifecycle(): List<ViewLifecycle> = listOf(ButterKnifeBindDelegate(this), presenter)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_booking_list, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        presenter.fetchBookings(bookingCategory(arguments), bookingType(arguments))
    }

    private fun setupRecyclerView() {
        recyclerView.apply {
            setHasFixedSize(true)
            adapter = this@BookingListFragment.adapter
        }
    }

    override fun updateList(bookings: List<Booking>) {
        emptyText.visibility = if (bookings.isEmpty()) View.VISIBLE else View.GONE
        emptyText.setText(R.string.empty_message)
        adapter.clear()
        adapter.addAll(bookings)
    }

    override fun setIndicator(isVisible: Boolean) {
        progress.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    override fun showError() {

    }

    class Adapter(private val itemClickListener: (Booking) -> Unit) : BaseRecyclerViewAdapter<Booking, Adapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.view_booking_summary, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val booking = get(position)
            holder.itemView.setOnClickListener { itemClickListener(booking) }
            holder.thumbnail.load(booking.sitter.iconUri)
            holder.fullName.text = booking.sitter.fullName()
            holder.elapsedTime.text = booking.updatedAt.toYYYYMMDD()
            holder.message.text = booking.message?.content
            holder.date.text = booking.period(holder.itemView.context)
        }

        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            @BindView(R.id.thumbnail) lateinit var thumbnail: ImageView
            @BindView(R.id.full_name) lateinit var fullName: TextView
            @BindView(R.id.elapsed_time) lateinit var elapsedTime: TextView
            @BindView(R.id.message) lateinit var message: TextView
            @BindView(R.id.date) lateinit var date: TextView

            init {
                ButterKnife.bind(this, itemView)
            }

        }

    }

}