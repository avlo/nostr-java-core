package com.prosilion.nostr.codec.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.prosilion.nostr.event.internal.Relay;
import com.prosilion.nostr.tag.RelaysTag;
import java.io.IOException;
import org.springframework.lang.NonNull;

public class RelaysTagSerializer extends JsonSerializer<RelaysTag> {

  @Override
  public void serialize(@NonNull RelaysTag relaysTag, @NonNull JsonGenerator jsonGenerator, @NonNull SerializerProvider serializerProvider) throws IOException {
    jsonGenerator.writeStartArray();
    jsonGenerator.writeString("relays");
    for (Relay json : relaysTag.getRelays()) {
      writeString(jsonGenerator, json.getUrl());
    }
    jsonGenerator.writeEndArray();
  }

  private static void writeString(JsonGenerator jsonGenerator, String json) throws IOException {
    jsonGenerator.writeString(json);
  }
}
