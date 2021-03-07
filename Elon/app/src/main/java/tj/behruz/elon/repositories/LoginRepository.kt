package tj.behruz.elon.repositories


import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import tj.behruz.elon.base.BaseResponse
import tj.behruz.elon.base.LoginResponse
import tj.behruz.elon.models.Login
import tj.behruz.elon.models.RegistrationModel
import tj.behruz.elon.networking.AppRetrofit
import tj.behruz.elon.networking.Networking

object LoginRepository {
    private val loginApi = AppRetrofit.retrofit!!.create(Networking::class.java)

    internal suspend fun login(numberPhone: String, hash: String): Response<LoginResponse> {

        return withContext(Dispatchers.IO) {
            loginApi.loginAsync("login","signup_by_number",numberPhone,hash)
        }
    }

    internal suspend fun registration(numberPhone: String, fullName: String, lastName: String, hash: String): Response<RegistrationModel> {

        return withContext(Dispatchers.IO) {
            loginApi.registrationAsync("login","signup",numberPhone, fullName, lastName, hash)
        }
    }


}