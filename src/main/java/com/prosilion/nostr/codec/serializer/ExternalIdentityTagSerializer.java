package com.prosilion.nostr.codec.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.prosilion.nostr.tag.ExternalIdentityTag;
import java.io.IOException;

public class ExternalIdentityTagSerializer extends JsonSerializer<ExternalIdentityTag> {
  @Override
  public void serialize(ExternalIdentityTag value, JsonGenerator jsonGenerator, SerializerProvider serializers) throws IOException {
    jsonGenerator.writeStartArray();
    jsonGenerator.writeString("i");
    jsonGenerator.writeString(
        value.platform() + ":" +
            value.getIdentity()
    );
    jsonGenerator.writeString(value.getProof());
    jsonGenerator.writeEndArray();
  }
}
