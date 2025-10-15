package com.prosilion.nostr;

import com.prosilion.nostr.enums.Kind;
import com.prosilion.nostr.filter.tag.ExternalIdentityTagFilter;
import com.prosilion.nostr.tag.ExternalIdentityTag;
import com.prosilion.nostr.tag.IdentifierTag;
import java.lang.reflect.Field;
import java.util.List;
import java.util.function.Predicate;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ExternalIdentityTagTest {
  Kind kind = Kind.BADGE_AWARD_EVENT;
  IdentifierTag identifierTag = new IdentifierTag("UNIT_UPVOTE");
  String formula = "+1";

  @Test
  void getSupportedFields() {
    ExternalIdentityTag externalIdentityTag = new ExternalIdentityTag(kind, identifierTag, formula);

    List<Field> fields = externalIdentityTag.getSupportedFields();
    anyFieldNameMatch(fields, field -> field.getName().equals("kind"));
    anyFieldNameMatch(fields, field -> field.getName().equals("identifierTag"));
    anyFieldNameMatch(fields, field -> field.getName().equals("formula"));

    anyFieldValueMatch(fields, externalIdentityTag, fieldValue -> fieldValue.equals(kind.toString()));
    anyFieldValueMatch(fields, externalIdentityTag, fieldValue -> fieldValue.equals(identifierTag.toString()));
    anyFieldValueMatch(fields, externalIdentityTag, fieldValue -> fieldValue.equals(formula));

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
                fieldValue.equals(identifierTag.toString() + "x")));
  }

  @Test
  void equalityNonEqualityTests() {
    ExternalIdentityTag one = new ExternalIdentityTag(kind, identifierTag, formula);
    ExternalIdentityTag two = new ExternalIdentityTag(kind, identifierTag, formula);
    assertEquals(one, two);

    ExternalIdentityTag three = new ExternalIdentityTag(Kind.REPOST, identifierTag, formula);
    assertNotEquals(one, three);

    ExternalIdentityTag four = new ExternalIdentityTag(kind, new IdentifierTag("UUID-A"), formula);
    assertNotEquals(one, four);

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
