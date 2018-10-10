package com.wanpaku.pochi.infra.api.response

import com.wanpaku.pochi.domain.dog.Dog
import com.wanpaku.pochi.domain.sitter.Sitter
import proguard.annotation.KeepClassMemberNames

@KeepClassMemberNames
data class PublicAssetResponse(val masterData: MasterDataResponse)

@KeepClassMemberNames
data class MasterDataResponse(val dogBreedTypes: List<MasterDataObject>,
                              val dogGenderTypes: List<MasterDataObject>,
                              val dogSizeTypes: List<MasterDataObject>,
                              val dogMaxAge: Int,
                              val dogAttributes: List<MasterDataObject>,
                              val sitterHouseTypes: List<MasterDataObject>,
                              val sitterSmokingPolicies: List<MasterDataObject>,
                              val sitterKidsTypes: List<MasterDataObject>,
                              val sitterDogKeepingStyles: List<MasterDataObject>,
                              val sitterWalkingPolicies: List<MasterDataObject>,
                              val sitterAcceptableDogTypes: List<MasterDataObject>,
                              val sitterOptions: List<MasterDataObject>)

@KeepClassMemberNames
data class MasterDataObject(val label: String,
                            val value: String)

fun MasterDataObject.toBreedType(): Dog.BreedType =
        Dog.BreedType(label = label, value = value)

fun MasterDataObject.toSizeType() = Dog.SizeType
        .values().find { it.VALUE == value }!!

fun MasterDataObject.toGenderType(): Dog.GenderType =
        Dog.GenderType(label = label, value = value)

fun MasterDataObject.toAttribute() = Dog.Attribute
        .values().find { it.VALUE == value }!!

fun MasterDataObject.toHouseType() = Sitter.HouseType
        .values().find { it.VALUE == value }!!

fun MasterDataObject.toSmokingPolicy() = Sitter.SmokingPolicy
        .values().find { it.VALUE == value }!!

fun MasterDataObject.toKidsType() = Sitter.KidsType
        .values().find { it.VALUE == value }!!

fun MasterDataObject.toDogKeepingStyle() = Sitter.DogKeepingStyle
        .values().find { it.VALUE == value }!!

fun MasterDataObject.toWalkingPolicy() = Sitter.WalkingPolicy
        .values().find { it.VALUE == value }!!

fun MasterDataObject.toOptions() = Sitter.Option
        .values().find { it.VALUE == value }!!