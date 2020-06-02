package com.cecilleo.lib.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cecilleo.lib.net.BaseResult
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

open class BaseViewModel : ViewModel() {
  open class BaseUiModel<T : Any>(
    var showLoading: Boolean = false,
    var showError: String? = null,
    var showSuccess: T? = null,
  )

  fun <T : Any> launchNetTask(task: suspend () -> BaseResult<T>, liveData: MutableLiveData<BaseUiModel<T>>) {
    viewModelScope.launch {
      try {
        liveData.value = BaseUiModel(true, null, null)
        val result = task()
        if (result is BaseResult.Success) {
          liveData.value = BaseUiModel(false, null, result.data)
        } else if (result is BaseResult.Error) {
          result.exception.cause?.message
          liveData.value = BaseUiModel(false, result.exception.message, null)
        }
      } catch (e: Exception) {
        liveData.value = BaseUiModel(false, e.message, null)
      }
    }
  }
}
