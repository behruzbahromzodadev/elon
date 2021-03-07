package tj.behruz.elon.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import tj.behruz.elon.base.BaseResponse
import tj.behruz.elon.helpers.Message
import tj.behruz.elon.models.AddAdsModel
import tj.behruz.elon.models.BannerPayload
import tj.behruz.elon.models.HistoryModel
import tj.behruz.elon.models.RegistrationModel
import tj.behruz.elon.repositories.HomeRepository
import java.net.ConnectException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException

class HomeViewModel : ViewModel() {

    var errorMessage: MutableLiveData<String> = MutableLiveData()
    private val handler = CoroutineExceptionHandler { _, exception ->
        when (exception) {
            is UnknownHostException -> errorMessage.postValue("Неизвестный хост, попробуйте позже.")
            is ConnectException -> errorMessage.postValue("Не удаётся подключиться к серверу, попробуйте позже.")
            is TimeoutException -> errorMessage.postValue("Время ожидания истекло, попробуйте позже.")
            else -> errorMessage.postValue("Неизвестный хост, попробуйте позже.")
        }

    }
    private var userInfo = MutableLiveData<BaseResponse<RegistrationModel>>()
    private var history = MutableLiveData<BaseResponse<HistoryModel>>()
    private var adsModel = MutableLiveData<BaseResponse<AddAdsModel>>()
    private val banners = MutableLiveData<BaseResponse<BannerPayload>>()

    fun getUserInfo(phone: String, hash: String): LiveData<BaseResponse<RegistrationModel>> {
        val response = BaseResponse<RegistrationModel>(0, "", null)

        viewModelScope.launch(handler) {
            val result = HomeRepository.getInfo(phone, hash)
            if (result.isSuccessful) {
                response.code = result.code()
                response.payload = result.body()
                response.message = Message.SUCCESS.toString()
            } else {
                response.code = result.code()
                response.payload = null
                response.message = Message.NOTFOUND.toString()

            }
            userInfo.postValue(response)
        }
        return userInfo
    }

    fun getAllHistory(
        action: String,
        method: String,
        token: String
    ): LiveData<BaseResponse<HistoryModel>> {
        val response = BaseResponse<HistoryModel>(0, "", null)
        viewModelScope.launch(handler) {

            val result = HomeRepository.getAllHistory(action, method, token)
            if (result.isSuccessful) {
                response.code = result.code()
                response.payload = result.body()
                response.message = Message.SUCCESS.toString()
            } else {
                response.code = result.code()
                response.payload = null
                response.message = Message.NOTFOUND.toString()

            }
            history.postValue(response)

        }
        return history
    }

    fun editProfile(
        home: RequestBody,
        fullName: RequestBody,
        token: RequestBody,
        editProfile: RequestBody,
        image: MultipartBody.Part
    ): LiveData<BaseResponse<AddAdsModel>> {
        val response = BaseResponse<AddAdsModel>(0, "", null)

        viewModelScope.launch(handler) {
            val result = HomeRepository.updateProfile(
                home,
                editProfile,
                token,
                image,
                fullName
            )
            if (result.isSuccessful) {
                response.code = result.code()
                response.payload = result.body()
                response.message = Message.SUCCESS.toString()
            } else {
                response.code = result.code()
                response.payload = null
                response.message = Message.NOTFOUND.toString()

            }



            adsModel.postValue(response)

        }
        return adsModel
    }

//    fun editProfile2(
//        home: RequestBody,
//        fullName: RequestBody,
//        token: RequestBody,
//        editProfile: RequestBody
//    ): LiveData<AddAdsModel> {
//        viewModelScope.launch(handler) {
//            adsModel.postValue(
//                HomeRepository.updateProfile2(home, editProfile, token, fullName).body()
//            )
//
//        }
//        return adsModel
//    }

    fun getBanners(token: String): LiveData<BaseResponse<BannerPayload>> {
        val response = BaseResponse<BannerPayload>(0, "", null)
        viewModelScope.launch(handler) {
            val result = HomeRepository.getBanners(token)
            if (result.isSuccessful) {
                response.code = result.code()
                response.payload = result.body()
                response.message = Message.SUCCESS.toString()
            } else {
                response.code = result.code()
                response.payload = null
                response.message = Message.NOTFOUND.toString()

            }
            banners.postValue(response)
        }

        return banners
    }
}