package com.prosilion.nostr.user;

import com.prosilion.nostr.crypto.NostrUtil;
import com.prosilion.nostr.crypto.bech32.Bech32;
import com.prosilion.nostr.crypto.bech32.Bech32Prefix;
import java.util.Arrays;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
final class BaseKey implements IKey {

  @EqualsAndHashCode.Exclude
  private final KeyType type;
  private final byte[] rawData;
  private final Bech32Prefix prefix;

  @Override
  public String toBech32String() {
    try {
      return Bech32.toBech32(prefix, rawData);
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  @Override
  public String toString() {
    return toHexString();
  }

  public String toHexString() {
    return NostrUtil.bytesToHex(rawData);
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 31 * hash + this.type.hashCode();
    hash = 31 * hash + (this.prefix == null ? 0 : this.prefix.hashCode());
    hash = 31 * hash + (this.rawData == null ? 0 : Arrays.hashCode(this.rawData));
    return hash;
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

    BaseKey baseKey = (BaseKey) o;

    // field comparison
    return Arrays.equals(rawData, baseKey.rawData);
  }
}
