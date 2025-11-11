package com.prosilion.nostr;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.prosilion.nostr.event.internal.Relay;
import com.prosilion.nostr.tag.RelaysTag;
import java.net.MalformedURLException;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.web.util.InvalidUrlException;

import static com.prosilion.nostr.codec.IDecoder.I_DECODER_MAPPER_AFTERBURNER;
import static com.prosilion.nostr.event.IEvent.MAPPER_AFTERBURNER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RelaysTagTest {

  public static final String RELAYS_KEY = "relays";
  public static final String LOCALHOST_VALUE_5555 = "ws://localhost:5555";
  public static final String LOCALHOST_VALUE_5432 = "ws://localhost:5432";
  public static final String ANOTHERLOCALHOST_VALUE = "ws://anotherlocalhost:5432";

  @Test
  void testSerialize() throws JsonProcessingException {
    final String expected = "[\"relays\",\"ws://localhost:5555\",\"ws://localhost:5432\"]";
    RelaysTag relaysTag = new RelaysTag(List.of(new Relay(LOCALHOST_VALUE_5555), new Relay(LOCALHOST_VALUE_5432)));
    String s = I_DECODER_MAPPER_AFTERBURNER.writeValueAsString(relaysTag);
    assertEquals(expected, s);
  }

  @Test
  void testDeserialize() throws JsonProcessingException, MalformedURLException {
    final String EXPECTED = "[\"relays\",\"ws://localhost:5555\"]";
    JsonNode node = MAPPER_AFTERBURNER.readTree(EXPECTED);
    RelaysTag deserialize = (RelaysTag) RelaysTag.deserialize(node);
    assertEquals(RELAYS_KEY, deserialize.getCode());
    assertEquals(LOCALHOST_VALUE_5555, deserialize.getRelays().getFirst().getUrl());
  }

  @Test
  void testDeserializeException() throws JsonProcessingException {
    final String EXPECTED = "[\"relays\",\"ws://anotherlocalhost:5555\"]";
    JsonNode node = MAPPER_AFTERBURNER.readTree(EXPECTED);
    assertThrows(InvalidUrlException.class, () -> RelaysTag.deserialize(node));
  }
}
