package com.wanpaku.pochi.app.search.contract

import com.wanpaku.pochi.domain.ZipCode
import com.wanpaku.pochi.domain.sitter.Sitter

interface SearchResultContract {

    interface View {

        fun updateList(sitters: List<Sitter>): Unit

        fun setIndicator(isVisible: Boolean): Unit

        fun showError(): Unit

    }

    interface Presenter {

        fun fetchSitters(zipCode: ZipCode): Unit

    }

}
