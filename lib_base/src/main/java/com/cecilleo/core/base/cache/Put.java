package com.cecilleo.core.base.cache;

import androidx.annotation.NonNull;
import com.cecilleo.core.base.cache.model.BaseMethod;
import com.cecilleo.core.base.cache.model.EntryData;
import java.util.concurrent.TimeUnit;

public class Put<T> extends BaseMethod<Boolean> {

  private long expiry = Store.NO_EXPIRY;
  private String key;
  private T object;

  Put(@NonNull String key, @NonNull T object) {
    this.key = key;
    this.object = object;
  }

  @Override
  public Boolean execute() {
    EntryData<T> entry = new EntryData<>();
    entry.data = object;
    entry.expiry = expiry;
    try {
      return Store.internalPut(key, entry);
    } catch (Exception e) {
      return false;
    }
  }

  /**
   * Sets the object expiry timestamp
   *
   * @param expiry the expiry timestamp
   */
  public Put<T> setExpiry(long expiry) {
    this.expiry = expiry;
    return this;
  }

  /**
   * Sets the object expiry timestamp
   *
   * @param duration the object duration
   * @param unit the time unit of the duration
   */
  public Put<T> setExpiry(long duration, @NonNull TimeUnit unit) {
    return setExpiry(System.currentTimeMillis() + unit.toMillis(duration));
  }
}
