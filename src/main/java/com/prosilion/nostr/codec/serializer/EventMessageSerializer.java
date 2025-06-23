package com.prosilion.nostr.codec.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.prosilion.nostr.message.EventMessage;
import java.io.IOException;

public class EventMessageSerializer extends StdSerializer<EventMessage> {
  public EventMessageSerializer() {
    super(EventMessage.class);
  }

  public void serialize(EventMessage value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
    gen.writeStartArray();
    gen.writePOJO(value.getCommand().getName());
    gen.writePOJO(value.getEvent());
    gen.writeEndArray();
  }
}
