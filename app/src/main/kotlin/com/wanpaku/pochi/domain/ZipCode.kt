package com.wanpaku.pochi.domain

import com.wanpaku.pochi.domain.PaperParcelZipCode
import paperparcel.PaperParcel
import paperparcel.PaperParcelable

@PaperParcel
data class ZipCode(val zipCode: String): PaperParcelable {

    companion object {

        @JvmField val CREATOR = PaperParcelZipCode.CREATOR

    }

    class Validator(val zipCode: String) {

        private val REGEX = "^[0-9]{7}\$"

        fun isValid() = !zipCode.isNullOrEmpty()
                && zipCode.matches(Regex(REGEX))

        fun to(): ZipCode? = if (isValid()) ZipCode(zipCode) else null

    }

}


