package com.cecilleo.baseexample

import retrofit2.http.GET

interface ExampleService {
  companion object {
    const val BASE_URL = "http://22e724-0.bj.1252336776.clb.myqcloud.com"
  }

  @GET("/news/index-tab")
  suspend fun getBanner(): NBAResponse<ExamleRes>
}