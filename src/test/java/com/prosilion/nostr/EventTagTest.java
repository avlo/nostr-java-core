package com.prosilion.nostr;

import com.prosilion.nostr.enums.Marker;
import com.prosilion.nostr.tag.EventTag;
import java.lang.reflect.Field;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EventTagTest {

  @Test
  void getSupportedFields() {
    String eventId = generateRandomHex64String();
    String recommendedRelayUrl = "ws://localhost:5555";

    EventTag eventTag = new EventTag(eventId, recommendedRelayUrl, Marker.REPLY);

    List<Field> fields = eventTag.getSupportedFields();
    anyFieldNameMatch(fields, field -> field.getName().equals("idEvent"));
    anyFieldNameMatch(fields, field -> field.getName().equals("recommendedRelayUrl"));
    anyFieldNameMatch(fields, field -> field.getName().equals("marker"));

    anyFieldValueMatch(fields, eventTag, fieldValue -> fieldValue.equals(eventId));
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
