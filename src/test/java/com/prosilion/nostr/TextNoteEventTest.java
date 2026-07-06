package com.prosilion.nostr;

import com.prosilion.nostr.event.TextNoteEvent;
import com.prosilion.nostr.tag.IdentifierTag;
import com.prosilion.nostr.user.Identity;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TextNoteEventTest {
  private static final String FORMULA_UNIT_UPVOTE = "FORMULA_UNIT_UPVOTE";
  private static final String FORMULA_UNIT_DOWNVOTE = "FORMULA_UNIT_DOWNVOTE";
  private static final IdentifierTag formulaUnitUpvote = new IdentifierTag(FORMULA_UNIT_UPVOTE);
  private static final IdentifierTag formulaUnitDownvote = new IdentifierTag(FORMULA_UNIT_DOWNVOTE);
  static final Identity aImgIdentity =
     // below produces e04e1c1c30df6058433f61681644fd24914f2e02e420496086c61f53eb504c04
     Identity.create("fa11661b5f43c8f18f11861b4d553c47337dac9e351083b27320e311b7b324ac");

  private final TextNoteEvent referenceTextNoteEvent;

  public TextNoteEventTest() {
    this.referenceTextNoteEvent = new TextNoteEvent(
       aImgIdentity,
       List.of(formulaUnitUpvote, formulaUnitDownvote), "referenceContent");
    System.out.println(referenceTextNoteEvent);
  }

  @Test
  final void testGenericEventRecordCtorEquality() {
    TextNoteEvent fromGer = new TextNoteEvent(referenceTextNoteEvent.asGenericEventRecord());
    assertEquals(referenceTextNoteEvent, fromGer);
  }

  @Test
  final void testTagsEquality() {
    TextNoteEvent reversedOrder = new TextNoteEvent(
       aImgIdentity,
       List.of(formulaUnitDownvote, formulaUnitUpvote), "referenceContent");

    assertTrue(referenceTextNoteEvent.getTags().containsAll(reversedOrder.getTags()));
  }

  public static String generateRandomHex64String() {
    return UUID.randomUUID().toString().concat(UUID.randomUUID().toString()).replaceAll("[^A-Za-z0-9]", "");
  }
}
