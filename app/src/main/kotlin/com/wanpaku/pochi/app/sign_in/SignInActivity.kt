package com.wanpaku.pochi.app.sign_in

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.wanpaku.pochi.R
import com.wanpaku.pochi.app.delegate.ToolbarDelegate
import com.wanpaku.pochi.app.internal.BaseActivity

class SignInActivity : BaseActivity() {

    companion object {
        fun startActivity(context: Context) {
            val i = Intent(context, SignInActivity::class.java)
            context.startActivity(i)
        }
    }

    override fun lifecycleDelegates() = listOf(
            ToolbarDelegate(title = getString(R.string.activity_sign_in_title),
                    displayHomeAsUpEnabled = true)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        supportFragmentManager
                .beginTransaction()
                .add(R.id.container, SignInFragment.newInstance())
                .commit()
    }

}
