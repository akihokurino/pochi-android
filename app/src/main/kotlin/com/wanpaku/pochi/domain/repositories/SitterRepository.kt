package com.wanpaku.pochi.domain.repositories

import com.wanpaku.pochi.domain.sitter.Sitter
import com.wanpaku.pochi.infra.api.PochiApiClient
import com.wanpaku.pochi.infra.api.response.SitterResponse
import com.wanpaku.pochi.infra.api.response.to
import com.wanpaku.pochi.infra.di.PerApplication
import io.reactivex.Single
import javax.inject.Inject

@PerApplication
class SitterRepository @Inject constructor(
        private val apiClient: PochiApiClient,
        private val publicAssetRepository: PublicAssetRepository) {

    fun fetchSitters(zipCode: String? = null, cursor: String? = null): Single<Pair<String, List<Sitter>>> =
            apiClient.fetchSitters(zipCode, cursor)
                    .map {
                        Pair(it.nextCursor, it.items.map {
                            convertSitterResponseToSitter(it)
                        })
                    }

    fun fetchSitter(sitterId: String) = apiClient.fetchSitter(sitterId)
            .map { convertSitterResponseToSitter(it) }

    fun fetchReviewes(sitterId: String) = apiClient.fetchSitterEvaluates(sitterId)
            .map { it.items.map { it.to() } }

    private fun convertSitterResponseToSitter(sitterResponse: SitterResponse) =
            sitterResponse.to(publicAssetRepository.dogSizeTypes(),
                    publicAssetRepository.sitterHouseTypes(),
                    publicAssetRepository.sitterSmokingPolicies(),
                    publicAssetRepository.sitterKidsTypes(),
                    publicAssetRepository.sitterDogKeepingStyles(),
                    publicAssetRepository.sitterWalkingPolicies(),
                    publicAssetRepository.dogAttributes(),
                    publicAssetRepository.sitterOptions())

}

class PagingSitterProvider @Inject constructor(
        private val repository: SitterRepository) {

    private var nextCursor: String? = null

    fun next(): Single<List<Sitter>> = repository.fetchSitters(nextCursor)
            .doOnSuccess { (nextCursor, _) -> this.nextCursor = nextCursor }
            .map { it.second }

}