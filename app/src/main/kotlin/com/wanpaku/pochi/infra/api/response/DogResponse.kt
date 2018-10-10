package com.wanpaku.pochi.infra.api.response

import com.wanpaku.pochi.domain.dog.Dog
import proguard.annotation.KeepClassMemberNames

@KeepClassMemberNames
data class DogResponse(val id: Long,
                       val userId: String,
                       val name: String,
                       val breed: String,
                       val gender: String,
                       val size: String,
                       val age: Int,
                       val birthdate: String,
                       val isCastrated: Boolean,
                       val iconUrl: String?,
                       val createdAt: Long,
                       val updatedAt: Long)

fun DogResponse.to(breedTypes: List<MasterDataObject>, genderTypes: List<MasterDataObject>, sizeTypes: List<MasterDataObject>): Dog
        = Dog(id = id,
        name = name,
        breed = breedTypes.find { it.value == breed }!!.toBreedType(),
        gender = genderTypes.find { it.value == gender }!!.toGenderType(),
        size = sizeTypes.find { it.value == size }!!.toSizeType(),
        age = age,
        birthdate = birthdate,
        isCastrated = isCastrated,
        iconUrl = iconUrl)

