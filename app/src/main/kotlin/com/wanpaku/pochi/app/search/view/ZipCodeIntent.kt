package com.wanpaku.pochi.app.search.view

import android.content.Intent
import com.wanpaku.pochi.domain.ZipCode

object ZipCodeIntent {

    private val KEY_ZIP_CODE = "key_zip_code"

    fun makeIntent(zipCode: ZipCode) = Intent().apply {
        putExtra(KEY_ZIP_CODE, zipCode)
    }

    fun extractZipCode(intent: Intent) =
            intent.getParcelableExtra<ZipCode>(KEY_ZIP_CODE)

}
