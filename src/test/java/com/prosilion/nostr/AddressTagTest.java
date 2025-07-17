package com.prosilion.nostr;

import com.prosilion.nostr.enums.Kind;
import com.prosilion.nostr.event.internal.Relay;
import com.prosilion.nostr.filter.tag.AddressTagFilter;
import com.prosilion.nostr.tag.AddressTag;
import com.prosilion.nostr.tag.IdentifierTag;
import com.prosilion.nostr.user.PublicKey;
import java.lang.reflect.Field;
import java.util.List;
import java.util.function.Predicate;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AddressTagTest {
  Kind kind = Kind.TEXT_NOTE;
  String author = "f1b419a95cb0233a11d431423b41a42734e7165fcab16081cd08ef1c90e0be75";
  PublicKey publicKey = new PublicKey(author);
  IdentifierTag identifierTag = new IdentifierTag("UUID-1");
  Relay relay = new Relay("ws://localhost:8080");

  @Test
  void getSupportedFields() {
    AddressTag addressTag = new AddressTag(
        kind, publicKey, identifierTag, relay
    );

    List<Field> fields = addressTag.getSupportedFields();
    anyFieldNameMatch(fields, field -> field.getName().equals("kind"));
    anyFieldNameMatch(fields, field -> field.getName().equals("publicKey"));
    anyFieldNameMatch(fields, field -> field.getName().equals("identifierTag"));
    anyFieldNameMatch(fields, field -> field.getName().equals("relay"));

    anyFieldValueMatch(fields, addressTag, fieldValue -> fieldValue.equals(kind.toString()));
    anyFieldValueMatch(fields, addressTag, fieldValue -> fieldValue.equals(publicKey.toString()));
    anyFieldValueMatch(fields, addressTag, fieldValue -> fieldValue.equals(identifierTag.toString()));
    anyFieldValueMatch(fields, addressTag, fieldValue -> fieldValue.equals(relay.toString()));

    assertFalse(fields.stream().anyMatch(field -> field.getName().equals("idEventXXX")));
//        TODO: below needs failable stream
    assertFalse(
        fields.stream().flatMap(field ->
            {
              try {
                return addressTag.getFieldValue(field).stream();
              } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException(e);
              }
            })
            .anyMatch(fieldValue ->
                fieldValue.equals(identifierTag.toString() + "x")));
  }

  @Test
  void equalityNonEqualityTests() {
    AddressTag one = new AddressTag(kind, publicKey);
    AddressTag two = new AddressTag(kind, publicKey);
    assertEquals(one, two);

    AddressTag three = new AddressTag(Kind.REPOST, publicKey);
    assertNotEquals(one, three);

    IdentifierTag identifierTagA = new IdentifierTag("UUID-A");
    AddressTag four = new AddressTag(kind, publicKey, identifierTagA);
    assertNotEquals(one, four);

    AddressTag five = new AddressTag(kind, publicKey, identifierTagA);
    assertEquals(four, five);

    Relay relayX = new Relay("ws://localhost:8080");
    AddressTag six = new AddressTag(kind, publicKey, identifierTagA, relayX);
//    TODO: revisit AddressTag equals for relay inclusion/superfluity
//    assertNotEquals(four, six);

    Relay relayY = new Relay("ws://localhost:8080");
    AddressTag seven = new AddressTag(kind, publicKey, identifierTagA, relayY);
    assertEquals(six, seven);

    Relay relayZ = new Relay("ws://localhost:8081");
    assertNotEquals(relayY, relayZ);

    AddressTag eight = new AddressTag(kind, publicKey, identifierTagA, relayZ);
//    TODO: revisit AddressTag equals for relay inclusion/superfluity    
//    assertNotEquals(seven, eight);

    AddressTagFilter atOne = new AddressTagFilter(one);
    AddressTagFilter atTwo = new AddressTagFilter(two);
    assertEquals(atOne, atTwo);

    AddressTagFilter atThree = new AddressTagFilter(three);
    assertNotEquals(atOne, atThree);

    AddressTagFilter atFour = new AddressTagFilter(four);
    AddressTagFilter atFive = new AddressTagFilter(five);
    assertEquals(atFour, atFive);

    AddressTagFilter atSix = new AddressTagFilter(six);
    AddressTagFilter atSeven = new AddressTagFilter(seven);
    assertEquals(atSix, atSeven);

    AddressTagFilter atEight = new AddressTagFilter(eight);
//    TODO: revisit AddressTag equals for relay inclusion/superfluity
//    assertNotEquals(atSix, atEight);
  }

  private static void anyFieldNameMatch(List<Field> fields, Predicate<Field> predicate) {
    assertTrue(fields.stream().anyMatch(predicate));
  }

  //        TODO: below needs failable stream  
  private static void anyFieldValueMatch(List<Field> fields, AddressTag addressTag, Predicate<String> predicate) {
    assertTrue(fields.stream().flatMap(field -> {
      try {
        return addressTag.getFieldValue(field).stream();
      } catch (NoSuchFieldException | IllegalAccessException e) {
        throw new RuntimeException(e);
      }
    }).anyMatch(predicate));
  }
}
