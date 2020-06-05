package com.cecilleo.lib.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cecilleo.lib.model.ErrorType.CANCEL
import com.cecilleo.lib.model.ErrorType.NONET
import com.cecilleo.lib.model.ErrorType.NORMAL
import com.cecilleo.lib.model.ErrorType.OTHERS
import com.cecilleo.lib.net.BaseException
import com.cecilleo.lib.net.BaseResult
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch
import java.lang.Exception
import java.net.ConnectException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException

open class BaseViewModel : ViewModel() {
  open class BaseUiModel<T : Any>(
    var showLoading: Boolean = false,
    var errorType: ErrorType? = null,
    var showSuccess: T? = null,
    var error: Exception? = null
  )

  fun <T : Any> launchNetTask(task: suspend () -> BaseResult<T>, liveData: MutableLiveData<BaseUiModel<T>>) {
    viewModelScope.launch {
      try {
        liveData.value = BaseUiModel(true)
        val result = task()
        if (result is BaseResult.Success) {
          liveData.value = BaseUiModel(showSuccess = result.data)
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
            NORMAL
          }
          is CancellationException -> {
            CANCEL
          }
          else -> {
            OTHERS
          }
        }
        liveData.value = BaseUiModel(errorType = errorType, error = e)
      }
    }
  }
}

enum class ErrorType {
  NONET,
  NORMAL,
  CANCEL,
  OTHERS
}
