package com.wanpaku.pochi.app.message.view

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import com.wanpaku.pochi.R

class RequestConfirmDialog : DialogFragment() {

    companion object {

        val TAG = RequestConfirmDialog::class.java.simpleName

        fun newInstance() = RequestConfirmDialog()
    }

    private val callback: Callback by lazy {
        parentFragment as Callback
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_request_confirm, null, false)
        view.findViewById<View>(R.id.send_request).setOnClickListener {
            callback.onSendRequestSelected()
            dismiss()
        }
        view.findViewById<View>(R.id.cancel_request).setOnClickListener {
            dismiss()
        }
        return AlertDialog.Builder(context)
                .setView(view)
                .create()
    }

    interface Callback {

        fun onSendRequestSelected(): Unit

    }

}
