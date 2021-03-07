package tj.behruz.elon.ui.registration

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import tj.behruz.elon.base.LoginResponse
import tj.behruz.elon.models.RegistrationModel
import tj.behruz.elon.repositories.LoginRepository
import java.net.ConnectException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException

class RegistrationViewModel : ViewModel() {

    var loginModel = MutableLiveData<LoginResponse>()
    var registration = MutableLiveData<RegistrationModel>()
    var errorMessage: MutableLiveData<String> = MutableLiveData()

    private val handler = CoroutineExceptionHandler { _, exception ->
        when (exception) {
            is UnknownHostException -> errorMessage.postValue("Неизвестный хост, попробуйте позже.")
            is ConnectException -> errorMessage.postValue("Не удаётся подключиться к серверу, попробуйте позже.")
            is TimeoutException -> errorMessage.postValue("Время ожидания истекло, попробуйте позже.")
            else -> errorMessage.postValue("Неизвестный хост, попробуйте позже.")
        }

    }

    fun login(phone: String, hash: String): LiveData<LoginResponse> {
        viewModelScope.launch(handler) {

            loginModel.postValue(LoginRepository.login(phone, hash).body())
        }
        return loginModel
    }

    fun registration(
        numberPhone: String,
        fullName: String,
        lastName: String,
        hash: String
    ): LiveData<RegistrationModel> {
        viewModelScope.launch(handler) {
            registration.postValue(
                LoginRepository.registration(
                    numberPhone,
                    fullName,
                    lastName,
                    hash
                ).body()
            )
        }
        return registration


    }


}