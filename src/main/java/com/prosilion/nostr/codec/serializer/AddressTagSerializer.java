package com.prosilion.nostr.codec.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.prosilion.nostr.tag.AddressTag;
import com.prosilion.nostr.tag.IdentifierTag;
import java.io.IOException;
import java.util.Optional;

public class AddressTagSerializer extends JsonSerializer<AddressTag> {
  @Override
  public void serialize(AddressTag value, JsonGenerator jsonGenerator, SerializerProvider serializers) throws IOException {
    jsonGenerator.writeStartArray();
    jsonGenerator.writeString("a");
    jsonGenerator.writeString(
        value.getKind() + ":" +
            value.getPublicKey().toString() + ":" +
            Optional.ofNullable(value.getIdentifierTag()).map(IdentifierTag::getUuid).orElse("")
    );

    if (value.getRelay() != null) {
      jsonGenerator.writeString(value.getRelay().getUrl());
    }
    jsonGenerator.writeEndArray();
  }

}
