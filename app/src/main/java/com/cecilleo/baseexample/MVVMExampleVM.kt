package com.cecilleo.baseexample

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cecilleo.baseexample.ExamleRes
import com.cecilleo.baseexample.ExampleRepository
import com.cecilleo.lib.model.BaseViewModel

class MVVMExampleVM(private val repository: ExampleRepository) : BaseViewModel() {
  val text = MutableLiveData<BaseUiModel<ExamleRes>>()

  fun getTab() {
    launchNetTask({
      repository.getTab()
    }, text)
  }
}