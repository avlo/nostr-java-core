package com.prosilion.nostr.codec.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.prosilion.nostr.tag.PubKeyTag;
import java.io.IOException;

public class PubKeyTagSerializer extends JsonSerializer<PubKeyTag> {
  @Override
  public final void serialize(PubKeyTag value, JsonGenerator jsonGenerator, SerializerProvider serializers) throws IOException {
    jsonGenerator.writeStartArray();
    jsonGenerator.writeString("p");
    jsonGenerator.writeString(value.getPublicKey().toString());
    if (value.getMainRelayUrl() != null) {
      jsonGenerator.writeString(value.getMainRelayUrl());
    }
    if (value.getPetName() != null) {
      jsonGenerator.writeString(value.getPetName());
    }
    jsonGenerator.writeEndArray();
  }
}
