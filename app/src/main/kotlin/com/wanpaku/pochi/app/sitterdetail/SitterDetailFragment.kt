package com.wanpaku.pochi.app.sitterdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import butterknife.BindView
import butterknife.OnClick
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.wanpaku.pochi.R
import com.wanpaku.pochi.app.delegate.ButterKnifeBindDelegate
import com.wanpaku.pochi.app.internal.BaseFragment
import com.wanpaku.pochi.app.internal.ViewLifecycle
import com.wanpaku.pochi.app.message.view.MessageActivity
import com.wanpaku.pochi.domain.booking.Booking
import com.wanpaku.pochi.domain.dog.Dog
import com.wanpaku.pochi.domain.sitter.Review
import com.wanpaku.pochi.domain.sitter.Sitter
import com.wanpaku.pochi.infra.util.GoogleMapLauncher
import javax.inject.Inject

class SitterDetailFragment : BaseFragment(), SitterDetailContract.View {

    companion object {

        val TAG = SitterDetailFragment::class.java.simpleName!!

        private val KEY_SITTER = "key_sitter"

        fun newInstance(sitter: Sitter) = SitterDetailFragment().apply {
            arguments = Bundle().apply { putParcelable(KEY_SITTER, sitter) }
        }

        fun sitter(bundle: Bundle): Sitter = bundle.getParcelable(KEY_SITTER)

    }

    @BindView(R.id.sitter_house_about_view) lateinit var sitterHouseAboutView: SitterHouseAboutView
    @BindView(R.id.sitter_interior_image_swipe_view) lateinit var sitterInteriorImageSwipeView: SitterInteriorImageSwipeView
    @BindView(R.id.sitter_profile_message_view) lateinit var sitterProfileMessageView: SitterProfileMessageView
    @BindView(R.id.sitter_house_address_label) lateinit var sitterHouseAddressLabel: TextView
    @BindView(R.id.sitter_house_address_container) lateinit var sitterHouseAddressContainer: View
    @BindView(R.id.sitter_house_address) lateinit var sitterHouseAddress: TextView
    @BindView(R.id.sitter_review_summary_view) lateinit var sitterReviewSummaryView: SitterReviewSummaryView
    @BindView(R.id.progress_bar) lateinit var progress: ProgressBar


    @Inject lateinit var presenter: SitterDetailPresenter

    override fun viewLifecycle(): List<ViewLifecycle> = listOf(ButterKnifeBindDelegate(this), presenter)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_sitter_detail, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sitter = sitter(arguments)
        setupMap()
        presenter.fetchSitter(sitter.userId)
    }

    override fun update(sitter: Sitter, reviews: List<Review>, dogs: List<Dog>) {
        updateSitterInteriorImageSwipeView(sitter)
        updateSitterHouseAboutView(sitter)
        updateSitterProfileMessage(sitter, dogs)
        updateHouseAddress(sitter)
        updateReviewSummary(reviews)
    }

    override fun launchMessage(booking: Booking) {
        MessageActivity.startActivityForUser(baseActivity, booking)
    }

    override fun setIndicator(isVisible: Boolean) {
        progress.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    @OnClick(R.id.send_message)
    fun onSendMessageClicked() {
        presenter.createBooking(sitter(arguments).userId)
    }

    private fun setupMap() {
        val fragment = SupportMapFragment.newInstance(GoogleMapOptions().liteMode(true))
        childFragmentManager.beginTransaction()
                .replace(R.id.map_container, fragment)
                .commit()
    }

    private fun updateSitterInteriorImageSwipeView(sitter: Sitter) {
        sitter.interiorImages.let { images ->
            sitterInteriorImageSwipeView.visibility = if (images.isNotEmpty()) View.VISIBLE else View.GONE
            sitterInteriorImageSwipeView.apply(SitterInteriorImageSwipeView.ViewModel(images, context))
        }
    }

    private fun updateSitterHouseAboutView(sitter: Sitter) {
        sitterHouseAboutView.visibility = View.VISIBLE
        sitterHouseAboutView.apply(SitterHouseAboutView.ViewModel(sitter))
    }

    private fun updateSitterProfileMessage(sitter: Sitter, dogs: List<Dog>) {
        sitterProfileMessageView.visibility = View.VISIBLE
        sitterProfileMessageView.apply(SitterProfileMessageView.ViewModel(sitter, dogs, context))
    }


    private fun updateHouseAddress(sitter: Sitter) {
        sitter.address?.let { address ->
            val latLng = LatLng(address.latitude, address.longitude)
            val mapFragment = childFragmentManager.findFragmentById(R.id.map_container) as? SupportMapFragment
            mapFragment?.apply {
                getMapAsync {
                    it.addMarker(MarkerOptions().position(latLng))
                    it.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17f))
                }
            }
            sitterHouseAddressLabel.visibility = View.VISIBLE
            sitterHouseAddressContainer.visibility = View.VISIBLE
            sitterHouseAddress.text = address.address
            sitterHouseAddressContainer.setOnClickListener {
                GoogleMapLauncher.laucnh(latLng.latitude, latLng.longitude, context)
            }
        }
    }

    private fun updateReviewSummary(reviews: List<Review>) {
        sitterReviewSummaryView.visibility = if (reviews.isEmpty()) View.GONE else View.VISIBLE
        if (reviews.isEmpty()) return
        val viewModel = SitterReviewSummaryView.ViewModel(reviews.first(), reviews.size, context) {
            // TODO
        }
        sitterReviewSummaryView.apply(viewModel)
    }

}