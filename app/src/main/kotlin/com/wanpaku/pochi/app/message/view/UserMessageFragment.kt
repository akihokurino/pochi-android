package com.wanpaku.pochi.app.message.view

import android.app.DatePickerDialog
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.View
import com.wanpaku.pochi.R
import com.wanpaku.pochi.app.delegate.CompositeDisposables
import com.wanpaku.pochi.app.delegate.CreditCardDelegate
import com.wanpaku.pochi.app.delegate.DisposableDelegate
import com.wanpaku.pochi.app.internal.ViewLifecycle
import com.wanpaku.pochi.app.message.contract.MessageContract
import com.wanpaku.pochi.app.message.presenter.MessagePresenter
import com.wanpaku.pochi.app.message.presenter.UserMessagePresenter
import com.wanpaku.pochi.domain.booking.Booking
import com.wanpaku.pochi.domain.dog.Dog
import com.wanpaku.pochi.domain.sitter.Sitter
import java.util.*
import javax.inject.Inject

class UserMessageFragment : MessageFragment<UserRequestView>(), MessageContract.View.User, RequestConfirmDialog.Callback, CompositeDisposables by DisposableDelegate() {

    @Inject lateinit var presenter: UserMessagePresenter

    private val creditCardDelegate = CreditCardDelegate(object : CreditCardDelegate.Callback {
        override fun onSuccess() {
            RequestConfirmDialog.newInstance().show(childFragmentManager, RequestConfirmDialog.TAG)
        }

        override fun onFailure() {

        }
    })

    override fun viewLifecycle(): List<ViewLifecycle> {
        return super.viewLifecycle().plus(creditCardDelegate)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component.inject(this)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        add(requestView.preRequestView
                .dataChangedObservable()
                .subscribe {
                    val price = priceMessageExcludeTax(it.dogs.size, it.daySpan(), booking(arguments).unitPrice)
                    val viewModel = makePreRequestViewModel(requestClickable = it.isValid(), price = price)
                    requestView.preRequestView.applyViewModel(viewModel)
                })

        requestView.requestingView.setOnCancelClickedListener {
            presenter.cancelRequest(booking(arguments).id)
        }

        requestView.requestAcceptedView.setOnSitterClickedListener {
            // TODO
        }
    }

    override fun onDestroyView() {
        clear()
        super.onDestroyView()
    }

    override fun senderUserId(booking: Booking) = booking.user.id

    override fun createPresenter(): MessagePresenter = presenter

    override fun createRequestView() = UserRequestView(context)

    override fun updatePreRequest(booking: Booking, dogs: List<Dog>) {

        fun showDogSelectDialog(): Unit {
            AlertDialog.Builder(context).setTitle(R.string.select_dog)
                    .setItems(dogs.map { it.name }.toTypedArray(), { dialog, which ->
                        requestView.preRequestView.applyViewModel(makePreRequestViewModel(dog = dogs[which].name))
                    }).show()
        }

        fun showDateSelectDialog(isStartDate: Boolean): Unit {
            val calendar = Calendar.getInstance()
            DatePickerDialog(context, { picker, year, month, dayOfMonth ->
                val date = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth)
                val viewModel = if (isStartDate) makePreRequestViewModel(startDate = date) else makePreRequestViewModel(endDate = date)
                requestView.preRequestView.applyViewModel(viewModel)
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        val unset = getString(R.string.unset)
        val price = priceMessageExcludeTax(0, 0, booking.unitPrice)
        val viewModel = makePreRequestViewModel(dog = unset, startDate = unset, endDate = unset, price = price)
        requestView.preRequestView.applyViewModel(viewModel)
        requestView.preRequestView.setCallback(object : UserPreRequestView.Callback {
            override fun onDogClicked(): Unit = showDogSelectDialog()

            override fun onStartDateClicked(): Unit = showDateSelectDialog(isStartDate = true)

            override fun onEndDateClicked(): Unit = showDateSelectDialog(isStartDate = false)

            override fun onRequestClicked() = creditCardDelegate.showCreditCardForm(this@UserMessageFragment)
        })
    }

    override fun updateRequesting(booking: Booking, dogs: List<Dog>) {
        val viewModel = makeRequestingViewModel(booking, dogs)
        requestView.requestingView.applyViewModel(viewModel)
    }

    override fun updateRequestAccepted(booking: Booking, dogs: List<Dog>, sitter: Sitter) {
        val viewModel = makeRequestAcceptedViewModel(booking, dogs, sitter)
        requestView.requestAcceptedView.applyViewModel(viewModel)
    }

    override fun onSendRequestSelected() {
        val formData = requestView.preRequestView.formData()
        if (!formData.isValid()) return
        val token = creditCardDelegate.token ?: return
        val bookingId = booking(arguments).id
        presenter.request(bookingId, formData.dogs, formData.startDate!!, formData.endDate!!, token)
    }

    private fun makePreRequestViewModel(dog: String? = null, startDate: String? = null, endDate: String? = null, price: String? = null, requestClickable: Boolean? = null): UserPreRequestView.PreRequestViewModel {
        return UserPreRequestView.PreRequestViewModel(dog = dog,
                startDate = startDate,
                endDate = endDate,
                price = price,
                requestClickable = requestClickable)
    }

    private fun makeRequestingViewModel(booking: Booking, dogs: List<Dog>): UserRequestingView.ViewModel {
        return UserRequestingView.ViewModel(dog = dogNames(booking.dogIds.toList(), dogs),
                startDate = booking.startDate!!,
                endDate = booking.endDate!!,
                price = priceMessageIncludeTax(booking))
    }

    private fun makeRequestAcceptedViewModel(booking: Booking, dogs: List<Dog>, sitter: Sitter): UserRequestAcceptedView.ViewModel {
        return UserRequestAcceptedView.ViewModel(dog = dogNames(booking.dogIds.toList(), dogs),
                startDate = booking.startDate!!,
                endDate = booking.endDate!!,
                price = priceMessageIncludeTax(booking),
                address = sitter.address?.address ?: getString(R.string.address_is_not_registered))
    }

    // TODO
    private fun dogNames(dogIds: List<Long>, dogs: List<Dog>) = dogIds.map { dogs.find { d -> d.id == it }!!.name }
            .foldIndexed("", { i, s1, s2 -> "$s1${if (i != 0) "," else ""}$s2" })

    // TODO
    private fun priceMessageIncludeTax(booking: Booking) = getString(R.string.price_text,
            booking.dogIds.size, booking.days, booking.unitPrice, booking.totalChargePrice)

    private fun priceMessageExcludeTax(dogCount: Int, days: Int, unitPrice: Int)
            = getString(R.string.price_text_tax_excluded, dogCount, days, unitPrice, unitPrice * dogCount * days)

}