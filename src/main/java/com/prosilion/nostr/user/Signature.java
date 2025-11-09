package com.prosilion.nostr.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.prosilion.nostr.crypto.NostrUtil;
import java.util.Arrays;
import java.util.Objects;
import org.springframework.lang.NonNull;

public class Signature {

  @JsonProperty("rawData")
  private byte[] rawData;

  public Signature(@NonNull String signatureString) {
    this(NostrUtil.hex128ToBytes(signatureString));
  }

  public Signature(@NonNull byte[] rawData) {
    this.rawData = rawData;
  }

  @JsonValue
  @Override
  public String toString() {
    return NostrUtil.bytesToHex(rawData);
  }

  @Override
  public boolean equals(@NonNull Object o) {
    if (getClass() != o.getClass()) return false;
    return Arrays.equals(this.rawData, ((Signature) o).rawData);
  }

  @Override
  public int hashCode() {
    return Objects.hash(Arrays.hashCode(rawData));
  }
}
