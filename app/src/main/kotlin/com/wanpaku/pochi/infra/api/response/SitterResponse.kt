package com.wanpaku.pochi.infra.api.response

import com.wanpaku.pochi.domain.sitter.*
import com.wanpaku.pochi.infra.util.toDateTime
import proguard.annotation.KeepClassMemberNames


@KeepClassMemberNames
data class SitterResponse(val userId: String,
                          val user: UserResponse,
                          val activated: Boolean,
                          val introductionMessage: String,
                          val acceptableDogSizes: List<String>,
                          val houseType: String,
                          val smokingPolicy: String,
                          val kidsTypes: List<String>,
                          val serviceDescription: String,
                          val dogKeepingStyle: String,
                          val walkingPolicy: String,
                          val acceptableDogTypes: List<String>,
                          val options: List<String>,
                          val geoHexCode: String,
                          val scoreAverage: Double,
                          val createdAt: Long,
                          val updatedAt: Long,
                          val address: AddressResponse?,
                          val mainImage: String?,
                          val interiorImages: List<ImageResponse>?)

@KeepClassMemberNames
data class AddressResponse(val latitude: Double,
                           val longitude: Double,
                           val zipCode: String,
                           val address: String)

@KeepClassMemberNames
data class ImageResponse(val id: Long,
                         val image: String)

@KeepClassMemberNames
data class SitterEvaluateResponse(val reviewer: UserOverViewResponse?,
                                  val score: Int,
                                  val comment: String,
                                  val createdAt: Long)

fun SitterResponse.to(dogSizeTypes: List<MasterDataObject>,
                      houseTypes: List<MasterDataObject>,
                      smokingPolicies: List<MasterDataObject>,
                      kidsTypes: List<MasterDataObject>,
                      dogKeepingStyles: List<MasterDataObject>,
                      walkingPolicies: List<MasterDataObject>,
                      dogAttributes: List<MasterDataObject>,
                      options: List<MasterDataObject>) = Sitter(userId = userId,
        user = user.to(),
        activated = activated,
        introductionMessage = introductionMessage,
        acceptableDogSizes = acceptableDogSizes.map { size -> dogSizeTypes.find { it.value == size }!!.toSizeType() },
        houseType = houseTypes.find { it.value == houseType }!!.toHouseType(),
        smokingPolicy = smokingPolicies.find { it.value == smokingPolicy }!!.toSmokingPolicy(),
        kidsTypes = this.kidsTypes.map { type -> kidsTypes.find { it.value == type }!!.toKidsType() },
        serviceDescription = serviceDescription,
        dogKeepingStyle = dogKeepingStyles.find { it.value == dogKeepingStyle }!!.toDogKeepingStyle(),
        walkingPolicy = walkingPolicies.find { it.value == walkingPolicy }!!.toWalkingPolicy(),
        acceptableDogTypes = acceptableDogTypes.map { type -> dogAttributes.find { it.value == type }!!.toAttribute() },
        options = this.options.map { option -> options.find { it.value == option }!!.toOptions() },
        geoHexCode = geoHexCode,
        scoreAverage = scoreAverage,
        address = address?.to(),
        mainImage = mainImage,
        interiorImages = interiorImages?.map { it.to() } ?: emptyList())

fun AddressResponse.to() = Address(latitude = latitude,
        longitude = longitude,
        zipCode = zipCode,
        address = address)

fun ImageResponse.to() = Image(id = id,
        image = image)

fun SitterEvaluateResponse.to() = Review(reviewer = reviewer?.to(),
        score = score,
        comment = comment,
        createdAt = createdAt.toDateTime())