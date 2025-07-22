package com.prosilion.nostr.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.prosilion.nostr.crypto.NostrUtil;
import com.prosilion.nostr.crypto.bech32.Bech32Prefix;

public final class PublicKey {

  private final BaseKey baseKey;

  public PublicKey(byte[] rawData) {
    baseKey = new BaseKey(KeyType.PUBLIC, rawData, Bech32Prefix.NPUB);
  }

  @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
  public PublicKey(String hexPubKey) {
    baseKey = new BaseKey(KeyType.PUBLIC, NostrUtil.hexToBytes(hexPubKey), Bech32Prefix.NPUB);
  }

  @Override
  @JsonValue
  public String toString() {
    return toHexString();
  }

  public String toHexString() {
    return baseKey.toHexString();
  }

  public String toBech32String() {
    return baseKey.toBech32String();
  }
  
  public byte[] getRawData() {
    return baseKey.getRawData();
  }

  @Override
  public int hashCode() {
    return baseKey.hashCode();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;

    // null check
    if (o == null)
      return false;

    // type check and cast
    if (getClass() != o.getClass())
      return false;

    return baseKey.equals(((PublicKey) o).baseKey);
  }
}
