package com.wanpaku.pochi.app.booking.presenter

import android.view.View
import com.wanpaku.pochi.app.booking.contract.BookingListContract
import com.wanpaku.pochi.app.booking.view.BookingCategory
import com.wanpaku.pochi.app.booking.view.BookingType
import com.wanpaku.pochi.app.internal.BaseFragment
import com.wanpaku.pochi.app.internal.ViewLifecycle
import com.wanpaku.pochi.domain.repositories.AppUserRepository
import com.wanpaku.pochi.domain.repositories.BookingRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class BookingListPresenter @Inject constructor(private val bookingRepository: BookingRepository,
                                               private val disposables: CompositeDisposable,
                                               private val userRepository: AppUserRepository)
    : BookingListContract.Presenter, ViewLifecycle {

    private val TAG = BookingListPresenter::class.java.simpleName

    private var view: BookingListContract.View? = null

    override fun fetchBookings(bookingCategory: BookingCategory, bookingType: BookingType) {
        view?.setIndicator(true)
        val userId = userRepository.restore()?.id ?: return
        val fetchBookings = when (bookingType) {
            BookingType.ForUser -> bookingRepository.fetchUserBookings(userId)
            BookingType.ForSitter -> bookingRepository.fetchSitterBookings(userId)
        }
        disposables.add(fetchBookings
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map { it.second.filter { bookingCategory.statuses.contains(it.status) } }
                .doFinally { view?.setIndicator(false) }
                .subscribe({
                    view?.updateList(it)
                }, {
                    Timber.tag(TAG).e(it, "failed to fetch bookings")
                    view?.showError()
                }))
    }

    override fun onViewCreated(fragment: BaseFragment, source: View) {
        view = fragment as? BookingListContract.View
    }

    override fun onDestroyView(fragment: BaseFragment) {
        view = null
        disposables.clear()
    }

}
