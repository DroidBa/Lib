package com.cecilleo.baseexample

import com.cecilleo.lib.net.BaseRepository
import com.cecilleo.lib.net.BaseResult

class ExampleRepository(private val service: ExampleService) : BaseRepository() {
  suspend fun getTab(): BaseResult<ExamleRes> {
    return executeResponse(service.getBanner())
  }
}