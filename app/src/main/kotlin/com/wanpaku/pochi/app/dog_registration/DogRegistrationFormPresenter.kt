package com.wanpaku.pochi.app.dog_registration

import android.view.View
import com.wanpaku.pochi.app.internal.BaseFragment
import com.wanpaku.pochi.app.internal.ViewLifecycle
import com.wanpaku.pochi.domain.repositories.AppUserRepository
import com.wanpaku.pochi.domain.repositories.DogRepository
import com.wanpaku.pochi.domain.repositories.PublicAssetRepository
import com.wanpaku.pochi.infra.api.request.toRequest
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class DogRegistrationFormPresenter @Inject constructor(
        private val publicAssetRepository: PublicAssetRepository,
        private val dogRepository: DogRepository,
        private val appUserRepository: AppUserRepository,
        private val disposables: CompositeDisposable)
    : ViewLifecycle, DogRegistrationFormContract.Presenter {

    private var view: DogRegistrationFormContract.View? = null
    private var formData: DogRegistrationFormData? = null

    override fun selectBreedType() {
        val breedTypes = publicAssetRepository.dogBreedTypes().map { it.label }
        view?.showBreedTypeSelectDialog(breedTypes)
    }

    override fun selectSizeType() {
        val sizeTypes = publicAssetRepository.dogSizeTypes().map { it.label }
        view?.showSizeTypeSelectDialog(sizeTypes)
    }

    override fun selectGenderType() {
        val genderTypes = publicAssetRepository.dogGenderTypes().map { it.label }
        view?.showGenderTypeSelectDialog(genderTypes)
    }

    override fun selectBirthday() {
        view?.showBirthdaySelectDialog()
    }

    override fun selectBirthday(year: Int, month: Int, dayOfMonth: Int) {
        val date = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth)
        view?.setBirthday(date)
    }

    override fun onFormDataInputted(validator: DogRegistrationFormData.Validator) {
        view?.setActiveRegistrationButton(validator.isValid(publicAssetRepository.dogBreedTypes(), publicAssetRepository.dogSizeTypes(), publicAssetRepository.dogGenderTypes()))
        formData = validator.to(publicAssetRepository.dogBreedTypes(), publicAssetRepository.dogSizeTypes(), publicAssetRepository.dogGenderTypes())
    }

    override fun selectRegistration() {
        formData?.let {
            val user = appUserRepository.restore()
            user ?: return@let
            view?.setIndicator(true)
            disposables.add(dogRepository.create(user.id, it.toRequest())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doFinally { view?.setIndicator(false) }
                    .subscribe({
                        view?.launchSearch()
                    }, {
                        Timber.e(it, "failed to create dog")
                        view?.showError("")
                    }))
        }
    }

    override fun onViewCreated(fragment: BaseFragment, source: View) {
        view = fragment as? DogRegistrationFormContract.View
    }

    override fun onDestroyView(fragment: BaseFragment) {
        view = null
        disposables.clear()
    }
}