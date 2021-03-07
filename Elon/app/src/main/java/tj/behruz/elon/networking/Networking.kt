package tj.behruz.elon.networking

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*
import tj.behruz.elon.base.LoginResponse
import tj.behruz.elon.models.*

interface Networking {

    @FormUrlEncoded
    @POST("android/")
    suspend fun loginAsync(@Field("action") login: String, @Field("method") signup_by_number: String, @Field("phone") phone: String, @Field("hash") hash: String): Response<LoginResponse>

    @FormUrlEncoded
    @POST("android/")
    suspend fun registrationAsync(@Field("action") login: String, @Field("method") signup: String, @Field("phone") phone: String, @Field("first_name") firstName: String, @Field("last_name") lastName: String, @Field("hash") hash: String): Response<RegistrationModel>

    @FormUrlEncoded
    @POST("android/")
    suspend fun getTvInfoAsync(@Field("action") home: String, @Field("method") getTvForShow: String, @Field("token") token: String): Response<TvModel>

    @FormUrlEncoded
    @POST("android/")
    suspend fun getCategoryAsync(@Field("action") home: String, @Field("method") getCategories: String, @Field("token") token: String): Response<CategoryModel>

    @FormUrlEncoded
    @POST("android/")
    suspend fun getInfoUserAsync(@Field("action") login: String, @Field("method") signin: String, @Field("phone") phone: String, @Field("hash") hash: String): Response<RegistrationModel>

    @FormUrlEncoded
    @POST("android/")
    suspend fun addAds(@Field("action") home: String, @Field("method") addRunningLine: String, @Field("token") token: String, @Field("text") text: String, @Field("phones") phone: String, @Field("tv") tv: String, @Field("count_day") day: String, @Field("bonus") bonus: String, @Field("price") price: String, @Field("category_id") category: String): Response<AddAdsModel>

    @FormUrlEncoded
    @POST("android/")
    suspend fun getAllHistoryAsync(@Field("action") action: String, @Field("method") getAllRunningLine: String, @Field("token") token: String): Response<HistoryModel>

    @Multipart
    @POST("android/")
    suspend fun updateProfile(@Part("action") home: RequestBody, @Part("method") editProfile: RequestBody, @Part("token") token: RequestBody, @Part("full_name") name: RequestBody, @Part avatar: MultipartBody.Part): Response<AddAdsModel>

    @Multipart
    @POST("android/")
    suspend fun updateProfile2(@Part("action") home: RequestBody, @Part("method") editProfile: RequestBody, @Part("token") token: RequestBody, @Part("full_name") name: RequestBody): Response<AddAdsModel>

    @FormUrlEncoded
    @POST("android/")
    suspend fun editAds(@Field("action") home: String, @Field("method") editRunningLine: String, @Field("token") token: String, @Field("text") text: String, @Field("phones") phone: String, @Field("tv_id") tv: String, @Field("count_day") day: String, @Field("bonus") bonus: String, @Field("price") price: String, @Field("category_id") category: String, @Field("id") Id: String): Response<AddAdsModel>

    @FormUrlEncoded
    @POST("android/")
    suspend fun deleteAds(@Field("action") home: String, @Field("method") cancelRunningLine: String, @Field("token") token: String, @Field("id") id: String): Response<AddAdsModel>

    @FormUrlEncoded
    @POST("android/")
    suspend fun getBanners(@Field("action") home: String, @Field("method") getBanners:String, @Field("token") token: String): Response<BannerPayload>

}