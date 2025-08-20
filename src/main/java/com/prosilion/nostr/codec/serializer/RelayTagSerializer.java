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
    writeString(jsonGenerator, relayTag.getRelay().getUri().toString());
    jsonGenerator.writeEndArray();
  }

  private static void writeString(JsonGenerator jsonGenerator, String json) throws IOException {
    jsonGenerator.writeString(json);
  }
}
