package com.cecilleo.baseexample

data class ExamleRes(
  var list: List<ExData>?
)

data class ExData(
  var id: String?,
  var name: String?,
  var type: Int?,
  var url: String?
)