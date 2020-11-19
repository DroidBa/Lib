package com.cecilleo.core.base.cache;

import androidx.annotation.NonNull;
import com.cecilleo.core.base.cache.model.BaseMethod;
import com.cecilleo.core.base.cache.model.Entry;

public class Expired extends BaseMethod<Boolean> {

  private String key;

  Expired(@NonNull String key) {
    this.key = key;
  }

  @Override
  public Boolean execute() {
    Entry entry = new GetEntry(key).execute();
    if (entry == null) {
      return null;
    }
    return entry.hasExpired();
  }
}
