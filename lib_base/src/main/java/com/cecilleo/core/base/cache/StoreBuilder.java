package com.cecilleo.core.base.cache;

import android.content.Context;
import androidx.annotation.NonNull;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;

import static com.cecilleo.core.base.cache.Storage.INTERNAL;
import static com.cecilleo.core.base.cache.Storage.PREFER_EXTERNAL;

public class StoreBuilder {

  File cacheDir;
  Gson gson;
  long maxSize;
  int version;

  private StoreBuilder(long maxSize) {
    this.maxSize = maxSize;
  }

  @NonNull
  private Gson createGson() {
    GsonBuilder builder = new GsonBuilder();
    builder.setVersion(version);
    return builder.create();
  }

  /**
   * Starts the configuration of the Store instance
   *
   * @param maxSize the maximum size in bytes
   */
  public static StoreBuilder configure(long maxSize) {
    return new StoreBuilder(maxSize);
  }

  /**
   * Initializes the Store instance
   */
  public synchronized void initialize() {
    if (cacheDir == null) {
      throw new RuntimeException("No cache directory has been specified.");
    }
    if (gson == null) {
      gson = createGson();
    }
    Store.initialize(this);
  }

  /**
   * Sets the cache directory
   *
   * @param context the application context
   * @param storage the storage to use for the cache
   */
  public StoreBuilder setCacheDirectory(@NonNull Context context, @Storage int storage) {
    switch (storage) {
      case INTERNAL:
        return setCacheDirectory(context.getCacheDir());
      case PREFER_EXTERNAL:
        File dir = context.getExternalCacheDir();
        if (dir == null || !dir.exists() || !dir.canWrite()) {
          dir = context.getCacheDir();
        }
        return setCacheDirectory(dir);
      default:
        throw new IllegalArgumentException("Invalid storage value: " + storage);
    }
  }

  /**
   * Sets the cache directory. It will be created if does not exist
   *
   * @param cacheDir the cache directory
   */
  public StoreBuilder setCacheDirectory(@NonNull File cacheDir) {
    this.cacheDir = cacheDir;
    return this;
  }

  /**
   * Sets the default cache directory
   *
   * @param context the application context
   */
  public StoreBuilder setDefaultCacheDirectory(@NonNull Context context) {
    return setCacheDirectory(context, INTERNAL);
  }

  /**
   * Sets the Gson instance
   *
   * @param gson the Gson instance
   */
  public StoreBuilder setGsonInstance(@NonNull Gson gson) {
    this.gson = gson;
    return this;
  }

  /**
   * Sets the app version
   *
   * @param version the app version
   */
  public StoreBuilder setVersion(int version) {
    this.version = version;
    return this;
  }
}
