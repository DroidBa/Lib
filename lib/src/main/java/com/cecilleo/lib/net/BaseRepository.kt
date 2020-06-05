package com.cecilleo.lib.net

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.suspendCancellableCoroutine
import java.io.IOException

abstract class BaseRepository {
  suspend fun <T : Any> executeResponse(response: BaseResponse<T>
  ): BaseResult<T> {
    return if (response.isSuccess()) {
      BaseResult.Success(response.getResult())
    } else {
      BaseResult.Error(Exception(response.getError()))
    }
  }
}