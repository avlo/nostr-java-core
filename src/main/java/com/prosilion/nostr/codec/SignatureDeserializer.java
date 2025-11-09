package com.prosilion.nostr.codec;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.prosilion.nostr.crypto.NostrUtil;
import com.prosilion.nostr.user.Signature;
import java.io.IOException;

public class SignatureDeserializer extends JsonDeserializer<Signature> {
  @Override
  public Signature deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
    return new Signature(NostrUtil.hex128ToBytes(jsonParser.getCodec().<JsonNode>readTree(jsonParser).asText()));
  }
}
