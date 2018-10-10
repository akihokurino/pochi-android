package com.wanpaku.pochi.app.search.contract

import com.wanpaku.pochi.domain.sitter.Sitter

interface SearchContract {

    interface View {

        fun setIndicator(isVisible: Boolean): Unit

        fun updateList(sitters: List<Sitter>, isLoggedIn: Boolean): Unit

        fun showError(message: String): Unit

    }

    interface Presenter {

        fun fetchSitters(): Unit

    }

}
