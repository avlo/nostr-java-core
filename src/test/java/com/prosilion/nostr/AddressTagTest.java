package com.prosilion.nostr;

import com.prosilion.nostr.enums.Kind;
import com.prosilion.nostr.event.internal.Relay;
import com.prosilion.nostr.filter.tag.AddressTagFilter;
import com.prosilion.nostr.tag.AddressTag;
import com.prosilion.nostr.tag.BaseTag;
import com.prosilion.nostr.tag.IdentifierTag;
import com.prosilion.nostr.user.PublicKey;
import com.prosilion.nostr.util.Util;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
class AddressTagTest {
  Kind kind = Kind.TEXT_NOTE;
  String author = "f1b419a95cb0233a11d431423b41a42734e7165fcab16081cd08ef1c90e0be75";
  PublicKey publicKey = new PublicKey(author);
  IdentifierTag identifierTag = new IdentifierTag("UUID-1");
  Relay relay = new Relay("ws://localhost:8080");

  @Test
  void getSupportedFields() {
    AddressTag addressTag = new AddressTag(
       kind, publicKey, identifierTag, relay);

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

  @Test
  void utilPrettyPrintTest() {
    AddressTag addressTagSansRelay = new AddressTag(kind, publicKey, identifierTag);
    String actual = Util.prettyPrintAddressTags(addressTagSansRelay);
    assertEquals(expectedAddressTagPrettyPrint(addressTagSansRelay), actual);
    assertEquals(addressTagSansRelay.toStringPrettyPrint(), actual);

    AddressTag addressTagSansRelaySansIdentifierTag = new AddressTag(kind, publicKey);
    String expectedJoinedFirstPair = String.join(",\n",
       expectedAddressTagPrettyPrint(addressTagSansRelay),
       expectedAddressTagPrettyPrint(addressTagSansRelaySansIdentifierTag));

    assertEquals(
       expectedJoinedFirstPair,
       Util.prettyPrintAddressTags(List.of(addressTagSansRelay, addressTagSansRelaySansIdentifierTag)));
    assertEquals(
       expectedJoinedFirstPair,
       Stream.of(addressTagSansRelay, addressTagSansRelaySansIdentifierTag).map(AddressTag::toStringPrettyPrint).collect(Collectors.joining(",\n")));

    AddressTag properAddressTag = new AddressTag(kind, publicKey, identifierTag, relay);
    assertEquals(expectedAddressTagPrettyPrint(properAddressTag), Util.prettyPrintAddressTags(properAddressTag));
    String expectedJoinedSecondPair = String.join(",\n",
       expectedJoinedFirstPair,
       expectedAddressTagPrettyPrint(properAddressTag));

    assertEquals(
       expectedJoinedSecondPair,
       Util.prettyPrintAddressTags(List.of(addressTagSansRelay, addressTagSansRelaySansIdentifierTag, properAddressTag)));
    assertEquals(
       expectedJoinedSecondPair,
       Stream.of(addressTagSansRelay, addressTagSansRelaySansIdentifierTag, properAddressTag).map(AddressTag::toStringPrettyPrint).collect(Collectors.joining(",\n")));
  }

  @Test
  void testRelayVariantsEquality() {
    AddressTag expected = new AddressTag(
       kind,
       publicKey,
       identifierTag,
       relay);

    AddressTag actualContainsRelay = new AddressTag(
       expected.getKind(),
       expected.getPublicKey(),
       expected.getIdentifierTag(),
       expected.getRelay());
    assertEquals(expected, actualContainsRelay);

    AddressTag actualNoContainsRelay = new AddressTag(
       expected.getKind(),
       expected.getPublicKey(),
       expected.getIdentifierTag());
    assertEquals(expected, actualNoContainsRelay);

    AddressTag actualRelayTagIsNull = new AddressTag(
       expected.getKind(),
       expected.getPublicKey(),
       expected.getIdentifierTag(),
       null);
    assertEquals(expected, actualRelayTagIsNull);

    AddressTag actualRelayTagDifferentValue = new AddressTag(
       expected.getKind(),
       expected.getPublicKey(),
       expected.getIdentifierTag(),
       new Relay("ws://localhost-nomatch:5555"));
    assertEquals(expected, actualRelayTagDifferentValue);

    assertTrue(Set.of(actualContainsRelay).contains(expected));
    assertTrue(Set.of(actualNoContainsRelay).contains(expected));
    assertTrue(Set.of(actualRelayTagIsNull).contains(expected));
    assertTrue(Set.of(actualRelayTagDifferentValue).contains(expected));

    AddressTag expectedWithoutRelayTag = new AddressTag(
       kind,
       publicKey,
       identifierTag);

    assertTrue(Set.of(actualContainsRelay).contains(expectedWithoutRelayTag));
    assertTrue(Set.of(actualNoContainsRelay).contains(expectedWithoutRelayTag));
    assertTrue(Set.of(actualRelayTagIsNull).contains(expectedWithoutRelayTag));
    assertTrue(Set.of(actualRelayTagDifferentValue).contains(expectedWithoutRelayTag));

    List<AddressTag> actualListContainsRelay = List.of(actualContainsRelay);
    List<? extends BaseTag> list1 = actualListContainsRelay.stream().map(expectedWithoutRelayTag.getClass()::cast).toList();
    Set<? extends BaseTag> collect1 = list1.stream().collect(Collectors.toSet());
    assertFalse((collect1.contains(expectedWithoutRelayTag)));
    assertTrue(collect1.stream().anyMatch(expectedWithoutRelayTag::equals));

    List<AddressTag> actualListNoContainsRelay = List.of(actualNoContainsRelay);
    List<? extends BaseTag> list2 = actualListNoContainsRelay.stream().map(expectedWithoutRelayTag.getClass()::cast).toList();
    Set<? extends BaseTag> collect2 = list2.stream().collect(Collectors.toSet());
    assertTrue(collect2.contains(expectedWithoutRelayTag));
    assertTrue(collect2.stream().anyMatch(expectedWithoutRelayTag::equals));

    List<AddressTag> actualListRelayNull = List.of(actualRelayTagIsNull);
    List<? extends BaseTag> list3 = actualListRelayNull.stream().map(expectedWithoutRelayTag.getClass()::cast).toList();
    Set<? extends BaseTag> collect3 = list3.stream().collect(Collectors.toSet());
    assertTrue(collect3.contains(expectedWithoutRelayTag));
    assertTrue(collect3.stream().anyMatch(expectedWithoutRelayTag::equals));

    List<AddressTag> actualListRelayDifferentValue = List.of(actualRelayTagDifferentValue);
    List<? extends BaseTag> list4 = actualListRelayDifferentValue.stream().map(expectedWithoutRelayTag.getClass()::cast).toList();
    Set<? extends BaseTag> collect4 = list4.stream().collect(Collectors.toSet());
    assertFalse(collect4.contains(expectedWithoutRelayTag));
    assertTrue(collect4.stream().anyMatch(expectedWithoutRelayTag::equals));
  }

  @Test
  void getRelayReturnsNostrException() {
    AddressTag addressTagSansRelay = new AddressTag(kind, publicKey, identifierTag);
    assertThrows(NostrException.class, addressTagSansRelay::requireRelay);
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

  private String expectedAddressTagPrettyPrint(AddressTag addressTag) {
    return "AddressTag[\n" +
       "  kind=" + addressTag.getKind().getValue() + "\n" +
       "  publicKey=" + addressTag.getPublicKey().toString() + "\n" +
       "  identifierTag=" + addressTag.findIdentifierTag().map(identifierTag ->
       "IdentifierTag[uuid=".concat(identifierTag.getUuid()).concat("]")).orElse("null") + "\n" +
       "  relay=" + addressTag.findRelay().map(relay ->
       "Relay[url=".concat(relay.getUrl()).concat("]")).orElse("null") + "]";
  }
}
