package com.prosilion.nostr.codec.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.prosilion.nostr.tag.EventTag;
import java.io.IOException;

public class EventTagSerializer extends JsonSerializer<EventTag> {
  @Override
  public final void serialize(EventTag value, JsonGenerator jsonGenerator, SerializerProvider serializers) throws IOException {
    jsonGenerator.writeStartArray();
    jsonGenerator.writeString("e");
    jsonGenerator.writeString(value.getIdEvent());

    if (value.findRelay().isPresent()) {
      jsonGenerator.writeString(value.requireRelay().getUrl());
    }
    jsonGenerator.writeEndArray();
  }
}
