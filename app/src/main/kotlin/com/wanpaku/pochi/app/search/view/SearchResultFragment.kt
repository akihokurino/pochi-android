package com.wanpaku.pochi.app.search.view

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import butterknife.BindView
import com.wanpaku.pochi.R
import com.wanpaku.pochi.app.delegate.ButterKnifeBindDelegate
import com.wanpaku.pochi.app.internal.BaseFragment
import com.wanpaku.pochi.app.internal.ViewLifecycle
import com.wanpaku.pochi.app.search.contract.SearchResultContract
import com.wanpaku.pochi.app.search.presenter.SearchResultPresenter
import com.wanpaku.pochi.app.sitterdetail.SitterDetailActivity
import com.wanpaku.pochi.domain.ZipCode
import com.wanpaku.pochi.domain.sitter.Sitter
import com.wanpaku.pochi.infra.ui.itemdecoration.MarginItemDecoration
import javax.inject.Inject

class SearchResultFragment : BaseFragment(), SearchResultContract.View {

    companion object {

        val TAG = SearchResultFragment::class.java.simpleName

        private val KEY_ZIP_CODE = "key_zip_code"

        fun newInstance(zipCode: ZipCode) = SearchResultFragment().apply {
            arguments = Bundle().apply {
                putParcelable(KEY_ZIP_CODE, zipCode)
            }
        }

        private fun zipCode(bundle: Bundle) = bundle.getParcelable<ZipCode>(KEY_ZIP_CODE)

    }

    @BindView(R.id.progress) lateinit var progress: ProgressBar
    @BindView(R.id.recycler_view) lateinit var recyclerView: RecyclerView

    @Inject lateinit var presenter: SearchResultPresenter

    private val sitterListAdapter = SitterListAdapter { launchSitter(it) }

    override fun viewLifecycle(): List<ViewLifecycle> = listOf(ButterKnifeBindDelegate(this), presenter)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_search_result, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        fetchSitters(zipCode(arguments))
    }

    override fun updateList(sitters: List<Sitter>) {
        sitterListAdapter.clear()
        val viewModels = sitters.map { SitterCardViewModel.from(it, context) }
        sitterListAdapter.addAll(viewModels)
    }

    override fun showError() {
        // TODO
    }

    override fun setIndicator(isVisible: Boolean) {
        progress.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    fun fetchSitters(zipCode: ZipCode) {
        presenter.fetchSitters(zipCode)
    }

    private fun setupRecyclerView() {
        recyclerView.apply {
            setHasFixedSize(true)
            addItemDecoration(MarginItemDecoration(spaceBetweenItemHeightPx = resources.getDimensionPixelSize(R.dimen.margin_card_vertical_height)))
            adapter = sitterListAdapter
        }
    }

    private fun launchSitter(sitter: Sitter) {
        SitterDetailActivity.startActivity(baseActivity, sitter)
    }

}