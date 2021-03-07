package tj.behruz.elon.repositories


import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import tj.behruz.elon.models.AddAdsModel
import tj.behruz.elon.models.BannerPayload
import tj.behruz.elon.models.HistoryModel
import tj.behruz.elon.models.RegistrationModel
import tj.behruz.elon.networking.AppRetrofit
import tj.behruz.elon.networking.Networking

object HomeRepository {
    private var homeApi= AppRetrofit.retrofit!!.create(Networking::class.java)

    suspend fun getInfo(phone:String, hash:String):Response<RegistrationModel> {
        return withContext(Dispatchers.IO){
            homeApi.getInfoUserAsync("login","signin",phone,hash)
        }


    }

    suspend fun getAllHistory(action:String,method:String,token:String):Response<HistoryModel>{
        return withContext(Dispatchers.IO){
            homeApi.getAllHistoryAsync(action,method,token)
        }

    }

    suspend fun updateProfile(home:RequestBody,editProfile:RequestBody,token: RequestBody,image: MultipartBody.Part,name:RequestBody):Response<AddAdsModel>{
        return withContext(Dispatchers.IO){
            homeApi.updateProfile(home,editProfile,token,name,image)
        }
    }


    suspend fun updateProfile2(home:RequestBody,editProfile:RequestBody,token: RequestBody,name:RequestBody):Response<AddAdsModel>{
        return withContext(Dispatchers.IO){
            homeApi.updateProfile2(home,editProfile,token,name)
        }
    }

    suspend fun getBanners(token: String):Response<BannerPayload>{
        return withContext(Dispatchers.IO){
            homeApi.getBanners("home","getBanners",token)

        }

    }
}