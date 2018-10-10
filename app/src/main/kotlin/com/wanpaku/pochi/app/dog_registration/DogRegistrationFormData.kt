package com.wanpaku.pochi.app.dog_registration

import android.text.TextUtils
import com.wanpaku.pochi.domain.dog.Dog
import com.wanpaku.pochi.infra.api.response.*

data class DogRegistrationFormData(
        val name: String,
        val breed: Dog.BreedType,
        val sizeType: Dog.SizeType,
        val gender: Dog.GenderType,
        val birthday: String) {

    class Validator(private val name: String,
                    private val breedTyoe: String,
                    private val sizeType: String,
                    private val genderType: String,
                    private val birthday: String) {

        private val MAX_NAME_LENGTH = 16

        fun isValid(breedTypes: List<MasterDataObject>, sizes: List<MasterDataObject>, genderTypes: List<MasterDataObject>): Boolean {
            return isValidName()
                    && isValidBreedType(breedTypes)
                    && isValidSizeType(sizes)
                    && isValidGenderType(genderTypes)
                    && isValidBirthday()
        }

        private fun isValidName() = !TextUtils.isEmpty(name) && name.length <= MAX_NAME_LENGTH

        private fun isValidBreedType(breedTypes: List<MasterDataObject>) =
                !TextUtils.isEmpty(breedTyoe) && breedTypes.find { it.label == breedTyoe } != null

        private fun isValidSizeType(sizes: List<MasterDataObject>) =
                !TextUtils.isEmpty(sizeType) && sizes.find { it.label == sizeType } != null

        private fun isValidGenderType(genderTypes: List<MasterDataObject>) =
                !TextUtils.isEmpty(genderType) && genderTypes.findLast { it.label == genderType } != null

        private fun isValidBirthday() = !TextUtils.isEmpty(birthday)

        fun to(breedTypes: List<MasterDataObject>, sizes: List<MasterDataObject>, genderTypes: List<MasterDataObject>): DogRegistrationFormData? {
            if (isValid(breedTypes, sizes, genderTypes)) return DogRegistrationFormData(name = name,
                    breed = breedTypes.find { it.label == breedTyoe }!!.toBreedType(),
                    sizeType = sizes.find { it.label == sizeType }!!.toSizeType(),
                    gender = genderTypes.findLast { it.label == genderType }!!.toGenderType(),
                    birthday = birthday)
            return null
        }

    }

}