package com.wanpaku.pochi.app.search.view

import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import com.wanpaku.pochi.R
import com.wanpaku.pochi.app.delegate.ToolbarDelegate
import com.wanpaku.pochi.app.internal.BaseActivity

class ChangeZipCodeActivity : BaseActivity() {

    companion object {

        fun startActivityForResult(activity: BaseActivity, requestCode: Int) {
            val intent = Intent(activity, ChangeZipCodeActivity::class.java)
            activity.startActivityForResult(intent, requestCode)
        }

    }

    override fun lifecycleDelegates() = listOf(
            ToolbarDelegate(icon = ContextCompat.getDrawable(this, R.drawable.ic_close_24dp),
                    title = getString(R.string.input_zip_code))
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_zip_code)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, ChangeZipCodeFragment.newInstance())
                    .commit()
        }
    }

}