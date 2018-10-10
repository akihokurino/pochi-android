package com.wanpaku.pochi.app.message.view

import android.os.Bundle
import android.view.View
import com.wanpaku.pochi.R
import com.wanpaku.pochi.app.message.contract.MessageContract
import com.wanpaku.pochi.app.message.presenter.MessagePresenter
import com.wanpaku.pochi.app.message.presenter.SitterMessagePresenter
import com.wanpaku.pochi.domain.booking.Booking
import com.wanpaku.pochi.domain.dog.Dog
import com.wanpaku.pochi.domain.sitter.Sitter
import javax.inject.Inject

class SitterMessageFragment : MessageFragment<SitterRequestView>(), MessageContract.View.Sitter {

    @Inject lateinit var presenter: SitterMessagePresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component.inject(this)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val booking = booking(arguments)
        requestView.requestingView.setOnAccpetClickedListener {
            presenter.acceptRequest(booking.id)
        }
        requestView.requestAcceptedView.setOnUserProfileClickedListener {
            // TODO
        }
    }


    override fun senderUserId(booking: Booking) = booking.sitter.id

    override fun createPresenter(): MessagePresenter = presenter

    override fun createRequestView() = SitterRequestView(context)

    override fun updatePreRequest(booking: Booking, dogs: List<Dog>) = Unit

    override fun updateRequesting(booking: Booking, dogs: List<Dog>) {
        val viewModel = SitterRequestingView.ViewModel(dog = dogs.map { it.name }.foldIndexed("", { i, s1, s2 -> "$s1${if (i == 0) "" else ","}$s2" }),
                startDate = booking.startDate!!,
                endDate = booking.endDate!!,
                point = getString(R.string.point, booking.maxRewardPoint!!))
        requestView.requestingView.applyViewModel(viewModel)
    }

    override fun updateRequestAccepted(booking: Booking, dogs: List<Dog>, sitter: Sitter) {
        val viewModel = SitterRequestAcceptedView.ViewModel(dog = dogs.map { it.name }.foldIndexed("", { i, s1, s2 -> "$s1${if (i == 0) "" else ","}$s2" }),
                startDate = booking.startDate!!,
                endDate = booking.endDate!!,
                point = getString(R.string.point, booking.maxRewardPoint!!))
        requestView.requestAcceptedView.applyViewModel(viewModel)
    }

}