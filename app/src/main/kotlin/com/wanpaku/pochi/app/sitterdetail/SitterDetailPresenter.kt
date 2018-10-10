package com.wanpaku.pochi.app.sitterdetail

import android.view.View
import com.wanpaku.pochi.app.internal.BaseFragment
import com.wanpaku.pochi.app.internal.ViewLifecycle
import com.wanpaku.pochi.domain.dog.Dog
import com.wanpaku.pochi.domain.repositories.BookingRepository
import com.wanpaku.pochi.domain.repositories.DogRepository
import com.wanpaku.pochi.domain.repositories.SitterRepository
import com.wanpaku.pochi.domain.sitter.Review
import com.wanpaku.pochi.domain.sitter.Sitter
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Function3
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class SitterDetailPresenter @Inject constructor(
        private val sitterRepository: SitterRepository,
        private val dogRepository: DogRepository,
        private val bookingRepository: BookingRepository,
        private val disposables: CompositeDisposable)
    : ViewLifecycle, SitterDetailContract.Presenter {

    private val TAG = SitterDetailPresenter::class.java.simpleName

    private var view: SitterDetailContract.View? = null

    override fun fetchSitter(sitterId: String) {
        view?.setIndicator(true)
        disposables.add(Single.zip(sitterRepository.fetchSitter(sitterId),
                sitterRepository.fetchReviewes(sitterId),
                dogRepository.fetchDogs(sitterId),
                Function3<Sitter, List<Review>, List<Dog>, Triple<Sitter, List<Review>, List<Dog>>> { t1, t2, t3 -> Triple(t1, t2, t3) })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally { view?.setIndicator(false) }
                .subscribe({
                    view?.update(it.first, it.second, it.third)
                }, {
                    Timber.tag(TAG).e(it, "failed to fetch sitter")
                }))
    }

    override fun createBooking(sitterId: String) {
        view?.setIndicator(true)
        disposables.add(bookingRepository
                .createBooking(sitterId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally { view?.setIndicator(false) }
                .subscribe({
                    view?.launchMessage(it)
                }, {
                    Timber.tag(TAG).e(it, "failed to create booking. sitterId is %s", sitterId)
                })
        )
    }

    override fun onViewCreated(fragment: BaseFragment, source: View) {
        view = fragment as? SitterDetailContract.View
    }

    override fun onDestroyView(fragment: BaseFragment) {
        view = null
        disposables.clear()
    }
}
