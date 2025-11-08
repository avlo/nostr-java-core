package com.prosilion.nostr;

import com.prosilion.nostr.filter.tag.ExternalIdentityTagFilter;
import com.prosilion.nostr.tag.ExternalIdentityTag;
import java.lang.reflect.Field;
import java.util.List;
import java.util.function.Predicate;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ExternalIdentityTagTest {
  public final String platform = "afterimage";
  public final String identity = "reputation";
  public final String proof = "666";

  @Test
  void getSupportedFields() {
    ExternalIdentityTag externalIdentityTag = new ExternalIdentityTag(platform, identity, proof);

    List<Field> fields = externalIdentityTag.getSupportedFields();
    anyFieldNameMatch(fields, field -> field.getName().equals("platform"));
    anyFieldNameMatch(fields, field -> field.getName().equals("identity"));
    anyFieldNameMatch(fields, field -> field.getName().equals("proof"));

    anyFieldValueMatch(fields, externalIdentityTag, fieldValue -> fieldValue.equals(platform));
    anyFieldValueMatch(fields, externalIdentityTag, fieldValue -> fieldValue.equals(identity));
    anyFieldValueMatch(fields, externalIdentityTag, fieldValue -> fieldValue.equals(proof));

    assertFalse(fields.stream().anyMatch(field -> field.getName().equals("idEventXXX")));
//        TODO: below needs failable stream
    assertFalse(
        fields.stream().flatMap(field ->
            {
              try {
                return externalIdentityTag.getFieldValue(field).stream();
              } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException(e);
              }
            })
            .anyMatch(fieldValue ->
                fieldValue.equals(platform + "x")));

    assertFalse(
        fields.stream().flatMap(field ->
            {
              try {
                return externalIdentityTag.getFieldValue(field).stream();
              } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException(e);
              }
            })
            .anyMatch(fieldValue ->
                fieldValue.equals(identity + "x")));

    assertFalse(
        fields.stream().flatMap(field ->
            {
              try {
                return externalIdentityTag.getFieldValue(field).stream();
              } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException(e);
              }
            })
            .anyMatch(fieldValue ->
                fieldValue.equals(proof + "x")));
  }

  @Test
  void equalityNonEqualityTests() {
    ExternalIdentityTag one = new ExternalIdentityTag(platform, identity, proof);
    ExternalIdentityTag two = new ExternalIdentityTag(platform, identity, proof);
    assertEquals(one, two);

    ExternalIdentityTag three = new ExternalIdentityTag(platform + "x", identity, proof);
    assertNotEquals(one, three);

    ExternalIdentityTag four = new ExternalIdentityTag(platform, identity + "x", proof);
    assertNotEquals(one, four);

    ExternalIdentityTag five = new ExternalIdentityTag(platform, identity, proof + "x");
    assertNotEquals(one, five);

    ExternalIdentityTagFilter atOne = new ExternalIdentityTagFilter(one);
    ExternalIdentityTagFilter atTwo = new ExternalIdentityTagFilter(two);
    assertEquals(atOne, atTwo);

    ExternalIdentityTagFilter atThree = new ExternalIdentityTagFilter(three);
    assertNotEquals(atOne, atThree);
  }

  private static void anyFieldNameMatch(List<Field> fields, Predicate<Field> predicate) {
    assertTrue(fields.stream().anyMatch(predicate));
  }

  //        TODO: below needs failable stream  
  private static void anyFieldValueMatch(List<Field> fields, ExternalIdentityTag externalIdentityTag, Predicate<String> predicate) {
    assertTrue(fields.stream().flatMap(field -> {
      try {
        return externalIdentityTag.getFieldValue(field).stream();
      } catch (NoSuchFieldException | IllegalAccessException e) {
        throw new RuntimeException(e);
      }
    }).anyMatch(predicate));
  }
}
