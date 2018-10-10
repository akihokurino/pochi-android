package com.wanpaku.pochi.domain.dog

data class Dog(val id: Long,
               val name: String,
               val breed: BreedType,
               val gender: GenderType,
               val size: SizeType,
               val age: Int,
               val birthdate: String, // TODO DateTimeにする
               val isCastrated: Boolean,
               val iconUrl: String?) {

    data class GenderType(
            val label: String,
            val value: String)

    data class BreedType(
            val label: String,
            val value: String)

    enum class SizeType(val VALUE: String) {
        Large("LARGE"),
        Medium("MEDIUM"),
        Small("SMALL");
    }

    enum class Attribute(val VALUE: String) {
        CastratedMale("CASTRATED_MALE"),
        CastratedFemale("CASTRATED_FEMALE"),
        UnCastratedMale("UN_CASTRATED_MALE"),
        UnCastratedFemale("UN_CASTRATED_FEMALE"),
        HasChewingHabit("HAS_CHEWING_HABIT"),
        HasBarkHabit("HAS_BARK_HABIT"),
        UnderOneYearOld("UNDER_ONE_YEAR_OLD"),
        OverTenYearsOld("OVER_TEN_YEARS_OLD"),
        PottyIndoor("POTTY_INDOOR"),
        PottyOutdoor("POTTY_OUTDOOR"),
        Indoor("INDOOR"),
        Outdoor("OUTDOOR"),
    }

}