package com.cecilleo.core.base.cache;

import androidx.annotation.NonNull;
import com.cecilleo.core.base.cache.model.BaseMethod;
import com.cecilleo.core.base.cache.model.Entry;

public class GetEntry extends BaseMethod<Entry> {

  private String key;

  GetEntry(@NonNull String key) {
    this.key = key;
  }

  @Override
  public Entry execute() {
    return Store.internalGet(key, Entry.class);
  }
}
