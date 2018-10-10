package com.wanpaku.pochi.app.delegate

import android.content.Intent
import co.omise.android.models.Token
import co.omise.android.ui.CreditCardActivity
import com.wanpaku.pochi.BuildConfig
import com.wanpaku.pochi.app.internal.BaseFragment
import com.wanpaku.pochi.app.internal.ViewLifecycle


class CreditCardDelegate(private val callback: Callback) : ViewLifecycle {

    private val REQUEST_CODE = 100

    var token: String? = null
        private set

    fun showCreditCardForm(fragment: BaseFragment) {
        val intent = Intent(fragment.activity, CreditCardActivity::class.java)
        intent.putExtra(CreditCardActivity.EXTRA_PKEY, BuildConfig.OMISE_PUBLIC_KEY)
        fragment.startActivityForResult(intent, REQUEST_CODE)
    }

    override fun onActivityResult(fragment: BaseFragment, requestCode: Int, resultCode: Int, data: Intent?) {
        when {
            requestCode == REQUEST_CODE && resultCode == CreditCardActivity.RESULT_CANCEL -> callback.onFailure()
            requestCode == REQUEST_CODE && data != null-> {
                token = data.getStringExtra(CreditCardActivity.EXTRA_TOKEN)
                callback.onSuccess()
            }
            else -> super.onActivityResult(fragment, requestCode, resultCode, data)
        }
    }

    interface Callback {

        fun onSuccess(): Unit

        fun onFailure(): Unit

    }

}