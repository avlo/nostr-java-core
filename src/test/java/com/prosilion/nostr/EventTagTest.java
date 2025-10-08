package com.prosilion.nostr;

import com.prosilion.nostr.enums.Marker;
import com.prosilion.nostr.tag.EventTag;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;
import org.junit.jupiter.api.Test;
import org.springframework.web.util.InvalidUrlException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EventTagTest {

  public static final String BAD_WS_LOCALHOST_5555 = "ws:/localhost:5555";
  public static final String NONSENSE = "adsfasdf";
  public static final String EVENT_ID = generateRandomHex64String();

  @Test
  void testLocalHostVariants() {
    new EventTag(EVENT_ID, "ws://localhost:5555");
    new EventTag(EVENT_ID, "ws://localhost");
    new EventTag(EVENT_ID, "wss://localhost:5555");
    new EventTag(EVENT_ID, "wss://localhost");
    new EventTag(EVENT_ID, "http://localhost:5555");
    new EventTag(EVENT_ID, "http://localhost");
    new EventTag(EVENT_ID, "https://localhost:5555");
    new EventTag(EVENT_ID, "https://localhost");
    new EventTag(EVENT_ID, "ws://localhost.com");
    new EventTag(EVENT_ID, "ws://localhost.com:1234");
    new EventTag(EVENT_ID, "ws://127.0.0.1");

  }

  @Test
  void testValidUrls() {
    new EventTag(EVENT_ID, "http://example.com");
    new EventTag(EVENT_ID, "https://example.com");
    new EventTag(EVENT_ID, "ws://example.com");
    new EventTag(EVENT_ID, "ws://localhost:5555");
    new EventTag(EVENT_ID, "wss://example.com");
    new EventTag(EVENT_ID, "wss://example.co");
    new EventTag(EVENT_ID, "http://example.com:5555");
  }

  @Test
  void testInvalidUrlException() {
    assertThrows(IllegalArgumentException.class, () -> URI.create(NONSENSE).toURL());
    assertThrows(IllegalArgumentException.class, () -> new URI(NONSENSE).toURL());
    assertThrows(MalformedURLException.class, () -> URI.create(BAD_WS_LOCALHOST_5555).toURL());
    assertThrows(MalformedURLException.class, () -> new URI(BAD_WS_LOCALHOST_5555).toURL());
    assertThrows(InvalidUrlException.class, () -> new EventTag(EVENT_ID, BAD_WS_LOCALHOST_5555, Marker.REPLY));
    assertThrows(InvalidUrlException.class, () -> new EventTag(EVENT_ID, NONSENSE, Marker.REPLY));
    assertThrows(InvalidUrlException.class, () -> new EventTag(EVENT_ID, "wsss://example.com"));
    assertThrows(InvalidUrlException.class, () -> new EventTag(EVENT_ID, "ws s://example.com"));
    assertThrows(InvalidUrlException.class, () -> new EventTag(EVENT_ID, "ftp://example.com"));
    assertThrows(InvalidUrlException.class, () -> new EventTag(EVENT_ID, "http://example.com:555a5"));
  }

  @Test
  void testBlankNullUrlsAsValidAttribute() {
    assertEquals("", new EventTag(EVENT_ID, "", Marker.REPLY).getRecommendedRelayUrl());
    assertEquals(null, new EventTag(EVENT_ID, null, Marker.REPLY).getRecommendedRelayUrl());
  }

  @Test
  void getSupportedFields() {
    String recommendedRelayUrl = "ws://localhost:5555";

    EventTag eventTag = new EventTag(EVENT_ID, recommendedRelayUrl, Marker.REPLY);

    List<Field> fields = eventTag.getSupportedFields();
    anyFieldNameMatch(fields, field -> field.getName().equals("idEvent"));
    anyFieldNameMatch(fields, field -> field.getName().equals("recommendedRelayUrl"));
    anyFieldNameMatch(fields, field -> field.getName().equals("marker"));

    anyFieldValueMatch(fields, eventTag, fieldValue -> fieldValue.equals(EVENT_ID));
    anyFieldValueMatch(fields, eventTag, fieldValue -> fieldValue.equalsIgnoreCase(Marker.REPLY.getValue()));
    anyFieldValueMatch(fields, eventTag, fieldValue -> fieldValue.equals(recommendedRelayUrl));

    assertFalse(fields.stream().anyMatch(field -> field.getName().equals("idEventXXX")));
  }

  private static void anyFieldNameMatch(List<Field> fields, Predicate<Field> predicate) {
    assertTrue(fields.stream().anyMatch(predicate));
  }

  private static void anyFieldValueMatch(List<Field> fields, EventTag eventTag, Predicate<String> predicate) {
    assertTrue(fields.stream().flatMap(field -> {
      try {
        return eventTag.getFieldValue(field).stream();
      } catch (NoSuchFieldException e) {
        throw new RuntimeException(e);
      } catch (IllegalAccessException e) {
        throw new RuntimeException(e);
      }
    }).anyMatch(predicate));
  }

  public static String generateRandomHex64String() {
    return UUID.randomUUID().toString().concat(UUID.randomUUID().toString()).replaceAll("[^A-Za-z0-9]", "");
  }
}
