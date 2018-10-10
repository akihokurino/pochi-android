package com.wanpaku.pochi.app.message.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.Unbinder
import com.jakewharton.rxbinding2.widget.RxTextView
import com.wanpaku.pochi.R
import com.wanpaku.pochi.infra.ui.widget.TwoStateButton
import io.reactivex.Observable
import io.reactivex.functions.Function3
import org.joda.time.DateTime
import org.joda.time.Days
import org.joda.time.format.DateTimeFormat

class UserRequestView @JvmOverloads constructor(context: Context,
                                                attrs: AttributeSet? = null,
                                                defStyleAttr: Int = 0,
                                                defStyleRes: Int = 0)
    : RequestView(context, attrs, defStyleAttr, defStyleRes) {

    override val preRequestView = UserPreRequestView(context)
    override val requestingView = UserRequestingView(context)
    override val requestAcceptedView = UserRequestAcceptedView(context)

    override fun headerTitle(): Int = R.string.request_content
}

class UserPreRequestView @JvmOverloads constructor(context: Context,
                                                   attrs: AttributeSet? = null,
                                                   defStyleAttr: Int = 0,
                                                   defStyleRes: Int = 0)
    : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

    @BindView(R.id.dogs) lateinit var dogs: TextView
    @BindView(R.id.start_date_text) lateinit var startDateText: TextView
    @BindView(R.id.end_date_text) lateinit var endDateText: TextView
    @BindView(R.id.price) lateinit var price: TextView
    @BindView(R.id.dog_count) lateinit var dogCount: View
    @BindView(R.id.start_date) lateinit var startDate: View
    @BindView(R.id.end_date) lateinit var endDate: View
    @BindView(R.id.request) lateinit var request: TwoStateButton

    private var unbinder: Unbinder? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.view_user_prerequest, this, true)
        unbinder = ButterKnife.bind(this)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        unbinder?.unbind()
    }

    fun applyViewModel(viewModel: PreRequestViewModel) {
        viewModel.dog?.let { dogs.text = it }
        viewModel.startDate?.let { startDateText.text = it }
        viewModel.endDate?.let { endDateText.text = it }
        viewModel.price?.let { price.text = it }
        viewModel.requestClickable?.let { request.isActive = it }
    }

    fun setCallback(callback: Callback) {
        dogCount.setOnClickListener { callback.onDogClicked() }
        startDate.setOnClickListener { callback.onStartDateClicked() }
        endDate.setOnClickListener { callback.onEndDateClicked() }
        request.setOnClickListener { callback.onRequestClicked() }
    }

    fun dataChangedObservable() = Observable
            .combineLatest<CharSequence, CharSequence, CharSequence, FormData>(RxTextView.textChanges(dogs),
                    RxTextView.textChanges(startDateText),
                    RxTextView.textChanges(endDateText),
                    Function3 { dogs, startDate, endDate ->
                        FormData.from(dogs.toString(), startDate.toString(), endDate.toString())
                    })

    fun formData() = FormData.from(dogs = dogs.text.toString(),
            startDate = startDateText.text.toString(),
            endDate = endDateText.text.toString())

    data class PreRequestViewModel(val dog: String?,
                                   val startDate: String?,
                                   val endDate: String?,
                                   val price: String?,
                                   val requestClickable: Boolean?)

    data class FormData(val dogs: List<String>,
                        val startDate: DateTime?,
                        val endDate: DateTime?) {

        fun isValid() = dogs.isNotEmpty() &&
                startDate != null &&
                endDate != null &&
                daySpan() > 0

        fun daySpan(): Int {
            if (startDate == null || endDate == null) return 0
            return Days.daysBetween(startDate, endDate).days
        }

        companion object {

            private val DATE_REGEX = Regex("^[0-9]{4}-[0-9]{2}-[0-9]{2}$")

            fun from(dogs: String, startDate: String, endDate: String): FormData {
                val formatter = DateTimeFormat.forPattern("yyyy-MM-dd")
                val startDateTime = if (startDate.matches(DATE_REGEX)) DateTime.parse(startDate, formatter) else null
                val endDateTime = if (endDate.matches(DATE_REGEX)) DateTime.parse(endDate, formatter) else null
                return FormData(dogs = dogs.split(",").toList(), startDate = startDateTime, endDate = endDateTime)
            }

        }

    }

    interface Callback {

        fun onDogClicked(): Unit

        fun onStartDateClicked(): Unit

        fun onEndDateClicked(): Unit

        fun onRequestClicked(): Unit

    }

}

class UserRequestingView @JvmOverloads constructor(context: Context,
                                                   attrs: AttributeSet? = null,
                                                   defStyleAttr: Int = 0,
                                                   defStyleRes: Int = 0)
    : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

    @BindView(R.id.dogs) lateinit var dogs: TextView
    @BindView(R.id.start_date_text) lateinit var startDateText: TextView
    @BindView(R.id.end_date_text) lateinit var endDateText: TextView
    @BindView(R.id.price) lateinit var price: TextView
    @BindView(R.id.cancel) lateinit var cancel: TextView

    private var unbinder: Unbinder? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.view_user_requesting, this, true)
        unbinder = ButterKnife.bind(this)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        unbinder?.unbind()
    }

    fun applyViewModel(viewModel: ViewModel) {
        dogs.text = viewModel.dog
        startDateText.text = viewModel.startDate
        endDateText.text = viewModel.endDate
        price.text = viewModel.price
    }

    fun setOnCancelClickedListener(listener: () -> Unit) {
        cancel.setOnClickListener { listener() }
    }

    data class ViewModel(val dog: String,
                         val startDate: String,
                         val endDate: String,
                         val price: String)

}

class UserRequestAcceptedView @JvmOverloads constructor(context: Context,
                                                        attrs: AttributeSet? = null,
                                                        defStyleAttr: Int = 0,
                                                        defStyleRes: Int = 0)
    : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

    @BindView(R.id.dogs) lateinit var dogs: TextView
    @BindView(R.id.start_date_text) lateinit var startDateText: TextView
    @BindView(R.id.end_date_text) lateinit var endDateText: TextView
    @BindView(R.id.price) lateinit var price: TextView
    @BindView(R.id.address) lateinit var address: TextView
    @BindView(R.id.sitter_profile) lateinit var sitterProfile: View

    private var unbinder: Unbinder? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.view_user_requestaccepted, this, true)
        unbinder = ButterKnife.bind(this)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        unbinder?.unbind()
    }

    fun applyViewModel(viewModel: ViewModel) {
        dogs.text = viewModel.dog
        startDateText.text = viewModel.startDate
        endDateText.text = viewModel.endDate
        price.text = viewModel.price
        address.text = viewModel.address
    }

    fun setOnSitterClickedListener(listener: () -> Unit) {
        sitterProfile.setOnClickListener { listener() }
    }

    data class ViewModel(val dog: String,
                         val startDate: String,
                         val endDate: String,
                         val price: String,
                         val address: String)


}