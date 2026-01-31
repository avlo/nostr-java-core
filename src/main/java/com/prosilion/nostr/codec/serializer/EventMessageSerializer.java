package com.prosilion.nostr.codec.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.prosilion.nostr.message.EventMessage;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EventMessageSerializer extends StdSerializer<EventMessage> {
  public EventMessageSerializer() {
    super(EventMessage.class);
  }

  public void serialize(EventMessage eventMessage, JsonGenerator gen, SerializerProvider serializers) throws IOException {
    gen.writeStartArray();
    gen.writePOJO(eventMessage.getCommand().getName());
    gen.writePOJO(eventMessage.getEvent().asGenericEventRecord());
//    log.debug(EventIF.createPrettyPrintJson(genericEventRecord));
    gen.writeEndArray();
  }
}
