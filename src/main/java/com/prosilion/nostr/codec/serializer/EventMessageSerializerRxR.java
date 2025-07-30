package com.prosilion.nostr.codec.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.prosilion.nostr.message.EventMessageRxR;
import java.io.IOException;

public class EventMessageSerializerRxR extends StdSerializer<EventMessageRxR> {
  public EventMessageSerializerRxR() {
    super(EventMessageRxR.class);
  }

  public void serialize(EventMessageRxR value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
    gen.writeStartArray();
    gen.writePOJO(value.getCommand().getName());
    gen.writePOJO(value.getEvent());
    gen.writeEndArray();
  }
}
