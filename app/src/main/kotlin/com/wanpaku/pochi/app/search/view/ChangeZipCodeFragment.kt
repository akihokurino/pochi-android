package com.wanpaku.pochi.app.search.view

import android.app.Activity
import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import butterknife.BindView
import com.jakewharton.rxbinding2.widget.RxTextView
import com.wanpaku.pochi.R
import com.wanpaku.pochi.app.delegate.ButterKnifeBindDelegate
import com.wanpaku.pochi.app.internal.BaseFragment
import com.wanpaku.pochi.app.internal.ViewLifecycle
import com.wanpaku.pochi.domain.ZipCode
import com.wanpaku.pochi.infra.ui.widget.TwoStateButton
import io.reactivex.disposables.CompositeDisposable

class ChangeZipCodeFragment : BaseFragment() {

    companion object {

        fun newInstance() = ChangeZipCodeFragment()

    }

    @BindView(R.id.zip_code_text_input) lateinit var zipCodeTextInputLayout: TextInputLayout
    @BindView(R.id.zip_code) lateinit var zipCodeEditText: EditText
    @BindView(R.id.save) lateinit var save: TwoStateButton

    private val disposables = CompositeDisposable()

    override fun viewLifecycle(): List<ViewLifecycle> = listOf(ButterKnifeBindDelegate(this))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_change_zip_code, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
    }

    override fun onDestroyView() {
        disposables.clear()
        super.onDestroyView()
    }

    private fun finish(zipCode: ZipCode) {
        val intent = ZipCodeIntent.makeIntent(zipCode)
        activity.setResult(Activity.RESULT_OK, intent)
        activity.finish()
    }

    private fun setupViews() {
        disposables.add(RxTextView.textChanges(zipCodeEditText)
                .map { ZipCode.Validator(it.toString()).isValid() }
                .subscribe { save.isActive = it })
        save.setOnClickListener {
            val zipCode = ZipCode.Validator(zipCodeEditText.text.toString()).to()
            if (zipCode != null) {
                finish(zipCode)
            }
        }
    }

}