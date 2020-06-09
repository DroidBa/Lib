package com.cecilleo.lib.cache.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import rx.Emitter;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public abstract class BaseMethod<T> {

  /**
   * Creates an observable that will be scheduled on the background thread
   *
   * @return Observable<T>
   */
  @NonNull
  public Observable<T> async() {
    return Observable.create(new Action1<Emitter<T>>() {
      @Override public void call(Emitter<T> tEmitter) {
        try {
          T result = execute();
          if (result == null) {
            tEmitter.onError(new NullPointerException("No Cache Data"));
          } else {
            tEmitter.onNext(result);
            tEmitter.onCompleted();
          }
        } catch (Throwable t) {
          tEmitter.onError(t);
        }
      }
    }, Emitter.BackpressureMode.LATEST).subscribeOn(Schedulers.io());
  }

  /**
   * Executes the method on a background thread
   *
   * @param callback the callback to call upon completion
   */
  public void async(@Nullable final Callback<T> callback) {
    ExecutorService executor = Executors.newSingleThreadExecutor();
    executor.execute(new Runnable() {
      @Override
      public void run() {
        T result = execute();
        if (callback != null) {
          callback.onResult(result);
        }
      }
    });
    executor.shutdown();
  }

  /**
   * Executes the method on the main thread
   *
   * @return the method result
   */
  public abstract T execute();
}
