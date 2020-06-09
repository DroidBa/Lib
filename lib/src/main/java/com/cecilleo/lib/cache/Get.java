package com.cecilleo.lib.cache;

import androidx.annotation.NonNull;
import com.cecilleo.lib.cache.model.BaseMethod;
import com.cecilleo.lib.cache.model.EntryData;
import com.cecilleo.lib.cache.model.EntryDataType;
import java.lang.reflect.Type;

public class Get<T> extends BaseMethod<T> {

  private boolean ignoreExpiry;
  private String key;
  private Type typeOfT;

  Get(@NonNull String key, @NonNull Type typeOfT) {
    this.key = key;
    this.typeOfT = typeOfT;
  }

  @Override
  public T execute() {
    try {
      if (!ignoreExpiry) {
        Boolean expired = Store.hasExpired(key).execute();
        if (expired != null && expired) {
          return null;
        }
      }
      EntryData<T> entry = Store.internalGet(key, new EntryDataType<>(typeOfT));
      if (entry == null) {
        return null;
      }
      return entry.data;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Set to true to ignore the expiration time
   *
   * @param ignore true to ignore the expiration time
   */
  public Get<T> setIgnoreExpiry(boolean ignore) {
    ignoreExpiry = ignore;
    return this;
  }
}
