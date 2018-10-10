package com.wanpaku.pochi.app.dog_registration

interface DogRegistrationFormContract {

    interface View {

        fun showBreedTypeSelectDialog(breedTypes: List<String>): Unit

        fun showSizeTypeSelectDialog(sizeTypes: List<String>): Unit

        fun showGenderTypeSelectDialog(genders: List<String>): Unit

        fun showBirthdaySelectDialog(): Unit

        fun setActiveRegistrationButton(isActive: Boolean): Unit

        fun setBirthday(birthday: String): Unit

        fun setIndicator(isVisible: Boolean): Unit

        fun showError(message: String): Unit

        fun launchSearch(): Unit
    }

    interface Presenter {

        fun selectBreedType(): Unit

        fun selectSizeType(): Unit

        fun selectGenderType(): Unit

        fun selectBirthday(): Unit

        fun selectRegistration(): Unit

        fun selectBirthday(year: Int, month: Int, dayOfMonth: Int): Unit

        fun onFormDataInputted(validator: DogRegistrationFormData.Validator)

    }

}