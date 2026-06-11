package com.prosilion.nostr.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.prosilion.nostr.crypto.NostrUtil;
import java.util.Arrays;
import java.util.Objects;
import lombok.NonNull;

public class Signature {
  @JsonProperty("rawData") final private byte[] rawData;

  public Signature(@NonNull String signatureString) {
    this(NostrUtil.hex128ToBytes(signatureString));
  }

  public Signature(byte[] rawData) {
    this.rawData = rawData;
  }

  @JsonValue
  @Override
  public final String toString() {
    return NostrUtil.bytesToHex(rawData);
  }

  @Override
  public final boolean equals(@NonNull Object o) {
    if (getClass() != o.getClass()) return false;
    return Arrays.equals(this.rawData, ((Signature) o).rawData);
  }

  @Override
  public final int hashCode() {
    return Objects.hash(Arrays.hashCode(rawData));
  }
}
