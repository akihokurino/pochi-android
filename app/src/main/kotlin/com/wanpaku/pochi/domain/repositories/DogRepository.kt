package com.wanpaku.pochi.domain.repositories

import com.wanpaku.pochi.infra.api.PochiApiClient
import com.wanpaku.pochi.infra.api.request.CreateDogRequest
import com.wanpaku.pochi.infra.api.response.DogResponse
import com.wanpaku.pochi.infra.api.response.to
import com.wanpaku.pochi.infra.di.PerApplication
import javax.inject.Inject

@PerApplication
class DogRepository @Inject constructor(private val apiClient: PochiApiClient,
                                        private val publicAssetRepository: PublicAssetRepository) {

    fun create(userId: String, request: CreateDogRequest) = apiClient
            .createDog(userId, request)
            .map { convertDogResponseToDog(it) }

    fun fetchDogs(userId: String) = apiClient
            .fetchDogs(userId)
            .map { it.items.map { convertDogResponseToDog(it) } }

    private fun convertDogResponseToDog(dogResponse: DogResponse) =
            dogResponse.to(publicAssetRepository.dogBreedTypes(),
                    publicAssetRepository.dogGenderTypes(),
                    publicAssetRepository.dogSizeTypes())

}