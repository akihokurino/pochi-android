package com.wanpaku.pochi.infra.api.request

import com.wanpaku.pochi.app.dog_registration.DogRegistrationFormData
import proguard.annotation.KeepClassMemberNames

@KeepClassMemberNames
data class CreateDogRequest(val name: String,
                            val breed: String,
                            val gender: String,
                            val size: String,
                            val birthdate: String)

fun DogRegistrationFormData.toRequest(): CreateDogRequest
        = CreateDogRequest(name = name,
        breed = breed.value,
        gender = gender.value,
        size = sizeType.VALUE,
        birthdate = birthday)