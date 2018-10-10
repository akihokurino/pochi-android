package com.wanpaku.pochi.domain.repositories

import com.wanpaku.pochi.infra.api.PochiApiClient
import com.wanpaku.pochi.infra.api.response.MasterDataObject
import com.wanpaku.pochi.infra.api.response.PublicAssetResponse
import com.wanpaku.pochi.infra.di.PerApplication
import io.reactivex.Completable
import javax.inject.Inject

@PerApplication
class PublicAssetRepository @Inject constructor(private val apiClient: PochiApiClient) {

    private var response: PublicAssetResponse? = null

    fun setup(): Completable = apiClient.fetchPublicAsset()
            .doOnSuccess { response = it }
            .toCompletable()

    fun dogBreedTypes(): List<MasterDataObject> {
        checkNotNull(response) { "must call setup" }
        return response!!.masterData.dogBreedTypes
    }

    fun dogSizeTypes(): List<MasterDataObject> {
        checkNotNull(response) { "must call setup" }
        return response!!.masterData.dogSizeTypes
    }

    fun dogGenderTypes(): List<MasterDataObject> {
        checkNotNull(response) { "must call setup" }
        return response!!.masterData.dogGenderTypes
    }

    fun dogMaxAge(): Int {
        checkNotNull(response) { "must call setup" }
        return response!!.masterData.dogMaxAge
    }

    fun dogAttributes(): List<MasterDataObject> {
        checkNotNull(response) { "must call setup" }
        return response!!.masterData.dogAttributes
    }

    fun sitterHouseTypes(): List<MasterDataObject> {
        checkNotNull(response) { "must call setup" }
        return response!!.masterData.sitterHouseTypes
    }

    fun sitterSmokingPolicies(): List<MasterDataObject> {
        checkNotNull(response) { "must call setup" }
        return response!!.masterData.sitterSmokingPolicies
    }

    fun sitterKidsTypes(): List<MasterDataObject> {
        checkNotNull(response) { "must call setup" }
        return response!!.masterData.sitterKidsTypes
    }

    fun sitterDogKeepingStyles(): List<MasterDataObject> {
        checkNotNull(response) { "must call setup" }
        return response!!.masterData.sitterDogKeepingStyles
    }

    fun sitterWalkingPolicies(): List<MasterDataObject> {
        checkNotNull(response) { "must call setup" }
        return response!!.masterData.sitterWalkingPolicies
    }

    fun sitterOptions(): List<MasterDataObject> {
        checkNotNull(response) { "must call setup" }
        return response!!.masterData.sitterOptions
    }

}