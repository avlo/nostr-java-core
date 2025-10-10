package com.prosilion.nostr.codec.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.prosilion.nostr.tag.RelayTag;
import java.io.IOException;
import org.springframework.lang.NonNull;

public class RelayTagSerializer extends JsonSerializer<RelayTag> {

  @Override
  public void serialize(@NonNull RelayTag relayTag, @NonNull JsonGenerator jsonGenerator, @NonNull SerializerProvider serializerProvider) throws IOException {
    jsonGenerator.writeStartArray();
    jsonGenerator.writeString("relay");
    jsonGenerator.writeString(relayTag.getRelay().getUrl());
    jsonGenerator.writeEndArray();
  }
}
