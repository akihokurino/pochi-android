package com.wanpaku.pochi.app.message.view

import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.view.*
import android.widget.ProgressBar
import butterknife.BindView
import com.stfalcon.chatkit.commons.models.IMessage
import com.stfalcon.chatkit.commons.models.IUser
import com.stfalcon.chatkit.commons.models.MessageContentType
import com.stfalcon.chatkit.messages.MessageInput
import com.stfalcon.chatkit.messages.MessagesList
import com.stfalcon.chatkit.messages.MessagesListAdapter
import com.wanpaku.pochi.R
import com.wanpaku.pochi.app.delegate.ButterKnifeBindDelegate
import com.wanpaku.pochi.app.delegate.TakePhotoDelegate
import com.wanpaku.pochi.app.internal.BaseFragment
import com.wanpaku.pochi.app.message.contract.MessageContract
import com.wanpaku.pochi.app.message.presenter.MessagePresenter
import com.wanpaku.pochi.domain.booking.Booking
import com.wanpaku.pochi.domain.booking.Message
import com.wanpaku.pochi.domain.dog.Dog
import com.wanpaku.pochi.domain.sitter.Sitter
import com.wanpaku.pochi.domain.user.UserOverView
import com.wanpaku.pochi.infra.ui.widget.load
import java.util.*

abstract class MessageFragment<out T : RequestView> : BaseFragment(), MessageContract.View {

    companion object {

        private val KEY_BOOKING = "key_booking"

        fun newInstanceForUser(booking: Booking): MessageFragment<*> = newInstance(UserMessageFragment(), booking)

        fun newInstanceForSitter(booking: Booking): MessageFragment<*> = newInstance(SitterMessageFragment(), booking)

        private fun newInstance(fragment: MessageFragment<*>, booking: Booking) = fragment.apply {
            arguments = Bundle().apply {
                putParcelable(KEY_BOOKING, booking)
            }
        }

        private fun updateBooking(fragment: MessageFragment<*>, booking: Booking) {
            fragment.arguments = Bundle().apply {
                putParcelable(KEY_BOOKING, booking)
            }
        }

        fun booking(bundle: Bundle) = bundle.getParcelable<Booking>(KEY_BOOKING)

    }

    private val takePhotoDelegate = TakePhotoDelegate {
        presenter.sendImage(booking(arguments).id, it)
    }

    protected val requestView: T by lazy { createRequestView() }
    private val presenter by lazy { createPresenter() }
    private val adapter by lazy {
        MessagesListAdapter<ViewModel>(senderUserId(booking(arguments)))
        { imageView, url -> imageView.load(url) }
    }

    @BindView(R.id.messages_list) lateinit var messagesList: MessagesList
    @BindView(R.id.message_input) lateinit var messageInput: MessageInput
    @BindView(R.id.request_container) lateinit var requestContainer: ViewGroup
    @BindView(R.id.progress) lateinit var progress: ProgressBar

    protected abstract fun createPresenter(): MessagePresenter

    protected abstract fun senderUserId(booking: Booking): String

    protected abstract fun createRequestView(): T

    protected abstract fun updatePreRequest(booking: Booking, dogs: List<Dog>): Unit

    protected abstract fun updateRequesting(booking: Booking, dogs: List<Dog>): Unit

    protected abstract fun updateRequestAccepted(booking: Booking, dogs: List<Dog>, sitter: Sitter): Unit

    override fun viewLifecycle() = listOf(presenter, ButterKnifeBindDelegate(this), takePhotoDelegate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_message, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupMessagesList()
        setupMessageInput()
        requestContainer.addView(requestView)
        val booking = booking(arguments)
        presenter.init(booking.user.id, booking.sitter.id)
        presenter.fetchMessages(booking.id)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        val isVisible = when (booking(arguments).status) {
            Booking.Status.Confirm, Booking.Status.Stay -> true
            else -> false
        }
        menu.findItem(R.id.action_cancel).isVisible = isVisible
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_message, menu)
        menu.findItem(R.id.action_cancel).setOnMenuItemClickListener {
            presenter.cancelRequest(booking(arguments).id)
            true
        }
    }

    override fun init(dogs: List<Dog>, sitter: Sitter) {
        updateRequest(booking(arguments), dogs, sitter)
    }

    override fun updateMessages(messages: List<Message>) {
        messages.map { ViewModel(it) }.let {
            adapter.addToEnd(it, false)
        }
    }

    override fun updateNewMessage(message: Message) {
        message.let { ViewModel(it) }.let {
            adapter.addToStart(it, true)
        }
    }

    final override fun updateRequest(booking: Booking, dogs: List<Dog>, sitter: Sitter) {
        when (booking.status) {
            Booking.Status.PreRequest -> {
                requestView.replaceView(RequestView.Status.PreRequest)
                updatePreRequest(booking, dogs)
            }
            Booking.Status.Request -> {
                requestView.replaceView(RequestView.Status.Requesting)
                updateRequesting(booking, dogs)
            }
            Booking.Status.Confirm, Booking.Status.Stay -> {
                requestView.replaceView(RequestView.Status.RequestAccepted)
                updateRequestAccepted(booking, dogs, sitter)
            }
            Booking.Status.Cancel -> baseActivity.finish()
            else -> return@updateRequest
        }
        updateBooking(this, booking)
        ActivityCompat.invalidateOptionsMenu(baseActivity)
    }

    override fun setIndicator(isVisible: Boolean) {
        progress.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    private fun setupMessagesList() {
        messagesList.setAdapter(adapter)
    }

    private fun setupMessageInput() {
        messageInput.setInputListener {
            val booking = booking(arguments)
            presenter.sendMessage(booking.id, senderUserId(booking), it.toString())
            true
        }
        messageInput.setAttachmentsListener {
            takePhotoDelegate.launchCamera(this@MessageFragment)
        }
    }

    private class ViewModel(private val message: Message) : IMessage, MessageContentType.Image {

        private val SYSTEM_USER = object : IUser {

            override fun getAvatar(): String? = null

            override fun getName(): String? = null

            override fun getId(): String = "system_user_id"

        }

        override fun getId(): String = message.id.toString()

        override fun getCreatedAt(): Date = message.createdAt.toDate()

        override fun getUser(): IUser = message.from?.let { User(it) } ?: SYSTEM_USER

        override fun getText(): String = message.content ?: ""

        override fun getImageUrl(): String? = message.imageUri

        private class User(private val user: UserOverView) : IUser {

            override fun getAvatar(): String? = user.iconUri

            override fun getName(): String = user.fullName()

            override fun getId(): String = user.id
        }

    }

}
