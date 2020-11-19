package com.cecilleo.core.base.net

abstract class BaseRepository {
  suspend fun <T : Any?> executeResponse(response: BaseResponse<T>
  ): BaseResult<T> {
    return if (response.isSuccess()) {
      BaseResult.Success(response.getResult())
    } else {
      BaseResult.Error(BaseException(response.getError()?:"", response.getErCode()))
    }
  }

  suspend fun executeResponseSelf(response: BaseResponse<*>): BaseResult<*> {
    return if (response.isSuccess()) {
      BaseResult.Success(response.getSelf())
    } else {
      BaseResult.Error(BaseException(response.getError(), response.getErCode()))
    }
  }
}