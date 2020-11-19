package com.cecilleo.core.base.cache;

import androidx.annotation.IntDef;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({ Storage.INTERNAL, Storage.PREFER_EXTERNAL })
@Retention(RetentionPolicy.SOURCE)
public @interface Storage {
  int INTERNAL = 0;
  int PREFER_EXTERNAL = 1;
}