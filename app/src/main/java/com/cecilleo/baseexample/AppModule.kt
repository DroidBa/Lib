package com.cecilleo.baseexample

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
  viewModel { MVVMExampleVM(get()) }
}

val repositoryModule = module {
  single { ExampleClient.getService(ExampleService::class.java, ExampleService.BASE_URL) }
  single { ExampleRepository(get()) }
}

val appModule = listOf(viewModelModule, repositoryModule)