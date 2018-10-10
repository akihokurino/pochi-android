package com.wanpaku.pochi.domain.sitter

import com.wanpaku.pochi.domain.dog.Dog
import com.wanpaku.pochi.domain.user.User
import paperparcel.PaperParcel
import paperparcel.PaperParcelable

@PaperParcel
data class Sitter(val userId: String,
                  val user: User,
                  val activated: Boolean,
                  val introductionMessage: String,
                  val acceptableDogSizes: List<Dog.SizeType>,
                  val houseType: HouseType,
                  val smokingPolicy: SmokingPolicy,
                  val kidsTypes: List<KidsType>,
                  val serviceDescription: String,
                  val dogKeepingStyle: DogKeepingStyle,
                  val walkingPolicy: WalkingPolicy,
                  val acceptableDogTypes: List<Dog.Attribute>,
                  val options: List<Option>,
                  val geoHexCode: String,
                  val scoreAverage: Double,
                  val address: Address?,
                  val mainImage: String?,
                  val interiorImages: List<Image>) : PaperParcelable {

    companion object {

        @JvmField val CREATOR = PaperParcelSitter.CREATOR

    }

    enum class HouseType(val VALUE: String) {
        Isolated("ISOLATED"),
        Apartment("APARTMENT");
    }

    enum class SmokingPolicy(val VALUE: String) {
        Smoking("SMOKING"),
        NonSmoking("NON_SMOKING"),
    }

    enum class KidsType(val VALUE: String) {
        Infant("INFANT"),
        Schoolchild("SCHOOLCHILD"),
    }

    enum class DogKeepingStyle(val VALUE: String) {
        IndoorCage("INDOOR_CAGE"),
        IndoorLoose("INDOOR_LOOSE"),
        OutdoorCage("OUTDOOR_CAGE"),
        OutdoorLoose("OUTDOOR_LOOSE"),
    }

    enum class WalkingPolicy(val VALUE: String) {
        None("NONE"),
        OnceADay("ONCE_A_DAY"),
        TwiceADay("TWICE_A_DAY"),
        ThreeTimesADay("THREE_TIMES_A_DAY"),
    }

    enum class Option(val VALUE: String) {
        EarlyMorning("EARLY_MORNING"),
        LateNight("LATE_NIGHT"),
    }

}

@PaperParcel
data class Address(val latitude: Double,
                   val longitude: Double,
                   val zipCode: String,
                   val address: String) : PaperParcelable {

    companion object {
        @JvmField val CREATOR = PaperParcelAddress.CREATOR
    }

}

@PaperParcel
data class Image(val id: Long,
                 val image: String) : PaperParcelable {

    companion object {
        @JvmField val CREATOR = PaperParcelImage.CREATOR
    }

    fun addSizeUrl(pixelSize: Int) = "$image=s$pixelSize"

}