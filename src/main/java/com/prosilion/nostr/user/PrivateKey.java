package com.prosilion.nostr.user;

import com.prosilion.nostr.crypto.NostrUtil;
import com.prosilion.nostr.crypto.bech32.Bech32Prefix;
import com.prosilion.nostr.crypto.schnorr.Schnorr;
import org.springframework.lang.NonNull;

public final class PrivateKey {

  private final BaseKey baseKey;

  public PrivateKey(@NonNull byte[] rawData) {
    baseKey = new BaseKey(KeyType.PRIVATE, rawData, Bech32Prefix.NSEC);
  }

  public PrivateKey(@NonNull String hexPrivKey) {
    baseKey = new BaseKey(KeyType.PRIVATE, NostrUtil.hexToBytes(hexPrivKey), Bech32Prefix.NSEC);
  }

  public byte[] getRawData() {
    return baseKey.getRawData();
  }

  @Override
  public int hashCode() {
    return baseKey.hashCode();
  }

  @Override
  public boolean equals(@NonNull Object o) {
    if (getClass() != o.getClass()) return false;
    return baseKey.equals(((PrivateKey) o).baseKey);
  }

  public static PrivateKey generateRandomPrivKey() {
    return new PrivateKey(Schnorr.generatePrivateKey());
  }
}
