package com.prosilion.nostr;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.prosilion.nostr.event.internal.Relay;
import com.prosilion.nostr.tag.RelaysTag;
import java.net.MalformedURLException;
import java.util.Set;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.web.util.InvalidUrlException;

import static com.prosilion.nostr.codec.IDecoder.I_DECODER_MAPPER_AFTERBURNER;
import static com.prosilion.nostr.event.IEvent.MAPPER_AFTERBURNER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
class RelaysTagTest {
  public static final String SERIALIZE_EXPECTED = "[\"relays\",\"ws://localhost:5555\",\"ws://localhost:5432\"]";
  public static final String SERIALIZE_EXPECTED_INV_ORDER = "[\"relays\",\"ws://localhost:5432\",\"ws://localhost:5555\"]";
  public static final String DESERIALIZE_EXPECTED = "[\"relays\",\"ws://localhost:5555\"]";

  public static final String RELAYS_KEY = "relays";
  public static final String LOCALHOST_VALUE_5555 = "ws://localhost:5555";
  public static final String LOCALHOST_VALUE_5432 = "ws://localhost:5432";
  public static final String ANOTHERLOCALHOST_VALUE = "ws://anotherlocalhost:5432";

  @Test
  final void testSerialize() throws JsonProcessingException {
    RelaysTag relaysTag = new RelaysTag(Set.of(new Relay(LOCALHOST_VALUE_5555), new Relay(LOCALHOST_VALUE_5432)));
    final String actual = I_DECODER_MAPPER_AFTERBURNER.writeValueAsString(relaysTag);
    assertFalse(Stream.of(SERIALIZE_EXPECTED, SERIALIZE_EXPECTED_INV_ORDER).filter(actual::equals).toList().isEmpty());
  }

  @Test
  final void testDeserialize() throws JsonProcessingException, MalformedURLException {
    JsonNode node = MAPPER_AFTERBURNER.readTree(DESERIALIZE_EXPECTED);
    RelaysTag deserialize = (RelaysTag) RelaysTag.deserialize(node);
    assertEquals(RELAYS_KEY, deserialize.getCode());
    assertEquals(LOCALHOST_VALUE_5555, deserialize.getRelays().stream().findFirst().map(Relay::getUrl).orElseThrow());
  }

  @Test
  final void testDeserializeException() throws JsonProcessingException {
    final String EXPECTED = "[\"relays\",\"ws://anotherlocalhost:5555\"]";
    JsonNode node = MAPPER_AFTERBURNER.readTree(EXPECTED);
    assertThrows(InvalidUrlException.class, () -> RelaysTag.deserialize(node));
  }
}
