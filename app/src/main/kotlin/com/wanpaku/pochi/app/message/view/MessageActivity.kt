package com.wanpaku.pochi.app.message.view

import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import com.wanpaku.pochi.R
import com.wanpaku.pochi.app.delegate.ToolbarDelegate
import com.wanpaku.pochi.app.internal.ActivityLifecycleDelegate
import com.wanpaku.pochi.app.internal.BaseActivity
import com.wanpaku.pochi.domain.booking.Booking

class MessageActivity : BaseActivity() {

    companion object {

        private val KEY_BOOKING = "key_booking"
        private val KEY_TYPE = "key_type"

        fun startActivityForUser(activity: BaseActivity, booking: Booking) {
            startActivity(activity, booking, Type.ForUser)
        }

        fun startActivityForSitter(activity: BaseActivity, booking: Booking) {
            startActivity(activity, booking, Type.ForSitter)
        }

        private fun startActivity(activity: BaseActivity, booking: Booking, type: Type) {
            val intent = Intent(activity, MessageActivity::class.java).apply {
                putExtra(KEY_BOOKING, booking)
                putExtra(KEY_TYPE, type)
            }
            activity.startActivity(intent)
        }

        private fun booking(intent: Intent) = intent.getParcelableExtra<Booking>(KEY_BOOKING)

        private fun type(intent: Intent) = intent.getSerializableExtra(KEY_TYPE) as Type

    }

    override fun lifecycleDelegates(): List<ActivityLifecycleDelegate> =
            listOf(ToolbarDelegate(icon = ContextCompat.getDrawable(this, R.drawable.ic_close_24dp),
                    displayHomeAsUpEnabled = false,
                    title = when (type(intent)) {
                        MessageActivity.Type.ForUser -> booking(intent).sitter.fullName()
                        MessageActivity.Type.ForSitter -> booking(intent).user.fullName()
                    }))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)
        if (savedInstanceState == null) {
            val fragment = when (type(intent)) {
                MessageActivity.Type.ForUser -> MessageFragment.newInstanceForUser(booking(intent))
                MessageActivity.Type.ForSitter -> MessageFragment.newInstanceForSitter(booking(intent))
            }
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit()
        }
    }

    private enum class Type {
        ForUser,
        ForSitter
    }

}
