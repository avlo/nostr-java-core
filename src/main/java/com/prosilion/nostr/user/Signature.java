package com.prosilion.nostr.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.prosilion.nostr.crypto.NostrUtil;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Signature {

    @JsonProperty("rawData")
    private byte[] rawData;

    @JsonIgnore
    private PublicKey pubKey;

    @JsonValue
    @Override
    public String toString() {
        return NostrUtil.bytesToHex(rawData);
    }

    public static Signature fromString(String signatureString) {
      Signature signature = new Signature();
      signature.setRawData(NostrUtil.hex128ToBytes(signatureString));
      return signature;
    }
}
