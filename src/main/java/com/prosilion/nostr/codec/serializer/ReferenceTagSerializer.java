package com.prosilion.nostr.codec.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.prosilion.nostr.tag.ReferenceTag;
import java.io.IOException;

public class ReferenceTagSerializer extends JsonSerializer<ReferenceTag> {

    @Override
    public void serialize(ReferenceTag refTag, JsonGenerator jsonGenerator, SerializerProvider serializers) throws IOException {
        jsonGenerator.writeStartArray();
        jsonGenerator.writeString("r");
        jsonGenerator.writeString(refTag.getUri().toString());
        jsonGenerator.writeEndArray();
    }
    
}
