package com.wanpaku.pochi.app.dog_registration

import android.content.Context
import android.content.Intent
import android.os.Bundle
import butterknife.ButterKnife
import butterknife.OnClick
import com.wanpaku.pochi.R
import com.wanpaku.pochi.app.delegate.ToolbarDelegate
import com.wanpaku.pochi.app.internal.BaseActivity

class DogRegistrationActivity : BaseActivity() {

    companion object {
        fun startActivity(context: Context) {
            val intent = Intent(context, DogRegistrationActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun lifecycleDelegates() = listOf(
            ToolbarDelegate(title = getString(R.string.activity_sign_up_pet_title),
                    displayHomeAsUpEnabled = true)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dog_registration)
        ButterKnife.bind(this)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, DogRegistrationFormFragment.newInstance())
                    .commit()
        }
    }

    @OnClick(R.id.skip)
    fun onSkipClicked() {
    }

}
