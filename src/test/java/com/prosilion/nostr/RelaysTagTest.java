package com.prosilion.nostr;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.prosilion.nostr.event.internal.Relay;
import com.prosilion.nostr.tag.BaseTag;
import com.prosilion.nostr.tag.RelaysTag;
import java.util.List;
import org.junit.jupiter.api.Test;

import static com.prosilion.nostr.codec.IDecoder.I_DECODER_MAPPER_AFTERBURNER;
import static com.prosilion.nostr.event.IEvent.MAPPER_AFTERBURNER;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class RelaysTagTest {

  public final static String RELAYS_KEY = "relays";
  public final static String HOST_VALUE = "ws://localhost:5555";
  public final static String HOST_VALUE2 = "ws://anotherlocalhost:5432";

  @Test
  void testSerialize() throws JsonProcessingException {
    final String expected = "[\"relays\",\"ws://localhost:5555\",\"ws://anotherlocalhost:5432\"]";
    RelaysTag relaysTag = new RelaysTag(List.of(new Relay(HOST_VALUE), new Relay(HOST_VALUE2)));
    String s = I_DECODER_MAPPER_AFTERBURNER.writeValueAsString(relaysTag);
    assertEquals(expected, s);
  }

  @Test
  void testDeserialize() {
    final String EXPECTED = "[\"relays\",\"ws://localhost:5555\"]";
    assertDoesNotThrow(() -> {
      JsonNode node = MAPPER_AFTERBURNER.readTree(EXPECTED);
      BaseTag deserialize = RelaysTag.deserialize(node);
      assertEquals(RELAYS_KEY, deserialize.getCode());
      assertEquals(HOST_VALUE, ((RelaysTag) deserialize).getRelays().getFirst().getUri());
    });
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
