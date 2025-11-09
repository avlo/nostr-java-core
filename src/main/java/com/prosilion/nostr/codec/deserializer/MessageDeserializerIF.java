package com.prosilion.nostr.codec.deserializer;

import com.fasterxml.jackson.databind.JsonNode;
import com.prosilion.nostr.enums.Kind;
import com.prosilion.nostr.event.GenericEventRecord;
import com.prosilion.nostr.tag.BaseTag;
import com.prosilion.nostr.user.PublicKey;
import com.prosilion.nostr.user.Signature;
import java.util.Arrays;

import static com.prosilion.nostr.codec.IDecoder.I_DECODER_MAPPER_AFTERBURNER;

public interface MessageDeserializerIF {
  default GenericEventRecord getEvent(JsonNode node) {
    return new GenericEventRecord(
        node.path("id").asText(),
        new PublicKey(
            node.path("pubkey").asText()),
        Long.valueOf(
            node.path("created_at").asText()),
        Kind.valueOf(
            node.path("kind").asInt()),
        Arrays.asList(
            I_DECODER_MAPPER_AFTERBURNER.convertValue(
                node.path("tags"),
                BaseTag[].class)),
        node.path("content").asText(),
        new Signature(
            node.path("sig").asText()));
  }
}
