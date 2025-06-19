package com.prosilion.nostr.codec;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.module.afterburner.AfterburnerModule;

public interface Encoder<T> {
  ObjectMapper ENCODER_MAPPED_AFTERBURNER = JsonMapper.builder().addModule(
          new AfterburnerModule()).build()
      .setSerializationInclusion(Include.NON_NULL);

  String encode(T value) throws JsonProcessingException;

  static ObjectNode createObjectNode() {
    return ENCODER_MAPPED_AFTERBURNER.createObjectNode();
  }

  default ObjectMapper getBaseTagEncoderMappedAfterburnerCopy(StdSerializer<T> jsonSerializer) {
    return ENCODER_MAPPED_AFTERBURNER.copy()
        .registerModule(
            new SimpleModule().addSerializer(jsonSerializer));
  }

  default String writeValueAsString(T value, StdSerializer<T> jsonSerializer) throws JsonProcessingException {
    return getBaseTagEncoderMappedAfterburnerCopy(jsonSerializer).writeValueAsString(value);
  }

  static JsonNode createJsonNode(String jsonNode) {
    try {
      return ENCODER_MAPPED_AFTERBURNER.readTree(jsonNode);
    } catch (JsonProcessingException e) {
      throw new IllegalArgumentException(String.format("Malformed encoding ReqMessage json: [%s]", jsonNode), e);
    }
  }
}
