package com.cecilleo.core.base.cache.model;

public class Entry {

  public long expiry;

  /**
   * Check if the entry has expired
   *
   * @return true if the entry has expired
   */
  public boolean hasExpired() {
    return (expiry > 0) && (System.currentTimeMillis() >= expiry);
  }
}
