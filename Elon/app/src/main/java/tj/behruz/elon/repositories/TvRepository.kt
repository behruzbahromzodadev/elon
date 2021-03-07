package tj.behruz.elon.repositories

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import tj.behruz.elon.models.AddAdsModel
import tj.behruz.elon.models.CategoryModel
import tj.behruz.elon.models.TvModel
import tj.behruz.elon.networking.AppRetrofit
import tj.behruz.elon.networking.Networking


object TvRepository {

    private val tvApi = AppRetrofit.retrofit!!.create(Networking::class.java)

    internal suspend fun getTvInfo(token:String):Response<TvModel>{
        return withContext(Dispatchers.IO) {
            tvApi.getTvInfoAsync("home","getTvForShow",token)
        }
    }
    internal suspend fun getCategory(token:String):Response<CategoryModel>{
        return withContext(Dispatchers.IO) {
            tvApi.getCategoryAsync("home","getCategories",token)
        }
    }
    internal suspend fun  addCategory(token: String,text:String,phones:String,tv:String,day:String,bonus:String,price:String,category:String):Response<AddAdsModel>{
        return withContext(Dispatchers.IO) {
            tvApi.addAds("home","addRunningLine",token,text,phones,tv,day,bonus,price,category)
        }
    }


    internal suspend fun  editAds(token: String,text:String,phones:String,tv:String,day:String,bonus:String,price:String,category:String,id:String):Response<AddAdsModel>{
        return withContext(Dispatchers.IO) {
            tvApi.editAds("home","editRunningLine",token,text,phones,tv,day,bonus,price,category,id)
        }
    }
    internal suspend fun  deleteAds(token: String,id:String):Response<AddAdsModel>{
        return withContext(Dispatchers.IO) {
            tvApi.deleteAds("home","cancelRunningLine",token,id)
        }
    }
}