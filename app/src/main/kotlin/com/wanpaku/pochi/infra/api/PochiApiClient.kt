package com.wanpaku.pochi.infra.api

import com.wanpaku.pochi.infra.api.request.*
import com.wanpaku.pochi.infra.api.response.*
import io.reactivex.Single
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import javax.inject.Inject


class PochiApiClient @Inject constructor(
        private val baseUrl: HttpUrl,
        private val client: OkHttpClient) {

    private val service: Service by lazy {
        Retrofit.Builder().baseUrl(baseUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build().create(Service::class.java)
    }

    // TODO 共通エラー処理

    fun fetchPublicAsset() = service.fetchPublicAsset()

    fun me() = service.loginUser()

    fun createuser(request: CreateUserRequest) = service.createUser(request)

    fun createDog(userId: String, request: CreateDogRequest) = service.createDog(userId, request)

    fun fetchDogs(userId: String) = service.fetchDogs(userId)

    fun fetchSitters(zipCode: String? = null, cursor: String? = null) = service.fetchSitters(zipCode, cursor)

    fun fetchSitter(sitterId: String) = service.fetchSitter(sitterId)

    fun fetchSitterEvaluates(sitterId: String) = service.fetchSitterEvaluates(sitterId)

    fun fetchUserBookings(userId: String) = service.fetchUserBookings(userId)

    fun fetchSitterBookings(sitterId: String) = service.fetchSitterBookings(sitterId)

    fun createBooking(sitterId: String) = service.createBooking(CreateBookingRequest(sitterId))

    fun requestBooking(bookingId: Long, request: RequestBookingRequest) = service.requestBooking(bookingId, request)

    fun updateBookingStatus(bookingId: Long, request: UpdateBookingStatusRequest) = service.updateBookingStatus(bookingId, request)

    fun fetchMessages(bookingId: Long, cursor: String? = null) = service.fetchMessages(bookingId, cursor)

    fun postMessage(bookingId: Long, request: CreateMessageRequest) = service.postMessage(bookingId, request)

    fun fetchUploadImageUrl(bookingId: Long) = service.fetchUploadImageUrl(bookingId)

    private interface Service {

        @GET("/pochi/v1/public_assets")
        fun fetchPublicAsset(): Single<PublicAssetResponse>

        @POST("/pochi/v1/users/{id}/dogs")
        fun createDog(@Path(value = "id") id: String, @Body param: CreateDogRequest): Single<DogResponse>

        @GET("/pochi/v1/users/{id}/dogs")
        fun fetchDogs(@Path(value = "id") userId: String): Single<ListResponse<DogResponse>>

        @POST("/pochi/v1/users/login")
        fun loginUser(): Single<UserResponse>

        @POST("/pochi/v1/users")
        fun createUser(@Body param: CreateUserRequest): Single<UserResponse>

        @GET("/pochi/v1/sitters")
        fun fetchSitters(@Query("zipCode") zipCode: String?, @Query("cursor") cursor: String?): Single<PaginationResponse<SitterResponse>>

        @GET("/pochi/v1/sitters/{sitterId}")
        fun fetchSitter(@Path(value = "sitterId") sitterId: String): Single<SitterResponse>

        @GET("/pochi/v1/sitters/{sitterId}/reviews")
        fun fetchSitterEvaluates(@Path(value = "sitterId") sitterId: String): Single<ListResponse<SitterEvaluateResponse>>

        @GET("/pochi/v1/users/{id}/bookings")
        fun fetchUserBookings(@Path(value = "id") id: String): Single<PaginationResponse<BookingResponse>>

        @GET("/pochi/v1/sitters/{id}/bookings")
        fun fetchSitterBookings(@Path(value = "id") id: String): Single<PaginationResponse<BookingResponse>>

        @POST("/pochi/v1/bookings")
        fun createBooking(@Body param: CreateBookingRequest): Single<BookingResponse>

        @PUT("/pochi/v1/bookings/{id}/request")
        fun requestBooking(@Path(value = "id") bookingId: Long, @Body param: RequestBookingRequest): Single<BookingResponse>

        @PUT("/pochi/v1/bookings/{id}/update_status")
        fun updateBookingStatus(@Path(value = "id") bookingId: Long, @Body param: UpdateBookingStatusRequest): Single<BookingResponse>

        @GET("/pochi/v1/bookings/{id}/messages")
        fun fetchMessages(@Path(value = "id") bookingId: Long, @Query("cursor") cursor: String?): Single<PaginationResponse<MessageResponse>>

        @POST("/pochi/v1/bookings/{id}/messages")
        fun postMessage(@Path(value = "id") bookingId: Long, @Body param: CreateMessageRequest): Single<MessageResponse>

        @GET("/pochi/v1/bookings/{id}/messages/image_upload")
        fun fetchUploadImageUrl(@Path(value = "id") bookingId: Long): Single<ImageUploadResponse>

    }

}