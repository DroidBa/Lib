package com.cecilleo.lib.cache.model;

public interface Callback<T> {
  void onResult(T result);
}
