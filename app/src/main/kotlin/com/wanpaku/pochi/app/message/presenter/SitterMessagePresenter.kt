package com.wanpaku.pochi.app.message.presenter

import android.view.View
import com.wanpaku.pochi.app.internal.BaseFragment
import com.wanpaku.pochi.app.message.contract.MessageContract
import com.wanpaku.pochi.domain.booking.Booking
import com.wanpaku.pochi.domain.repositories.BookingRepository
import com.wanpaku.pochi.domain.repositories.DogRepository
import com.wanpaku.pochi.domain.repositories.MessageRepository
import com.wanpaku.pochi.domain.repositories.SitterRepository
import com.wanpaku.pochi.infra.api.request.UpdateBookingStatusRequest
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class SitterMessagePresenter @Inject constructor(messageRepository: MessageRepository,
                                                 dogRepository: DogRepository,
                                                 sitterRepository: SitterRepository,
                                                 bookingRepository: BookingRepository,
                                                 disposables: CompositeDisposable)
    : MessagePresenter(messageRepository, dogRepository, sitterRepository, bookingRepository, disposables), MessageContract.Presenter.Sitter {

    private val TAG = SitterMessagePresenter::class.java.simpleName

    private var view: MessageContract.View.Sitter? = null

    override fun acceptRequest(bookingId: Long) {
        view?.setIndicator(true)
        disposables.add(bookingRepository
                .updateBookingStatus(bookingId, UpdateBookingStatusRequest(Booking.Status.Confirm.VALUE))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally { view?.setIndicator(false) }
                .subscribe({
                    view?.updateRequest(it, dogs, sitter)
                }, {
                    Timber.tag(TAG).e(it, "failed to accept request. booking id is %d", bookingId)
                }))
    }

    override fun cancelRequest(bookingId: Long) {
        view?.setIndicator(true)
        disposables.add(bookingRepository
                .updateBookingStatus(bookingId, UpdateBookingStatusRequest(Booking.Status.Cancel.VALUE))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally { view?.setIndicator(false) }
                .subscribe({
                    view?.updateRequest(it, dogs, sitter)
                }, {
                    Timber.tag(TAG).e(it, "failed to cancel request. booking id is %d", bookingId)
                }))
    }

    override fun onViewCreated(fragment: BaseFragment, source: View) {
        super.onViewCreated(fragment, source)
        view = fragment as? MessageContract.View.Sitter
    }

    override fun onDestroyView(fragment: BaseFragment) {
        view = null
        super.onDestroyView(fragment)
    }
}
