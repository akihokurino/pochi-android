package com.wanpaku.pochi.app.dog_registration

import android.app.DatePickerDialog
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.Unbinder
import com.jakewharton.rxbinding2.widget.RxTextView
import com.wanpaku.pochi.R
import com.wanpaku.pochi.app.internal.BaseFragment
import com.wanpaku.pochi.app.internal.ViewLifecycle
import com.wanpaku.pochi.app.maintab.MainTabActivity
import com.wanpaku.pochi.infra.ui.widget.TwoStateButton
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Function5
import java.util.*
import javax.inject.Inject

class DogRegistrationFormFragment : BaseFragment(), DogRegistrationFormContract.View {

    companion object {

        fun newInstance() = DogRegistrationFormFragment()

    }

    @BindView(R.id.name_edittext)
    lateinit var nameEditText: EditText

    @BindView(R.id.breed_type_edittext)
    lateinit var breedTypeEditText: EditText

    @BindView(R.id.size_type_edittext)
    lateinit var sizeTypeEditText: EditText

    @BindView(R.id.gender_type_edittext)
    lateinit var genderTypeEditText: EditText

    @BindView(R.id.birthday_edittext)
    lateinit var birthdayEditText: EditText

    @BindView(R.id.registration)
    lateinit var registrationButton: TwoStateButton

    @BindView(R.id.progress_bar)
    lateinit var progress: ProgressBar

    private lateinit var unbinder: Unbinder

    @Inject lateinit var presenter: DogRegistrationFormPresenter

    private val disposable = CompositeDisposable()

    override fun viewLifecycle(): List<ViewLifecycle> = listOf(presenter)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_dog_registration, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view ?: return
        unbinder = ButterKnife.bind(this, view)
        setupView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        unbinder.unbind()
        disposable.clear()
    }

    private fun setupView() {
        breedTypeEditText.setOnClickListener { presenter.selectBreedType() }
        sizeTypeEditText.setOnClickListener { presenter.selectSizeType() }
        genderTypeEditText.setOnClickListener { presenter.selectGenderType() }
        birthdayEditText.setOnClickListener { presenter.selectBirthday() }
        registrationButton.setOnClickListener { presenter.selectRegistration() }

        val nameObs = RxTextView.textChanges(nameEditText)
        val breedTypeObs = RxTextView.textChanges(breedTypeEditText)
        val sizeTypeObs = RxTextView.textChanges(sizeTypeEditText)
        val genderTypeObs = RxTextView.textChanges(genderTypeEditText)
        val birthdayObs = RxTextView.textChanges(birthdayEditText)

        disposable.add(Observable
                .combineLatest<CharSequence, CharSequence, CharSequence, CharSequence, CharSequence, DogRegistrationFormData.Validator>(
                        nameObs,
                        breedTypeObs,
                        sizeTypeObs,
                        genderTypeObs,
                        birthdayObs,
                        Function5 { name, breedType, sizeType, genderType, birthday ->
                            DogRegistrationFormData.Validator(name = name.toString(),
                                    breedTyoe = breedType.toString(),
                                    sizeType = sizeType.toString(),
                                    genderType = genderType.toString(),
                                    birthday = birthday.toString())
                        }
                ).subscribe { presenter.onFormDataInputted(it) })
    }

    override fun showBreedTypeSelectDialog(breedTypes: List<String>) {
        AlertDialog.Builder(context).setItems(breedTypes.toTypedArray(), { dialog, which ->
            breedTypeEditText.setText(breedTypes.get(which))
        }).show()
    }

    override fun showSizeTypeSelectDialog(sizeTypes: List<String>) {
        AlertDialog.Builder(context).setItems(sizeTypes.toTypedArray(), { dialog, which ->
            sizeTypeEditText.setText(sizeTypes.get(which))
        }).show()
    }

    override fun showGenderTypeSelectDialog(genders: List<String>) {
        AlertDialog.Builder(context).setItems(genders.toTypedArray(), { dialog, which ->
            genderTypeEditText.setText(genders.get(which))
        }).show()
    }

    override fun showBirthdaySelectDialog() {
        val calendar = Calendar.getInstance()
        DatePickerDialog(context, { picker, year, month, dayOfMonth ->
            presenter.selectBirthday(year, month, dayOfMonth)
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
    }

    override fun setBirthday(birthday: String) {
        birthdayEditText.setText(birthday)
    }

    override fun setActiveRegistrationButton(isActive: Boolean) {
        registrationButton.isActive = isActive
    }

    override fun setIndicator(isVisible: Boolean) {
        progress.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    override fun showError(message: String) {

    }

    override fun launchSearch() {
        MainTabActivity.startActivity(context)
    }

}