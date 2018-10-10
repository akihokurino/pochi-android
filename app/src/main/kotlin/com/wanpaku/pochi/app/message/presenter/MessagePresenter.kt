package com.wanpaku.pochi.app.message.presenter

import android.net.Uri
import android.view.View
import com.wanpaku.pochi.app.internal.BaseFragment
import com.wanpaku.pochi.app.internal.ViewLifecycle
import com.wanpaku.pochi.app.message.contract.MessageContract
import com.wanpaku.pochi.domain.dog.Dog
import com.wanpaku.pochi.domain.repositories.BookingRepository
import com.wanpaku.pochi.domain.repositories.DogRepository
import com.wanpaku.pochi.domain.repositories.MessageRepository
import com.wanpaku.pochi.domain.repositories.SitterRepository
import com.wanpaku.pochi.domain.sitter.Sitter
import com.wanpaku.pochi.infra.api.request.CreateMessageRequest
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.io.File

abstract class MessagePresenter(private val messageRepository: MessageRepository,
                                private val dogRepository: DogRepository,
                                private val sitterRepository: SitterRepository,
                                protected val bookingRepository: BookingRepository,
                                protected val disposables: CompositeDisposable) : MessageContract.Presenter, ViewLifecycle {

    private val TAG = MessagePresenter::class.java.simpleName

    private var view: MessageContract.View? = null

    protected lateinit var dogs: List<Dog>
    protected lateinit var sitter: Sitter

    override fun init(userId: String, sitterId: String) {
        view?.setIndicator(true)
        disposables.add(Single
                .zip<List<Dog>, Sitter, Pair<List<Dog>, Sitter>>(
                        dogRepository.fetchDogs(userId),
                        sitterRepository.fetchSitter(sitterId),
                        BiFunction { t1, t2 -> Pair(t1, t2) })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally { view?.setIndicator(false) }
                .subscribe({
                    dogs = it.first
                    sitter = it.second
                    view?.init(dogs, sitter)
                }, {
                    Timber.tag(TAG).e(it, "failed to fetch dogs. user id is %s. failed to fetch sitter. sitter id is %d", userId, sitterId)
                }))
    }

    override fun fetchMessages(bookingId: Long) {
        disposables.add(messageRepository.fetchMessages(bookingId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view?.updateMessages(it.second)
                }, {
                    Timber.tag(TAG).e(it, "failed to fetch messages. booking id is %d", bookingId)
                }))
    }

    override fun sendMessage(bookingId: Long, fromUserId: String, message: String) {
        disposables.add(messageRepository.postMessage(bookingId, CreateMessageRequest(fromUserId, message))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view?.updateNewMessage(it)
                }, {
                    Timber.tag(TAG).e(it, "failed to post message. booking id is %d. from user id is %s. message is %s", bookingId, fromUserId, message)
                })

        )
    }

    override fun sendImage(bookingId: Long, file: File) {
        disposables.add(messageRepository.uploadImageUrl(bookingId)
                .flatMap { messageRepository.uploadImage(file, it) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view?.updateNewMessage(it)
                }, {
                    Timber.tag(TAG).e(it, "failed to upload image. booking id is %d", bookingId)
                }))
    }

    override fun onViewCreated(fragment: BaseFragment, source: View) {
        view = fragment as? MessageContract.View
    }

    override fun onDestroyView(fragment: BaseFragment) {
        view = null
        disposables.clear()
    }
}
