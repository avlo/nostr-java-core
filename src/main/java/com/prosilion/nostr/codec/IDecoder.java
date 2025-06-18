package com.prosilion.nostr.codec;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.module.afterburner.AfterburnerModule;
import com.prosilion.nostr.tag.BaseTag;

public interface IDecoder<T extends BaseTag> {
  ObjectMapper I_DECODER_MAPPER_AFTERBURNER = JsonMapper
      .builder().addModule(
          new AfterburnerModule())
      .build().configure(
          JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
}
