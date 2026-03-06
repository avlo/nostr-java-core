package com.prosilion.nostr;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.prosilion.nostr.event.internal.Relay;
import com.prosilion.nostr.tag.RelayTag;
import java.net.MalformedURLException;
import org.junit.jupiter.api.Test;

import static com.prosilion.nostr.codec.IDecoder.I_DECODER_MAPPER_AFTERBURNER;
import static com.prosilion.nostr.event.IEvent.MAPPER_AFTERBURNER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class RelayTagTest {

  public static final String RELAY_KEY = "relay";
  public static final String LOCALHOST_VALUE = "ws://localhost:5555";
  public static final String ZERO_LOCALHOST_VALUE = "ws://0.0.0.0:5555";

  @Test
  void testSerializeLocalhost() throws JsonProcessingException {
    final String expected = "[\"relay\",\"ws://localhost:5555\"]";
    RelayTag relayTag = new RelayTag(new Relay(LOCALHOST_VALUE));
    String s = I_DECODER_MAPPER_AFTERBURNER.writeValueAsString(relayTag);
    assertEquals(expected, s);
  }

  @Test
  void testSerializeZeros() throws JsonProcessingException {
    final String expected = "[\"relay\",\"ws://0.0.0.0:5555\"]";
    RelayTag relayTag = new RelayTag(new Relay(ZERO_LOCALHOST_VALUE));
    String s = I_DECODER_MAPPER_AFTERBURNER.writeValueAsString(relayTag);
    assertEquals(expected, s);
  }

  @Test
  void testDeserializeLocalhost() throws JsonProcessingException, MalformedURLException {
    final String EXPECTED = "[\"relay\",\"ws://localhost:5555\"]";
    JsonNode node = MAPPER_AFTERBURNER.readTree(EXPECTED);
    RelayTag deserialize = (RelayTag) RelayTag.deserialize(node);
    assertEquals(RELAY_KEY, deserialize.getCode());
    assertEquals(LOCALHOST_VALUE, deserialize.getRelay().getUrl());
  }

  @Test
  void testDeserializeZeros() throws JsonProcessingException, MalformedURLException {
    final String EXPECTED = "[\"relay\",\"ws://0.0.0.0:5555\"]";
    JsonNode node = MAPPER_AFTERBURNER.readTree(EXPECTED);
    RelayTag deserialize = (RelayTag) RelayTag.deserialize(node);
    assertEquals(RELAY_KEY, deserialize.getCode());
    assertEquals(ZERO_LOCALHOST_VALUE, deserialize.getRelay().getUrl());
  }

  @Test
  void relayEqualityNonEqualityTests() {
    Relay one = new Relay("ws://localhost:5555");
    Relay two = new Relay("ws://localhost:5555");
    assertEquals(one, two);

    Relay three = new Relay("ws://localhost:5556");
    assertNotEquals(one, three);
  }
}
