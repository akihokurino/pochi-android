package com.wanpaku.pochi.app.delegate

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import com.wanpaku.pochi.BuildConfig
import com.wanpaku.pochi.app.internal.BaseFragment
import com.wanpaku.pochi.app.internal.ViewLifecycle
import timber.log.Timber
import java.io.File
import java.io.IOException


class TakePhotoDelegate(private val cameraTakenListener: (File) -> Unit) : ViewLifecycle {

    private val TAG = TakePhotoDelegate::class.java.simpleName
    private val REQUEST_CODE = 1000
    private val AUTHORITIES = "${BuildConfig.APPLICATION_ID}.fileprovider"

    fun launchCamera(fragment: BaseFragment) {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageFile(fragment.context)?.toUri(fragment.context))
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        fragment.startActivityForResult(intent, REQUEST_CODE)
    }

    override fun onActivityResult(fragment: BaseFragment, requestCode: Int, resultCode: Int, data: Intent?) {
        when {
            requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK ->
                imageFile(fragment.context)?.let { cameraTakenListener(it) }
            else -> return super.onActivityResult(fragment, requestCode, resultCode, data)
        }
    }

    private fun imageFile(context: Context): File? {
        val img = File(context.filesDir, "image/tmp")
        if (!img.exists()) {
            try {
                img.parentFile.mkdirs()
                img.createNewFile()
            } catch (e: IOException) {
                Timber.tag(TAG).e(e, "failed to create new image file.")
                return null
            }
        }
        return img
    }

    private fun File.toUri(context: Context) = FileProvider.getUriForFile(context, AUTHORITIES, this)

}
