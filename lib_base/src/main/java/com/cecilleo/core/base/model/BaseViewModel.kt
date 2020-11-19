package com.cecilleo.core.base.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cecilleo.core.base.model.ErrorType.CANCEL
import com.cecilleo.core.base.model.ErrorType.NONET
import com.cecilleo.core.base.model.ErrorType.NORMAL
import com.cecilleo.core.base.model.ErrorType.OTHERS
import com.cecilleo.core.base.net.BaseException
import com.cecilleo.core.base.net.BaseResult
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import java.lang.Exception
import java.net.ConnectException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException

open class BaseViewModel : ViewModel() {

  open class BaseUiModel<P : Any>(
    var showLoading: Boolean = false,
    var errorType: ErrorType? = null,
    var showSuccess: P? = null,
    var error: Exception? = null
  )

  fun <T : Any?, P : Any> launchNetTask(task: suspend () -> BaseResult<T>,
    processData: suspend (T) -> P, liveData: MutableLiveData<BaseUiModel<P>>?,
    errorCall: suspend (errorType: ErrorType, e: Exception) -> Unit = { _, _ -> }) {
    viewModelScope.launch {
      try {
        liveData?.value = BaseUiModel(true)
        val result = task()
        if (result is BaseResult.Success) {
          val processResult = processData(result.data)
          liveData?.value = BaseUiModel(showSuccess = processResult)
        } else if (result is BaseResult.Error) {
          throw result.exception
        }
      } catch (e: Exception) {
        var errorType = ErrorType.NORMAL
        errorType = when (e) {
          is ConnectException, is UnknownHostException, is TimeoutException -> {
            NONET
          }
          is BaseException -> {
            commonDoBusinessError(e)
            NORMAL
          }
          is CancellationException -> {
            CANCEL
          }
          else -> {
            OTHERS
          }
        }
        liveData?.let {
          it.value = BaseUiModel(errorType = errorType, error = e)
        } ?: kotlin.run { errorCall(errorType, e) }
      }
    }
  }

  open fun commonDoBusinessError(e: BaseException) {}
}

enum class ErrorType {
  NONET,
  NORMAL,
  CANCEL,
  OTHERS
}
