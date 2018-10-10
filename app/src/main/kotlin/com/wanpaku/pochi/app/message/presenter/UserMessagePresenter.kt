package com.wanpaku.pochi.app.message.presenter

import android.view.View
import com.wanpaku.pochi.app.internal.BaseFragment
import com.wanpaku.pochi.app.message.contract.MessageContract
import com.wanpaku.pochi.domain.booking.Booking
import com.wanpaku.pochi.domain.repositories.BookingRepository
import com.wanpaku.pochi.domain.repositories.DogRepository
import com.wanpaku.pochi.domain.repositories.MessageRepository
import com.wanpaku.pochi.domain.repositories.SitterRepository
import com.wanpaku.pochi.infra.api.request.RequestBookingRequest
import com.wanpaku.pochi.infra.api.request.UpdateBookingStatusRequest
import com.wanpaku.pochi.infra.util.toYYYYtoMMtoDD
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.joda.time.DateTime
import timber.log.Timber
import javax.inject.Inject

class UserMessagePresenter @Inject constructor(messageRepository: MessageRepository,
                                               bookingRepository: BookingRepository,
                                               sitterRepository: SitterRepository,
                                               dogRepository: DogRepository,
                                               disposables: CompositeDisposable)
    : MessagePresenter(messageRepository, dogRepository, sitterRepository, bookingRepository, disposables), MessageContract.Presenter.User {

    private val TAG = UserMessagePresenter::class.java.simpleName

    private var view: MessageContract.View.User? = null

    override fun request(bookingId: Long, dogNames: List<String>, startDate: DateTime, endDate: DateTime, cardToken: String) {
        view?.setIndicator(true)
        val dogIds = dogNames.map { dogs.find { d -> d.name == it }!!.id }
        disposables.add(bookingRepository
                .requestBooking(bookingId, RequestBookingRequest(dogIds, startDate.toYYYYtoMMtoDD(), endDate.toYYYYtoMMtoDD(), cardToken))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally { view?.setIndicator(false) }
                .subscribe({
                    view?.updateRequest(it, dogs, sitter)
                }, {
                    Timber.tag(TAG).e(it, "failed to request request. booking id is %d", bookingId)
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
        view = fragment as MessageContract.View.User
    }

    override fun onDestroyView(fragment: BaseFragment) {
        view = null
        super.onDestroyView(fragment)
    }

}