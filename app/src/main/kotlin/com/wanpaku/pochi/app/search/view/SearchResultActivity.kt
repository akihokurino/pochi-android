package com.wanpaku.pochi.app.search.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.wanpaku.pochi.R
import com.wanpaku.pochi.app.delegate.SearchResultToolbarDelegate
import com.wanpaku.pochi.app.internal.BaseActivity
import com.wanpaku.pochi.domain.ZipCode

class SearchResultActivity : BaseActivity() {

    companion object {

        private val KEY_ZIP_CODE = "key_zip_code"
        private val REQUEST_CODE = 1

        fun startActivity(activity: BaseActivity, zipCode: ZipCode) {
            val intent = Intent(activity, SearchResultActivity::class.java).apply {
                putExtra(KEY_ZIP_CODE, zipCode)
            }
            activity.startActivity(intent)
        }

        private fun zipCode(intent: Intent) =
                intent.getParcelableExtra<ZipCode>(KEY_ZIP_CODE)

    }

    private val toolbarDelegate by lazy {
        SearchResultToolbarDelegate(title = getString(R.string.zip_code),
                subTitle = zipCode(intent).zipCode) {
            ChangeZipCodeActivity.startActivityForResult(this, REQUEST_CODE)
        }
    }

    override fun lifecycleDelegates() = listOf(toolbarDelegate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_result)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, SearchResultFragment.newInstance(zipCode(intent)), SearchResultFragment.TAG)
                    .commit()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode != REQUEST_CODE) return
        if (resultCode != Activity.RESULT_OK) return
        if (data == null) return
        val zipCode = ZipCodeIntent.extractZipCode(data)
        refresh(zipCode)
    }

    private fun refresh(zipCode: ZipCode) {
        toolbarDelegate.subTitle = zipCode.zipCode
        val fragment = supportFragmentManager
                .findFragmentByTag(SearchResultFragment.TAG) as? SearchResultFragment
        fragment?.fetchSitters(zipCode)
    }

}
