package com.wanpaku.pochi.app.search.view

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import butterknife.BindView
import butterknife.ButterKnife
import com.wanpaku.pochi.R
import com.wanpaku.pochi.app.delegate.ButterKnifeBindDelegate
import com.wanpaku.pochi.app.internal.BaseFragment
import com.wanpaku.pochi.app.internal.ViewLifecycle
import com.wanpaku.pochi.app.search.contract.SearchContract
import com.wanpaku.pochi.app.search.presenter.SearchPresenter
import com.wanpaku.pochi.app.sitterdetail.SitterDetailActivity
import com.wanpaku.pochi.domain.sitter.Sitter
import com.wanpaku.pochi.infra.ui.adapter.BaseRecyclerViewAdapter
import com.wanpaku.pochi.infra.ui.adapter.MergeRecyclerAdapter
import com.wanpaku.pochi.infra.ui.adapter.SingleTitleAdapter
import com.wanpaku.pochi.infra.ui.itemdecoration.MarginItemDecoration
import javax.inject.Inject

class SearchFragment : BaseFragment(), SearchContract.View {

    companion object {

        val TAG = SearchFragment::class.java.simpleName

        fun newInstance() = SearchFragment()

    }

    @BindView(R.id.progress) lateinit var progress: ProgressBar
    @BindView(R.id.recycler_view) lateinit var recyclerView: RecyclerView

    @Inject lateinit var presenter: SearchPresenter

    override fun viewLifecycle(): List<ViewLifecycle> = listOf(presenter,
            ButterKnifeBindDelegate(this))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_search, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view ?: return
        setupRecyclerView()
        presenter.fetchSitters()
    }

    private fun setupRecyclerView() {
        recyclerView.addItemDecoration(MarginItemDecoration(spaceBetweenItemHeightPx = resources.getDimensionPixelSize(R.dimen.margin_card_vertical_height)))
        recyclerView.setHasFixedSize(true)
    }

    override fun setIndicator(isVisible: Boolean) {
        progress.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    override fun updateList(sitters: List<Sitter>, isLoggedIn: Boolean) {
        val sitterViewModels = sitters.map { SitterCardViewModel.from(it, context) }
        val sitterListAdapter = SitterListAdapter { launchSitter(it) }
                .apply { addAll(sitterViewModels) }

        fun createAdapter(): MergeRecyclerAdapter<RecyclerView.Adapter<*>> =
                MergeRecyclerAdapter<RecyclerView.Adapter<*>>(context).apply {
                    if (!isLoggedIn) addAdapter(SignUpBannerAdapter())
                    addAdapter(SingleTitleAdapter(R.string.all_sitter))
                    addAdapter(sitterListAdapter)
                }

        recyclerView.adapter = createAdapter()
    }

    override fun showError(message: String) {
        // TODO
    }

    private fun launchSitter(sitter: Sitter): Unit {
        SitterDetailActivity.startActivity(baseActivity, sitter)
    }

    class SignUpBannerAdapter : BaseRecyclerViewAdapter<Unit, SignUpBannerAdapter.ViewHolder>() {

        override fun getItemCount(): Int = 1

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.banner_sign_up, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.itemView.setOnClickListener { }
            holder.close.setOnClickListener { }
        }

        class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

            @BindView(R.id.close) lateinit var close: View

            init {
                ButterKnife.bind(this, itemView)
            }

        }

    }

}
