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
  public final static String PLATFORM = ExternalIdentityTagTest.class.getPackageName();
  public final static String IDENTITY = ExternalIdentityTagTest.class.getSimpleName();
  public final static String PROOF = String.valueOf(ExternalIdentityTagTest.class.hashCode());

  @Test
  void getSupportedFields() {
    ExternalIdentityTag externalIdentityTag = new ExternalIdentityTag(PLATFORM, IDENTITY, PROOF);

    List<Field> fields = externalIdentityTag.getSupportedFields();
    anyFieldNameMatch(fields, field -> field.getName().equals("platform"));
    anyFieldNameMatch(fields, field -> field.getName().equals("identity"));
    anyFieldNameMatch(fields, field -> field.getName().equals("proof"));

    anyFieldValueMatch(fields, externalIdentityTag, fieldValue -> fieldValue.equals(PLATFORM));
    anyFieldValueMatch(fields, externalIdentityTag, fieldValue -> fieldValue.equals(IDENTITY));
    anyFieldValueMatch(fields, externalIdentityTag, fieldValue -> fieldValue.equals(PROOF));

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
                fieldValue.equals("platformx")));

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
                fieldValue.equals("identityx")));

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
                fieldValue.equals("proofx")));
  }

  @Test
  void equalityNonEqualityTests() {
    ExternalIdentityTag one = new ExternalIdentityTag(PLATFORM, IDENTITY, PROOF);
    ExternalIdentityTag two = new ExternalIdentityTag(PLATFORM, IDENTITY, PROOF);
    assertEquals(one, two);

    ExternalIdentityTag three = new ExternalIdentityTag(PLATFORM + "x", IDENTITY, PROOF);
    assertNotEquals(one, three);

    ExternalIdentityTag four = new ExternalIdentityTag(PLATFORM, IDENTITY + "x", PROOF);
    assertNotEquals(one, four);

    ExternalIdentityTag five = new ExternalIdentityTag(PLATFORM, IDENTITY, PROOF + "x");
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
