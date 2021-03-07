package tj.behruz.elon.ui.ads

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import tj.behruz.elon.base.BaseResponse
import tj.behruz.elon.helpers.Message
import tj.behruz.elon.models.AddAdsModel
import tj.behruz.elon.models.Ads
import tj.behruz.elon.models.CategoryModel
import tj.behruz.elon.models.TvModel
import tj.behruz.elon.repositories.TvRepository
import java.net.ConnectException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException

class AddRequestViewModel() : ViewModel() {

    private var listOfTv = MutableLiveData<BaseResponse<TvModel>>()
    private var listOfCategory = MutableLiveData<BaseResponse<CategoryModel>>()
    private var adsData = MutableLiveData<AddAdsModel>()

    var errorMessage: MutableLiveData<String> = MutableLiveData()

    private val handler = CoroutineExceptionHandler { _, exception ->
        when (exception) {
            is UnknownHostException -> errorMessage.postValue("Неизвестный хост, попробуйте позже.")
            is ConnectException -> errorMessage.postValue("Не удаётся подключиться к серверу, попробуйте позже.")
            is TimeoutException -> errorMessage.postValue("Время ожидания истекло, попробуйте позже.")
            else -> errorMessage.postValue("Неизвестный хост, попробуйте позже.")
        }

    }

    fun getTvInfo(token: String): LiveData<BaseResponse<TvModel>> {
        val response = BaseResponse<TvModel>(0, "", null)
        viewModelScope.launch(handler) {
            val result = TvRepository.getTvInfo(token)
            if (result.isSuccessful) {
                response.code = result.code()
                response.message = Message.SUCCESS.toString()
                response.payload = result.body()
            } else {
                response.code == result.code()
                response.message = Message.NOTFOUND.toString()
                response.payload = null

            }

            listOfTv.postValue(response)
        }

        return listOfTv
    }


    fun getCategory(token: String): LiveData<BaseResponse<CategoryModel>> {
        val response = BaseResponse<CategoryModel>(0, "", null)
        viewModelScope.launch(handler) {
            val result = TvRepository.getCategory(token)
            if (result.isSuccessful) {
                response.code = result.code()
                response.message = Message.SUCCESS.toString()
                response.payload = result.body()
            } else {
                response.code = result.code()
                response.message = Message.NOTFOUND.toString()
                response.payload = null

            }
            listOfCategory.postValue(response)
        }

        return listOfCategory

    }

    fun addAds(ads: Ads, token: String): LiveData<AddAdsModel> {

        viewModelScope.launch(handler) {
            adsData.postValue(
                TvRepository.addCategory(
                    token,
                    ads.text.toString(),
                    ads.phone.toString(),
                    ads.tvId,
                    ads.day.toString(),
                    ads.bonus.toString(),
                    ads.price.toString(),
                    ads.category.toString()
                ).body()
            )
        }

        return adsData

    }

    fun editAds(ads: Ads, token: String, id: String): LiveData<AddAdsModel> {

        viewModelScope.launch(handler) {
            adsData.postValue(
                TvRepository.editAds(
                    token,
                    ads.text.toString(),
                    ads.phone.toString(),
                    ads.tvId.toString(),
                    ads.day.toString(),
                    ads.bonus.toString(),
                    ads.price.toString(),
                    ads.category.toString(),
                    id
                ).body()
            )
        }

        return adsData

    }

    fun deleteAds(token: String, id: String): LiveData<AddAdsModel> {

        viewModelScope.launch(handler) {
            adsData.postValue(TvRepository.deleteAds(token, id).body())
        }

        return adsData
    }

}